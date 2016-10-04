/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import javax.swing.JPopupMenu;
import static webcamstudiofx.TrackPanelFXController.ProgressIndicatorBar.setTotalWork;
import static webcamstudiofx.StreamPanelFXController.setListenerTPX;
import static webcamstudiofx.WebcamStudioFX.TS;
import static webcamstudiofx.TruckliststudioUIController.setListenerTSfxTPfx;
import webcamstudiofx.components.ResourceMonitorFXController;
import webcamstudiofx.components.ResourceMonitorLabelFX;
import webcamstudiofx.components.SpinnerAutoCommit;
import webcamstudiofx.mixers.PrePlayer;
import webcamstudiofx.mixers.SystemPlayer;
import webcamstudiofx.remote.Listener;
import webcamstudiofx.remote.WebRemote;
import webcamstudiofx.streams.SourceTrack;
import webcamstudiofx.streams.SourceWebcam;
import webcamstudiofx.streams.Stream;
import webcamstudiofx.studio.Studio;
import webcamstudiofx.tracks.MasterTracks;
import webcamstudiofx.util.Tools;

/**
 * FXML Controller class
 *
 * @author elli
 */
public class TrackPanelFXController implements Initializable, TruckliststudioUIController.Listener, Studio.Listener, Listener, StreamPanelFXController.Listener {

    public static ResourceMonitorFXController resMon;
    SpinnerValueFactory valJumpPos = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
    public static SpinnerValueFactory valTrkDuration = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
    String selectTrack = null;
    final ContextMenu remoteContextMenu = new ContextMenu();

    public static MasterTracks master = MasterTracks.getInstance();
    public static final ArrayList<Integer> CHTimers = new ArrayList<>();
    public static final ArrayList<String> arrayListTracks = new ArrayList<>();
    private WebRemote remote;
    public ArrayList<Stream> streamS = MasterTracks.getInstance().getStreams();
    public int trkOn = 0;
    public String trkNxName = null;
    public static int trkNextTime = 0;
    public static int timeToTimer = 0;
    public static int totalToTimer = 0;
    static int CHTimer = 0;
    public Timer trkT = new Timer();
    String CHptS = null;
    public Boolean stopTrkPt = false;
    public static boolean inTimer = false;
    JPopupMenu remotePopup = new JPopupMenu();
    private static String remUser = "webcamstudiofx";
    private static String remPsw = "webcamstudiofx";
    private static int remPort = 8000;
    Preferences preferences = Preferences.userNodeForPackage(this.getClass());
    int playingIndex = 0;
    protected static Timeline countDown;
    public static String lblPlayingTrack = "";
    static ProgressIndicatorBar trkProgressTime;

    private SpinnerAutoCommit<Integer> spinJumpPos = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinTrkDuration = new SpinnerAutoCommit<>();

    final public static ReadOnlyDoubleWrapper workDone = new ReadOnlyDoubleWrapper();
    HBox resourceMonitorFX;
    public static ListView<String> list_;
    AnchorPane remoteSettingsFX;

    @FXML
    private Pane resourcePanel;
    @FXML
    public ToggleButton tglStartTrack;
    @FXML
    private Button btnSkipTrack;
    @FXML
    private Button btnStopOnlyStreams;
    @FXML
    private Button btnStopAllStream;
    @FXML
    private Button btnUp;
    @FXML
    private Button btnDown;
    @FXML
    private Button btnJump;
    @FXML
    private Button btnDuplicateTrk;
    @FXML
    private Button btnRemove;
    @FXML
    private Button btnClearAllTrk;
    @FXML
    private Button btnUpdate;
    @FXML
    private AnchorPane anchorBar;
    @FXML
    private ToggleButton tglRemote;
    @FXML
    private AnchorPane TrackPanelFX;
    @FXML
    private ListView<String> list;

    @FXML
    public javafx.scene.control.Label lblOnAir;
    @FXML
    private ToolBar trackToolBar;

    public static javafx.scene.control.Label lblOnAir_;
    @FXML
    private Pane sepPane;
    @FXML
    private Pane sepListPane;
    @FXML
    private Button btnAdd;
    @FXML
    private TextField txtName;

    private void spinTrkDurationStateChanged() {
        CHTimer = valTrkDuration.getValue().hashCode() * 1000;
//        System.out.println(CHTimer);
        if (list.getSelectionModel().getSelectedIndex() != -1) {
            int ChIndex = list.getSelectionModel().getSelectedIndex();
            CHTimers.set(ChIndex, CHTimer);
        }
    }

