/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import static webcamstudiofx.TruckliststudioUIController.stream_;
import static webcamstudiofx.TruckliststudioUIController.wsDistroWatch;
import webcamstudiofx.streams.SourceImage;
import webcamstudiofx.streams.SourceImageGif;
import webcamstudiofx.streams.SourceText;
import webcamstudiofx.streams.Stream;
import webcamstudiofx.util.AudioSource;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class StreamDesktopFXController implements Initializable {

    @FXML
    private TitledPane StreamDesktopFX;

    Stream stream = null;

    Listener listener = null;
    private boolean runMe = true;
    private int speed = 1; // + is faster - is slower
    AudioSource[] sourcesAudio;
    String distro = wsDistroWatch();

    @FXML
    private void streamDesktopClicked(MouseEvent event) {
        if (listener != null) {
            new Thread(() -> {
                listener.selectedSource(stream);
            }).start();

        }
    }

    @FXML
    private void streamDesktopEntered(MouseEvent event) {
//        if (listener!=null){
//            new Thread(new Runnable(){
//                
//                @Override
//                public void run() {
////                    System.out.println("Listener Not Null !!!");
//                    listener.selectedSource(stream);
//                }
//            }).start();
//            
//        }
    }

    public interface Listener {

        public void selectedSource(Stream source);

        public void closeSource(String name);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stream = stream_;
//        System.out.println("Panel="+stream.getPanelType());

        if (stream instanceof SourceText) {
//            System.out.println("Found TEXT !!!");
            try {
                final AnchorPane streamPanelFX = (AnchorPane) FXMLLoader.load(getClass().getResource("/webcamstudiofx/StreamPanelTextFX.fxml"));
                streamPanelFX.setMaxWidth(Double.MAX_VALUE);
                AnchorPane.setTopAnchor(streamPanelFX, 0.0);
                AnchorPane.setBottomAnchor(streamPanelFX, 0.0);
                AnchorPane.setLeftAnchor(streamPanelFX, 0.0);
                AnchorPane.setRightAnchor(streamPanelFX, 0.0);
                StreamDesktopFX.setContent(streamPanelFX);
            } catch (IOException ex) {
                Logger.getLogger(StreamDesktopFXController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (stream instanceof SourceImage || stream instanceof SourceImageGif) {
            try {
                final AnchorPane streamPanelFX = (AnchorPane) FXMLLoader.load(getClass().getResource("/webcamstudiofx/StreamPanelImageFX.fxml"));
                streamPanelFX.setMaxWidth(Double.MAX_VALUE);
                AnchorPane.setTopAnchor(streamPanelFX, 0.0);
                AnchorPane.setBottomAnchor(streamPanelFX, 0.0);
                AnchorPane.setLeftAnchor(streamPanelFX, 0.0);
                AnchorPane.setRightAnchor(streamPanelFX, 0.0);
                StreamDesktopFX.setContent(streamPanelFX);
            } catch (IOException ex) {
                Logger.getLogger(StreamDesktopFXController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                final AnchorPane streamPanelFX = (AnchorPane) FXMLLoader.load(getClass().getResource("/webcamstudiofx/StreamPanelFX.fxml"));
                streamPanelFX.setMaxWidth(Double.MAX_VALUE);
                AnchorPane.setTopAnchor(streamPanelFX, 0.0);
                AnchorPane.setBottomAnchor(streamPanelFX, 0.0);
                AnchorPane.setLeftAnchor(streamPanelFX, 0.0);
                AnchorPane.setRightAnchor(streamPanelFX, 0.0);
                StreamDesktopFX.setContent(streamPanelFX);
            } catch (IOException ex) {
                Logger.getLogger(StreamDesktopFXController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setListener(Listener l) {
        listener = l;
    }

}
