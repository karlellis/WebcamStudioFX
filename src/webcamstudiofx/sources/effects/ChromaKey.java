/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.sources.effects;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author pballeux (modified by karl)
 */
public class ChromaKey extends Effect {

    private int rTolerance = 0;
    private int gTolerance = 0;
    private int bTolerance = 0;
    private String colorHexString = "0xFFFFFF";
    private Color color = new Color(Color.decode(colorHexString).getRGB());

    @Override
    public void applyEffect(BufferedImage img) {
        color = new Color(Color.decode(colorHexString).getRGB());
        int[] data = ((java.awt.image.DataBufferInt) img.getRaster().getDataBuffer()).getData();
        int r, g, b, c;
        for (int i = 0; i < data.length; i++) {
            c = data[i];
            r = (c >> 16) & 0xff;
            g = (c >> 8) & 0xff;
            b = c & 0xff;
            int rRatio = Math.abs(color.getRed() - r) * 100 / 255;
            int gRatio = Math.abs(color.getGreen() - g) * 100 / 255;
            int bRatio = Math.abs(color.getBlue() - b) * 100 / 255;
            if (rTolerance >= rRatio && gTolerance >= gRatio && bTolerance >= bRatio) {
                data[i] &= 0x00FFFFFF;
            }
        }
    }

    public String getColor() {
        return colorHexString;
    }

    public void setColor(String col) {
        colorHexString = col;
    }

    @Override
    public AnchorPane getControl() throws IOException {
        effectFX = this;
        AnchorPane ChromaKeyControlFX = (AnchorPane) FXMLLoader.load(getClass().getResource("/webcamstudiofx/sources/effects/controls/ChromaKeyControlFX.fxml"));
        return ChromaKeyControlFX;
    }

    /**
     * @return the hTolerance
     */
    public int getrTolerance() {
        return rTolerance;
    }

    /**
     * @param rTolerance
     */
    public void setrTolerance(int rTolerance) {
        this.rTolerance = rTolerance;
    }

    /**
     * @return the sTolerance
     */
    public int getgTolerance() {
        return gTolerance;
    }

    /**
     * @param gTolerance
     */
    public void setgTolerance(int gTolerance) {
        this.gTolerance = gTolerance;
    }

    /**
     * @return the bTolerance
     */
    public int getbTolerance() {
        return bTolerance;
    }

    /**
     * @param bTolerance the bTolerance to set
     */
    public void setbTolerance(int bTolerance) {
        this.bTolerance = bTolerance;
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
