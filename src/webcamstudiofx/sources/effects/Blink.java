/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.sources.effects;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author pballeux (modified by karl)
 */
public class Blink extends Effect {

    long mark = System.currentTimeMillis();
    boolean blink = false;

    @Override
    public void applyEffect(BufferedImage img) {
        if (blink) {
            Graphics2D buffer = img.createGraphics();
            buffer.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            buffer.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_SPEED);
            buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF);
            buffer.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            buffer.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            buffer.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                    RenderingHints.VALUE_COLOR_RENDER_SPEED);
            buffer.setRenderingHint(RenderingHints.KEY_DITHERING,
                    RenderingHints.VALUE_DITHER_DISABLE);
            buffer.setBackground(new java.awt.Color(0, 0, 0, 0));
            buffer.clearRect(0, 0, img.getWidth(), img.getHeight());
            buffer.dispose();
        }
        if (System.currentTimeMillis() - mark > 1000) {
            blink = !blink;
            mark = System.currentTimeMillis();
        }

    }

    @Override
    public boolean needApply() {
        return needApply = true;
    }

    @Override
    public AnchorPane getControl() throws IOException {
        return null;
    }

    @Override
    public void resetFX() {
        // nothing here.
    }
}
