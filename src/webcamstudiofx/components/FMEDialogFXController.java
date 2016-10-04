/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.components;

import java.awt.image.BufferedImage;
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
import javafx.scene.paint.Paint;
import static webcamstudiofx.OutputPanelFXController.currFME_;
import webcamstudiofx.externals.FME;
import webcamstudiofx.streams.Stream;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class FMEDialogFXController implements Initializable, Stream.Listener {

    FME thisFME;
    public static String add = "cancel";
    private Paint warnCol = javafx.scene.paint.Color.RED;

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
    private TextField textName;
    @FXML
    private AnchorPane FMEDialogFX;

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
        FMEDialogFX.getChildren().add(spinVideoRate);
        spinVideoRate.setVisible(true);

        spinAudioRate.editableProperty().set(true);
        spinAudioRate.setLayoutX(196.0);
        spinAudioRate.setLayoutY(196.0);
        spinAudioRate.setPrefHeight(26.0);
        spinAudioRate.setPrefWidth(154.0);
        FMEDialogFX.getChildren().add(spinAudioRate);
        spinAudioRate.setVisible(true);

        spinPort.editableProperty().set(true);
        spinPort.setLayoutX(179.0);
        spinPort.setLayoutY(352.0);
        spinPort.setPrefHeight(26.0);
        FMEDialogFX.getChildren().add(spinPort);
        spinPort.setVisible(true);

        spinKeyInt.editableProperty().set(true);
        spinKeyInt.setLayoutX(69.0);
        spinKeyInt.setLayoutY(230.0);
        spinKeyInt.setPrefHeight(26.0);
        FMEDialogFX.getChildren().add(spinKeyInt);
        spinKeyInt.setVisible(true);

        valSpinVideoRate.valueProperty().addListener((obs, oldValue, newValue)
                -> spinVideoRateStateChanged());
        spinVideoRate.setValueFactory(valSpinVideoRate);

        valSpinAudioRate.valueProperty().addListener((obs, oldValue, newValue)
                -> spinAudioRateStateChanged());
        spinAudioRate.setValueFactory(valSpinAudioRate);

        valSpinPort.valueProperty().addListener((obs, oldValue, newValue)
                -> spinPortStateChanged());
        spinPort.setValueFactory(valSpinPort);

        valSpinKeyInt.valueProperty().addListener((obs, oldValue, newValue)
                -> spinKeyIntStateChanged());
        spinKeyInt.setValueFactory(valSpinKeyInt);
    }

    @FXML
    private void chkHQModeAction(ActionEvent event) {
        if (chkHQMode.isSelected()) {
            thisFME.setStandard("HQ");
        } else {
            thisFME.setStandard("STD");
        }
    }

    private void spinVideoRateStateChanged() {
        //To change body of generated methods, choose Tools | Templates.
    }

    private void spinAudioRateStateChanged() {
        //To change body of generated methods, choose Tools | Templates.
    }

    private void spinPortStateChanged() {
        //To change body of generated methods, choose Tools | Templates.
    }

    private void spinKeyIntStateChanged() {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sourceUpdated(Stream stream) {
        // Nothing here.
    }

    @Override
    public void updatePreview(BufferedImage image) {
        // Nothing here.
    }

    @FXML
    private void btnOKAction(ActionEvent event) {
        if (textName.getText().isEmpty()) {
            lblName.setTextFill(warnCol);
        } else {
            thisFME.setName(textName.getText());
            thisFME.setUrl(textURL.getText());
            thisFME.setStream(textStream.getText());
            thisFME.setVbitrate(Integer.toString(spinVideoRate.getValue()));
            thisFME.setAbitrate(Integer.toString(spinAudioRate.getValue()));
            thisFME.setMount(textMount.getText());

            String psw = textPsw.getText();
            thisFME.setPassword(psw);
            if (spinPort.getValue().hashCode() != 0) {
                thisFME.setPort(Integer.toString(spinPort.getValue()));
            }
            thisFME.setKeyInt(Integer.toString(spinKeyInt.getValue()));

            add = "ok";
            FMEDialogFX.getScene().getWindow().hide();
            FMEDialogFX = null;
        }
    }

    @FXML
    private void btnCancelAction(ActionEvent event) {
        add = "cancel";
        FMEDialogFX.getScene().getWindow().hide();
        FMEDialogFX = null;
    }

}
