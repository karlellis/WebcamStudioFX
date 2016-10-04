/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.sources.effects.controls;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import static webcamstudiofx.sources.effects.Effect.effectFX;
import webcamstudiofx.sources.effects.RevealLeftNFade;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class RevealLeftNFadeControlFXController implements Initializable {

    RevealLeftNFade effect;

    @FXML
    private AnchorPane RevealLeftNFadeControlFX;
    @FXML
    private Slider sldVelocity;
    @FXML
    private CheckBox jcbLoop;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        effect = (RevealLeftNFade) effectFX;
        sldVelocity.setValue(effect.getVel());
        sldVelocity.valueProperty().addListener((obs, oldValue, newValue)
                -> sldVelocityStateChanged());
        jcbLoop.setSelected(effect.getLoop());
    }

    @FXML
    private void jcbLoopAction(ActionEvent event) {
        if (jcbLoop.isSelected()) {
            effect.setLoop(true);
        } else {
            effect.setLoop(false);
        }
    }

    private void sldVelocityStateChanged() {
        effect.setVel((int) sldVelocity.getValue());
    }

}
