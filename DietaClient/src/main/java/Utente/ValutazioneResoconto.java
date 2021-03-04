package Utente;

import Impostazioni.Calendario;
import Impostazioni.JSON;
import Impostazioni.Richieste;
import Impostazioni.Xml_pars;
import java.awt.Color;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Bernardo and Paolo
 */
public class ValutazioneResoconto {
    //INTERFACCIA
    private Xml_pars xmlparser;
    private Xml_pars xmlparser2;
    private Richieste ric;
    private JSON pj;
    private Calendario calendario;
    private int calorie_totali;
    private int carboidrati_totali;
    private int carboidrati_assunti;
    private int proteine_totali;
    private int proteine_assunte;
    private int grassi_totali;
    private int grassi_assunti;
    private int colazione_totale;
    private int colazione_assunta;
    private int spuntino_totale;
    private int spuntino_assunto;
    private int pranzo_totale;
    private int pranzo_assunto;
    private int cena_totale;
    private int cena_assunta;
    
    //IMPLEMENTAZIONE
    public ValutazioneResoconto() {
        xmlparser = new Xml_pars("Impostazioni.xml");
        xmlparser2 = new Xml_pars("token.save");
        if (!xmlparser.exists()) {
            xmlparser.ImpoDefault();
        }
        pj = new JSON();
        ric = new Richieste(xmlparser.getElement("PROTOCOL"), xmlparser.getElement("SERVER_ADDRES"), xmlparser.getElement("SERVER_PORTA"));
        calendario = new Calendario();
    }
    
    public String setProprietaMacronutrienti(String data) {
        String msg = "Proprieta trovate correttamente";
        Map dati = new HashMap();
        dati.put("nome_utente", xmlparser2.getElement("Nome_utente"));
        dati.put("data", data);
        InputStream richiesta = ric.GetRichiesta("/utente1/prendiResoconto", dati, null);
        dati.clear();
        dati = pj.LeggiJson(richiesta);
        String stato = (String) dati.get("stato");
        if(!stato.equals("obiettivi trovati")) {
            msg = "Resoconto non disponibile per questa data.";
        } else {
            calorie_totali = Integer.valueOf(""+dati.get("calorie_totali"));
            carboidrati_totali = Integer.valueOf(""+dati.get("carboidrati_totali"));
            carboidrati_assunti = Integer.valueOf(""+dati.get("carboidrati_assunti"));
            proteine_totali = Integer.valueOf(""+dati.get("proteine_totali"));
            proteine_assunte = Integer.valueOf(""+dati.get("proteine_assunte"));
            grassi_totali = Integer.valueOf(""+dati.get("grassi_totali"));
            grassi_assunti = Integer.valueOf(""+dati.get("grassi_assunti"));
        }
        return msg;
    }
    
    public String setProprietaPasti(String data, String pasto) {
        String msg = "Proprieta trovate correttamente";
        Map dati = new HashMap();
        dati.put("nome_utente", xmlparser2.getElement("Nome_utente"));
        dati.put("data", data);
        dati.put("tabella", pasto);
        InputStream richiesta = ric.GetRichiesta("/utente1/obiettiviPasti", dati, null);
        dati.clear();
        dati = pj.LeggiJson(richiesta);
        String stato = (String) dati.get("stato");
        if(!stato.equals("obiettivi trovati")) {
            msg = "Resoconto non disponibile per questa data.";
        } else {
            switch(pasto) {
                case "colazione": colazione_totale = Integer.valueOf(""+dati.get("calorie_totali"));
                                  colazione_assunta = Integer.valueOf(""+dati.get("calorie_assunte"));
                                  break;
                case "spuntino":  spuntino_totale = Integer.valueOf(""+dati.get("calorie_totali"));
                                  spuntino_assunto = Integer.valueOf(""+dati.get("calorie_assunte"));
                                  break;
                case "pranzo": pranzo_totale = Integer.valueOf(""+dati.get("calorie_totali"));
                               pranzo_assunto = Integer.valueOf(""+dati.get("calorie_assunte"));
                               break;
                case "cena": cena_totale = Integer.valueOf(""+dati.get("calorie_totali"));
                             cena_assunta = Integer.valueOf(""+dati.get("calorie_assunte"));
                             break;
            }
        }
        return msg;
    }
    
    public Color statoMacronutrienti(int totale, int assunto) {
        float tot = 1.0f * totale; 
        float ass = 1.0f * assunto;
        float percentuale = (ass / tot) * 100;
        System.out.println(percentuale);
        if(percentuale < 90) {
            return Color.yellow; //Troppo poco
        } else if (percentuale > 90 && percentuale <= 110) {
            return Color.green; //Giusto
        } else if (percentuale > 110 && percentuale <= 120) {
            return Color.orange; //Leggermente troppo
        } else {
            return Color.red; //Troppo
        }
    }
    
    public Color statoPasti(int totale, int assunto) {
        float tot = 1.0f * totale; 
        float ass = 1.0f * assunto;
        float percentuale = (ass / tot) * 100;
        System.out.println(percentuale);
        if(percentuale < 90) {
            return Color.yellow; //Troppo poco
        } else if (percentuale > 90 && percentuale <= 110) {
            return Color.green; //Giusto
        } else if (percentuale > 110 && percentuale <= 120) {
            return Color.ORANGE; //Leggermente troppo
        } else {
            return Color.red; //Troppo
        }
    }
    
    public int getCalorieTotali() {
        return calorie_totali;
    }
    
    public int getCalorieAssunte() {
        return getColazioneAssunta() + getSpuntinoAssunto() + getPranzoAssunto() + getCenaAssunta(); 
    }
    
    public int getCarboidratiTotali() {
        return carboidrati_totali;
    }
    
    public int getCarboidratiAssunti() {
        return carboidrati_assunti;
    }
    
    public int getProteineTotali() {
        return proteine_totali;
    }
    
    public int getProteineAssunte() {
        return proteine_assunte;
    }
    
    public int getGrassiTotali() {
        return grassi_totali;
    }
    
    public int getGrassiAssunti() {
        return grassi_assunti;
    }
    
    public int getColazioneTotale() {
        return colazione_totale;
    }
    
    public int getColazioneAssunta() {
        return colazione_assunta;
    }
    
    public int getSpuntinoTotale() {
        return spuntino_totale;
    }
    
    public int getSpuntinoAssunto() {
        return spuntino_assunto;
    }
    
    public int getPranzoTotale() {
        return pranzo_totale;
    }
    
    public int getPranzoAssunto() {
        return pranzo_assunto;
    }
    
    public int getCenaTotale() {
        return cena_totale;
    }
    
    public int getCenaAssunta() {
        return cena_assunta;
    }
}
