/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.tracks.transitions;

import webcamstudiofx.streams.Stream;
import webcamstudiofx.util.Tools;

/**
 *
 * @author patrick
 */
public class FadeIn extends Transition {

    protected FadeIn(Stream source) {
        super(source);
    }

    @Override
    protected void execute() {
        int rate = source.getRate();
        for (int i = 0; i <= rate; i++) {
            source.setOpacity(i * 100 / rate);
            Tools.sleep(1000 / rate);
        }
    }

}
