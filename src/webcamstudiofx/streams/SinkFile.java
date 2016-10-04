/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.streams;

import java.awt.image.BufferedImage;
import java.io.File;
import static webcamstudiofx.TruckliststudioUIController.os;
import static webcamstudiofx.TruckliststudioUIController.outFMEbe;
import static webcamstudiofx.TruckliststudioUIController.x64;
import webcamstudiofx.externals.ProcessRenderer;
import webcamstudiofx.mixers.Frame;
import webcamstudiofx.mixers.MasterMixer;
import webcamstudiofx.util.Tools;

/**
 *
 * @author patrick (modified by karl)
 */
public class SinkFile extends Stream {

    private ProcessRenderer capture = null;

    public SinkFile(File f) {
        file = f;
        name = f.getName();
        switch (outFMEbe) {
            case 0:
                this.setComm("FF");
                break;
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
        rate = MasterMixer.getInstance().getRate();
        captureWidth = MasterMixer.getInstance().getWidth();
        captureHeight = MasterMixer.getInstance().getHeight();
        String plugin = "file";
        if (os == Tools.OS.WINDOWS) {
            if (!x64) {
                plugin = "file86";
            }
        }
        capture = new ProcessRenderer(this, ProcessRenderer.ACTION.OUTPUT, plugin, comm);
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
        return true;
    }

    @Override
    public void readNext() {

    }

    @Override
    public void play() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
