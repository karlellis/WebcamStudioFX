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
import webcamstudiofx.sources.effects.Gain;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class GainControlFXController implements Initializable {

    Gain effect;

    @FXML
    private AnchorPane GainControlFX;
    @FXML
    private Slider sldBias;
    @FXML
    private Slider sldGain;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        effect = (Gain) effectFX;
        sldBias.setValue(effect.getBias());
        sldGain.setValue(effect.getGain());

        sldBias.valueProperty().addListener((obs, oldValue, newValue)
                -> sldBiasStateChanged());
        sldGain.valueProperty().addListener((obs, oldValue, newValue)
                -> sldGainStateChanged());

    }

    private void sldBiasStateChanged() {
        effect.setBias((int) sldBias.getValue());
    }

    private void sldGainStateChanged() {
        effect.setGain((int) sldGain.getValue());
    }

}
