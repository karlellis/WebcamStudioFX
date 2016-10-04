/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.components;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import static webcamstudiofx.components.SourceControls.sourceStream;
import webcamstudiofx.streams.SourceTrack;
import webcamstudiofx.streams.Stream;
import webcamstudiofx.tracks.MasterTracks;
import webcamstudiofx.tracks.transitions.Transition;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class SourceControlTransitionsController implements Initializable {

    Stream stream;
    ArrayList<Stream> streamS = MasterTracks.getInstance().getStreams();

    @FXML
    private ListView<String> lstStartTransitions;
    @FXML
    private ListView<String> lstEndTransitions;
    @FXML
    private Button btnApplyTransToAll;
    @FXML
    private Button btnResetTrans;
    @FXML
    private AnchorPane SourceControlTransitionsFX;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.stream = sourceStream;
        SourceControlTransitionsFX.setId("Transitions");
        final ObservableList<String> start = FXCollections.observableArrayList();
        start.addAll(Arrays.asList(Transition.getStartTransitions()));
        final ObservableList<String> end = FXCollections.observableArrayList();
        end.addAll(Arrays.asList(Transition.getEndTransitions()));
        lstStartTransitions.setItems(start);
        lstEndTransitions.setItems(end);

        if (stream != null) {
//            System.out.println("Stream Not Null !!!");
stream.getStartTransitions().stream().forEach((t) -> {
    lstStartTransitions.getSelectionModel().select(t.getClass().getSimpleName());
//                System.out.println("start=" + t.getClass().getSimpleName());
            });
stream.getEndTransitions().stream().forEach((t) -> {
    lstEndTransitions.getSelectionModel().select(t.getClass().getSimpleName());
//                System.out.println("end=" + t.getClass().getSimpleName());
            });
        }
    }

    public static <String> void forceListRefreshOn(ListView<String> lsv) {
        ObservableList<String> items = lsv.<String>getItems();
        lsv.<String>setItems(null);
        lsv.<String>setItems(items);
    }

    @FXML
    private void btnApplyTransToAllAction(ActionEvent event) {
        streamS.stream().map((s) -> {
            ObservableList<String> sList = lstStartTransitions.getSelectionModel().getSelectedItems();
            s.getStartTransitions().clear();
            s.getTracks().stream().forEach((sc) -> {
                sc.startTransitions.clear();
            });
            sList.stream().map((t) -> Transition.getInstance(s, t)).map((sT) -> {
                s.addStartTransition(sT);
                return sT;
            }).forEach((sT) -> {
                s.getTracks().stream().forEach((sc) -> {
                    sc.startTransitions.add(sT);
                });
            });
            return s;
        }).forEach((s) -> {
            ObservableList<String> eList = lstEndTransitions.getSelectionModel().getSelectedItems();
            s.getEndTransitions().clear();
            s.getTracks().stream().forEach((sc) -> {
                sc.endTransitions.clear();
            });
            eList.stream().map((t) -> Transition.getInstance(s, t)).map((eT) -> {
                s.addEndTransition(eT);
                return eT;
            }).forEach((eT) -> {
                s.getTracks().stream().forEach((sc) -> {
                    sc.endTransitions.add(eT);
                });
            });
        });
        ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Transitions Applied to all Tracks");
        ResourceMonitorFXController.getInstance().addMessage(label);
    }

    @FXML
    private void btnResetTransAction(ActionEvent event) {
        stream.getStartTransitions().clear();
        stream.getEndTransitions().clear();
        lstEndTransitions.getSelectionModel().clearSelection();
        lstStartTransitions.getSelectionModel().clearSelection();
    }

}
