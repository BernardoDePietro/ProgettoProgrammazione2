package Autenticazione;

import Autenticazione.Persona;
import Impostazioni.JSON;
import Impostazioni.Richieste;
import Impostazioni.Xml_pars;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Bernardo and Paolo
 */
public class Dottore extends Persona {
    //INTERFACCIA DELLA CLASSE
    private Richieste ric;
    private Xml_pars xmlparser;
    private JSON pj;
    
    //IMPLEMENTAZIONE DELLA CLASSE
    public Dottore() {
        xmlparser = new Xml_pars("Impostazioni.xml");
        if (!xmlparser.exists()) {
            xmlparser.ImpoDefault();
        }
        pj = new JSON();
        ric = new Richieste(xmlparser.getElement("PROTOCOL"), xmlparser.getElement("SERVER_ADDRES"), xmlparser.getElement("SERVER_PORTA"));
    }
    
    @Override
    public String login(String nome_utente, String password) {
        String msg = "Accesso effettuato correttamente.";
        if(!nome_utente.isEmpty()) {
            if(!password.isEmpty()) {
                Map dati = new HashMap();
                dati.put("nome_utente", nome_utente);
                dati.put("password", password);
                dati.put("tabella", "dottore");
                InputStream richiesta = ric.GetRichiesta("/utente1/autenticazione", dati, null);
                dati.clear();
                dati = pj.LeggiJson(richiesta);
                String result = (String) dati.get("result");
                if(!result.equals("accesso confermato")) {
                    msg = "Credenziali errate.";
                }
            } else {
                msg = "Inserire la password";
            }
        } else {
            msg = "Inserire il nome utente";
        }
        return msg;
    }
    
    public Map prendiDatiDottore(String nome_utente) {
        Map dati = new HashMap();
        dati.put("Nome_utente", nome_utente);
        dati.put("Tabella", "dottore");
        InputStream richiesta = ric.GetRichiesta("/utente1/prendiDati", dati, null);
        dati.clear();
        dati = pj.LeggiJson(richiesta);
        return dati;
    }
}
