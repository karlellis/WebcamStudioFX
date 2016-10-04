/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.components;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import static webcamstudiofx.components.SourceControls.sourceStream;
import webcamstudiofx.streams.Stream;
import webcamstudiofx.tracks.MasterTracks;
import webcamstudiofx.util.Tools;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class SourceControlGSEffectsController implements Initializable {
    Stream stream;
    ArrayList<Stream> streamS = MasterTracks.getInstance().getStreams();
    public static Properties gsEffects = new Properties();
    
    @FXML
    private AnchorPane SourceControlGSEffects;
    @FXML
    private ComboBox cboGSEffects;
    @FXML
    private Label lblGSEffect;
    @FXML
    private Button btnSetGSEffect;
    @FXML
    private Button btnUnsetGSEffect;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.stream = sourceStream;
        SourceControlGSEffects.setId("GS FX");
        if ("GS".equals(stream.getComm())){
            cboGSEffects.setDisable(false);
            btnSetGSEffect.setDisable(false);
        } else {
            cboGSEffects.setDisable(true);
            btnSetGSEffect.setDisable(true);
            btnUnsetGSEffect.setDisable(true);
        }
        try {
            gsEffects.load(getClass().getResourceAsStream("/webcamstudiofx/externals/linux/gseffects.properties"));
            ObservableList<Object> options = FXCollections.observableArrayList();
            gsEffects.keySet().stream().forEach((o) -> {
                options.add(o);
            });
            cboGSEffects.setItems(options);
            cboGSEffects.getSelectionModel().select(0);
            
        } catch (IOException ex) {
            Logger.getLogger(SourceControlGSEffectsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (!"".equals(stream.getGSEffect())){
            btnSetGSEffect.setDisable(true);
            btnUnsetGSEffect.setDisable(false);
            lblGSEffect.setText(stream.getGSEffect());
        }
        
    }    

    @FXML
    private void cboGSEffectsAction(ActionEvent event) {
    }

    @FXML
    private void btnSetGSEffectAction(ActionEvent event) {
        lblGSEffect.setText(cboGSEffects.getSelectionModel().getSelectedItem().toString());
        stream.setGSEffect(cboGSEffects.getSelectionModel().getSelectedItem().toString().toLowerCase());
        if (stream.isPlaying()){
            stream.stop();
            Tools.sleep(100);
            stream.read();
        }
        btnSetGSEffect.setDisable(true);
        btnUnsetGSEffect.setDisable(false);
    }

    @FXML
    private void btnUnsetGSEffectAction(ActionEvent event) {
        if (!"".equals(lblGSEffect.getText())){
            stream.setGSEffect("");
            if (stream.isPlaying()){
                stream.stop();
                Tools.sleep(100);
                stream.read();
            }
            lblGSEffect.setText("None");
            btnSetGSEffect.setDisable(false);
            btnUnsetGSEffect.setDisable(true);
        }
    }
    
}
