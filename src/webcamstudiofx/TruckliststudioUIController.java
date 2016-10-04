/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import static javafx.application.Application.STYLESHEET_CASPIAN;
import static javafx.application.Application.STYLESHEET_MODENA;
import static javafx.application.Application.setUserAgentStylesheet;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;
import static webcamstudiofx.MainPanelFXController.spinFPS_;
import static webcamstudiofx.MainPanelFXController.spinHeight_;
import static webcamstudiofx.MainPanelFXController.spinWidth_;
import static webcamstudiofx.MainPanelFXController.valFPS;
import static webcamstudiofx.MainPanelFXController.valH;
import static webcamstudiofx.MainPanelFXController.valW;
import static webcamstudiofx.TrackPanelFXController.lblPlayingTrack;
import static webcamstudiofx.TrackPanelFXController.list_;
import static webcamstudiofx.WebcamStudioFX.TS;
import static webcamstudiofx.WebcamStudioFX.cmdAutoStart;
import static webcamstudiofx.WebcamStudioFX.cmdFile;
import static webcamstudiofx.WebcamStudioFX.cmdOut;
import static webcamstudiofx.WebcamStudioFX.cmdRemote;
import webcamstudiofx.components.ResourceMonitorFXController;
import webcamstudiofx.components.ResourceMonitorLabelFX;
import webcamstudiofx.components.SourceControls;
import webcamstudiofx.externals.ProcessRenderer;
import webcamstudiofx.mixers.MasterMixer;
import webcamstudiofx.mixers.PrePlayer;
import webcamstudiofx.mixers.PreviewMixer;
import webcamstudiofx.mixers.SystemPlayer;
import webcamstudiofx.streams.SourceAudioSource;
import webcamstudiofx.streams.SourceImage;
import webcamstudiofx.streams.SourceImageGif;
import webcamstudiofx.streams.SourceMovie;
import webcamstudiofx.streams.SourceMusic;
import webcamstudiofx.streams.SourceText;
import webcamstudiofx.streams.SourceTrack;
import webcamstudiofx.streams.Stream;
import webcamstudiofx.studio.Studio;
import webcamstudiofx.tracks.MasterTracks;
import webcamstudiofx.util.BackEnd;
import webcamstudiofx.util.Tools;
import webcamstudiofx.util.Tools.OS;
import static webcamstudiofx.WebcamStudioFX.listenerTSfxTSContr;
import static webcamstudiofx.util.Tools.sleep;
import static webcamstudiofx.WebcamStudioFX.listenerTSfxTSContr;
import webcamstudiofx.exporter.vloopback.VideoDevice;
import webcamstudiofx.streams.SourceDesktop;
import webcamstudiofx.streams.SourceWebcam;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class TruckliststudioUIController implements Initializable, StreamDesktopFXController.Listener, WebcamStudioFX.Listener {

    public static TruckliststudioUIController truckliststudiouicontroller;
    OutputPanelFXController recorderFX;
    public Paint busyTab = javafx.scene.paint.Color.RED;
    private Paint resetTab = javafx.scene.paint.Color.BLACK;
    public Paint selectedText = javafx.scene.paint.Color.GREEN;
    boolean loadingFinish = false;

    String stringPlay = "-fx-background-color: linear-gradient(greenyellow, limegreen);";
    String stringStop = "-fx-background-color: -fx-box-border, -fx-inner-border, -fx-body-color;";

    AnchorPane outputPanelFX;
    AnchorPane trackPanelFX;
    AnchorPane mainPanelFX;
    AnchorPane streamPanelFX;

    public static Stream stream_;
    public static Preferences prefs = null;
    public static Properties animations = new Properties();
    // FF = 0 ; AV = 1 ; GS = 2
    public static int outFMEbe = 1;
    boolean resetPrefs = false;
    private final static String userHomeDir = Tools.getUserHome();
    Frame about = new Frame();

    public static int audioFreq = 22050;
    public static String theme = "Modena";
    ArrayList<Stream> streamS = MasterTracks.getInstance().getStreams();
    public static File lastFolder = null;
    boolean ffmpeg = BackEnd.ffmpegDetected();
    boolean avconv = BackEnd.avconvDetected();
    boolean firstRun = true;
    static boolean autoAR = false;
    public static OS os = Tools.getOS();
    static boolean autoTrack = false;

    public int numWebcams = 0;
    public int numVideos = 0;
    public int numMusics = 0;
    public int numPictures = 0;
    public int numTexts = 0;
    public int numAudioIns = 0;
    public int numDesktops = 0;
    public int tabCount = 0;

    public Label lblWebcam = new Label("Webcams(0)");
    public Label lblVideo = new Label("Videos(0)");
    public Label lblMusic = new Label("Musics(0)");
    public Label lblPicture = new Label("Pictures(0)");
    public static Label lblText = new Label("Texts(0)");
    public static Label lblAudioIn = new Label("AudioIns(0)");
    public static Label lblDesktop = new Label("Desktops(0)");

    ArrayList<Accordion> tabs = new ArrayList<>();
    public static boolean x64 = false;
    public static boolean winGS = false;
    public static ComboBox cboAnimations_;

    final static SwingNode sourceControlsNode = new SwingNode();
    Alert alert;
    String stringSourceSelected;
    String selectedBack = "-fx-background-color: linear-gradient(yellow, orange);";
    String unselectedBack = "-fx-background-color: -fx-box-border, -fx-inner-border, -fx-body-color;";
    String shortName = "";

    @FXML
    private Button btnNewStudio;
    @FXML
    private Button btnAddFile;
    @FXML
    private Button btnAddFolder;
    @FXML
    private Button btnLoadStudio;
    @FXML
    private Button btnImportStudio;
    @FXML
    private Button btnSaveStudio;
    @FXML
    private Button btnAbout;
    @FXML
    private Label lblAudioFreq;
    @FXML
    private ComboBox<String> cboAudioHz;
    @FXML
    private Label lblThemeSwitch;
    @FXML
    private ComboBox<String> cboTheme;
    @FXML
    private Button btnSysGC;
    @FXML
    private SplitPane mainSplit;
    @FXML
    private SplitPane mainVerticalSplit;
    @FXML
    private SplitPane mainHorizontalSplit;
    @FXML
    private SplitPane mainBottomSplit;
    @FXML
    private AnchorPane topLPane;
    @FXML
    private AnchorPane bottomLPane;
    @FXML
    private AnchorPane bottomRPane;
    @FXML
    public AnchorPane truckliststudioFX;
    @FXML
    private ToggleButton tglFFmpeg;
    @FXML
    private ToggleButton tglAVconv;
    @FXML
    private ToggleButton tglGST;
    @FXML
    private AnchorPane panSources;
    @FXML
    private TabPane tabSources;
    @FXML
    private Accordion videoDesktop;
    @FXML
    private ToggleButton tglAutoAR;
    @FXML
    private ToggleButton tglAutoTrack;
    @FXML
    private Button btnAddAudio;
    @FXML
    private Accordion musicDesktop;
    @FXML
    private Accordion pictureDesktop;
    @FXML
    private Accordion textDesktop;
    @FXML
    private Accordion audioInDesktop;
    @FXML
    private Button btnAddAnimation;
    @FXML
    private ComboBox cboAnimations;
    @FXML
    private Button btnAddText;
    @FXML
    private AnchorPane controlPane;
    @FXML
    private TabPane controlTab;
    @FXML
    private Button btnRemoveSource;
    @FXML
    private Label lblSourceSelected;
    @FXML
    private ComboBox cboWebcam;
    @FXML
    private Button btnAddWebcams;
    @FXML
    private Button btnResetPrefs;
    @FXML
    private Accordion webcamDesktop;
    @FXML
    private Accordion desktopDesktop;
    @FXML
    private Button btnAddDesktop;

    @FXML
    private void btnImportStudioAction(ActionEvent event) {
        final ArrayList<String> allStreams = new ArrayList<>();
        MasterTracks.getInstance().getStreams().stream().filter((str) -> (!str.toString().toLowerCase().contains("sink"))).forEach((str) -> {
            allStreams.add(str.getName());
        });
        FileChooser chooser = new FileChooser();
        if (lastFolder.exists()) {
            chooser.setInitialDirectory(lastFolder);
        }
        FileChooser.ExtensionFilter studioFilter = new FileChooser.ExtensionFilter("Studio files (*.studio)", "*.studio", "*.studio");
        FileChooser.ExtensionFilter allFilesFilter = new FileChooser.ExtensionFilter("All files (*.*)", "*.*", "*.*");
        chooser.getExtensionFilters().add(studioFilter);
        chooser.getExtensionFilters().add(allFilesFilter);
        chooser.setSelectedExtensionFilter(studioFilter);
        chooser.setTitle("Import a Studio ...");
        Stage primaryStage = new Stage();
        final File file = chooser.showOpenDialog(primaryStage);

        if (file != null) {

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(TS);
            Window window = alert.getOwner();
            alert.setX(window.getX() + window.getWidth() / 2 - alert.getWidth() / 2);
            alert.setY(window.getY() + window.getHeight() / 2 - alert.getHeight() / 2);
            Platform.runLater(() -> {
                alert.setTitle("TS Information");
                alert.setHeaderText(null);
                alert.setContentText("Importing Studio, please wait ...");
                alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            });

            Task task = new Task<Void>() {
                @Override
                public Void call() throws IOException {
                    lastFolder = file.getParentFile();
                    try {
                        Studio.LText = new ArrayList<>();
                        Studio.extstream = new ArrayList<>();
                        Studio.ImgMovMus = new ArrayList<>();
                        Studio.load(file, "add");
                        Studio.main();
                    } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
                        Logger.getLogger(TruckliststudioUIController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    for (int u = 0; u < Studio.ImgMovMus.size(); u++) {
                        Tools.sleep(10);
                        Stream s = Studio.extstream.get(u);
                        stream_ = s;
                        boolean noDouble = true;
                        if (s != null) {
                            //                    System.out.println("Stream Ch: "+s.getTracks());
                            // to fix 0 channels .studio import
                            for (String str : allStreams) {
                                //                    System.out.println("ComparedStreamName="+str);
                                if (s.getName().equals(str) && s.getisATrack()) {
                                    //                            System.out.println("Double Stream !!!");
                                    noDouble = false;
                                    s.destroy();
                                }
                            }
                            if (noDouble) {
                                if (s.getTracks().isEmpty()) {
                                    ArrayList<String> allChan = new ArrayList<>();
                                    MasterTracks.getInstance().getTracks().stream().forEach((scn) -> {
                                        allChan.add(scn);
                                        //                                System.out.println("Current Studio Ch: "+scn+" added.");
                                    });
                                    allChan.stream().forEach((sc) -> {
                                        s.addTrack(SourceTrack.getTrack(sc, s));
                                    });
                                }

                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/webcamstudiofx/StreamDesktopFX.fxml"));
                                TitledPane streamDesktopFX = (TitledPane) fxmlLoader.load();
                                StreamDesktopFXController controller = fxmlLoader.<StreamDesktopFXController>getController();
                                controller.setListener(truckliststudiouicontroller);
                                streamDesktopFX.setText(s.getName());
                                streamDesktopFX.setMaxWidth(Double.MAX_VALUE);
                                AnchorPane.setTopAnchor(streamDesktopFX, 0.0);
                                AnchorPane.setBottomAnchor(streamDesktopFX, 0.0);
                                AnchorPane.setLeftAnchor(streamDesktopFX, 0.0);
                                AnchorPane.setRightAnchor(streamDesktopFX, 0.0);

                                if (s instanceof SourceMovie) {

                                    Platform.runLater(() -> {
                                        videoDesktop.getPanes().add(streamDesktopFX);
                                    });

                                    numVideos += 1;
                                    lblVideo.setTextFill(busyTab);

                                    Platform.runLater(() -> {
                                        lblVideo.setText("Videos(" + numVideos + ")");
                                    });

                                } else if (s instanceof SourceMusic) {

                                    Platform.runLater(() -> {
                                        musicDesktop.getPanes().add(streamDesktopFX);
                                    });

                                    numMusics += 1;
                                    lblMusic.setTextFill(busyTab);

                                    Platform.runLater(() -> {
                                        lblMusic.setText("Musics(" + numMusics + ")");
                                    });

                                } else if (s instanceof SourceImage || s instanceof SourceImageGif) { //|| s instanceof SourceImageU 

                                    Platform.runLater(() -> {
                                        pictureDesktop.getPanes().add(streamDesktopFX);
                                    });

                                    numPictures += 1;
                                    lblPicture.setTextFill(busyTab);

                                    Platform.runLater(() -> {
                                        lblPicture.setText("Pictures(" + numPictures + ")");
                                    });

                                } else if (s instanceof SourceAudioSource) {

                                    Platform.runLater(() -> {
                                        audioInDesktop.getPanes().add(streamDesktopFX);
                                    });

                                    numAudioIns += 1;
                                    lblAudioIn.setTextFill(busyTab);

                                    Platform.runLater(() -> {
                                        lblAudioIn.setText("AudioIns(" + numAudioIns + ")");
                                    });

                                } else if (s instanceof SourceWebcam) {
                                    numWebcams += 1;
                                    lblWebcam.setTextFill(busyTab);
                                    Platform.runLater(() -> {
                                        webcamDesktop.getPanes().add(streamDesktopFX);
                                        lblWebcam.setText("Webcams(" + numWebcams + ")");
                                    });

                                } else if (s instanceof SourceDesktop) {
                                    numDesktops += 1;
                                    lblDesktop.setTextFill(busyTab);
                                    Platform.runLater(() -> {
                                        desktopDesktop.getPanes().add(streamDesktopFX);
                                        lblDesktop.setText("Desktops(" + numDesktops + ")");
                                    });

                                }
                                s.setLoaded(false);
                            }
                            System.out.println("Adding Source: " + s.getName());
                        }
                    }

                    Studio.extstream.clear();
                    Studio.extstream = null;
                    Studio.ImgMovMus.clear();
                    Studio.ImgMovMus = null;
                    for (int t = 0; t < Studio.LText.size(); t++) {
                        SourceText text = Studio.LText.get(t);
                        // to fix 0 channels .studio import
                        if (text.getTracks().isEmpty()) {
                            ArrayList<String> allChan = new ArrayList<>();
                            MasterTracks.getInstance().getTracks().stream().forEach((scn) -> {
                                allChan.add(scn);
                                //                                    System.out.println("Current Studio Ch: "+scn+" added.");
                            });
                            allChan.stream().forEach((sc) -> {
                                text.addTrack(SourceTrack.getTrack(sc, text));
                            });
                        }
                        stream_ = text;

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/webcamstudiofx/StreamDesktopFX.fxml"));
                        TitledPane streamDesktopFX = (TitledPane) fxmlLoader.load();
                        StreamDesktopFXController controller = fxmlLoader.<StreamDesktopFXController>getController();
                        controller.setListener(truckliststudiouicontroller);
                        streamDesktopFX.setText(text.getName());
                        streamDesktopFX.setMaxWidth(Double.MAX_VALUE);
                        AnchorPane.setTopAnchor(streamDesktopFX, 0.0);
                        AnchorPane.setBottomAnchor(streamDesktopFX, 0.0);
                        AnchorPane.setLeftAnchor(streamDesktopFX, 0.0);
                        AnchorPane.setRightAnchor(streamDesktopFX, 0.0);

                        Platform.runLater(() -> {
                            textDesktop.getPanes().add(streamDesktopFX);
                        });

                        numTexts += 1;
                        lblText.setTextFill(busyTab);

                        Platform.runLater(() -> {
                            lblText.setText("Texts(" + numTexts + ")");
                        });

                        text.setLoaded(false);

                        System.out.println("Adding Source: " + text.getName());

                    }
                    Studio.LText.clear();
                    Studio.LText = null;
                    Tools.sleep(300);
                    MasterTracks master = MasterTracks.getInstance(); //
                    ArrayList<String> chNameL = new ArrayList<>();
                    Studio.trackLoad.stream().forEach((chsct) -> {
                        chNameL.add(chsct.getName());
//                        System.out.println("TrackLoad="+chsct.getName());
                    });
                    LinkedHashSet<String> hs = new LinkedHashSet<>(chNameL);
                    chNameL.clear();
                    chNameL.addAll(hs);
                    chNameL.stream().map((chsct) -> {
                        listenerTSfxTPfx.addLoadingTrackfx(chsct);
                        return chsct;
                    }).forEach((chsct) -> {
                        master.insertStudio(chsct);
                    });
                    Tools.sleep(500);
                    Studio.trackLoad.clear();
                    ArrayList<String> allChan = new ArrayList<>();
                    master.getInstance().getTracks().stream().forEach((scn) -> {
                        allChan.add(scn);
                    });
                    master.getStreams().stream().filter((s) -> (s.getisATrack())).forEach((s) -> {
                        int i = 0;
                        for (String chan : allChan) {
                            if (s.getTrkName().equals(chan)) {
                                boolean backState = false;
                                if (s.isPlaying()) {
                                    backState = true;
                                }
                                s.setIsPlaying(true);
                                s.addTrackAt(SourceTrack.getTrack(allChan.get(i), s), i);
                                if (backState) {
                                    s.setIsPlaying(true);
                                } else {
                                    s.setIsPlaying(false);
                                }
                            } else {
                                s.addTrackAt(SourceTrack.getTrackIgnorePlay(allChan.get(i), s), i);
                            }
                            i++;
                        }
                    });
                    ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Studio is imported!");
                    ResourceMonitorFXController.getInstance().addMessage(label);
                    if (alert.isShowing()) {
                        Platform.runLater(() -> alert.close());
                    }
                    return null;
                }
            };

            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();

        } else {
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Import Cancelled!");
            ResourceMonitorFXController.getInstance().addMessage(label);
        }

    }

    private boolean checkWinBits() {
        File prgSystemDir = new File("C:\\");
        File[] listFile = prgSystemDir.listFiles();
        for (File f : listFile) {
//            System.out.println("File Name: " + f.getName());
            if (f.getName().equals("Program Files (x86)")) {
                x64 = true;
                break;
            } else {
                x64 = false;
            }
        }
        System.out.println("bit64: " + x64);
        return x64;
    }

    private boolean checkWinGS() {
        File prgSystemDir = new File("C:\\");
        File[] listFile = prgSystemDir.listFiles();
        for (File f : listFile) {
//            System.out.println("File Name: " + f.getName());
            if (f.getName().equals("gstreamer")) {
                winGS = true;
                break;
            } else {
                winGS = false;
            }
        }
        System.out.println("winGS: " + winGS);
        return winGS;
    }

    @FXML
    private void btnNewStudioAction(ActionEvent event) {
        boolean doNew = true;
        ArrayList<Stream> streamzI = MasterTracks.getInstance().getStreams();
        int sinkStream = 0;
        sinkStream = streamzI.stream().filter((s) -> (s.getClass().toString().contains("Sink"))).map((_item) -> 1).reduce(sinkStream, Integer::sum);//            System.out.println("Stream: "+s);

        ArrayList<String> sourceChI = MasterTracks.getInstance().getTracks();
        if (streamzI.size() - sinkStream > 0 || sourceChI.size() > 0) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initOwner(TS);
            Window window = alert.getOwner();
            alert.setX(window.getX() + window.getWidth() / 2 - alert.getWidth() / 2);
            alert.setY(window.getY() + window.getHeight() / 2 - alert.getHeight() / 2);
            alert.setTitle("TS Confirmation");
            alert.setHeaderText("Current Studio will be closed !!!");
//            alert.setContentText("Are you ok with this?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                doNew = true;
            } else {
                doNew = false;
            }
        }

        if (doNew) {
            SystemPlayer.getInstanceFX(null).stop();
            Tools.sleep(10);
            PrePlayer.getPreInstanceFX(null).stop();
            Tools.sleep(10);
            MasterTracks.getInstance().endAllStream();
            MasterTracks.getInstance().getStreams().stream().forEach((s) -> {
                s.updateStatus();
            });
            ArrayList<Stream> streamz = MasterTracks.getInstance().getStreams();
            ArrayList<String> sourceCh = MasterTracks.getInstance().getTracks();
            do {
                for (int l = 0; l < streamz.size(); l++) {
                    Stream removeS = streamz.get(l);
                    removeS.destroy();
                    removeS = null;
                }
                for (int a = 0; a < sourceCh.size(); a++) {
                    String removeSc = sourceCh.get(a);
                    MasterTracks.getInstance().removeTrack(removeSc);
                    listenerTSfxTPfx.removeTracksfx(removeSc, a);
                }
            } while ((streamz.size() > 0 || sourceCh.size() > 0));

            if (list_.<String>getItems() != null) {
                list_.<String>getItems().clear();
            }

            listenerTSfxTPfx.stopChTimefx(null);
            listenerTSfxTPfx.resetBtnStatesfx();
            listenerOPfx.resetBtnStatesfx();
            listenerOPfx.resetSinksfx(null);

            controlTab.getTabs().clear();

            lblSourceSelected.setText("");
            cleanDesktops();

            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "New Studio Created.");
            ResourceMonitorFXController.getInstance().addMessage(label);
            TS.setTitle("WebcamStudioFX " + Version.version);
        } else {
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "New Studio Cancelled.");
            ResourceMonitorFXController.getInstance().addMessage(label);
        }
        System.gc();
    }

    @FXML
    private void cboAudioHzAction(ActionEvent event) {
        final String audioHz = cboAudioHz.getSelectionModel().getSelectedItem();
        if (audioHz.equals("22050Hz")) {
            audioFreq = 22050;
        } else {
            audioFreq = 44100;
        }
        MasterMixer.getInstance().stop();
        PreviewMixer.getInstance().stop();
        Tools.sleep(100);
        SystemPlayer.getInstanceFX(null).stop();
        Tools.sleep(30);
        PrePlayer.getPreInstanceFX(null).stop();
        Tools.sleep(30);
        MasterTracks.getInstance().stopAllStream();
        streamS.stream().forEach((s) -> {
            s.updateStatus();
        });
        MasterMixer.getInstance().start();
        PreviewMixer.getInstance().start();
        ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Audio set to: " + audioFreq + "Hz");
        ResourceMonitorFXController.getInstance().addMessage(label);
    }

    @FXML
    private void cboThemeAction(ActionEvent event) {
        final String themeSW = cboTheme.getSelectionModel().getSelectedItem();
        if (themeSW.equals("Modena")) {
            theme = "Modena";
            setUserAgentStylesheet(STYLESHEET_MODENA);
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Theme set to \"" + theme + "\"");
            ResourceMonitorFXController.getInstance().addMessage(label);
        } else {
            theme = "Caspian";
            setUserAgentStylesheet(STYLESHEET_CASPIAN);
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Theme set to \"" + theme + "\"");
            ResourceMonitorFXController.getInstance().addMessage(label);
        }
