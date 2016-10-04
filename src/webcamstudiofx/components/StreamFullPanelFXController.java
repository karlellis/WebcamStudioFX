/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.components;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import static webcamstudiofx.MainPanelFXController.viewerFX;
import static webcamstudiofx.components.ViewerFXController.liveViewCanvas;
import webcamstudiofx.mixers.Frame;
import webcamstudiofx.mixers.MasterMixer;
import webcamstudiofx.mixers.SystemPlayer;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class StreamFullPanelFXController implements Initializable, MasterMixer.SinkListener {

    private SystemPlayer player = null;

    @FXML
    private AnchorPane StreamFullPanelFX;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        liveViewCanvas.widthProperty().set(StreamFullPanelFX.getWidth());
        liveViewCanvas.heightProperty().set((StreamFullPanelFX.getHeight()));
        liveViewCanvas.widthProperty().bind(StreamFullPanelFX.widthProperty());
        liveViewCanvas.heightProperty().bind(StreamFullPanelFX.heightProperty());
        StreamFullPanelFX.getChildren().add(liveViewCanvas);
        player = SystemPlayer.getInstanceFX(viewerFX);
    }

    @Override
    public void newFrame(Frame frame) {
        player.addFrame(frame);
    }

}
