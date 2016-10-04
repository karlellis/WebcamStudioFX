/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.components;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import static webcamstudiofx.components.SourceControls.sourceStream;
import webcamstudiofx.streams.SourceText;
import webcamstudiofx.streams.Stream;
import webcamstudiofx.tracks.MasterTracks;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class SourceControlsTextFXController implements Initializable {
    SourceText stream;
    ArrayList<Stream> streamS = MasterTracks.getInstance().getStreams();
    
    @FXML
    private ColorPicker txtHexColor;
    @FXML
    private RadioButton rdNone;
    @FXML
    private RadioButton rdRect;
    @FXML
    private RadioButton rdOval;
    @FXML
    private RadioButton rdRoundRect;
    @FXML
    private Slider jslBGOpacity;
    @FXML
    private AnchorPane SourceControlsTextFX;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.stream = (SourceText) sourceStream;
        SourceControlsTextFX.setId("Text");
        
//        if( stream.getLoaded()){
            String hex = stream.getBackgroundColor();
            txtHexColor.setValue(Color.valueOf(hex));
//        } else {
//            txtHexColor.setValue(Color.valueOf("#000000"));
//        }
        switch(stream.getBackground()){
            case NONE:
                rdNone.setSelected(true);
                rdRect.selectedProperty().set(false);
                rdOval.selectedProperty().set(false);
                rdRoundRect.selectedProperty().set(false);
                break;
            case RECTANGLE:
                rdRect.setSelected(true);
                rdNone.selectedProperty().set(false);
                rdOval.selectedProperty().set(false);
                rdRoundRect.selectedProperty().set(false);
                break;
            case OVAL:
                rdOval.setSelected(true);
                rdRect.selectedProperty().set(false);
                rdNone.selectedProperty().set(false);
                rdRoundRect.selectedProperty().set(false);
                break;
            case ROUNDRECT:
                rdRoundRect.setSelected(true);
                rdRect.selectedProperty().set(false);
                rdOval.selectedProperty().set(false);
                rdNone.selectedProperty().set(false);
                break;
        }
        jslBGOpacity.setValue((int)(stream.getBackgroundOpacity()*100f));
        if (stream.getBackgroundOpacity() != 0) {
            stream.setBackgroundOpacity(stream.getBackgroundOpacity());
        } else {
            stream.setBackgroundOpacity(0/100f);
        }
        if (stream.getBackground() != null){
            stream.setBackground(stream.getBackground());
        } else {
            stream.setBackground(SourceText.Shape.NONE);
        }
        jslBGOpacity.valueProperty().addListener((obs, oldValue, newValue) -> 
            jslBGOpacityStateChanged());
    }    

    @FXML
    private void txtHexColorAction(ActionEvent event) {
        Color c = txtHexColor.getValue();
        String hex = String.format( "#%02X%02X%02X",
            (int)( c.getRed() * 255 ),
            (int)( c.getGreen() * 255 ),
            (int)( c.getBlue() * 255 ) );
//        System.out.println(hex);
//        System.out.print(java.awt.Color.decode(hex));
        stream.setBackGroundColor(hex);
    }

    @FXML
    private void rdNoneAction(ActionEvent event) {
        stream.setBackground(SourceText.Shape.NONE);
        stream.setStrBackground("none");
        rdRect.selectedProperty().set(false);
        rdOval.selectedProperty().set(false);
        rdRoundRect.selectedProperty().set(false);
    }

    @FXML
    private void rdRectAction(ActionEvent event) {
        stream.setBackground(SourceText.Shape.RECTANGLE);
        stream.setStrBackground("rectangle");
        rdNone.selectedProperty().set(false);
        rdOval.selectedProperty().set(false);
        rdRoundRect.selectedProperty().set(false);
        
    }

    @FXML
    private void rdOvalAction(ActionEvent event) {
        stream.setBackground(SourceText.Shape.OVAL);
        stream.setStrBackground("oval");
        rdRect.selectedProperty().set(false);
        rdNone.selectedProperty().set(false);
        rdRoundRect.selectedProperty().set(false);
    }

    @FXML
    private void rdRoundRectAction(ActionEvent event) {
        stream.setBackground(SourceText.Shape.ROUNDRECT);
        stream.setStrBackground("roundrect");
        rdRect.selectedProperty().set(false);
        rdOval.selectedProperty().set(false);
        rdNone.selectedProperty().set(false);
    }

    private void jslBGOpacityStateChanged() {
        stream.setBackgroundOpacity((float)(jslBGOpacity.getValue())/100f);
    }
    
}
