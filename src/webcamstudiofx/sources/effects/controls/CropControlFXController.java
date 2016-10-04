/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.sources.effects.controls;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import webcamstudiofx.mixers.MasterMixer;
import webcamstudiofx.sources.effects.Crop;
import static webcamstudiofx.sources.effects.Effect.effectFX;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class CropControlFXController implements Initializable {

    Crop effect;

    @FXML
    private Slider jslX;
    @FXML
    private Slider jslY;
    @FXML
    private Slider jslWidth;
    @FXML
    private Slider jslHeight;
    @FXML
    private AnchorPane CropControlFX;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        effect = (Crop) effectFX;

        jslX.setMax(MasterMixer.getInstance().getWidth());
        jslY.setMax(MasterMixer.getInstance().getHeight());
        jslWidth.setMax(MasterMixer.getInstance().getWidth());
        jslWidth.setMin(1);
        jslHeight.setMax(MasterMixer.getInstance().getHeight());
        jslHeight.setMin(1);

        jslX.setValue((int) effect.getX());
        jslY.setValue((int) effect.getY());
        jslWidth.setValue((int) effect.getWidth());
        jslHeight.setValue((int) effect.getHeight());

        jslX.valueProperty().addListener((obs, oldValue, newValue)
                -> jslXStateChanged());
        jslY.valueProperty().addListener((obs, oldValue, newValue)
                -> jslYStateChanged());
        jslWidth.valueProperty().addListener((obs, oldValue, newValue)
                -> jslWidthStateChanged());
        jslHeight.valueProperty().addListener((obs, oldValue, newValue)
                -> jslHeightStateChanged());

    }

    private void jslXStateChanged() {
        int x = (int) jslX.getValue();
        effect.setX(x);
    }

    private void jslYStateChanged() {
        int y = (int) jslY.getValue();
        effect.setY(y);
    }

    private void jslWidthStateChanged() {
        int w = (int) jslWidth.getValue();
        effect.setWidth(w);
    }

    private void jslHeightStateChanged() {
        int h = (int) jslHeight.getValue();
        effect.setHeight(h);
    }

}
