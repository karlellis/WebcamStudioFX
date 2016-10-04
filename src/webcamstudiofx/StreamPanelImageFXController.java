/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import static webcamstudiofx.TruckliststudioUIController.stream_;
import static webcamstudiofx.TruckliststudioUIController.wsDistroWatch;
import webcamstudiofx.components.SpinnerAutoCommit;
import webcamstudiofx.mixers.MasterMixer;
import webcamstudiofx.streams.Stream;
import webcamstudiofx.util.Tools;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class StreamPanelImageFXController implements Initializable, Stream.Listener, StreamDesktopFXController.Listener {

    Stream stream = null;
    int oldW;
    int oldH;
    boolean lockRatio = false;

    SpinnerValueFactory valSpinX;
    SpinnerValueFactory valSpinY;
    SpinnerValueFactory valSpinW;
    SpinnerValueFactory valSpinH;
    SpinnerValueFactory valSpinOpacity = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
    SpinnerValueFactory valSpinZOrder = new SpinnerValueFactory.IntegerSpinnerValueFactory(-10, +10);

    String stringPlay = "-fx-background-color: linear-gradient(greenyellow, limegreen);";
    String stringStop = "-fx-background-color: -fx-box-border, -fx-inner-border, -fx-body-color;";

    private SpinnerAutoCommit<Integer> spinX = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinY = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinW = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinH = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinOpacity = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinZOrder = new SpinnerAutoCommit<>();

    String distro = wsDistroWatch();
    private boolean runMe = true;
    private int speed = 1;
    Tooltip layer = new Tooltip();

    @FXML
    private AnchorPane StreamPanelFX;
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
    private ToggleButton tglAR;
    @FXML
    private ToolBar streamToolBar;
    @FXML
    private ToggleButton tglActiveStream;
    @FXML
    private ToggleButton tglPreview;
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
        valSpinZOrder.setValue(stream.getZOrder());
        tglActiveStream.setSelected(stream.isPlaying());

        if (stream.isPlaying()) {
            sDesktopFX.lookup(".title").setStyle(stringPlay);
            tglPreview.setDisable(true);
        } else {
            sDesktopFX.lookup(".title").setStyle(stringStop);
            tglPreview.setDisable(false);
        }

        layer.setText("Layer=" + stream.getZOrder());
        sDesktopFX.setTooltip(layer);
    }

    @Override
    public void updatePreview(BufferedImage image) {
        // Nothing here.
    }

    @Override
    public void selectedSource(Stream source) {
        // Nothing here.
    }

    @Override
    public void closeSource(String name) {
        // Nothing here.
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

    private void spinZOrderStateChanged() {
        stream.setZOrder(spinZOrder.getValue());
        jslSpinZOrder.setValue(spinZOrder.getValue());
    }

    private void jslSpinZOrderStateChanged() {
        valSpinZOrder.setValue((int) jslSpinZOrder.getValue());
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
        stream.setPanelType("Panel");

        oldW = stream.getWidth();
        oldH = stream.getHeight();

        spinOpacity.editableProperty().set(true);
        spinOpacity.setLayoutX(124.0);
        spinOpacity.setLayoutY(124.0);
        spinOpacity.setMaxHeight(-Double.MAX_VALUE);
        spinOpacity.setMinHeight(20.0);
        spinOpacity.setPrefHeight(20.0);
        spinOpacity.setPrefWidth(93.0);
        StreamPanelFX.getChildren().add(spinOpacity);
        spinOpacity.setVisible(true);

        spinX.editableProperty().set(true);
        spinX.setLayoutX(124.0);
        spinX.setLayoutY(40.0);
        spinX.setMaxHeight(-Double.MAX_VALUE);
        spinX.setMinHeight(20.0);
        spinX.setPrefHeight(20.0);
        spinX.setPrefWidth(93.0);
        StreamPanelFX.getChildren().add(spinX);
        spinX.setVisible(true);

        spinY.editableProperty().set(true);
        spinY.setLayoutX(124.0);
        spinY.setLayoutY(61.0);
        spinY.setMaxHeight(-Double.MAX_VALUE);
        spinY.setMinHeight(20.0);
        spinY.setPrefHeight(20.0);
        spinY.setPrefWidth(93.0);
        StreamPanelFX.getChildren().add(spinY);
        spinY.setVisible(true);

        spinW.editableProperty().set(true);
        spinW.setLayoutX(124.0);
        spinW.setLayoutY(82.0);
        spinW.setMaxHeight(-Double.MAX_VALUE);
        spinW.setMinHeight(20.0);
        spinW.setPrefHeight(20.0);
        spinW.setPrefWidth(93.0);
        StreamPanelFX.getChildren().add(spinW);
        spinW.setVisible(true);

        spinH.editableProperty().set(true);
        spinH.setLayoutX(124.0);
        spinH.setLayoutY(103.0);
        spinH.setMaxHeight(-Double.MAX_VALUE);
        spinH.setMinHeight(20.0);
        spinH.setPrefHeight(20.0);
        spinH.setPrefWidth(93.0);
        StreamPanelFX.getChildren().add(spinH);
        spinH.setVisible(true);

        spinZOrder.editableProperty().set(true);
        spinZOrder.setLayoutX(124.0);
        spinZOrder.setLayoutY(145.0);
        spinZOrder.setMaxHeight(-Double.MAX_VALUE);
        spinZOrder.setMinHeight(20.0);
        spinZOrder.setPrefHeight(20.0);
        spinZOrder.setPrefWidth(93.0);
        StreamPanelFX.getChildren().add(spinZOrder);
        spinZOrder.setVisible(true);

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

        valSpinZOrder.valueProperty().addListener((obs, oldValue, newValue)
                -> spinZOrderStateChanged());
        jslSpinZOrder.valueProperty().addListener((obs, oldValue, newValue)
                -> jslSpinZOrderStateChanged());
        spinZOrder.setValueFactory(valSpinZOrder);
        valSpinZOrder.setValue(stream.getZOrder());
        stream.setListener(this);

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
    private void tglActiveStreamAction(ActionEvent event) {
        if (tglActiveStream.isSelected()) {
            tglPreview.setDisable(true);
            TitledPane sDesktopFX = (TitledPane) StreamPanelFX.getParent().getParent();
            sDesktopFX.lookup(".title").setStyle(stringPlay);
            stream.read();
        } else {
            TitledPane sDesktopFX = (TitledPane) StreamPanelFX.getParent().getParent();
            sDesktopFX.lookup(".title").setStyle(stringStop);
            tglPreview.setDisable(false);
            stream.stop();

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

}
