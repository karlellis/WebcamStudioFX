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
import static webcamstudiofx.sources.effects.Effect.effectFX;
import webcamstudiofx.sources.effects.RGB;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class RGBControlFXController implements Initializable {

    RGB effect;

    @FXML
    private AnchorPane RGBControlFX;
    @FXML
    private Slider sliderRed;
    @FXML
    private Slider sliderGreen;
    @FXML
    private Slider sliderBlue;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        effect = (RGB) effectFX;
        sliderRed.setValue(effect.getRThreshold());
        sliderGreen.setValue(effect.getGThreshold());
        sliderBlue.setValue(effect.getBThreshold());
        sliderRed.valueProperty().addListener((obs, oldValue, newValue)
                -> sliderRedStateChanged());
        sliderGreen.valueProperty().addListener((obs, oldValue, newValue)
                -> sliderGreenStateChanged());
        sliderBlue.valueProperty().addListener((obs, oldValue, newValue)
                -> sliderBlueStateChanged());
    }

    private void sliderRedStateChanged() {
        effect.setRThreshold((int) sliderRed.getValue());
    }

    private void sliderGreenStateChanged() {
        effect.setGThreshold((int) sliderGreen.getValue());
    }

    private void sliderBlueStateChanged() {
        effect.setBThreshold((int) sliderBlue.getValue());
    }

}
