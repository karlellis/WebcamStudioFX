/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import static webcamstudiofx.TrackPanelFXController.lblOnAir_;
import static webcamstudiofx.TruckliststudioUIController.lastFolder;
import static webcamstudiofx.TruckliststudioUIController.prefs;
import static webcamstudiofx.TruckliststudioUIController.wsDistroWatch;
import webcamstudiofx.components.FMEDialogFXController;
import webcamstudiofx.components.ResourceMonitorFXController;
import webcamstudiofx.components.ResourceMonitorLabelFX;
import webcamstudiofx.exporter.vloopback.VideoDevice;
import webcamstudiofx.externals.FME;
import webcamstudiofx.media.renderer.Exporter;
import webcamstudiofx.mixers.MasterMixer;
import webcamstudiofx.streams.SinkAudio;
import webcamstudiofx.streams.SinkBroadcast;
import webcamstudiofx.streams.SinkFile;
import webcamstudiofx.streams.SinkLinuxDevice;
import webcamstudiofx.streams.SinkUDP;
import webcamstudiofx.streams.Stream;
import webcamstudiofx.tracks.MasterTracks;
import webcamstudiofx.util.Tools;
import webcamstudiofx.util.Tools.OS;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class OutputPanelFXController implements Initializable, Stream.Listener, TruckliststudioUIController.Listener, TrackPanelFXController.Listener, Exporter.Listener {

    final ContextMenu fmeContextMenu = new ContextMenu();
    final ContextMenu recordContextMenu = new ContextMenu();
    final ContextMenu udpContextMenu = new ContextMenu();
    private final String[] v4l2PixelFormats = {"RGB24", "UYUV", "BGR24"};

    private TreeMap<String, SinkFile> files = new TreeMap<>();
    private TreeMap<String, SinkBroadcast> broadcasts = new TreeMap<>();
    private ArrayList<String> broadcastsOut = new ArrayList<>();
    TreeMap<String, SinkLinuxDevice> devices = new TreeMap<>();
    ArrayList<String> devicesOut = new ArrayList<>();
    private TreeMap<String, SinkUDP> udpOut = new TreeMap<>();
    private TreeMap<String, SinkAudio> audioOut = new TreeMap<>();
    private TreeMap<String, FME> fmes = new TreeMap<>();
    public final static String userHomeDir = Tools.getUserHome();
    private int fmeCount = 0;
    private int camCount = 0;
    private TreeMap<String, ResourceMonitorLabelFX> labels = new TreeMap<>();
    private FME currFME;
    public static FME currFME_;
    private File f;
    private SinkFile fileStream;
    private SinkUDP udpStream;
    public static SinkFile fileStream_;
    public static SinkUDP udpStream_;
    private SinkAudio audioStream;
    private boolean audioOutState = false;
    private boolean udpOutState = false;
    private boolean audioOutSwitch = false;
    private boolean udpOutSwitch = false;
    private boolean fmeOutState = false;
    private boolean fmeOutSwitch = false;
    private boolean camOutState = false;
    private boolean camOutSwitch = false;
    AnchorPane fmeDialogFX;
    AnchorPane fmeSettingsFX;
    AnchorPane sinkSettingsFX;
    public static boolean isFileStream = true;

    @FXML
    public AnchorPane OutputPanelFX;
    @FXML
    private ToggleButton tglAudioOut;
    @FXML
    private ToggleButton tglRecordToFile;
    @FXML
    private ToggleButton tglUDP;
    @FXML
    private Button btnAddFME;
    @FXML
    private VBox outputPanelArea;
    @FXML
    private ComboBox comboPixelFormat;
    @FXML
    private TitledPane webcamOut;
    @FXML
    private VBox webcamOutPanelArea;
    @FXML
    private TitledPane defaultOut;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currFME_ = currFME;

        ObservableList<String> options = FXCollections.observableArrayList();
        options.addAll(v4l2PixelFormats);
        comboPixelFormat.setItems(options);
        comboPixelFormat.getSelectionModel().selectFirst();

        fmes.values().stream().forEach((fme) -> {
            addButtonBroadcast(fme);
        });
        f = new File(userHomeDir + "/.webcamstudiofx/Record To File");
        udpStream = new SinkUDP();
        fileStream = new SinkFile(f);
        audioStream = new SinkAudio();
        fmeInitPopUp();
        sinkFileInitPopUp();
        sinkUDPInitPopUp();

        tglRecordToFile.setOnMousePressed((javafx.scene.input.MouseEvent event) -> {
            ToggleButton button = ((ToggleButton) event.getSource());
            if (!button.isSelected()) {
                if (event.isSecondaryButtonDown()) {
                    recordContextMenu.show(outputPanelArea, event.getScreenX(), event.getScreenY());
                }
            }
        });

        tglUDP.setOnMousePressed((javafx.scene.input.MouseEvent event) -> {
            ToggleButton button = ((ToggleButton) event.getSource());
            if (!button.isSelected()) {
                if (event.isSecondaryButtonDown()) {
                    udpContextMenu.show(outputPanelArea, event.getScreenX(), event.getScreenY());
                }
            }
        });

        TruckliststudioUIController.setListenerOPfx(this);
        TrackPanelFXController.setListenerCPOPanelFX(this);
        Exporter.setListenerEx(this);
        
        if (Tools.getOS() == OS.LINUX) {
            paintWSCamButtons ();
        }
        
        fileStream.setWidth(MasterMixer.getInstance().getWidth());
        fileStream.setHeight(MasterMixer.getInstance().getHeight());
        fileStream.setRate(MasterMixer.getInstance().getRate());

        udpStream.setWidth(MasterMixer.getInstance().getWidth());
        udpStream.setHeight(MasterMixer.getInstance().getHeight());
        udpStream.setRate(MasterMixer.getInstance().getRate());

        if (wsDistroWatch().toLowerCase().equals("windows")) {
            tglAudioOut.setDisable(true);
        }
    }

    public void loadPrefs(Preferences prefs) {
        Preferences fmePrefs = prefs.node("fme");
        Preferences filePrefs = prefs.node("filerec");
        Preferences udpPrefs = prefs.node("udp");
        try {
            String[] services = fmePrefs.childrenNames();
            String[] servicesF = filePrefs.childrenNames();
            String[] servicesU = udpPrefs.childrenNames();

            for (String s : servicesF) {
                Preferences serviceF = filePrefs.node(s);
                fileStream.setVbitrate(serviceF.get("vbitrate", "1200"));
                fileStream.setAbitrate(serviceF.get("abitrate", "128"));
            }

            for (String s : servicesU) {
                Preferences serviceU = udpPrefs.node(s);
                udpStream.setVbitrate(serviceU.get("vbitrate", "1200"));
                udpStream.setAbitrate(serviceU.get("abitrate", "128"));
                udpStream.setStandard(serviceU.get("standard", "STD"));
            }

            for (String s : services) {
                Preferences service = fmePrefs.node(s);
                String url = service.get("url", "");
                String name = service.get("name", "");
                String abitrate = service.get("abitrate", "512000");
                String vbitrate = service.get("vbitrate", "96000");
                String vcodec = service.get("vcodec", "");
                String acodec = service.get("acodec", "");
                String width = service.get("width", "");
                String height = service.get("height", "");
                String stream = service.get("stream", "");
                String mount = service.get("mount", "");
                String password = service.get("password", "");
                String port = service.get("port", "");
                String keyInt = service.get("keyint", "");
                String standard = service.get("standard", "STD");
                // for compatibility before KeyInt
                if ("".equals(keyInt)) {
                    keyInt = "125";
                }

//                System.out.println("Loaded KeyInt: "+keyInt+"###");
                FME fme = new FME(url, stream, name, abitrate, vbitrate, vcodec, acodec, width, height, mount, password, port, keyInt);
                fme.setStandard(standard);
                fmes.put(fme.getName(), fme);
                addButtonBroadcast(fme);
            }
        } catch (BackingStoreException ex) {
            Logger.getLogger(OutputPanelFXController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void savePrefs(Preferences prefs) {
        Preferences fmePrefs = prefs.node("fme");
        Preferences filePrefs = prefs.node("filerec");
        Preferences udpPrefs = prefs.node("udp");
        try {
            fmePrefs.removeNode();
            fmePrefs.flush();
            fmePrefs = prefs.node("fme");
            filePrefs.removeNode();
            filePrefs.flush();
            filePrefs = prefs.node("filerec");
            udpPrefs.removeNode();
            udpPrefs.flush();
            udpPrefs = prefs.node("udp");
        } catch (BackingStoreException ex) {
            Logger.getLogger(OutputPanelFXController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (FME fme : fmes.values()) {
            Preferences service = fmePrefs.node(fme.getName());
            service.put("url", fme.getUrl());
            service.put("name", fme.getName());
            service.put("abitrate", fme.getAbitrate());
            service.put("vbitrate", fme.getVbitrate());
            service.put("vcodec", fme.getVcodec());
            service.put("acodec", fme.getAcodec());
            service.put("width", fme.getWidth());
            service.put("height", fme.getHeight());
            service.put("stream", fme.getStream());
            service.put("mount", fme.getMount());
            service.put("password", fme.getPassword());
            service.put("port", fme.getPort());
            service.put("keyint", fme.getKeyInt());
            service.put("standard", fme.getStandard());
        }
        Preferences serviceF = filePrefs.node("frecordset");
        serviceF.put("abitrate", fileStream.getAbitrate());
        serviceF.put("vbitrate", fileStream.getVbitrate());
        Preferences serviceU = udpPrefs.node("uoutset");
        serviceU.put("abitrate", udpStream.getAbitrate());
        serviceU.put("vbitrate", udpStream.getVbitrate());
        serviceU.put("standard", udpStream.getStandard());
    }

    private String checkDoubleBroad(String s) {
        String res = s;
        for (String broName : broadcastsOut) {
            if (s.equals(broName)) {
                res = "";
            }
        }
        return res;
    }

    private String checkDoubleCam(String s) {
        String res = s;
        for (String camName : devicesOut) {
            if (s.equals(camName)) {
                res = "";
            }
        }
        return res;
    }

    private void paintWSCamButtons() {
        final OutputPanelFXController instanceSink = this;
        for (final VideoDevice d : VideoDevice.getInputDevices()) {
            String vdName = d.getFile().getName();
            String vdPath = d.getFile().getAbsolutePath();
            if (!vdName.endsWith("video21")) {
                ToggleButton wsCamButton = new ToggleButton();
//                Dimension dim = new Dimension(139,22);
//                wsCamButton.setPreferredSize(dim);
                wsCamButton.setText(d.getName());
//                wsCamButton.setActionCommand(d.getFile().getAbsolutePath());
//                wsCamButton.setIcon(tglRecordToFile.getGraphic().getIcon());
//                wsCamButton.setSelectedIcon(tglRecordToFile.getSelectedIcon());
//                wsCamButton.setRolloverEnabled(false);
                wsCamButton.setOnAction((ActionEvent actionEvent) -> {
//                wsCamButton.addActionListener(new java.awt.event.ActionListener() {
//                    @Override
//                    public void actionPerformed(java.awt.event.ActionEvent evt) {

//                        FME fme1 = fmes.get(button1.getText());
                    String device = vdPath;
//                        ToggleButton button1 = ((ToggleButton) evt.getSource());
                    ToggleButton button1 = (ToggleButton) actionEvent.getSource();
                    if (button1.isSelected()) {
                        camOutState = true;
                        String cleanCam = checkDoubleCam(button1.getText());
                        if (!"".equals(cleanCam)) {
                            devicesOut.add(d.getName());
                        }
                        camCount++;
                        SinkLinuxDevice stream = new SinkLinuxDevice(new File(device), button1.getText(), (comboPixelFormat.getSelectionModel().getSelectedIndex() + 1));
                        stream.setRate(MasterMixer.getInstance().getRate());
                        stream.setWidth(MasterMixer.getInstance().getWidth());
                        stream.setHeight(MasterMixer.getInstance().getHeight());
                        stream.setListener(instanceSink);
                        stream.read();
                        devices.put(button1.getText(), stream);
                        ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Rendering to " + button1.getText() + " (" + (comboPixelFormat.getSelectionModel().getSelectedIndex() + 1) + ")");
                        labels.put(button1.getText(), label);
                        ResourceMonitorFXController.getInstance().addMessage(label);
                        comboPixelFormat.setDisable(true);
                    } else {
                        camOutState = camCount > 0;
                        devicesOut.remove(d.getName());
                        SinkLinuxDevice stream = devices.get(button1.getText());
                        if (stream != null) {
                            camCount--;
                            stream.stop();
                            devices.remove(button1.getText());
//                                System.out.println("WS Camera Stopped ...");
                            ResourceMonitorLabelFX label = labels.remove(button1.getText());
                            ResourceMonitorFXController.getInstance().removeMessage(label);
//                                System.out.println("StopCamCount = "+camCount);
                            if (camCount == 0) {
                                comboPixelFormat.setDisable(false);
                            }
                        }
                        if (camCount == 0) {
                            comboPixelFormat.setDisable(false);
                        }
                    }
//                    }
                });

                Platform.runLater(() -> {
                    webcamOutPanelArea.getChildren().add(wsCamButton);
                });
//                outputPanelArea.getChildren().add(wsCamButton);
//                panelPixelFormat.revalidate();
//                this.revalidate();
            }
        }
    }

    @FXML
    private void tglAudioOutAction(ActionEvent event) {
//        System.out.println("tglAudioOut Pressed!!!");
        if (tglAudioOut.isSelected()) {
            audioOutState = true;
            audioStream.setListener(this);
            audioStream.read();
            audioOut.put("AudioOut", audioStream);
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Master Audio to Speakers");
            labels.put("AudioOut", label);
            ResourceMonitorFXController.getInstance().addMessage(label);
        } else {
            audioOutState = false;
            SinkAudio audioStream = audioOut.get("AudioOut");
            if (audioStream != null) {
                audioStream.stop();
                audioStream = null;
                audioOut.remove("AudioOut");
                ResourceMonitorLabelFX label = labels.get("AudioOut");
                ResourceMonitorFXController.getInstance().removeMessage(label);
            }
        }
    }

    @Override
    public void sourceUpdated(Stream stream) {
//        System.out.println(stream.getName());
        if (stream instanceof SinkFile) {
//            System.out.println("TGLRECORD UPDATED !!!");
            tglRecordToFile.setSelected(stream.isPlaying());
        } else if (stream instanceof SinkUDP) {
            tglUDP.setSelected(stream.isPlaying());
//            System.out.println("tglUDP UPDATED !!!");
        } else if (stream instanceof SinkAudio) {
//            System.out.println("TGLAUDIO UPDATED !!!");
            tglAudioOut.setSelected(stream.isPlaying());
        } else if (stream instanceof SinkBroadcast) {
            String name = stream.getName();
            outputPanelArea.getChildren().stream().filter((c) -> (c instanceof ToggleButton)).map((c) -> (ToggleButton) c).filter((b) -> (b.getText().equals(name))).forEach((b) -> {
                b.setSelected(stream.isPlaying());
            });

            if (!stream.isPlaying()) {
                fmeCount--;
            }
            if (fmeCount == 0 && !udpOutState) {
//                if (theme.equals("Caspian")) {
//                    lblOnAir_.setTextFill(javafx.scene.paint.Color.WHITE);
//                } else {
                lblOnAir_.setTextFill(javafx.scene.paint.Color.BLACK);
//                }
            }
        }
    }

    @FXML
    private void tglRecordToFileAction(ActionEvent event) {
        if (tglRecordToFile.isSelected()) {
            File file = null;
            FileChooser chooser = new FileChooser();
            if (lastFolder.exists()) {
                chooser.setInitialDirectory(lastFolder);
            }
            FileChooser.ExtensionFilter recAviFilter = new FileChooser.ExtensionFilter("AVI files (*.avi)", "*.avi");
            FileChooser.ExtensionFilter recMp4Filter = new FileChooser.ExtensionFilter("MP4 files (*.mp4)", "*.mp4");
            FileChooser.ExtensionFilter recFlvFilter = new FileChooser.ExtensionFilter("FLV files (*.flv)", "*.flv");
            FileChooser.ExtensionFilter allFilesFilter = new FileChooser.ExtensionFilter("All files (*.*)", "*.*", "*.*");
            chooser.getExtensionFilters().add(recMp4Filter);
            chooser.getExtensionFilters().add(recAviFilter);
            chooser.getExtensionFilters().add(recFlvFilter);
            chooser.getExtensionFilters().add(allFilesFilter);
            chooser.setSelectedExtensionFilter(recFlvFilter);
            chooser.setTitle("Record to a file ...");
            Stage primaryStage = new Stage();
            file = chooser.showSaveDialog(primaryStage);

            if (file != null) {
                String path = file.getAbsolutePath();
                ExtensionFilter eFilter = chooser.getSelectedExtensionFilter();
                if (eFilter.equals(recAviFilter)) {
                    if (!path.endsWith(".avi")) {
                        file = new File(path + ".avi");
                    }
                } else if (eFilter.equals(recMp4Filter) && !path.endsWith(".mp4")) {
                    file = new File(path + ".mp4");
                } else if (eFilter.equals(recFlvFilter) && !path.endsWith(".flv")) {
                    file = new File(path + ".flv");
                }

                fileStream.setFile(file);
                fileStream.setListener(this);
                // Fix lost prefs
                if ("".equals(fileStream.getVbitrate())) {
                    fileStream.setVbitrate("1200");
                }
                if ("".equals(fileStream.getAbitrate())) {
                    fileStream.setAbitrate("128");
                }

                fileStream.read();
//                System.out.println("VBitRate: "+fileStream.getVbitrate());
                files.put("RECORD", fileStream);
                ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Recording to " + f.getName());
                labels.put("RECORD", label);
                ResourceMonitorFXController.getInstance().addMessage(label);
            } else {
                tglRecordToFile.setSelected(false);
                ResourceMonitorLabelFX label3 = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Record Cancelled!");
                ResourceMonitorFXController.getInstance().addMessage(label3);
            }
        } else {
            SinkFile fileStream = files.get("RECORD");
            if (fileStream != null) {
                fileStream.stop();
                fileStream = null;
                files.remove("RECORD");
                ResourceMonitorLabelFX label = labels.get("RECORD");
                ResourceMonitorFXController.getInstance().removeMessage(label);

                ResourceMonitorLabelFX label2 = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "File is saved!");
                ResourceMonitorFXController.getInstance().addMessage(label2);
            }
        }
    }

    @FXML
    private void tglUDPAction(ActionEvent event) {
        if (tglUDP.isSelected()) {
            udpOutState = true;
            udpStream.setListener(this);
            // Fix lost prefs
            if ("".equals(udpStream.getVbitrate())) {
                udpStream.setVbitrate("1200");
            }
            if ("".equals(udpStream.getAbitrate())) {
                udpStream.setAbitrate("128");
            }
            if ("".equals(udpStream.getStandard())) {
                udpStream.setStandard("STD");
            }

            udpStream.read();

            udpOut.put("UDPOut", udpStream);
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Mpeg2 to udp://127.0.0.1:7000");
            labels.put("UDPOut", label);
            ResourceMonitorFXController.getInstance().addMessage(label);
            lblOnAir_.setTextFill(javafx.scene.paint.Color.RED);
        } else {
            udpOutState = false;
            SinkUDP udpStream = udpOut.get("UDPOut");
            if (udpStream != null) {
                udpStream.stop();
                udpStream = null;
                udpOut.remove("UDPOut");
                ResourceMonitorLabelFX label = labels.get("UDPOut");
                ResourceMonitorFXController.getInstance().removeMessage(label);
            }
            if (fmeCount == 0) {
//                if (theme.equals("Caspian")) {
//                    lblOnAir_.setTextFill(javafx.scene.paint.Color.WHITE);
//                } else {
                lblOnAir_.setTextFill(javafx.scene.paint.Color.BLACK);
//                }
            }
        }
    }

    @FXML
    private void btnAddFMEAction(ActionEvent event) throws IOException {
        final FME fme = new FME();
        currFME_ = fme;
        Stage fmeDialogStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/webcamstudiofx/components/FMEDialogFX.fxml"));
        fmeDialogFX = (AnchorPane) loader.load();
        Scene scene = new Scene(fmeDialogFX);
        fmeDialogStage.setScene(scene);
        fmeDialogStage.show();
        Thread addFME = new Thread(() -> {
            while (fmeDialogStage.isShowing()) {
                Tools.sleep(100);
            }
            if (FMEDialogFXController.add.equals("ok")) {
                fmes.put(fme.getName(), fme);
                addButtonBroadcast(fme);
            }
        });
        addFME.setPriority(Thread.MIN_PRIORITY);
        addFME.start();
    }

    public void addButtonBroadcast(final FME fme) {
        final OutputPanelFXController instanceSinkFME = this;
        ToggleButton button = new ToggleButton();
        button.setText(fme.getName());
        button.setOnAction((ActionEvent actionEvent) -> {
            ToggleButton button1 = (ToggleButton) actionEvent.getSource();
            FME fme1 = fmes.get(button1.getText());
            if (button1.isSelected()) {
                if (fme1 != null) {
                    fmeOutState = true;
                    // CHECK DOUBLES !!!
                    String cleanBroad = checkDoubleBroad(button1.getText());
                    if (!"".equals(cleanBroad)) {
                        broadcastsOut.add(cleanBroad);
//                            System.out.println("broadcastsOut: "+broadcastsOut);
                    }
                    fmeCount++;
                    SinkBroadcast broadcast = new SinkBroadcast(fme1);
                    broadcast.setStandard(fme1.getStandard());
                    broadcast.setRate(MasterMixer.getInstance().getRate());
                    broadcast.setWidth(MasterMixer.getInstance().getWidth());
                    fme1.setWidth(Integer.toString(MasterMixer.getInstance().getWidth()));
                    broadcast.setHeight(MasterMixer.getInstance().getHeight());
                    fme1.setHeight(Integer.toString(MasterMixer.getInstance().getHeight()));
                    broadcast.setListener(instanceSinkFME);
                    broadcast.read();
                    broadcasts.put(button1.getText(), broadcast);
                    ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Broadcasting to " + fme1.getName());
                    labels.put(fme1.getName(), label);
                    ResourceMonitorFXController.getInstance().addMessage(label);
                    //                        System.out.println("Label On Red");
                    lblOnAir_.setTextFill(javafx.scene.paint.Color.RED);
                } else {
                    fmeCount--;
                    button1.setSelected(false);
                }
//                    System.out.println("StartFMECount = "+fmeCount);
            } else {
                fmeOutState = fmeCount > 0;
                broadcastsOut.remove(button1.getText());
                SinkBroadcast broadcast = broadcasts.get(button1.getText());
                if (broadcast != null) {
                    fmeCount--;
                    broadcast.stop();
                    broadcast.destroy();
                    broadcasts.remove(fme1.getName());
                    ResourceMonitorLabelFX label = labels.get(fme1.getName());
                    labels.remove(fme1.getName());
                    //                        System.out.println("StopFMECount = "+fmeCount);
                    ResourceMonitorFXController.getInstance().removeMessage(label);
                    if (fmeCount == 0 && !udpOutState) {
//                        if (theme.equals("Caspian")) {
//                            lblOnAir_.setTextFill(javafx.scene.paint.Color.WHITE);
//                        } else {
                        lblOnAir_.setTextFill(javafx.scene.paint.Color.BLACK);
//                        }
                    }
                }
            }
        });

        button.setOnMouseDragged((javafx.scene.input.MouseEvent event) -> {
            if (event.getX() > outputPanelArea.getWidth()) {
                ToggleButton button1 = (ToggleButton) event.getSource();
                if (!button1.isSelected() && event.getX() > outputPanelArea.getWidth()) {
                    System.out.println(button1.getText() + " removed ...");
                    SinkBroadcast broadcast = broadcasts.remove(button1.getText());
                    if (broadcast != null) {
                        MasterTracks.getInstance().unregister(broadcast);
                    }
                    fmes.remove(button1.getText());
                    labels.remove(fme.getName());
                    outputPanelArea.getChildren().remove(button1);
                }
            }
        }
        );
        button.setOnMousePressed((javafx.scene.input.MouseEvent event) -> {
            ToggleButton button1 = (ToggleButton) event.getSource();
            if (!button1.isSelected()) {
                if (event.isSecondaryButtonDown()) {
                    fmeContextMenu.show(outputPanelArea, event.getScreenX(), event.getScreenY());
                }
                currFME = fme;
            }
        }
        );

        Platform.runLater(() -> {
            outputPanelArea.getChildren().add(button);
        });

    }

    private void fmeInitPopUp() {
        MenuItem settings = new MenuItem("FME Settings");
        fmeContextMenu.getItems().add(settings);
        settings.setOnAction((ActionEvent event) -> {
            //                System.out.println("FME="+currFME);
            currFME_ = currFME;
            Stage fmeSettingsStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/webcamstudiofx/components/FMESettingsFX.fxml"));
            try {
                fmeSettingsFX = (AnchorPane) loader.load();
            } catch (IOException ex) {
                Logger.getLogger(OutputPanelFXController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Scene scene = new Scene(fmeSettingsFX);
            fmeSettingsStage.setScene(scene);
            fmeSettingsStage.show();
        });
    }

    private void sinkUDPInitPopUp() {
        MenuItem settings = new MenuItem("UDP Settings");
        udpContextMenu.getItems().add(settings);
        settings.setOnAction((ActionEvent event) -> {
            udpStream_ = udpStream;
            isFileStream = false;

            Stage fmeSettingsStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/webcamstudiofx/components/SinkSettingsFX.fxml"));
            try {
                sinkSettingsFX = (AnchorPane) loader.load();
            } catch (IOException ex) {
                Logger.getLogger(OutputPanelFXController.class.getName()).log(Level.SEVERE, null, ex);
            }
            fmeSettingsStage.setTitle(udpStream.getName() + " Settings");
            Scene scene = new Scene(sinkSettingsFX);
            fmeSettingsStage.setScene(scene);
            fmeSettingsStage.show();
        });
    }

    private void sinkFileInitPopUp() {
        MenuItem settings = new MenuItem("Record Settings");
        recordContextMenu.getItems().add(settings);
        settings.setOnAction((ActionEvent event) -> {
            fileStream_ = fileStream;
            isFileStream = true;

            Stage fmeSettingsStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/webcamstudiofx/components/SinkSettingsFX.fxml"));
            try {
                sinkSettingsFX = (AnchorPane) loader.load();
            } catch (IOException ex) {
                Logger.getLogger(OutputPanelFXController.class.getName()).log(Level.SEVERE, null, ex);
            }
            fmeSettingsStage.setTitle(fileStream.getName() + " Settings");
            Scene scene = new Scene(sinkSettingsFX);
            fmeSettingsStage.setScene(scene);
            fmeSettingsStage.show();
        });
    }

    @Override
    public void updatePreview(BufferedImage image) {
        // nothing here.  
    }

    @Override
    public void stopChTimefx(java.awt.event.ActionEvent evt) {
        // nothing here.  
    }

    @Override
    public void resetBtnStatesfx() {
        fmeCount = 0;
        broadcastsOut.clear();
    }

    @Override
    public void resetAutoPLBtnStatefx(java.awt.event.ActionEvent evt) {
        // Nothing Here
    }

    @Override
    public void resetSinksfx(java.awt.event.ActionEvent evt) {
        fileStream.destroy();
        udpStream.destroy();
        audioStream.destroy();
        f = new File(userHomeDir + "/.webcamstudiofx/Record To File");
        fileStream = new SinkFile(f);
        udpStream = new SinkUDP();
        audioStream = new SinkAudio();
        Preferences filePrefs = prefs.node("filerec");
        Preferences udpPrefs = prefs.node("udp");
        try {
            String[] servicesF = filePrefs.childrenNames();
            String[] servicesU = udpPrefs.childrenNames();

            for (String s : servicesF) {
                Preferences serviceF = filePrefs.node(s);
                fileStream.setVbitrate(serviceF.get("vbitrate", ""));
                fileStream.setAbitrate(serviceF.get("abitrate", ""));
            }

            for (String s : servicesU) {
                Preferences serviceU = udpPrefs.node(s);
                udpStream.setVbitrate(serviceU.get("vbitrate", ""));
                udpStream.setAbitrate(serviceU.get("abitrate", ""));
                udpStream.setStandard(serviceU.get("standard", ""));
            }
        } catch (BackingStoreException ex) {
            Logger.getLogger(OutputPanelFXController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void addLoadingTrackfx(String name) {
        outputPanelArea.getChildren().stream().filter((c) -> (c instanceof ToggleButton)).map((c) -> (ToggleButton) c).filter((b) -> (b.getText().contains(name))).forEach((b) -> {
            b.fire();
        }); // At this moment this workaround. After will make the proper fix.
    }

    @Override
    public void removeTracksfx(String removeSc, int a) {
        // Nothing Here
    }

    @Override
    public void setRemoteOnfx() {
        // Nothing Here
    }

    @Override
    public void closeItsTrackfx(String name) {
        // Nothing Here
    }

    @Override
    public void resetButtonsStates() {
        fmeCount = 0;
        broadcastsOut.clear();
    }

    @Override
    public void requestReset() {
        audioOutSwitch = audioOutState;
        udpOutSwitch = udpOutState;
        if (fmeOutState) {
            fmeOutSwitch = true;
            fmeCount = 0;
        } else {
            fmeOutSwitch = false;
        }
        lblOnAir_.setTextFill(javafx.scene.paint.Color.BLACK);
    }

    @Override
    public void requestStop() {
        audioOutSwitch = false;
        udpOutSwitch = false;
        fmeOutSwitch = false;
    }

    @Override
    public void requestStart() {
        String[] currentBroadcasts = new String[broadcastsOut.size()];
        currentBroadcasts = broadcastsOut.toArray(currentBroadcasts);
        if (audioOutSwitch) {
            tglAudioOut.fire();
//            tglAudioOut.setSelected(true);
        }
        if (udpOutSwitch) {
            tglUDP.fire();
//            tglUDP.setSelected(true);
        }
        if (fmeOutSwitch) {
            for (String bro : currentBroadcasts) {
                outputPanelArea.getChildren().stream().filter((c) -> (c instanceof ToggleButton)).map((c) -> (ToggleButton) c).filter((b) -> (b.getText().equals(bro))).forEach((b) -> {
                    b.fire();
//                    b.setSelected(true);
                });
            }
        }
    }

    @Override
    public void resetFMECount() {
        fmeCount = 0;
    }
}
