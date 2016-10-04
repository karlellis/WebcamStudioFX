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
import static webcamstudiofx.MainPanelFXController.previewerFX;
import webcamstudiofx.mixers.Frame;
import webcamstudiofx.mixers.PrePlayer;
import webcamstudiofx.mixers.PreviewMixer;
import static webcamstudiofx.components.PreviewerFXController.previewCanvas;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class TSPreviewPanelFXController implements Initializable, PreviewMixer.SinkListener {

    private PrePlayer player = null;

    @FXML
    private AnchorPane TSPreviewPanelFX;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        previewCanvas.widthProperty().set(TSPreviewPanelFX.getWidth());
        previewCanvas.heightProperty().set((TSPreviewPanelFX.getHeight()));
        previewCanvas.widthProperty().bind(TSPreviewPanelFX.widthProperty());
        previewCanvas.heightProperty().bind(TSPreviewPanelFX.heightProperty());
        TSPreviewPanelFX.getChildren().add(previewCanvas);
        player = PrePlayer.getPreInstanceFX(previewerFX);
    }

    @Override
    public void newPreFrame(Frame frame) {
        player.addFrame(frame);
    }

}
