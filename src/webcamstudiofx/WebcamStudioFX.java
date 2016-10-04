/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import static webcamstudiofx.TruckliststudioUIController.exitApplication;
import static webcamstudiofx.Version.version;
import webcamstudiofx.streams.Stream;
import webcamstudiofx.tracks.MasterTracks;
import webcamstudiofx.util.Tools;

/**
 *
 * @author karl
 */
public class WebcamStudioFX extends Application {

    public static Scene scene;
    public static Stage TS;
    public static File cmdFile = null;
    public static String cmdOut = null;
    public static boolean cmdAutoStart = false;
    public static boolean cmdRemote = false;
    private final static String userHomeDir = Tools.getUserHome();

    public interface Listener {

        public void savePreferences();
    }
    static Listener listenerTSfxTSContr = null;

    public static void listenerTSfxTSContr(Listener l) {
        listenerTSfxTSContr = l;
    }

    @Override
    public void start(Stage tsStage) throws IOException {

        TS = tsStage;
        Platform.setImplicitExit(false);
        File dir = new File(userHomeDir, ".webcamstudiofx");
        if (!dir.exists()) {
            dir.mkdir();
        }
        AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/webcamstudiofx/TruckliststudioUI.fxml"));

        scene = new Scene(root);
        tsStage.setTitle("WebcamStudioFX " + version);
        tsStage.setScene(scene);
        tsStage.show();

        tsStage.setOnCloseRequest((WindowEvent t) -> {
            listenerTSfxTSContr.savePreferences();
            boolean close = true;
            ArrayList<Stream> streamzI = MasterTracks.getInstance().getStreams();
            int sinkStream = 0;
            sinkStream = streamzI.stream().filter((s) -> (s.getClass().toString().contains("Sink"))).map((_item) -> 1).reduce(sinkStream, Integer::sum);
            ArrayList<String> sourceChI = MasterTracks.getInstance().getTracks();

            if (streamzI.size() - sinkStream > 0 || sourceChI.size() > 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initOwner(TS);
                Window window = alert.getOwner();
                alert.setX(window.getX() + window.getWidth() / 2 - alert.getWidth() / 2);
                alert.setY(window.getY() + window.getHeight() / 2 - alert.getHeight() / 2);
                alert.setTitle("TS Confirmation");
                alert.setHeaderText("Save Studio Remainder");
                alert.setContentText("Really Close WebcamStudioFX ?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    close = true;
                } else {
                    close = false;
                    t.consume();
                    tsStage.show();
                }
            }
            exitApplication(close);
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args != null){
            int c = 0;
            for (String arg : args){
//                System.out.println("Argument: "+arg);
                if (arg.endsWith("studio")){
                    cmdFile = new File(arg);
                }
                if (arg.equals("-o")) {
                    cmdOut = args[c+1];
                }
                if (arg.equals("-autoplay")) {
                    cmdAutoStart = true;
                }
                if (arg.equals("-remote")) {
                    cmdRemote = true;
                }
                c++;
            }
        }
        launch(args);
    }

}
