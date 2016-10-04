/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx;

import static com.jhlabs.image.ImageUtils.cloneImage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.sound.sampled.LineUnavailableException;
import static webcamstudiofx.TrackPanelFXController.listenerCPOP;
import webcamstudiofx.components.PreviewerFXController;
import webcamstudiofx.components.ResourceMonitorFXController;
import webcamstudiofx.components.ResourceMonitorLabelFX;
import webcamstudiofx.components.SpinnerAutoCommit;
import webcamstudiofx.components.ViewerFXController;
import static webcamstudiofx.components.ViewerFXController.liveViewCanvas;
import webcamstudiofx.mixers.Frame;
import webcamstudiofx.mixers.MasterMixer;
import webcamstudiofx.mixers.PrePlayer;
import webcamstudiofx.mixers.PreviewMixer;
import webcamstudiofx.mixers.SystemPlayer;
import webcamstudiofx.streams.SourceImage;
import webcamstudiofx.streams.SourceText;
import webcamstudiofx.streams.SourceTrack;
import webcamstudiofx.streams.Stream;
import webcamstudiofx.tracks.MasterTracks;
import webcamstudiofx.util.Tools;
import static webcamstudiofx.components.PreviewerFXController.previewCanvas;
import static webcamstudiofx.components.ViewerFXController.viewerRate;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class MainPanelFXController implements Initializable, MasterMixer.SinkListener, TrackPanelFXController.Listener, PreviewMixer.SinkListener {

    public static ViewerFXController viewerFX = new ViewerFXController();
    public static PreviewerFXController previewerFX = new PreviewerFXController();

    public static SystemPlayer player = null;
    public static PrePlayer prePlayer = null;

    private final MasterMixer mixer = MasterMixer.getInstance();
    private final PreviewMixer preMixer = PreviewMixer.getInstance();

    MasterTracks master = MasterTracks.getInstance();
    final static public Dimension PANEL_SIZE = new Dimension(150, 400);
    final static public Dimension SMALL_PANEL_SIZE = new Dimension(240, 125);
    ArrayList<Stream> streamM = MasterTracks.getInstance().getStreams();
    Stream stream = null;
    SourceText sTx = null;
    SourceImage sImg = null;
    boolean lockRatio = false;
    private BufferedImage liveImg = null;
    int opacity = 50;
    int rate = mixer.getRate();
    int i = rate;
    public static float masterVolume = 0f;
    boolean transition = false;

    public static SpinnerValueFactory valW = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1280);
    public static SpinnerValueFactory valH = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1024);
    public static SpinnerValueFactory valFPS = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30);

    private SpinnerAutoCommit<Integer> spinWidth = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinHeight = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinFPS = new SpinnerAutoCommit<>();

    public static SpinnerAutoCommit<Integer> spinWidth_;
    public static SpinnerAutoCommit<Integer> spinHeight_;
    public static SpinnerAutoCommit<Integer> spinFPS_;
    public static AnchorPane viewerFXNode;
    public static AnchorPane streamFullPanelFX;
    public static AnchorPane previewerFXNode;
    public static AnchorPane TSPreviewPanelFX;

    @FXML
    private Button btnFullScreen;
    @FXML
    private Slider jslMasterVolume;
    @FXML
    private Button btnApply;
    @FXML
    private Button btnApplyToStreams;
    @FXML
    private ToggleButton tglLockRatio;
    @FXML
    private ToggleButton tglSound;
    @FXML
    private Pane panelPreview;
    @FXML
    private Slider jslOpacity;
    @FXML
    private Pane panelLiveView;
    @FXML
    private AnchorPane mainPanelFX;
    @FXML
    private Button btnPreview;
    @FXML
    private Pane tabMixer;
    @FXML
    private ComboBox cboViewRate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            viewerFXNode = (AnchorPane) FXMLLoader.load(getClass().getResource("/webcamstudiofx/components/ViewerFX.fxml"));
            previewerFXNode = (AnchorPane) FXMLLoader.load(getClass().getResource("/webcamstudiofx/components/PreviewerFX.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MainPanelFXController.class.getName()).log(Level.SEVERE, null, ex);
        }

        ObservableList<Integer> vRates = FXCollections.observableArrayList();
        vRates.add(5);
        vRates.add(10);
        vRates.add(15);
        vRates.add(20);
        vRates.add(25);
        vRates.add(30);
        cboViewRate.setItems(vRates);
        cboViewRate.getSelectionModel().select(2);

        spinWidth.editableProperty().set(true);
        spinWidth.setLayoutX(69.0);
        spinWidth.setLayoutY(9.0);
        spinWidth.setPrefHeight(26.0);
        spinWidth.setPrefWidth(83.0);
        tabMixer.getChildren().add(spinWidth);
        spinWidth.setVisible(true);

        spinHeight.editableProperty().set(true);
        spinHeight.setLayoutX(69.0);
        spinHeight.setLayoutY(39.0);
        spinHeight.setPrefHeight(26.0);
        spinHeight.setPrefWidth(83.0);
        tabMixer.getChildren().add(spinHeight);
        spinHeight.setVisible(true);

        spinFPS.editableProperty().set(true);
        spinFPS.setLayoutX(69.0);
        spinFPS.setLayoutY(71.0);
        spinFPS.setPrefHeight(26.0);
        spinFPS.setPrefWidth(161.0);
        tabMixer.getChildren().add(spinFPS);
        spinFPS.setVisible(true);

        spinWidth_ = spinWidth;
        spinHeight_ = spinHeight;
        spinFPS_ = spinFPS;

        panelLiveView.getChildren().add(viewerFXNode);
        panelPreview.getChildren().add(previewerFXNode);

        valW.setValue(MasterMixer.getInstance().getWidth());
        valH.setValue(MasterMixer.getInstance().getHeight());
        valFPS.setValue(MasterMixer.getInstance().getRate());
        spinWidth.setValueFactory(valW);
        spinHeight.setValueFactory(valH);
        spinFPS.setValueFactory(valFPS);

        spinWidth.valueProperty().addListener((obs, oldValue, newValue)
                -> spinWidthStateChanged());
        jslMasterVolume.valueProperty().addListener((obs, oldValue, newValue)
                -> jslMasterVolumeStateChanged());
        jslOpacity.valueProperty().addListener((obs, oldValue, newValue)
                -> jslOpacityStateChanged());
        jslOpacity.setValue(opacity);

        player = SystemPlayer.getInstanceFX(viewerFX);
        prePlayer = PrePlayer.getPreInstanceFX(previewerFX);

        mixer.register(this);
        preMixer.register(this);

        TrackPanelFXController.setListenerCPMPanel(this);

    }

    public void applyLoadedMixer() {
        int w = spinWidth.getValue();
        int h = spinHeight.getValue();
        mixer.stop();
        mixer.setWidth(w);
        mixer.setHeight(h);
        mixer.setRate(spinFPS.getValue());
        MasterMixer.getInstance().start();
        preMixer.stop();
        preMixer.setWidth(w);
        preMixer.setHeight(h);
        PreviewMixer.getInstance().start();
    }

    private void jslOpacityStateChanged() {
        opacity = (int) jslOpacity.getValue();
    }

    private void spinWidthStateChanged() {
        int oldW = mixer.getWidth();
        int oldH = mixer.getHeight();
        int w = spinWidth.getValue();
        valW.setValue(w);
        int h;
        if (tglLockRatio.isSelected()) {
            h = (oldH * w) / oldW;
            valH.setValue(h);
        }
    }

    @FXML
    private void btnFullScreenAction(ActionEvent event) throws IOException {
        btnFullScreen.setDisable(true);
        panelLiveView.getChildren().remove(viewerFXNode);
        Stage streamFullPanelStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/webcamstudiofx/components/StreamFullPanelFX.fxml"));
        streamFullPanelFX = (AnchorPane) loader.load();
        Scene scene = new Scene(streamFullPanelFX);
        streamFullPanelStage.setScene(scene);
        streamFullPanelStage.show();
        streamFullPanelStage.setAlwaysOnTop(true);

        streamFullPanelStage.setOnCloseRequest((WindowEvent t) -> {
//            System.out.println("FullClosed!!!");
            liveViewCanvas.widthProperty().unbind();
            liveViewCanvas.heightProperty().unbind();
            liveViewCanvas.widthProperty().set(240);
            liveViewCanvas.heightProperty().set(110);
            panelLiveView.getChildren().add(liveViewCanvas);
            btnFullScreen.setDisable(false);
        });
    }

    @FXML
    private void btnApplyAction(ActionEvent event) {
        SystemPlayer.getInstanceFX(null).stop();
        Tools.sleep(30);
        PrePlayer.getPreInstanceFX(null).stop();
        Tools.sleep(10);
        MasterTracks.getInstance().stopAllStream();
        streamM.stream().forEach((s) -> {
            s.updateStatus();
        });
        Tools.sleep(30);
        listenerCPOP.resetButtonsStates();
        int w = spinWidth.getValue();
        int h = spinHeight.getValue();

        mixer.stop();
        preMixer.stop();

        mixer.setWidth(w);
        preMixer.setWidth(w);

        mixer.setHeight(h);
        preMixer.setHeight(h);

        mixer.setRate(spinFPS.getValue());

        mixer.start();
        preMixer.start();

        streamM.stream().forEach((s) -> {
            String streamName = s.getClass().getName();
            //            System.out.println("StreamName: "+streamName);
            s.setRate(mixer.getRate());
            if (streamName.contains("SinkFile") || streamName.contains("SinkUDP")) {
                //                System.out.println("Sink New Size: "+w+"x"+h);
                s.setWidth(w);
                s.setHeight(h);
                s.updateStatus();
            }
        });
        listenerCPOP.resetButtonsStates();
        ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Mixer Settings Applied");
        ResourceMonitorFXController.getInstance().addMessage(label);
    }

    @FXML
    private void btnApplyToStreamsAction(ActionEvent event) {
        ArrayList<Stream> allStreams = MasterTracks.getInstance().getStreams();
        int wi = mixer.getWidth();
        int he = mixer.getHeight();
        int rate = mixer.getRate();
        int oldCW;
        int oldCH;
        for (Stream oneStream : allStreams) {
            if (!oneStream.getClass().toString().contains("Sink")) { // Don't Update SinkStreams
//                System.out.println("Processing "+oneStream.getName()+": ...");
                if (oneStream instanceof SourceText) {
                    sTx = (SourceText) oneStream;
                    oldCW = sTx.getTextCW();
                    oldCH = sTx.getTextCH();
                } else if (oneStream instanceof SourceImage) {
                    sImg = (SourceImage) oneStream;
                    oldCW = sImg.getImgCW();
                    oldCH = sImg.getImgCH();
                } else {
                    oldCW = oneStream.getCaptureWidth();
                    oldCH = oneStream.getCaptureHeight();
                }
//                System.out.println("oldCW: "+oldCW);
//                System.out.println("oldCH: "+oldCH);
                int oldW = oneStream.getWidth();
//                System.out.println("oldW: "+oldW);
                int oldH = oneStream.getHeight();
//                System.out.println("oldH: "+oldH);
                int oldX = oneStream.getX();
//                System.out.println("oldX: "+oldX);
                int oldY = oneStream.getY();
//                System.out.println("oldY: "+oldY);
                int newW = (oldW * wi) / oldCW;
//                System.out.println("newW: "+newW);
                int newH = (oldH * he) / oldCH;
//                System.out.println("newH: "+newH);
                int newX = (oldX * wi) / oldCW;
//                System.out.println("newX: "+newX);
                int newY = (oldY * he) / oldCH;
//                System.out.println("newY: "+newY);
                if (oneStream instanceof SourceText) {
                    oneStream.setWidth(newW);
                    oneStream.setHeight(newH);
                    oneStream.setX(newX);
                    oneStream.setY(newY);
                    oneStream.setCaptureWidth(newW);
                    oneStream.setCaptureHeight(newH);
                    oneStream.setRate(rate);
                    sTx.setTextCW(wi);
                    sTx.setTextCH(he);
//                    System.out.println(oneStream.getName()+" UpdateStatus !!!");
                    oneStream.updateStatus();
                    oneStream.getTracks().stream().map((ssc) -> {
                        ssc.setWidth(newW);
                        return ssc;
                    }).map((ssc) -> {
                        ssc.setHeight(newH);
                        return ssc;
                    }).map((ssc) -> {
                        ssc.setX(newX);
                        return ssc;
                    }).map((ssc) -> {
                        ssc.setY(newY);
                        return ssc;
                    }).map((ssc) -> {
                        ssc.setCapWidth(newW);
                        return ssc;
                    }).forEach((ssc) -> {
                        ssc.setCapHeight(newH);
                    });
                } else if (oneStream instanceof SourceImage) {
                    oneStream.setWidth(newW);
                    oneStream.setHeight(newH);
//                    System.out.println(oneStream.getName()+" Height:"+newH);
                    oneStream.setX(newX);
                    oneStream.setY(newY);
                    oneStream.setCaptureWidth(newW);
                    oneStream.setCaptureHeight(newH);
                    oneStream.setRate(rate);
                    sImg.setImgCW(wi);
                    sImg.setImgCH(he);
//                    System.out.println(oneStream.getName()+" UpdateStatus !!!");
                    oneStream.updateStatus();
                    oneStream.getTracks().stream().map((ssc) -> {
                        ssc.setWidth(newW);
                        return ssc;
                    }).map((ssc) -> {
                        ssc.setHeight(newH);
                        return ssc;
                    }).map((ssc) -> {
                        ssc.setX(newX);
                        return ssc;
                    }).map((ssc) -> {
                        ssc.setY(newY);
                        return ssc;
                    }).map((ssc) -> {
                        ssc.setCapWidth(newW);
                        return ssc;
                    }).forEach((ssc) -> {
                        ssc.setCapHeight(newH);
                    });
                } else {
                    oneStream.setWidth(newW);
                    oneStream.setHeight(newH);
                    oneStream.setX(newX);
                    oneStream.setY(newY);
                    oneStream.setCaptureWidth(wi);
                    oneStream.setCaptureHeight(he);
                    oneStream.setRate(rate);
//                    System.out.println(oneStream.getName()+" UpdateStatus !!!");
                    oneStream.updateStatus();
                    oneStream.getTracks().stream().map((ssc) -> {
                        ssc.setWidth(newW);
                        return ssc;
                    }).map((ssc) -> {
                        ssc.setHeight(newH);
                        return ssc;
                    }).map((ssc) -> {
                        ssc.setX(newX);
                        return ssc;
                    }).map((ssc) -> {
                        ssc.setY(newY);
                        return ssc;
                    }).map((ssc) -> {
                        ssc.setCapWidth(wi);
                        return ssc;
                    }).forEach((ssc) -> {
                        ssc.setCapHeight(he);
                    });
                }
            }
        }
        ResourceMonitorLabelFX label = new ResourceMonitorLabelFX(System.currentTimeMillis() + 10000, "Mixer Settings to All Streams");
        ResourceMonitorFXController.getInstance().addMessage(label);
    }

    @FXML
    private void tglLockRatioAction(ActionEvent event) {
        if (tglLockRatio.isSelected()) {
            spinHeight.setDisable(true);
            lockRatio = true;
        } else {
            spinHeight.setDisable(false);
            lockRatio = false;
        }
    }

    @FXML
    private void tglSoundAction(ActionEvent event) {
        if (tglSound.isSelected()) {
            try {
                player.play();
            } catch (LineUnavailableException ex) {
//                Logger.getLogger(MainPanelFXController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            player.stop();
        }
    }

    @Override
    public void newFrame(Frame frame) {
        if (transition) {
            if (i == 0) {
                i = rate;
                transition = false;
            }
            BufferedImage img = cloneImage(frame.getImage());
            BufferedImage temp = cloneImage(img);
            Graphics2D buffer = img.createGraphics();
            buffer.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_SPEED);
            buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF);
            buffer.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            buffer.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            buffer.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                    RenderingHints.VALUE_COLOR_RENDER_SPEED);
            buffer.setRenderingHint(RenderingHints.KEY_DITHERING,
                    RenderingHints.VALUE_DITHER_DISABLE);
            buffer.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, i * 100 / rate / 100F));
            buffer.setBackground(new Color(0, 0, 0, 0));
            buffer.clearRect(0, 0, img.getWidth(), img.getHeight());
            buffer.drawImage(temp, 0, 0, null);
            buffer.dispose();
            frame.setImage(img);
            i--;
        }
        player.addFrame(frame);
        liveImg = frame.getImage();
    }

    @Override
    public void resetButtonsStates() {
        // nothing here
    }

    @Override
    public void requestReset() {
        btnApply.fire();
    }

    @Override
    public void requestStop() {
        // nothing here
    }

    @Override
    public void requestStart() {
        // nothing here
    }

    @Override
    public void newPreFrame(Frame frame) {
        BufferedImage img = cloneImage(frame.getImage());
        BufferedImage lImg = cloneImage(liveImg);
        if (lImg != null) {
            Graphics2D buffer = lImg.createGraphics();
            buffer.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            buffer.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_SPEED);
            buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF);
            buffer.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            buffer.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            buffer.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                    RenderingHints.VALUE_COLOR_RENDER_SPEED);
            buffer.setRenderingHint(RenderingHints.KEY_DITHERING,
                    RenderingHints.VALUE_DITHER_DISABLE);
            buffer.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, opacity / 100F));
            buffer.drawImage(img, 0, 0, null);
            buffer.dispose();
            frame.setImage(lImg);
        }
        prePlayer.addFrame(frame);
    }

    @FXML
    private void btnPreviewAction(ActionEvent event) throws IOException {
        btnPreview.setDisable(true);
        panelPreview.getChildren().remove(previewerFXNode);
        Stage TSPreviewPanelStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/webcamstudiofx/components/TSPreviewPanelFX.fxml"));
        TSPreviewPanelFX = (AnchorPane) loader.load();
        Scene scene = new Scene(TSPreviewPanelFX);
        TSPreviewPanelStage.setScene(scene);
        TSPreviewPanelStage.show();
        TSPreviewPanelStage.setAlwaysOnTop(true);

        TSPreviewPanelStage.setOnCloseRequest((WindowEvent t) -> {
//            System.out.println("PreFullClosed!!!");
            previewCanvas.widthProperty().unbind();
            previewCanvas.heightProperty().unbind();
            previewCanvas.widthProperty().set(240);
            previewCanvas.heightProperty().set(110);
            panelPreview.getChildren().add(previewCanvas);
            btnPreview.setDisable(false);
        });
    }

    private void jslMasterVolumeStateChanged() {
        double value = jslMasterVolume.getValue();
        float v = 0;
        v = ((Number) value).floatValue();
        masterVolume = v / 100f;
    }

    @FXML
    private void cboViewRateAction(ActionEvent event) {
        viewerRate = (int) cboViewRate.getValue();
    }

}
