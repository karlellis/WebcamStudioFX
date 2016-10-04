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
import webcamstudiofx.sources.effects.Mosaic;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class MosaicControlFXController implements Initializable {

    Mosaic effect;

    @FXML
    private AnchorPane MosaicControlFX;
    @FXML
    private Slider sldSplitBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        effect = (Mosaic) effectFX;
        sldSplitBox.setValue(effect.getSplitValue());
        sldSplitBox.valueProperty().addListener((obs, oldValue, newValue)
                -> sldSplitBoxStateChanged());
    }

    private void sldSplitBoxStateChanged() {
        effect.setSplitValue((int) sldSplitBox.getValue());
    }

}
