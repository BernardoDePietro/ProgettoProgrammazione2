package Dottore;

import Alimentazione.Alimento;
import javax.swing.JOptionPane;

/**
 *
 * @author Bernardo and Paolo
 */
public class InserisciAlimento extends javax.swing.JFrame {

    private Alimento alim;

    public InserisciAlimento() {
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
        jLabel1 = new javax.swing.JLabel();
        campoAlimento = new javax.swing.JTextField();
        campoCalorie = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        campoCarboidrati = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        campoProteine = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        campoGrassi = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        inserisciBtn = new javax.swing.JButton();

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
        titolo.setText("Inserisci alimento");
        background.add(titolo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 50));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Alimento");
        background.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, 380, -1));
        background.add(campoAlimento, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 380, 40));
        background.add(campoCalorie, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, 80, 40));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Calorie(kcal)");
        background.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 194, 80, -1));
        background.add(campoCarboidrati, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 80, 40));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Carboidrati(g)");
        background.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 194, 80, -1));
        background.add(campoProteine, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 80, 40));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Proteine(g)");
        background.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 194, 80, -1));
        background.add(campoGrassi, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 210, 80, 40));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Grassi(g)");
        background.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 194, 80, -1));

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Inserisci le proprietà dell'alimento per una porzione di un grammo");
        background.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, 380, -1));

        inserisciBtn.setText("Inserisci alimento");
        inserisciBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inserisciBtnMouseClicked(evt);
            }
        });
        background.add(inserisciBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 300, 380, 50));

        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 400));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inserisciBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inserisciBtnMouseClicked
        String alimento = campoAlimento.getText();
        String calorie = campoCalorie.getText();
        String carboidrati = campoCarboidrati.getText();
        String proteine = campoProteine.getText();
        String grassi = campoGrassi.getText();
        String msg = alim.controllaNuovoAlimento(alimento, calorie, carboidrati, proteine, grassi);
        System.out.println(msg);
        if(msg.equalsIgnoreCase("Alimento corretto")) {
            int n = JOptionPane.showConfirmDialog(null, "Confermi di voler inserire un nuovo alimento?", "Gestione alimento", JOptionPane.YES_NO_OPTION);
            if(n == JOptionPane.YES_OPTION) {
                alim.inserisciNuovoAlimento(alimento, calorie, carboidrati, proteine, grassi);
                JOptionPane.showMessageDialog(null, "Alimento inserito correttamente.", "Alimento", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_inserisciBtnMouseClicked

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
            java.util.logging.Logger.getLogger(InserisciAlimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InserisciAlimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InserisciAlimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InserisciAlimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InserisciAlimento().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel background;
    private javax.swing.JTextField campoAlimento;
    private javax.swing.JTextField campoCalorie;
    private javax.swing.JTextField campoCarboidrati;
    private javax.swing.JTextField campoGrassi;
    private javax.swing.JTextField campoProteine;
    private javax.swing.JButton inserisciBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel titolo;
    // End of variables declaration//GEN-END:variables
}
