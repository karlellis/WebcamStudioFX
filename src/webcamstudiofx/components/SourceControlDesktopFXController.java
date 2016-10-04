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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import static webcamstudiofx.TruckliststudioUIController.os;
import static webcamstudiofx.components.SourceControls.sourceStream;
import webcamstudiofx.mixers.MasterMixer;
import webcamstudiofx.streams.SourceDesktop;
import webcamstudiofx.util.Screen;
import webcamstudiofx.util.Tools;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class SourceControlDesktopFXController implements Initializable {

    SourceDesktop source;
    ArrayList<String> xidList = new ArrayList<>();
    ArrayList<String> deskList = new ArrayList<>();
    protected String[] screenID = Screen.getSources();
    SpinnerValueFactory valSpinX = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
    SpinnerValueFactory valSpinY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
    SpinnerValueFactory valSpinW = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
    SpinnerValueFactory valSpinH = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
    SpinnerValueFactory valSpinRate; // = new SpinnerValueFactory.IntegerSpinnerValueFactory(source.getRate(), MasterMixer.getInstance().getRate());
    SpinnerValueFactory valSpinN = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Screen.getSources().length - 1);
    private SpinnerAutoCommit<Integer> spinX = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinY = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinW = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinH = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinRate = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinN = new SpinnerAutoCommit<>();

    @FXML
    private AnchorPane SourceControlDesktopFX;
    @FXML
    private CheckBox jchEnableWindowsCap;
    @FXML
    private ComboBox jcbWindowsCapList;
    @FXML
    private GridPane deskGrid;
    @FXML
    private Label lblDesktopX;
    @FXML
    private Label lblDesktopY;
    @FXML
    private Label lblDesktopW;
    @FXML
    private Label lblDesktopH;
    @FXML
    private Label lblDesktopN;
    @FXML
    private Label lblWindowsCap;
    @FXML
    private Button btnRefreshWindowsList;
    @FXML
    private Label lblDesktopRate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.source = (SourceDesktop) sourceStream;
        SourceControlDesktopFX.setId("Desktop");
        valSpinRate = new SpinnerValueFactory.IntegerSpinnerValueFactory(source.getRate(), MasterMixer.getInstance().getRate());

        spinX.editableProperty().set(true);
        valSpinX.valueProperty().addListener((obs, oldValue, newValue)
                -> spinXStateChanged());
        spinX.setValueFactory(valSpinX);
        deskGrid.add(spinX, 1, 0);
        spinX.setVisible(true);

        spinY.editableProperty().set(true);
        valSpinY.valueProperty().addListener((obs, oldValue, newValue)
                -> spinYStateChanged());
        spinY.setValueFactory(valSpinY);
        deskGrid.add(spinY, 1, 1);
        spinY.setVisible(true);

        spinW.editableProperty().set(true);
        valSpinW.valueProperty().addListener((obs, oldValue, newValue)
                -> spinWStateChanged());
        spinW.setValueFactory(valSpinW);
        deskGrid.add(spinW, 1, 2);
        spinW.setVisible(true);

        spinH.editableProperty().set(true);
        valSpinH.valueProperty().addListener((obs, oldValue, newValue)
                -> spinHStateChanged());
        spinH.setValueFactory(valSpinH);
        deskGrid.add(spinH, 1, 3);
        spinH.setVisible(true);

        spinRate.editableProperty().set(true);
        valSpinRate.valueProperty().addListener((obs, oldValue, newValue)
                -> spinRateStateChanged());
        spinRate.setValueFactory(valSpinRate);
        deskGrid.add(spinRate, 1, 4);
        spinRate.setVisible(true);

        spinN.editableProperty().set(true);
        valSpinN.valueProperty().addListener((obs, oldValue, newValue)
                -> spinNStateChanged());
        spinN.setValueFactory(valSpinN);
        deskGrid.add(spinN, 1, 5);
        spinN.setVisible(true);

        valSpinX.setValue(source.getDesktopX());
        valSpinY.setValue(source.getDesktopY());
        valSpinW.setValue(source.getDesktopW());
        valSpinH.setValue(source.getDesktopH());
        valSpinN.setValue(source.getDeskN());

        spinRate.setDisable(true);
        lblDesktopRate.setDisable(true);
