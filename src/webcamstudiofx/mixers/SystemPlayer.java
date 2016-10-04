/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.mixers;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import static webcamstudiofx.TruckliststudioUIController.audioFreq;
import webcamstudiofx.components.ViewerFXController;
import webcamstudiofx.util.Tools;

/**
 *
 * @author patrick (modified by karl)
 */
public class SystemPlayer implements Runnable {

    private static SystemPlayer instance = null;

    public static SystemPlayer getInstanceFX(ViewerFXController viewerFX) {
        if (instance == null) {
            instance = new SystemPlayer(viewerFX);
        }
        return instance;
    }

    boolean stopMe = false;
    public boolean stopMePub = stopMe;
    private SourceDataLine source;
    private ExecutorService executor = null;
    private FrameBuffer frames = null;
    private ViewerFXController viewerFX = null;

    private SystemPlayer(ViewerFXController viewerFX) {
        this.viewerFX = viewerFX;
    }

    public void addFrame(Frame frame) {
        BufferedImage fImage = frame.getImage();
        viewerFX.setImageFX(fImage);
        int lAL = MasterMixer.getInstance().getAudioLevelLeft();
        int lAR = MasterMixer.getInstance().getAudioLevelRight();
        viewerFX.setAudioLevelFX(lAL, lAR);
        if (source != null) {
            frames.push(frame);
        }
    }

    public void play() throws LineUnavailableException {
        frames = new FrameBuffer(MasterMixer.getInstance().getWidth(), MasterMixer.getInstance().getHeight(), MasterMixer.getInstance().getRate());
        AudioFormat format = new AudioFormat(audioFreq, 16, 2, true, true);
        source = javax.sound.sampled.AudioSystem.getSourceDataLine(format);
        source.open();
        source.start();
        executor = java.util.concurrent.Executors.newCachedThreadPool();
        executor.submit(this);
        executor.shutdown();
    }

    @Override
    public void run() {
        stopMe = false;
        frames.clear();
        while (!stopMe) {
            Frame frame = frames.pop();
            byte[] d = frame.getAudioData();
            if (d != null) {
                source.write(d, 0, d.length);
            }
        }
    }

    public void stop() {
        stopMe = true;
        if (frames != null) {
            Tools.sleep(30);
            frames.abort();
        }
        if (source != null) {
            Tools.sleep(30);
            source.stop();
            Tools.sleep(30);
            source.close();
            Tools.sleep(30);
            source = null;
        }
        Tools.sleep(20);
        executor = null;
    }

}
