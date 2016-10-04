/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.components;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class PreviewerFXController implements Initializable {

    private GraphicsContext gc;
    private WritableImage wr = null;
    private static final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private static final GraphicsDevice device = env.getDefaultScreenDevice();
    private static final GraphicsConfiguration config = device.getDefaultConfiguration();
    private static BufferedImage preImg = config.createCompatibleImage(320, 240, BufferedImage.TYPE_INT_ARGB);
    private static int audioLeft = 0;
    private static int audioRight = 0;
    public Paint audioFill = javafx.scene.paint.Color.GREEN;
    public Paint rectFill = javafx.scene.paint.Color.BLACK;
    public static Canvas previewCanvas = new Canvas(240, 110);

    @FXML
    private AnchorPane PreviewerFX;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PreviewerFX.getChildren().add(previewCanvas);
        gc = previewCanvas.getGraphicsContext2D();

        Thread preCapture = null;

        int r = 5;
        long frameDelay = (1000 / r) * 1000000;

        Task preTask = new Task<Void>() {

            @Override
            public Void call() throws IOException, InterruptedException {

                new AnimationTimer() {
                    private long lastUpdate = 0;

                    @Override
                    public void handle(long currentNanoTime) {
                        if (currentNanoTime - lastUpdate >= frameDelay) {
                            double w = previewCanvas.getWidth();
                            double h = previewCanvas.getHeight();
                            gc.setFill(rectFill);
                            gc.clearRect(0, 0, w, h);

                            if (preImg != null) {
                                double imgWidth = h * preImg.getWidth() / preImg.getHeight();
                                double border = (w - imgWidth) / 2;
                                wr = SwingFXUtils.toFXImage(preImg, null);
                                gc.drawImage(wr, border, 0, imgWidth, h);
                            }

                            if (audioLeft > 0 || audioRight > 0) {
                                gc.setFill(audioFill);
                                gc.fillRect(0, h - audioLeft * h / 128, 15, audioLeft * h / 128);
                                gc.fillRect(w - 15, h - audioRight * h / 128, 15, audioRight * h / 128);
                            }
                            lastUpdate = currentNanoTime;
                        }
                    }
                }.start();
                return null;
            }

        };
        preCapture = new Thread(preTask);
        preCapture.setDaemon(true);
//        System.out.println("Starting PreCapture ...");
        preCapture.start();
    }

    public static void setPreImageFX(BufferedImage preImage) {

        preImg = preImage;

    }

    public static void setPreAudioLevelFX(int l, int r) {

        audioLeft = l;
        audioRight = r;

    }

}
