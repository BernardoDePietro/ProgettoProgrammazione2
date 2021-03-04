package Impostazioni;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JSON {

    private JSONObject object;
    private JSONParser parser;
    private Date data;
    private Date today;
    private Calendar calendar;
    private int format;
    private Random random;

    public JSON() {
        parser = new JSONParser();
        data = new Date();
        calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
        format = DateFormat.MEDIUM;
        random = new Random();
    }

    //PRENDE I VALORI DELLE QUANTITA' CHE DOVREBBE MANGIARE UNA PERSONA NORMOPESO
    public String leggiQuantita(String cibo) throws FileNotFoundException, IOException, ParseException {
        String result = "100";
        try (FileReader reader = new FileReader("quantita.json")) {
            Object obj = getParser().parse(reader);
            JSONObject json = (JSONObject) obj;
            if(json.containsKey(cibo)) {
                result = (String) json.get(cibo);
            }
        } catch (Exception e) {
            System.out.println("Impossibile prendere la quantita " + e);
        }
        return result;
    }
    
    //LEGGE IL FILE PREDEFINITO PER LA DIETA CHE HA SELEZIONATO L'UTENTE
    public Map leggiDieta(String dieta, String pasto) throws FileNotFoundException, IOException, ParseException {
        Map map = new HashMap();
        
        try (FileReader reader = new FileReader(dieta + ".json")) {
            Object obj = getParser().parse(reader);
            JSONObject json = (JSONObject) obj;
            JSONObject p = (JSONObject) json.get(pasto);
            for(int i = 0; i < p.size(); i++) {
                map.put("" + i, p.get("" + i));
            }
            return map;
        }
    }
    
    //LEGGE I CIBI DELLA SOTTOCATEGORIA SELEZIONATA DALL'UTENTE
    //ES: SE L'UTENTE SELEZIONA "red meat", QUESTO METODO ENTRA NEL TAG RED MEAT
    //E PRENDE UN VALORE CASUALE AL SUO INTERNO
    public Map leggiCibi(String pasto, Map dati) throws FileNotFoundException, IOException, ParseException {
        Map map = new HashMap();
        
        try (FileReader reader = new FileReader("cibi.json")) {
            Object obj = getParser().parse(reader);
            JSONObject json = (JSONObject) obj;
            JSONObject p = (JSONObject) json.get(pasto);
            JSONObject a;
            for(int i = 0; i < 7; i++) {
                a = (JSONObject) p.get(dati.get("" + random.nextInt(dati.size())));
                map.put("" + i , a.get("" + random.nextInt(a.size())));
            }

            return map;
        }
    }
    
    //NEL CASO DELLA DIETA PERSONALE L'UTENTE ENTRARA' IN CONTATTO CON TUTTI GLI ALIMENTO CHE 
    //PUO' GESTIRE IL SISTEMA IN MODO DA NON VENIRE PIU' SELEZIONATI CASUALMENTE MA ANZI,
    //L'UTENTE LI POTRA' SCEGLIERE IN BASE AI PROPRI GUSTI
    public Map leggiCibiPersonali(String pasto) throws FileNotFoundException, IOException, ParseException {
        Map map = new HashMap();
        
        try (FileReader reader = new FileReader("cibiPersonal.json")) {
            Object obj = getParser().parse(reader);
            JSONObject json = (JSONObject) obj;
            JSONObject p = (JSONObject) json.get(pasto);
            for(int i = 0; i < p.size(); i++) {
                map.put(""+i, p.get(""+i));
            }

            return map;
        }
    }
    
    public void inserisciNewAlimento(Map dati) throws FileNotFoundException, IOException, ParseException {
        try (FileReader reader = new FileReader("cibiPersonal.json")) {
            Object obj = getParser().parse(reader);
            JSONObject json = (JSONObject) obj;
            JSONObject p = (JSONObject) json.get(dati.get("pasto"));
            int size = p.size();
            
            p.put("" + size, dati.get("alimento"));
            json.put(dati.get("pasto"), p);
            
            try (FileWriter file = new FileWriter("cibiPersonal.json")){
                file.write(json.toJSONString());
                file.flush();
            }
        }
    }
    
    public void inserisciNewQuantità(Map dati) throws FileNotFoundException, IOException, ParseException {
        try (FileReader reader = new FileReader("quantita.json")) {
            Object obj = getParser().parse(reader);
            JSONObject json = (JSONObject) obj;
            json.put(dati.get("alimento"), dati.get("grammi"));
            
            try (FileWriter file = new FileWriter("quantita.json")){
                file.write(json.toJSONString());
                file.flush();
            }
        }
    }
    

    /**
     *
     * @param elementi
     * @return
     */
    public String ScriviJson(Map elementi) {
        setObject(new JSONObject());
        if (!elementi.isEmpty()) {
            Iterator it = elementi.entrySet().iterator(); //cerca su internet
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next(); //chiave - valore 
                String key = (String) pair.getKey();
                String value = (String) pair.getValue();
                getObject().put(key, value);
                it.remove();
            }

        }
        return getObject().toJSONString();
    }

    /**
     *
     * @param is
     * @return
     */
    public Map LeggiJson(InputStream is) {
        Map dati = new HashMap();
        try {
            Object parse = getParser().parse(new InputStreamReader(is));
            JSONObject jsonObject = (JSONObject) parse; //casting implicito

            Iterator iterator = jsonObject.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = (String) jsonObject.get(key);
                dati.put(key, value);
            }

        } catch (IOException ex) {
            System.out.println("ERRORE I/O" + ex);
        } catch (ParseException ex) {
            System.out.println("IMPOSSIBILE PARSARE IL FILE I/O" + ex);
        }

        return dati;
    }

    /**
     * @return the object
     */
    public JSONObject getObject() {
        return object;
    }
    
    public String getDate() {
        today = calendar.getTime();
        DateFormat dateFormat = DateFormat.getDateInstance(format, Locale.ITALY);
        return dateFormat.format(today);
    }

    /**
     * @param object the object to set
     */
    public void setObject(JSONObject object) {
        this.object = object;
    }

    /**
     * @return the parser
     */
    public JSONParser getParser() {
        return parser;
    }

    /**
     * @param parser the parser to set
     */
    public void setParser(JSONParser parser) {
        this.parser = parser;
    }

}

