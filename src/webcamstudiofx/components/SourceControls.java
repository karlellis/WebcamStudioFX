/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.components;

import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import webcamstudiofx.streams.SourceDesktop;
import webcamstudiofx.streams.SourceImage;
import webcamstudiofx.streams.SourceImageGif;
import webcamstudiofx.streams.SourceText;
import webcamstudiofx.streams.Stream;

/**
 *
 * @author patrick (modified by karl)
 */
public class SourceControls {

    AnchorPane SourceControlTransitions;
    AnchorPane SourceControlTracks;
    AnchorPane SourceControlTextFX;
    AnchorPane SourceControlDesktopFX;
    AnchorPane SourceControlEffectsFX;
    AnchorPane SourceControlGSEffects;

    static SourceControlTransitionsController transController;
    static Stream sourceStream;

    public ArrayList<AnchorPane> getControls(Stream source) throws IOException {
        sourceStream = source;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/webcamstudiofx/components/SourceControlTransitions.fxml"));
        SourceControlTransitions = (AnchorPane) loader.load();
        loader = new FXMLLoader(getClass().getResource("/webcamstudiofx/components/SourceControlTrack.fxml"));
        SourceControlTracks = (AnchorPane) loader.load();
        if (sourceStream instanceof SourceText) {
            loader = new FXMLLoader(getClass().getResource("/webcamstudiofx/components/SourceControlsTextFX.fxml"));
            SourceControlTextFX = (AnchorPane) loader.load();
        }
        if (sourceStream instanceof SourceDesktop) {
            loader = new FXMLLoader(getClass().getResource("/webcamstudiofx/components/SourceControlDesktopFX.fxml"));
            SourceControlDesktopFX = (AnchorPane) loader.load();
        }
        loader = new FXMLLoader(getClass().getResource("/webcamstudiofx/components/SourceControlEffectsFX.fxml"));
        SourceControlEffectsFX = (AnchorPane) loader.load();
        loader = new FXMLLoader(getClass().getResource("/webcamstudiofx/components/SourceControlGSEffects.fxml"));
        SourceControlGSEffects = (AnchorPane) loader.load();

        ArrayList<AnchorPane> comps = new ArrayList<>();
        AnchorPane c = null;
        AnchorPane d = null;
        c = SourceControlTransitions;
        comps.add(c);
        d = SourceControlTracks;
        comps.add(d);

        if (source instanceof SourceText) {
            c = SourceControlTextFX;
            comps.add(c);
            c = SourceControlEffectsFX;
            comps.add(c);
        } else if (source instanceof SourceImage) {
            c = SourceControlEffectsFX;
            comps.add(c);
        } else if (source instanceof SourceImageGif) {
            // Nothing here
        } else if (source instanceof SourceDesktop) {
            c = SourceControlEffectsFX;
            comps.add(c);
            c = SourceControlDesktopFX;
            comps.add(c);
            c = SourceControlGSEffects;
            comps.add(c);
        } else {
            c = SourceControlEffectsFX;
            comps.add(c);
            c = SourceControlGSEffects;
            comps.add(c);
        }
        return comps;
    }

    public SourceControls() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/webcamstudiofx/components/SourceControlTransitions.fxml"));
        SourceControlTransitions = (AnchorPane) loader.load();
        transController = loader.getController();
    }
}
