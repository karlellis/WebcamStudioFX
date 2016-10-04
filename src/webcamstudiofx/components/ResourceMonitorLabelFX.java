/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.components;

import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

/**
 *
 * @author patrick
 */
public class ResourceMonitorLabelFX extends Label {
    long endTime = 0;
    public Paint labelColor = javafx.scene.paint.Color.RED;
    public ResourceMonitorLabelFX(long endTime,String text){
        this.setTextFill(labelColor);
        this.setStyle("-fx-font-weight: bold;");
        this.endTime = endTime;
        setText(text);
    }
    public long getEndTime(){
        return endTime;
    }
}
