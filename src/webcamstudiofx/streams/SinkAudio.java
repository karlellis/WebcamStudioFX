/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.streams;

import java.awt.image.BufferedImage;
import static webcamstudiofx.TruckliststudioUIController.os;
import static webcamstudiofx.TruckliststudioUIController.outFMEbe;
import static webcamstudiofx.TruckliststudioUIController.x64;
import webcamstudiofx.externals.ProcessRenderer;
import webcamstudiofx.mixers.Frame;
import webcamstudiofx.util.Tools;

/**
 *
 * @author karl
 */
public class SinkAudio extends Stream {

    private ProcessRenderer capture = null;

    public SinkAudio() {
        name = "AudioOut";
        this.setOnlyAudio(true);
//        System.out.println("SinkAudio outFMEbe= "+outFMEbe);
        switch (outFMEbe) {
            case 0:
                this.setComm("FF");
                break;
//        System.out.println("SinkAudio BE= "+this.getComm());
            case 1:
                this.setComm("AV");
                break;
            case 2:
                this.setComm("GS");
                break;
            default:
                break;
        }
    }

    @Override
    public void read() {
        String plugin = "spkAudioOut";
        if (os == Tools.OS.WINDOWS) {
            if (!x64) {
                plugin = "spkAudioOut86";
            }
        }
        capture = new ProcessRenderer(this, ProcessRenderer.ACTION.OUTPUT, plugin, comm); //"spkAudioOut"
        capture.writeCom();
    }

    @Override
    public void pause() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stop() {
        if (capture != null) {
            capture.stop();
            capture = null;
        }
        if (this.getBackFF()) {
            this.setComm("FF");
        }
    }

    @Override
    public boolean needSeek() {
        return needSeekCTRL = false;
    }

    @Override
    public boolean isPlaying() {
        if (capture != null) {
            return !capture.isStopped();
        } else {
            return false;
        }
    }

    @Override
    public BufferedImage getPreview() {
        return null;
    }

    @Override
    public Frame getFrame() {
        return null;
    }

    @Override
    public boolean hasAudio() {
        return true;
    }

    @Override
    public boolean hasVideo() {
        return false;
    }

    @Override
    public void readNext() {

    }

    @Override
    public void play() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
