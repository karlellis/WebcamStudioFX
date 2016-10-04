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
import webcamstudiofx.sources.effects.Rotation;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class RotationControlFXController implements Initializable {

    Rotation effect;

    @FXML
    private AnchorPane RotationControlFX;
    @FXML
    private Slider sldRotation;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        effect = (Rotation) effectFX;
        sldRotation.setValue(effect.getRotation());
        sldRotation.valueProperty().addListener((obs, oldValue, newValue)
                -> sldRotationStateChanged());
    }

    private void sldRotationStateChanged() {
        effect.setRotation((int) sldRotation.getValue());
    }

}
