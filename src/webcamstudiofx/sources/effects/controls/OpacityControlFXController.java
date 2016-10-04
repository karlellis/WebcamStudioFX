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
import webcamstudiofx.sources.effects.Opacity;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class OpacityControlFXController implements Initializable {

    Opacity effect;

    @FXML
    private AnchorPane OpacityControlFX;
    @FXML
    private Slider sldOpacity;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        effect = (Opacity) effectFX;
        sldOpacity.setValue(effect.getOpacity());
        sldOpacity.valueProperty().addListener((obs, oldValue, newValue)
                -> sldOpacityStateChanged());
    }

    private void sldOpacityStateChanged() {
        effect.setOpacity((int) sldOpacity.getValue());
    }

}
