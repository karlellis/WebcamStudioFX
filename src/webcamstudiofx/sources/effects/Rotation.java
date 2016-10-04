/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.sources.effects;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.Rotate;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import static webcamstudiofx.sources.effects.Effect.effectFX;

/**
 *
 * @author karl
 */
public class Rotation extends Effect {

    private int rotation = 0;

    @Override
    public void applyEffect(BufferedImage img) {

        FastBitmap imageIn = new FastBitmap(img);
        Rotate.Algorithm algorithm = Rotate.Algorithm.BILINEAR;
        Rotate c = new Rotate(rotation, algorithm);
        c.applyInPlace(imageIn);
        BufferedImage temp = imageIn.toBufferedImage();

        Graphics2D buffer = img.createGraphics();
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
        buffer.setBackground(new Color(0, 0, 0, 0));
        buffer.clearRect(0, 0, img.getWidth(), img.getHeight());
        buffer.drawImage(temp, 0, 0, img.getWidth(), img.getHeight(), 0, 0, temp.getWidth(), temp.getHeight(), null);
        buffer.dispose();
    }

    @Override
    public boolean needApply() {
        return needApply = true;
    }

    public void setRotation(int value) {
        rotation = value;
    }

    public int getRotation() {
        return rotation;
    }

    @Override
    public AnchorPane getControl() throws IOException {
        effectFX = this;
        AnchorPane RotationControlFX = (AnchorPane) FXMLLoader.load(getClass().getResource("/webcamstudiofx/sources/effects/controls/RotationControlFX.fxml"));
        return RotationControlFX;
    }

    @Override
    public void resetFX() {
        // nothing here.
    }
}
