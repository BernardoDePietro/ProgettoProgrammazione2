package Admin;

import Impostazioni.JSON;
import Impostazioni.Richieste;
import Impostazioni.Xml_pars;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Bernardo and Paolo
 */
public class Amministrazione {
    
    private Xml_pars xmlparser;
    private Xml_pars xmlparser2;
    private Richieste ric;
    private JSON pj;
    private ArrayList<String> dottore = new ArrayList(10);
    private int domande_con_risposta;
    private int risposte_dottore;
    private int domande_senza_risposta;
    private float media_valutazione;
    
    public Amministrazione() {
        xmlparser = new Xml_pars("Impostazioni.xml");
        xmlparser2 = new Xml_pars("token.save");
        if (!xmlparser.exists()) {
            xmlparser.ImpoDefault();
        }
        pj = new JSON();
        ric = new Richieste(xmlparser.getElement("PROTOCOL"), xmlparser.getElement("SERVER_ADDRES"), xmlparser.getElement("SERVER_PORTA"));
    }
    
    public String inserisciDottore(String nome, String cognome, String eta, String genere, String nome_utente, String password) {
        String msg = "Nuovo dottore inserito correttamente";
        if(password.length() >= 6) {
            if(!nome.isEmpty() && !cognome.isEmpty() && !eta.isEmpty() && !nome_utente.isEmpty()) {
                if(Integer.valueOf(eta) >= 18 && !controlloLettere(eta)) {
                    if(controlloLettere(nome) && controlloLettere(cognome)) {
                        Map dati = new HashMap();
                        dati.put("nome", nome);
                        dati.put("cognome", cognome);
                        dati.put("nome_utente", nome_utente);
                        dati.put("password", password);
                        dati.put("eta", eta);
                        dati.put("genere", genere);
                        InputStream richiesta = ric.GetRichiesta("/admin/inserisciDottore", dati, null);
                        dati.clear();
                        dati = pj.LeggiJson(richiesta);
                        msg = (String) dati.get("stato");
                    } else {
                        msg = "Nome e cognome non possono contenere numeri o caratteri speciali.";
                    }
                } else {
                    msg = "L'età non può essere minore di 18 anni.";
                }
            } else {
                msg = "Compilare tutti i campi.";
            }
        } else {
            msg = "La password deve contenere almeno 6 caratteri.";
        }
        return msg;
    }
    
    public String eliminaUtente(String nome_utente, String tipologia) {
        String msg = "Utente eliminato correttamente.";
        if(!nome_utente.isEmpty()) {
            Map dati = new HashMap();
            dati.put("nome_utente", nome_utente);
            dati.put("tipologia", tipologia);
            InputStream richiesta = ric.GetRichiesta("/admin/eliminaUtenti", dati, null);
            dati.clear();
            dati = pj.LeggiJson(richiesta);
            String stato = (String) dati.get("stato");
            if(stato.equals("niente")) {
                msg = "Il nome utente inserito non è presente nel sistema.";
            }
        } else { 
           msg = "Inserire il nome utente che si vuole eliminare dal sistema.";
        }
        return msg;
    }
    
    public void prendiDomandeRisposte(String nome_utente) {
        Map dati = new HashMap();
        dati.put("nome_utente", nome_utente);
        InputStream richiesta = ric.GetRichiesta("/admin/prendiDomandeRisposte", dati, null);
        dati.clear();
        dati = pj.LeggiJson(richiesta);
        risposte_dottore = Integer.valueOf(""+dati.get("risposte_dottore"));
        domande_con_risposta = Integer.valueOf(""+dati.get("domande_con_risposta"));
        domande_senza_risposta = Integer.valueOf(""+dati.get("domande_senza_risposta"));
        media_valutazione = Float.valueOf(""+dati.get("media_valutazione"));
    }
    
    public void prendiDottori() {
        Map dati = new HashMap();
        InputStream richiesta = ric.GetRichiesta("/admin/prendiDottori", dati, null);
        dati = pj.LeggiJson(richiesta);
        String stato = (String) dati.get("stato");
        if(stato.equals("Dottori trovati")) {
            dati.remove("stato");
            for(int i = 0; i < dati.size(); i++) {
                dottore.add((String) dati.get("dottore"+i));
            }
        } else {
            dottore.add("Nessun dottore trovato");
        }
    }
    
    public ArrayList<String> getDottori() {
        return dottore;
    }
    
    private boolean controlloLettere(String value) {
        for(int i = 0; i < value.length(); i++) {
            if(!Character.isAlphabetic(value.charAt(i))) {
                if(!Character.isSpaceChar(value.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getRisposteDottore() {
        return risposte_dottore;
    }
    
    public int getDomandeConRisposta() {
        return domande_con_risposta;
    }
    
    public int getDomandeSenzaRisposta() {
        return domande_senza_risposta;
    }
    
    public float getMediaValutazione() {
        return media_valutazione;
    }
    
    public int getDomandeTotali() {
        return domande_senza_risposta + domande_con_risposta;
    }
    
    public int getPercentualeDottore() {
        return risposte_dottore / getDomandeTotali();
    }
}
