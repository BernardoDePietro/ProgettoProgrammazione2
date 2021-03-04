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
public class Admin extends Persona {
    //INTERFACCIA DELLA CLASSE
    private Richieste ric;
    private Xml_pars xmlparser;
    private JSON pj;
    
    public Admin() {
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
                if(!nome_utente.equals("Admin")) {
                    msg = "Nome utente errato.";
                    if(!password.equals("Admin")) {
                        msg = "La password inserita è errata.";
                    }
                }
            } else {
                msg = "Inserire la password";
            }
        } else {
            msg = "Inserire il nome utente";
        }
        return msg;
    }
}
