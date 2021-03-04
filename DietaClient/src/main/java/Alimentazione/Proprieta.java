package Alimentazione;

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
public class Proprieta {
    
    private Xml_pars xmlparser;
    private Xml_pars xmlparser2;
    private Richieste ric;
    private JSON pj;
    private Calendario calendario;
    private ArrayList AlimentiColazione = new ArrayList(10);
    private ArrayList AlimentiSpuntino = new ArrayList(10);
    private ArrayList AlimentiPranzo = new ArrayList(10);
    private ArrayList AlimentiCena = new ArrayList(10);
    private int calorie_totali;
    private int carboidrati_totali;
    private int carboidrati_assunti;
    private int proteine_totali;
    private int proteine_assunte;
    private int grassi_totali;
    private int grassi_assunti;
    private int colazione_totale;
    private int colazione_assunta;
    private int pranzo_totale;
    private int pranzo_assunto;
    private int cena_totale;
    private int cena_assunta;
    private int spuntino_totale;
    private int spuntino_assunto;
    
    public Proprieta() {
        xmlparser = new Xml_pars("Impostazioni.xml");
        xmlparser2 = new Xml_pars("token.save");
        if (!xmlparser.exists()) {
            xmlparser.ImpoDefault();
        }
        pj = new JSON();
        ric = new Richieste(xmlparser.getElement("PROTOCOL"), xmlparser.getElement("SERVER_ADDRES"), xmlparser.getElement("SERVER_PORTA"));
        calendario = new Calendario();
    }
    
    public String getObiettiviMacronutrienti() {
        String msg = "L'obiettivo esiste";
        Map dati = new HashMap();
        dati.put("nome_utente", xmlparser2.getElement("Nome_utente"));
        dati.put("data", calendario.getGiorno());
        InputStream richiesta = ric.GetRichiesta("/utente1/obiettiviMacronutrienti", dati, null);
        dati.clear();
        dati = pj.LeggiJson(richiesta);
        //prendo i valori dei macronutrienti e li salvo nelle variabili di classe
        String stato = (String) dati.get("stato");
        if(!stato.equals("nessun obiettivo trovato")) {
            setCalorieTotali(Integer.valueOf(""+dati.get("calorie_totali")));
            setCarboidratiTotali(Integer.valueOf(""+dati.get("carboidrati_totali")));
            setCarboidratiAssunti(Integer.valueOf(""+dati.get("carboidrati_assunti")));
            setProteineTotali(Integer.valueOf(""+dati.get("proteine_totali")));
            setProteineAssunte(Integer.valueOf(""+dati.get("proteine_assunte")));
            setGrassiTotali(Integer.valueOf(""+dati.get("grassi_totali")));
            setGrassiAssunti(Integer.valueOf(""+dati.get("grassi_assunti")));
        } else {
            msg = "Nessun obiettivo trovato";
        }
        return msg;
    }
    
    public void getObiettivoPasto(String pasto) {
        Map dati = new HashMap();
        dati.put("nome_utente", xmlparser2.getElement("Nome_utente"));
        dati.put("data", calendario.getGiorno());
        dati.put("tabella", pasto);
        InputStream richiesta = ric.GetRichiesta("/utente1/obiettiviPasti", dati, null);
        dati.clear();
        dati = pj.LeggiJson(richiesta);
        String stato = (String) dati.get("stato");
        //prendo i valori dei pasti e li salvo nelle variabili di classe
        if(stato.equals("obiettivi trovati")) {
            if(pasto.equals("colazione")) {
                setColazioneTotale(Integer.valueOf(""+dati.get("calorie_totali")));
                setColazioneAssunta(Integer.valueOf(""+dati.get("calorie_assunte")));
            } else if (pasto.equals("spuntino")) {
                setSpuntinoTotale(Integer.valueOf(""+dati.get("calorie_totali")));
                setSpuntinoAssunto(Integer.valueOf(""+dati.get("calorie_assunte")));
            } else if (pasto.equals("pranzo")) {
                setPranzoTotale(Integer.valueOf(""+dati.get("calorie_totali")));
                setPranzoAssunto(Integer.valueOf(""+dati.get("calorie_assunte")));
            } else if (pasto.equals("cena")) {
                setCenaTotale(Integer.valueOf(""+dati.get("calorie_totali")));
                setCenaAssunta(Integer.valueOf(""+dati.get("calorie_assunte")));
            }
        }
    }
    
