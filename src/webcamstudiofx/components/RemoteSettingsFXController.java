/*
 * Copyright (C) 2016 karl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package webcamstudiofx.components;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import static webcamstudiofx.TrackPanelFXController.getRemPort;
import static webcamstudiofx.TrackPanelFXController.getRemPsw;
import static webcamstudiofx.TrackPanelFXController.getRemUsr;
import static webcamstudiofx.TrackPanelFXController.setRemPort;
import static webcamstudiofx.TrackPanelFXController.setRemPsw;
import static webcamstudiofx.TrackPanelFXController.setRemUsr;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class RemoteSettingsFXController implements Initializable {
    SpinnerValueFactory valRemPort = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 65535);
    private SpinnerAutoCommit<Integer> spinRemotePort = new SpinnerAutoCommit<>();
    
    @FXML
    private AnchorPane RemoteSettingsFX;
    @FXML
    private Label lblName;
    @FXML
    private Spinner<?> spinRemPort;
    @FXML
    private Button btnOK;
    @FXML
    private Button btnCancel;
    @FXML
    private PasswordField pswPassword;
    @FXML
    private TextField txtUser;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtUser.setText(getRemUsr());
        pswPassword.setText(getRemPsw());
//        <Spinner fx:id="spinRemPort" layoutX="150.0" layoutY="108.0" prefHeight="26.0" prefWidth="163.0" AnchorPane.rightAnchor="15.0" />
        
        spinRemotePort.editableProperty().set(true);
        spinRemotePort.setLayoutX(150.0);
        spinRemotePort.setLayoutY(108.0);
        spinRemotePort.setPrefHeight(26.0);
        spinRemotePort.setPrefWidth(163.0);
        AnchorPane.setRightAnchor(spinRemotePort, 15.0);
        RemoteSettingsFX.getChildren().add(spinRemotePort);
        spinRemotePort.setVisible(true);
        spinRemotePort.setValueFactory(valRemPort);
        
        valRemPort.setValue(getRemPort());
    }    

    @FXML
    private void btnOKAction(ActionEvent event) {
        setRemPort(valRemPort.getValue().hashCode());
        setRemUsr(txtUser.getText());
        String psw = pswPassword.getText();
        setRemPsw(psw);
        RemoteSettingsFX.getScene().getWindow().hide();
        RemoteSettingsFX = null;
    }

    @FXML
    private void btnCancelAction(ActionEvent event) {
        RemoteSettingsFX.getScene().getWindow().hide();
        RemoteSettingsFX = null;
    }
    
}
