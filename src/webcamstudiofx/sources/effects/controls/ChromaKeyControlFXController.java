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
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import webcamstudiofx.sources.effects.ChromaKey;
import static webcamstudiofx.sources.effects.ChromaKey.effectFX;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class ChromaKeyControlFXController implements Initializable {

    ChromaKey effect = null;

    @FXML
    private Slider sliderRed;
    @FXML
    private Slider sliderGreen;
    @FXML
    private Slider sliderBlue;
    @FXML
    private AnchorPane ChromaKeyControlFX;
    @FXML
    private ColorPicker btnSelectColor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        effect = (ChromaKey) effectFX;
        sliderRed.valueProperty().addListener((obs, oldValue, newValue)
                -> sliderRedStateChanged());
        sliderGreen.valueProperty().addListener((obs, oldValue, newValue)
                -> sliderGreenStateChanged());
        sliderBlue.valueProperty().addListener((obs, oldValue, newValue)
                -> sliderBlueStateChanged());
        sliderRed.setValue(effect.getrTolerance());
        sliderGreen.setValue(effect.getgTolerance());
        sliderBlue.setValue(effect.getbTolerance());
        int red = java.awt.Color.decode(effect.getColor()).getRed();
        int blue = java.awt.Color.decode(effect.getColor()).getBlue();
        int green = java.awt.Color.decode(effect.getColor()).getGreen();
        btnSelectColor.setValue(Color.rgb(red, green, blue));
    }

    @FXML
    private void btnSelectColorAction(ActionEvent event) {
        Color c = btnSelectColor.getValue();
        String hex = String.format("#%02X%02X%02X",
                (int) (c.getRed() * 255),
                (int) (c.getGreen() * 255),
                (int) (c.getBlue() * 255));
//        System.out.println(hex);
//        System.out.print(java.awt.Color.decode(hex));
        effect.setColor(hex);
    }

    private void sliderRedStateChanged() {
        effect.setrTolerance((int) sliderRed.getValue());
    }

    private void sliderGreenStateChanged() {
        effect.setgTolerance((int) sliderGreen.getValue());
    }

    private void sliderBlueStateChanged() {
        effect.setbTolerance((int) sliderBlue.getValue());
    }

}
