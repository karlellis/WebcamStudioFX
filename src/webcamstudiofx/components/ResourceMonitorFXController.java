/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.components;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import webcamstudiofx.mixers.MasterMixer;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class ResourceMonitorFXController implements Initializable {

    Timer timer = new Timer();
    private static ResourceMonitorFXController instance = null;
    static ProgressIndicatorBar memBar;
    static long maxMem;
    static long usedMem;
    public static HBox ResourceMonitorFX_;
    public static Label lblFPS_;

    @FXML
    private HBox ResourceMonitorFX;
    @FXML
    private Label lblFPS;
    @FXML
    private Label lblMixerSize;
    @FXML
    private AnchorPane memAnchor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timer.scheduleAtFixedRate(new ResourceMonitorThreadFX(this), 0, 1000);
        maxMem = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        final String maxMem_LABEL_FORMAT = "%.0f";
        memBar = new ProgressIndicatorBar(
                maxMem,
                maxMem_LABEL_FORMAT
        );
        ResourceMonitorFX_ = ResourceMonitorFX;
        lblFPS_ = lblFPS;
        AnchorPane.setTopAnchor(memBar, 0.0);
        AnchorPane.setBottomAnchor(memBar, 0.0);
        AnchorPane.setLeftAnchor(memBar, 0.0);
        AnchorPane.setRightAnchor(memBar, 0.0);
        memAnchor.getChildren().add(memBar);
        memBar.setVisible(true);
    }

    public static ResourceMonitorFXController getInstance() {
        if (instance == null) {
            instance = new ResourceMonitorFXController();
        }
        return instance;
    }

    public void addMessage(ResourceMonitorLabelFX label) {
        Platform.runLater(() -> {
            label.setBorder(lblFPS_.getBorder());
            ResourceMonitorFX_.getChildren().add(label);
            if (label.getEndTime() != 0) {
                System.err.println(label.getText());
            }
        });
    }

    public void removeMessage(ResourceMonitorLabelFX label) {
        Platform.runLater(() -> {
            if (ResourceMonitorFX != null) {
                ResourceMonitorFX.getChildren().remove(label);
            }
        });
    }

    public void updateInfo() {
        usedMem = ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024);
        Platform.runLater(() -> {
            if (memBar != null) {
                memBar.bar.setProgress((int) usedMem);
                memBar.text.setText(usedMem + "MB/" + maxMem + "MB");
                memBar.syncProgress();
            }
            lblFPS.setText(Math.round(MasterMixer.getInstance().getFPS()) + " fps");
            lblMixerSize.setText(MasterMixer.getInstance().getWidth() + "X" + MasterMixer.getInstance().getHeight());
            ResourceMonitorFX.getChildren().stream().filter((n) -> (n instanceof ResourceMonitorLabelFX)).map((n) -> (ResourceMonitorLabelFX) n).filter((rml) -> (rml.getEndTime() != 0 && rml.getEndTime() < System.currentTimeMillis())).forEach((rml) -> {
                removeMessage(rml);
            });
        });

    }

    class ResourceMonitorThreadFX extends TimerTask {

        ResourceMonitorFXController monitor;

        ResourceMonitorThreadFX(ResourceMonitorFXController m) {
            monitor = m;
        }

        @Override
        public void run() {
            monitor.updateInfo();
        }
    }

    public static class ProgressIndicatorBar extends StackPane {

        private double totalMem;

        final public ProgressBar bar = new ProgressBar();
        final private Text text = new Text();
        final private String labelFormatSpecifier;

        final private static int DEFAULT_LABEL_PADDING = 5;

        ProgressIndicatorBar(final double totalWork, final String labelFormatSpecifier) {
            this.totalMem = totalWork;
            this.labelFormatSpecifier = labelFormatSpecifier;

            bar.setMaxWidth(Double.MAX_VALUE); // allows the progress bar to expand to fill available horizontal space.
            bar.setMaxHeight(Double.MAX_VALUE);
            bar.setStyle("-fx-accent: orange;");
            text.setFont(javafx.scene.text.Font.font("Verdana", 18));

            getChildren().setAll(bar, text);
        }

        // synchronizes the progress
        public void syncProgress() {
            if (usedMem == 0 || totalMem == 0) {
                text.setText("");
                bar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
            } else {
                text.setText(usedMem + "MB/" + maxMem + "MB");
                bar.setProgress((int) usedMem / totalMem);
            }
            bar.setMinHeight(text.getBoundsInLocal().getHeight() + DEFAULT_LABEL_PADDING * 2);
            bar.setMinWidth(text.getBoundsInLocal().getWidth() + DEFAULT_LABEL_PADDING * 2);
        }
    }

}
