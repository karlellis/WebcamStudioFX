/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.streams;

import java.awt.image.BufferedImage;
import static webcamstudiofx.TruckliststudioUIController.os;
import static webcamstudiofx.TruckliststudioUIController.outFMEbe;
import static webcamstudiofx.TruckliststudioUIController.x64;
import webcamstudiofx.externals.FME;
import webcamstudiofx.externals.ProcessRenderer;
import webcamstudiofx.mixers.MasterMixer;
import webcamstudiofx.util.Tools;

/**
 *
 * @author patrick (modified by karl)
 */
public class SinkBroadcast extends Stream {

    private ProcessRenderer capture = null;
    private FME fme = null;
    private boolean isPlaying = false;
    private String standard = "STD";

    public SinkBroadcast(FME fme) {
        this.fme = fme;
        name = fme.getName();
        url = fme.getUrl() + "/" + fme.getStream();
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
    public String getName() {
        return name;
    }

    @Override
    public void read() {
        isPlaying = true;
        rate = MasterMixer.getInstance().getRate();
        captureWidth = MasterMixer.getInstance().getWidth();
        captureHeight = MasterMixer.getInstance().getHeight();
        if (!"".equals(this.fme.getMount())) {
            String plugin = "iceCast";
            String pluginHD = "iceCastHQ";
            if (os == Tools.OS.WINDOWS) {
                if (!x64) {
                    plugin = "iceCast86";
                    pluginHD = "iceCastHQ86";
                }
            }
            if (standard.equals("STD")) {
                capture = new ProcessRenderer(this, fme, plugin);
            } else {
                capture = new ProcessRenderer(this, fme, pluginHD);
            }
        } else {
            String plugin = "broadcast";
            String pluginHD = "broadcastHQ";
            if (os == Tools.OS.WINDOWS) {
                if (!x64) {
                    plugin = "broadcast86";
                    pluginHD = "broadcastHQ86";
                }
            }
            if (standard.equals("STD")) {
                capture = new ProcessRenderer(this, fme, plugin);
            } else {
                capture = new ProcessRenderer(this, fme, pluginHD);
            }
        }
        capture.writeCom();
    }

    @Override
    public void pause() {
        // nothing here.
    }

    @Override
    public void stop() {
        isPlaying = false;
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
        return isPlaying;
    }

    @Override
    public BufferedImage getPreview() {
        return null;
    }

    public void setStandard(String gStandard) {
        standard = gStandard;
    }

    public String getStandard() {
        return standard;
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
        // nothing here.
    }

    @Override
    public void play() {
        // nothing here.
    }
}
