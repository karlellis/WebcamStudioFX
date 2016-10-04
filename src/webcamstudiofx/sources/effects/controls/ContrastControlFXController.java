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
import webcamstudiofx.sources.effects.Contrast;
import static webcamstudiofx.sources.effects.Effect.effectFX;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class ContrastControlFXController implements Initializable {

    Contrast effect;

    @FXML
    private Slider sldBrightness;
    @FXML
    private AnchorPane ContrastControlFX;
    @FXML
    private Slider sldContrast;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        effect = (Contrast) effectFX;
        sldContrast.setValue(effect.getContrast());
        sldBrightness.setValue(effect.getBrightness());
        sldContrast.valueProperty().addListener((obs, oldValue, newValue)
                -> sldContrastStateChanged());
        sldBrightness.valueProperty().addListener((obs, oldValue, newValue)
                -> sldBrightnessStateChanged());
    }

    private void sldContrastStateChanged() {
        effect.setContrast((int) sldContrast.getValue());
    }

    private void sldBrightnessStateChanged() {
        effect.setBrightness((int) sldBrightness.getValue());
    }

}
