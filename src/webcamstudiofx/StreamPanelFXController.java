/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import static webcamstudiofx.TrackPanelFXController.forceListRefreshOn;
import static webcamstudiofx.TrackPanelFXController.lblPlayingTrack;
import static webcamstudiofx.TrackPanelFXController.list_;
import static webcamstudiofx.TruckliststudioUIController.stream_;
import static webcamstudiofx.TruckliststudioUIController.wsDistroWatch;
import webcamstudiofx.components.SpinnerAutoCommit;
import webcamstudiofx.mixers.MasterMixer;
import webcamstudiofx.streams.SourceAudioSource;
import webcamstudiofx.streams.SourceImage;
import webcamstudiofx.streams.SourceImageGif;
import webcamstudiofx.streams.SourceMovie;
import webcamstudiofx.streams.SourceMusic;
import webcamstudiofx.streams.Stream;
import webcamstudiofx.util.AudioSource;
import webcamstudiofx.util.BackEnd;
import webcamstudiofx.util.PaCTL;
import webcamstudiofx.util.Tools;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class StreamPanelFXController implements Initializable, Stream.Listener, StreamDesktopFXController.Listener {

    Stream stream = null;
    private float volume = 0;
    private float vol = 0;
    int oldW;
    int oldH;
    boolean lockRatio = false;

    SpinnerValueFactory valSpinX; // = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1280);
    SpinnerValueFactory valSpinY; // = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1024);
    SpinnerValueFactory valSpinW; // = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1280);
    SpinnerValueFactory valSpinH; // = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1024);
    SpinnerValueFactory valSpinOpacity = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
    SpinnerValueFactory valSpinVolume = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 200);
    SpinnerValueFactory valSpinZOrder = new SpinnerValueFactory.IntegerSpinnerValueFactory(-10, +10);
    SpinnerValueFactory valSpinVDelay = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000);
    SpinnerValueFactory valSpinADelay = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000);
    SpinnerValueFactory valSpinSeek = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000);

    String stringPlay = "-fx-background-color: linear-gradient(greenyellow, limegreen);";
    String stringStop = "-fx-background-color: -fx-box-border, -fx-inner-border, -fx-body-color;";

    private SpinnerAutoCommit<Integer> spinX = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinY = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinW = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinH = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinOpacity = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinVolume = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinZOrder = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinVDelay = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinADelay = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinSeek = new SpinnerAutoCommit<>();

    String distro = wsDistroWatch();
    AudioSource[] sourcesAudio;
    private boolean runMe = true;
    private int speed = 1;
    Tooltip layer = new Tooltip();

    @FXML
    private AnchorPane StreamPanelFX;
    @FXML
    private Menu jmScroll;
    @FXML
    private CheckMenuItem jcbRightToLeft;
    @FXML
    private CheckMenuItem jcbLeftToRight;
    @FXML
    private CheckMenuItem jcbBottomToTop;
    @FXML
    private CheckMenuItem jcbTopToBottom;
    @FXML
    private CheckMenuItem jcbHBouncing;
    @FXML
    private Menu jmSpeed;
    @FXML
    private RadioMenuItem radioSpeed1;
    @FXML
    private RadioMenuItem radioSpeed2;
    @FXML
    private RadioMenuItem radioSpeed3;
    @FXML
    private RadioMenuItem radioSpeed4;
    @FXML
    private RadioMenuItem radioSpeed5;
    @FXML
    private Menu jmAudioSource;
    @FXML
    private Menu jmBackend;
    @FXML
    private CheckMenuItem jcbGStreamer;
    @FXML
    private CheckMenuItem jcbLibAV;
    @FXML
    private CheckMenuItem jcbFFmpeg;
    @FXML
    private Slider jslSpinX;
    @FXML
    private Slider jslSpinY;
    @FXML
    private Slider jslSpinW;
    @FXML
    private Slider jslSpinH;
    @FXML
    private Slider jslSpinOpacity;
    @FXML
    private Slider jslSpinZOrder;
    @FXML
    private Slider jslSpinVDelay;
    @FXML
    private Slider jslSpinADelay;
    @FXML
    private Slider jslSpinSeek;
    @FXML
    private ToggleButton tglAR;
    @FXML
    private ToggleButton tglAudio;
    @FXML
    private ToggleButton tglLoop;
    @FXML
    private ToggleButton tglPreview;
    @FXML
    private ToggleButton tglVideo;
    @FXML
    private Slider jslSpinV;
    @FXML
    private Label jlbDuration;
    @FXML
    private ToggleButton tglActiveStream;
    @FXML
    private ToggleButton tglPause;
    @FXML
    private Label lblSeek;
    @FXML
    private ToolBar streamToolBar;
    @FXML
    private Label lblADelay;
    @FXML
    private Label lblVolume;
    @FXML
    private Separator leftSeparator;
    @FXML
    private Separator rightSeparator;
    @FXML
    private ToolBar bottomBar;

    private void spinVDelayStateChanged() {
        stream.setVDelay(spinVDelay.getValue());
        jslSpinVDelay.setValue(spinVDelay.getValue());
    }

    private void jslSpinVDelayStateChanged() {
        valSpinVDelay.setValue((int) jslSpinVDelay.getValue());
    }

    private void spinADelayStateChanged() {
        stream.setADelay(spinADelay.getValue());
        jslSpinADelay.setValue(spinADelay.getValue());
    }

    private void jslSpinADelayStateChanged() {
        valSpinADelay.setValue((int) jslSpinADelay.getValue());
    }

    private void spinSeekStateChanged() {
        stream.setSeek(spinSeek.getValue());
        jslSpinSeek.setValue(spinSeek.getValue());
    }

    private void jslSpinSeekStateChanged() {
        valSpinSeek.setValue((int) jslSpinSeek.getValue());
    }

    public interface Listener {

        public void startItsTrack(String name);

        public void stopItsTrack();
    }

    static StreamPanelFXController.Listener listenerTPX = null;

    public static void setListenerTPX(StreamPanelFXController.Listener l) {
        listenerTPX = l;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.stream = stream_;
        jcbLibAV.setVisible(BackEnd.avconvDetected());
        jcbFFmpeg.setVisible(BackEnd.ffmpegDetected());
        if (distro.equals("windows")) {
            jcbGStreamer.setVisible(false);
        }
        if (stream instanceof SourceAudioSource) {
            final ArrayList<CheckMenuItem> aSMenuItem = new ArrayList<>();
            try {
                sourcesAudio = PaCTL.getSources();
            } catch (IOException ex) {
                Logger.getLogger(StreamPanelFXController.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (AudioSource audioSource : sourcesAudio) {
                final CheckMenuItem jCBMenuItem = new CheckMenuItem();
                jCBMenuItem.setText(audioSource.description);
                jCBMenuItem.setId(audioSource.device); // NOI18N
                jCBMenuItem.setOnAction((ActionEvent e) -> {
                    stream.setAudioSource(jCBMenuItem.getId());
                    aSMenuItem.stream().filter((jCb) -> (!jCb.getId().equals(stream.getAudioSource()))).forEach((jCb) -> {
                        jCb.setSelected(false);
                    });
                });
                jmAudioSource.getItems().add(jCBMenuItem);
                aSMenuItem.add(jCBMenuItem);
                if (stream.getLoaded()) {
                    aSMenuItem.stream().filter((jCb) -> (jCb.getId().equals(stream.getAudioSource()))).forEach((jCb) -> {
                        jCb.setSelected(true);
                    });
                } else {
                    CheckMenuItem initJCb = aSMenuItem.get(0);
                    initJCb.setSelected(true);
                    stream.setAudioSource(initJCb.getId());
                }
            }
        }

        if (stream.getLoaded()) {
            switch (stream.getComm()) {
                case "AV":
                    jcbLibAV.setSelected(true);
                    stream.setComm("AV");
                    jcbGStreamer.setSelected(false);
                    break;
                case "GS":
                    jcbGStreamer.setSelected(true);
                    stream.setComm("GS");
                    jcbLibAV.setSelected(false);
                    break;
                case "FF":
                    jcbFFmpeg.setSelected(true);
                    stream.setComm("FF");
                    stream.setBackFF(true);
                    jcbLibAV.setSelected(false);
                    jcbGStreamer.setSelected(false);
                    break;
                default:
                    if (stream instanceof SourceAudioSource) { // ||stream instanceof SourceImageU
                        jcbGStreamer.setSelected(true);
                        stream.setComm("GS");
                        jcbLibAV.setSelected(false);
                        jcbFFmpeg.setSelected(false);
                    } else {
                        jcbLibAV.setSelected(true);
                        stream.setComm("AV");
                        jcbGStreamer.setSelected(false);
                        jcbFFmpeg.setSelected(false);
                    }
                    break;
            }
        } else {
            if (stream instanceof SourceAudioSource) {
                jcbGStreamer.setSelected(true);
                stream.setComm("GS");
                jcbLibAV.setSelected(false);
                jcbFFmpeg.setSelected(false);
            } else if (distro.toLowerCase().equals("ubuntu")) {
                jcbLibAV.setSelected(true);
                stream.setComm("AV");
                jcbGStreamer.setSelected(false);
                jcbFFmpeg.setSelected(false);
            } else if (distro.toLowerCase().equals("windows")) {
                stream.setComm("FF");
                stream.setBackFF(true);
                jcbFFmpeg.setSelected(true);
            } else {
                jcbLibAV.setSelected(false);
                stream.setComm("FF");
                stream.setBackFF(true);
                jcbGStreamer.setSelected(false);
                jcbFFmpeg.setSelected(true);
            }
            tglLoop.setVisible(false);
        }
        if (stream instanceof SourceImageGif || stream instanceof SourceImage) {
            jmBackend.setVisible(false);
            tglLoop.setVisible(false);
        }

        stream.setPanelType("Panel");

        if (stream instanceof SourceAudioSource) {
            jmAudioSource.setVisible(true);
            tglLoop.setVisible(false);
        } else {
            jmAudioSource.setVisible(false);
        }

        if (stream instanceof SourceMovie || stream instanceof SourceMusic) {
            tglLoop.setVisible(true);
            tglLoop.setSelected(stream.getLoop());
        }

        oldW = stream.getWidth();
        oldH = stream.getHeight();
        volume = stream.getVolume();
        vol = stream.getVolume();

        spinVolume.editableProperty().set(true);
        spinVolume.setLayoutX(124.0);
        spinVolume.setLayoutY(40.0);
        spinVolume.setMaxHeight(-Double.MAX_VALUE);
        spinVolume.setMinHeight(20.0);
        spinVolume.setPrefHeight(20.0);
        spinVolume.setPrefWidth(93.0);
        StreamPanelFX.getChildren().add(spinVolume);
        spinVolume.setVisible(true);

        spinADelay.editableProperty().set(true);
        spinADelay.setLayoutX(124.0);
        spinADelay.setLayoutY(259.0);
        spinADelay.setMaxHeight(-Double.MAX_VALUE);
        spinADelay.setMinHeight(20.0);
        spinADelay.setPrefHeight(20.0);
        spinADelay.setPrefWidth(93.0);
        StreamPanelFX.getChildren().add(spinADelay);
        spinADelay.setVisible(true);

        spinSeek.editableProperty().set(true);
        spinSeek.setLayoutX(124.0);
        spinSeek.setLayoutY(280.0);
        spinSeek.setMaxHeight(-Double.MAX_VALUE);
        spinSeek.setMinHeight(20.0);
        spinSeek.setPrefHeight(20.0);
        spinSeek.setPrefWidth(93.0);
        StreamPanelFX.getChildren().add(spinSeek);
        spinSeek.setVisible(true);

        spinOpacity.editableProperty().set(true);
        spinOpacity.setLayoutX(124.0);
        spinOpacity.setLayoutY(145.0);
        spinOpacity.setMaxHeight(-Double.MAX_VALUE);
        spinOpacity.setMinHeight(20.0);
        spinOpacity.setPrefHeight(20.0);
        spinOpacity.setPrefWidth(93.0);
        StreamPanelFX.getChildren().add(spinOpacity);
        spinOpacity.setVisible(true);

        spinX.editableProperty().set(true);
        spinX.setLayoutX(124.0);
        spinX.setLayoutY(61.0);
        spinX.setMaxHeight(-Double.MAX_VALUE);
        spinX.setMinHeight(20.0);
        spinX.setPrefHeight(20.0);
        spinX.setPrefWidth(93.0);
        StreamPanelFX.getChildren().add(spinX);
        spinX.setVisible(true);

        spinY.editableProperty().set(true);
        spinY.setLayoutX(124.0);
        spinY.setLayoutY(82.0);
        spinY.setMaxHeight(-Double.MAX_VALUE);
        spinY.setMinHeight(20.0);
        spinY.setPrefHeight(20.0);
        spinY.setPrefWidth(93.0);
        StreamPanelFX.getChildren().add(spinY);
        spinY.setVisible(true);

        spinW.editableProperty().set(true);
        spinW.setLayoutX(124.0);
        spinW.setLayoutY(103.0);
        spinW.setMaxHeight(-Double.MAX_VALUE);
        spinW.setMinHeight(20.0);
        spinW.setPrefHeight(20.0);
        spinW.setPrefWidth(93.0);
        StreamPanelFX.getChildren().add(spinW);
        spinW.setVisible(true);

        spinH.editableProperty().set(true);
        spinH.setLayoutX(124.0);
        spinH.setLayoutY(124.0);
        spinH.setMaxHeight(-Double.MAX_VALUE);
        spinH.setMinHeight(20.0);
        spinH.setPrefHeight(20.0);
        spinH.setPrefWidth(93.0);
        StreamPanelFX.getChildren().add(spinH);
        spinH.setVisible(true);

        spinZOrder.editableProperty().set(true);
        spinZOrder.setLayoutX(124.0);
        spinZOrder.setLayoutY(166.0);
        spinZOrder.setMaxHeight(-Double.MAX_VALUE);
        spinZOrder.setMinHeight(20.0);
        spinZOrder.setPrefHeight(20.0);
        spinZOrder.setPrefWidth(93.0);
        StreamPanelFX.getChildren().add(spinZOrder);
        spinZOrder.setVisible(true);

        spinVDelay.editableProperty().set(true);
        spinVDelay.setLayoutX(124.0);
        spinVDelay.setLayoutY(238.0);
        spinVDelay.setMaxHeight(-Double.MAX_VALUE);
        spinVDelay.setMinHeight(20.0);
        spinVDelay.setPrefHeight(20.0);
        spinVDelay.setPrefWidth(93.0);
        StreamPanelFX.getChildren().add(spinVDelay);
        spinVDelay.setVisible(true);

        valSpinX = new SpinnerValueFactory.IntegerSpinnerValueFactory(-stream.getCaptureWidth(), stream.getCaptureWidth());
        jslSpinX.setMax(stream.getCaptureWidth());
        jslSpinX.setMin(-stream.getCaptureWidth());
        valSpinX.valueProperty().addListener((obs, oldValue, newValue)
                -> spinXStateChanged());
        jslSpinX.valueProperty().addListener((obs, oldValue, newValue)
                -> jslSpinXStateChanged());
        jslSpinX.setMajorTickUnit(stream.getCaptureWidth() / 2);
        jslSpinX.showTickMarksProperty().setValue(Boolean.TRUE);
        spinX.setValueFactory(valSpinX);
        valSpinX.setValue(stream.getX());

        valSpinY = new SpinnerValueFactory.IntegerSpinnerValueFactory(-stream.getCaptureHeight(), stream.getCaptureHeight());
        jslSpinY.setMax(stream.getCaptureHeight());
        jslSpinY.setMin(-stream.getCaptureHeight());
        valSpinY.valueProperty().addListener((obs, oldValue, newValue)
                -> spinYStateChanged());
        jslSpinY.valueProperty().addListener((obs, oldValue, newValue)
                -> jslSpinYStateChanged());
        jslSpinY.setMajorTickUnit(stream.getCaptureHeight() / 2);
        jslSpinY.showTickMarksProperty().setValue(Boolean.TRUE);
        spinY.setValueFactory(valSpinY);
        valSpinY.setValue(stream.getY());

        valSpinW = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, stream.getCaptureWidth());
        jslSpinW.setMax(stream.getCaptureWidth());
        valSpinW.valueProperty().addListener((obs, oldValue, newValue)
                -> spinWStateChanged());
        jslSpinW.valueProperty().addListener((obs, oldValue, newValue)
                -> jslSpinWStateChanged());
        jslSpinW.setMajorTickUnit(stream.getCaptureWidth() / 2);
        jslSpinW.showTickMarksProperty().setValue(Boolean.TRUE);
        spinW.setValueFactory(valSpinW);
        valSpinW.setValue(stream.getWidth());

        valSpinH = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, stream.getCaptureHeight());
        jslSpinH.setMax(stream.getCaptureHeight());
        valSpinH.valueProperty().addListener((obs, oldValue, newValue)
                -> spinHStateChanged());
        jslSpinH.valueProperty().addListener((obs, oldValue, newValue)
                -> jslSpinHStateChanged());
        jslSpinH.setMajorTickUnit(stream.getCaptureHeight() / 2);
        jslSpinH.showTickMarksProperty().setValue(Boolean.TRUE);
        spinH.setValueFactory(valSpinH);
        valSpinH.setValue(stream.getHeight());

        valSpinOpacity.valueProperty().addListener((obs, oldValue, newValue)
                -> spinOpacityStateChanged());
        jslSpinOpacity.valueProperty().addListener((obs, oldValue, newValue)
                -> jslSpinOpacityStateChanged());
        spinOpacity.setValueFactory(valSpinOpacity);
        valSpinOpacity.setValue(stream.getOpacity());

        valSpinVolume.valueProperty().addListener((obs, oldValue, newValue)
                -> spinVolumeStateChanged());
        jslSpinV.valueProperty().addListener((obs, oldValue, newValue)
                -> jslSpinVStateChanged());
        spinVolume.setValueFactory(valSpinVolume);
        valSpinVolume.setValue((int) (stream.getVolume() * 100));
        jslSpinV.setDisable(!stream.hasAudio());

        valSpinZOrder.valueProperty().addListener((obs, oldValue, newValue)
                -> spinZOrderStateChanged());
        jslSpinZOrder.valueProperty().addListener((obs, oldValue, newValue)
                -> jslSpinZOrderStateChanged());
        spinZOrder.setValueFactory(valSpinZOrder);
        valSpinZOrder.setValue(stream.getZOrder());

        valSpinVDelay.valueProperty().addListener((obs, oldValue, newValue)
                -> spinVDelayStateChanged());
        jslSpinVDelay.valueProperty().addListener((obs, oldValue, newValue)
                -> jslSpinVDelayStateChanged());
        spinVDelay.setValueFactory(valSpinVDelay);
        valSpinVDelay.setValue(stream.getVDelay());

        valSpinADelay.valueProperty().addListener((obs, oldValue, newValue)
                -> spinADelayStateChanged());
        jslSpinADelay.valueProperty().addListener((obs, oldValue, newValue)
                -> jslSpinADelayStateChanged());
        spinADelay.setValueFactory(valSpinADelay);
        valSpinADelay.setValue(stream.getADelay());

        valSpinSeek.valueProperty().addListener((obs, oldValue, newValue)
                -> spinSeekStateChanged());
        jslSpinSeek.valueProperty().addListener((obs, oldValue, newValue)
                -> jslSpinSeekStateChanged());
        spinSeek.setValueFactory(valSpinSeek);
        valSpinSeek.setValue(stream.getSeek());

        spinVDelay.setDisable(!stream.hasVideo());
        jslSpinVDelay.setDisable(!stream.hasVideo());

        spinADelay.setVisible(stream.hasAudio());
        jslSpinADelay.setVisible(stream.hasAudio());
        lblADelay.setVisible(stream.hasAudio());

        spinVolume.setVisible(stream.hasAudio());
        jslSpinV.setVisible(stream.hasAudio());
        lblVolume.setVisible(stream.hasAudio());

        if (!stream.needSeekCTRL()) {
            StreamPanelFX.getChildren().remove(spinSeek);
            StreamPanelFX.getChildren().remove(jslSpinSeek);
            StreamPanelFX.getChildren().remove(lblSeek);
            leftSeparator.setPrefHeight(294);
            rightSeparator.setPrefHeight(294);
            bottomBar.setLayoutY(286);
        }

        jlbDuration.setText("Play Time " + stream.getStreamTime());

        stream.setListener(this);
        if (!stream.hasVideo()) {
            spinX.setDisable(true);
            jslSpinX.setDisable(true);
            spinY.setDisable(true);
            jslSpinY.setDisable(true);
            spinW.setDisable(true);
            jslSpinW.setDisable(true);
            spinH.setDisable(true);
            jslSpinH.setDisable(true);
            spinOpacity.setDisable(true);
            jslSpinOpacity.setDisable(true);
        }
        if (stream instanceof SourceAudioSource) {
            jlbDuration.setVisible(false);
            tglAudio.setVisible(true);
            tglPause.setVisible(false);
        } else if (stream instanceof SourceMusic) {
            tglAudio.setVisible(false);
            tglPause.setVisible(true);
        } else if (stream instanceof SourceMovie) {
            if (stream.isOnlyVideo()) {
                tglAudio.setVisible(false);
                tglVideo.setVisible(false);
            } else {
                tglAudio.setVisible(true);
                tglVideo.setDisable(true);
            }
        } else if (stream instanceof SourceImage || stream instanceof SourceImageGif) { //|| stream instanceof SourceImageU 
            jlbDuration.setText(" ");
            jlbDuration.setVisible(!jslSpinV.isVisible());
            tglAudio.setVisible(false);
            streamToolBar.getItems().remove(tglPause);
            tglVideo.setVisible(false);
        } else {
            jlbDuration.setText(" ");
            jlbDuration.setVisible(!jslSpinV.isVisible());
            tglAudio.setVisible(false);
            tglVideo.setVisible(false);
        }
        tglVideo.setSelected(stream.isOnlyAudio());
        tglAudio.setSelected(!stream.hasAudio());
        if (tglAudio.isSelected()) {
            tglAudio.setDisable(false);
            tglVideo.setDisable(true);
        } else if (tglVideo.isSelected()) {
            tglVideo.setDisable(false);
            tglAudio.setDisable(true);
        } else {
            tglAudio.setDisable(false);
            tglVideo.setDisable(false);
        }

    }

    public void setStream(Stream s) {
        stream = s;
    }

    private void spinXStateChanged() {
        stream.setX(spinX.getValue());
        jslSpinX.setValue(spinX.getValue());
    }

    private void jslSpinXStateChanged() {
        valSpinX.setValue((int) jslSpinX.getValue());
    }

    private void spinYStateChanged() {
        stream.setY(spinY.getValue());
        jslSpinY.setValue(spinY.getValue());
    }

    private void jslSpinYStateChanged() {
        valSpinY.setValue((int) jslSpinY.getValue());
    }

    private void spinWStateChanged() {
        int w = spinW.getValue();
        jslSpinW.setValue(w);
        int h = oldH;
        if (lockRatio) {
            h = (oldH * w) / oldW;
            if (h >= 1) {
                valSpinH.setValue(h);
            } else {
                h = 1;
            }
        }
        stream.setWidth(w);
    }

    private void jslSpinWStateChanged() {
        valSpinW.setValue((int) jslSpinW.getValue());
    }

    private void spinHStateChanged() {
        int h = spinH.getValue();
        jslSpinH.setValue(h);
        if (!lockRatio) {
            oldH = stream.getHeight();
        }
        stream.setHeight(h);
    }

    private void jslSpinHStateChanged() {
        valSpinH.setValue((int) jslSpinH.getValue());
    }

    private void spinOpacityStateChanged() {
        stream.setOpacity(spinOpacity.getValue());
        jslSpinOpacity.setValue(spinOpacity.getValue());
    }

    private void jslSpinOpacityStateChanged() {
        valSpinOpacity.setValue((int) jslSpinOpacity.getValue());
    }

    private void spinVolumeStateChanged() {
        String jSVol = spinVolume.getValue().toString().replace(".0", "");
        int jVol = Integer.parseInt(jSVol);
        jslSpinV.setValue(jVol);
        Object value = spinVolume.getValue();
        float v = 0;
        if (value instanceof Float) {
            v = (Float) value;
        } else if (value instanceof Integer) {
            v = ((Number) value).floatValue();
        }
        if (stream.getisPaused()) {
            if (v / 100f != 0) {
                vol = v / 100f;
            }
        } else {
            stream.setVolume(v / 100f);
            volume = v / 100f;
        }
    }

    private void jslSpinVStateChanged() {
        valSpinVolume.setValue((int) jslSpinV.getValue());
    }

    private void spinZOrderStateChanged() {
        stream.setZOrder(spinZOrder.getValue());
        jslSpinZOrder.setValue(spinZOrder.getValue());
    }

    private void jslSpinZOrderStateChanged() {
        valSpinZOrder.setValue((int) jslSpinZOrder.getValue());
    }

    @FXML
    private void jcbRightToLeftAction(ActionEvent event) {
        final int oldBkX = stream.getX();
        runMe = true;
        if (jcbRightToLeft.isSelected()) {

            jcbLeftToRight.setDisable(true);
            jcbBottomToTop.setDisable(true);
            jcbTopToBottom.setDisable(true);
            jcbHBouncing.setDisable(true);

            Thread scrollRtL = new Thread(new Runnable() {
                int deltaX = 0;

                @Override
                public void run() {
                    int startX = stream.getX();
                    while (runMe && stream.isPlaying()) {
                        final int mixerW = MasterMixer.getInstance().getWidth();
                        final int streamW = stream.getWidth();
//                        System.out.println("deltax="+(startX+streamW));
                        if ((startX + streamW) <= 0) {
                            stream.setX(mixerW);
                        }
                        final int rate = stream.getRate();
                        for (int i = 0; i < rate; i++) {
                            if (runMe) {
                                startX = stream.getX();
                                stream.setX(startX - speed);
                                Tools.sleep(1000 / rate);
                            } else {
                                stream.setX(oldBkX);
                                break;
                            }
                        }
                    }
                    stream.setX(oldBkX);
                }
            });
            scrollRtL.setPriority(Thread.MIN_PRIORITY);
            scrollRtL.start();
        } else {
            runMe = false;
            stream.setX(oldBkX);
            jcbLeftToRight.setDisable(false);
            jcbBottomToTop.setDisable(false);
            jcbTopToBottom.setDisable(false);
            jcbHBouncing.setDisable(false);
        }
    }

    @FXML
    private void jcbLeftToRightAction(ActionEvent event) {
        final int oldBkX = stream.getX();
        runMe = true;
        if (jcbLeftToRight.isSelected()) {

            jcbRightToLeft.setDisable(true);
            jcbBottomToTop.setDisable(true);
            jcbTopToBottom.setDisable(true);
            jcbHBouncing.setDisable(true);

            Thread scrollRtL = new Thread(() -> {
                int startX = stream.getX();
                while (runMe && stream.isPlaying()) {
                    final int mixerW = MasterMixer.getInstance().getWidth();
                    final int streamW = stream.getWidth();
//                        System.out.println("deltax="+(startX+streamW));
                    if ((startX) >= mixerW) {
                        stream.setX(-streamW);
                    }
                    final int rate = stream.getRate();
                    for (int i = 0; i < rate; i++) {
                        if (runMe) {
                            startX = stream.getX();
                            stream.setX(startX + speed);
                            Tools.sleep(1000 / rate);
                        } else {
                            stream.setX(oldBkX);
                            break;
                        }
                    }
                }
                stream.setX(oldBkX);
            });
            scrollRtL.setPriority(Thread.MIN_PRIORITY);
            scrollRtL.start();
        } else {
            runMe = false;
            stream.setX(oldBkX);
            jcbRightToLeft.setDisable(false);
            jcbBottomToTop.setDisable(false);
            jcbTopToBottom.setDisable(false);
            jcbHBouncing.setDisable(false);
        }
    }

    @FXML
    private void jcbBottomToTopAction(ActionEvent event) {
        final int oldBkY = stream.getY();
        runMe = true;
        if (jcbBottomToTop.isSelected()) {

            jcbLeftToRight.setDisable(true);
            jcbRightToLeft.setDisable(true);
            jcbTopToBottom.setDisable(true);
            jcbHBouncing.setDisable(true);

            Thread scrollRtL = new Thread(new Runnable() {
                int deltaY = 0;

                @Override
                public void run() {
                    int startY = stream.getY();
                    while (runMe && stream.isPlaying()) {
                        final int mixerH = MasterMixer.getInstance().getHeight();
                        final int streamH = stream.getHeight();
//                        System.out.println("deltax="+(startX+streamW));
                        if ((startY + streamH) <= 0) {
                            stream.setY(mixerH);
                        }
                        final int rate = stream.getRate();
                        for (int i = 0; i < rate; i++) {
                            if (runMe) {
                                startY = stream.getY();
                                stream.setY(startY - speed);
                                Tools.sleep(1000 / rate);
                            } else {
                                stream.setY(oldBkY);
                                break;
                            }
                        }
                    }
                    stream.setY(oldBkY);
                }
            });
            scrollRtL.setPriority(Thread.MIN_PRIORITY);
            scrollRtL.start();
        } else {
            runMe = false;
            stream.setY(oldBkY);
            jcbLeftToRight.setDisable(false);
            jcbRightToLeft.setDisable(false);
            jcbTopToBottom.setDisable(false);
            jcbHBouncing.setDisable(false);
        }
    }

    @FXML
    private void jcbTopToBottomAction(ActionEvent event) {
        final int oldBkY = stream.getY();
        runMe = true;
        if (jcbTopToBottom.isSelected()) {

            jcbLeftToRight.setDisable(true);
            jcbBottomToTop.setDisable(true);
            jcbRightToLeft.setDisable(true);
            jcbHBouncing.setDisable(true);

            Thread scrollRtL = new Thread(() -> {
                int startY = stream.getY();
                while (runMe && stream.isPlaying()) {
                    final int mixerH = MasterMixer.getInstance().getHeight();
                    final int streamH = stream.getHeight();
//                        System.out.println("deltax="+(startX+streamW));
                    if ((startY) >= mixerH) {
                        stream.setY(-streamH);
                    }
                    final int rate = stream.getRate();
                    for (int i = 0; i < rate; i++) {
                        if (runMe) {
                            startY = stream.getY();
                            stream.setY(startY + speed);
                            Tools.sleep(1000 / rate);
                        } else {
                            stream.setY(oldBkY);
                            break;
                        }
                    }
                }
                stream.setY(oldBkY);
            });
            scrollRtL.setPriority(Thread.MIN_PRIORITY);
            scrollRtL.start();
        } else {
            runMe = false;
            stream.setY(oldBkY);
            jcbLeftToRight.setDisable(false);
            jcbBottomToTop.setDisable(false);
            jcbRightToLeft.setDisable(false);
            jcbHBouncing.setDisable(false);
        }
    }

    @FXML
    private void jcbHBouncingAction(ActionEvent event) {
        final int oldBkX = stream.getX();
        runMe = true;

        if (jcbHBouncing.isSelected()) {

            jcbLeftToRight.setDisable(true);
            jcbRightToLeft.setDisable(true);
            jcbTopToBottom.setDisable(true);
            jcbBottomToTop.setDisable(true);

            Thread scrollRtL = new Thread(new Runnable() {
                int deltaX = 0;

                @Override
                public void run() {
                    int startX = stream.getX();
                    boolean oneWay = true;
                    boolean toLeft = true;
                    final int rate = stream.getRate();
                    final int mixerW = MasterMixer.getInstance().getWidth();
                    while (runMe && stream.isPlaying()) {
                        int streamW = stream.getWidth();
                        for (int i = 0; i < rate; i++) {
                            if (runMe) {
                                startX = stream.getX();
                                if (startX > 0 && oneWay) {
                                    toLeft = true;
                                    stream.setX(startX - speed);
                                }
                                if (startX <= 0 || !oneWay) {
                                    toLeft = false;
                                    oneWay = false;
                                    stream.setX(startX + speed);
                                    if ((startX + streamW) >= mixerW) {
                                        oneWay = true;
                                    }
                                }
                                Tools.sleep(1000 / rate);
                            } else {
                                stream.setX(oldBkX);
                                break;
                            }
                        }
                        if ((startX + streamW) < mixerW && !toLeft) {
                            oneWay = false;
                        } else {
                            oneWay = true;
                        }
                    }
                    stream.setX(oldBkX);
                }
            });
            scrollRtL.setPriority(Thread.MIN_PRIORITY);
            scrollRtL.start();
        } else {
            runMe = false;
            stream.setX(oldBkX);
            jcbLeftToRight.setDisable(false);
            jcbBottomToTop.setDisable(false);
            jcbTopToBottom.setDisable(false);
            jcbRightToLeft.setDisable(false);
        }
    }

    @FXML
    private void radioSpeed1Action(ActionEvent event) {
        speed = 1;
        radioSpeed2.setSelected(false);
        radioSpeed3.setSelected(false);
        radioSpeed4.setSelected(false);
        radioSpeed5.setSelected(false);
    }

    @FXML
    private void radioSpeed2Action(ActionEvent event) {
        speed = 3;
        radioSpeed1.setSelected(false);
        radioSpeed3.setSelected(false);
        radioSpeed4.setSelected(false);
        radioSpeed5.setSelected(false);
    }

    @FXML
    private void radioSpeed3Action(ActionEvent event) {
        speed = 5;
        radioSpeed1.setSelected(false);
        radioSpeed2.setSelected(false);
        radioSpeed4.setSelected(false);
        radioSpeed5.setSelected(false);
    }

    @FXML
    private void radioSpeed4Action(ActionEvent event) {
        speed = 7;
        radioSpeed1.setSelected(false);
        radioSpeed2.setSelected(false);
        radioSpeed3.setSelected(false);
        radioSpeed5.setSelected(false);
    }

    @FXML
    private void radioSpeed5Action(ActionEvent event) {
        speed = 9;
        radioSpeed1.setSelected(false);
        radioSpeed2.setSelected(false);
        radioSpeed3.setSelected(false);
        radioSpeed4.setSelected(false);
    }

    @FXML
    private void jcbGStreamerAction(ActionEvent event) {
        if (jcbGStreamer.isSelected()) {
            stream.setComm("GS");
            stream.setBackFF(false);
            jcbLibAV.setSelected(false);
            jcbFFmpeg.setSelected(false);
            new Thread(() -> {
                selectedSource(stream);
            }).start();

        } else {
            jcbLibAV.setSelected(true);
            jcbGStreamer.setSelected(false);
            jcbFFmpeg.setSelected(false);
            stream.setBackFF(false);
            stream.setComm("AV");
        }
    }

    @FXML
    private void jcbLibAVAction(ActionEvent event) {
        if (jcbLibAV.isSelected()) {
            stream.setComm("AV");
            stream.setBackFF(false);
            jcbGStreamer.setSelected(false);
            jcbFFmpeg.setSelected(false);

            new Thread(() -> {
                selectedSource(stream);
            }).start();

        } else {
            jcbGStreamer.setSelected(true);
            jcbLibAV.setSelected(false);
            jcbFFmpeg.setSelected(false);
            stream.setComm("GS");
            stream.setBackFF(false);
        }
    }

    @FXML
    private void jcbFFmpegAction(ActionEvent event) {
        if (jcbFFmpeg.isSelected()) {
            stream.setComm("FF");
            stream.setBackFF(true);
            jcbLibAV.setSelected(false);
            jcbGStreamer.setSelected(false);
            new Thread(() -> {
                selectedSource(stream);
            }).start();

        } else {
            stream.setComm("AV");
            stream.setBackFF(false);
            jcbLibAV.setSelected(true);
            jcbGStreamer.setSelected(false);
        }
    }

    @FXML
    private void tglARAction(ActionEvent event) {
        if (tglAR.isSelected()) {
            spinH.setDisable(true);
            jslSpinH.setDisable(true);
            lockRatio = true;
            oldW = stream.getWidth();
            oldH = stream.getHeight();
        } else {
            spinH.setDisable(false);
            jslSpinH.setDisable(false);
            lockRatio = false;
            oldW = stream.getWidth();
            oldH = stream.getHeight();
        }

    }

    @FXML
    private void tglAudioAction(ActionEvent event) {
        if (tglAudio.isSelected()) {
            stream.setHasAudio(false);
            stream.setOnlyVideo(true);
            tglVideo.setDisable(true);
        } else {
            stream.setHasAudio(true);
            stream.setOnlyVideo(false);
            tglVideo.setDisable(false);
        }
    }

    @FXML
    private void tglPauseAction(ActionEvent event) {
        if (tglPause.isSelected()) {
            stream.setVolume(0);
            stream.setisPaused(true);
            stream.pause();
        } else {
            stream.setVolume(vol);
            valSpinVolume.setValue((int) (vol * 100));
            stream.setisPaused(false);
            stream.play();
        }
    }

    @FXML
    private void tglVideoAction(ActionEvent event) {
        if (tglVideo.isSelected()) {
            tglAudio.setDisable(true);
            stream.setOnlyAudio(true);
        } else {
            tglAudio.setDisable(false);
            stream.setOnlyAudio(false);
        }
    }

    @FXML
    private void tglLoopAction(ActionEvent event) {
        if (tglLoop.isSelected()) {
            stream.setLoop(true);
        } else {
            stream.setLoop(false);
        }
    }

    @FXML
    private void tglPreviewAction(ActionEvent event) {
        if (tglPreview.isSelected()) {
            stream.setPreView(true);
        } else {
            stream.setPreView(false);
        }
    }

    @FXML
    private void tglActiveStreamAction(ActionEvent event) {
        if (tglActiveStream.isSelected()) {
            if (stream.getisATrack() && !stream.getPreView()) {
                String name = stream.getName();
                listenerTPX.startItsTrack(name);
                TitledPane sDesktopFX = (TitledPane) StreamPanelFX.getParent().getParent();
//                System.out.println("Parent = " + sDesktopFX);
                sDesktopFX.lookup(".title").setStyle(stringPlay);
            } else {
                if (tglVideo.isSelected()) {
                    stream.setOnlyAudio(true);
                } else {
                    stream.setOnlyAudio(false);
                }
//                System.out.println("Play Volume: " + volume);
                tglVideo.setDisable(true);
                spinVDelay.setDisable(true);
                jslSpinVDelay.setDisable(true);
                spinADelay.setDisable(true);
                jslSpinADelay.setDisable(true);
                spinSeek.setDisable(true);
                jslSpinSeek.setDisable(true);
                tglAudio.setDisable(true);
                tglPreview.setDisable(true);
                tglPause.setDisable(false);
                TitledPane sDesktopFX = (TitledPane) StreamPanelFX.getParent().getParent();
                sDesktopFX.lookup(".title").setStyle(stringPlay);
                stream.read();
            }
        } else {
            if (stream.getisATrack() && lblPlayingTrack.contains(stream.getName())) {
                int index = list_.getSelectionModel().getSelectedIndex();
                forceListRefreshOn(list_);
                list_.getSelectionModel().select(index);
                lblPlayingTrack = "";
            }
            TitledPane sDesktopFX = (TitledPane) StreamPanelFX.getParent().getParent();
            sDesktopFX.lookup(".title").setStyle(stringStop);
            spinVDelay.setDisable(!stream.hasVideo());
            jslSpinVDelay.setDisable(!stream.hasVideo());
            spinADelay.setDisable(!stream.hasAudio());
            jslSpinADelay.setDisable(!stream.hasAudio());
            spinSeek.setDisable(!stream.needSeekCTRL());
            jslSpinSeek.setDisable(!stream.needSeekCTRL());
            tglPreview.setDisable(false);
            if (tglAudio.isSelected()) {
                tglAudio.setDisable(false);
            } else if (tglVideo.isSelected()) {
                tglVideo.setDisable(false);
            } else {
                tglAudio.setDisable(false);
                tglVideo.setDisable(false);
            }
            tglPause.setSelected(false);
            tglPause.setDisable(true);
            stream.setisPaused(false);
            if (stream.getLoop()) {
                stream.setLoop(false);
                stream.stop();
                stream.setLoop(true);
                stream.setVolume(volume);
            } else {
                stream.stop();
                if (stream.getVolume() == 0) {
                    stream.setVolume(0);
                } else {
                    stream.setVolume(volume);
                }
            }
            if (stream.getisATrack() && !stream.getPreView()) {
                listenerTPX.stopItsTrack();
            }
        }
    }

    @Override
    public void sourceUpdated(Stream stream) {
        int mixerW = MasterMixer.getInstance().getWidth();
        int mixerH = MasterMixer.getInstance().getHeight();
        TitledPane sDesktopFX = (TitledPane) StreamPanelFX.getParent().getParent();

        if (jslSpinX.getValue() > mixerW) {
            valSpinX.setValue(stream.getX());
        }
        jslSpinX.setMax(mixerW);

        if (jslSpinX.getValue() < -mixerW) {
            valSpinX.setValue(stream.getX());
        }
        jslSpinX.setMin(-mixerW);

        if (jslSpinY.getValue() > mixerH) {
            valSpinY.setValue(stream.getY());
        }
        jslSpinY.setMax(mixerH);

        if (jslSpinY.getValue() < -mixerH) {
            valSpinY.setValue(stream.getY());
        }
        jslSpinY.setMin(-mixerH);

        if (jslSpinW.getValue() > mixerW) {
            valSpinW.setValue(stream.getWidth());
        }
        jslSpinW.setMax(mixerW);

        if (jslSpinH.getValue() > mixerH) {
            valSpinH.setValue(stream.getHeight());
        }
        jslSpinH.setMax(mixerH);

        valSpinX.setValue(stream.getX());
        valSpinY.setValue(stream.getY());
        valSpinH.setValue(stream.getHeight());
        valSpinW.setValue(stream.getWidth());
        valSpinOpacity.setValue(stream.getOpacity());
        valSpinVolume.setValue((int) (stream.getVolume() * 100));
        valSpinZOrder.setValue(stream.getZOrder());
        tglActiveStream.setSelected(stream.isPlaying());
        if (stream.isPlaying()) {
            tglPause.setSelected(stream.getisPaused());
        } else {
            tglPause.setSelected(false);
            stream.setisPaused(false);
        }
        if (stream.isPlaying()) {
            sDesktopFX.lookup(".title").setStyle(stringPlay);
            spinVDelay.setDisable(true);
            jslSpinVDelay.setDisable(true);
            spinADelay.setDisable(true);
            jslSpinADelay.setDisable(true);
            spinSeek.setDisable(true);
            jslSpinSeek.setDisable(true);
            tglAudio.setDisable(true);
            tglPreview.setDisable(true);
            tglVideo.setDisable(true);
            spinVolume.setDisable(!stream.hasAudio());
            jslSpinV.setDisable(!stream.hasAudio());
            tglPause.setDisable(false);
        } else {
            sDesktopFX.lookup(".title").setStyle(stringStop);
            spinVDelay.setDisable(!stream.hasVideo());
            jslSpinVDelay.setDisable(!stream.hasVideo());
            spinADelay.setDisable(!stream.hasAudio());
            jslSpinADelay.setDisable(!stream.hasAudio());
            spinSeek.setDisable(!stream.needSeekCTRL());
            jslSpinSeek.setDisable(!stream.needSeekCTRL());
            tglPreview.setDisable(false);
            if (tglAudio.isSelected()) {
                tglAudio.setDisable(false);
            } else if (tglVideo.isSelected()) {
                tglVideo.setDisable(false);
            } else {
                tglAudio.setDisable(false);
                tglVideo.setDisable(false);
            }
            spinVolume.setDisable(!stream.hasAudio());
            jslSpinV.setDisable(!stream.hasAudio());
            tglPause.setDisable(true);
            tglPause.setDisable(true);
        }
        layer.setText("Layer=" + stream.getZOrder());
        sDesktopFX.setTooltip(layer);

    }

    @Override
    public void updatePreview(BufferedImage image) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void selectedSource(Stream source) {
        TitledPane sDesktopFX = (TitledPane) StreamPanelFX.getParent().getParent();
        MouseEvent mouse = new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null);
        sDesktopFX.fireEvent(mouse);
    }

    @Override
    public void closeSource(String name) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
