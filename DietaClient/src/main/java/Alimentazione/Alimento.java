package Alimentazione;

import Impostazioni.Calendario;
import Impostazioni.JSON;
import Impostazioni.Richieste;
import Impostazioni.Xml_pars;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Bernardo and Paolo
 */
public class Alimento {
    private Xml_pars xmlparser;
    private Xml_pars xmlparser2;
    private Richieste ric;
    private JSON pj;
    private Calendario calendario;
    private ArrayList<String> alimento = new ArrayList<String>(10);
    private float calorie;
    private float carboidrati;
    private float proteine;
    private float grassi;
    
    public Alimento() {
        xmlparser = new Xml_pars("Impostazioni.xml");
        xmlparser2 = new Xml_pars("token.save");
        if (!xmlparser.exists()) {
            xmlparser.ImpoDefault();
        }
        pj = new JSON();
        ric = new Richieste(xmlparser.getElement("PROTOCOL"), xmlparser.getElement("SERVER_ADDRES"), xmlparser.getElement("SERVER_PORTA"));
        calendario = new Calendario();
    }
    
    public String getListaAlimenti(String chiave) {
        String msg = "Alimenti trovati";
        Map dati = new HashMap();
        dati.put("chiave", chiave);
        InputStream richiesta = ric.GetRichiesta("/utente1/ricercaAlimenti", dati, null);
        dati.clear();
        dati = pj.LeggiJson(richiesta);
        String stato = (String) dati.get("stato");
        int len = Integer.valueOf(""+dati.get("len"));
        if(len != 0) {
            if(stato.equals("Alimenti trovati")) {
                if(!alimento.isEmpty()) {
                    alimento.clear();
                }
                for(int i = 0; i < len; i++) {
                    alimento.add((String) dati.get("alimento"+i));
                }
            }
        } else {
            msg = "elemento non trovato";
        }
        return msg;
    }
    
    public String macronutrientiAlimento(String elemento) {
        String msg = "Macronutrienti presi correttamente";
        Map dati = new HashMap();
        dati.put("alimento", elemento);
        InputStream richiesta = ric.GetRichiesta("/utente1/prendiMacronutrientiAlimento", dati, null);
        dati.clear();
        dati = pj.LeggiJson(richiesta);
        String stato = (String) dati.get("stato");
        if(stato.equals("Macronutrienti presi correttamente")) {
            this.calorie = Float.valueOf(""+dati.get("calorie"));
            this.carboidrati = Float.valueOf(""+dati.get("carboidrati"));
            this.proteine = Float.valueOf(""+dati.get("proteine"));
            this.grassi = Float.valueOf(""+dati.get("grassi"));
        } else {
            msg = "Errore nei macronutrienti dell'alimento";
        }
        return msg;
    }
    
    public String inserisciAlimentoAssunto(String elemento, String grammi, String pasto) {
        String msg = "Alimento non inserito correttamente";
        if(!grammi.isEmpty() && controllaGrammi(grammi)) {
            int gr = Integer.valueOf(grammi);
            Map dati = new HashMap();
            dati.put("nome_utente", xmlparser2.getElement("Nome_utente"));
            dati.put("data", calendario.getGiorno());
            dati.put("pasto", pasto);
            dati.put("alimento", elemento);
            dati.put("grammi", grammi);
            dati.put("calorie", "" + (int)(getCalorie() * gr));
            dati.put("carboidrati", "" + (int)(getCarboidrati() * gr));
            dati.put("proteine", "" + (int)(getProteine() * gr));
            dati.put("grassi", "" + (int)(getGrassi() * gr));
            msg = "Alimento: " + elemento + ", " + grammi + "g\n"
                    + "Calorie: " + dati.get("calorie") + "kcal\n"
                    + "Carboidrati: " + dati.get("carboidrati") + "g\n"
                    + "Proteine: " + dati.get("proteine") + "g\n"
                    + "Grassi: " + dati.get("grassi") + "g";
            int n = JOptionPane.showConfirmDialog(null, msg + "\nSei sicuro di voler inserire questo alimento?", "Conferma inserimento", JOptionPane.YES_NO_OPTION);
            if(n == JOptionPane.YES_OPTION) {
                InputStream richiesta = ric.GetRichiesta("/utente1/inserisciAlimentoAssunto", dati, null);
                msg = "Alimento inserito correttamente";
            } else {
                msg = "Alimento non inserito";
            }
        } else {
            msg = "La quantità inserita non è corretta";
        }
        return msg;
    }
    
    public void inserisciNuovoAlimento(String nome, String calorie, String carboidrati, String proteine, String grassi) {
        calorie = calorie.replace(",", ".");
        carboidrati = carboidrati.replace(",", ".");
        proteine = proteine.replace(",", ".");
        grassi = grassi.replace(",", ".");
        Map dati = new HashMap();
        dati.put("alimento", nome);
        dati.put("calorie", calorie);
        dati.put("carboidrati", carboidrati);
        dati.put("proteine", proteine);
        dati.put("grassi", grassi);
        InputStream richiesta = ric.GetRichiesta("/dottore/inserisciAlimento", dati, null);
    }
    
    public void eliminaAlimento(String nome) {
        Map dati = new HashMap();
        dati.put("alimento", nome);
        InputStream richiesta = ric.GetRichiesta("/dottore/eliminaAlimento", dati, null);
    }
    
    public String controllaNuovoAlimento(String nome, String calorie, String carboidrati, String proteine, String grassi) {
        calorie = calorie.replace(",", ".");
        carboidrati = carboidrati.replace(",", ".");
        proteine = proteine.replace(",", ".");
        grassi = grassi.replace(",", ".");
        String msg = "Alimento corretto";
        if(!nome.isEmpty() && !calorie.isEmpty() && !carboidrati.isEmpty() && !proteine.isEmpty() && !grassi.isEmpty()) {
            if(controllaNomeAlimento(nome)) {
                if(controllaProprieta(calorie) && controllaProprieta(carboidrati) && controllaProprieta(proteine) && controllaProprieta(grassi)) {
                    System.out.println("Le proprietà dell'alimento che si vuole inserire sono corrette.");
                } else {
                    msg = "Le proprieta dell'alimento non possono contenere lettere e/o caratteri speciali";
                }
            } else {
                msg = "Il nome non può contenere caratteri speciali e/o numeri.";
            }
        } else {
            msg = "Tutti i campi devono essere compilati.";
        }
        return msg;
    }
    
    public boolean controllaNomeAlimento(String nome) {
        char[] space = " ".toCharArray();
        for(int i = 0; i < nome.length(); i++) {
            if(!Character.isAlphabetic(nome.charAt(i))) {
                char carattere = nome.charAt(i);
                if(!(carattere == space[0])) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean controllaProprieta(String value) {
        char[] dot = ".".toCharArray();
        for(int i = 0; i < value.length(); i++) {
            if(!Character.isDigit(value.charAt(i))) {
                char carattere = value.charAt(i);
                if(!(carattere == dot[0])) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean controllaGrammi(String value) {
        for(int i = 0; i < value.length(); i++) {
            if(!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public String getAlimento(int x) {
        return alimento.get(x);
    }
    
    public float getCalorie() {
        return calorie;
    }
    
    public float getCarboidrati() {
        return carboidrati;
    }
    
    public float getProteine() {
        return proteine;
    }
    
    public float getGrassi() {
        return grassi;
    }
    
    public float lenElementi() {
        return alimento.size();
    }
}