    public String getAlimento() {
        String msg = "Alimenti trovati";
        Map dati = new HashMap();
        dati.put("nome_utente", xmlparser2.getElement("Nome_utente"));
        dati.put("data", calendario.getGiorno());
        InputStream richiesta = ric.GetRichiesta("/utente1/alimento", dati, null);
        dati.clear();
        dati = pj.LeggiJson(richiesta);
        String stato = (String) dati.get("stato");
        if(stato.equals("Alimento trovato")) {
            int len = Integer.valueOf(""+dati.get("len"));
            for(int i = 0; i < len; i++) {
                String pasto = (String) dati.get("pasto"+i);
                if(pasto.equals("Colazione")) {
                    AlimentiColazione.add(dati.get("alimento"+i));
                    AlimentiColazione.add(dati.get("grammi"+i));
                    AlimentiColazione.add(dati.get("calorie"+i));
                    AlimentiColazione.add(dati.get("carboidrati"+i));
                    AlimentiColazione.add(dati.get("proteine"+i));
                    AlimentiColazione.add(dati.get("grassi"+i));
                } else if(pasto.equals("Spuntino")) {
                    AlimentiSpuntino.add(dati.get("alimento"+i));
                    AlimentiSpuntino.add(dati.get("grammi"+i));
                    AlimentiSpuntino.add(dati.get("calorie"+i));
                    AlimentiSpuntino.add(dati.get("carboidrati"+i));
                    AlimentiSpuntino.add(dati.get("proteine"+i));
                    AlimentiSpuntino.add(dati.get("grassi"+i));
                } else if(pasto.equals("Pranzo")) {
                    AlimentiPranzo.add(dati.get("alimento"+i));
                    AlimentiPranzo.add(dati.get("grammi"+i));
                    AlimentiPranzo.add(dati.get("calorie"+i));
                    AlimentiPranzo.add(dati.get("carboidrati"+i));
                    AlimentiPranzo.add(dati.get("proteine"+i));
                    AlimentiPranzo.add(dati.get("grassi"+i));
                } else if(pasto.equals("Cena")) {
                    AlimentiCena.add(dati.get("alimento"+i));
                    AlimentiCena.add(dati.get("grammi"+i));
                    AlimentiCena.add(dati.get("calorie"+i));
                    AlimentiCena.add(dati.get("carboidrati"+i));
                    AlimentiCena.add(dati.get("proteine"+i));
                    AlimentiCena.add(dati.get("grassi"+i));
                }
            }
        }
        return msg;
    }
    
    public String eliminaAlimentoAssunto(Map dati) {
        String msg = "Alimento eliminato correttamente.";
        dati.put("nome_utente", xmlparser2.getElement("Nome_utente"));
        dati.put("data", calendario.getGiorno());
        InputStream richiesta = ric.GetRichiesta("/utente1/eliminaAlimentoAssunto", dati, null);
        return msg;
    }
    
    public int getCalorieTotaliAssunte() {
        return getCenaAssunta() + getColazioneAssunta() + getPranzoAssunto() + getSpuntinoAssunto();
    }
    
    public void setCalorieTotali(int calorie_totali) {
        this.calorie_totali = calorie_totali;
    }
    
    public int getCalorieTotali() {
        return calorie_totali;
    }
    
    public void setCarboidratiTotali(int carboidrati_totali) {
        this.carboidrati_totali = carboidrati_totali;
    }
    
    public void setCarboidratiAssunti(int carboidrati_assunti) {
        this.carboidrati_assunti = carboidrati_assunti;
    }
    
    public int getCarboidratiTotali() {
        return carboidrati_totali;
    }
    
    public int getCarboidratiAssunti() {
        return carboidrati_assunti;
    }
    
    public void setProteineTotali(int proteine_totali) {
        this.proteine_totali = proteine_totali;
    }
    
    public void setProteineAssunte(int proteine_assunte) {
        this.proteine_assunte = proteine_assunte;
    }
    
    public int getProteineTotali() {
        return proteine_totali;
    }
    
    public int getProteineAssunte() {
        return proteine_assunte;
    }
    
    public void setGrassiTotali(int grassi_totali) {
        this.grassi_totali = grassi_totali;
    }
    
    public void setGrassiAssunti(int grassi_assunti) {
        this.grassi_assunti = grassi_assunti;
    }
    
    public int getGrassiTotali() {
        return grassi_totali;
    }
    
    public int getGrassiAssunti() {
        return grassi_assunti;
    }
    
    public void setColazioneTotale(int colazione_totale) {
        this.colazione_totale = colazione_totale;
    }
    
    public void setColazioneAssunta(int colazione_assunta) {
        this.colazione_assunta = colazione_assunta;
    }
    
    public int getColazioneTotale() {
        return colazione_totale;
    }
    
    public int getColazioneAssunta() {
        return colazione_assunta;
    }
    
    public void setCenaTotale(int cena_totale) {
        this.cena_totale = cena_totale;
    }
    
    public void setCenaAssunta(int cena_assunta) {
        this.cena_assunta = cena_assunta;
    }
    
    public int getCenaTotale() {
        return cena_totale;
    }
    
    public int getCenaAssunta() {
        return cena_assunta;
    }
    
    public void setSpuntinoTotale(int spuntino_totale) {
        this.spuntino_totale = spuntino_totale;
    }
    
    public void setSpuntinoAssunto(int spuntino_assunto) {
        this.spuntino_assunto = spuntino_assunto;
    }
    
    public int getSpuntinoTotale() {
        return spuntino_totale;
    }
    
    public int getSpuntinoAssunto() {
        return spuntino_assunto;
    }
    
    public void setPranzoTotale(int pranzo_totale) {
        this.pranzo_totale = pranzo_totale;
    }
    
    public void setPranzoAssunto(int pranzo_assunto) {
        this.pranzo_assunto = pranzo_assunto;
    }
    
    public int getPranzoTotale() {
        return pranzo_totale;
    }
    
    public int getPranzoAssunto() {
        return pranzo_assunto;
    }
    
    public ArrayList getAlimentiColazione() {
        return AlimentiColazione;
    }
    
    public ArrayList getAlimentiSpuntino() {
        return AlimentiSpuntino;
    }
    
    public ArrayList getAlimentiPranzo() {
        return AlimentiPranzo;
    }
    
    public ArrayList getAlimentiCena() {
        return AlimentiCena;
    }
}