//        Alert alert = new Alert(AlertType.INFORMATION);
//        alert.initOwner(TS);
//        Window window = alert.getOwner();
//        alert.setX(window.getX() + window.getWidth() / 2 - alert.getWidth() / 2);
//        alert.setY(window.getY() + window.getHeight() / 2 - alert.getHeight() / 2);
//        alert.setTitle("TS Information");
//        alert.setHeaderText(null);
//        alert.setContentText("You need to restart TrucklistStudio for the changes to take effect.");
//        alert.initModality(Modality.APPLICATION_MODAL);
//        alert.showAndWait();
    }

    public void restartDialog() {

    }

    @FXML
    private void btnSaveStudioAction(ActionEvent event) {
        boolean stopAll = true;
        File file = null;
        try {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initOwner(TS);
            Window window = alert.getOwner();
            alert.setX(window.getX() + window.getWidth() / 2 - alert.getWidth() / 2);
            alert.setY(window.getY() + window.getHeight() / 2 - alert.getHeight() / 2);
            alert.setTitle("TS Confirmation");
            alert.setHeaderText("All Playing Streams will be Stopped !!!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                stopAll = true;
            } else {
                stopAll = false;
            }

            if (stopAll) {
                FileChooser chooser = new FileChooser();
                if (lastFolder.exists()) {
                    chooser.setInitialDirectory(lastFolder);
                }
                ExtensionFilter studioFilter = new ExtensionFilter("Studio files (*.studio)", "*.studio", "*.studio");
                ExtensionFilter allFilesFilter = new ExtensionFilter("All files (*.*)", "*.*", "*.*");
                chooser.getExtensionFilters().add(studioFilter);
                chooser.getExtensionFilters().add(allFilesFilter);
                chooser.setSelectedExtensionFilter(studioFilter);
                chooser.setTitle("Save a Studio ...");
                Stage primaryStage = new Stage();
                file = chooser.showSaveDialog(primaryStage);
            }

            if (file != null && stopAll) {

                final Alert saveInfo = new Alert(AlertType.INFORMATION);
                saveInfo.initOwner(TS);
                window = saveInfo.getOwner();
                saveInfo.setX(window.getX() + window.getWidth() / 2 - saveInfo.getWidth() / 2);
                saveInfo.setY(window.getY() + window.getHeight() / 2 - saveInfo.getHeight() / 2);
//                System.out.println("Wait dialog...");
                Platform.runLater(() -> {
                    saveInfo.setTitle("TS Information");
                    saveInfo.setHeaderText(null);
                    saveInfo.setContentText("Saving Studio, please wait ...");
                    saveInfo.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
                    saveInfo.initModality(Modality.APPLICATION_MODAL);
                    saveInfo.showAndWait();
                });

                final File fileF = file;
                lblSourceSelected.setText("");

                Task task = new Task<Void>() {
                    @Override
                    public Void call() throws IOException {
                        if (fileF != null) {
                            File fileS = fileF;
                            lastFolder = fileS.getParentFile();
                            SystemPlayer.getInstanceFX(null).stop();
                            Tools.sleep(100);
                            PrePlayer.getPreInstanceFX(null).stop();
                            Tools.sleep(100);
                            MasterTracks.getInstance().stopAllStream();
                            Tools.sleep(100);
                            listenerTSfxTPfx.stopChTimefx(null);
                            MasterTracks.getInstance().getStreams().stream().forEach((s) -> {
                                s.updateStatus();
                            });
                            if (!fileS.getName().endsWith(".studio")) {
                                fileS = new File(fileS.getParent(), fileS.getName() + ".studio");
                            }
                            try {
                                Studio.save(fileS);
                            } catch (IOException | XMLStreamException | IllegalArgumentException | IllegalAccessException | TransformerException ex) {
                                Logger.getLogger(TruckliststudioUIController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Studio is saved.");
                            ResourceMonitorFXController.getInstance().addMessage(label);
                            Platform.runLater(() -> TS.setTitle("WebcamStudioFX " + Version.version + " (" + fileF.getName() + ")"));
                        }

                        if (saveInfo.isShowing()) {
                            Platform.runLater(() -> saveInfo.close());
                        }
                        return null;
                    }
                };
                Thread th = new Thread(task);
                th.setDaemon(true);
                th.start();

            } else {

                ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Saving Cancelled!");
                ResourceMonitorFXController.getInstance().addMessage(label);
            }
        } catch (HeadlessException ex) {
            Logger.getLogger(TruckliststudioUIController.class.getName()).log(Level.SEVERE, null, ex);
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Error: " + ex.getMessage());
            ResourceMonitorFXController.getInstance().addMessage(label);
        }
    }

    @FXML
    private void btnSysGCAction(ActionEvent event) {
        System.gc();
    }

    @Override
    public void savePreferences() {
        savePrefs();
    }

    @FXML
    private void btnRemoveSourceAction(ActionEvent event) {
        int tabIndex = tabSources.getSelectionModel().getSelectedIndex();
        String name = stringSourceSelected;
        boolean rem = true;

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initOwner(TS);
        Window window = alert.getOwner();
        alert.setX(window.getX() + window.getWidth() / 2 - alert.getWidth() / 2);
        alert.setY(window.getY() + window.getHeight() / 2 - alert.getHeight() / 2);
        alert.setTitle("TS Confirmation");
        alert.setHeaderText("Really remove \"" + name + "\" source?");
        alert.setContentText("Warning: If it's a Video/Music its Track also will be removed !!");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            rem = true;
        } else {
            rem = false;
        }

        if (rem) {

            ArrayList<Stream> streamz = MasterTracks.getInstance().getStreams();
            for (int l = 0; l < streamz.size(); l++) {
                Stream removeS = streamz.get(l);
                if (removeS.getName().equals(name)) {
                    removeS.destroy();
                    removeS = null;
                }
            }

            switch (tabIndex) {
                case 0: {
                    numWebcams -= 1;
                    TitledPane removeTP = null;
                    for (TitledPane tP : webcamDesktop.getPanes()) {
                        if (tP.getText().equals(name)) {
                            removeTP = tP;
                        }
                    }
                    if (numWebcams > 0) {
                        lblWebcam.setText("Webcams(" + numWebcams + ")");
                        webcamDesktop.getPanes().remove(removeTP);
                    } else {
                        numWebcams = 0;
                        lblWebcam.setTextFill(resetTab);
                        lblWebcam.setText("Webcams(" + numWebcams + ")");
                        webcamDesktop.getPanes().remove(removeTP);
                    }
                    break;
                }
                case 1: {
                    numVideos -= 1;
                    TitledPane removeTP = null;
                    for (TitledPane tP : videoDesktop.getPanes()) {
                        if (tP.getText().equals(name)) {
                            removeTP = tP;
                        }
                    }
                    if (numVideos > 0) {
                        lblVideo.setText("Videos(" + numVideos + ")");
                        videoDesktop.getPanes().remove(removeTP);
                    } else {
                        numVideos = 0;
                        lblVideo.setTextFill(resetTab);
                        lblVideo.setText("Videos(" + numVideos + ")");
                        videoDesktop.getPanes().remove(removeTP);
                    }
                    break;
                }
                case 2: {
                    numMusics -= 1;
                    TitledPane removeTP = null;
                    for (TitledPane tP : musicDesktop.getPanes()) {
                        if (tP.getText().equals(name)) {
                            removeTP = tP;
                        }
                    }
                    if (numMusics > 0) {
                        lblMusic.setText("Musics(" + numMusics + ")");
                        musicDesktop.getPanes().remove(removeTP);
                    } else {
                        numMusics = 0;
                        lblMusic.setTextFill(resetTab);
                        lblMusic.setText("Musics(" + numMusics + ")");
                        musicDesktop.getPanes().remove(removeTP);
                    }
                    break;
                }
                case 3: {
                    numPictures -= 1;
                    TitledPane removeTP = null;
                    for (TitledPane tP : pictureDesktop.getPanes()) {
                        if (tP.getText().equals(name)) {
                            removeTP = tP;
                        }
                    }
                    if (numPictures > 0) {
                        lblPicture.setText("Pictures(" + numPictures + ")");
                        pictureDesktop.getPanes().remove(removeTP);
                    } else {
                        numPictures = 0;
                        lblPicture.setTextFill(resetTab);
                        lblPicture.setText("Pictures(" + numPictures + ")");
                        pictureDesktop.getPanes().remove(removeTP);
                    }
                    break;
                }
                case 4: {
                    numTexts -= 1;
                    TitledPane removeTP = null;
                    for (TitledPane tP : textDesktop.getPanes()) {
                        if (tP.getText().equals(name)) {
                            removeTP = tP;
                        }
                    }
                    if (numTexts > 0) {
                        lblText.setText("Texts(" + numTexts + ")");
                        textDesktop.getPanes().remove(removeTP);
                    } else {
                        numTexts = 0;
                        lblText.setTextFill(resetTab);
                        lblText.setText("Texts(" + numTexts + ")");
                        textDesktop.getPanes().remove(removeTP);
                    }
                    break;
                }
                case 5: {
                    if (os == OS.LINUX) {
                        numAudioIns -= 1;
                        TitledPane removeTP = null;
                        for (TitledPane tP : audioInDesktop.getPanes()) {
                            if (tP.getText().equals(name)) {
                                removeTP = tP;
                            }
                        }
                        if (numAudioIns > 0) {
                            lblAudioIn.setText("AudioIns(" + numAudioIns + ")");
                            audioInDesktop.getPanes().remove(removeTP);
                        } else {
                            numAudioIns = 0;
                            lblAudioIn.setTextFill(resetTab);
                            lblAudioIn.setText("AudioIns(" + numAudioIns + ")");
                            audioInDesktop.getPanes().remove(removeTP);
                        }
                        break;
                    } else if (os == OS.WINDOWS) {
                        numDesktops -= 1;
                        TitledPane removeTP = null;
                        for (TitledPane tP : desktopDesktop.getPanes()) {
                            if (tP.getText().equals(name)) {
                                removeTP = tP;
                            }
                        }
                        if (numDesktops > 0) {
                            lblDesktop.setText("Desktops(" + numDesktops + ")");
                            desktopDesktop.getPanes().remove(removeTP);
                        } else {
                            numDesktops = 0;
                            lblDesktop.setTextFill(resetTab);
                            lblDesktop.setText("Desktops(" + numDesktops + ")");
                            desktopDesktop.getPanes().remove(removeTP);
                        }
                        break;
                    }
                }
                case 6: {
                    numDesktops -= 1;
                    TitledPane removeTP = null;
                    for (TitledPane tP : desktopDesktop.getPanes()) {
                        if (tP.getText().equals(name)) {
                            removeTP = tP;
                        }
                    }
                    if (numDesktops > 0) {
                        lblDesktop.setText("Desktops(" + numDesktops + ")");
                        desktopDesktop.getPanes().remove(removeTP);
                    } else {
                        numDesktops = 0;
                        lblDesktop.setTextFill(resetTab);
                        lblDesktop.setText("Desktops(" + numDesktops + ")");
                        desktopDesktop.getPanes().remove(removeTP);
                    }
                    break;
                }
                default:
                    break;
            }
            listenerTSfxTPfx.closeItsTrackfx(name);

            Platform.runLater(() -> {
                controlTab.getTabs().stream().forEach((t) -> {
                    t.getContent().setVisible(false);
                    t.setText("");
                });
                lblSourceSelected.setText("");
            });

//            Alert infoRem = new Alert(AlertType.INFORMATION);
//            infoRem.initOwner(TS);
//            Window infoWindowRem = infoRem.getOwner();
//            infoRem.setX(infoWindowRem.getX() + infoWindowRem.getWidth() / 2 - infoRem.getWidth() / 2);
//            infoRem.setY(infoWindowRem.getY() + infoWindowRem.getHeight() / 2 - infoRem.getHeight() / 2);
//            Platform.runLater(() -> {
//                infoRem.setTitle("TS Information");
//                infoRem.setHeaderText(null);
//                infoRem.setContentText("\"" + name + "\" was removed.");
//                infoRem.initModality(Modality.APPLICATION_MODAL);
//                infoRem.showAndWait();
//            });
        }

    }

    public static void getWebcamParams(Stream stream, VideoDevice d) {
        String infoCmd;
        Runtime rt = Runtime.getRuntime();
        infoCmd = "v4l2-ctl --get-fmt-video --device " + d.getFile();
//        System.out.println("infoCmd: "+infoCmd);
        File fileD = new File(userHomeDir + "/.webcamstudio/" + "dSize.sh");
        FileOutputStream fosD;
        Writer dosD = null;
        try {
            fosD = new FileOutputStream(fileD);
            dosD = new OutputStreamWriter(fosD);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProcessRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (dosD != null) {
                dosD.write("#!/bin/bash\n");
                dosD.write(infoCmd + "\n");
                dosD.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ProcessRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
        fileD.setExecutable(true);
        String batchWCComm = userHomeDir + "/.webcamstudio/" + "dSize.sh";
        try {
            Process infoP = rt.exec(batchWCComm);
            Tools.sleep(10);
            infoP.waitFor(); //Author spoonybard896
            InputStream lsOut = infoP.getInputStream();
            InputStreamReader isr = new InputStreamReader(lsOut);
            BufferedReader in = new BufferedReader(isr);
            String lineR;
            while ((lineR = in.readLine()) != null) {
//                System.out.println("lineR: "+lineR);
                if (lineR.contains("Width")) {
                    lineR = lineR.trim();
                    String[] temp;
                    temp = lineR.split(":");
//                    System.out.println("Split:"+temp[0]+" Split:"+temp[1]);
                    String Res = temp[1].replaceAll(" ", "");
                    String[] wh;
                    wh = Res.split("/");

                    int w = Integer.parseInt(wh[0]);
                    int h = Integer.parseInt(wh[1]);
//                    System.out.println("W:"+w+" H:"+h);
                    int mixerW = MasterMixer.getInstance().getWidth();
                    int mixerH = MasterMixer.getInstance().getHeight();
                    int hAR = (mixerW * h) / w;
                    int wAR = (mixerH * w) / h;
                    if (hAR > mixerH) {
                        hAR = mixerH;
                        int xPos = (mixerW - wAR) / 2;
                        stream.setX(xPos);
                        stream.setWidth(wAR);
                    }
                    if (w > mixerW) {
                        int yPos = (mixerH - hAR) / 2;
                        stream.setY(yPos);
                        stream.setHeight(hAR);
                    } else if (h < mixerH) {
                        int yPos = (mixerH - hAR) / 2;
                        stream.setY(yPos);
                    } else {
                        hAR = mixerH;
                    }
                    stream.setHeight(hAR);
                }
            }
        } catch (IOException | InterruptedException | NumberFormatException e) {
        }
    }

    @FXML
    private void btnAddWebcamsAction(ActionEvent event) throws IOException {
        final String wCam = cboWebcam.getSelectionModel().getSelectedItem().toString();
        if (Tools.getOS() == OS.LINUX) {
            for (VideoDevice d : VideoDevice.getOutputDevices()) {
                if (d.getName().equals(wCam)) {
                    Stream webcam = new SourceWebcam(d.getFile());
                    stream_ = webcam;
                    webcam.setName(d.getName());
                    ArrayList<String> allChan = new ArrayList<>();
                    for (String scn : MasterTracks.getInstance().getTracks()) {
                        allChan.add(scn);
                    }
                    for (String sc : allChan) {
                        webcam.addTrack(SourceTrack.getTrack(sc, webcam));
                    }
                    getWebcamParams(webcam, d);

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/webcamstudiofx/StreamDesktopFX.fxml"));
                    TitledPane streamDesktopFX = (TitledPane) fxmlLoader.load();
                    StreamDesktopFXController controller = fxmlLoader.<StreamDesktopFXController>getController();
                    controller.setListener(truckliststudiouicontroller);
                    streamDesktopFX.setText(webcam.getName());
                    streamDesktopFX.setMaxWidth(Double.MAX_VALUE);
                    AnchorPane.setTopAnchor(streamDesktopFX, 0.0);
                    AnchorPane.setBottomAnchor(streamDesktopFX, 0.0);
                    AnchorPane.setLeftAnchor(streamDesktopFX, 0.0);
                    AnchorPane.setRightAnchor(streamDesktopFX, 0.0);
                    numWebcams += 1;
                    lblWebcam.setTextFill(busyTab);
                    Platform.runLater(() -> {
                        webcamDesktop.getPanes().add(streamDesktopFX);
                        lblWebcam.setText("Webcams(" + numWebcams + ")");
                    });
//                    if (autoTrack) {
//                        TrackPanelFXController.makeATrack(webcam);
//                    }
//                    StreamDesktop frame = new StreamDesktop(webcam, this);
//                    numCameras += 1;
//                    cameraDesktop.add(frame, javax.swing.JLayeredPane.DEFAULT_LAYER);
//                    lblCamera.setForeground(busyTab);
//                    Font font = new Font("Ubuntu", Font.BOLD, 11);
//                    lblCamera.setFont(font);
//                    lblCamera.setText("Cameras("+numCameras+")");
//                    try {
//                        frame.setSelected(true);
//                    } catch (PropertyVetoException ex) {
//                        Logger.getLogger(WebcamStudio.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                }
            }
        }
    }

    @FXML
    private void btnAddDesktopAction(ActionEvent event) throws IOException {
        SourceDesktop streamDesk;
        streamDesk = new SourceDesktop();
        stream_ = streamDesk;
        ArrayList<String> allChan = new ArrayList<>();
        for (String scn : MasterTracks.getInstance().getTracks()) {
            allChan.add(scn);
        }
        for (String sc : allChan) {
            streamDesk.addTrack(SourceTrack.getTrack(sc, streamDesk));
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/webcamstudiofx/StreamDesktopFX.fxml"));
        TitledPane streamDesktopFX = (TitledPane) fxmlLoader.load();
        StreamDesktopFXController controller = fxmlLoader.<StreamDesktopFXController>getController();
        controller.setListener(truckliststudiouicontroller);
        streamDesktopFX.setText(streamDesk.getName());
        streamDesktopFX.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setTopAnchor(streamDesktopFX, 0.0);
        AnchorPane.setBottomAnchor(streamDesktopFX, 0.0);
        AnchorPane.setLeftAnchor(streamDesktopFX, 0.0);
        AnchorPane.setRightAnchor(streamDesktopFX, 0.0);
        numDesktops += 1;
        lblDesktop.setTextFill(busyTab);
        Platform.runLater(() -> {
            desktopDesktop.getPanes().add(streamDesktopFX);
            lblDesktop.setText("Desktops(" + numDesktops + ")");
        });
//        StreamDesktop frame = new StreamDesktop(streamDesk, this);
//        numDesktops += 1;
//        desktopDesktop.add(frame, javax.swing.JLayeredPane.DEFAULT_LAYER);
//        lblDesktop.setForeground(busyTab);
//        Font font = new Font("Ubuntu", Font.BOLD, 11);
//        lblDesktop.setFont(font);
//        lblDesktop.setText("Desktops("+numDesktops+")");
//        try {
//            frame.setSelected(true);
//        } catch (PropertyVetoException ex) {
//            Logger.getLogger(WebcamStudio.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public interface Listener {

        public void stopChTimefx(java.awt.event.ActionEvent evt);

        public void resetBtnStatesfx();

        public void resetAutoPLBtnStatefx(java.awt.event.ActionEvent evt); // not used

        public void resetSinksfx(java.awt.event.ActionEvent evt);

        public void addLoadingTrackfx(String name);

        public void removeTracksfx(String removeSc, int a);

        public void setRemoteOnfx();

        public void closeItsTrackfx(String name);
    }
    static Listener listenerTSfxTPfx = null;

    public static void setListenerTSfxTPfx(Listener l) {
        listenerTSfxTPfx = l;
    }

    static Listener listenerOPfx = null;

    public static void setListenerOPfx(Listener l) {
        listenerOPfx = l;
    }

    static Listener listenerControlfx = null;

    public static void setListenerControlfx(Listener l) {
        listenerControlfx = l;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        File userSettings = new File(userHomeDir + "/.webcamstudiofx");
        if (!userSettings.exists()) {
            System.out.println("No Home folder");

        } else {
            System.out.println("Home folder");
        }
        lblSourceSelected.setTextFill(selectedText);
        tglAutoAR.getStylesheets().add(getClass().getResource(
                "/webcamstudiofx/imagetoggleAR.css"
        ).toExternalForm());
        tglAutoTrack.getStylesheets().add(getClass().getResource(
                "/webcamstudiofx/imagetoggleAutoTrack.css"
        ).toExternalForm());
        truckliststudiouicontroller = this;
        cboAnimations_ = cboAnimations;
        listenerTSfxTSContr(this);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/webcamstudiofx/OutputPanelFX.fxml"));
            outputPanelFX = (AnchorPane) loader.load();
            recorderFX = loader.getController();
            trackPanelFX = (AnchorPane) FXMLLoader.load(getClass().getResource("/webcamstudiofx/TrackPanelFX.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(TruckliststudioUIController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tabSources.getTabs().get(0).setGraphic(lblWebcam);
        tabSources.getTabs().get(1).setGraphic(lblVideo);
        tabSources.getTabs().get(2).setGraphic(lblMusic);
        tabSources.getTabs().get(3).setGraphic(lblPicture);
        tabSources.getTabs().get(4).setGraphic(lblText);
        tabSources.getTabs().get(5).setGraphic(lblAudioIn);
        tabSources.getTabs().get(6).setGraphic(lblDesktop);

        AnchorPane.setTopAnchor(trackPanelFX, 0.0);
        AnchorPane.setBottomAnchor(trackPanelFX, 0.0);
        AnchorPane.setLeftAnchor(trackPanelFX, 0.0);
        AnchorPane.setRightAnchor(trackPanelFX, 0.0);
        bottomLPane.getChildren().add(trackPanelFX);

        AnchorPane.setTopAnchor(outputPanelFX, 0.0);
        AnchorPane.setBottomAnchor(outputPanelFX, 0.0);
        AnchorPane.setLeftAnchor(outputPanelFX, 0.0);
        AnchorPane.setRightAnchor(outputPanelFX, 0.0);
        bottomRPane.getChildren().add(outputPanelFX);

        mainHorizontalSplit.setDividerPositions(new double[]{0.215});

        // Fix Split Divider
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
            @Override
            public void run() {
                Set<Node> horDividers = mainHorizontalSplit.lookupAll(".split-pane-divider");
                int i = 0;
                for (Iterator<Node> it = horDividers.iterator(); it.hasNext();) {
                    Node f = it.next();
                    if (i == 1) {
                        f.mouseTransparentProperty().setValue(Boolean.TRUE);
                    }
                    i++;
                }
            }
        },
                2000
        );

        prefs = Preferences.userNodeForPackage(this.getClass());

        if (prefs.getBoolean("resetprefs", resetPrefs)) {
            resetPrefs = false;
            prefs.putBoolean("resetprefs", resetPrefs);
        } else {
            loadPrefs();
        }

        MasterMixer.getInstance().start();
        PreviewMixer.getInstance().start();
        try {
            mainPanelFX = (AnchorPane) FXMLLoader.load(getClass().getResource("/webcamstudiofx/MainPanelFX.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(TruckliststudioUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        AnchorPane.setTopAnchor(mainPanelFX, 0.0);
        AnchorPane.setBottomAnchor(mainPanelFX, 0.0);
        AnchorPane.setLeftAnchor(mainPanelFX, 0.0);
        AnchorPane.setRightAnchor(mainPanelFX, 0.0);
        topLPane.getChildren().add(mainPanelFX);

        if (os == OS.WINDOWS) {
            checkWinBits();
            checkWinGS();
            tabSources.getTabs().remove(4);
            tglAVconv.setDisable(true);
        }

        initAnimations();
        initAudioMainSW();
        initThemeMainSW();
        initMainOutBE();
        initWebcam();

        tglAutoAR.setSelected(autoAR);
        tglAutoTrack.setSelected(autoTrack);
        if (cmdFile != null) {
            try {
                loadAtStart(cmdFile, null);
//                btnMinimizeTabActionPerformed(null);
            } catch (IOException ex) {
                Logger.getLogger(TruckliststudioUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (!loadingFinish) {
                sleep(1000);
            }
        }
        if (cmdOut != null) {

            listenerOPfx.addLoadingTrackfx(cmdOut);

            // used addLoadingTrack to activate Output from command line.
        }
        if (cmdAutoStart) {
            if (loadingFinish) {
                listenerTSfxTPfx.resetSinksfx(null);
            }
            // used resetSinksfx to activate Autostart from command line.
        }
        if (cmdRemote) {
            listenerTSfxTPfx.setRemoteOnfx();
        }

    }

    @SuppressWarnings("unchecked")
    private void initWebcam() {
        ObservableList<Object> options = FXCollections.observableArrayList();
        if (Tools.getOS() == OS.LINUX) {
            for (VideoDevice d : VideoDevice.getOutputDevices()) {
                options.add(d.getName());
            }
        }
        cboWebcam.setItems(options);
        cboWebcam.getSelectionModel().select(0);
    }

    @SuppressWarnings("unchecked")
    private void initAudioMainSW() {
        ObservableList<String> aFreq = FXCollections.observableArrayList();
        aFreq.add("22050Hz");
        aFreq.add("44100Hz");
        cboAudioHz.setItems(aFreq);
        if (audioFreq == 22050) {
            cboAudioHz.getSelectionModel().select("22050Hz");
        } else {
            cboAudioHz.getSelectionModel().select("44100Hz");
        }
    }

    @SuppressWarnings("unchecked")
    private void initThemeMainSW() {
        ObservableList<String> themes = FXCollections.observableArrayList();
        themes.add("Modena");
        themes.add("Caspian");
        cboTheme.setItems(themes);
        if (theme.equals("Modena")) {
            cboTheme.getSelectionModel().select("Modena");
            setUserAgentStylesheet(STYLESHEET_MODENA);
        } else {
            cboTheme.getSelectionModel().select("Caspian");
            setUserAgentStylesheet(STYLESHEET_CASPIAN);
        }
    }

    @SuppressWarnings("unchecked")
    private void initAnimations() {
        try {
            animations.load(getClass().getResourceAsStream("/webcamstudiofx/resources/animations/animations.properties"));
            ObservableList<Object> options = FXCollections.observableArrayList();
            animations.keySet().stream().forEach((o) -> {
                options.add(o);
            });
            cboAnimations.setItems(options);
            cboAnimations.getSelectionModel().select(0);
        } catch (IOException ex) {
            Logger.getLogger(TruckliststudioUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void exitApplication(boolean close) {
        if (close) {
            SystemPlayer.getInstanceFX(null).stop();
            Tools.sleep(10);
            PrePlayer.getPreInstanceFX(null).stop();
            Tools.sleep(10);
            MasterTracks.getInstance().endAllStream();
            Tools.sleep(10);
            MasterMixer.getInstance().stop();
            PreviewMixer.getInstance().stop();
            Tools.sleep(100);
            System.out.println("Cleaning up ...");
            File directory = new File(userHomeDir + "/.webcamstudiofx");
            for (File f : directory.listFiles()) {
                if (f.getName().startsWith("WSU") || f.getName().startsWith("WSC")) {
                    f.delete();
                }
            }
            System.out.println("Thanks for using WebcamStudioFX ...");
            System.out.println("GoodBye!");
            Platform.exit();
            System.exit(0);
        } else {
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Quit Cancelled.");
            ResourceMonitorFXController.getInstance().addMessage(label);
        }
    }

    @FXML
    private void btnAddFolderAction(ActionEvent event) throws IOException {

        final ArrayList<String> allStreams = new ArrayList<>();
        MasterTracks.getInstance().getStreams().stream().filter((str) -> (!str.toString().toLowerCase().contains("sink"))).forEach((str) -> {
            allStreams.add(str.getName());
        });

        DirectoryChooser chooser = new DirectoryChooser();
        if (lastFolder.exists()) {
            chooser.setInitialDirectory(lastFolder);
        }
        chooser.setTitle("Add Media folder ...");
        Stage primaryStage = new Stage();
        final File dir = chooser.showDialog(primaryStage);

        if (dir != null) {

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(TS);
            Window window = alert.getOwner();
            alert.setX(window.getX() + window.getWidth() / 2 - alert.getWidth() / 2);
            alert.setY(window.getY() + window.getHeight() / 2 - alert.getHeight() / 2);
            Platform.runLater(() -> {
                alert.setTitle("TS Information");
                alert.setHeaderText(null);
                alert.setContentText("Importing Folder, please wait ...");
                alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            });

            Task task = new Task<Void>() {
                @Override
                public Void call() throws IOException {

                    lastFolder = dir.getAbsoluteFile();
                    File[] contents = dir.listFiles();
//                        for ( File f : contents) {
//                            String fileName = f.getName();
//                            System.out.println("Name: " + fileName);
//                        }
                    ArrayList<Stream> tracks = new ArrayList<>();
                    for (File file : contents) {
                        Stream s = Stream.getInstance(file);
                        stream_ = s;
                        boolean noError = true;
                        boolean noDouble = true;

                        if (s != null) {
                            boolean isMovie = s instanceof SourceMovie;
                            boolean isMusic = s instanceof SourceMusic;
                            if (isMovie || isMusic) {
                                getStreamParams(s, file, null);
                                if (s.getStreamTime().equals("N/A")) {
                                    noError = false;
                                    s.destroy();
                                }
//                                    System.out.println("StreamName="+s.getName());
                                for (String str : allStreams) {
//                                        System.out.println("ComparedStreamName="+str);
                                    if (s.getName().equals(str)) {
//                                            System.out.println("Double Stream !!!");
                                        noDouble = false;
                                        s.destroy();
                                    }
                                }
                                if (noError && noDouble) {
                                    if (s instanceof SourceMovie) {
                                        try {
                                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/webcamstudiofx/StreamDesktopFX.fxml"));
                                            TitledPane streamDesktopFX = (TitledPane) fxmlLoader.load();
                                            StreamDesktopFXController controller = fxmlLoader.<StreamDesktopFXController>getController();
                                            controller.setListener(truckliststudiouicontroller);
                                            streamDesktopFX.setText(s.getName());
                                            streamDesktopFX.setMaxWidth(Double.MAX_VALUE);
                                            AnchorPane.setTopAnchor(streamDesktopFX, 0.0);
                                            AnchorPane.setBottomAnchor(streamDesktopFX, 0.0);
                                            AnchorPane.setLeftAnchor(streamDesktopFX, 0.0);
                                            AnchorPane.setRightAnchor(streamDesktopFX, 0.0);
                                            numVideos += 1;
                                            lblVideo.setTextFill(busyTab);
                                            Platform.runLater(() -> {
                                                videoDesktop.getPanes().add(streamDesktopFX);
                                                lblVideo.setText("Videos(" + numVideos + ")");
                                            });
                                        } catch (IOException ex) {
                                            Logger.getLogger(TruckliststudioUIController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    } else if (s instanceof SourceMusic) {
                                        try {
                                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/webcamstudiofx/StreamDesktopFX.fxml"));
                                            TitledPane streamDesktopFX = (TitledPane) fxmlLoader.load();
                                            StreamDesktopFXController controller = fxmlLoader.<StreamDesktopFXController>getController();
                                            controller.setListener(truckliststudiouicontroller);
                                            streamDesktopFX.setText(s.getName());
                                            streamDesktopFX.setMaxWidth(Double.MAX_VALUE);
                                            AnchorPane.setTopAnchor(streamDesktopFX, 0.0);
                                            AnchorPane.setBottomAnchor(streamDesktopFX, 0.0);
                                            AnchorPane.setLeftAnchor(streamDesktopFX, 0.0);
                                            AnchorPane.setRightAnchor(streamDesktopFX, 0.0);
                                            numMusics += 1;
                                            lblMusic.setTextFill(busyTab);
                                            Platform.runLater(() -> {
                                                musicDesktop.getPanes().add(streamDesktopFX);
                                                lblMusic.setText("Musics(" + numMusics + ")");
                                            });
                                        } catch (IOException ex) {
                                            Logger.getLogger(TruckliststudioUIController.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    }
                                    if (autoTrack) {
//                                        TrackPanelFXController.makeATrack(s);
                                        tracks.add(s);
                                    }
                                } else {
                                    if (!noError) {
                                        ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Error adding " + file.getName() + "!");
                                        ResourceMonitorFXController.getInstance().addMessage(label);
                                    }
                                    if (!noDouble) {
                                        ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, file.getName() + " Duplicated!");
                                        ResourceMonitorFXController.getInstance().addMessage(label);
                                    }
                                }
                            } else {
                                System.out.println("Not a Movie/Music !!!");
                                s.destroy();
                            }
                        }
                    }
                    TrackPanelFXController.makeMultipleTracks(tracks);
                    Tools.sleep(500);
                    MasterTracks master = MasterTracks.getInstance(); //
                    ArrayList<String> allTrack = new ArrayList<>();
                    master.getInstance().getTracks().stream().forEach((scn) -> {
                        allTrack.add(scn);
                    });
                    allTrack.stream().forEach((track) -> {
                        master.insertStudio(track);
                    });
                    master.getStreams().stream().filter((s) -> (s.getisATrack())).forEach((s) -> {
                        int i = 0;
                        for (String track : allTrack) {
                            if (s.getTrkName().equals(track)) {
                                boolean backState = false;
                                if (s.isPlaying()) {
                                    backState = true;
                                }
                                s.setIsPlaying(true);
                                s.addTrackAt(SourceTrack.getTrack(allTrack.get(i), s), i);
                                if (backState) {
                                    s.setIsPlaying(true);
                                } else {
                                    s.setIsPlaying(false);
                                }
                            } else {
                                s.addTrackAt(SourceTrack.getTrackIgnorePlay(allTrack.get(i), s), i);
                            }
                            i++;
                        }
                    });
                    stream_ = null;
                    ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Folder Import is Complete.");
                    ResourceMonitorFXController.getInstance().addMessage(label);
                    if (alert.isShowing()) {
                        Platform.runLater(() -> alert.close());
                    }
                    return null;
                }
            };

            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();

        } else {
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "No Folder Selected!");
            ResourceMonitorFXController.getInstance().addMessage(label);
        }
    }

    @FXML
    private void btnAddTextAction(ActionEvent event) throws IOException {
        SourceText streamTXT;
        streamTXT = new SourceText("ts");
        stream_ = streamTXT;
        ArrayList<String> allChan = new ArrayList<>();
        MasterTracks.getInstance().getTracks().stream().forEach((scn) -> {
            allChan.add(scn);
        });
        allChan.stream().forEach((sc) -> {
            streamTXT.addTrack(SourceTrack.getTrack(sc, streamTXT));
        });
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/webcamstudiofx/StreamDesktopFX.fxml"));
        TitledPane streamDesktopFX = (TitledPane) fxmlLoader.load();
        StreamDesktopFXController controller = fxmlLoader.<StreamDesktopFXController>getController();
        controller.setListener(truckliststudiouicontroller);

        streamDesktopFX.setText(streamTXT.getName());
        streamDesktopFX.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setTopAnchor(streamDesktopFX, 0.0);
        AnchorPane.setBottomAnchor(streamDesktopFX, 0.0);
        AnchorPane.setLeftAnchor(streamDesktopFX, 0.0);
        AnchorPane.setRightAnchor(streamDesktopFX, 0.0);

        Platform.runLater(() -> {
            textDesktop.getPanes().add(streamDesktopFX);
        });

        numTexts += 1;
        lblText.setTextFill(busyTab);

        Platform.runLater(() -> {
            lblText.setText("Texts(" + numTexts + ")");
        });
    }

    @FXML
    private void tglAutoARAction(ActionEvent event) {
        if (tglAutoAR.isSelected()) {
            autoAR = true;
        } else {
            autoAR = false;
        }
    }

    @FXML
    private void tglAutoTrackAction(ActionEvent event) {
        if (tglAutoTrack.isSelected()) {
            autoTrack = true;
        } else {
            autoTrack = false;
        }
    }

    @FXML
    private void btnAddFileAction(ActionEvent event) throws IOException {
        final ArrayList<String> allStreams = new ArrayList<>();
        MasterTracks.getInstance().getStreams().stream().filter((str) -> (!str.toString().toLowerCase().contains("sink"))).forEach((str) -> {
            allStreams.add(str.getName());
        });
        FileChooser chooser = new FileChooser();
        if (lastFolder.exists()) {
            chooser.setInitialDirectory(lastFolder);
        }
        ExtensionFilter mediaFilter = new ExtensionFilter("Supported Media files (*.media)", "*.avi", "*.ogg", "*.jpeg", "*.ogv", "*.mp4", "*.m4v", "*.mpg", "*.divx", "*.wmv", "*.flv", "*.mov", "*.mkv", "*.vob", "*.jpg", "*.bmp", "*.png", "*.gif", "*.mp3", "*.wav", "*.wma", "*.m4a", "*.mp2");
        ExtensionFilter allFilesFilter = new ExtensionFilter("All files (*.*)", "*.*", "*.*");
        chooser.getExtensionFilters().add(mediaFilter);
        chooser.getExtensionFilters().add(allFilesFilter);
        chooser.setSelectedExtensionFilter(mediaFilter);
        chooser.setTitle("Add Media file ...");
        Stage primaryStage = new Stage();

        final File file = chooser.showOpenDialog(primaryStage);

        if (file != null) {
            lastFolder = file.getParentFile();

        }
        if (file != null) {
            Stream s = Stream.getInstance(file);
            stream_ = s;
            boolean noError = true;
            boolean noDouble = true;
            if (s != null) {
                getStreamParams(s, file, null);
//                    System.out.println("IsOnlyAudio="+s.isOnlyAudio());
//                    System.out.println("IsOnlyVideo="+s.isOnlyVideo());
                if (s.getStreamTime().equals("N/A") && (s instanceof SourceMovie || s instanceof SourceMusic)) {
                    noError = false;
                    s.destroy();
                }
                for (String str : allStreams) {
                    if (s.getName().equals(str)) {
                        noDouble = false;
                        s.destroy();
                    }
                }
                if (noError && noDouble) {
                    ArrayList<String> allChan = new ArrayList<>();
                    MasterTracks.getInstance().getTracks().stream().forEach((scn) -> {
                        allChan.add(scn);
                    });
                    allChan.stream().forEach((sc) -> {
                        s.addTrack(SourceTrack.getTrack(sc, s));
                    });

                    if (s instanceof SourceMovie) {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/webcamstudiofx/StreamDesktopFX.fxml"));
                        TitledPane streamDesktopFX = (TitledPane) fxmlLoader.load();
                        StreamDesktopFXController controller = fxmlLoader.<StreamDesktopFXController>getController();
                        controller.setListener(truckliststudiouicontroller);
                        streamDesktopFX.setText(s.getName());
                        streamDesktopFX.setMaxWidth(Double.MAX_VALUE);
                        AnchorPane.setTopAnchor(streamDesktopFX, 0.0);
                        AnchorPane.setBottomAnchor(streamDesktopFX, 0.0);
                        AnchorPane.setLeftAnchor(streamDesktopFX, 0.0);
                        AnchorPane.setRightAnchor(streamDesktopFX, 0.0);
                        Platform.runLater(() -> {
                            videoDesktop.getPanes().add(streamDesktopFX);
                        });
                        numVideos += 1;
                        lblVideo.setTextFill(busyTab);
                        Platform.runLater(() -> {
                            lblVideo.setText("Videos(" + numVideos + ")");
                        });

                    } else if (s instanceof SourceMusic) {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/webcamstudiofx/StreamDesktopFX.fxml"));
                        TitledPane streamDesktopFX = (TitledPane) fxmlLoader.load();
                        StreamDesktopFXController controller = fxmlLoader.<StreamDesktopFXController>getController();
                        controller.setListener(truckliststudiouicontroller);
                        streamDesktopFX.setText(s.getName());
                        streamDesktopFX.setMaxWidth(Double.MAX_VALUE);
                        AnchorPane.setTopAnchor(streamDesktopFX, 0.0);
                        AnchorPane.setBottomAnchor(streamDesktopFX, 0.0);
                        AnchorPane.setLeftAnchor(streamDesktopFX, 0.0);
                        AnchorPane.setRightAnchor(streamDesktopFX, 0.0);
                        Platform.runLater(() -> {
                            musicDesktop.getPanes().add(streamDesktopFX);
                        });
                        numMusics += 1;
                        lblMusic.setTextFill(busyTab);
                        Platform.runLater(() -> {
                            lblMusic.setText("Musics(" + numMusics + ")");
                        });

                    } else if (s instanceof SourceImage || s instanceof SourceImageGif) { //|| s instanceof SourceImageU

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/webcamstudiofx/StreamDesktopFX.fxml"));
                        TitledPane streamDesktopFX = (TitledPane) fxmlLoader.load();
                        StreamDesktopFXController controller = fxmlLoader.<StreamDesktopFXController>getController();
                        controller.setListener(truckliststudiouicontroller);
                        streamDesktopFX.setText(s.getName());
                        streamDesktopFX.setMaxWidth(Double.MAX_VALUE);
                        AnchorPane.setTopAnchor(streamDesktopFX, 0.0);
                        AnchorPane.setBottomAnchor(streamDesktopFX, 0.0);
                        AnchorPane.setLeftAnchor(streamDesktopFX, 0.0);
                        AnchorPane.setRightAnchor(streamDesktopFX, 0.0);
                        Platform.runLater(() -> {
                            pictureDesktop.getPanes().add(streamDesktopFX);
                        });
                        numPictures += 1;
                        lblPicture.setTextFill(busyTab);
                        Platform.runLater(() -> {
                            lblPicture.setText("Pictures(" + numPictures + ")");
                        });
                    }

                    if (autoTrack) {
                        if (s instanceof SourceMovie || s instanceof SourceMusic) {
                            TrackPanelFXController.makeATrack(s);
                        }
                    }
                } else {
                    if (!noError) {
                        ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Error adding " + file.getName() + "!");
                        ResourceMonitorFXController.getInstance().addMessage(label);
                    }
                    if (!noDouble) {
                        ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, file.getName() + " Duplicated!");
                        ResourceMonitorFXController.getInstance().addMessage(label);
                    }
                }
            }
            stream_ = null;
        } else {
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "No File Selected!");
            ResourceMonitorFXController.getInstance().addMessage(label);
        }
    }

    @FXML
    private void btnResetPrefsAction() {
        resetPrefs = true;
        prefs.putBoolean("resetprefs", resetPrefs);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(TS);
        Window window = alert.getOwner();
        alert.setX(window.getX() + window.getWidth() / 2 - alert.getWidth() / 2);
        alert.setY(window.getY() + window.getHeight() / 2 - alert.getHeight() / 2);
        alert.setTitle("TS Information");
        alert.setHeaderText(null);
        alert.setContentText("You need to restart WebcamStudioFX for the changes to take effect.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    @FXML
    private void btnAddAudioAction(ActionEvent event) throws IOException {
        SourceAudioSource source = new SourceAudioSource();
        ArrayList<String> allChan = new ArrayList<>();
        MasterTracks.getInstance().getTracks().stream().forEach((scn) -> {
            allChan.add(scn);
        });
        allChan.stream().forEach((sc) -> {
            source.addTrack(SourceTrack.getTrack(sc, source));
        });
        stream_ = source;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/webcamstudiofx/StreamDesktopFX.fxml"));
        TitledPane streamDesktopFX = (TitledPane) fxmlLoader.load();
        StreamDesktopFXController controller = fxmlLoader.<StreamDesktopFXController>getController();
        controller.setListener(truckliststudiouicontroller);
        streamDesktopFX.setText(source.getName());
        streamDesktopFX.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setTopAnchor(streamDesktopFX, 0.0);
        AnchorPane.setBottomAnchor(streamDesktopFX, 0.0);
        AnchorPane.setLeftAnchor(streamDesktopFX, 0.0);
        AnchorPane.setRightAnchor(streamDesktopFX, 0.0);

        Platform.runLater(() -> {
            audioInDesktop.getPanes().add(streamDesktopFX);
        });

        numAudioIns += 1;
        lblAudioIn.setTextFill(busyTab);

        Platform.runLater(() -> {
            lblAudioIn.setText("AudioIns(" + numAudioIns + ")");
        });
    }

    @FXML
    private void btnAddAnimationAction(ActionEvent event) throws IOException {
        try {
            String key = cboAnimations.getSelectionModel().getSelectedItem().toString();
            String res = animations.getProperty(key);
            URL url = getClass().getResource("/webcamstudiofx/resources/animations/" + res);
            Stream streamAnm;
            streamAnm = new SourceImageGif(key, url);
            BufferedImage gifImage = ImageIO.read(url);
            getStreamParams(streamAnm, null, gifImage);
            ArrayList<String> allChan = new ArrayList<>();
            MasterTracks.getInstance().getTracks().stream().forEach((scn) -> {
                allChan.add(scn);
            });
            allChan.stream().forEach((sc) -> {
                streamAnm.addTrack(SourceTrack.getTrack(sc, streamAnm));
            });

            stream_ = streamAnm;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/webcamstudiofx/StreamDesktopFX.fxml"));
            TitledPane streamDesktopFX = (TitledPane) fxmlLoader.load();
            StreamDesktopFXController controller = fxmlLoader.<StreamDesktopFXController>getController();
            controller.setListener(truckliststudiouicontroller);

            streamDesktopFX.setText(streamAnm.getName());
            streamDesktopFX.setMaxWidth(Double.MAX_VALUE);
            AnchorPane.setTopAnchor(streamDesktopFX, 0.0);
            AnchorPane.setBottomAnchor(streamDesktopFX, 0.0);
            AnchorPane.setLeftAnchor(streamDesktopFX, 0.0);
            AnchorPane.setRightAnchor(streamDesktopFX, 0.0);

            Platform.runLater(() -> {
                pictureDesktop.getPanes().add(streamDesktopFX);
            });
            numPictures += 1;
            lblPicture.setTextFill(busyTab);
            lblPicture.setText("Pictures(" + numPictures + ")");
        } catch (IOException ex) {
            Logger.getLogger(TruckliststudioUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnAboutAction(ActionEvent event) throws IOException {
        Stage aboutStage = new Stage();
        AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/webcamstudiofx/aboutTS.fxml"));
        Scene scene = new Scene(root);
        aboutStage.setScene(scene);

        aboutStage.initOwner(TS);
        Window window = aboutStage.getOwner();
        aboutStage.setX(window.getX() + window.getWidth() / 2 - 100);
        aboutStage.setY(window.getY() + window.getHeight() / 2 - 100);

        aboutStage.show();
    }

    public static void getStreamParams(Stream stream, File file, BufferedImage image) {
        String fileType = "not";
        if (stream instanceof SourceMovie) {
            fileType = "mov";
        } else if (stream instanceof SourceMusic) {
            fileType = "mus";
        } else if (stream instanceof SourceImage || stream instanceof SourceImageGif) { //stream instanceof SourceImageU ||
            fileType = "pic";
        }
        if (image != null) {
            if (autoAR) {
                int w = image.getWidth();
                int h = image.getHeight();
                int mixerW = MasterMixer.getInstance().getWidth();
                int mixerH = MasterMixer.getInstance().getHeight();
                int hAR = (mixerW * h) / w;
                int wAR = (mixerH * w) / h;
                if (hAR > mixerH) {
                    hAR = mixerH;
                    int xPos = (mixerW - wAR) / 2;
                    stream.setX(xPos);
                    stream.setWidth(wAR);
                }
                if (w > mixerW) {
                    int yPos = (mixerH - hAR) / 2;
                    stream.setY(yPos);
                    stream.setHeight(hAR);
                } else if (h < mixerH) {
                    int yPos = (mixerH - hAR) / 2;
                    stream.setY(yPos);
                } else {
                    hAR = mixerH;
                }
                stream.setHeight(hAR);
            }
        } else {
            String infoCmd;
            File fileD;
            String batchDurationComm;
            Runtime rt = Runtime.getRuntime();
            if (wsDistroWatch().toLowerCase().equals("windows")) {
                infoCmd = "ffmpeg -i " + "\"" + file.getAbsolutePath() + "\"";
                fileD = new File(userHomeDir + "/.webcamstudiofx/" + "DCalc.bat");
                FileOutputStream fosD;
                Writer dosD = null;
                try {
                    fosD = new FileOutputStream(fileD);
                    dosD = new OutputStreamWriter(fosD);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ProcessRenderer.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if (dosD != null) {
                        dosD.write(infoCmd + "\n");
                        dosD.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ProcessRenderer.class.getName()).log(Level.SEVERE, null, ex);
                }
                fileD.setExecutable(true);
                batchDurationComm = userHomeDir + "/.webcamstudiofx/" + "DCalc.bat";
            } else {
                if (BackEnd.avconvDetected()) {
                    infoCmd = "avconv -i " + "\"" + file.getAbsolutePath() + "\"";
                } else {
                    infoCmd = "ffmpeg -i " + "\"" + file.getAbsolutePath() + "\"";
                }
                fileD = new File(userHomeDir + "/.webcamstudiofx/" + "DCalc.sh");
                FileOutputStream fosD;
                Writer dosD = null;
                try {
                    fosD = new FileOutputStream(fileD);
                    dosD = new OutputStreamWriter(fosD);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ProcessRenderer.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if (dosD != null) {
                        dosD.write("#!/bin/bash\n");
                        dosD.write(infoCmd + "\n");
                        dosD.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ProcessRenderer.class.getName()).log(Level.SEVERE, null, ex);
                }
                fileD.setExecutable(true);
                batchDurationComm = userHomeDir + "/.webcamstudiofx/" + "DCalc.sh";
            }
//            System.out.println(infoCmd);

            try {
                Process duration = rt.exec(batchDurationComm);
                boolean audiofind = false;
                Tools.sleep(10);
                duration.waitFor(); //Author spoonybard896
                InputStream lsOut = duration.getErrorStream();
                InputStreamReader isr = new InputStreamReader(lsOut);
                BufferedReader in = new BufferedReader(isr);
                String lineR;

                while ((lineR = in.readLine()) != null) {
                    if (lineR.contains("Duration:") && !fileType.equals("pic")) {
                        lineR = lineR.replaceFirst("Duration: ", "");
                        lineR = lineR.trim();
                        String resu = lineR.substring(0, 8);
                        String[] temp;
                        temp = resu.split(":");
                        int hours = Integer.parseInt(temp[0]);
                        int minutes = Integer.parseInt(temp[1]);
                        int seconds = Integer.parseInt(temp[2]);
                        int totalTime = hours * 3600 + minutes * 60 + seconds;
                        String strDuration = Integer.toString(totalTime);
                        stream.setStreamTime(strDuration + "s");
                    }

                    if (lineR.contains("Audio:")) {
                        if (lineR.contains("0 channels")) {
                            audiofind = false;
                        } else {
                            audiofind = true;
                        }
                    }

                    if (autoAR && !fileType.equals("mus")) {
                        if (lineR.contains("Video:")) {
                            if (lineR.toLowerCase().contains("could not find")) {
                                System.out.println("Cannot get Video parameters from this Source !!!");
                            } else {
                                String[] lineRParts = lineR.split(",");
                                String[] tempNativeSize = lineRParts[2].split(" ");
                                String[] videoNativeSize = tempNativeSize[1].split("x");
                                int w = Integer.parseInt(videoNativeSize[0]);
                                int h = Integer.parseInt(videoNativeSize[1]);
                                int mixerW = MasterMixer.getInstance().getWidth();
                                int mixerH = MasterMixer.getInstance().getHeight();
                                int hAR = (mixerW * h) / w;
                                int wAR = (mixerH * w) / h;
                                if (hAR > mixerH) {
                                    hAR = mixerH;
                                    int xPos = (mixerW - wAR) / 2;
                                    stream.setX(xPos);
                                    stream.setWidth(wAR);
                                }
                                if (w > mixerW) {
                                    int yPos = (mixerH - hAR) / 2;
                                    stream.setY(yPos);
                                    stream.setHeight(hAR);
                                } else if (h < mixerH) {
                                    int yPos = (mixerH - hAR) / 2;
                                    stream.setY(yPos);
                                } else {
                                    hAR = mixerH;
                                }
                                stream.setHeight(hAR);
                            }
                        }
                    }
                }
//                System.out.println(audiofind);
                stream.setOnlyVideo(!audiofind);
                stream.setAudio(audiofind);
            } catch (IOException | InterruptedException | NumberFormatException e) {
            }
        }
    }

    public void cleanDesktops() {
        numWebcams = 0;
        lblWebcam.setTextFill(resetTab);
        Platform.runLater(() -> {
            lblWebcam.setText("Webcams(" + numWebcams + ")");
            webcamDesktop.getPanes().clear();
        });

        numVideos = 0;
        lblVideo.setTextFill(resetTab);
        Platform.runLater(() -> {
            lblVideo.setText("Videos(" + numVideos + ")");
            videoDesktop.getPanes().clear();
        });

        numMusics = 0;
        lblMusic.setTextFill(resetTab);
        Platform.runLater(() -> {
            lblMusic.setText("Musics(" + numMusics + ")");
            musicDesktop.getPanes().clear();
        });

        numPictures = 0;
        lblPicture.setTextFill(resetTab);
        Platform.runLater(() -> {
            lblPicture.setText("Pictures(" + numPictures + ")");
            pictureDesktop.getPanes().clear();
        });

        numTexts = 0;
        lblText.setTextFill(resetTab);
        Platform.runLater(() -> {
            lblText.setText("Texts(" + numTexts + ")");
            textDesktop.getPanes().clear();
        });

        numAudioIns = 0;
        lblAudioIn.setTextFill(resetTab);
        Platform.runLater(() -> {
            lblAudioIn.setText("AudioIns(" + numAudioIns + ")");
            audioInDesktop.getPanes().clear();
        });

        numDesktops = 0;
        lblDesktop.setTextFill(resetTab);
        Platform.runLater(() -> {
            lblDesktop.setText("Desktops(" + numDesktops + ")");
            desktopDesktop.getPanes().clear();
        });
    }

    @FXML
    private void btnLoadStudioAction(ActionEvent event) throws IOException {

        ArrayList<Stream> streamzI = MasterTracks.getInstance().getStreams();
        ArrayList<String> sourceChI = MasterTracks.getInstance().getTracks();

        int sinkStream = 0;
        sinkStream = streamzI.stream().filter((s) -> (s.getClass().toString().contains("Sink"))).map((_item) -> 1).reduce(sinkStream, Integer::sum);

        if (streamzI.size() - sinkStream > 0 || sourceChI.size() > 0) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(TS);
            Window window = alert.getOwner();
            alert.setX(window.getX() + window.getWidth() / 2 - alert.getWidth() / 2);
            alert.setY(window.getY() + window.getHeight() / 2 - alert.getHeight() / 2);
            alert.setTitle("TS Information");
            alert.setHeaderText(null);
            alert.setContentText("Current Studio will be closed !!!");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }

        FileChooser chooser = new FileChooser();
        if (lastFolder.exists()) {
            chooser.setInitialDirectory(lastFolder);
        }
        ExtensionFilter studioFilter = new ExtensionFilter("Studio files (*.studio)", "*.studio", "*.studio");
        ExtensionFilter allFilesFilter = new ExtensionFilter("All files (*.*)", "*.*", "*.*");
        chooser.getExtensionFilters().add(studioFilter);
        chooser.getExtensionFilters().add(allFilesFilter);
        chooser.setSelectedExtensionFilter(studioFilter);
        chooser.setTitle("Load a Studio ...");
        Stage primaryStage = new Stage();
        final File file = chooser.showOpenDialog(primaryStage);

        if (file != null) {

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(TS);
            Window window = alert.getOwner();
            alert.setX(window.getX() + window.getWidth() / 2 - alert.getWidth() / 2);
            alert.setY(window.getY() + window.getHeight() / 2 - alert.getHeight() / 2);
            Platform.runLater(() -> {
                alert.setTitle("TS Information");
                alert.setHeaderText(null);
                alert.setContentText("Loading Studio, please wait ...");
                alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            });

            Task task = new Task<Void>() {
                @Override
                public Void call() throws IOException {
                    lastFolder = file.getParentFile();

                    SystemPlayer.getInstanceFX(null).stop();
                    Tools.sleep(10);
                    PrePlayer.getPreInstanceFX(null).stop();
                    Tools.sleep(10);
                    MasterTracks.getInstance().endAllStream();
                    MasterTracks.getInstance().getStreams().stream().forEach((s) -> {
                        s.updateStatus();
                    });

                    ArrayList<Stream> streamz = MasterTracks.getInstance().getStreams();
                    ArrayList<String> sourceCh = MasterTracks.getInstance().getTracks();
                    do {
                        for (int l = 0; l < streamz.size(); l++) {
                            Stream removeS = streamz.get(l);
                            Tools.sleep(20);
                            removeS.destroy();
                            removeS = null;
                        }
                        for (int a = 0; a < sourceCh.size(); a++) {
                            String removeSc = sourceCh.get(a);
                            MasterTracks.getInstance().removeTrack(removeSc);
                            Tools.sleep(20);
                            listenerTSfxTPfx.removeTracksfx(removeSc, a);
                        }
                    } while (streamz.size() > 0 || sourceCh.size() > 0);
                    list_.<String>getItems().clear();

                    listenerTSfxTPfx.stopChTimefx(null);
                    listenerTSfxTPfx.resetBtnStatesfx();
                    listenerOPfx.resetBtnStatesfx();
                    Platform.runLater(() -> {
                        controlTab.getTabs().clear();
                        lblSourceSelected.setText("");
                    });

                    cleanDesktops();

                    try {
                        Studio.LText = new ArrayList<>();
                        Studio.extstream = new ArrayList<>();
                        Studio.ImgMovMus = new ArrayList<>();
                        Studio.load(file, "load");
                        Studio.main();
                        valW.setValue(MasterMixer.getInstance().getWidth());
                        valH.setValue(MasterMixer.getInstance().getHeight());
                        valFPS.setValue(MasterMixer.getInstance().getRate());
                        int mW = spinWidth_.getValue();
                        int mH = spinHeight_.getValue();
                        MasterMixer.getInstance().stop();
                        MasterMixer.getInstance().setWidth(mW);
                        MasterMixer.getInstance().setHeight(mH);
                        MasterMixer.getInstance().setRate(spinFPS_.getValue());
                        MasterMixer.getInstance().start();
                        PreviewMixer.getInstance().stop();
                        PreviewMixer.getInstance().setWidth(mW);
                        PreviewMixer.getInstance().setHeight(mH);
                        PreviewMixer.getInstance().start();
                    } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
                        Logger.getLogger(TruckliststudioUIController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    // loading studio streams
                    for (int u = 0; u < Studio.ImgMovMus.size(); u++) {
                        Stream s = Studio.extstream.get(u);
                        stream_ = s;
                        if (s != null) {

                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/webcamstudiofx/StreamDesktopFX.fxml"));
                            TitledPane streamDesktopFX = (TitledPane) fxmlLoader.load();
                            StreamDesktopFXController controller = fxmlLoader.<StreamDesktopFXController>getController();
                            controller.setListener(truckliststudiouicontroller);
                            streamDesktopFX.setText(s.getName());
                            streamDesktopFX.setMaxWidth(Double.MAX_VALUE);
                            AnchorPane.setTopAnchor(streamDesktopFX, 0.0);
                            AnchorPane.setBottomAnchor(streamDesktopFX, 0.0);
                            AnchorPane.setLeftAnchor(streamDesktopFX, 0.0);
                            AnchorPane.setRightAnchor(streamDesktopFX, 0.0);

                            if (s instanceof SourceMovie) {
                                numVideos += 1;
                                lblVideo.setTextFill(busyTab);
                                Platform.runLater(() -> {
                                    videoDesktop.getPanes().add(streamDesktopFX);
                                    lblVideo.setText("Videos(" + numVideos + ")");
                                });

                            } else if (s instanceof SourceMusic) {
                                numMusics += 1;
                                lblMusic.setTextFill(busyTab);
                                Platform.runLater(() -> {
                                    musicDesktop.getPanes().add(streamDesktopFX);
                                    lblMusic.setText("Musics(" + numMusics + ")");
                                });

                            } else if (s instanceof SourceImage || s instanceof SourceImageGif) {
                                numPictures += 1;
                                lblPicture.setTextFill(busyTab);
                                Platform.runLater(() -> {
                                    pictureDesktop.getPanes().add(streamDesktopFX);
                                    lblPicture.setText("Pictures(" + numPictures + ")");
                                });

                            } else if (s instanceof SourceAudioSource) {
                                numAudioIns += 1;
                                lblAudioIn.setTextFill(busyTab);
                                Platform.runLater(() -> {
                                    audioInDesktop.getPanes().add(streamDesktopFX);
                                    lblAudioIn.setText("AudioIns(" + numAudioIns + ")");
                                });

                            } else if (s instanceof SourceWebcam) {
                                System.out.println("Instance of Webcams");
                                numWebcams += 1;
                                lblWebcam.setTextFill(busyTab);
                                Platform.runLater(() -> {
                                    webcamDesktop.getPanes().add(streamDesktopFX);
                                    lblWebcam.setText("Webcams(" + numWebcams + ")");
                                });

                            } else if (s instanceof SourceDesktop) {
                                numDesktops += 1;
                                lblDesktop.setTextFill(busyTab);
                                Platform.runLater(() -> {
                                    desktopDesktop.getPanes().add(streamDesktopFX);
                                    lblDesktop.setText("Desktops(" + numDesktops + ")");
                                });

                            }
                            s.setLoaded(false);
                        }
                        System.out.println("Adding Source: " + s.getName());
                    }

                    Studio.extstream.clear();
                    Studio.extstream = null;
                    Studio.ImgMovMus.clear();
                    Studio.ImgMovMus = null;

                    for (SourceText text : Studio.LText) {
                        stream_ = text;
                        if (text != null) {

                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/webcamstudiofx/StreamDesktopFX.fxml"));
                            TitledPane streamDesktopFX = (TitledPane) fxmlLoader.load();
                            StreamDesktopFXController controller = fxmlLoader.<StreamDesktopFXController>getController();
                            controller.setListener(truckliststudiouicontroller);
                            streamDesktopFX.setText(text.getName());
                            streamDesktopFX.setMaxWidth(Double.MAX_VALUE);
                            AnchorPane.setTopAnchor(streamDesktopFX, 0.0);
                            AnchorPane.setBottomAnchor(streamDesktopFX, 0.0);
                            AnchorPane.setLeftAnchor(streamDesktopFX, 0.0);
                            AnchorPane.setRightAnchor(streamDesktopFX, 0.0);

                            numTexts += 1;
                            lblText.setTextFill(busyTab);
                            Platform.runLater(() -> {
                                textDesktop.getPanes().add(streamDesktopFX);
                                lblText.setText("Texts(" + numTexts + ")");
                            });

                            text.setLoaded(false);

                        }
                        System.out.println("Adding Source: " + text.getName());
                    }
                    Studio.LText.clear();
                    Studio.LText = null;
                    // loading studio tracks
                    ArrayList<String> chNameL = new ArrayList<>();
                    Studio.trackLoad.stream().forEach((chsct) -> {
                        chNameL.add(chsct.getName());
//                                System.out.println("TrackLoad="+chsct.getName());
                    });
                    LinkedHashSet<String> hs = new LinkedHashSet<>(chNameL);
                    chNameL.clear();
                    chNameL.addAll(hs);
                    chNameL.stream().forEach((chsct) -> {
                        listenerTSfxTPfx.addLoadingTrackfx(chsct);
                    });
                    Tools.sleep(500);
                    Studio.trackLoad.clear();

                    listenerOPfx.resetSinksfx(null);
                    ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Studio is loaded!");
                    ResourceMonitorFXController.getInstance().addMessage(label);

                    if (alert.isShowing()) {
                        Platform.runLater(() -> alert.close());
                    }
                    list_.scrollTo(0);
                    list_.getSelectionModel().select(0);
                    return null;
                }

            };
            TS.setTitle("WebcamStudioFX " + Version.version + " (" + file.getName() + ")");
            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();

        } else {
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Loading Cancelled!");
            ResourceMonitorFXController.getInstance().addMessage(label);
        }
    }

    private void loadAtStart(File file, ActionEvent event) throws IOException {

//        ArrayList<Stream> streamzI = MasterTracks.getInstance().getStreams();
//        ArrayList<String> sourceChI = MasterTracks.getInstance().getTracks();
//
//        int sinkStream = 0;
//        sinkStream = streamzI.stream().filter((s) -> (s.getClass().toString().contains("Sink"))).map((_item) -> 1).reduce(sinkStream, Integer::sum);
//
//        if (streamzI.size() - sinkStream > 0 || sourceChI.size() > 0) {
//            Alert alert = new Alert(AlertType.INFORMATION);
//            alert.initOwner(TS);
//            Window window = alert.getOwner();
//            alert.setX(window.getX() + window.getWidth() / 2 - alert.getWidth() / 2);
//            alert.setY(window.getY() + window.getHeight() / 2 - alert.getHeight() / 2);
//            alert.setTitle("TS Information");
//            alert.setHeaderText(null);
//            alert.setContentText("Current Studio will be closed !!!");
//            alert.initModality(Modality.APPLICATION_MODAL);
//            alert.showAndWait();
//        }
//
//        FileChooser chooser = new FileChooser();
//        if (lastFolder.exists()) {
//            chooser.setInitialDirectory(lastFolder);
//        }
//        ExtensionFilter studioFilter = new ExtensionFilter("Studio files (*.studio)", "*.studio", "*.studio");
//        ExtensionFilter allFilesFilter = new ExtensionFilter("All files (*.*)", "*.*", "*.*");
//        chooser.getExtensionFilters().add(studioFilter);
//        chooser.getExtensionFilters().add(allFilesFilter);
//        chooser.setSelectedExtensionFilter(studioFilter);
//        chooser.setTitle("Load a Studio ...");
//        Stage primaryStage = new Stage();
//        final File file = chooser.showOpenDialog(primaryStage);
        if (file != null) {

//            Alert alert = new Alert(AlertType.INFORMATION);
////            alert.initOwner(TS);
////            Window window = alert.getOwner();
////            alert.setX(window.getX() + window.getWidth() / 2 - alert.getWidth() / 2);
////            alert.setY(window.getY() + window.getHeight() / 2 - alert.getHeight() / 2);
//            Platform.runLater(() -> {
//                alert.setTitle("TS Information");
//                alert.setHeaderText(null);
//                alert.setContentText("Loading Studio, please wait ...");
//                alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
//                alert.initModality(Modality.APPLICATION_MODAL);
//                alert.showAndWait();
//            });
            Task task = new Task<Void>() {
                @Override
                public Void call() throws IOException {
                    lastFolder = file.getParentFile();

                    SystemPlayer.getInstanceFX(null).stop();
                    Tools.sleep(10);
                    PrePlayer.getPreInstanceFX(null).stop();
                    Tools.sleep(10);
                    MasterTracks.getInstance().endAllStream();
                    MasterTracks.getInstance().getStreams().stream().forEach((s) -> {
                        s.updateStatus();
                    });

                    ArrayList<Stream> streamz = MasterTracks.getInstance().getStreams();
                    ArrayList<String> sourceCh = MasterTracks.getInstance().getTracks();
                    do {
                        for (int l = 0; l < streamz.size(); l++) {
                            Stream removeS = streamz.get(l);
                            Tools.sleep(20);
                            removeS.destroy();
                            removeS = null;
                        }
                        for (int a = 0; a < sourceCh.size(); a++) {
                            String removeSc = sourceCh.get(a);
                            MasterTracks.getInstance().removeTrack(removeSc);
                            Tools.sleep(20);
                            listenerTSfxTPfx.removeTracksfx(removeSc, a);
                        }
                    } while (streamz.size() > 0 || sourceCh.size() > 0);
                    list_.<String>getItems().clear();

                    listenerTSfxTPfx.stopChTimefx(null);
                    listenerTSfxTPfx.resetBtnStatesfx();
                    listenerOPfx.resetBtnStatesfx();
                    Platform.runLater(() -> {
                        controlTab.getTabs().clear();
                        lblSourceSelected.setText("");
                    });

                    cleanDesktops();

                    try {
                        Studio.LText = new ArrayList<>();
                        Studio.extstream = new ArrayList<>();
                        Studio.ImgMovMus = new ArrayList<>();
                        Studio.load(file, "load");
                        Studio.main();
                        valW.setValue(MasterMixer.getInstance().getWidth());
                        valH.setValue(MasterMixer.getInstance().getHeight());
                        valFPS.setValue(MasterMixer.getInstance().getRate());
                        int mW = spinWidth_.getValue();
                        int mH = spinHeight_.getValue();
                        MasterMixer.getInstance().stop();
                        MasterMixer.getInstance().setWidth(mW);
                        MasterMixer.getInstance().setHeight(mH);
                        MasterMixer.getInstance().setRate(spinFPS_.getValue());
                        MasterMixer.getInstance().start();
                        PreviewMixer.getInstance().stop();
                        PreviewMixer.getInstance().setWidth(mW);
                        PreviewMixer.getInstance().setHeight(mH);
                        PreviewMixer.getInstance().start();
                    } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
                        Logger.getLogger(TruckliststudioUIController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    // loading studio streams
                    for (int u = 0; u < Studio.ImgMovMus.size(); u++) {
                        Stream s = Studio.extstream.get(u);
                        stream_ = s;
                        if (s != null) {

                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/webcamstudiofx/StreamDesktopFX.fxml"));
                            TitledPane streamDesktopFX = (TitledPane) fxmlLoader.load();
                            StreamDesktopFXController controller = fxmlLoader.<StreamDesktopFXController>getController();
                            controller.setListener(truckliststudiouicontroller);
                            streamDesktopFX.setText(s.getName());
                            streamDesktopFX.setMaxWidth(Double.MAX_VALUE);
                            AnchorPane.setTopAnchor(streamDesktopFX, 0.0);
                            AnchorPane.setBottomAnchor(streamDesktopFX, 0.0);
                            AnchorPane.setLeftAnchor(streamDesktopFX, 0.0);
                            AnchorPane.setRightAnchor(streamDesktopFX, 0.0);

                            if (s instanceof SourceMovie) {
                                numVideos += 1;
                                lblVideo.setTextFill(busyTab);
                                Platform.runLater(() -> {
                                    videoDesktop.getPanes().add(streamDesktopFX);
                                    lblVideo.setText("Videos(" + numVideos + ")");
                                });

                            } else if (s instanceof SourceMusic) {
                                numMusics += 1;
                                lblMusic.setTextFill(busyTab);
                                Platform.runLater(() -> {
                                    musicDesktop.getPanes().add(streamDesktopFX);
                                    lblMusic.setText("Musics(" + numMusics + ")");
                                });

                            } else if (s instanceof SourceImage || s instanceof SourceImageGif) {
                                numPictures += 1;
                                lblPicture.setTextFill(busyTab);
                                Platform.runLater(() -> {
                                    pictureDesktop.getPanes().add(streamDesktopFX);
                                    lblPicture.setText("Pictures(" + numPictures + ")");
                                });

                            } else if (s instanceof SourceAudioSource) {
                                numAudioIns += 1;
                                lblAudioIn.setTextFill(busyTab);
                                Platform.runLater(() -> {
                                    audioInDesktop.getPanes().add(streamDesktopFX);
                                    lblAudioIn.setText("AudioIns(" + numAudioIns + ")");
                                });

                            } else if (s instanceof SourceWebcam) {
                                numWebcams += 1;
                                lblWebcam.setTextFill(busyTab);
                                Platform.runLater(() -> {
                                    webcamDesktop.getPanes().add(streamDesktopFX);
                                    lblWebcam.setText("Webcams(" + numWebcams + ")");
                                });

                            } else if (s instanceof SourceDesktop) {
                                numDesktops += 1;
                                lblDesktop.setTextFill(busyTab);
                                Platform.runLater(() -> {
                                    desktopDesktop.getPanes().add(streamDesktopFX);
                                    lblDesktop.setText("Desktops(" + numDesktops + ")");
                                });

                            }
                            s.setLoaded(false);
                        }
                        System.out.println("Adding Source: " + s.getName());
                    }

                    Studio.extstream.clear();
                    Studio.extstream = null;
                    Studio.ImgMovMus.clear();
                    Studio.ImgMovMus = null;

                    for (SourceText text : Studio.LText) {
                        stream_ = text;
                        if (text != null) {

                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/webcamstudiofx/StreamDesktopFX.fxml"));
                            TitledPane streamDesktopFX = (TitledPane) fxmlLoader.load();
                            StreamDesktopFXController controller = fxmlLoader.<StreamDesktopFXController>getController();
                            controller.setListener(truckliststudiouicontroller);
                            streamDesktopFX.setText(text.getName());
                            streamDesktopFX.setMaxWidth(Double.MAX_VALUE);
                            AnchorPane.setTopAnchor(streamDesktopFX, 0.0);
                            AnchorPane.setBottomAnchor(streamDesktopFX, 0.0);
                            AnchorPane.setLeftAnchor(streamDesktopFX, 0.0);
                            AnchorPane.setRightAnchor(streamDesktopFX, 0.0);

                            numTexts += 1;
                            lblText.setTextFill(busyTab);
                            Platform.runLater(() -> {
                                textDesktop.getPanes().add(streamDesktopFX);
                                lblText.setText("Texts(" + numTexts + ")");
                            });

                            text.setLoaded(false);

                        }
                        System.out.println("Adding Source: " + text.getName());
                    }
                    Studio.LText.clear();
                    Studio.LText = null;
                    // loading studio tracks
                    ArrayList<String> chNameL = new ArrayList<>();
                    Studio.trackLoad.stream().forEach((chsct) -> {
                        chNameL.add(chsct.getName());
//                                System.out.println("TrackLoad="+chsct.getName());
                    });
                    LinkedHashSet<String> hs = new LinkedHashSet<>(chNameL);
                    chNameL.clear();
                    chNameL.addAll(hs);
                    chNameL.stream().forEach((chsct) -> {
                        listenerTSfxTPfx.addLoadingTrackfx(chsct);
                    });
                    Tools.sleep(500);
                    Studio.trackLoad.clear();

                    listenerOPfx.resetSinksfx(null);
                    ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Studio is loaded!");
                    ResourceMonitorFXController.getInstance().addMessage(label);

//                    if (alert.isShowing()) {
//                        Platform.runLater(() -> alert.close());
//                    }
                    list_.scrollTo(0);
                    list_.getSelectionModel().select(0);
                    loadingFinish = true;
                    return null;
                }

            };
            TS.setTitle("WebcamStudioFX " + Version.version + " (" + file.getName() + ")");
            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();

        } else {
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Loading Cancelled!");
            ResourceMonitorFXController.getInstance().addMessage(label);
        }
    }

    private void loadPrefs() {
        int x = prefs.getInt("main-x", 100);
        int y = prefs.getInt("main-y", 100);
        int w = prefs.getInt("main-w", 800);
        int h = prefs.getInt("main-h", 400);
//        System.out.println(MasterMixer.getInstance().getWidth());
        MasterMixer.getInstance().setWidth(prefs.getInt("mastermixer-w", MasterMixer.getInstance().getWidth()));
        MasterMixer.getInstance().setHeight(prefs.getInt("mastermixer-h", MasterMixer.getInstance().getHeight()));
        MasterMixer.getInstance().setRate(prefs.getInt("mastermixer-r", MasterMixer.getInstance().getRate()));
        PreviewMixer.getInstance().setWidth(prefs.getInt("mastermixer-w", MasterMixer.getInstance().getWidth()));
        PreviewMixer.getInstance().setHeight(prefs.getInt("mastermixer-h", MasterMixer.getInstance().getHeight()));
        PreviewMixer.getInstance().setRate(5);
        double mSplit = 0;
        for (double d : mainSplit.getDividerPositions()) {
            mSplit = d;
        }
        mainSplit.setDividerPositions(prefs.getDouble("split-x", mSplit));
        for (double d : mainVerticalSplit.getDividerPositions()) {
            mSplit = d;
        }
        mainVerticalSplit.setDividerPositions(prefs.getDouble("split-y", mSplit));
        for (double d : mainBottomSplit.getDividerPositions()) {
            mSplit = d;
        }
        mainBottomSplit.setDividerPositions(prefs.getDouble("split-x-bottom", mSplit));
        lastFolder = new File(prefs.get("lastfolder", "."));
        audioFreq = prefs.getInt("audio-freq", audioFreq);
        theme = prefs.get("theme", theme);
        outFMEbe = prefs.getInt("out-FME", outFMEbe);
        autoAR = prefs.getBoolean("autoar", autoAR);
        autoTrack = prefs.getBoolean("autotrack", autoTrack);
        truckliststudioFX.setLayoutX(x);
        truckliststudioFX.setLayoutY(y);
        truckliststudioFX.setPrefSize(w, h);
        Platform.runLater(() -> {
            recorderFX.loadPrefs(prefs);
        });

    }

    public static String wsDistroWatch() {
        String system = null;
        Runtime rt = Runtime.getRuntime();
        String distroCmd = "uname -a";
        if (os == OS.LINUX) {
            try {
                Process distroProc = rt.exec(distroCmd);
                Tools.sleep(10);
                distroProc.waitFor();
                BufferedReader buf = new BufferedReader(new InputStreamReader(
                        distroProc.getInputStream()));
                String lineR;
                while ((lineR = buf.readLine()) != null) {
                    if (lineR.toLowerCase().contains("ubuntu")) {
                        system = "ubuntu";
                    } else {
                        system = "others";
                    }
                }
            } catch (IOException | InterruptedException | NumberFormatException e) {
            }
        } else if (os == OS.WINDOWS) {
            system = "windows";
        }

        return system;
    }

    private void savePrefs() {
        prefs.putInt("main-x", (int) truckliststudioFX.getLayoutX());
        prefs.putInt("main-y", (int) truckliststudioFX.getLayoutY());
        prefs.putInt("main-w", (int) truckliststudioFX.getWidth());
        prefs.putInt("main-h", (int) truckliststudioFX.getHeight());
        prefs.putInt("mastermixer-w", MasterMixer.getInstance().getWidth());
        prefs.putInt("mastermixer-h", MasterMixer.getInstance().getHeight());
        prefs.putInt("mastermixer-r", MasterMixer.getInstance().getRate());
//        System.out.println(MasterMixer.getInstance().getWidth());
        double mSplit = 0;
        for (double d : mainSplit.getDividerPositions()) {
            mSplit = d;
        }
        prefs.putDouble("split-x", mSplit);
        for (double d : mainVerticalSplit.getDividerPositions()) {
            mSplit = d;
        }
        prefs.putDouble("split-y", mSplit);
        for (double d : mainBottomSplit.getDividerPositions()) {
            mSplit = d;
        }
        prefs.putDouble("split-x-bottom", mSplit);
        if (lastFolder != null) {
            prefs.put("lastfolder", lastFolder.getAbsolutePath());
        }
        prefs.putInt("audio-freq", audioFreq);
        prefs.put("theme", theme);
//        System.out.println("Theme:"+theme);
        prefs.putInt("out-FME", outFMEbe);
        prefs.putBoolean("autoar", autoAR);
        prefs.putBoolean("autotrack", autoTrack);
        recorderFX.savePrefs(prefs);
        try {
            prefs.flush();
        } catch (BackingStoreException ex) {
            Logger.getLogger(TruckliststudioUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initMainOutBE() {
        // FF = 0 ; AV = 1 ; GS = 2
        if (wsDistroWatch().toLowerCase().equals("windows")) {
            btnAddAudio.setDisable(true);
            if (winGS) {
                tglGST.setDisable(false);
            } else {
                tglGST.setDisable(true);
            }
            if (outFMEbe == 0 || outFMEbe == 1) {
                outFMEbe = 0;
                tglFFmpeg.setSelected(true);
            } else if (outFMEbe == 2) {
                if (!tglGST.isDisable()) {
                    tglFFmpeg.setSelected(false);
                    tglGST.setSelected(true);
                } else {
                    outFMEbe = 0;
                    tglFFmpeg.setSelected(true);
                }
            }
        } else if (ffmpeg && !avconv) {
            if (outFMEbe == 0 || outFMEbe == 1) {
                outFMEbe = 0;
                tglFFmpeg.setSelected(true);
                tglAVconv.setDisable(true);
                tglGST.setDisable(false);
            } else if (outFMEbe == 2) {
                tglFFmpeg.setSelected(false);
                tglFFmpeg.setDisable(false);
                tglAVconv.setDisable(true);
                tglGST.setSelected(true);
            }
        } else if (ffmpeg && avconv) {
            switch (outFMEbe) {
                case 0:
                    tglFFmpeg.setSelected(true);
                    tglAVconv.setDisable(false);
                    tglGST.setDisable(false);
                    break;
                case 1:
                    tglFFmpeg.setDisable(false);
                    tglAVconv.setSelected(true);
                    tglGST.setDisable(false);
                    break;
                case 2:
                    tglFFmpeg.setDisable(false);
                    tglAVconv.setDisable(false);
                    tglGST.setSelected(true);
                    break;
            }
        } else if (!ffmpeg && avconv) {
            if (outFMEbe == 1 || outFMEbe == 0) {
                outFMEbe = 1;
                tglAVconv.setSelected(true);
                tglFFmpeg.setDisable(true);
                tglGST.setDisable(false);
            } else if (outFMEbe == 2) {
                tglFFmpeg.setDisable(true);
                tglAVconv.setDisable(false);
                tglGST.setSelected(true);
            }
        }
        listenerOPfx.resetSinksfx(null);
//        System.out.println("OutFMEbe: " + outFMEbe);
    }

    @FXML
    private void tglFFmpegAction(ActionEvent event) {
        if (tglFFmpeg.isSelected()) {
            tglAVconv.setSelected(false);
            tglGST.setSelected(false);
            outFMEbe = 0;
            listenerOPfx.resetSinksfx(null);
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Outputs switched to FFmpeg.");
            ResourceMonitorFXController.getInstance().addMessage(label);
        } else {
            outFMEbe = 2;
            tglAVconv.setDisable(!avconv);
            tglGST.setDisable(false);
            tglGST.setSelected(true);
            listenerOPfx.resetSinksfx(null);
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Outputs switched to GStreamer.");
            ResourceMonitorFXController.getInstance().addMessage(label);
        }
    }

    @FXML
    private void tglAVconvAction(ActionEvent event) {
        if (tglAVconv.isSelected()) {
            tglFFmpeg.setSelected(false);
            tglGST.setSelected(false);
            outFMEbe = 1;
            listenerOPfx.resetSinksfx(null);
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Outputs switched to Libav.");
            ResourceMonitorFXController.getInstance().addMessage(label);
        } else {
            outFMEbe = 2;
            tglFFmpeg.setDisable(!ffmpeg);
            tglGST.setDisable(false);
            tglGST.setSelected(true);
            listenerOPfx.resetSinksfx(null);
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Outputs switched to Gstreamer.");
            ResourceMonitorFXController.getInstance().addMessage(label);
        }
    }

    @FXML
    private void tglGSTAction(ActionEvent event) {
        if (tglGST.isSelected()) {
            tglFFmpeg.setSelected(false);
            tglAVconv.setSelected(false);
            outFMEbe = 2;
            listenerOPfx.resetSinksfx(null);
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Outputs switched to GStreamer.");
            ResourceMonitorFXController.getInstance().addMessage(label);
        } else {
            if (ffmpeg && !avconv) {
                outFMEbe = 0;
                tglFFmpeg.setSelected(true);
                tglAVconv.setDisable(true);
                tglGST.setDisable(false);
            } else if (ffmpeg && avconv) {
                outFMEbe = 1;
                tglFFmpeg.setDisable(false);
                tglAVconv.setSelected(true);
                tglGST.setDisable(false);

            } else {
                outFMEbe = 1;
                tglFFmpeg.setDisable(true);
                tglAVconv.setSelected(true);
                tglGST.setDisable(false);
            }

            listenerOPfx.resetSinksfx(null);
            if (outFMEbe == 1) {
                ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Outputs switched to Libav.");
                ResourceMonitorFXController.getInstance().addMessage(label);
            } else {
                ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Outputs switched to FFmpeg.");
                ResourceMonitorFXController.getInstance().addMessage(label);
            }
        }
    }

    @Override
    public void selectedSource(Stream source) {
        String sourceName = source.getName();
//        String shortName = "";
        if (sourceName.length() > 30) {
            shortName = source.getName().substring(0, 30) + " ...";
        } else {
            shortName = sourceName;
        }

        stringSourceSelected = source.getName();
        if (stringSourceSelected.equals(lblPlayingTrack)) {
            btnRemoveSource.setDisable(true);
        } else {
            btnRemoveSource.setDisable(false);
        }

        Platform.runLater(() -> {
            controlTab.getTabs().clear();
            ArrayList<AnchorPane> comps = null;
            SourceControls sourceCtrl = null;
            try {
                sourceCtrl = new SourceControls();
                comps = sourceCtrl.getControls(source);
            } catch (IOException ex) {
                Logger.getLogger(TruckliststudioUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (AnchorPane c : comps) {

                Tab tab = new Tab(c.getId());
                AnchorPane.setTopAnchor(c, 0.0);
                AnchorPane.setBottomAnchor(c, 0.0);
                AnchorPane.setLeftAnchor(c, 0.0);
                AnchorPane.setRightAnchor(c, 0.0);

                tab.setContent(c);

                controlTab.getTabs().add(tab);
            }
            source.updateStatus();
        });
        Platform.runLater(() -> {
            lblSourceSelected.setText(shortName);
        });
    }

    @Override
    public void closeSource(String name) {
        lblSourceSelected.setText("");
        int tabIndex = tabSources.getSelectionModel().getSelectedIndex();
//        System.out.println("TabIndex="+tabIndex);
        String tabTitle = tabSources.getTabs().get(tabIndex).getText();
        if (tabTitle.contains("Videos")) {
            numVideos -= 1;
            if (numVideos > 0) {
                tabSources.getTabs().get(tabIndex).setText("Videos(" + numVideos + ")");
            } else {
                tabSources.getTabs().get(tabIndex).setStyle("-fx-text-fill: black"); //setTextFill(resetTab);

                tabSources.getTabs().get(tabIndex).setText("Videos(" + numVideos + ")");
            }
        } else if (tabTitle.contains("Musics")) {
            numMusics -= 1;
            if (numMusics > 0) {
                tabSources.getTabs().get(tabIndex).setText("Musics(" + numMusics + ")");
            } else {
                tabSources.getTabs().get(tabIndex).setStyle("-fx-text-fill: black"); //setForeground(resetTab);

                tabSources.getTabs().get(tabIndex).setText("Musics(" + numMusics + ")");
            }
        } else if (tabTitle.contains("Pictures")) {
            numPictures -= 1;
            if (numPictures > 0) {
                tabSources.getTabs().get(tabIndex).setText("Pictures(" + numPictures + ")");
            } else {
                tabSources.getTabs().get(tabIndex).setStyle("-fx-text-fill: black"); //.setForeground(resetTab);

                tabSources.getTabs().get(tabIndex).setText("Pictures(" + numPictures + ")");
            }
        } else if (tabTitle.contains("Texts")) {
            numTexts -= 1;
            if (numTexts > 0) {
                tabSources.getTabs().get(tabIndex).setText("Texts(" + numTexts + ")");
            } else {
                tabSources.getTabs().get(tabIndex).setStyle("-fx-text-fill: black"); //.setForeground(resetTab);

                tabSources.getTabs().get(tabIndex).setText("Texts(" + numTexts + ")");
            }
        } else if (tabTitle.contains("AudioIns")) {
            numAudioIns -= 1;
            if (numAudioIns > 0) {
                tabSources.getTabs().get(tabIndex).setText("AudioIns(" + numAudioIns + ")");
            } else {
                tabSources.getTabs().get(tabIndex).setStyle("-fx-text-fill: black"); //.setForeground(resetTab);

                tabSources.getTabs().get(tabIndex).setText("AudioIns(" + numAudioIns + ")");
            }
        } else if (tabTitle.contains("Desktops")) {
            numDesktops -= 1;
            if (numDesktops > 0) {
                tabSources.getTabs().get(tabIndex).setText("Desktops(" + numDesktops + ")");
            } else {
                tabSources.getTabs().get(tabIndex).setStyle("-fx-text-fill: black"); //.setForeground(resetTab);

                tabSources.getTabs().get(tabIndex).setText("Desktops(" + numDesktops + ")");
            }
        } else if (tabTitle.contains("Webcams")) {
            numWebcams -= 1;
            if (numWebcams > 0) {
                tabSources.getTabs().get(tabIndex).setText("Webcams(" + numWebcams + ")");
            } else {
                tabSources.getTabs().get(tabIndex).setStyle("-fx-text-fill: black"); //.setForeground(resetTab);

                tabSources.getTabs().get(tabIndex).setText("Webcams(" + numWebcams + ")");
            }
        }
        listenerTSfxTPfx.closeItsTrackfx(name);

    }
}
