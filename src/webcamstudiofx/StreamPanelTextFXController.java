/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javax.swing.ImageIcon;
import static webcamstudiofx.TrackPanelFXController.workDone;
import static webcamstudiofx.TruckliststudioUIController.stream_;
import webcamstudiofx.components.SpinnerAutoCommit;
import webcamstudiofx.mixers.MasterMixer;
import webcamstudiofx.streams.SourceText;
import webcamstudiofx.streams.Stream;
import webcamstudiofx.util.Tools;

/**
 *
 * @author elli
 */
public class StreamPanelTextFXController implements Initializable, Stream.Listener, StreamDesktopFXController.Listener {

    Stream stream = null;
    SourceText sTx = null;
    boolean stopClock = false;
    boolean stopCDown = false;
    private Timer time = new Timer();
    private Timer countDown = new Timer();
    private TimerTask clockIn = new clock();
    private TimerTask cDownIn = new cDown();
    boolean lockRatio = false;
    int oldW = 1;
    int oldH = 1;
    MasterMixer mixer = MasterMixer.getInstance();
    SpinnerValueFactory valSpinX;// = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1280);
    SpinnerValueFactory valSpinY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1024);
    SpinnerValueFactory valSpinW = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1280);
    SpinnerValueFactory valSpinH = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1024);
    SpinnerValueFactory valSpinZOrder = new SpinnerValueFactory.IntegerSpinnerValueFactory(-10, +10);
    SpinnerValueFactory valSpinDuration = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);

    String stringPlay = "-fx-background-color: linear-gradient(greenyellow, limegreen);";
    String stringStop = "-fx-background-color: -fx-box-border, -fx-inner-border, -fx-body-color;";

    private SpinnerAutoCommit<Integer> spinX = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinY = new SpinnerAutoCommit<>();
    private SpinnerAutoCommit<Integer> spinW = new SpinnerAutoCommit<Integer>();
    private SpinnerAutoCommit<Integer> spinH = new SpinnerAutoCommit<Integer>();
    private SpinnerAutoCommit<Integer> spinDuration = new SpinnerAutoCommit<Integer>();
    private SpinnerAutoCommit<Integer> spinZOrder = new SpinnerAutoCommit<Integer>();

    private int speed = 1;
    private boolean runMe = true;
    Tooltip clock = new Tooltip();
    Tooltip timer = new Tooltip();
    Tooltip name = new Tooltip();

    @FXML
    private AnchorPane StreamPanelTextFX;
    @FXML
    private Label lblTxtMode;
    @FXML
    private CheckBox jcbPlaylist;
    @FXML
    private ComboBox cboFonts;
    @FXML
    private ColorPicker txtHexColor;
    @FXML
    private TextArea txtArea;
    @FXML
    private ToggleButton tglCDown;
    @FXML
    private ToggleButton tglClock;
    @FXML
    private ToggleButton tglQRCode;
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
    private Slider jslSpinX;
    @FXML
    private Slider jslSpinY;
    @FXML
    private Slider jslSpinW;
    @FXML
    private Slider jslSpinH;
    @FXML
    private Slider jslSpinZOrder;
    @FXML
    private ToggleButton tglAR;
    @FXML
    private ToggleButton tglPreview;
    @FXML
    private ToggleButton tglActiveStream;

    @FXML
    private void jcbPlaylistAction(ActionEvent event) {
        if (jcbPlaylist.isSelected()) {
            stream.setPlayList(true);
            spinDuration.setDisable(true);
        } else {
            stream.setPlayList(false);
            spinDuration.setDisable(false);
        }
    }

    @FXML
    private void txtHexColorAction(ActionEvent event) {
        Color c = txtHexColor.getValue();
        String hex = String.format("#%02X%02X%02X",
                (int) (c.getRed() * 255),
                (int) (c.getGreen() * 255),
                (int) (c.getBlue() * 255));
//        System.out.println(hex);
        System.out.print(java.awt.Color.decode(hex));
        stream.setColor(hex);
//        System.out.println((int)c.getRed()+(int)c.getGreen()+(int)c.getBlue());
    }

    @FXML
    private void tglCDownAction(ActionEvent event) {
        if (tglCDown.isSelected()) {
            tglQRCode.setDisable(true);
            tglClock.setDisable(true);
            stream.setIsACDown(true);
            if (stream.getIsQRCode()) {
                lblTxtMode.setText("QR Timer Mode.");
            } else {
                lblTxtMode.setText("Timer Mode.");
            }
            stopCDown = false;
        } else {
            tglQRCode.setDisable(false);
            tglClock.setDisable(false);
            stream.setIsACDown(false);
            if (stream.getIsQRCode()) {
                lblTxtMode.setText("QR Code Mode.");
            } else {
                lblTxtMode.setText("Text Mode.");
            }
            stopCDown = true;
            stream.updateContent(txtArea.getText());
        }
    }

    @FXML
    private void tglClockAction(ActionEvent event) {
        if (tglClock.isSelected()) {
            tglQRCode.setDisable(true);
            tglCDown.setDisable(true);
            stream.setIsATimer(true);
            if (stream.getIsQRCode()) {
                lblTxtMode.setText("QR Clock Mode.");
            } else {
                lblTxtMode.setText("Text Clock Mode.");
            }
            stopClock = false;
        } else {
            tglQRCode.setDisable(false);
            tglCDown.setDisable(false);
            stream.setIsATimer(false);
            if (stream.getIsQRCode()) {
                lblTxtMode.setText("QR Code Mode.");
            } else {
                lblTxtMode.setText("Text Mode.");
                stream.updateContent(txtArea.getText());
            }
            stopClock = true;
        }
    }

    @FXML
    private void tglQRCodeAction(ActionEvent event) {
        if (tglQRCode.isSelected()) {
            stream.setIsQRCode(true);
            lblTxtMode.setText("QR Code Mode.");
            cboFonts.setDisable((stream.getIsQRCode()));
            txtHexColor.setDisable((stream.getIsQRCode()));
            txtHexColor.setDisable((stream.getIsQRCode()));
        } else {
            stream.setIsQRCode(false);
            lblTxtMode.setText("Text Mode.");
            stream.updateContent(txtArea.getText());
            cboFonts.setDisable((stream.getIsQRCode()));
            txtHexColor.setDisable((stream.getIsQRCode()));
        }
    }

    @FXML
    private void tglActiveStreamAction(ActionEvent event) {
        if (tglClock.isSelected()) {
            time = new Timer();
            clockIn = new clock();
            if (tglActiveStream.isSelected()) {
                TitledPane sDesktopFX = (TitledPane) StreamPanelTextFX.getParent().getParent();
                sDesktopFX.lookup(".title").setStyle(stringPlay);
                tglClock.setDisable(true);
                tglQRCode.setDisable(true);
                tglCDown.setDisable(true);
                tglPreview.setDisable(true);
                if (stream.getIsQRCode()) {
                    lblTxtMode.setText("QR Clock Mode.");
                } else {
                    lblTxtMode.setText("Text Clock Mode.");
                }
                stopClock = false;
                time.schedule(clockIn, 0);
                stream.read();
//                System.out.println("Starting Clock ...");
            } else {
                TitledPane sDesktopFX = (TitledPane) StreamPanelTextFX.getParent().getParent();
                sDesktopFX.lookup(".title").setStyle(stringStop);
                tglPreview.setDisable(false);
                tglClock.setDisable(false);
                tglQRCode.setDisable(tglClock.isSelected());
                tglCDown.setDisable(tglClock.isSelected());
                time.cancel();
                time.purge();
                clockIn.cancel();
                stopClock = true;
                stream.stop();
//                System.out.println("Stopping Clock ...");
            }
        } else if (tglCDown.isSelected()) {
            if (stream.getDuration() == 0 && !stream.getPlayList()) {
                tglActiveStream.setSelected(false);
            } else {
                countDown = new Timer();
                cDownIn = new cDown();
                if (tglActiveStream.isSelected()) {
                    stopCDown = false;
                    countDown.schedule(cDownIn, 0);
                    stream.read();
                    TitledPane sDesktopFX = (TitledPane) StreamPanelTextFX.getParent().getParent();
                    sDesktopFX.lookup(".title").setStyle(stringPlay);
                    tglClock.setDisable(true);
                    tglQRCode.setDisable(true);
                    tglCDown.setDisable(true);
                    tglPreview.setDisable(true);
                    if (stream.getIsQRCode()) {
                        lblTxtMode.setText("QR Timer Mode.");
                    } else {
                        lblTxtMode.setText("Timer Mode.");
                    }
                } else {
                    countDown.cancel();
                    countDown.purge();
                    cDownIn.cancel();
                    stopCDown = true;
                    stream.stop();
                    TitledPane sDesktopFX = (TitledPane) StreamPanelTextFX.getParent().getParent();
                    sDesktopFX.lookup(".title").setStyle(stringStop);
                    tglPreview.setDisable(false);
                    tglClock.setDisable(tglCDown.isSelected());
                    tglQRCode.setDisable(tglCDown.isSelected());
                    tglCDown.setDisable(false);
                }
            }
        } else if (tglActiveStream.isSelected()) {
            tglPreview.setDisable(true);
            tglClock.setDisable(true);
            tglQRCode.setDisable(true);
            tglCDown.setDisable(true);
            TitledPane sDesktopFX = (TitledPane) StreamPanelTextFX.getParent().getParent();
            sDesktopFX.lookup(".title").setStyle(stringPlay);
            stream.read();
        } else {
            tglPreview.setDisable(false);
            tglClock.setDisable(false);
            tglQRCode.setDisable(false);
            tglCDown.setDisable(false);
            TitledPane sDesktopFX = (TitledPane) StreamPanelTextFX.getParent().getParent();
            sDesktopFX.lookup(".title").setStyle(stringStop);
            stream.stop();
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
    private void tglPreviewAction(ActionEvent event) {
        if (tglPreview.isSelected()) {
            stream.setPreView(true);
        } else {
            stream.setPreView(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.stream = stream_;

        stream.setPanelType("PanelText");

        if (TruckliststudioUIController.theme.toLowerCase().equals("modena")) {
            lblTxtMode.setTextFill(javafx.scene.paint.Color.BLACK);
        }
        oldW = stream.getWidth();
        oldH = stream.getHeight();

        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = e.getAllFonts(); // Get the fonts
        ObservableList<String> fontList = FXCollections.observableArrayList();
        for (Font f : fonts) {
            fontList.add(f.getName());
        }
        cboFonts.setItems(fontList);

        this.stream = stream_;
        sTx = (SourceText) stream;

        spinDuration.editableProperty().set(true);
        spinDuration.setLayoutX(185.0);
        spinDuration.setLayoutY(71.0);
        spinDuration.setMaxHeight(-Double.MAX_VALUE);
        spinDuration.setMinHeight(20.0);
        spinDuration.setPrefHeight(20.0);
        spinDuration.setPrefWidth(93.0);
        StreamPanelTextFX.getChildren().add(spinDuration);
        spinDuration.setVisible(true);

        spinX.editableProperty().set(true);
        spinX.setLayoutX(124.0);
        spinX.setLayoutY(168.0);
        spinX.setMaxHeight(-Double.MAX_VALUE);
        spinX.setMinHeight(20.0);
        spinX.setPrefHeight(20.0);
        spinX.setPrefWidth(93.0);
        StreamPanelTextFX.getChildren().add(spinX);
        spinX.setVisible(true);

        spinY.editableProperty().set(true);
        spinY.setLayoutX(124.0);
        spinY.setLayoutY(190.0);
        spinY.setMaxHeight(-Double.MAX_VALUE);
        spinY.setMinHeight(20.0);
        spinY.setPrefHeight(20.0);
        spinY.setPrefWidth(93.0);
        StreamPanelTextFX.getChildren().add(spinY);
        spinY.setVisible(true);

        spinW.editableProperty().set(true);
        spinW.setLayoutX(124.0);
        spinW.setLayoutY(212.0);
        spinW.setMaxHeight(-Double.MAX_VALUE);
        spinW.setMinHeight(20.0);
        spinW.setPrefHeight(20.0);
        spinW.setPrefWidth(93.0);
        StreamPanelTextFX.getChildren().add(spinW);
        spinW.setVisible(true);

        spinH.editableProperty().set(true);
        spinH.setLayoutX(124.0);
        spinH.setLayoutY(234.0);
        spinH.setMaxHeight(-Double.MAX_VALUE);
        spinH.setMinHeight(20.0);
        spinH.setPrefHeight(20.0);
        spinH.setPrefWidth(93.0);
        StreamPanelTextFX.getChildren().add(spinH);
        spinH.setVisible(true);

        spinZOrder.editableProperty().set(true);
        spinZOrder.setLayoutX(124.0);
        spinZOrder.setLayoutY(256.0);
        spinZOrder.setMaxHeight(-Double.MAX_VALUE);
        spinZOrder.setMinHeight(20.0);
        spinZOrder.setPrefHeight(20.0);
        spinZOrder.setPrefWidth(93.0);
        StreamPanelTextFX.getChildren().add(spinZOrder);
        spinZOrder.setVisible(true);

        valSpinX = new SpinnerValueFactory.IntegerSpinnerValueFactory(-mixer.getWidth(), mixer.getWidth());
        jslSpinX.setMax(mixer.getWidth());
        jslSpinX.setMin(-mixer.getWidth());
        valSpinX.valueProperty().addListener((obs, oldValue, newValue)
                -> spinXStateChanged());
        jslSpinX.valueProperty().addListener((obs, oldValue, newValue)
                -> jslSpinXStateChanged());
        jslSpinX.setMajorTickUnit(mixer.getWidth() / 2);
        jslSpinX.showTickMarksProperty().setValue(Boolean.TRUE);
        spinX.setValueFactory(valSpinX);
        valSpinX.setValue(sTx.getX());

        valSpinY = new SpinnerValueFactory.IntegerSpinnerValueFactory(-mixer.getHeight(), mixer.getHeight());
        jslSpinY.setMax(mixer.getHeight());
        jslSpinY.setMin(-mixer.getHeight());
        valSpinY.valueProperty().addListener((obs, oldValue, newValue)
                -> spinYStateChanged());
        jslSpinY.valueProperty().addListener((obs, oldValue, newValue)
                -> jslSpinYStateChanged());
        jslSpinY.setMajorTickUnit(mixer.getHeight() / 2);
        jslSpinY.showTickMarksProperty().setValue(Boolean.TRUE);
        spinY.setValueFactory(valSpinY);
        valSpinY.setValue(sTx.getY());

        valSpinW = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, mixer.getWidth());
        jslSpinW.setMax(mixer.getWidth());
        valSpinW.valueProperty().addListener((obs, oldValue, newValue)
                -> spinWStateChanged());
        jslSpinW.valueProperty().addListener((obs, oldValue, newValue)
                -> jslSpinWStateChanged());
        jslSpinW.setMajorTickUnit(mixer.getWidth() / 2);
        jslSpinW.showTickMarksProperty().setValue(Boolean.TRUE);
        spinW.setValueFactory(valSpinW);
        valSpinW.setValue(sTx.getWidth());

        valSpinH = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, mixer.getHeight());
        jslSpinH.setMax(mixer.getHeight());
        valSpinH.valueProperty().addListener((obs, oldValue, newValue)
                -> spinHStateChanged());
        jslSpinH.valueProperty().addListener((obs, oldValue, newValue)
                -> jslSpinHStateChanged());
        jslSpinH.setMajorTickUnit(mixer.getHeight() / 2);
        jslSpinH.showTickMarksProperty().setValue(Boolean.TRUE);
        spinH.setValueFactory(valSpinH);
        valSpinH.setValue(sTx.getHeight());

        spinDuration.setDisable(sTx.getPlayList());
        valSpinDuration = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);
        valSpinDuration.valueProperty().addListener((obs, oldValue, newValue)
                -> spinDurationStateChanged());

        spinDuration.setValueFactory(valSpinDuration);
        valSpinDuration.setValue(sTx.getDuration());

        valSpinZOrder.valueProperty().addListener((obs, oldValue, newValue)
                -> spinZOrderStateChanged());
        jslSpinZOrder.valueProperty().addListener((obs, oldValue, newValue)
                -> jslSpinZOrderStateChanged());
        spinZOrder.setValueFactory(valSpinZOrder);
        valSpinZOrder.setValue(sTx.getZOrder());

        cboFonts.getSelectionModel().select(sTx.getFont());

        txtHexColor.setValue(Color.valueOf(sTx.getColor())); //setText(Integer.toHexString(sTx.getColor()));

        if (sTx.getIsATimer()) {
            if (sTx.getIsQRCode()) {
                lblTxtMode.setText("QR Clock Mode.");
            } else {
                lblTxtMode.setText("Text Clock Mode.");
                tglQRCode.setDisable(true);
                tglCDown.setDisable(true);
            }
        } else if (sTx.getIsACDown()) {
            if (sTx.getIsQRCode()) {
                lblTxtMode.setText("QR Timer Mode.");
            } else {
                lblTxtMode.setText("Timer Mode.");
                tglQRCode.setDisable(true);
                tglClock.setDisable(true);
            }
        } else {
            txtArea.setText(sTx.getContent());
            lblTxtMode.setText("Text Mode.");
        }
        jcbPlaylist.setSelected(sTx.getPlayList());
        cboFonts.setDisable(sTx.getIsQRCode());
        txtHexColor.setDisable(sTx.getIsQRCode());
        txtHexColor.setDisable(sTx.getIsQRCode());
        tglClock.setSelected(sTx.getIsATimer());
        tglQRCode.setSelected(sTx.getIsQRCode());
        tglCDown.setSelected(sTx.getIsACDown());
        sTx.setListener(this);
    }

    public String getHHMMSS(long seconds) {
        long hr = seconds / 3600;
        long rem = seconds % 3600;
        long mn = rem / 60;
        long sec = rem % 60;
        String hrStr = (hr < 10 ? "0" : "") + hr;
        String mnStr = (mn < 10 ? "0" : "") + mn;
        String secStr = (sec < 10 ? "0" : "") + sec;
        return hrStr + ":" + mnStr + ":" + secStr;
    }

    @Override
    public void sourceUpdated(Stream stream) {
//        System.out.println("Source Updated !!!");

        clock.setText("Clock Mode | Layer=" + stream.getZOrder());
        timer.setText("Timer Mode | Layer=" + stream.getZOrder());

        TitledPane sDesktopFX = (TitledPane) StreamPanelTextFX.getParent().getParent();
        if (stream.isPlaying()) {

            sDesktopFX.lookup(".title").setStyle(stringPlay);
            tglPreview.setDisable(true);
            if (stream.getIsATimer()) {
                stopClock = false;
                time = new Timer();
                clockIn = new clock();
                time.schedule(clockIn, 0);
                tglClock.setSelected(true);
                tglCDown.setSelected(false);
                sDesktopFX.setTooltip(clock);
            } else if (stream.getIsACDown()) {
                stopCDown = false;
                countDown = new Timer();
                cDownIn = new cDown();
                countDown.schedule(cDownIn, 0);
                tglCDown.setSelected(true);
                tglClock.setSelected(false);
                sDesktopFX.setTooltip(timer);
//                System.out.println("Source Updated Starting Timer ...");
            } else {
                stopCDown = true;
                countDown.cancel();
                countDown.purge();
                cDownIn.cancel();
                stopClock = true;
                time.cancel();
                time.purge();
                clockIn.cancel();
                tglClock.setSelected(false);
                tglCDown.setSelected(false);
            }
        } else {
            sDesktopFX.lookup(".title").setStyle(stringStop);
            tglPreview.setDisable(false);
            if (stream.getIsATimer()) {
                stopClock = true;
                time.cancel();
                time.purge();
                clockIn.cancel();
                tglClock.setSelected(true);
                sDesktopFX.setTooltip(clock);
//                System.out.println("Source Updated Stopping Clock ...");
            } else if (stream.getIsACDown()) {
                stopCDown = true;
                countDown.cancel();
                countDown.purge();
                cDownIn.cancel();
                tglCDown.setSelected(true);
                sDesktopFX.setTooltip(timer);
//                System.out.println("Source Updated Stopping Timer ...");
            } else {
                tglClock.setSelected(false);
                tglCDown.setSelected(false);
            }
        }
        if (stream.getIsQRCode()) {
            tglQRCode.setSelected(true);
            cboFonts.setDisable((stream.getIsQRCode()));
            txtHexColor.setDisable((stream.getIsQRCode()));
        } else {
            tglQRCode.setSelected(false);
            cboFonts.setDisable((stream.getIsQRCode()));
            txtHexColor.setDisable((stream.getIsQRCode()));
        }
        int mixerW = MasterMixer.getInstance().getWidth();
        int mixerH = MasterMixer.getInstance().getHeight();

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
        valSpinDuration.setValue(stream.getDuration());
        cboFonts.getSelectionModel().select(this.stream.getFont()); //setSelectedItem(this.stream.getFont());
        txtHexColor.setValue(Color.valueOf(stream.getColor()));
        valSpinZOrder.setValue(stream.getZOrder());

        if (!stream.getIsATimer() && !stream.getIsACDown()) {
            txtArea.setText(this.stream.getContent());
//            System.out.println("Tip Setted:" + this.stream.getContent());
            Platform.runLater(() -> {
                name.setText("\"" + this.stream.getContent() + "\"" + " | Layer=" + stream.getZOrder());
                sDesktopFX.setTooltip(name);
            });
        }
        tglActiveStream.setSelected(stream.isPlaying());

        if (stream.getIsATimer() || stream.getIsACDown() || stream.isPlaying()) {
            tglQRCode.setDisable(true);
        } else {
            tglQRCode.setDisable(false);
        }
        if (stream.getIsATimer() || stream.isPlaying()) {
            tglCDown.setDisable(true);
        } else {
            tglCDown.setDisable(false);
        }
        if (stream.getIsACDown() || stream.isPlaying()) {
            tglClock.setDisable(true);
        } else {
            tglClock.setDisable(false);
        }
        jcbPlaylist.setSelected(stream.getPlayList());
    }

    @Override
    public void updatePreview(BufferedImage image) {
        // nothing here.
    }

    @Override
    public void selectedSource(Stream source) {
        // nothing here.
    }

    @Override
    public void closeSource(String name) {
        // nothing here.
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

    private void spinDurationStateChanged() {
        stream.setDuration((Integer) valSpinDuration.getValue());
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
    private void cboFontsAction(ActionEvent event) {
        stream.setFont(cboFonts.getSelectionModel().getSelectedItem().toString());
    }

    @FXML
    private void tglAudioAction(ActionEvent event) {
    }

    @FXML
    private void txtAreaKeyReleasedAction(KeyEvent event) {
        if (!stream.getIsATimer() && !stream.getIsACDown()) {
            stream.updateContent(txtArea.getText());
        }
    }

    class clock extends TimerTask {

        @Override
        public void run() {
            while (!stopClock) {
                long milliSeconds = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
                Date resultdate = new Date(milliSeconds);
                String time = sdf.format(resultdate);
                stream.updateLineContent(time);
                Tools.sleep(1000);
            }
            StreamPanelTextFXController.clock.this.stop();
        }

        public void stop() {
            time.cancel();
            time.purge();
            clockIn.cancel();
            stopClock = true;
            stream.stop();
//            System.out.println("Stopping Clock ...");
        }

    }

    class cDown extends TimerTask {

        @Override
        public void run() {

            if (stream.getPlayList()) {
                while (!stopCDown) {
                    int chTimeTo = TrackPanelFXController.totalToTimer;
                    final String chTotalTime = getHHMMSS(chTimeTo);
                    int dur = chTimeTo - workDone.getValue().intValue();
                    String duration = getHHMMSS(dur);
                    stream.updateLineContent(duration + " / " + chTotalTime);
                    Tools.sleep(1000);
                }
            } else {
                int timeTo = stream.getDuration();
                final String totalTime = getHHMMSS(timeTo);
                int count = 0;
                String duration = "";
                while (!stopCDown && count < timeTo) {
                    duration = getHHMMSS(count);
                    stream.updateLineContent(duration + " / " + totalTime);
                    Tools.sleep(1000);
                    count++;
                }
            }
            StreamPanelTextFXController.cDown.this.stop();
        }

        public void stop() {
            countDown.cancel();
            countDown.purge();
            cDownIn.cancel();
            stopCDown = true;
            stream.stop();
            stream.updateStatus();
//            System.out.println("Stopping Timer ...");
        }
    }

    public ImageIcon getIcon() {
        ImageIcon icon = null;
        if (stream.getPreview() != null) {
            icon = new ImageIcon(stream.getPreview().getScaledInstance(32, 32, BufferedImage.SCALE_FAST));
        }
        return icon;
    }

    public void remove() {
        stream.stop();
        stream = null;

    }
}
