/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.components;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import static webcamstudiofx.OutputPanelFXController.fileStream_;
import static webcamstudiofx.OutputPanelFXController.isFileStream;
import static webcamstudiofx.OutputPanelFXController.udpStream_;
import webcamstudiofx.TruckliststudioUIController;
import webcamstudiofx.streams.SinkFile;
import webcamstudiofx.streams.SinkUDP;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class SinkSettingsFXController implements Initializable {

    SinkFile thisSinkFile = null;
    SinkUDP thisSinkUDP = null;

    SpinnerValueFactory valSpinVideoRate = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
    SpinnerValueFactory valSpinAudioRate = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);

    private final SpinnerAutoCommit<Integer> spinVideoRate = new SpinnerAutoCommit<>();
    private final SpinnerAutoCommit<Integer> spinAudioRate = new SpinnerAutoCommit<>();

    @FXML
    private Label lblName;
    @FXML
    private CheckBox chkHQMode;
    @FXML
    private Spinner<?> spinVideoRateClassic;
    @FXML
    private Spinner<?> spinAudioRateClassic;
    @FXML
    private Button btnOK;
    @FXML
    private Button btnCancel;
    @FXML
    private AnchorPane SinkSettingsFX;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        spinVideoRate.editableProperty().set(true);
        spinVideoRate.setLayoutX(187.0);
        spinVideoRate.setLayoutY(70.0);
        spinVideoRate.setPrefHeight(26.0);
        spinVideoRate.setPrefWidth(163.0);
        SinkSettingsFX.getChildren().add(spinVideoRate);
        spinVideoRate.setVisible(true);

        spinAudioRate.editableProperty().set(true);
        spinAudioRate.setLayoutX(187.0);
        spinAudioRate.setLayoutY(104.0);
        spinAudioRate.setPrefHeight(26.0);
        spinAudioRate.setPrefWidth(163.0);
        SinkSettingsFX.getChildren().add(spinAudioRate);
        spinAudioRate.setVisible(true);

        spinVideoRate.setValueFactory(valSpinVideoRate);
        spinAudioRate.setValueFactory(valSpinAudioRate);

        if (isFileStream) {
            chkHQMode.setDisable(true);
            thisSinkFile = fileStream_;
            thisSinkUDP = null;
            lblName.setText(thisSinkFile.getName());

            if (thisSinkFile.getVbitrate().isEmpty()) {
                valSpinVideoRate.setValue(0);
                spinVideoRate.setDisable(true);
            } else {
                valSpinVideoRate.setValue(Integer.parseInt(thisSinkFile.getVbitrate()));
            }

            if (thisSinkFile.getAbitrate().isEmpty()) {
                valSpinAudioRate.setValue(0);
                spinAudioRate.setDisable(true);
            } else {
                valSpinAudioRate.setValue(Integer.parseInt(thisSinkFile.getAbitrate()));
            }
        } else {
            thisSinkUDP = udpStream_;
            thisSinkFile = null;
            lblName.setText(thisSinkUDP.getName());
            if (thisSinkUDP.getVbitrate().isEmpty()) {
                valSpinVideoRate.setValue(0);
                spinVideoRate.setDisable(true);
            } else {
                valSpinVideoRate.setValue(Integer.parseInt(thisSinkUDP.getVbitrate()));
            }
            if (thisSinkUDP.getAbitrate().isEmpty()) {
                valSpinAudioRate.setValue(0);
                spinAudioRate.setDisable(false);
            } else {
                valSpinAudioRate.setValue(Integer.parseInt(thisSinkUDP.getAbitrate()));
            }
            chkHQMode.setSelected(thisSinkUDP.getStandard().equals("HQ"));
        }
    }

    @FXML
    private void chkHQModeAction(ActionEvent event) {
        if (chkHQMode.isSelected()) {
            thisSinkUDP.setStandard("HQ");
        } else {
            thisSinkUDP.setStandard("STD");
        }
    }

    @FXML
    private void btnOKAction(ActionEvent event) {
        if (thisSinkFile != null) {

            if (thisSinkFile.getVbitrate().isEmpty()) {
                // Nothing here.
            } else {
                thisSinkFile.setVbitrate(Integer.toString(spinVideoRate.getValue().hashCode()));
            }

            if (thisSinkFile.getAbitrate().isEmpty()) {
                // Nothing here.
            } else {
                thisSinkFile.setAbitrate(Integer.toString(spinAudioRate.getValue().hashCode()));
            }

        } else {
            if (thisSinkUDP.getVbitrate().isEmpty()) {

            } else {
                thisSinkUDP.setVbitrate(Integer.toString(spinVideoRate.getValue().hashCode()));
            }
            if (thisSinkUDP.getAbitrate().isEmpty()) {

            } else {
                thisSinkUDP.setAbitrate(Integer.toString(spinAudioRate.getValue().hashCode()));
            }
        }

        if (thisSinkFile != null) {
            Preferences filePrefs = TruckliststudioUIController.prefs.node("filerec");
            try {
                filePrefs.removeNode();
                filePrefs.flush();
                filePrefs = TruckliststudioUIController.prefs.node("filerec");
            } catch (BackingStoreException ex) {
                Logger.getLogger(SinkSettingsFXController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Preferences serviceF = filePrefs.node("frecordset");
            serviceF.put("abitrate", thisSinkFile.getAbitrate());
            serviceF.put("vbitrate", thisSinkFile.getVbitrate());
        } else {
            Preferences udpPrefs = TruckliststudioUIController.prefs.node("udp");
            try {
                udpPrefs.removeNode();
                udpPrefs.flush();
                udpPrefs = TruckliststudioUIController.prefs.node("udp");
            } catch (BackingStoreException ex) {
                Logger.getLogger(SinkSettingsFXController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Preferences serviceU = udpPrefs.node("uoutset");
            serviceU.put("abitrate", thisSinkUDP.getAbitrate());
            serviceU.put("vbitrate", thisSinkUDP.getVbitrate());
            serviceU.put("standard", thisSinkUDP.getStandard());
        }
        SinkSettingsFX.getScene().getWindow().hide();
        SinkSettingsFX = null;
    }

    @FXML
    private void btnCancelAction(ActionEvent event) {
        SinkSettingsFX.getScene().getWindow().hide();
        SinkSettingsFX = null;
    }

}
