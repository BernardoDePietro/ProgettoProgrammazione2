package Utente;

import Alimentazione.Obiettivi1;
import Messaggistica.Consulta;
import Informazioni.StatisticheUtente;
import Alimentazione.Obiettivi2;
import Alimentazione.Proprieta;
import Autenticazione.Login;
import Impostazioni.Calendario;
import Impostazioni.JSON;
import Impostazioni.Richieste;
import Impostazioni.Xml_pars;
import Informazioni.GestioneUtente;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Bernardo and Paolo
 */
public class Dashboard extends javax.swing.JFrame {

    private Xml_pars xmlparser;
    private Xml_pars xmlparser2;
    private Richieste ric;
    private JSON pj;
    private Calendario calendario;
    private Proprieta attivita;
    private String Login;
    /**
     * Creates new form Dashboard
     */
    public Dashboard() {
        initComponents();
        xmlparser = new Xml_pars("Impostazioni.xml");
        xmlparser2 = new Xml_pars("token.save");
        if (!xmlparser.exists()) {
            xmlparser.ImpoDefault();
        }
        pj = new JSON();
        ric = new Richieste(xmlparser.getElement("PROTOCOL"), xmlparser.getElement("SERVER_ADDRES"), xmlparser.getElement("SERVER_PORTA"));
        calendario = new Calendario();
        attivita = new Proprieta();
        setObiettivo();
    }
 public Dashboard(String login) {
        initComponents();
        xmlparser = new Xml_pars("Impostazioni.xml");
        
        if (!xmlparser.exists()) {
            xmlparser.ImpoDefault();
        }
        pj = new JSON();
        ric = new Richieste(xmlparser.getElement("PROTOCOL"), xmlparser.getElement("SERVER_ADDRES"), xmlparser.getElement("SERVER_PORTA"));
        calendario = new Calendario();
        attivita = new Proprieta();
        this.Login=login;
        setObiettivo(Login);
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
        obiettivi = new javax.swing.JButton();
        statistiche = new javax.swing.JButton();
        resoconto = new javax.swing.JButton();
        aiuto1 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        gestioneBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        background.setBackground(new java.awt.Color(231, 76, 60));
        background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        titolo.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        titolo.setForeground(new java.awt.Color(255, 255, 255));
        titolo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titolo.setText("Dashboard");
        background.add(titolo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 60));

        obiettivi.setText("Obiettivi");
        obiettivi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                obiettiviMouseClicked(evt);
            }
        });
        background.add(obiettivi, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 420, 60));

        statistiche.setText("Statistiche");
        statistiche.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                statisticheMouseClicked(evt);
            }
        });
        statistiche.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statisticheActionPerformed(evt);
            }
        });
        background.add(statistiche, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 420, 60));

        resoconto.setText("Resoconto");
        resoconto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resocontoMouseClicked(evt);
            }
        });
        background.add(resoconto, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, 420, 60));

        aiuto1.setText("Consulta dottore");
        aiuto1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                aiuto1MouseClicked(evt);
            }
        });
        background.add(aiuto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 420, 60));

        jButton1.setText("Logout");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        background.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 90, -1));

        gestioneBtn.setText("Gestione utente");
        gestioneBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gestioneBtnMouseClicked(evt);
            }
        });
        background.add(gestioneBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 380, 420, 60));

        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 500));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void obiettiviMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_obiettiviMouseClicked
        String msg = attivita.getObiettiviMacronutrienti();
        esisteObiettivo(msg);
        dispose();
    }//GEN-LAST:event_obiettiviMouseClicked

    private void aiuto1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aiuto1MouseClicked
        Consulta consulta = new Consulta();
        consulta.setVisible(true);
        dispose();
    }//GEN-LAST:event_aiuto1MouseClicked

    private void statisticheMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statisticheMouseClicked
        StatisticheUtente su = new StatisticheUtente();
        su.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_statisticheMouseClicked

    private void resocontoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resocontoMouseClicked
        ResocontoUtente ru = new ResocontoUtente();
        ru.setVisible(true);
        dispose();
    }//GEN-LAST:event_resocontoMouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        Login login = new Login();
        login.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton1MouseClicked

    private void gestioneBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gestioneBtnMouseClicked
        GestioneUtente gu = new GestioneUtente();
        gu.setVisible(true);
        dispose();
    }//GEN-LAST:event_gestioneBtnMouseClicked

    private void statisticheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statisticheActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statisticheActionPerformed

    public void setObiettivo() {
        String msg = "Obiettivo reimpostato";
        Map dati = new HashMap();
        dati.put("nome_utente", xmlparser2.getElement("Nome_utente"));
        dati.put("data", calendario.getGiorno());
        InputStream richiesta = ric.GetRichiesta("/utente1/reimpostaObiettivo", dati, null);
    }
      public void setObiettivo(String login) {
        String msg = "Obiettivo reimpostato";
        Map dati = new HashMap();
        dati.put("nome_utente", login);
        dati.put("data", calendario.getGiorno());
        InputStream richiesta = ric.GetRichiesta("/utente1/reimpostaObiettivo", dati, null);
    }
    
    public void esisteObiettivo(String msg) {
        if(!msg.equals("L'obiettivo esiste")) {
            int n = JOptionPane.showConfirmDialog(null, msg + ".\nVuoi impostare un nuovo obiettivo adesso?", "Obiettivo", JOptionPane.YES_NO_OPTION);
            if(n == JOptionPane.YES_OPTION) {
            Obiettivi1 obiettivi1 = new Obiettivi1();
            obiettivi1.setVisible(true);
            } else {
                Dashboard dash = new Dashboard();
                dash.setVisible(true);
            }
        } else {
            Obiettivi2 obiettivi2 = new Obiettivi2();
            obiettivi2.setVisible(true);
        }
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
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aiuto1;
    private javax.swing.JPanel background;
    private javax.swing.JButton gestioneBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton obiettivi;
    private javax.swing.JButton resoconto;
    private javax.swing.JButton statistiche;
    private javax.swing.JLabel titolo;
    // End of variables declaration//GEN-END:variables
}
