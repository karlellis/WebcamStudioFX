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
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import webcamstudiofx.TrackPanelFXController;
import static webcamstudiofx.components.SourceControls.sourceStream;
import webcamstudiofx.streams.SourceMovie;
import webcamstudiofx.streams.SourceMusic;
import webcamstudiofx.streams.SourceTrack;
import webcamstudiofx.streams.SourceWebcam;
import webcamstudiofx.streams.Stream;
import webcamstudiofx.tracks.MasterTracks;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class SourceControlTrackController implements Initializable {
    Stream stream;
    ArrayList<Stream> streamS = MasterTracks.getInstance().getStreams();
    
    @FXML
    private AnchorPane SourceControlTracksFX;
    @FXML
    private Button btnApplyAllTrk;
    @FXML
    private Button btnApplyAllTrkSet;
    @FXML
    private Button btnApplyAllTrkPos;
    @FXML
    private Button btnMakeATrack;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.stream = sourceStream;
        SourceControlTracksFX.setId("Track opt");
        if (!(stream instanceof SourceMovie) && !(stream instanceof SourceMusic)) { //&& !(stream instanceof SourceWebcam)
            btnMakeATrack.setDisable(true);
        }
    }    

    @FXML
    private void btnApplyAllTrkAction(ActionEvent event) {
        SourceTrack sch = null;
        for (int i=0; i < stream.getTracks().size(); i++){
            String scName = stream.getTracks().get(i).getName();
            sch=SourceTrack.getTrack(scName, stream);
            stream.removeTrackAt(i);
            stream.addTrackAt(sch,i);
        }
        ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis()+10000, stream.getName() + " copied to all Tracks.");
        ResourceMonitorFXController.getInstance().addMessage(label);
    }

    @FXML
    private void btnApplyAllTrkSetAction(ActionEvent event) {
        SourceTrack sch = null;
        String selChName = TrackPanelFXController.getSelectedTrack();
        for (int i=0; i < stream.getTracks().size(); i++){
            String scName = stream.getTracks().get(i).getName();
            boolean isPlay = stream.getTracks().get(i).getIsPlaying();
            if (!selChName.equals(scName)) {
                sch=SourceTrack.getTrackIgnorePlay(scName, stream);
                sch.setIsPlaying(isPlay);
                stream.removeTrackAt(i);
                stream.addTrackAt(sch,i);
            }
        }
        ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis()+10000, stream.getName() + " copied to all Tracks.");
        ResourceMonitorFXController.getInstance().addMessage(label);
    }

    @FXML
    private void btnApplyAllTrkPosAction(ActionEvent event) {
        SourceTrack sch = null;
        for (int i=0; i < stream.getTracks().size(); i++){
            String scName = stream.getTracks().get(i).getName();
            int oTime = stream.getTracks().get(i).getDuration();
            String oCont = stream.getTracks().get(i).getText();
            boolean isPlay = stream.getTracks().get(i).getIsPlaying();
            sch=SourceTrack.getTrackIgnoreContent(scName, stream);
            sch.setText(oCont);
            sch.setDuration(oTime);
            sch.setIsPlaying(isPlay);
            stream.removeTrackAt(i);
            stream.addTrackAt(sch,i);
        }
        ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis()+10000, stream.getName() + " copied to all Tracks.");
        ResourceMonitorFXController.getInstance().addMessage(label);
    }

    @FXML
    private void btnMakeATrackAction(ActionEvent event) {
        TrackPanelFXController.makeATrack(stream);
    }
    
}
