package Impostazioni;

import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonNode;
import io.joshworks.restclient.http.Unirest;
import io.joshworks.restclient.request.GetRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class Alimenti {

    private String indirizzo;
    private Map<String, String> Headers;

    public Alimenti(String indirizzo, Map<String, String> Headers) {
        this.indirizzo = indirizzo;
        this.Headers = Headers;

    }

    public Alimenti(Map<String, String> Headers) {
        this.Headers = new <String, String> HashMap();

    }

    public Map ricerca2(String cibo, String grammi) {
        Map dati = new HashMap();
        
        HttpResponse<JsonNode> response = Unirest.get("https://edamam-edamam-nutrition-analysis.p.rapidapi.com/api/nutrition-data?ingr="+ grammi + "g%20" + cibo)
            .header("x-rapidapi-host", "edamam-edamam-nutrition-analysis.p.rapidapi.com")
            .header("x-rapidapi-key", "6147bd5da4mshfacf56a7067aa5ep1ff9e6jsn60984dedb281")
            .asJson();
        
        JSONObject json = response.getBody().getObject().getJSONObject("totalNutrients");
        
        dati.put("calorie", json.getJSONObject("ENERC_KCAL").getInt("quantity") + "");
        dati.put("grassi", json.getJSONObject("FAT").getDouble("quantity") + "");
        dati.put("zuccheri", json.getJSONObject("SUGAR").getDouble("quantity") + "");
        dati.put("proteine", json.getJSONObject("PROCNT").getDouble("quantity") + "");
        dati.put("sodio", json.getJSONObject("NA").getDouble("quantity") + "");
        dati.put("calcio", json.getJSONObject("CA").getDouble("quantity") + "");
        dati.put("magnesio", json.getJSONObject("MG").getDouble("quantity") + "");
        dati.put("potassio", json.getJSONObject("K").getDouble("quantity") + "");
        dati.put("ferro", json.getJSONObject("FE").getDouble("quantity") + "");
        dati.put("acqua", json.getJSONObject("WATER").getDouble("quantity") + "");
        
        JSONArray array = response.getBody().getObject().getJSONArray("healthLabels");
        for(int i = 0; i < array.length(); i++) {
            dati.put("labels" + i, array.getString(i));
        }    

        return dati;
    }
    
    
    public Map ricerca(String cibo) {
        Map dati = new HashMap();
        GetRequest get = Unirest.get(getIndirizzo() + cibo);
        if (getHeaders() == null) {
            setHeaders((Map<String, String>) new <String, String> HashMap());
        }
        if (!Headers.isEmpty()) {
            Iterator iterator = getHeaders().entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next(); //chiave - valore 
                String key = (String) pair.getKey();
                String value = (String) pair.getValue();
                get.header(key, value);
               
            }

        }
        HttpResponse<JsonNode> asJson = get.asJson();
        if (asJson.getStatus() != 200) {
            return null;
        }
        else{
            JSONObject object = asJson.getBody().getObject();
            dati.put("cibo",cibo);
            dati.put("calories",String.valueOf(object.getInt("calories")));
            dati.put("totalWeight",String.valueOf(object.getInt("totalWeight")));
            
            
            JSONObject jsonObject = object.getJSONObject("totalNutrients");
           // operatore ternario ?
            
            dati.put("quantityf",jsonObject.has("FAT")?String.valueOf(jsonObject.getJSONObject("FAT").getDouble("quantity")):"0");
            dati.put("quantitys",jsonObject.has("SUGAR")?String.valueOf(jsonObject.getJSONObject("SUGAR").getDouble("quantity")):"0");
            dati.put("quantityk",jsonObject.has("ENERC_KCAL")?String.valueOf(jsonObject.getJSONObject("ENERC_KCAL").getDouble("quantity")):"0");
            dati.put("quantityp",jsonObject.has("PROCNT")?String.valueOf(jsonObject.getJSONObject("PROCNT").getDouble("quantity")):"0");
            dati.put("quantityc",jsonObject.has("CHOCDF")?String.valueOf(jsonObject.getJSONObject("CHOCDF").getDouble("quantity")):"0");
            dati.put("quantityfi",jsonObject.has("FIBTG")?String.valueOf(jsonObject.getJSONObject("FIBTG").getDouble("quantity")):"0");
            
            
          
         
            Iterator<Object> iterator = object.getJSONArray("cautions").iterator();
            String avvertenze=""; 
            while(iterator.hasNext()){
                String next = (String) iterator.next();
                avvertenze+=next+",";
            }
                       
            dati.put("quantityavv",avvertenze);
           
            Iterator<Object> iterator1 = object.getJSONArray("dietLabels").iterator();
            String caratteristiche=""; 
            while(iterator1.hasNext()){
                String next = (String) iterator1.next();
                caratteristiche+=next+",";
            }
                       
            dati.put("quantitycar",caratteristiche);
            
        
        }
        
        
        
        
        return  dati;

    }

    /**
     * @return the indirizzo
     */
    public String getIndirizzo() {
        return indirizzo;
    }

    /**
     * @param indirizzo the indirizzo to set
     */
    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    /**
     * @return the Headers
     */
    public Map<String, String> getHeaders() {
        return Headers;
    }

    /**
     * @param Headers the Headers to set
     */
    public void setHeaders(Map<String, String> Headers) {
        this.Headers = Headers;
    }

   

}
