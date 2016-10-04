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

import webcamstudiofx.sources.effects.Perspective;

/**
 *
 * @author pballeux (modified by karl)
 */
public class PerspectiveControl extends javax.swing.JPanel {

    Perspective effect = null;
    /** Creates new form MosaicControl
     * @param effect */
    public PerspectiveControl(Perspective effect) {
        initComponents();
        this.effect=effect;
        jSpinX1.setValue((int)effect.getX1());
        jSpinY1.setValue((int)effect.getY1());
        jSpinX3.setValue((int)effect.getX3());
        jSpinY3.setValue((int)effect.getY3());
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
        jSpinX3 = new javax.swing.JSpinner();
        jSpinX1 = new javax.swing.JSpinner();
        jSpinY1 = new javax.swing.JSpinner();
        jSpinY3 = new javax.swing.JSpinner();
        jSY1Top = new javax.swing.JSlider();
        jSlY3Bottom = new javax.swing.JSlider();
        jSlX1Left = new javax.swing.JSlider();
        jSlX3Right = new javax.swing.JSlider();
        jSeparator1 = new javax.swing.JSeparator();

        setMinimumSize(new java.awt.Dimension(0, 0));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(250, 119));

        label.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        label.setText("X Left ");
        label.setName("label"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel2.setText("X Right");
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel4.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel4.setText("Y Up");
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel6.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel6.setText("Y Down");
        jLabel6.setName("jLabel6"); // NOI18N

        jSpinX3.setMinimumSize(new java.awt.Dimension(0, 0));
        jSpinX3.setName("jSpinX3"); // NOI18N
        jSpinX3.setPreferredSize(new java.awt.Dimension(64, 20));
        jSpinX3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinX3StateChanged(evt);
            }
        });

        jSpinX1.setMinimumSize(new java.awt.Dimension(0, 0));
        jSpinX1.setName("jSpinX1"); // NOI18N
        jSpinX1.setPreferredSize(new java.awt.Dimension(64, 20));
        jSpinX1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinX1StateChanged(evt);
            }
        });

        jSpinY1.setName("jSpinY1"); // NOI18N
        jSpinY1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinY1StateChanged(evt);
            }
        });

        jSpinY3.setName("jSpinY3"); // NOI18N
        jSpinY3.setPreferredSize(new java.awt.Dimension(64, 20));
        jSpinY3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinY3StateChanged(evt);
            }
        });

        jSY1Top.setMajorTickSpacing(100);
        jSY1Top.setMaximum(576);
        jSY1Top.setMinimum(-576);
        jSY1Top.setMinorTickSpacing(50);
        jSY1Top.setValue(0);
        jSY1Top.setName("jSY1Top"); // NOI18N
        jSY1Top.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSY1TopStateChanged(evt);
            }
        });

        jSlY3Bottom.setMajorTickSpacing(100);
        jSlY3Bottom.setMaximum(576);
        jSlY3Bottom.setMinimum(-576);
        jSlY3Bottom.setMinorTickSpacing(50);
        jSlY3Bottom.setName("jSlY3Bottom"); // NOI18N
        jSlY3Bottom.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlY3BottomStateChanged(evt);
            }
        });

        jSlX1Left.setMajorTickSpacing(100);
        jSlX1Left.setMaximum(720);
        jSlX1Left.setMinimum(-720);
        jSlX1Left.setMinorTickSpacing(50);
        jSlX1Left.setValue(0);
        jSlX1Left.setName("jSlX1Left"); // NOI18N
        jSlX1Left.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlX1LeftStateChanged(evt);
            }
        });

        jSlX3Right.setMajorTickSpacing(100);
        jSlX3Right.setMaximum(720);
        jSlX3Right.setMinimum(-720);
        jSlX3Right.setMinorTickSpacing(50);
        jSlX3Right.setName("jSlX3Right"); // NOI18N
        jSlX3Right.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlX3RightStateChanged(evt);
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
                    .addComponent(jSlX3Right, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jSlX1Left, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinX1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinX3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSlY3Bottom, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jSY1Top, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinY1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinY3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                                .addComponent(jSpinX1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSpinY1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSY1Top, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSlX1Left, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jSpinY3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSlY3Bottom, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jSpinX3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSlX3Right, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(6, 6, 6))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jSpinX1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinX1StateChanged
        effect.setX1((Integer) jSpinX1.getValue());
    }//GEN-LAST:event_jSpinX1StateChanged

    private void jSpinY1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinY1StateChanged
        effect.setY1((Integer) jSpinY1.getValue());
    }//GEN-LAST:event_jSpinY1StateChanged

    private void jSpinX3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinX3StateChanged
        effect.setX3((Integer) jSpinX3.getValue());
    }//GEN-LAST:event_jSpinX3StateChanged

    private void jSpinY3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinY3StateChanged
        effect.setY3((Integer) jSpinY3.getValue());
    }//GEN-LAST:event_jSpinY3StateChanged

    private void jSlX1LeftStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlX1LeftStateChanged
        jSpinX1.setValue(jSlX1Left.getValue());
    }//GEN-LAST:event_jSlX1LeftStateChanged

    private void jSY1TopStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSY1TopStateChanged
        jSpinY1.setValue(jSY1Top.getValue());
    }//GEN-LAST:event_jSY1TopStateChanged

    private void jSlX3RightStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlX3RightStateChanged
        jSpinX3.setValue(jSlX3Right.getValue());
    }//GEN-LAST:event_jSlX3RightStateChanged

    private void jSlY3BottomStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlY3BottomStateChanged
        jSpinY3.setValue(jSlY3Bottom.getValue());
    }//GEN-LAST:event_jSlY3BottomStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSlider jSY1Top;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSlider jSlX1Left;
    private javax.swing.JSlider jSlX3Right;
    private javax.swing.JSlider jSlY3Bottom;
    private javax.swing.JSpinner jSpinX1;
    private javax.swing.JSpinner jSpinX3;
    private javax.swing.JSpinner jSpinY1;
    private javax.swing.JSpinner jSpinY3;
    private javax.swing.JLabel label;
    // End of variables declaration//GEN-END:variables

}
