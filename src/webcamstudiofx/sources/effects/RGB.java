/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.sources.effects;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import static webcamstudiofx.sources.effects.Effect.effectFX;

/**
 *
 * @author pballeux
 */
public class RGB extends Effect {

    private int rThreshold = 100;
    private int gThreshold = 100;
    private int bThreshold = 100;

    @Override
    public void applyEffect(BufferedImage img) {
        int[] data = ((java.awt.image.DataBufferInt) img.getRaster().getDataBuffer()).getData();
        int r, g, b, c, a;
        for (int i = 0; i < data.length; i++) {
            c = data[i];
            r = (c & 0x00FF0000) >> 16;
            g = (c & 0x0000FF00) >> 8;
            b = c & 0x000000FF;
            a = (c & 0xFF000000) >> 24;
            r = r * rThreshold / 100;
            g = g * gThreshold / 100;
            b = b * bThreshold / 100;
            if (r > 255) {
                r = 255;
            }
            if (b > 255) {
                b = 255;
            }
            if (g > 255) {
                g = 255;
            }
            data[i] = (a << 24) | (r << 16) | (g << 8) | b;
        }

    }

    public void setRThreshold(int t) {
        rThreshold = t;
    }

    public void setGThreshold(int t) {
        gThreshold = t;
    }

    public void setBThreshold(int t) {
        bThreshold = t;
    }

    public int getRThreshold() {
        return rThreshold;
    }

    public int getGThreshold() {
        return gThreshold;
    }

    public int getBThreshold() {
        return bThreshold;
    }

    @Override
    public AnchorPane getControl() throws IOException {
        effectFX = this;
        AnchorPane RGBControlFX = (AnchorPane) FXMLLoader.load(getClass().getResource("/webcamstudiofx/sources/effects/controls/RGBControlFX.fxml"));
        return RGBControlFX;
    }

    @Override
    public boolean needApply() {
        return needApply = true;
    }

    @Override
    public void resetFX() {
        // nothing here.
    }
}
