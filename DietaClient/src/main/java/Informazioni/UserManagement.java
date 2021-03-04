package Informazioni;

import Impostazioni.JSON;
import Impostazioni.Richieste;
import Impostazioni.Xml_pars;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Bernardo
 */
public class UserManagement {
    private String nome_utente;
    private String nome;
    private String cognome;
    private String eta;
    private String genere;
    private Xml_pars xmlparser;
    private Xml_pars xmlparser2;
    private Richieste ric;
    private JSON pj;
    
    public UserManagement() {
        xmlparser = new Xml_pars("Impostazioni.xml");
        xmlparser2 = new Xml_pars("token.save");
        if (!xmlparser.exists()) {
            xmlparser.ImpoDefault();
        }
        pj = new JSON();
        ric = new Richieste(xmlparser.getElement("PROTOCOL"), xmlparser.getElement("SERVER_ADDRES"), xmlparser.getElement("SERVER_PORTA"));
    }
    
    public void setInformazioni() {
        nome_utente = xmlparser2.getElement("Nome_utente");
        nome = xmlparser2.getElement("Nome");
        cognome = xmlparser2.getElement("Cognome");
        eta = xmlparser2.getElement("Eta");
        genere = xmlparser2.getElement("Genere");
    }
    
    public String cambioPassword(String nuovapassword, String vecchiapassword) {
        String msg = "Cambio password effettuato correttamente. Al prossimo accesso inserisci la nuova password.";
        if(nuovapassword.equals(vecchiapassword)) {
            msg = "La nuova password deve essere diversa dalla vecchia. Inserisci un'altra password.";
        } else if(nuovapassword.length() < 6) { 
            msg = "La password deve essere lunga almeno 6 caratteri.";
        } else {
            Map dati = new HashMap();
            dati.put("nome_utente", xmlparser2.getElement("Nome_utente"));
            dati.put("vecchiapassword", vecchiapassword);
            dati.put("nuova", nuovapassword);
            dati.put("tipologia", xmlparser2.getElement("Tipologia"));
            InputStream richiesta = ric.GetRichiesta("/utente1/cambioPassword", dati, null);
            dati.clear();
            dati = pj.LeggiJson(richiesta);
            String stato = (String) dati.get("stato");
            if(stato.equalsIgnoreCase("Password non aggiornata") || stato.equalsIgnoreCase("Password attuale errata")) {
                msg = "La password non è stata aggiornata correttamente";
            }
        }  
        return msg;
    }
    
    public String cambioEta(String eta) {
        String msg = "Cambio età effettuato correttamente.";
        Map dati = new HashMap();
        dati.put("nome_utente", xmlparser2.getElement("Nome_utente"));
        dati.put("eta", eta);
        dati.put("tipologia", xmlparser2.getElement("Tipologia"));
        InputStream richiesta = ric.GetRichiesta("/utente1/cambioEta", dati, null);
        dati.clear();
        dati = pj.LeggiJson(richiesta);
        String stato = (String) dati.get("stato");
        msg = stato;
        if(stato.equalsIgnoreCase("Età aggiornata correttamente")) {
            xmlparser2.ChangeSetting("Eta", eta);
        }
        return msg;
    }
    
    public String getNomeUtente() {
        return nome_utente;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getCognome() {
        return cognome;
    }
    
    public String getEta() {
        return eta;
    }
    
    public String getGenere() {
        return genere;
    }
}
