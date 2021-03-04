/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.server_dieta;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;




public class JSON {

    private JSONObject object;
    private JSONParser parser;

    /**
     *
     */
    public JSON() {
        parser = new JSONParser();
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
