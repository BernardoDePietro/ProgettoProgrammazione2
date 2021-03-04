package Alimentazione;

import Impostazioni.JSON;
import Impostazioni.Richieste;
import Impostazioni.Xml_pars;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import Impostazioni.Calendario;

/**
 *
 * @author Bernardo and Paolo
 */
public class Scheduler {
    //INTERFACCIA DELLA CLASSE
    private Xml_pars xmlparser;
    private Xml_pars xmlparser2;
    private Richieste ric;
    private JSON pj;
    private Calendario calendario;
    private int metabolismoBasale;
    private int fabbisognoCalorico;
    private int carboidrati;
    private int proteine;
    private int grassi;
    private int colazione;
    private int spuntino;
    private int pranzo;
    private int cena;
    private final float[] PC = {0.40f, 0.55f, 0.50f}; //PC = Percentuale Carboidrati
    private final float[] PP = {0.50f, 0.35f, 0.35f}; //PP = Percentuale Proteine
    private final float[] PASTI = {0.20f, 0.10f, 0.35f}; //PASTI = {colazione, spuntino, pranzo} il rimanente è cena
    private final float[] LAF = {1.2f, 1.375f, 1.55f, 1.725f, 1.9f};  //LAF = Livello di Attività Fisica
    
    //IMPLEMENTAZIONE
    public Scheduler() {
        xmlparser = new Xml_pars("Impostazioni.xml");
        xmlparser2 = new Xml_pars("token.save");
        if (!xmlparser.exists()) {
            xmlparser.ImpoDefault();
        }
        pj = new JSON();
        ric = new Richieste(xmlparser.getElement("PROTOCOL"), xmlparser.getElement("SERVER_ADDRES"), xmlparser.getElement("SERVER_PORTA"));
        calendario = new Calendario();
    }
    
    public void setMetabolismoBasale() {
        //LA FORMULA DEL METABOLISMO BASALE E':
        //  PER LE DONNE = [(10 x PESO) + (6,25 x ALTEZZA) - (5 x ETA) - 161] kcal
        //  PER GLI UOMINI = [(10 x PESO) + (6,25 x ALTEZZA) - (5 x ETA) + 5] kcal
        
        //PRENDO I DATI DELL'UTENTE SALVATI DURANTE IL LOGIN NEL FILE token.save
        int peso = Integer.valueOf(xmlparser2.getElement("Peso"));
        int altezza = Integer.valueOf(xmlparser2.getElement("Altezza"));
        int eta = Integer.valueOf(xmlparser2.getElement("Eta"));
        String genere = xmlparser2.getElement("Genere");
        if(genere == "Donna") {
            metabolismoBasale = (int)((10 * peso) + (6.25f * altezza) - (5 * eta) - 161); //Il risultato è in kcal
        } else {
            metabolismoBasale = (int)((10 * peso) + (6.25f * altezza) - (5 * eta) + 5); //Il risultato è in kcal
        }
    }
    
    public void setfabbisognoCalorico(String lavoro, String sport) {
        //LA FORMULA DEL FABBISOGNO CALORICO E' 
        //IL PRODOTTO TRA IL METABOLISMO BASALE E LIVELLO DI ATTIVITA'(LAF)
        if(getMetabolismoBasale() != 0) {
            //DA RIVEDERE PERCHE ALCUNE VOLTE NON FUNZIONA
            if(lavoro.equals("Lavoro da scrivania") && sport.equals("Non pratico sport")) {
                fabbisognoCalorico = (int) (getMetabolismoBasale() * LAF[0]);
            } else if((lavoro.equals("Lavoro da scrivania") && (sport.equalsIgnoreCase("almeno 2 volte"))) || (lavoro.equals("Lavoro fisicamente attivo ma leggero") && sport.equals("Non pratico sport"))) {
                fabbisognoCalorico = (int) (getMetabolismoBasale() * LAF[1]);
            } else if((lavoro.equals("Lavoro fisicamente attivo ma leggero") && (sport.equalsIgnoreCase("almeno 2 volte"))) || (lavoro.equals("Lavoro fisicamente pesante ma non eccessivamente") && sport.equals("Non pratico sport"))) {
                fabbisognoCalorico = (int) (getMetabolismoBasale() * LAF[2]);
            } else if((lavoro.equals("Lavoro fisicamente pesante ma non eccessivamente") && (sport.equalsIgnoreCase("almeno 2 volte"))) || ((lavoro.equals("Lavoro pesante") && sport.equals("Non pratico sport")))) {
                fabbisognoCalorico = (int) (getMetabolismoBasale() * LAF[3]);
            } else if(lavoro.equals("Lavoro pesante") && sport.equalsIgnoreCase("almeno 2 volte")) {
                fabbisognoCalorico = (int) (getMetabolismoBasale() * LAF[4]);
            }
        }
    }
    
