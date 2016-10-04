/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.sources.effects.controls;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import static webcamstudiofx.sources.effects.Effect.effectFX;
import webcamstudiofx.sources.effects.Shapes;

/**
 * FXML Controller class
 *
 * @author karl
 */
public class ShapesControlFXController implements Initializable {

    Shapes effect;
    private Properties shapes = new Properties();

    @FXML
    private AnchorPane ShapesControlFX;
    @FXML
    private ComboBox cboShapes;
    @FXML
    private ToggleButton tglReverse;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        effect = (Shapes) effectFX;
        initShapes();
    }

    @FXML
    private void cboShapesAction(ActionEvent event) {
        String sh = cboShapes.getSelectionModel().getSelectedItem().toString();
        effect.setShape(sh);
    }

    @FXML
    private void tglReverseAction(ActionEvent event) {
        if (tglReverse.isSelected()) {
            effect.setReverse(true);
        } else {
            effect.setReverse(false);
        }
    }

    @SuppressWarnings("unchecked")
    private void initShapes() {
        try {
            shapes.load(getClass().getResourceAsStream("/webcamstudiofx/resources/shapes/Shapes.properties"));
            ObservableList<Object> options = FXCollections.observableArrayList();
            shapes.keySet().stream().forEach((o) -> {
                options.add(o);
            });
            cboShapes.setItems(options);
            cboShapes.getSelectionModel().select(0);
        } catch (IOException ex) {
            Logger.getLogger(ShapesControlFXController.class.getName()).log(Level.SEVERE, null, ex);
        }

        String shapeL = effect.getShape();
        cboShapes.getSelectionModel().select(shapeL);
        tglReverse.setSelected(effect.getReverse());
    }

}
