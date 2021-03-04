package Autenticazione;

import Impostazioni.JSON;
import Impostazioni.Richieste;
import Impostazioni.Xml_pars;
import Autenticazione.Persona;
import Autenticazione.Persona;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Bernardo and Paolo
 */
public class Utente extends Persona {
    //Interfaccia
    private Richieste ric;
    private Xml_pars xmlparser;
    private JSON pj;
    private String nome_utente;
    private String password;
    
    //implementazione
    public Utente() {
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
                dati.put("tabella", "utente");
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
    
    public String registrazione(String nome, String cognome, String genere, int altezza, int peso, String nome_utente, String password, int eta) {
        String msg = "registrazione riuscita";
        if(!nome_utente.isEmpty() && !password.isEmpty() && !nome.isEmpty() && !cognome.isEmpty() && !genere.isEmpty() && altezza != 0 && peso != 0 && eta != 0) {
            if(controlloNomeUtente(nome_utente) && controlloDati(nome) && controlloDati(cognome)) {
                if(eta >= 12  && eta <= 80 && !controlloDati(""+eta)) {
                    if(password.length() > 6) {
                    Map dati = new HashMap();
                    dati.put("nome_utente", nome_utente);
                    InputStream richiesta = ric.GetRichiesta("/utente1/verificaNomeUtente", dati, null);
                    dati.clear();
                    dati = pj.LeggiJson(richiesta);
                    String result = (String) dati.get("result");
                        if(result.equals("esiste")) {
                            msg = "Nome utente gia' presente";
                        } else {
                            super.setNome(nome);
                            super.setCognome(cognome);
                            super.setGenere(genere);
                            super.setAltezza(altezza);
                            super.setPeso(peso);
                            super.setEta(eta);
                            this.nome_utente = nome_utente;
                            this.password = password;
                        }
                    } else {
                        msg = "La password deve contenere almeno 6 caratteri.";
                    }
                } else {
                    msg = "L'età dell'utente deve essere compresa fra 12 e 80";
                }
            } else {
                msg = "Non inserire caratteri speciali nei campi Nome utente, Nome e Cognome.";
            }
        } else {
            msg = "Inserisci tutti i parametri.";
        }
        
        return msg;
    }
    
    public void nuovoUtente() {
        Map dati = new HashMap();
        dati.put("nome_utente", this.nome_utente);
        dati.put("password", this.password);
        dati.put("nome", super.getNome());
        dati.put("cognome", super.getCognome());
        dati.put("genere", super.getGenere());
        dati.put("altezza", ""+super.getAltezza());
        dati.put("peso", ""+super.getPeso());
        dati.put("eta", ""+super.getEta());
        InputStream richiesta = ric.GetRichiesta("/utente1/nuovoUtente", dati, null);
    }
    
    public Map prendiDatiUtente(String nome_utente) {
        Map dati = new HashMap();
        dati.put("Nome_utente", nome_utente);
        dati.put("Tabella", "utente");
        InputStream richiesta = ric.GetRichiesta("/utente1/prendiDati", dati, null);
        dati.clear();
        dati = pj.LeggiJson(richiesta);
        return dati;
    }
    
    private boolean controlloNomeUtente(String value) {
        for(int i = 0; i < value.length(); i++) {
            if(!(Character.isAlphabetic(value.charAt(i)) || Character.isDigit(value.charAt(i)))) {
                return false;
            }
        }        
        return true;
    }
    
    private boolean controlloDati(String value) {
        for(int i = 0; i < value.length(); i++) {
            if(Character.isDigit(value.charAt(i))) {
                return false;
            }
        }        
        return true;
    }
    
    public String getNomeUtente() {
        return this.nome_utente;
    }
    
    public String getPassword() {
        return this.password;
    }
}
