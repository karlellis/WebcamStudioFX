/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package webcamstudiofx.mixers;

import static java.awt.AlphaComposite.SRC_OVER;
import static java.awt.AlphaComposite.getInstance;
import java.awt.Graphics2D;
import java.awt.Image;
import static java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION;
import static java.awt.RenderingHints.KEY_COLOR_RENDERING;
import static java.awt.RenderingHints.KEY_DITHERING;
import static java.awt.RenderingHints.KEY_FRACTIONALMETRICS;
import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.KEY_RENDERING;
import static java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED;
import static java.awt.RenderingHints.VALUE_COLOR_RENDER_SPEED;
import static java.awt.RenderingHints.VALUE_DITHER_DISABLE;
import static java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR;
import static java.awt.RenderingHints.VALUE_RENDER_SPEED;
import java.awt.image.BufferedImage;
import static java.lang.Short.MAX_VALUE;
import static java.lang.Short.MIN_VALUE;
import static java.lang.System.currentTimeMillis;
import static java.nio.ByteBuffer.wrap;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import static webcamstudiofx.MainPanelFXController.masterVolume;
import webcamstudiofx.streams.Stream;
import static webcamstudiofx.util.Tools.sleep;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static webcamstudiofx.mixers.MasterMixer.getInstance;

/**
 *
 * @author patrick (modified by karl and George)
 */
public class MasterFrameBuilder implements Runnable {

    private static final ArrayList<Stream> streams = new ArrayList<>();// add private
    private static int fps = 0;

    public static synchronized void register(Stream s) {
        if (!streams.contains(s)) {
            streams.add(s);
//            System.out.println("Register Master Stream Size: "+streams.size());
        }
    }

    public static synchronized void unregister(Stream s) {
        streams.remove(s);
//        System.out.println("UnRegister Master Stream Size: "+streams.size());
    }
    private Image imageF;
    private int imageX, imageY, imageW, imageH;
    private boolean stopMe = false;
    private long mark = currentTimeMillis();
    private FrameBuffer frameBuffer = null;
    private final TreeMap<Integer, Frame> orderedFrames = new TreeMap<>();

    public MasterFrameBuilder(int w, int h, int r) {
        frameBuffer = new FrameBuffer(w, h, r);
    }

    public void stop() {
        stopMe = true;
    }

    private void mixImages(Collection<Frame> frames, Frame targetFrame) {
        frames.stream().forEach((f) -> {
            orderedFrames.put(f.getZOrder(), f);
        });

        BufferedImage image = targetFrame.getImage();
        if (image != null) {
            Graphics2D g = image.createGraphics();
            g.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_SPEED);
            g.setRenderingHint(KEY_RENDERING, VALUE_RENDER_SPEED);
            g.setRenderingHint(KEY_FRACTIONALMETRICS, VALUE_FRACTIONALMETRICS_OFF);
            g.setRenderingHint(KEY_DITHERING, VALUE_DITHER_DISABLE);
            g.setRenderingHint(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_SPEED);
            g.clearRect(0, 0, image.getWidth(), image.getHeight());
            orderedFrames.values().stream().map((f) -> {
                imageF = f.getImage();
                return f;
            }).map((f) -> {
                imageX = f.getX();
                return f;
            }).map((f) -> {
                imageY = f.getY();
                return f;
            }).map((f) -> {
                imageW = f.getWidth();
                return f;
            }).map((f) -> {
                imageH = f.getHeight();
                return f;
            }).map((f) -> {
                g.setComposite(getInstance(SRC_OVER, f.getOpacity() / 100F));
                return f;
            }).forEach((_item) -> {
                g.drawImage(imageF, imageX, imageY, imageW, imageH, null);
            });
            g.dispose();

        }
        orderedFrames.clear();
    }

    private void mixAudio(Collection<Frame> frames, Frame targetFrame) {
        byte[] audioData = targetFrame.getAudioData();
        ShortBuffer outputBuffer = wrap(audioData).asShortBuffer();
        for (int i = 0; i < audioData.length; i++) {
            audioData[i] = 0;
        }
        frames.stream().forEach((f) -> {
            byte[] data = f.getAudioData();
            if (data != null) {
                ShortBuffer buffer = wrap(data).asShortBuffer();
                outputBuffer.rewind();
                while (buffer.hasRemaining()) {
                    //                    System.out.println("Volume="+f.getVolume());
                    float volume = f.getVolume() + masterVolume;
                    if (volume < 0) {
                        volume = 0;
                    }
                    float mix = buffer.get() * (volume);
                    outputBuffer.mark();
                    if (outputBuffer.position() < outputBuffer.limit()) { //25fps IOException
                        mix += outputBuffer.get();
                    }
                    outputBuffer.reset();
                    if (mix > MAX_VALUE) {
                        mix = MAX_VALUE;
                    } else if (mix < MIN_VALUE) {
                        mix = MIN_VALUE;
                    }
                    if (outputBuffer.position() < outputBuffer.limit()) { //25fps IOException
                        outputBuffer.put((short) mix);
                    }
                }
                f.setAudio(null);
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() throws NullPointerException {
        stopMe = false;
        ArrayList<Frame> frames = new ArrayList<>();
        mark = currentTimeMillis();
        int r = getInstance().getRate();
        long frameDelay = 1000 / r;
        long timeCode = currentTimeMillis();

        while (!stopMe) {
            timeCode += frameDelay;
            Frame targetFrame = frameBuffer.getFrameToUpdate();
            frames.clear();
            boolean threadedCaptureMode = true;
            ExecutorService pool = newCachedThreadPool();

            if (threadedCaptureMode) {
                ArrayList<Future<Frame>> resultsT = new ArrayList<>();
                try {
                    resultsT = ((ArrayList) pool.invokeAll(streams, 5, SECONDS));
                } catch (InterruptedException ex) {
                    getLogger(MasterFrameBuilder.class.getName()).log(SEVERE, null, ex);
                }
                ArrayList<Future<Frame>> results = resultsT;
                Frame f;
                for (Future stream : results) {
                    try {
                        f = (Frame) stream.get();
                        if (f != null) {
                            frames.add(f);
                        }
                    } catch (CancellationException | InterruptedException | ExecutionException ex) {
//                        Logger.getLogger(MasterFrameBuilder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                for (int i = 0; i < streams.size(); i++) {
                    Frame f;
                    try {
                        Stream s = streams.get(i);
                        f = s.call();
                        if (f != null) {
                            frames.add(f);
                        }
                    } catch (Exception e) {
                    }
                }
            }
            long now = currentTimeMillis();
            long sleepTime = (timeCode - now);
            fps++;
            mixAudio(frames, targetFrame);
            mixImages(frames, targetFrame);
            targetFrame = null;
            frameBuffer.doneUpdate();
            getInstance().setCurrentFrame(frameBuffer.pop());

            float delta = (now - mark);
            if (delta >= 1000) {
                mark = now;
                getInstance().setFPS(fps / (delta / 1000F));
                fps = 0;
            }
            if (sleepTime > 0) {
                sleep(sleepTime);
            }
        }
    }
}
