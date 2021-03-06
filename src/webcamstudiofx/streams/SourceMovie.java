/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.streams;

import java.awt.image.BufferedImage;
import java.io.File;
import webcamstudiofx.externals.ProcessRenderer;
import webcamstudiofx.mixers.Frame;
import webcamstudiofx.mixers.MasterFrameBuilder;
import webcamstudiofx.mixers.MasterMixer;
import webcamstudiofx.mixers.PreviewFrameBuilder;
import webcamstudiofx.sources.effects.Effect;

/**
 *
 * @author patrick (modified by karl)
 */
public class SourceMovie extends Stream {

    ProcessRenderer capture = null;
    BufferedImage lastPreview = null;
    boolean isPlaying = false;

    public SourceMovie(File movie) {
        super();
        rate = MasterMixer.getInstance().getRate();
        file = movie;
        name = movie.getName();
    }

    @Override
    public void read() {
        isPlaying = true;
        lastPreview = new BufferedImage(captureWidth, captureHeight, BufferedImage.TYPE_INT_ARGB);
        if (getPreView()) {
            PreviewFrameBuilder.register(this);
        } else {
            MasterFrameBuilder.register(this);
        }
        capture = new ProcessRenderer(this, ProcessRenderer.ACTION.CAPTURE, "movie", comm);
        capture.read();
    }

    @Override
    public void pause() {
        isPaused = true;
        capture.pause();
    }

    @Override
    public void stop() {
        if (loop) {
            if (capture != null) {
                capture.stop();
                capture = null;
            }
            if (this.getBackFF()) {
                this.setComm("FF");
            }
            this.read();
        } else {
            for (int fx = 0; fx < this.getEffects().size(); fx++) {
                Effect fxT = this.getEffects().get(fx);
                if (fxT.getName().endsWith("Stretch") || fxT.getName().endsWith("Crop")) {
                    // do nothing.
                } else {
                    fxT.resetFX();
                }
            }
            isPlaying = false;
            if (getPreView()) {
                PreviewFrameBuilder.unregister(this);
            } else {
                MasterFrameBuilder.unregister(this);
            }
            if (capture != null) {
                capture.stop();
                capture = null;
            }
            if (this.getBackFF()) {
                this.setComm("FF");
            }
        }
    }

    @Override
    public boolean needSeek() {
        return needSeekCTRL = true;
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void setIsPlaying(boolean setIsPlaying) {
        isPlaying = setIsPlaying;
    }

    @Override
    public boolean hasFakeVideo() {
        return true;
    }

    @Override
    public boolean hasFakeAudio() {
        return true;
    }

    @Override
    public BufferedImage getPreview() {
        return lastPreview;
    }

    @Override
    public Frame getFrame() {
        return nextFrame;
    }

    @Override
    public void readNext() {
        Frame f = null;
        if (capture != null) {
            f = capture.getFrame();
            if (f != null) {
                BufferedImage img = f.getImage();
                applyEffects(img);
            }
            if (f != null) {
                setAudioLevel(f);
                lastPreview.getGraphics().drawImage(f.getImage(), 0, 0, null);
            }
        }
        nextFrame = f;
    }

    @Override
    public void play() {
        isPaused = false;
        capture.play();
    }

}
