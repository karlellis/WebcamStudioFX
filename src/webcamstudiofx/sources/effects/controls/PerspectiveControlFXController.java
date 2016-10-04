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
import static webcamstudiofx.sources.effects.Effect.effectFX;
import webcamstudiofx.sources.effects.Perspective;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class PerspectiveControlFXController implements Initializable {

    Perspective effect;

    @FXML
    private AnchorPane PerspectiveControlFX;
    @FXML
    private Slider jslXLeft;
    @FXML
    private Slider jslXRight;
    @FXML
    private Slider jslYTop;
    @FXML
    private Slider jslYBottom;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        effect = (Perspective) effectFX;

        jslXLeft.setMax(MasterMixer.getInstance().getWidth());
        jslXLeft.setMin(-MasterMixer.getInstance().getWidth());

        jslXRight.setMax(MasterMixer.getInstance().getWidth());
        jslXRight.setMin(-MasterMixer.getInstance().getWidth());

        jslYTop.setMax(MasterMixer.getInstance().getHeight());
        jslYTop.setMin(-MasterMixer.getInstance().getHeight());

        jslYBottom.setMax(MasterMixer.getInstance().getHeight());
        jslYBottom.setMin(-MasterMixer.getInstance().getHeight());

        jslXLeft.setValue((int) effect.getX3());
        jslYTop.setValue((int) effect.getY1());
        jslXRight.setValue((int) effect.getX1());
        jslYBottom.setValue((int) effect.getY3());

        jslXLeft.valueProperty().addListener((obs, oldValue, newValue)
                -> jslXLeftStateChanged());
        jslXRight.valueProperty().addListener((obs, oldValue, newValue)
                -> jslXRightStateChanged());
        jslYTop.valueProperty().addListener((obs, oldValue, newValue)
                -> jslYTopStateChanged());
        jslYBottom.valueProperty().addListener((obs, oldValue, newValue)
                -> jslYBottomStateChanged());
    }

    private void jslXLeftStateChanged() {
        effect.setX3((int) jslXLeft.getValue());
    }

    private void jslXRightStateChanged() {
        effect.setX1((int) jslXRight.getValue());
    }

    private void jslYTopStateChanged() {
        effect.setY1((int) jslYTop.getValue());
    }

    private void jslYBottomStateChanged() {
        effect.setY3((int) jslYBottom.getValue());
    }

}
