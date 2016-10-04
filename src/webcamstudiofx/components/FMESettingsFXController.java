/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.components;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import static webcamstudiofx.OutputPanelFXController.currFME_;
import webcamstudiofx.externals.FME;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class FMESettingsFXController implements Initializable {

    FME thisFME;

    SpinnerValueFactory valSpinVideoRate = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
    SpinnerValueFactory valSpinAudioRate = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
    SpinnerValueFactory valSpinPort = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
    SpinnerValueFactory valSpinKeyInt = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);

    private SpinnerAutoCommit<Integer> spinVideoRate = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinAudioRate = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinPort = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinKeyInt = new SpinnerAutoCommit<>();

    @FXML
    private Label lblName;
    @FXML
    private CheckBox chkHQMode;
    @FXML
    private TextField textURL;
    @FXML
    private TextField textStream;
    @FXML
    private Spinner<?> spinVideoRateClassic;
    @FXML
    private Spinner<?> spinAudioRateClassic;
    @FXML
    private TextField textMount;
    @FXML
    private PasswordField textPsw;
    @FXML
    private Spinner<?> spinPortClassic;
    @FXML
    private Spinner<?> spinKeyIntClassic;
    @FXML
    private Button btnOK;
    @FXML
    private Button btnCancel;
    @FXML
    private AnchorPane FMESettingsFX;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        thisFME = currFME_;

        spinVideoRate.editableProperty().set(true);
        spinVideoRate.setLayoutX(187.0);
        spinVideoRate.setLayoutY(162.0);
        spinVideoRate.setPrefHeight(26.0);
        spinVideoRate.setPrefWidth(163.0);
        FMESettingsFX.getChildren().add(spinVideoRate);
        spinVideoRate.setVisible(true);

        spinAudioRate.editableProperty().set(true);
        spinAudioRate.setLayoutX(196.0);
        spinAudioRate.setLayoutY(196.0);
        spinAudioRate.setPrefHeight(26.0);
        spinAudioRate.setPrefWidth(154.0);
        FMESettingsFX.getChildren().add(spinAudioRate);
        spinAudioRate.setVisible(true);

        spinPort.editableProperty().set(true);
        spinPort.setLayoutX(179.0);
        spinPort.setLayoutY(352.0);
        spinPort.setPrefHeight(26.0);
        FMESettingsFX.getChildren().add(spinPort);
        spinPort.setVisible(true);

        spinKeyInt.editableProperty().set(true);
        spinKeyInt.setLayoutX(69.0);
        spinKeyInt.setLayoutY(230.0);
        spinKeyInt.setPrefHeight(26.0);
        FMESettingsFX.getChildren().add(spinKeyInt);
        spinKeyInt.setVisible(true);

        spinVideoRate.setValueFactory(valSpinVideoRate);
        spinAudioRate.setValueFactory(valSpinAudioRate);
        spinPort.setValueFactory(valSpinPort);
        spinKeyInt.setValueFactory(valSpinKeyInt);

        String shortName = "";
        String sourceName = thisFME.getName();
//        System.out.println("sourceName: "+sourceName);
        if (sourceName.length() > 9) {
            shortName = sourceName.substring(0, 9) + " ...";
//            System.out.println("shortName: "+shortName);
        } else {
            shortName = sourceName;
        }
        lblName.setText(shortName);
        textURL.setText(thisFME.getUrl());
        if (thisFME.getStream().isEmpty()) {
            textStream.setText("");
            textStream.setDisable(true);
        } else {
            textStream.setText(thisFME.getStream());
        }
        if (thisFME.getVbitrate().isEmpty()) {
            valSpinVideoRate.setValue(0);
            spinVideoRate.setDisable(true);
        } else {
            valSpinVideoRate.setValue(Integer.parseInt(thisFME.getVbitrate()));
        }

        if (thisFME.getAbitrate().isEmpty()) {
            valSpinAudioRate.setValue(0);
            spinAudioRate.setDisable(true);
        } else {
            valSpinAudioRate.setValue(Integer.parseInt(thisFME.getAbitrate()));
        }
        if (thisFME.getMount().isEmpty()) {
            textMount.setText("");
            textMount.setDisable(true);
        } else {
            textMount.setText(thisFME.getMount());
        }
        if (thisFME.getPassword().isEmpty()) {
            textPsw.setText("");
            textPsw.setDisable(true);
        } else {
            textPsw.setText(thisFME.getPassword());
//            System.out.println("Password: " + thisFME.getPassword());
        }
        if (thisFME.getPort().isEmpty()) {
            valSpinPort.setValue(0);
            spinPort.setDisable(true);
        } else {
            valSpinPort.setValue(Integer.parseInt(thisFME.getPort()));
        }
        if (thisFME.getKeyInt().isEmpty()) {
            valSpinKeyInt.setValue(0);
            spinKeyInt.setDisable(false);
        } else {
            valSpinKeyInt.setValue(Integer.parseInt(thisFME.getKeyInt()));
        }
        chkHQMode.setSelected(thisFME.getStandard().equals("HQ"));
    }

    @FXML
    private void chkHQModeAction(ActionEvent event) {
        if (chkHQMode.isSelected()) {
            thisFME.setStandard("HQ");
        } else {
            thisFME.setStandard("STD");
        }

    }

    @FXML
    private void btnOKAction(ActionEvent event) {
        if (thisFME.getUrl().isEmpty()) {

        } else {
            thisFME.setUrl(textURL.getText());
        }

        if (thisFME.getStream().isEmpty()) {

        } else {
            thisFME.setStream(textStream.getText());
        }

        if (thisFME.getVbitrate().isEmpty()) {

        } else {
            thisFME.setVbitrate(Integer.toString(spinVideoRate.getValue().hashCode()));
        }

        if (thisFME.getAbitrate().isEmpty()) {

        } else {
            thisFME.setAbitrate(Integer.toString(spinAudioRate.getValue().hashCode()));
        }

        if (thisFME.getMount().isEmpty()) {

        } else {
            thisFME.setMount(textMount.getText());
        }

        if (thisFME.getPassword().isEmpty()) {

        } else {
            String psw = textPsw.getText();
            thisFME.setPassword(psw);
//            System.out.println("Password: " + psw);
        }

        if (thisFME.getPort().isEmpty()) {

        } else {
            thisFME.setPort(Integer.toString(spinPort.getValue().hashCode()));
        }

        if (thisFME.getKeyInt().isEmpty()) {

        } else {
            thisFME.setKeyInt(Integer.toString(spinKeyInt.getValue().hashCode()));
        }
        FMESettingsFX.getScene().getWindow().hide();
        FMESettingsFX = null;
    }

    @FXML
    private void btnCancelAction(ActionEvent event) {
        FMESettingsFX.getScene().getWindow().hide();
        FMESettingsFX = null;
    }

}
