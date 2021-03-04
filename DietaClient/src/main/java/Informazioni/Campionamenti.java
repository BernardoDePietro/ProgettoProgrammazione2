package Informazioni;

import Impostazioni.Calendario;
import Impostazioni.JSON;
import Impostazioni.Richieste;
import Impostazioni.Xml_pars;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Bernardo and Paolo
 * 
 */
public class Campionamenti {
    
    private Xml_pars xmlparser;
    private Xml_pars xmlparser2;
    private Richieste ric;
    private JSON pj;
    private double[] carboidrati;
    private double[] proteine;
    private double[] grassi;
    private double[] peso;
    private int sizeMacronutrienti;
    private int sizePeso;
    
    public Campionamenti() {
        xmlparser = new Xml_pars("Impostazioni.xml");
        xmlparser2 = new Xml_pars("token.save");
        if (!xmlparser.exists()) {
            xmlparser.ImpoDefault();
        }
        pj = new JSON();
        ric = new Richieste(xmlparser.getElement("PROTOCOL"), xmlparser.getElement("SERVER_ADDRES"), xmlparser.getElement("SERVER_PORTA"));
    }
    
    public String prendiCampionamenti() {
        String msg = "Campionamenti esistenti";
        Map dati = new HashMap();
        dati.put("nome_utente", xmlparser2.getElement("Nome_utente"));
        InputStream richiesta = ric.GetRichiesta("/statistiche/prendiCampionamenti", dati, null);
        dati.clear();
        dati = pj.LeggiJson(richiesta);
        String stato = (String) dati.get("stato");
        if(stato.equals("Campionamenti non trovati")) {
            msg = "Campionamenti macronutrienti non esistenti";
        } else {
            dati.remove("stato");
            sizeMacronutrienti = dati.size() / 3;
            carboidrati = new double[sizeMacronutrienti];
            proteine = new double[sizeMacronutrienti];
            grassi = new double[sizeMacronutrienti];
            for(int i = 0; i < sizeMacronutrienti; i++) {
                carboidrati[i] = Double.valueOf(""+dati.get("carboidrati"+i));
                proteine[i] = Double.valueOf(""+dati.get("proteine"+i));
                grassi[i] = Double.valueOf(""+dati.get("grassi"+i));
            }
        }
        return msg;
    }
    
    public String prendiCampionamentiPeso() {
        String msg = "Campionamenti esistenti";
        Map dati = new HashMap();
        dati.put("nome_utente", xmlparser2.getElement("Nome_utente"));
        InputStream richiesta = ric.GetRichiesta("/statistiche/prendiCampionamentiPeso", dati, null);
        dati.clear();
        dati = pj.LeggiJson(richiesta);
        String stato = (String) dati.get("stato");
        if(stato.equals("Campionamenti non trovati")) {
            msg = "Campionamenti peso non esistenti";
        } else {
            dati.remove("stato");
            sizePeso = dati.size();
            peso = new double[sizePeso];
            for(int i = 0; i < dati.size(); i++) {
                peso[i] = Double.valueOf(""+dati.get("peso"+i));
            }
        }
        return msg;
    }
    
    public String inserisciNuovoPeso(String peso) {
        String msg = "Peso inserito correttamente";
        Calendario calendario = new Calendario();
        String data = calendario.getGiorno();
        Map dati = new HashMap();
        dati.put("nome_utente", xmlparser2.getElement("Nome_utente"));
        dati.put("peso", peso);
        dati.put("data", data);
        InputStream richiesta = ric.GetRichiesta("/utente1/inserisciNuovoPeso", dati, null);
        xmlparser2.ChangeSetting("Peso", peso);
        return msg;
    }
    
    public double[] getCarboidrati() {
        return carboidrati;
    }
    
    public double[] getProteine() {
        return proteine;
    }
    
    public double[] getGrassi() {
        return grassi;
    }
    
    public double[] getPeso() {
        return peso;
    }
    
    public double getPeso(int i) {
        return peso[i];
    }
    
    public float getHeight() {
        return Float.valueOf(xmlparser2.getElement("Altezza"));
    }
    
    public int getSizeMacronutrienti() {
        return sizeMacronutrienti;
    }
    
    public int getSizePeso() {
        return sizePeso;
    }
}
