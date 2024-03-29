package Dottore;

import Alimentazione.Alimento;
import javax.swing.JOptionPane;

/**
 *
 * @author Bernardo and Paolo
 */
public class EliminaAlimento extends javax.swing.JFrame {

    Alimento alim;
    
    public EliminaAlimento() {
        initComponents();
        alim = new Alimento();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        titolo = new javax.swing.JLabel();
        campoAlimento = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cerca = new javax.swing.JButton();
        resultPanel = new javax.swing.JPanel();
        alimentiResult = new javax.swing.JComboBox<>();
        labelRicerca1 = new javax.swing.JLabel();
        eliminaBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        background.setBackground(new java.awt.Color(231, 76, 60));
        background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        titolo.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        titolo.setForeground(new java.awt.Color(255, 255, 255));
        titolo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titolo.setText("Elimina alimento");
        background.add(titolo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 40));
        background.add(campoAlimento, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 360, 40));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Alimento");
        background.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 460, -1));

        cerca.setText("Cerca");
        cerca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cercaMouseClicked(evt);
            }
        });
        background.add(cerca, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 70, 90, 40));

        resultPanel.setOpaque(false);
        resultPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        resultPanel.add(alimentiResult, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 460, 40));

        labelRicerca1.setForeground(new java.awt.Color(255, 255, 255));
        labelRicerca1.setText("Ricerca per: \"alimento\"");
        resultPanel.add(labelRicerca1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 360, -1));

        eliminaBtn.setText("Elimina alimento");
        eliminaBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eliminaBtnMouseClicked(evt);
            }
        });
        resultPanel.add(eliminaBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 460, 40));

        background.add(resultPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 460, 150));

        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 300));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cercaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cercaMouseClicked
        String msg = "Alimenti trovati";
        msg = alim.getListaAlimenti(campoAlimento.getText());
        if(!msg.equals("Alimenti trovati")) {
            JOptionPane.showMessageDialog(null, "Nessun alimento trovato", "Ricerca", JOptionPane.INFORMATION_MESSAGE);
        } else {
            labelRicerca1.setText("Ricerca per: \'" + campoAlimento.getText() + "\'");
            resultPanel.setVisible(true);
            alimentiResult.removeAllItems();
            for(int i = 0; i < alim.lenElementi(); i++) {
                alimentiResult.addItem(alim.getAlimento(i));
            }
        }
    }//GEN-LAST:event_cercaMouseClicked

    private void eliminaBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminaBtnMouseClicked
        int n = JOptionPane.showConfirmDialog(null, "Confermi di voler eliminare l'alimento?", "Elimina alimento", JOptionPane.YES_NO_CANCEL_OPTION);
        if(n == JOptionPane.YES_OPTION) {
            alim.eliminaAlimento((String) alimentiResult.getSelectedItem());
            JOptionPane.showMessageDialog(null, "Alimento eliminato correttamente", "Gestione alimento", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_eliminaBtnMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        GestioneAlimenti ga = new GestioneAlimenti();
        ga.setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EliminaAlimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EliminaAlimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EliminaAlimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EliminaAlimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EliminaAlimento().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> alimentiResult;
    private javax.swing.JPanel background;
    private javax.swing.JTextField campoAlimento;
    private javax.swing.JButton cerca;
    private javax.swing.JButton eliminaBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel labelRicerca1;
    private javax.swing.JPanel resultPanel;
    private javax.swing.JLabel titolo;
    // End of variables declaration//GEN-END:variables
}