    public void setMacronutrienti(String obiettivo) {
        if(obiettivo.equals("Dimagrimento")) {
            this.carboidrati = (int) (getFabbisognoCalorico() * PC[0]) / 5;
            this.proteine = (int) (getFabbisognoCalorico() * PP[0]) / 5;
            this.grassi = (getFabbisognoCalorico() - (this.carboidrati + this.proteine)) / 12; 
        } else if(obiettivo.equals("Mantenere il peso")) {
            this.carboidrati = (int) (getFabbisognoCalorico() * PC[1]) / 5;
            this.proteine = (int) (getFabbisognoCalorico() * PP[1]) / 5;
            this.grassi = (getFabbisognoCalorico() - (this.carboidrati + this.proteine)) / 12; 
        } else if(obiettivo.equals("Sviluppo Muscolare")) {
            this.carboidrati = (int) (getFabbisognoCalorico() * PC[2]) / 5;
            this.proteine = (int) (getFabbisognoCalorico() * PP[2]) / 5;
            this.grassi = (getFabbisognoCalorico() - (this.carboidrati + this.proteine)) / 12;
        }
    }
    
    public void setPasti(String obiettivo) {
        this.colazione = (int) (getFabbisognoCalorico() * PASTI[0]);
        this.spuntino = (int) (getFabbisognoCalorico() * PASTI[1]);
        this.pranzo = (int) (getFabbisognoCalorico() * PASTI[2]);
        this.cena = getFabbisognoCalorico() - (this.colazione + this.spuntino + this.pranzo);
    }
    
    public String pianificazione() {
        String msg = "Obiettivo pianificato correttamente.\nIl fabbisogno calorico gionaliero è: " + getFabbisognoCalorico() + " kcal.\nIl metabolismo basale è: " + getMetabolismoBasale() + " kcal.";
        msg += "\nRicorda: Lo stesso obiettivo sarà impostato ogni giorno fin quando non verrà cambiato.";
        Map dati = new HashMap();
        dati.put("nome_utente", xmlparser2.getElement("Nome_utente"));
        dati.put("fabbisognoCalorico", ""+getFabbisognoCalorico());
        dati.put("carboidrati", ""+getCarboidrati());
        dati.put("proteine", ""+getProteine());
        dati.put("grassi", ""+getGrassi());
        dati.put("colazione", ""+getColazione());
        dati.put("spuntino", ""+getSpuntino());
        dati.put("pranzo", ""+getPranzo());
        dati.put("cena", ""+getCena());
        dati.put("data", calendario.getGiorno());
        System.out.println(dati);
        InputStream richiesta = ric.GetRichiesta("/utente1/resoconto", dati, null);
        dati.clear();
        dati = pj.LeggiJson(richiesta);
        String result = (String) dati.get("result");
        if(!result.equals("Obiettivo impostato correttamente") && !result.equals("L'obiettivo è stato aggiornato")) {
            msg = "L'obiettivo non è stato impostato correttamente";
        } else if (result.equals("L'obiettivo è stato aggiornato")) {
            msg = "Obiettivo è stato aggiornato correttamente.\nIl fabbisogno calorico gionaliero è: " + getFabbisognoCalorico() + " kcal.\nIl metabolismo basale è: " + getMetabolismoBasale() + " kcal.";
        }
        return msg;
    }
    
    public int getFabbisognoCalorico() {
        return fabbisognoCalorico;
    }
    
    public int getMetabolismoBasale() {
        return metabolismoBasale;
    }
    
    public int getCarboidrati() {
        return carboidrati;
    }
    
    public int getProteine() {
        return proteine;
    }
    
    public int getGrassi() {
        return grassi;
    }
    
    public int getColazione() {
        return colazione;
    }
    
    public int getSpuntino() {
        return spuntino;
    }
    
    public int getPranzo() {
        return pranzo;
    }
    
    public int getCena() {
        return cena;
    }
}
