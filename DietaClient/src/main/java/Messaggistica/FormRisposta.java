package Messaggistica;

import javax.swing.JOptionPane;

/**
 *
 * @author Bernardo
 */
public class FormRisposta extends javax.swing.JFrame {

    private static String id_domanda;
    private static String domanda;
    private Chat chat;
    
    public FormRisposta(String id, String domanda) {
        initComponents();
        chat = new Chat();
        this.id_domanda = id;
        this.domanda = domanda;
        setDomanda();
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
        label1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descrizione = new javax.swing.JTextArea();
        inviaBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        domandaUtente = new javax.swing.JTextArea();
        label2 = new javax.swing.JLabel();

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
        titolo.setText("Form di risposta");
        background.add(titolo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 40));

        label1.setForeground(new java.awt.Color(255, 255, 255));
        label1.setText("Domanda dell'utente");
        background.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 410, -1));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        descrizione.setColumns(20);
        descrizione.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        descrizione.setLineWrap(true);
        descrizione.setRows(5);
        descrizione.setAutoscrolls(false);
        jScrollPane1.setViewportView(descrizione);

        background.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 410, 170));

        inviaBtn.setText("Invia risposta");
        inviaBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inviaBtnMouseClicked(evt);
            }
        });
        background.add(inviaBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 442, 410, 40));

        domandaUtente.setEditable(false);
        domandaUtente.setColumns(20);
        domandaUtente.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        domandaUtente.setLineWrap(true);
        domandaUtente.setRows(5);
        domandaUtente.setAutoscrolls(false);
        jScrollPane2.setViewportView(domandaUtente);

        background.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 410, 150));

        label2.setForeground(new java.awt.Color(255, 255, 255));
        label2.setText("Inserisci la descrizione della tua domanda (max 200 caratteri)");
        background.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 410, -1));

        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 500));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        DomandeDottore dd = new DomandeDottore();
        dd.setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    private void inviaBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inviaBtnMouseClicked
        String messaggio_descrizione = descrizione.getText();
        String msg = chat.inviaMessaggio(messaggio_descrizione, Integer.valueOf(getIdDomanda()));
        if(msg.equals("Messaggio inviato correttamente.")) {
            JOptionPane.showMessageDialog(null, msg, "Invio Messaggio", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, msg, "Invio Messaggio", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_inviaBtnMouseClicked

    public void setDomanda() {
        domandaUtente.setText(getDomanda());
    }
    
    public String getDomanda() {
        return domanda;
    }
    
    public String getIdDomanda() {
        return id_domanda;
    }
    
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
            java.util.logging.Logger.getLogger(FormRisposta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormRisposta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormRisposta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormRisposta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormRisposta(id_domanda, domanda).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel background;
    private javax.swing.JTextArea descrizione;
    private javax.swing.JTextArea domandaUtente;
    private javax.swing.JButton inviaBtn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel label1;
    private javax.swing.JLabel label2;
    private javax.swing.JLabel titolo;
    // End of variables declaration//GEN-END:variables
}
