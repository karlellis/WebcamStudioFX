/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.streams;

import java.awt.image.BufferedImage;
import java.io.File;
import webcamstudiofx.exporter.vloopback.V4L2Loopback;
import webcamstudiofx.exporter.vloopback.VideoOutput;
import static webcamstudiofx.exporter.vloopback.VideoOutput.RGB24;
import static webcamstudiofx.exporter.vloopback.VideoOutput.UYVY;
import static webcamstudiofx.exporter.vloopback.VideoOutput.BGR24;
import webcamstudiofx.mixers.Frame;
import webcamstudiofx.mixers.MasterMixer;

/**
 *
 * @author patrick
 */
public class SinkLinuxDevice extends Stream implements MasterMixer.SinkListener {

    VideoOutput device;
    boolean stop = false;
    int pixelFormat;

    public SinkLinuxDevice(File f, String name, int pixelFormat) {
        file = f;
        device = new V4L2Loopback(null);
        this.name = name;
        if (pixelFormat > 0)
        {
            this.pixelFormat = pixelFormat;
        }
        else
        {
            this.pixelFormat = UYVY;
        }
    }

    @Override
    public void read() {
        stop = false;
        rate = MasterMixer.getInstance().getRate();
        device.open(file.getAbsolutePath(), width, height, pixelFormat);
        MasterMixer.getInstance().register(this);
    }

    @Override
    public void pause() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stop() {
        stop = true;
        if (device != null) {
            device.close();
            device = null;
        }
        MasterMixer.getInstance().unregister(this);
    }
    @Override
    public boolean needSeek() {
            return needSeekCTRL=false;
    }

    @Override
    public boolean isPlaying() {
        return !stop;
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
        return false;
    }

    @Override
    public boolean hasVideo() {
        return true;
    }

    @Override
    public void newFrame(Frame frame) {
            if (frame != null) {
                BufferedImage image = frame.getImage();
                if (image != null) {
                    int[] imgData = ((java.awt.image.DataBufferInt) image.getRaster().getDataBuffer()).getData();
                    if (device != null) {
                        device.write(imgData);
                    }
                }
            }
        }

    @Override
    public void readNext() {

    }

    @Override
    public void play() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
