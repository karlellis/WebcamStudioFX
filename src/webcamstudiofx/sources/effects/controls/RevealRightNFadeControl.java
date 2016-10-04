/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MosaicControl.java
 *
 * Created on 2010-01-15, 01:51:51
 */

package webcamstudiofx.sources.effects.controls;

import webcamstudiofx.sources.effects.RevealRightNFade;

/**
 *
 * @author pballeux (modified by karl)
 */
public class RevealRightNFadeControl extends javax.swing.JPanel {

    RevealRightNFade effect = null;
    /** Creates new form MosaicControl
     * @param effect */
    public RevealRightNFadeControl(RevealRightNFade effect) {
        initComponents();
        this.effect=effect;
        slider.setValue(effect.getVel());
        jCheckBox1.setSelected(effect.getLoop());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label = new javax.swing.JLabel();
        slider = new javax.swing.JSlider();
        jCheckBox1 = new javax.swing.JCheckBox();

        setPreferredSize(new java.awt.Dimension(250, 44));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("truckliststudio/Languages"); // NOI18N
        label.setText(bundle.getString("VELOCITY")); // NOI18N
        label.setName("label"); // NOI18N

        slider.setMajorTickSpacing(10);
        slider.setMaximum(20);
        slider.setMinimum(1);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setName("slider"); // NOI18N
        slider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderStateChanged(evt);
            }
        });

        jCheckBox1.setText("Loop");
        jCheckBox1.setName("jCheckBox1"); // NOI18N
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(slider, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(slider, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox1)))
                .addContainerGap(80, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderStateChanged
        effect.setVel(slider.getValue());
    }//GEN-LAST:event_sliderStateChanged

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (jCheckBox1.isSelected()){
            effect.setLoop(true);
        } else {
            effect.setLoop(false);
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel label;
    private javax.swing.JSlider slider;
    // End of variables declaration//GEN-END:variables

}