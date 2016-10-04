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

import webcamstudiofx.mixers.MasterMixer;
import webcamstudiofx.sources.effects.Stretch;

/**
 *
 * @author pballeux (modified by karl)
 */
public class StretchControl extends javax.swing.JPanel {

    Stretch effect = null;
    /** Creates new form MosaicControl
     * @param effect */
    public StretchControl(Stretch effect) {
        initComponents();
        this.effect=effect;
        jSpinX.setValue((int)effect.getX());
        jSpinY.setValue((int)effect.getY());
        jSpinWidth.setValue((int)effect.getWidth());
        jSpinHeight.setValue((int)effect.getHeight());
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
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jSpinWidth = new javax.swing.JSpinner();
        jSpinX = new javax.swing.JSpinner();
        jSpinY = new javax.swing.JSpinner();
        jSpinHeight = new javax.swing.JSpinner();
        jslY = new javax.swing.JSlider();
        jslHeight = new javax.swing.JSlider();
        jslX = new javax.swing.JSlider();
        jslWidth = new javax.swing.JSlider();
        jSeparator1 = new javax.swing.JSeparator();

        setMinimumSize(new java.awt.Dimension(0, 0));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(250, 119));

        label.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        label.setText("X ");
        label.setName("label"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel2.setText("Width");
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel4.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel4.setText("Y");
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel6.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel6.setText("Height");
        jLabel6.setName("jLabel6"); // NOI18N

        jSpinWidth.setMinimumSize(new java.awt.Dimension(0, 0));
        jSpinWidth.setName("jSpinWidth"); // NOI18N
        jSpinWidth.setPreferredSize(new java.awt.Dimension(64, 20));
        jSpinWidth.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinWidthStateChanged(evt);
            }
        });

        jSpinX.setMinimumSize(new java.awt.Dimension(0, 0));
        jSpinX.setName("jSpinX"); // NOI18N
        jSpinX.setPreferredSize(new java.awt.Dimension(64, 20));
        jSpinX.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinXStateChanged(evt);
            }
        });

        jSpinY.setName("jSpinY"); // NOI18N
        jSpinY.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinYStateChanged(evt);
            }
        });

        jSpinHeight.setName("jSpinHeight"); // NOI18N
        jSpinHeight.setPreferredSize(new java.awt.Dimension(64, 20));
        jSpinHeight.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinHeightStateChanged(evt);
            }
        });

        jslY.setMaximum(MasterMixer.getInstance().getHeight());
        jslY.setValue(0);
        jslY.setName("jslY"); // NOI18N
        jslY.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jslYStateChanged(evt);
            }
        });

        jslHeight.setMaximum(MasterMixer.getInstance().getHeight());
        jslHeight.setMinimum(1);
        jslHeight.setValue(MasterMixer.getInstance().getHeight());
        jslHeight.setName("jslHeight"); // NOI18N
        jslHeight.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jslHeightStateChanged(evt);
            }
        });

        jslX.setMaximum(MasterMixer.getInstance().getWidth());
        jslX.setValue(0);
        jslX.setName("jslX"); // NOI18N
        jslX.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jslXStateChanged(evt);
            }
        });

        jslWidth.setMaximum(MasterMixer.getInstance().getWidth());
        jslWidth.setMinimum(1);
        jslWidth.setValue(MasterMixer.getInstance().getWidth());
        jslWidth.setName("jslWidth"); // NOI18N
        jslWidth.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jslWidthStateChanged(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setName("jSeparator1"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jslWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jslX, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jslHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jslY, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinY, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(jSpinX, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSpinY, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jslY, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jslX, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jSpinHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jslHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jSpinWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jslWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(6, 6, 6))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jSpinXStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinXStateChanged
        int x = (Integer) jSpinX.getValue();
        jslX.setValue(x);
        effect.setX(x);
    }//GEN-LAST:event_jSpinXStateChanged

    private void jSpinYStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinYStateChanged
        int y = (Integer) jSpinY.getValue();
        jslY.setValue(y);
        effect.setY(y);
    }//GEN-LAST:event_jSpinYStateChanged

    private void jSpinWidthStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinWidthStateChanged
        int w = (Integer) jSpinWidth.getValue();
        jslWidth.setValue(w);
        effect.setWidth(w);
    }//GEN-LAST:event_jSpinWidthStateChanged

    private void jSpinHeightStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinHeightStateChanged
        int h = (Integer) jSpinHeight.getValue();
        jslHeight.setValue(h);
        effect.setHeight(h);
    }//GEN-LAST:event_jSpinHeightStateChanged

    private void jslXStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jslXStateChanged
        int x = (Integer) jslX.getValue();
        jSpinX.setValue(x);
    }//GEN-LAST:event_jslXStateChanged

    private void jslYStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jslYStateChanged
        int y = (Integer) jslY.getValue();
        jSpinY.setValue(y);
    }//GEN-LAST:event_jslYStateChanged

    private void jslWidthStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jslWidthStateChanged
        int w = (Integer) jslWidth.getValue();
        jSpinWidth.setValue(w);
    }//GEN-LAST:event_jslWidthStateChanged

    private void jslHeightStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jslHeightStateChanged
        int h = (Integer) jslHeight.getValue();
        jSpinHeight.setValue(h);
    }//GEN-LAST:event_jslHeightStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner jSpinHeight;
    private javax.swing.JSpinner jSpinWidth;
    private javax.swing.JSpinner jSpinX;
    private javax.swing.JSpinner jSpinY;
    private javax.swing.JSlider jslHeight;
    private javax.swing.JSlider jslWidth;
    private javax.swing.JSlider jslX;
    private javax.swing.JSlider jslY;
    private javax.swing.JLabel label;
    // End of variables declaration//GEN-END:variables

}