    @FXML
    private void btnAddAction(ActionEvent event) {
        String name = txtName.getText();
        boolean noDuplicateCh = true;
        boolean noTrack = false;
        for (String chName : arrayListTracks){
            if (name.equals(chName)){
                noDuplicateCh = false;
                break;
            }
        }
        if (arrayListTracks.isEmpty()) {
            noTrack = true;
        }
        if (noDuplicateCh) {
            if (name.length() > 0) {
//                stream.setIsPlaying(true);
//                stream.setisATrack(true);
//                stream.setTrkName(sourceName);
                String playingTrack = lblPlayingTrack;
                if (playingTrack.isEmpty()) {
                    master.addTrack(name);
                } else {
//                    System.out.println("PlayingTrack="+playingTrack);
                    master.addPlayTrack(name, lblPlayingTrack);
                }
                list_.getItems().add(name);
                CHTimers.add(0);
                arrayListTracks.add(name);
                if (noTrack) {
                    list_.getSelectionModel().select(0);
                }
                int index = list_.getSelectionModel().getSelectedIndex();
                forceListRefreshOn(list_);
                list_.getSelectionModel().select(index);

//                stream.setIsPlaying(false);
            }
        } else {
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 5000, "Track " + name + " Duplicated !!!");
            ResourceMonitorFXController.getInstance().addMessage(label);
        }
//        if (name.length() > 0 && noDuplicateCh) {
//            master.addTrack(name);
//            master.addTrkTransitions(name);
//            model.addElement(name);
//            aModel.addElement(name);
//            CHCurrNext.add(name);
//            CHTimers.add(CHTimer);
//            listTrack.add(name);
////            lstChannels.revalidate();
//            lstNextChannel.revalidate();
//            lstChannels.setSelectedValue(name, true);
//        } else {
//            if (!noDuplicateCh){
//                ResourceMonitorLabel label = new ResourceMonitorLabel(System.currentTimeMillis()+10000, "Channel "+name+" Duplicated !!!");
//                ResourceMonitor.getInstance().addMessage(label);
//            }
//        }
    }

    public interface Listener {

        public void resetButtonsStates();

        public void requestReset();

        public void requestStop();

        public void requestStart();
    }

    public static Listener listenerCPOP = null;

    public static void setListenerCPOPanelFX(Listener l) {
        listenerCPOP = l;
    }

    static Listener listenerCPMP = null;

    public static void setListenerCPMPanel(Listener l) {
        listenerCPMP = l;
    }
    final ObservableList<String> listTrack = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final TrackPanelFXController instanceTrkPnl = this;
        setListenerTPX(instanceTrkPnl);
        setListenerTSfxTPfx(instanceTrkPnl);
        tglRemote.getStylesheets().add(getClass().getResource(
                "/webcamstudiofx/imagetoggleRemote.css"
        ).toExternalForm());
        lblOnAir_ = lblOnAir;
        list_ = list;
        remote = new WebRemote(instanceTrkPnl);
        Studio.setListener(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/webcamstudiofx/components/ResourceMonitorFX.fxml"));
        try {
            resourceMonitorFX = (HBox) loader.load();
        } catch (IOException ex) {
            Logger.getLogger(TrackPanelFXController.class.getName()).log(Level.SEVERE, null, ex);
        }

        loadPrefs();

        spinTrkDuration.editableProperty().set(true);
        spinTrkDuration.setLayoutX(872.0);
        spinTrkDuration.setLayoutY(58.0);
        spinTrkDuration.setPrefHeight(26.0);
        spinTrkDuration.setPrefWidth(93.0);
        AnchorPane.setRightAnchor(spinTrkDuration, 14.0);
        TrackPanelFX.getChildren().add(spinTrkDuration);
        spinTrkDuration.setVisible(true);

        spinJumpPos.editableProperty().set(true);
        spinJumpPos.setPrefHeight(26.0);
        spinJumpPos.setPrefWidth(71.0);
        trackToolBar.getItems().add(3, spinJumpPos);
        spinJumpPos.setVisible(true);

        remoteInitPopUp();

        list.setCellFactory((ListView<String> p) -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String t, boolean bln) {
                    super.updateItem(t, bln);
                    if (bln) {
                        Platform.runLater(() -> {
                            setText(null);
                            setGraphic(null);
                        });
                    } else if (t != null) {
                        Platform.runLater(() -> {
                            setText("[" + (getIndex() + 1) + "] " + t);
                        });
                        setFont(new javafx.scene.text.Font("Verdana", 18));
                        if (t.equals(lblPlayingTrack)) {
                            setTextFill(javafx.scene.paint.Color.RED.darker());
                        } else {
                            setTextFill(javafx.scene.paint.Color.BLACK);

                        }
                    }
                }
            };
            return cell;
        });

        list.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (list.getSelectionModel().getSelectedIndex() != -1) {
                selectTrack = list.getSelectionModel().getSelectedItem();
                int SelectCHIndex = list.getSelectionModel().getSelectedIndex();
                if (lblPlayingTrack.equals(selectTrack)) {
                    btnRemove.setDisable(true);
                } else {
                    btnRemove.setDisable(false);
                }
//                    System.out.println("Track="+selectTrack);
                valTrkDuration.setValue(CHTimers.get(SelectCHIndex) / 1000);
                tglRemote.setDisable(false);
            } else {
                tglRemote.setDisable(true);
            }

            int index = list.getSelectionModel().getSelectedIndex();
            forceListRefreshOn(list);
            list.getSelectionModel().select(index);
        });

        list.setOnMouseClicked((MouseEvent click) -> {
            if (click.getClickCount() == 2) {
                if (!tglStartTrack.isSelected()) {
                    if (list.getSelectionModel().getSelectedIndex() != -1) {
                        String name = list.getSelectionModel().getSelectedItem();
                        master.selectTrack(name);
                        savePrefs();
                        System.out.println("Playing: " + name);
//                        System.out.println("Before=" + lblPlayingTrack);
                        lblPlayingTrack = name;
//                        System.out.println("After=" + lblPlayingTrack);
                        btnRemove.setDisable(true);
                        tglRemote.setDisable(false);

                        if (CHTimers.get(list.getSelectionModel().getSelectedIndex()) != 0) {
                            inTimer = true;
                            trkT = new Timer();
                            trkT.schedule(new TSelectActionPerformed(), CHTimers.get(list.getSelectionModel().getSelectedIndex()));
                            trkNextTime = CHTimers.get(list.getSelectionModel().getSelectedIndex());
                            totalToTimer = trkNextTime / 1000;
                            stopTrkPt = false;
                            trkT.schedule(new UpdateCHtUITask(), 0);
                        }
                    }
                    tglStartTrack.setSelected(true);
                    updateTrackOn();
                } else {
                    if (trkOn != list.getSelectionModel().getSelectedIndex()) {
                        RemoteStopCHTimerActionPerformed();
                        master.stopTextCDown();
                        Tools.sleep(100);

                        if (list.getSelectionModel().getSelectedIndex() != -1) {
                            String name = list.getSelectionModel().getSelectedItem();
                            master.selectTrack(name);
                            savePrefs();
                            System.out.println("Playing: " + name);
                            lblPlayingTrack = name;
                            btnRemove.setDisable(true);
                            tglRemote.setDisable(false);

                            if (CHTimers.get(list.getSelectionModel().getSelectedIndex()) != 0) {
                                inTimer = true;
                                trkT = new Timer();
                                trkT.schedule(new TSelectActionPerformed(), CHTimers.get(list.getSelectionModel().getSelectedIndex()));
                                trkNextTime = CHTimers.get(list.getSelectionModel().getSelectedIndex());
                                totalToTimer = trkNextTime / 1000;
                                stopTrkPt = false;
                                trkT.schedule(new UpdateCHtUITask(), 0);
                            }
                        }

                    }
                    tglStartTrack.setSelected(true);
                    updateTrackOn();
                }

                int index = list.getSelectionModel().getSelectedIndex();
                forceListRefreshOn(list);
                list.getSelectionModel().select(index);
            }
        });

        valJumpPos.valueProperty().addListener((obs, oldValue, newValue)
                -> spinJumpPosStateChanged());
        spinJumpPos.setValueFactory(valJumpPos);

        spinTrkDuration.valueProperty().addListener((obs, oldValue, newValue)
                -> spinTrkDurationStateChanged());
        spinTrkDuration.setValueFactory(valTrkDuration);

        int TOTAL_WORK = 1;
        final String WORK_DONE_LABEL_FORMAT = "%.0f";

        trkProgressTime = new ProgressIndicatorBar(
                workDone.getReadOnlyProperty(),
                TOTAL_WORK,
                WORK_DONE_LABEL_FORMAT
        );

        AnchorPane.setTopAnchor(trkProgressTime, 0.0);
        AnchorPane.setBottomAnchor(trkProgressTime, 0.0);
        AnchorPane.setLeftAnchor(trkProgressTime, 0.0);
        AnchorPane.setRightAnchor(trkProgressTime, 0.0);
        anchorBar.getChildren().add(trkProgressTime);
        trkProgressTime.setVisible(true);

        AnchorPane.setTopAnchor(resourceMonitorFX, 0.0);
        AnchorPane.setBottomAnchor(resourceMonitorFX, 0.0);
        AnchorPane.setLeftAnchor(resourceMonitorFX, 0.0);
        AnchorPane.setRightAnchor(resourceMonitorFX, 0.0);
        resourcePanel.getChildren().add(resourceMonitorFX);

        tglRemote.setOnMousePressed((javafx.scene.input.MouseEvent event) -> {
            ToggleButton button = ((ToggleButton) event.getSource());
            if (!button.isSelected()) {
                if (event.isSecondaryButtonDown()) {
                    remoteContextMenu.show(TrackPanelFX, event.getScreenX(), event.getScreenY());
                }
            }
        });

    }

    private void remoteInitPopUp() {
        MenuItem settings = new MenuItem("Remote Settings");
        remoteContextMenu.getItems().add(settings);
        settings.setOnAction((ActionEvent event) -> {
//            udpStream_ = udpStream;
//            isFileStream = false;

            Stage fmeSettingsStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/webcamstudiofx/components/RemoteSettingsFX.fxml"));
            try {
                remoteSettingsFX = (AnchorPane) loader.load();
            } catch (IOException ex) {
                Logger.getLogger(OutputPanelFXController.class.getName()).log(Level.SEVERE, null, ex);
            }
//            fmeSettingsStage.setTitle(udpStream.getName() + " Settings");
            Scene scene = new Scene(remoteSettingsFX);
            fmeSettingsStage.setScene(scene);
            fmeSettingsStage.show();
        });
    }

    public static void setRemPsw(String psw) {
        remPsw = psw;
    }

    public static String getRemPsw() {
        return remPsw;
    }

    public static void setRemUsr(String usr) {
        remUser = usr;
    }

    public static String getRemUsr() {
        return remUser;
    }

    public static void setRemPort(int port) {
        remPort = port;
    }

    public static int getRemPort() {
        return remPort;
    }

    public static void setInTimer(boolean inT) {
        inTimer = inT;
    }

    public static boolean getInTimer() {
        return inTimer;
    }

    @SuppressWarnings("unchecked")
    public static void makeATrack(Stream stream) {
        String sourceName = stream.getName();
        boolean noDuplicateCh = true;
        boolean noTrack = false;
        for (String chName : arrayListTracks) {
            if (sourceName.equals(chName)) {
                noDuplicateCh = false;
                break;
            }
        }
        if (arrayListTracks.isEmpty()) {
            noTrack = true;
        }
        if (noDuplicateCh) {
            if (sourceName.length() > 0) {
                stream.setIsPlaying(true);
                stream.setisATrack(true);
                stream.setTrkName(sourceName);
                String playingTrack = lblPlayingTrack;
                if (playingTrack.isEmpty()) {
                    master.addTrack(sourceName);
                } else {
//                    System.out.println("PlayingTrack="+playingTrack);
                    master.addPlayTrack(sourceName, lblPlayingTrack);
                }
                list_.getItems().add(sourceName);

                if (!(stream instanceof SourceWebcam)) {
                    String sPrepTime = stream.getStreamTime().replaceAll("s", "");
                    int sDuration = Integer.parseInt(sPrepTime);
                    int chTimer = sDuration * 1000;
                    CHTimers.add(chTimer);
                } else {
                    CHTimers.add(0);
                }

                arrayListTracks.add(sourceName);
                if (noTrack) {
                    list_.getSelectionModel().select(0);
                }
                int index = list_.getSelectionModel().getSelectedIndex();
                forceListRefreshOn(list_);
                list_.getSelectionModel().select(index);

                stream.setIsPlaying(false);
            }
        } else {
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 5000, "Track " + sourceName + " Duplicated !!!");
            ResourceMonitorFXController.getInstance().addMessage(label);
        }
    }

    @SuppressWarnings("unchecked")
    public static void makeMultipleTracks(ArrayList<Stream> streamList) {
        boolean noTrack = false;
        ObservableList<String> impList = FXCollections.observableArrayList();
        if (arrayListTracks.isEmpty()) {
            noTrack = true;
        } else {
            arrayListTracks.stream().forEach((s) -> {
                impList.add(s);
            });
        }
        for (Stream stream : streamList) {
            String sourceName = stream.getName();
            boolean noDuplicateCh = true;
//            boolean noTrack = false;
            for (String chName : arrayListTracks) {
                if (sourceName.equals(chName)) {
                    noDuplicateCh = false;
                    break;
                }
            }
//            if (arrayListTracks.isEmpty()) {
//                noTrack = true;
//            }
            if (noDuplicateCh) {
                if (sourceName.length() > 0) {
                    stream.setIsPlaying(true);
                    stream.setisATrack(true);
                    stream.setTrkName(sourceName);
                    String playingTrack = lblPlayingTrack;
                    if (playingTrack.isEmpty()) {
                        master.addTrack(sourceName);
                    } else {
//                    System.out.println("PlayingTrack="+playingTrack);
                        master.addPlayTrack(sourceName, lblPlayingTrack);
                    }
//                    list_.getItems().add(sourceName);

                    impList.add(stream.getName());

                    String sPrepTime = stream.getStreamTime().replaceAll("s", "");
                    int sDuration = Integer.parseInt(sPrepTime);
                    int chTimer = sDuration * 1000;
                    CHTimers.add(chTimer);
                    arrayListTracks.add(sourceName);
//                    if (noTrack) {
//                        list_.getSelectionModel().select(0);
//                    }
//                    int index = list_.getSelectionModel().getSelectedIndex();
//                    forceListRefreshOn(list_);
//                    list_.getSelectionModel().select(index);

                    stream.setIsPlaying(false);
                }
            } else {
                ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 5000, "Track " + sourceName + " Duplicated !!!");
                ResourceMonitorFXController.getInstance().addMessage(label);
            }
        }

        if (noTrack) {
            list_.setItems(impList);
            list_.getSelectionModel().select(0);
        } else {
            int index = list_.getSelectionModel().getSelectedIndex();
            list_.setItems(impList);
            forceListRefreshOn(list_);
            list_.getSelectionModel().select(index);
        }
    }

    public static <String> void forceListRefreshOn(ListView<String> lsv) {
        ObservableList<String> items = lsv.<String>getItems();
        lsv.<String>setItems(null);
        lsv.<String>setItems(items);
    }

    public static class TimeLine {

        public void TimeLine(int TOTAL_WORK) {
            countDown = new Timeline(
                    new KeyFrame(Duration.seconds(0), new KeyValue(workDone, TOTAL_WORK / 1000)),
                    new KeyFrame(Duration.seconds(TOTAL_WORK / 1000), new KeyValue(workDone, 0))
            );
//            System.out.println(workDone.getValue());
        }
    }

    public static class UpdateCHtUITask extends TimerTask {

        @Override
        public void run() {
            new TimeLine().TimeLine(trkNextTime);
            setTotalWork(trkNextTime / 1000);
            countDown.playFromStart();

        }
    }

    private void loadPrefs() {
        remUser = preferences.get("remoteuser", "webcamstudiofx");
        remPsw = preferences.get("remotepsw", "webcamstudiofx");
        remPort = preferences.getInt("remoteport", 8000);
        remote.setPort(remPort);
    }

    public void savePrefs() {
        preferences.put("remoteuser", remUser);
        preferences.put("remotepsw", remPsw);
        preferences.putInt("remoteport", remPort);
    }

    private void spinJumpPosStateChanged() {
        int listSize = list.getItems().size();
        if (listSize > 0) {
            if (spinJumpPos.getValue() > listSize) {
                valJumpPos.setValue(listSize);
            }
        }
    }

    @Override
    public void stopChTimefx(java.awt.event.ActionEvent evt) {
        RemoteStopCHTimerActionPerformed();
    }

    @Override
    public void resetBtnStatesfx() {
        btnRemove.setDisable(false);
        lblPlayingTrack = "";
//        if (theme.equals("Caspian")) {
//            lblOnAir_.setTextFill(javafx.scene.paint.Color.WHITE);
//        } else {
        lblOnAir_.setTextFill(javafx.scene.paint.Color.BLACK);
//        }
        arrayListTracks.clear();
        CHTimers.clear();
        valTrkDuration.setValue(0);
    }

    public void updateTrackOn() {
        int index = 0;
        for (String currPlaying : arrayListTracks) {
            if (currPlaying.equals(lblPlayingTrack)) {
                trkOn = index;
                break;
            }
            index++;
        }
    }

    public static String getSelectedTrack() {
        return (list_.getSelectionModel().getSelectedItem());
    }

    @Override
    public void resetAutoPLBtnStatefx(java.awt.event.ActionEvent evt) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetSinksfx(java.awt.event.ActionEvent evt) { // used for remote start
        tglStartTrack.fire();
    }

    @Override
    public void addLoadingTrackfx(String name) {
        boolean noDuplicateTrk = true;
        for (String chName : arrayListTracks) {
            if (name.equals(chName)) {
                noDuplicateTrk = false;
                break;
            }
        }
        if (noDuplicateTrk) {
            if (name.length() > 0) {
                listTrack.add(name);
                list.setItems(listTrack);
                System.out.println("Adding " + name);
                arrayListTracks.add(name);
            }
        } else {
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Track " + name + " Duplicated !!!");
            ResourceMonitorFXController.getInstance().addMessage(label);
        }
    }

    @Override
    public void removeTracksfx(String removeSc, int a) {
        list.getItems().remove(removeSc);
        CHTimers.remove(a);
        arrayListTracks.remove(removeSc);
    }

    @Override
    public void setRemoteOnfx() {
        tglRemote.fire();
    }

    @Override
    public void closeItsTrackfx(String name) {
        ArrayList<Stream> allStreams = MasterTracks.getInstance().getStreams();
        allStreams.stream().filter((s) -> (s.getisATrack())).filter((s) -> (s.getTrkName().equals(name))).forEach((s) -> {
            s.setisATrack(false);
//                    System.out.println("StreamName="+s.getName());
//                    System.out.println("IsaTrack="+s.getisATrack());
        });
        boolean isATrack = false;
        int SelectCHIndex = 0;
        for (String currClosing : arrayListTracks) {
            if (currClosing.equals(name)) {
                isATrack = true;
                break;
            }
            SelectCHIndex++;
        }
//        System.out.println(SelectCHIndex);
        if (isATrack) {
            if (SelectCHIndex == 0 && list.getItems().size() > 1) {
                master.removeTrack(name);
                list.getItems().remove(name);
                CHTimers.remove(SelectCHIndex);
//                valTrkDuration.setValue(0);
                arrayListTracks.remove(name);
                int index = list.getSelectionModel().getSelectedIndex();
                forceListRefreshOn(list);
                list.getSelectionModel().select(index);
            } else {
                master.removeTrack(name);
                list.getItems().remove(name);
                CHTimers.remove(SelectCHIndex);
//                valTrkDuration.setValue(0);
                arrayListTracks.remove(name);
                int index = list.getSelectionModel().getSelectedIndex();
                forceListRefreshOn(list);
                list.getSelectionModel().select(index);
            }
        }
    }

    public void RemoteStopCHTimerActionPerformed() {
        stopTrkPt = true;
        trkT.cancel();
        trkT.purge();
        list.setDisable(false);
        spinTrkDuration.setDisable(false);
        inTimer = false;
        if (countDown != null) {
            countDown.stop();
        }
        trkProgressTime.bar.setProgress(0);
        trkProgressTime.text.setText("0");
        tglStartTrack.setSelected(false);
    }

    public void RemoteStopCHTimerOnlyActionPerformed() {
        trkT.cancel();
        trkT.purge();
        stopTrkPt = true;
        inTimer = false;
        setTotalWork(0);
        if (countDown != null) {
            countDown.stop();
        }
        trkProgressTime.bar.setProgress(0);
        trkProgressTime.text.setText("0");
    }

    @Override
    public ArrayList<Integer> getCHTimers() {
        return CHTimers;
    }

    @Override
    public void requestStart() {
//        tglStartTrack.setSelected(true);
        tglStartTrack.fire();
        listenerCPOP.requestStart();
    }

    @Override
    public void requestStop() {
        btnStopOnlyStreams.fire();
        listenerCPOP.requestStop();
    }

    @Override
    public void requestReset() {
        listenerCPMP.requestReset();
        listenerCPOP.requestReset();
    }

    @Override
    public String requestlogin(String login) {
        String res = "";
        String[] loginSplit = login.split("\\?");
        String userPsw = loginSplit[1].replace("j_username=", "");
        userPsw = userPsw.replace("j_password=", "");
        userPsw = userPsw.replace(" HTTP/1.1", "");
//        System.out.println("userPsw: "+userPsw);
        if (!userPsw.equals("&")) {
            String[] userPswSplit = userPsw.split("&");
            if (!userPsw.equals("&")) {
                if (userPswSplit[0].equals(remUser) && userPswSplit[1].equals(remPsw)) {
                    boolean play = false;
                    for (Stream stream : streamS) {
                        if (!stream.getClass().toString().contains("Sink")) {
                            if (stream.isPlaying()) {
                                play = true;
                            }
                        }
                    }
                    if (play) {
                        res = "/run";
                    } else {
                        res = "/stop";
                    }
                } else {
                    res = "/error";
                }
            }
        } else {
            res = "/login";
        }
        return res;
    }

    @Override
    public void listening(String localURL) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void startItsTrack(String name) {
        RemoteStopCHTimerActionPerformed();
        master.stopTextCDown();
        Tools.sleep(100);
        lblPlayingTrack = name;
        updateTrackOn();
        master.selectTrack(name);
        savePrefs();
        System.out.println("Playing: " + name);
        lblPlayingTrack = name;
        btnRemove.setDisable(true);
        tglRemote.setDisable(false);
        tglStartTrack.setSelected(true);
        int index = list.getSelectionModel().getSelectedIndex();
        forceListRefreshOn(list);
        list.getSelectionModel().select(index);

        if (CHTimers.get(trkOn) != 0) {
            inTimer = true;
            trkT = new Timer();
            trkT.schedule(new TSelectActionPerformed(), CHTimers.get(trkOn));
            trkNextTime = CHTimers.get(trkOn);
            totalToTimer = trkNextTime / 1000;
            stopTrkPt = false;
            trkT.schedule(new UpdateCHtUITask(), 0);
        }
    }

    @Override
    public void stopItsTrack() {
//        RemoteStopCHTimerActionPerformed();
        master.stopTextCDown();
        if (countDown != null) {
            countDown.stop();
        }
        trkProgressTime.bar.setProgress(0);
        trkProgressTime.text.setText("0");

        Tools.sleep(100);
        ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Track Stopped.");
        ResourceMonitorFXController.getInstance().addMessage(label);
        tglStartTrack.setSelected(false);
        int index = list.getSelectionModel().getSelectedIndex();
        forceListRefreshOn(list);
        list.getSelectionModel().select(index);
    }

    public static class ProgressIndicatorBar extends StackPane {

        private ReadOnlyDoubleProperty workDone;
        private static double totalWork;

        final public ProgressBar bar = new ProgressBar();
        final private Text text = new Text();
        final private String labelFormatSpecifier;

        final private static int DEFAULT_LABEL_PADDING = 5;

        ProgressIndicatorBar(ReadOnlyDoubleProperty workDone, final double totalWork, final String labelFormatSpecifier) {
            this.workDone = workDone;
            this.totalWork = totalWork;
            this.labelFormatSpecifier = labelFormatSpecifier;

            syncProgress();
            workDone.addListener((ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
                syncProgress();
            });

            bar.setMaxWidth(Double.MAX_VALUE); // allows the progress bar to expand to fill available horizontal space.
            bar.setMaxHeight(Double.MAX_VALUE);
            bar.setStyle("-fx-accent: red;");
            text.setFont(javafx.scene.text.Font.font("Verdana", 28));

            getChildren().setAll(bar, text);
        }

        public static void setTotalWork(int work) {
            totalWork = work;
        }

        // synchronizes the progress indicated with the work done.
        public void syncProgress() {
            if (workDone == null || totalWork == 0) {
                text.setText("");
                bar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
            } else {
                text.setText(String.format(labelFormatSpecifier, Math.ceil(workDone.get())));

                bar.setProgress(workDone.get() / totalWork);
            }

            bar.setMinHeight(text.getBoundsInLocal().getHeight() + DEFAULT_LABEL_PADDING * 2);
            bar.setMinWidth(text.getBoundsInLocal().getWidth() + DEFAULT_LABEL_PADDING * 2);
        }
    }

    public class TSelectActionPerformed extends TimerTask {

        @Override
        public void run() {
            updateTrackOn();
//            System.out.println("IndexPlaying="+trkOn+" PlaylistSize="+arrayListTracks.size());
            if (trkOn == arrayListTracks.size() - 1) {
                trkNxName = arrayListTracks.get(0);
            } else {
                trkNxName = arrayListTracks.get(trkOn + 1);
            }
            int n = 0;
            for (String h : arrayListTracks) {
                if (h.equals(trkNxName)) {
                    trkNextTime = CHTimers.get(n);
                }
                n += 1;
            }
            totalToTimer = trkNextTime / 1000;
            list.getSelectionModel().select(trkNxName);
            list.scrollTo(trkNxName);
            master.selectTrack(trkNxName);
            String name = list.getSelectionModel().getSelectedItem();
            System.out.println("Playing: " + name);
            lblPlayingTrack = name;
//            boolean noDuplicateCh = true;
            int index = list.getSelectionModel().getSelectedIndex();
            forceListRefreshOn(list);
            list.getSelectionModel().select(index);
            if (trkNextTime != 0) {
                trkT = new Timer();
                trkT.schedule(new TSelectActionPerformed(), trkNextTime);
                trkNextTime = CHTimers.get(list.getSelectionModel().getSelectedIndex());
                stopTrkPt = false;
                trkT.schedule(new UpdateCHtUITask(), 0);
            } else {
                trkT.cancel();
                trkT.purge();
                stopTrkPt = true;
                list.setDisable(false);
                spinTrkDuration.setDisable(false);
                inTimer = false;
            }
            btnRemove.setDisable(true);
        }
    }

    @FXML
    private void tglStartTrackAction(ActionEvent event) {
        if (tglStartTrack.isSelected()) {
            if (list.getSelectionModel().getSelectedIndex() != -1) {
                String name = list.getSelectionModel().getSelectedItem();
                master.selectTrack(name);
                savePrefs();
                System.out.println("Playing: " + name);
                lblPlayingTrack = name;
                btnRemove.setDisable(true);
                tglRemote.setDisable(false);

                if (CHTimers.get(list.getSelectionModel().getSelectedIndex()) != 0) {
                    inTimer = true;
                    trkT = new Timer();
                    trkT.schedule(new TSelectActionPerformed(), CHTimers.get(list.getSelectionModel().getSelectedIndex()));
                    trkNextTime = CHTimers.get(list.getSelectionModel().getSelectedIndex());
                    totalToTimer = trkNextTime / 1000;
                    stopTrkPt = false;
                    trkT.schedule(new UpdateCHtUITask(), 0);
                }
            }
            updateTrackOn();
        } else {
            RemoteStopCHTimerActionPerformed();
            if (countDown != null) {
                countDown.stop();
            }
            trkProgressTime.bar.setProgress(0);
            trkProgressTime.text.setText("0");
            master.stopTextCDown();

//            Tools.sleep(100);
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Track Timer Stopped.");
            resMon.getInstance().addMessage(label);
        }
        int index = list.getSelectionModel().getSelectedIndex();
        forceListRefreshOn(list);
        list.getSelectionModel().select(index);
    }

    @FXML
    private void btnSkipTrackAction(ActionEvent event) {
        if (tglStartTrack.isSelected()) {
            RemoteStopCHTimerActionPerformed();
            master.stopTextCDown();
            Tools.sleep(100);
            updateTrackOn();
//            System.out.println("IndexPlaying="+trkOn+" PlaylistSize="+arrayListTracks.size());
            if (trkOn == arrayListTracks.size() - 1) {
                trkNxName = arrayListTracks.get(0);
            } else {
                trkNxName = arrayListTracks.get(trkOn + 1);
            }
            int n = 0;
            for (String h : arrayListTracks) {
                if (h.equals(trkNxName)) {
                    trkNextTime = CHTimers.get(n);
                }
                n += 1;
            }
            totalToTimer = trkNextTime / 1000;
            list.getSelectionModel().select(trkNxName);
            list.scrollTo(trkNxName);
            master.selectTrack(trkNxName);
            String name = list.getSelectionModel().getSelectedItem();
            System.out.println("Playing: " + name);
            lblPlayingTrack = name;
            int index = list.getSelectionModel().getSelectedIndex();
            forceListRefreshOn(list);
            list.getSelectionModel().select(index);
            if (trkNextTime != 0) {
                trkT = new Timer();
                trkT.schedule(new TSelectActionPerformed(), trkNextTime);
                trkNextTime = CHTimers.get(list.getSelectionModel().getSelectedIndex());
                stopTrkPt = false;
                if (countDown != null) {
                    countDown.stop();
                }
                trkProgressTime.bar.setProgress(0);
                trkProgressTime.text.setText("0");
                trkT.schedule(new UpdateCHtUITask(), 0);
            } else {
                trkT.cancel();
                trkT.purge();
                stopTrkPt = true;
                list.setDisable(false);
                spinTrkDuration.setDisable(false);
                inTimer = false;
                if (countDown != null) {
                    countDown.stop();
                }
                trkProgressTime.bar.setProgress(0);
                trkProgressTime.text.setText("0");
            }
            tglStartTrack.setSelected(true);
            forceListRefreshOn(list);
            list.getSelectionModel().select(index);
        }
    }

    @FXML
    private void btnStopOnlyStreamsAction(ActionEvent event) {
        MasterTracks.getInstance().stopOnlyStream();
        streamS.stream().forEach((s) -> {
            s.updateStatus();
        });
        Tools.sleep(30);
        lblPlayingTrack = "";
        btnRemove.setDisable(false);
        if (inTimer) {
            RemoteStopCHTimerActionPerformed();
        } else {
            RemoteStopCHTimerOnlyActionPerformed();
        }
        if (tglStartTrack.isSelected()) {
            tglStartTrack.setSelected(false);
        }
        ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Streams Stopped.");
        resMon.getInstance().addMessage(label);
        int index = list.getSelectionModel().getSelectedIndex();
        forceListRefreshOn(list);
        list.getSelectionModel().select(index);
        setTotalWork(0);
        countDown.stop();
        trkProgressTime.bar.setProgress(0);
        trkProgressTime.text.setText("0");
        System.gc();
    }

    @FXML
    private void btnStopAllStreamAction(ActionEvent event) {
        SystemPlayer.getInstanceFX(null).stop();
        Tools.sleep(30);
        PrePlayer.getPreInstanceFX(null).stop();
        Tools.sleep(10);
        MasterTracks.getInstance().stopAllStream();
        streamS.stream().forEach((s) -> {
            s.updateStatus();
        });
        Tools.sleep(30);
        lblPlayingTrack = "";
        btnRemove.setDisable(false);
        if (inTimer) {
            RemoteStopCHTimerActionPerformed();
        } else {
            RemoteStopCHTimerOnlyActionPerformed();
        }
        if (tglStartTrack.isSelected()) {
            tglStartTrack.setSelected(false);
        }
        listenerCPOP.resetButtonsStates();
        ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "All Stopped.");
        resMon.getInstance().addMessage(label);
        int index = list.getSelectionModel().getSelectedIndex();
        forceListRefreshOn(list);
        list.getSelectionModel().select(index);
        setTotalWork(0);
        if (countDown != null) {
            countDown.stop();
        }
        trkProgressTime.bar.setProgress(0);
        trkProgressTime.text.setText("0");
        lblOnAir.setTextFill(javafx.scene.paint.Color.BLACK);
        System.gc();
    }

    @FXML
    private void btnUpAction(ActionEvent event) {
        if (list.getSelectionModel().getSelectedIndex() != -1) {
            int selectedCHIndex = list.getSelectionModel().getSelectedIndex();
            String selectedChName = arrayListTracks.get(selectedCHIndex);
            int selectedCHTimer = CHTimers.get(selectedCHIndex);
            int previousCHIndex;
            String previousChName;
            int previousCHTimer;
            //        System.out.println("List Channels Timers: "+CHTimers);
            if (list != null && selectedCHIndex > 0) {
                if (selectedCHIndex == 1) {
                    previousCHIndex = selectedCHIndex - 1;
                    previousChName = arrayListTracks.get(previousCHIndex);
                    previousCHTimer = CHTimers.get(previousCHIndex);
                    //                System.out.println("Master Channels Before:"+master.getTracks());
                    // Update Master Channels
                    master.removeTrackAt(selectedChName);
                    master.removeTrackAt(previousChName);
                    master.addTrackAt(selectedChName, previousCHIndex);
                    master.addTrackAt(previousChName, selectedCHIndex);
                    // Update Streams Channels
                    streamS.stream().forEach((stream) -> {
                        String streamName = stream.getClass().getName();
                        if (!streamName.contains("Sink")) {
                            SourceTrack tempSelSC = null;
                            SourceTrack tempPrevSC = null;
                            for (SourceTrack sc : stream.getTracks()) {
                                if (sc.getName().equals(selectedChName)) {
                                    tempSelSC = sc;
                                }
                                if (sc.getName().equals(selectedChName)) {
                                    tempSelSC = sc;
                                }
                                if (sc.getName().equals(previousChName)) {
                                    tempPrevSC = sc;
                                }
                            }
                            stream.addTrackAt(tempSelSC, previousCHIndex);
                            stream.addTrackAt(tempPrevSC, selectedCHIndex);
                        }
                    });
                    //                System.out.println("Master Channels After:"+master.getTracks());
                    // Update UI lists and WS lists Channels
                    list.getItems().remove(selectedChName);
                    list.getItems().remove(previousChName);
                    CHTimers.remove(selectedCHIndex);
                    CHTimers.remove(previousCHIndex);
                    arrayListTracks.remove(selectedChName);
                    arrayListTracks.remove(previousChName);
                    int index = list.getSelectionModel().getSelectedIndex();
                    forceListRefreshOn(list);
                    list.getSelectionModel().select(index);

                    list.getItems().add(previousCHIndex, selectedChName);
                    list.getItems().add(selectedCHIndex, previousChName);
                    CHTimers.add(previousCHIndex, selectedCHTimer);
                    CHTimers.add(selectedCHIndex, previousCHTimer);
                    arrayListTracks.add(previousCHIndex, selectedChName);
                    arrayListTracks.add(selectedCHIndex, previousChName);
                    forceListRefreshOn(list);
                    list.getSelectionModel().select(previousCHIndex);
//                    listTracks.setSelectedIndex(previousCHIndex);               
                } else {
                    previousCHIndex = selectedCHIndex - 1;
                    previousChName = arrayListTracks.get(previousCHIndex);
                    previousCHTimer = CHTimers.get(previousCHIndex);
                    //                System.out.println("Master Channels Before:"+master.getTracks());
                    // Update Master Channels
                    master.removeTrackAt(selectedChName);
                    master.removeTrackAt(previousChName);
                    master.addTrackAt(selectedChName, previousCHIndex);
                    master.addTrackAt(previousChName, selectedCHIndex);
                    // Update Streams Channels
                    streamS.stream().forEach((stream) -> {
                        String streamName = stream.getClass().getName();
                        if (!streamName.contains("Sink")) {
                            SourceTrack tempSelSC = null;
                            SourceTrack tempPrevSC = null;
                            for (SourceTrack sc : stream.getTracks()) {
                                if (sc.getName().equals(selectedChName)) {
                                    tempSelSC = sc;
                                }
                                if (sc.getName().equals(previousChName)) {
                                    tempPrevSC = sc;
                                }
                            }
                            stream.addTrackAt(tempSelSC, previousCHIndex);
                            stream.addTrackAt(tempPrevSC, selectedCHIndex);
                        }
                    });
                    //                System.out.println("Master Channels After:"+master.getTracks());
                    // Update UI Channels lists and WS lists
                    list.getItems().remove(selectedChName);
                    list.getItems().remove(previousChName);
                    CHTimers.remove(selectedCHIndex);
                    CHTimers.remove(previousCHIndex);
                    arrayListTracks.remove(selectedChName);
                    arrayListTracks.remove(previousChName);
                    int index = list.getSelectionModel().getSelectedIndex();
                    forceListRefreshOn(list);
                    list.getSelectionModel().select(index);

                    list.getItems().add(previousCHIndex, selectedChName);
                    list.getItems().add(selectedCHIndex, previousChName);
                    CHTimers.add(previousCHIndex, selectedCHTimer);
                    CHTimers.add(selectedCHIndex, previousCHTimer);
                    arrayListTracks.add(previousCHIndex, selectedChName);
                    arrayListTracks.add(selectedCHIndex, previousChName);
                    forceListRefreshOn(list);
                    list.getSelectionModel().select(previousCHIndex);
                }
                list.scrollTo(list.getSelectionModel().getSelectedIndex());
                updateTrackOn();
            }
        }
    }

    @FXML
    private void btnDownAction(ActionEvent event) {
        if (list.getSelectionModel().getSelectedIndex() != -1) {
            int selectedCHIndex = list.getSelectionModel().getSelectedIndex();
            String selectedChName = arrayListTracks.get(selectedCHIndex);
            int selectedCHTimer = CHTimers.get(selectedCHIndex);
            int nextCHIndex;
            String nextChName;
            int nextCHTimer;
            if (list != null && selectedCHIndex < arrayListTracks.size() - 1) {
                if (selectedCHIndex == arrayListTracks.size() - 2) {
                    nextCHIndex = selectedCHIndex + 1;
                    nextChName = arrayListTracks.get(nextCHIndex);
                    nextCHTimer = CHTimers.get(nextCHIndex);
                    //                System.out.println("Master Channels Before:"+master.getTracks());
                    // Update Master Channels
                    master.removeTrackAt(selectedChName);
                    master.removeTrackAt(nextChName);
                    master.addToTracks(nextChName);
                    master.addToTracks(selectedChName);
                    // Update Streams Channels
                    streamS.stream().forEach((stream) -> {
                        String streamName = stream.getClass().getName();
                        if (!streamName.contains("Sink")) {
                            SourceTrack tempSelSC = null;
                            SourceTrack tempNextSC = null;
                            for (SourceTrack sc : stream.getTracks()) {
                                if (sc.getName().equals(selectedChName)) {
                                    tempSelSC = sc;
                                }
                                if (sc.getName().equals(nextChName)) {
                                    tempNextSC = sc;
                                }
                            }
                            stream.addTrackAt(tempSelSC, nextCHIndex);
                            stream.addTrackAt(tempNextSC, selectedCHIndex);
                        }
                    });
                    //                System.out.println("Master Channels After:"+master.getTracks());
                    // Update UI lists and WS lists Channels
                    list.getItems().remove(selectedChName);
                    list.getItems().remove(nextChName);
                    CHTimers.remove(selectedCHIndex);
                    CHTimers.remove(selectedCHIndex);
                    arrayListTracks.remove(selectedChName);
                    arrayListTracks.remove(nextChName);
                    int index = list.getSelectionModel().getSelectedIndex();
                    forceListRefreshOn(list);
                    list.getSelectionModel().select(index);

                    list.getItems().add(nextChName);
                    list.getItems().add(selectedChName);
                    CHTimers.add(nextCHTimer);
                    CHTimers.add(selectedCHTimer);
                    arrayListTracks.add(nextChName);
                    arrayListTracks.add(selectedChName);
                    forceListRefreshOn(list);
                    list.getSelectionModel().select(nextCHIndex);

                } else {
                    nextCHIndex = selectedCHIndex + 1;
                    nextChName = arrayListTracks.get(nextCHIndex);
                    nextCHTimer = CHTimers.get(nextCHIndex);
                    //                System.out.println("Master Channels Before:"+master.getTracks());
                    // Update Master Channels
                    master.removeTrackAt(selectedChName);
                    master.removeTrackAt(nextChName);
                    master.addTrackAt(nextChName, selectedCHIndex);
                    master.addTrackAt(selectedChName, nextCHIndex);
                    // Update Streams Channels
                    streamS.stream().forEach((stream) -> {
                        String streamName = stream.getClass().getName();
                        if (!streamName.contains("Sink")) {
                            SourceTrack tempSelSC = null;
                            SourceTrack tempNextSC = null;
                            for (SourceTrack sc : stream.getTracks()) {
                                if (sc.getName().equals(selectedChName)) {
                                    tempSelSC = sc;
                                }
                                if (sc.getName().equals(nextChName)) {
                                    tempNextSC = sc;
                                }
                            }
                            stream.addTrackAt(tempSelSC, nextCHIndex);
                            stream.addTrackAt(tempNextSC, selectedCHIndex);
                        }
                    });
                    //                System.out.println("Master Channels After:"+master.getTracks());
                    // Update UI Channels lists and WS lists
                    list.getItems().remove(selectedChName);
                    list.getItems().remove(nextChName);

                    CHTimers.remove(selectedCHIndex);
                    CHTimers.remove(selectedCHIndex);
                    //                System.out.println("List Channels Timers Removed: "+CHTimers);
                    arrayListTracks.remove(selectedChName);
                    arrayListTracks.remove(nextChName);
                    int index = list.getSelectionModel().getSelectedIndex();
                    forceListRefreshOn(list);
                    list.getSelectionModel().select(index);

                    list.getItems().add(selectedCHIndex, nextChName);
                    list.getItems().add(nextCHIndex, selectedChName);
                    CHTimers.add(selectedCHIndex, nextCHTimer);
                    CHTimers.add(nextCHIndex, selectedCHTimer);
                    //                System.out.println("List Channels Timers After: "+CHTimers);
                    arrayListTracks.add(selectedCHIndex, nextChName);
                    arrayListTracks.add(nextCHIndex, selectedChName);
                    forceListRefreshOn(list);
                    list.getSelectionModel().select(nextCHIndex);
                }
                list.scrollTo(list.getSelectionModel().getSelectedIndex());
                updateTrackOn();
            }
        }
    }

    @FXML
    private void btnJumpAction(ActionEvent event) {
        if (list.getSelectionModel().getSelectedIndex() != -1) {
            int selectedCHIndex = list.getSelectionModel().getSelectedIndex();
            String selectedChName = arrayListTracks.get(selectedCHIndex);
            int selectedCHTimer = CHTimers.get(selectedCHIndex);
            int jumpPos = spinJumpPos.getValue() - 1;

            if (selectedCHIndex > jumpPos) {
                int deltaPos = selectedCHIndex - jumpPos;
                int previousCHIndex;
                String previousChName;
                int previousCHTimer;
                for (int t = 0; t < deltaPos; t++) {
                    if (list != null && selectedCHIndex > 0) {
                        if (selectedCHIndex == 1) {
                            previousCHIndex = selectedCHIndex - 1;
                            previousChName = arrayListTracks.get(previousCHIndex);
                            previousCHTimer = CHTimers.get(previousCHIndex);
                            // Update Master Tracks
                            master.removeTrackAt(selectedChName);
                            master.removeTrackAt(previousChName);
                            master.addTrackAt(selectedChName, previousCHIndex);
                            master.addTrackAt(previousChName, selectedCHIndex);
                            // Update Streams Tracks
                            for (Stream stream : streamS) {
                                String streamName = stream.getClass().getName();
                                if (!streamName.contains("Sink")) {
                                    SourceTrack tempSelSC = null;
                                    SourceTrack tempPrevSC = null;
                                    for (SourceTrack sc : stream.getTracks()) {
                                        if (sc.getName().equals(selectedChName)) {
                                            tempSelSC = sc;
                                        }
                                        if (sc.getName().equals(selectedChName)) {
                                            tempSelSC = sc;
                                        }
                                        if (sc.getName().equals(previousChName)) {
                                            tempPrevSC = sc;
                                        }
                                    }
                                    stream.addTrackAt(tempSelSC, previousCHIndex);
                                    stream.addTrackAt(tempPrevSC, selectedCHIndex);
                                }
                            }
                            // Update UI Tracklist and TS lists Tracks
                            list.getItems().remove(selectedChName);
                            list.getItems().remove(previousChName);
                            CHTimers.remove(selectedCHIndex);
                            CHTimers.remove(previousCHIndex);
                            arrayListTracks.remove(selectedChName);
                            arrayListTracks.remove(previousChName);
                            int index = list.getSelectionModel().getSelectedIndex();
                            forceListRefreshOn(list);
                            list.getSelectionModel().select(index);

                            list.getItems().add(previousCHIndex, selectedChName);
                            list.getItems().add(selectedCHIndex, previousChName);
                            CHTimers.add(previousCHIndex, selectedCHTimer);
                            CHTimers.add(selectedCHIndex, previousCHTimer);
                            arrayListTracks.add(previousCHIndex, selectedChName);
                            arrayListTracks.add(selectedCHIndex, previousChName);
                            forceListRefreshOn(list);
                            list.getSelectionModel().select(previousCHIndex);
                        } else {
                            previousCHIndex = selectedCHIndex - 1;
                            previousChName = arrayListTracks.get(previousCHIndex);
                            previousCHTimer = CHTimers.get(previousCHIndex);
                            // Update Master Tracks
                            master.removeTrackAt(selectedChName);
                            master.removeTrackAt(previousChName);
                            master.addTrackAt(selectedChName, previousCHIndex);
                            master.addTrackAt(previousChName, selectedCHIndex);
                            // Update Streams Tracks
                            for (Stream stream : streamS) {
                                String streamName = stream.getClass().getName();
                                if (!streamName.contains("Sink")) {
                                    SourceTrack tempSelSC = null;
                                    SourceTrack tempPrevSC = null;
                                    for (SourceTrack sc : stream.getTracks()) {
                                        if (sc.getName().equals(selectedChName)) {
                                            tempSelSC = sc;
                                        }
                                        if (sc.getName().equals(previousChName)) {
                                            tempPrevSC = sc;
                                        }
                                    }
                                    stream.addTrackAt(tempSelSC, previousCHIndex);
                                    stream.addTrackAt(tempPrevSC, selectedCHIndex);
                                }
                            }
                            // Update UI Tracks lists and TS lists
                            list.getItems().remove(selectedChName);
                            list.getItems().remove(previousChName);
                            CHTimers.remove(selectedCHIndex);
                            CHTimers.remove(previousCHIndex);
                            arrayListTracks.remove(selectedChName);
                            arrayListTracks.remove(previousChName);
                            int index = list.getSelectionModel().getSelectedIndex();
                            forceListRefreshOn(list);
                            list.getSelectionModel().select(index);
                            list.getItems().add(previousCHIndex, selectedChName);
                            list.getItems().add(selectedCHIndex, previousChName);
                            CHTimers.add(previousCHIndex, selectedCHTimer);
                            CHTimers.add(selectedCHIndex, previousCHTimer);
                            arrayListTracks.add(previousCHIndex, selectedChName);
                            arrayListTracks.add(selectedCHIndex, previousChName);
                            forceListRefreshOn(list);
                            list.getSelectionModel().select(previousCHIndex);
                        }
                    }
                    selectedCHIndex--;
                }
                list.scrollTo(list.getSelectionModel().getSelectedIndex());
                updateTrackOn();
            } else if (selectedCHIndex < jumpPos) {
                int deltaPos = jumpPos - selectedCHIndex;
                int nextCHIndex;
                String nextChName;
                int nextCHTimer;
                for (int t = 0; t < deltaPos; t++) {
                    if (list != null && selectedCHIndex < arrayListTracks.size() - 1) {
                        if (selectedCHIndex == arrayListTracks.size() - 2) {
                            nextCHIndex = selectedCHIndex + 1;
                            nextChName = arrayListTracks.get(nextCHIndex);
                            nextCHTimer = CHTimers.get(nextCHIndex);
                            // Update Master Tracks
                            master.removeTrackAt(selectedChName);
                            master.removeTrackAt(nextChName);
                            master.addToTracks(nextChName);
                            master.addToTracks(selectedChName);
                            // Update Streams Tracks
                            for (Stream stream : streamS) {
                                String streamName = stream.getClass().getName();
                                if (!streamName.contains("Sink")) {
                                    SourceTrack tempSelSC = null;
                                    SourceTrack tempNextSC = null;
                                    for (SourceTrack sc : stream.getTracks()) {
                                        if (sc.getName().equals(selectedChName)) {
                                            tempSelSC = sc;
                                        }
                                        if (sc.getName().equals(nextChName)) {
                                            tempNextSC = sc;
                                        }
                                    }
                                    stream.addTrackAt(tempSelSC, nextCHIndex);
                                    stream.addTrackAt(tempNextSC, selectedCHIndex);
                                }
                            }
                            // Update UI lists and TS lists Tracks
                            list.getItems().remove(selectedChName);
                            list.getItems().remove(nextChName);
                            CHTimers.remove(selectedCHIndex);
                            CHTimers.remove(selectedCHIndex);
                            arrayListTracks.remove(selectedChName);
                            arrayListTracks.remove(nextChName);
                            int index = list.getSelectionModel().getSelectedIndex();
                            forceListRefreshOn(list);
                            list.getSelectionModel().select(index);
                            list.getItems().add(nextChName);
                            list.getItems().add(selectedChName);
                            CHTimers.add(nextCHTimer);
                            CHTimers.add(selectedCHTimer);
                            arrayListTracks.add(nextChName);
                            arrayListTracks.add(selectedChName);
                            forceListRefreshOn(list);
                            list.getSelectionModel().select(nextCHIndex);
                        } else {
                            nextCHIndex = selectedCHIndex + 1;
                            nextChName = arrayListTracks.get(nextCHIndex);
                            nextCHTimer = CHTimers.get(nextCHIndex);
                            // Update Master Tracks
                            master.removeTrackAt(selectedChName);
                            master.removeTrackAt(nextChName);
                            master.addTrackAt(nextChName, selectedCHIndex);
                            master.addTrackAt(selectedChName, nextCHIndex);
                            // Update Streams Tracks
                            for (Stream stream : streamS) {
                                String streamName = stream.getClass().getName();
                                if (!streamName.contains("Sink")) {
                                    SourceTrack tempSelSC = null;
                                    SourceTrack tempNextSC = null;
                                    for (SourceTrack sc : stream.getTracks()) {
                                        if (sc.getName().equals(selectedChName)) {
                                            tempSelSC = sc;
                                        }
                                        if (sc.getName().equals(nextChName)) {
                                            tempNextSC = sc;
                                        }
                                    }
                                    stream.addTrackAt(tempSelSC, nextCHIndex);
                                    stream.addTrackAt(tempNextSC, selectedCHIndex);
                                }
                            }
                            // Update UI Tracks lists and TS lists
                            list.getItems().remove(selectedChName);
                            list.getItems().remove(nextChName);
                            CHTimers.remove(selectedCHIndex);
                            CHTimers.remove(selectedCHIndex);
                            arrayListTracks.remove(selectedChName);
                            arrayListTracks.remove(nextChName);
                            int index = list.getSelectionModel().getSelectedIndex();
                            forceListRefreshOn(list);
                            list.getSelectionModel().select(index);
                            list.getItems().add(selectedCHIndex, nextChName);
                            list.getItems().add(nextCHIndex, selectedChName);
                            CHTimers.add(selectedCHIndex, nextCHTimer);
                            CHTimers.add(nextCHIndex, selectedCHTimer);
                            arrayListTracks.add(selectedCHIndex, nextChName);
                            arrayListTracks.add(nextCHIndex, selectedChName);
                            forceListRefreshOn(list);
                            list.getSelectionModel().select(nextCHIndex);
                        }
                    }
                    selectedCHIndex++;
                }
                list.scrollTo(list.getSelectionModel().getSelectedIndex());
                updateTrackOn();
            }
        }
    }

    @FXML
    private void btnDuplicateTrkAction(ActionEvent event) {
        if (list.getSelectionModel().getSelectedIndex() != -1) {
            int selectedTrkIndex = list.getSelectionModel().getSelectedIndex();
            String selectedTrkName = arrayListTracks.get(selectedTrkIndex);
            int count = 0;
            count = arrayListTracks.stream().filter((trkName) -> (trkName.contains(selectedTrkName))).map((_item) -> 1).reduce(count, Integer::sum);//                    System.out.println("Counting...");
            String duplicatedTrkName = selectedTrkName + "(" + count + ")";

            boolean found = false;
            do {
                for (String trkName : arrayListTracks) {
                    if (trkName.equals(duplicatedTrkName)) {
                        count++;
                        duplicatedTrkName = selectedTrkName + "(" + count + ")";
                        found = true;
                    }
                }
            } while (found = false);

            int selectedTrkTimer = CHTimers.get(selectedTrkIndex);

            ArrayList<Stream> allStreams = MasterTracks.getInstance().getStreams();
            for (Stream s : allStreams) {
                if (!s.getClass().toString().contains("Sink")) {
//                    System.out.println("StreamName="+s.getName());
                    SourceTrack streamTrk = master.getTrack(selectedTrkName, s);
                    SourceTrack dupTrk = SourceTrack.duplicateTrack(streamTrk);
                    dupTrk.setName(duplicatedTrkName);
                    s.addTrack(dupTrk);
                }
            }

            master.addTrack2List(duplicatedTrkName);
            list.getItems().add(duplicatedTrkName);
            CHTimers.add(selectedTrkTimer);
            arrayListTracks.add(duplicatedTrkName);

            int index = list.getSelectionModel().getSelectedIndex();
            forceListRefreshOn(list);
            list.getSelectionModel().select(index);
        }

    }

    @FXML
    private void btnRemoveAction(ActionEvent event
    ) {
        boolean rem = true;
        if (list.getSelectionModel().getSelectedIndex() != -1) {
            String name = list.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(TS);
            Window window = alert.getOwner();
            alert.setX(window.getX() + window.getWidth() / 2 - alert.getWidth() / 2);
            alert.setY(window.getY() + window.getHeight() / 2 - alert.getHeight() / 2);
            alert.setTitle("TS Confirmation");
            alert.setHeaderText("Really remove \"" + name + "\" Track?");
            alert.setContentText("Info: Its Source will not be removed !!");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                rem = true;
            } else {
                rem = false;
            }
            if (rem) {
                ArrayList<Stream> allStreams = MasterTracks.getInstance().getStreams();
                allStreams.stream().filter((s) -> (s.getisATrack())).filter((s) -> (s.getTrkName().equals(name))).forEach((s) -> {
                    s.setisATrack(false);
//                    System.out.println("StreamName="+s.getName());
//                    System.out.println("IsaTrack="+s.getisATrack());
                });
                int SelectCHIndex = list.getSelectionModel().getSelectedIndex();
//        System.out.println(SelectCHIndex);
                if (SelectCHIndex == 0 && list.getItems().size() > 1) {
                    master.removeTrack(name);
                    list.getItems().remove(name);
                    CHTimers.remove(SelectCHIndex);
//                valTrkDuration.setValue(0);
                    arrayListTracks.remove(name);
                    int index = list.getSelectionModel().getSelectedIndex();
                    forceListRefreshOn(list);
                    list.getSelectionModel().select(index);
                } else {
                    master.removeTrack(name);
                    list.getItems().remove(name);
                    CHTimers.remove(SelectCHIndex);
//                valTrkDuration.setValue(0);
                    arrayListTracks.remove(name);
                    int index = list.getSelectionModel().getSelectedIndex();
                    forceListRefreshOn(list);
                    list.getSelectionModel().select(index);
                }
                updateTrackOn();
//        System.out.println("StreamDurationArray="+CHTimers.toString());
            }
        }
    }

    @FXML
    private void btnClearAllTrkAction(ActionEvent event
    ) {
        boolean clear = true;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(TS);
        Window window = alert.getOwner();
        alert.setX(window.getX() + window.getWidth() / 2 - alert.getWidth() / 2);
        alert.setY(window.getY() + window.getHeight() / 2 - alert.getHeight() / 2);
        alert.setTitle("TS Confirmation");
        alert.setHeaderText("All Tracks will be Deleted !!!");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            clear = true;
        } else {
            clear = false;
        }

        if (clear) {
            ArrayList<Stream> allStreams = MasterTracks.getInstance().getStreams();
            allStreams.stream().filter((s) -> (s.getisATrack())).forEach((s) -> {
                s.setisATrack(false);
            });
            ArrayList<String> sourceChI = MasterTracks.getInstance().getTracks();
            if (sourceChI.size() > 0) {
                do {
                    for (int a = 0; a < sourceChI.size(); a++) {
                        String removeSc = sourceChI.get(a);
                        MasterTracks.getInstance().removeTrack(removeSc);
                        removeTracksfx(removeSc, a);
                    }
                } while (sourceChI.size() > 0);
                resetBtnStatesfx();
            }
        } else {
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Delete All Tracks Cancelled!");
            ResourceMonitorFXController.getInstance().addMessage(label);
        }
    }

    @FXML
    private void btnUpdateAction(ActionEvent event
    ) {
        if (list.getSelectionModel().getSelectedIndex() != -1) {
            String name = list.getSelectionModel().getSelectedItem();
            master.updateTrack(name);
            master.addTrkTransitions(name);
            ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Track " + name + " Updated");
            ResourceMonitorFXController.getInstance().addMessage(label);
        }
    }

    @FXML
    private void tglRemoteAction(ActionEvent event) {
        if (tglRemote.isSelected()) {
            remote.setPort(remPort);
            remote.start();
        } else {
            remote.stop();
        }
    }

}