//        valSpinRate.setValue(source.getRate());
        if (os == Tools.OS.LINUX) {
            initCapWindows();
        }
        if ("GS".equals(source.getComm())) {
            jchEnableWindowsCap.setSelected(source.getSingleWindow());
            if (source.getSingleWindow()) {
                lblDesktopX.setDisable(true);
                spinX.setDisable(true);
                lblDesktopY.setDisable(true);
                spinY.setDisable(true);
                lblDesktopW.setDisable(true);
                spinW.setDisable(true);
                lblDesktopH.setDisable(true);
                spinH.setDisable(true);
                jcbWindowsCapList.setDisable(false);
                lblWindowsCap.setDisable(false);
                btnRefreshWindowsList.setDisable(false);
            } else {
                lblDesktopX.setDisable(false);
                spinX.setDisable(false);
                lblDesktopY.setDisable(false);
                spinY.setDisable(false);
                lblDesktopW.setDisable(false);
                spinW.setDisable(false);
                lblDesktopH.setDisable(false);
                spinH.setDisable(false);
                jcbWindowsCapList.setDisable(true);
                lblWindowsCap.setDisable(true);
                btnRefreshWindowsList.setDisable(true);
            }
        } else {
            lblDesktopX.setDisable(false);
            spinX.setDisable(false);
            lblDesktopY.setDisable(false);
            spinY.setDisable(false);
            lblDesktopW.setDisable(false);
            spinW.setDisable(false);
            lblDesktopH.setDisable(false);
            spinH.setDisable(false);
            jcbWindowsCapList.setDisable(true);
            lblWindowsCap.setDisable(true);
            btnRefreshWindowsList.setDisable(true);
            jchEnableWindowsCap.setSelected(false);
            source.setSingleWindow(false);
            source.setDesktopXid("");
            source.setElementXid("");
            source.setDesktopN("0");
        }
        if (!"".equals(source.getElementXid())) {
            jcbWindowsCapList.getSelectionModel().select(source.getElementXid());
        }

    }

    @FXML
    private void jchEnableWindowsCapAction(ActionEvent event) {
        if (jchEnableWindowsCap.isSelected()) {
            lblDesktopX.setDisable(true);
            spinX.setDisable(true);
            lblDesktopY.setDisable(true);
            spinY.setDisable(true);
            lblDesktopW.setDisable(true);
            spinW.setDisable(true);
            lblDesktopH.setDisable(true);
            spinH.setDisable(true);
            jcbWindowsCapList.setDisable(false);
            lblWindowsCap.setDisable(false);
            btnRefreshWindowsList.setDisable(false);
            source.setSingleWindow(true);
        } else {
            lblDesktopX.setDisable(false);
            spinX.setDisable(false);
            lblDesktopY.setDisable(false);
            spinY.setDisable(false);
            lblDesktopW.setDisable(false);
            spinW.setDisable(false);
            lblDesktopH.setDisable(false);
            spinH.setDisable(false);
            jcbWindowsCapList.setDisable(true);
            lblWindowsCap.setDisable(true);
            btnRefreshWindowsList.setDisable(true);
            source.setSingleWindow(false);
            source.setDesktopXid("");
            source.setElementXid("");
            source.setDesktopN("0");
        }
    }

    @FXML
    private void jcbWindowsCapListAction(ActionEvent event) {
        String elementXid = jcbWindowsCapList.getSelectionModel().getSelectedItem().toString();
        String[] getXid = null;
        for (int i = 0; i < xidList.size(); i++) {
            if (xidList.get(i).contains(elementXid)) {
                getXid = xidList.get(i).split(" ");
                source.setDesktopXid(getXid[0]);
                source.setElementXid(elementXid);
                source.setDesktopN(deskList.get(i));
            }
        }
        setWindowGeometry(source.getDesktopXid());
    }

    private void spinXStateChanged() {
        source.setDesktopX((Integer) spinX.getValue());
    }

    private void spinYStateChanged() {
        source.setDesktopY((Integer) spinY.getValue());
    }

    private void spinWStateChanged() {
        source.setDesktopW((Integer) spinW.getValue());
    }

    private void spinHStateChanged() {
        source.setDesktopH((Integer) spinH.getValue());
    }

    private void spinRateStateChanged() {
        source.setRate((Integer) spinRate.getValue());
    }

    private void spinNStateChanged() {
        int deskN = (Integer) spinN.getValue();
        if (deskN < 0) {
            valSpinN.setValue(0);
        } else {
            for (int i = 0; i <= Screen.getSources().length - 1; i++) {
                if ((Integer) spinN.getValue() == i) {
                    valSpinX.setValue(Screen.getX(screenID[i]));
                    valSpinY.setValue(Screen.getY(screenID[i]));
                    valSpinW.setValue(Screen.getWidth(screenID[i]));
                    valSpinH.setValue(Screen.getHeight(screenID[i]));
                }
            }
            source.setDeskN(deskN);
        }
    }

    @SuppressWarnings("unchecked")
    private void setWindowGeometry(String xid) {
        Runtime rt = Runtime.getRuntime();
        String setWinGeometry = "xwininfo -id " + xid;
        try {
            Process setWindowGeometry = rt.exec(setWinGeometry);
            Tools.sleep(10);
            setWindowGeometry.waitFor(); //Author spoonybard896
            BufferedReader buf = new BufferedReader(new InputStreamReader(
                    setWindowGeometry.getInputStream()));
            String line = "";
            while ((line = buf.readLine()) != null) {
//                System.out.println("Windows Info: "+line);
                line = line.replaceAll("  ", "");
                line = line.replaceAll(" ", "");
                if (line.contains("Absolute")) {
                    if (line.contains("X")) {
                        String[] value = line.split(":");
                        source.setWindowX(Integer.parseInt(value[1]));
                    }
                    if (line.contains("Y")) {
                        String[] value = line.split(":");
                        source.setWindowY(Integer.parseInt(value[1]));
                    }
                }
                if (line.contains("Width")) {
                    String[] value = line.split(":");
                    source.setWindowW(Integer.parseInt(value[1]));
                }
                if (line.contains("Height")) {
                    String[] value = line.split(":");
                    source.setWindowH(Integer.parseInt(value[1]));
                }
            }
            System.out.println("X:" + source.getWindowX() + " Y:" + source.getWindowX() + " W:" + source.getWindowW() + " H:" + source.getWindowH());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void initCapWindows() {
        Runtime rt = Runtime.getRuntime();
        String getAllWindowsComm = "wmctrl -l";
//        DefaultComboBoxModel model = new DefaultComboBoxModel();
        ObservableList<Object> options = FXCollections.observableArrayList();

        try {
            Process getAllWindowsList = rt.exec(getAllWindowsComm);
            Tools.sleep(10);
            getAllWindowsList.waitFor(); //Author spoonybard896
            BufferedReader buf = new BufferedReader(new InputStreamReader(
                    getAllWindowsList.getInputStream()));
            String line = "";
            while ((line = buf.readLine()) != null) {
//                System.out.println("Windows: "+line);
                line = line.replaceAll("  ", " ");
                String[] window = line.split(" ");
                String windowRest = "";
                for (int i = 3; i < window.length; i++) {
                    windowRest += window[i];
                }
                options.add(windowRest);
                xidList.add(window[0] + " " + windowRest);
                String desktop = window[1];
//                System.out.println("desktop: "+desktop);
                deskList.add(desktop);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        jcbWindowsCapList.setItems(options);
        if ("GS".equals(source.getComm())) {
            jchEnableWindowsCap.setDisable(false);
        } else {
            jchEnableWindowsCap.setDisable(true);
        }

    }

    @FXML
    private void btnRefreshWindowsListAction(ActionEvent event) {
        initCapWindows();
    }

}
