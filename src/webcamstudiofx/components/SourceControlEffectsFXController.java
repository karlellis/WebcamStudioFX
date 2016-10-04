/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.components;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import static webcamstudiofx.TrackPanelFXController.master;
import static webcamstudiofx.components.SourceControls.sourceStream;
import webcamstudiofx.sources.effects.Effect;
import webcamstudiofx.sources.effects.Stretch;
import webcamstudiofx.streams.SourceImage;
import webcamstudiofx.streams.SourceText;
import webcamstudiofx.streams.Stream;
import webcamstudiofx.tracks.MasterTracks;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class SourceControlEffectsFXController implements Initializable {

    Stream stream;
    final ObservableList<Effect> listModel = FXCollections.observableArrayList();

    ArrayList<Stream> streamS = MasterTracks.getInstance().getStreams();
    @FXML
    private AnchorPane SourceControlEffectsFX;
    @FXML
    private ComboBox<Effect> cboEffects;
    @FXML
    private Button btnAddEffect;
    @FXML
    private Button btnDeleteEffect;
    @FXML
    private Button btnMoveUp;
    @FXML
    private Button btnMoveDown;
    @FXML
    private ListView<Effect> lstEffects;
    @FXML
    private AnchorPane panSettings;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.stream = sourceStream;
        SourceControlEffectsFX.setId("FX");

        lstEffects.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Effect> observable, Effect oldValue, Effect newValue) -> {
            panSettings.getChildren().clear();
            if (lstEffects.getSelectionModel().getSelectedItem() != null) {
                try {
                    Effect e = (Effect) lstEffects.getSelectionModel().getSelectedItem();
                    if (e.getControl() != null) {
                        AnchorPane effectControl = e.getControl();
                        AnchorPane.setTopAnchor(effectControl, 0.0);
                        AnchorPane.setBottomAnchor(effectControl, 0.0);
                        AnchorPane.setLeftAnchor(effectControl, 0.0);
                        AnchorPane.setRightAnchor(effectControl, 0.0);
                        panSettings.getChildren().add(effectControl);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(SourceControlEffectsFXController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        boolean found = false;
        ObservableList<Effect> model = FXCollections.observableArrayList();
        for (Effect e : Effect.getEffects().values()) {
            found = false;
            for (Effect se : stream.getEffects()) {
                if (se.getName().equals(e.getName())) {
                    listModel.add(se);
                    model.add(se);
                    found = true;
                    break;
                }
            }
            if (!found) {
                model.add(e);
            }
        }
        cboEffects.setItems(model);
        lstEffects.setItems(listModel);
        cboEffects.getSelectionModel().select(0);
    }

    @FXML
    private void cboEffectsAction(ActionEvent event) {
    }

    @FXML
    private void btnAddEffectAction(ActionEvent event) {
        listModel.add(cboEffects.getSelectionModel().getSelectedItem());
        Effect fx = (Effect) cboEffects.getSelectionModel().getSelectedItem();
        if (fx instanceof Stretch && (stream instanceof SourceText || stream instanceof SourceImage)) {
            ((Stretch) fx).setWidth(stream.getWidth());
            ((Stretch) fx).setHeight(stream.getHeight());
        }
        stream.addEffect(fx);
        if (stream.getisATrack()) {
//            System.out.println("SourceName=" + stream.getName());
            master.updateTrack(stream.getName());
        }
        lstEffects.getSelectionModel().select(cboEffects.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void btnDeleteEffectAction(ActionEvent event) {
        if (lstEffects.getSelectionModel().getSelectedItem() != null) {
            Effect e = (Effect) lstEffects.getSelectionModel().getSelectedItem();
            e.resetFX();
            listModel.remove(e);
            stream.removeEffect(e);
            if (stream.getisATrack()) {
                master.updateTrack(stream.getName());
            }
        }
    }

    @FXML
    private void btnMoveUpAction(ActionEvent event) {
        if (lstEffects.getSelectionModel().getSelectedItem() != null) {
            int indexSelected = lstEffects.getSelectionModel().getSelectedIndex();
            if (indexSelected > 0) {
                int indexPrevious = indexSelected - 1;
                Effect previous = (Effect) listModel.get(indexPrevious); //getElementAt(indexPrevious);
                Effect selected = (Effect) listModel.get(indexSelected); //getElementAt(indexSelected);
                listModel.set(indexPrevious, selected); //setElementAt(selected, indexPrevious);
                listModel.set(indexSelected, previous); //setElementAt(previous, indexSelected);
                stream.getEffects().set(indexSelected, previous);
                stream.getEffects().set(indexPrevious, selected);
                lstEffects.getSelectionModel().select(indexPrevious); // setSelectedIndex(indexPrevious);
            }
        }
    }

    @FXML
    private void btnMoveDownAction(ActionEvent event) {
        if (lstEffects.getSelectionModel().getSelectedItem() != null) {
            int indexSelected = lstEffects.getSelectionModel().getSelectedIndex();
            if (indexSelected != -1 && indexSelected < (listModel.size() - 1)) {
                int indexNext = indexSelected + 1;
                Effect next = (Effect) listModel.get(indexNext); // getElementAt(indexNext);
                Effect selected = (Effect) listModel.get(indexSelected); // getElementAt(indexSelected);
                listModel.set(indexNext, selected); // setElementAt(selected, indexNext);
                listModel.set(indexSelected, next); // setElementAt(next, indexSelected);
                stream.getEffects().set(indexSelected, next);
                stream.getEffects().set(indexNext, selected);
                lstEffects.getSelectionModel().select(indexNext); // setSelectedIndex(indexNext);
            }
        }
    }

}
