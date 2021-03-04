/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Impostazioni;

import com.github.scribejava.apis.LinkedInApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.github.scribejava.core.model.Response;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Linkedin {

    private String ClientId;
    private String ClientSecret;
    private String UrlCallBack;
    private String Scopes;
    private OAuth20Service service;
    private OAuth2AccessToken accessToken;
    private OAuthRequest request;

    /**
     *
     */
    public Linkedin() {
    }

    /**
     *
     * @param ClientId
     * @param ClientSecret
     * @param UrlCallBack
     * @param Scopes
     */
    public Linkedin(String ClientId, String ClientSecret, String UrlCallBack, String Scopes) {
        this.ClientId = ClientId;
        this.ClientSecret = ClientSecret;
        this.UrlCallBack = UrlCallBack;
        this.Scopes = Scopes;
    }

    /**
     *
     */
    public void Builder() {
        service = new ServiceBuilder(this.ClientId)
                .apiSecret(this.ClientSecret)
                .defaultScope(this.Scopes) // replace with desired scope
                .callback(this.UrlCallBack)
                .build(LinkedInApi20.instance());
    }

    /**
     *
     * @return
     */
    public String RequestURl() {
        return service.getAuthorizationUrl();
    }

    /**
     *
     * @param code
     * @return
     */
    public OAuth2AccessToken GetToken(String code) {
        try {
            this.accessToken = service.getAccessToken(code);
            return accessToken;
        } catch (IOException ex) {
            System.out.println("Errore I/O" + ex);
        } catch (InterruptedException ex) {
            System.out.println("Errore Interrupted" + ex);
        } catch (ExecutionException ex) {
            System.out.println("Errore  di Esecuzioni I/O" + ex);
        }
        return null;
    }

    /**
     *
     * @return
     */
    public String GetEmail() {
        JSONParser parser = new JSONParser();
        JSONObject object = null;
        JSONArray jsonArr ;
        request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))");

        request.addHeader("X-Restli-Protocol-Version", "2.0.0");
        request.addHeader("Accept-Language", "it-IT");
        service.signRequest(accessToken, request);
        try {
            Response response = service.execute(request);
            //System.out.println(response.getBody()); //possiamo vedere la risposta della chiamata rest
            object=(JSONObject) parser.parse(response.getBody());
            jsonArr=(JSONArray)object.get("elements");
            for(int i=0 ;i<jsonArr.size();i++){
                object =(JSONObject) jsonArr.get(i);
                Iterator iterator = object.keySet().iterator();
            while(iterator.hasNext()){
                String next = (String) iterator.next();
                if(next.equals("handle~")){
                  JSONObject risultato=(JSONObject) object.get(next);
                    return (String) risultato.get("emailAddress");
                }
              
            }
            
            
            }
            
           // object=(JSONObject)object.get("handle~");
           // return (String)object.get("emailAddress");
           return "";
        } catch (InterruptedException ex) {
            System.out.println("Errore Interrupt" + ex);
        } catch (ExecutionException ex) {
            System.out.println("Errore esecuzione" + ex);
        } catch (IOException ex) {
            System.out.println("Errore I/O" + ex);
        } catch (ParseException ex) {
            Logger.getLogger(Linkedin.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    /**
     *
     * @return
     */
    public Map GetUser() {
        JSONParser parser = new JSONParser();
        JSONObject object = null;
        Map datiutente = new HashMap();
        request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v2/me");
        request.addHeader("X-Restli-Protocol-Version", "2.0.0");
        request.addHeader("Accept-Language", "it-IT");
        service.signRequest(accessToken, request);
        try {
            Response response = service.execute(request);
            object = (JSONObject) parser.parse(response.getBody());// mi rappresenta la risposta della richiesta.
            Iterator iterator = object.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String value = (entry.getValue() instanceof String) ? (String) entry.getValue() : "";
                if (!value.equals("")) {
                    datiutente.put(key, value);
                }
            }
            return datiutente;
        } catch (InterruptedException ex) {
            System.out.println("Errore Interrupt" + ex);
        } catch (ExecutionException ex) {
            System.out.println("Errore esecuzione" + ex);
        } catch (IOException ex) {
            System.out.println("Errore I/O" + ex);
        } catch (ParseException ex) {
            System.out.println("Errore del parser " + ex);
        }
        return null;
    }
     public String generateKey() {
        String key = "";
        Random rand = new Random();
        for (int i = 0; i < 32; i++) {
            int c = rand.nextInt(122 - 48) + 48; //Utilizza solo lettere e numeri escludendo gli altri caratteri ASCII
            if ((c >= 58 && c <= 64) | (c >= 91 && c <= 96)) {
                i--;
                continue;
            }
            key += ((char) c);
        }
        return key;
    }

    /**
     * @return the ClientId
     */
    public String getClientId() {
        return ClientId;
    }

    /**
     * @param ClientId the ClientId to set
     */
    public void setClientId(String ClientId) {
        this.ClientId = ClientId;
    }

    /**
     * @return the ClientSecret
     */
    public String getClientSecret() {
        return ClientSecret;
    }

    /**
     * @param ClientSecret the ClientSecret to set
     */
    public void setClientSecret(String ClientSecret) {
        this.ClientSecret = ClientSecret;
    }

    /**
     * @return the UrlCallBack
     */
    public String getUrlCallBack() {
        return UrlCallBack;
    }

    /**
     * @param UrlCallBack the UrlCallBack to set
     */
    public void setUrlCallBack(String UrlCallBack) {
        this.UrlCallBack = UrlCallBack;
    }

    /**
     * @return the Scopes
     */
    public String getScopes() {
        return Scopes;
    }

    /**
     * @param Scopes the Scopes to set
     */
    public void setScopes(String Scopes) {
        this.Scopes = Scopes;
    }

    /**
     * @return the service
     */
    public OAuth20Service getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(OAuth20Service service) {
        this.service = service;
    }

    /**
     * @return the accessToken
     */
    public OAuth2AccessToken getAccessToken() {
        return accessToken;
    }

    /**
     * @param accessToken the accessToken to set
     */
    public void setAccessToken(OAuth2AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * @return the request
     */
    public OAuthRequest getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(OAuthRequest request) {
        this.request = request;
    }

}
