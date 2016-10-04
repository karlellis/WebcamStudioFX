/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamstudiofx.components;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
//import truckliststudio.TrucklistStudio_old;
import webcamstudiofx.streams.Stream;
import webcamstudiofx.util.Tools;

/**
 *
 * @author Patrick Balleux
 */
public class SourceControlGSEffects extends javax.swing.JPanel {

//    private DefaultListModel listModel = new DefaultListModel();
    private Stream stream;
    public static Properties gsEffects = new Properties();
    /**
     * Creates new form SourceControlEffects
     * @param s
     */
    @SuppressWarnings("unchecked") 
    public SourceControlGSEffects(Stream s) {
        initComponents();
        stream = s;
        if ("GS".equals(s.getComm())){
            cboGSEffects.setEnabled(true);
            btnSetGSEffect.setEnabled(true);
        } else {
            cboGSEffects.setEnabled(false);
            btnSetGSEffect.setEnabled(false);

        }
        try {
            gsEffects.load(getClass().getResourceAsStream("/webcamstudiofx/externals/linux/gseffects.properties"));
            DefaultComboBoxModel model = new DefaultComboBoxModel();
            gsEffects.keySet().stream().forEach((o) -> {
                model.addElement(o);
            });
            cboGSEffects.setModel(model);
        } catch (IOException ex) {
            Logger.getLogger(SourceControlGSEffects.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!"".equals(s.getGSEffect())){
            btnSetGSEffect.setEnabled(false);
            btnUnsetGSEffect.setEnabled(true);
            lblGSEffect.setText(s.getGSEffect());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cboGSEffects = new javax.swing.JComboBox();
        btnSetGSEffect = new javax.swing.JButton();
        btnUnsetGSEffect = new javax.swing.JButton();
        lblGSEffect = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(300, 50));
        setPreferredSize(new java.awt.Dimension(395, 161));

        cboGSEffects.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnSetGSEffect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/truckliststudio/resources/tango/list-add.png"))); // NOI18N
        btnSetGSEffect.setAlignmentY(0.0F);
        btnSetGSEffect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetGSEffectActionPerformed(evt);
            }
        });

        btnUnsetGSEffect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/truckliststudio/resources/tango/list-remove.png"))); // NOI18N
        btnUnsetGSEffect.setEnabled(false);
        btnUnsetGSEffect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnsetGSEffectActionPerformed(evt);
            }
        });

        lblGSEffect.setFont(new java.awt.Font("Ubuntu", 2, 15)); // NOI18N
        lblGSEffect.setForeground(new java.awt.Color(1, 123, 122));
        lblGSEffect.setText("None");

        jLabel1.setText("Current loaded Effect:");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/truckliststudio/resources/GStreamerLogo.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboGSEffects, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblGSEffect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 175, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSetGSEffect)
                            .addComponent(btnUnsetGSEffect)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboGSEffects, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSetGSEffect, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnUnsetGSEffect, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGSEffect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    @SuppressWarnings("unchecked") 
    private void btnSetGSEffectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetGSEffectActionPerformed
        lblGSEffect.setText(cboGSEffects.getSelectedItem().toString());
        stream.setGSEffect(cboGSEffects.getSelectedItem().toString().toLowerCase());
        if (stream.isPlaying()){
            stream.stop();
            Tools.sleep(100);
            stream.read();
        }
        btnSetGSEffect.setEnabled(false);
        btnUnsetGSEffect.setEnabled(true);
    }//GEN-LAST:event_btnSetGSEffectActionPerformed

    private void btnUnsetGSEffectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnsetGSEffectActionPerformed
        if (!"".equals(lblGSEffect.getText())){
            stream.setGSEffect("");
            if (stream.isPlaying()){
                stream.stop();
                Tools.sleep(100);
                stream.read();
            }
            lblGSEffect.setText("None");
            btnSetGSEffect.setEnabled(true);
            btnUnsetGSEffect.setEnabled(false);
        }
    }//GEN-LAST:event_btnUnsetGSEffectActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSetGSEffect;
    private javax.swing.JButton btnUnsetGSEffect;
    private javax.swing.JComboBox cboGSEffects;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblGSEffect;
    // End of variables declaration//GEN-END:variables
}
