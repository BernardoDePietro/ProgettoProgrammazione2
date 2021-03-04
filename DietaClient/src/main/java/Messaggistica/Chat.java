package Messaggistica;


import Impostazioni.Calendario;
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
public class Chat {
    
    private Xml_pars xmlparser;
    private Xml_pars xmlparser2;
    private Richieste ric;
    private JSON pj;
    private Calendario calendario;
    private ArrayList<String> messaggi = new ArrayList<String>(10);
    private ArrayList<String> risposte = new ArrayList<String>(10);
    
    public Chat() {
        xmlparser = new Xml_pars("Impostazioni.xml");
        xmlparser2 = new Xml_pars("token.save");
        if (!xmlparser.exists()) {
            xmlparser.ImpoDefault();
        }
        pj = new JSON();
        ric = new Richieste(xmlparser.getElement("PROTOCOL"), xmlparser.getElement("SERVER_ADDRES"), xmlparser.getElement("SERVER_PORTA"));
        calendario = new Calendario();
    }
    
    public String inviaMessaggio(String messaggio, String categoria) {
        String msg = "Messaggio inviato correttamente.";
        if(messaggio.length() > 200) {
            msg = "Messaggio non inviato.\nIl messaggio contiene più di 200 caratteri.";
        } else {
            Map dati = new HashMap();
            dati.put("username", xmlparser2.getElement("Nome_utente"));
            dati.put("messaggio", messaggio);
            dati.put("user", "utente");
            dati.put("categoria", categoria);
            InputStream richiesta = ric.GetRichiesta("/chat/inviaMessaggio", dati, null);
        }
        return msg;
    }
    
    public String inviaMessaggio(String messaggio, int id) {
        String msg = "Messaggio inviato correttamente.";
        if(messaggio.length() > 200) {
            msg = "Messaggio non inviato.\nIl messaggio contiene più di 200 caratteri.";
        } else {
            Map dati = new HashMap();
            dati.put("username", xmlparser2.getElement("Nome_utente"));
            dati.put("messaggio", messaggio);
            dati.put("id", ""+id);
            dati.put("user", "Dottore");
            InputStream richiesta = ric.GetRichiesta("/chat/inviaMessaggio", dati, null);
        }
        return msg;
    }
    
    public String leggiMessaggi() {
        String msg = "Messaggi presenti";
        InputStream richiesta = ric.GetRichiesta("/dottore/leggiMessaggi", null, null);
        Map dati = pj.LeggiJson(richiesta);
        String stato = (String) dati.get("stato");
        if(stato.equals("Messaggi trovati")) {
            dati.remove("stato");
            for(int i = 0; i < dati.size() / 4; i++) {
                messaggi.add((String) dati.get("id"+i));
                messaggi.add((String) dati.get("utente"+i));
                messaggi.add((String) dati.get("categoria"+i));
                messaggi.add((String) dati.get("domanda"+i));
            }
        }
        return msg;
    }
    
    public String visualizzaRisposte() {
        String msg = "Messaggi presenti";
        Map dati = new HashMap();
        dati.put("nome_utente", xmlparser2.getElement("Nome_utente"));
        InputStream richiesta = ric.GetRichiesta("/utente1/visualizzaRisposte", dati, null);
        dati.clear();
        dati = pj.LeggiJson(richiesta);
        String stato = (String) dati.get("stato");
        if(stato.equals("Risposte trovate")) {
            dati.remove("stato");
            for(int i = 0; i < dati.size() / 4; i++) {
                risposte.add((String) dati.get("id"+i));
                risposte.add((String) dati.get("dottore"+i));
                risposte.add((String) dati.get("domanda"+i));
                risposte.add((String) dati.get("risposta"+i));
            }
        } else {
            msg = "Nessuna risposta trovata";
        }
        return msg;
    }
    
    public void inviaValutazione(String id, String valutazione) {
        Map dati = new HashMap();
        dati.put("id", id);
        dati.put("valutazione", valutazione);
        InputStream richiesta = ric.GetRichiesta("/utente1/inviaValutazione", dati, null);
    }
    
    public ArrayList<String> getMessaggi() {
        return messaggi;
    }

    public ArrayList<String> getRisposte() {
        return risposte;
    } 
}
