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
import webcamstudiofx.sources.effects.HSB;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class HSBControlFXController implements Initializable {

    HSB effect;

    @FXML
    private AnchorPane HSBControlFX;
    @FXML
    private Slider sldHue;
    @FXML
    private Slider sldSaturation;
    @FXML
    private Slider sldBrightness;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        effect = (HSB) effectFX;
        sldHue.setValue(effect.getHFactor());
        sldSaturation.setValue(effect.getSFactor());
        sldBrightness.setValue(effect.getBFactor());

        sldHue.valueProperty().addListener((obs, oldValue, newValue)
                -> sldHueStateChanged());
        sldSaturation.valueProperty().addListener((obs, oldValue, newValue)
                -> sldSaturationStateChanged());
        sldBrightness.valueProperty().addListener((obs, oldValue, newValue)
                -> sldBrightnessStateChanged());
    }

    private void sldHueStateChanged() {
        effect.setHFactor((int) sldHue.getValue());
    }

    private void sldSaturationStateChanged() {
        effect.setSFactor((int) sldSaturation.getValue());
    }

    private void sldBrightnessStateChanged() {
        effect.setBFactor((int) sldBrightness.getValue());
    }

}
