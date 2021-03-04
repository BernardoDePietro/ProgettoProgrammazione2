package com.mycompany.server_dieta;

import Impostazioni.Alimenti;
import Impostazioni.DataBase;
import Impostazioni.DropBox;
import Impostazioni.FileLogger;
import Impostazioni.Linkedin;
import Impostazioni.Xml_pars;
import com.github.scribejava.core.model.OAuth2AccessToken;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import static spark.Spark.*;
import org.apache.log4j.PropertyConfigurator;
import spark.Spark;

public class Server {

    //variabili server globali 
    private static int portServer = 0;
    private static String keystoreFile = "";
    private static String keystorePassword = ""; // percorso keystore -- pass associato al keystore
    //variabili static del server 
    private static FileLogger filelog;
    private static Xml_pars xmlparser;

    private static Linkedin linkedin;
    private static Map<String, Linkedin> richiestesospese;
    private static Map<String, Linkedin> richiesteapprovate;

    private static DataBase database;

    private static DropBox db;

    private static JSON js;

    private static Alimenti alimenti;
    private static Map<String, String> headers_api;

    public static void main(String[] args) {
        // TODO code application logic here
        filelog = new FileLogger(); // istanzio l'oggetto per controllare l'esistenza delle impostazioni del logger.
        xmlparser = new Xml_pars("Impostazioni.xml");
        if (!filelog.exists()) {
            filelog.Scrivi();
        }
        PropertyConfigurator.configure(filelog.getPercorso());
        if (!xmlparser.exists()) {
            xmlparser.ImpoDefault();
        }
        portServer = Integer.parseInt(xmlparser.getElement("PORT_SERVER"));
        keystoreFile = xmlparser.getElement("PATH_KEYSTORE");
        keystorePassword = xmlparser.getElement("KEYSTORE_PASSWORD");

        // inizializzazione server di SparkServer       
        Spark.port(portServer);// inizializza spark sulla porta portServer 
        //Spark.secure(keystoreFile, keystorePassword, null, null, false);
        Spark.init();

        System.out.println("Spark Avviato sulla porta :" + Spark.port());

        // creazione oggetto database 
        String indirizzodb = xmlparser.getElement("SERVER_DATABASE");
        String porta = xmlparser.getElement("PORT_DATABASE");
        String nomedb = xmlparser.getElement("NAME_DATABASE");
        database = new DataBase(indirizzodb, Integer.valueOf(porta), xmlparser.getElement("USER_DATABASE"), xmlparser.getElement("PASS_DATABASE"), nomedb);
        Connection connection = database.getConnection();
        System.out.println("connessione al DB" + indirizzodb + porta + nomedb);

        // inizializzazione DropBox
        db = new DropBox(xmlparser.getElement("DROPBOX_CONFIG"), xmlparser.getElement("DROPBOX_ACCESS_TOKEN"));

        System.out.println("DROPBOX  avviato con le seguenti inmpostazioni \n"
                + "ACCESS TOKEN : " + db.getAccessToken() + "\n"
                + "CONFIGURAZIONE CARTELLA : " + db.getConfigstring() + "\n");

        richiestesospese = new HashMap<String, Linkedin>();
        richiesteapprovate = new HashMap<String, Linkedin>();

        js = new JSON();
        headers_api = new HashMap<String, String>();
        headers_api.put("x-rapidapi-host", xmlparser.getElement("x-rapidapi-host"));
        headers_api.put("x-rapidapi-key", xmlparser.getElement("x-rapidapi-key"));

        alimenti = new Alimenti(xmlparser.getElement("address_apirest"), headers_api);

        path("/", () -> {
            
            //CANCELLA TUTTE LE RIGHE
            
            // localhost:8085/login?id=5
            path("linkedin", () -> {
                get("", (req, res) -> {
                    return "";
                });
                get("/validate", (req, res) -> {
                    String querykey = req.queryParams("ID_LINKEDIN");// recupera l'id associato alla richiesta di autenticazione 
                    boolean ExistUserDb = database.ExistUserDb(querykey);
                    if (!ExistUserDb) {

                        HttpServletResponse raw = res.raw();
                        raw.sendError(500);
                    } else {
                        res.raw().setStatus(200);
                        return "ok";
                    }

                    return "";
                });
                get("/accestoken", (req, res) -> {
                    String querykey = req.queryParams("ID_LINKEDIN");// recupera l'id associato alla richiesta di autenticazione 
                    Linkedin remove = richiesteapprovate.remove(querykey);
                    String iduser;
                    if (remove != null) {
                        System.out.println("qui ci sono");
                        Map GetUser = remove.GetUser();// contiene last name - name - id 
                        GetUser.put("email", remove.GetEmail());  // recupero l'email da linkedin
                        // chiedo al db se l'utente è la prima volta che si collega ?
                        iduser = (String) GetUser.get("id");
                        boolean ExistUserDb = database.ExistUserDb(iduser);
                        if (!ExistUserDb) {
                            database.InsertUser(GetUser);
                        }
                        Map dati = new HashMap();

                        dati.put("Nome", GetUser.get("localizedFirstName"));
                        dati.put("Cognome", GetUser.get("localizedLastName"));
                        dati.put("Id", iduser);

                        String ScriviJson = js.ScriviJson(dati);
                        // mando la risposta
                        ServletOutputStream outputStream = res.raw().getOutputStream();
                        outputStream.print(ScriviJson);
                        outputStream.flush();

                    } else {
                        // se non conferma l'url errore 500
                        HttpServletResponse raw = res.raw();
                        raw.sendError(500);

                    }

                    return "";
                });
                get("/code", (req, res) -> {
                    //serve per url di callback di linkedin 
                    String id = req.queryParams("ID");
                    String code = req.queryParams("code");
                    Linkedin get = richiestesospese.remove(id);
                    OAuth2AccessToken GetToken = get.GetToken(code);
                    get.setAccessToken(GetToken);
                    richiesteapprovate.put(id, get);
                    System.out.println("autenticazione riuscita ");
                    return "Richiesta approvata  con ID :" + id + "\n" + "Ora puoi premere il tasto conferma dell app ";
                });
                post("/add", (req, res) -> {
                    // creazione oggetto linkedin 

                    linkedin = new Linkedin();
                    linkedin.setClientId(xmlparser.getElement("LINKEDIN_CLIENT_ID"));
                    linkedin.setClientSecret(xmlparser.getElement("LINKEDIN_CLIENT_SECRET"));
                    String generateKey = linkedin.generateKey();
                    linkedin.setUrlCallBack(xmlparser.getElement("LINKEDIN_URL_CALLBACK") + "?ID=" + generateKey);
                    linkedin.setScopes(xmlparser.getElement("LINKEDIN_SCOPES"));
                    linkedin.Builder();// builda la richiesta
                    richiestesospese.put(generateKey, linkedin);// inserisco nella mappa "sospesi" questi la richiesta generata dall'utente

                    Map dati = new HashMap();// dati che stiamo tornado indietro all'utente 
                    dati.put("URL", linkedin.RequestURl());
                    dati.put("ID_LINKEDIN", generateKey);

                    String ScriviJson = js.ScriviJson(dati);

                    // rimando indietro json 
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();

                    return res.status();

                });
                delete("/delete", (req, res) -> {
                    return "";
                });
                put("/change", (req, res) -> {
                    return "";
                });
                options("/options", (req, res) -> {
                    return "";
                });

            });
            
            //NUOVE CHIAMATE 
            path("utente1", () -> {
                //verifica se esiste già un nome utente nel database
                get("/prendiDati", (req, res) -> {
                    String nome_utente = req.queryParams("Nome_utente");
                    String tabella = req.queryParams("Tabella");
                    Map result = database.PrendiDati(nome_utente, tabella);
                    String ScriviJson = js.ScriviJson(result);
                    // rimando indietro json
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok"; 
                });
                
                get("/verificaNomeUtente", (req, res) -> {
                   String nome_utente = req.queryParams("nome_utente");
                   
                   //Se la funzione VerificaNomeUtente restituisce true significa 
                   //che il nome dell'utente è già presente quindi non è possibile creare un nuovo utente
                   //istanziamo l'hashmap result.put("esiste", true o false);
                   Map result = new HashMap();
                   if(database.VerificaNomeUtente(nome_utente)) {
                       result.put("result", "esiste");
                   } else {
                       result.put("result", "non esiste");
                   }
                   String ScriviJson = js.ScriviJson(result);
                   // rimando indietro json
                   ServletOutputStream outputStream = res.raw().getOutputStream();
                   outputStream.print(ScriviJson);
                   outputStream.flush();
                   return "ok";
                });
                
                //inserisce un nuovo utente nel sistema
                get("/nuovoUtente", (req, res) -> {
                    Map dati = new HashMap();
                    dati.put("nome_utente", req.queryParams("nome_utente"));
                    dati.put("nome", req.queryParams("nome"));
                    dati.put("cognome", req.queryParams("cognome"));
                    dati.put("genere", req.queryParams("genere"));
                    dati.put("password", req.queryParams("password"));
                    dati.put("altezza", req.queryParams("altezza"));
                    dati.put("peso", req.queryParams("peso"));
                    dati.put("eta", req.queryParams("eta"));
                    database.InserisciNuovoUtente(dati);
                    return "ok";
                });
                
                //effettua la verifica dei dati inseriti nel login
                get("/autenticazione", (req, res) -> {
                    Map dati = new HashMap();
                    dati.put("nome_utente", req.queryParams("nome_utente"));
                    dati.put("password", req.queryParams("password"));
                    dati.put("tabella", req.queryParams("tabella"));
                    if(database.Autenticazione(dati)) {
                        dati.put("result", "accesso confermato");
                    } else {
                        dati.put("result", "accesso non confermato");
                    }
                    String ScriviJson = js.ScriviJson(dati);
                    // rimando indietro json
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok"; 
                });
                
                //controlla se esiste un obiettivo, se esiste non fa niente mentre se non esiste risetta quello precedente
                get("/reimpostaObiettivo", (req, res) -> {
                    Map dati = new HashMap();
                    dati.put("nome_utente", req.queryParams("nome_utente"));
                    dati.put("data", req.queryParams("data"));
                    Map result = new HashMap();
                    Map macro = new HashMap();
                    if(!database.EsisteObiettivo(dati)) {
                        System.out.println("L'obiettivo esiste");
                        result = database.PrendiUltimoObiettivo(dati);
                        System.out.println("Ho preso i dati:" + result);
                        macro = database.PrendiUltimoObiettivoPasto(dati, "colazione");
                        database.InserisciObiettivoPasto("colazione", macro);
                        macro = database.PrendiUltimoObiettivoPasto(dati, "spuntino");
                        database.InserisciObiettivoPasto("spuntino", macro);
                        macro = database.PrendiUltimoObiettivoPasto(dati, "pranzo");
                        database.InserisciObiettivoPasto("pranzo", macro);
                        macro = database.PrendiUltimoObiettivoPasto(dati, "cena");
                        database.InserisciObiettivoPasto("cena", macro);
                        database.InserisciObiettivo(result);
                        result.put("stato", "E' stato reimpostato l'obiettivo precedente");
                    } else {
                        result.put("stato", "L\'obiettivo non esiste");
                    }
                    String ScriviJson = js.ScriviJson(result);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
                
                //inserisce il nuovo obiettivo stabilito nel resoconto
                get("/resoconto", (req, res) -> {
                    Map dati = new HashMap();
                    dati.put("nome_utente", req.queryParams("nome_utente"));
                    dati.put("fabbisognoCalorico", req.queryParams("fabbisognoCalorico"));
                    dati.put("proteine", req.queryParams("proteine"));
                    dati.put("carboidrati", req.queryParams("carboidrati"));
                    dati.put("grassi", req.queryParams("grassi"));
                    dati.put("colazione", req.queryParams("colazione"));
                    dati.put("cena", req.queryParams("cena"));
                    dati.put("pranzo", req.queryParams("pranzo"));
                    dati.put("spuntino", req.queryParams("spuntino"));
                    dati.put("data", req.queryParams("data"));
                    if(database.EsisteObiettivo(dati)) {
                        database.EliminaAlimentiAssunti(dati);
                        database.AggiornaObiettivo(dati);
                        database.AggiornaObiettivoPasto("colazione", dati);
                        database.AggiornaObiettivoPasto("cena", dati);
                        database.AggiornaObiettivoPasto("pranzo", dati);
                        database.AggiornaObiettivoPasto("spuntino", dati);                       
                        dati.clear();
                        dati.put("result", "L'obiettivo è stato aggiornato");
                    } else {
                        database.InserisciObiettivo(dati);
                        database.InserisciObiettivoPasto("colazione", dati);
                        database.InserisciObiettivoPasto("pranzo", dati);
                        database.InserisciObiettivoPasto("cena", dati);
                        database.InserisciObiettivoPasto("spuntino", dati);
                        dati.clear();
                        dati.put("result", "Obiettivo impostato correttamente");
                    }
                    String ScriviJson = js.ScriviJson(dati);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
                
                get("/prendiResoconto", (req, res) -> {
                    Map dati = new HashMap();
                    dati.put("nome_utente", req.queryParams("nome_utente"));
                    dati.put("data", req.queryParams("data"));
                    Map result = database.PrendiMacronutrienteResoconto(dati);
                    String ScriviJson = js.ScriviJson(result);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
                
                get("/obiettiviMacronutrienti", (req, res) -> {
                    Map dati = new HashMap();
                    dati.put("nome_utente", req.queryParams("nome_utente"));
                    dati.put("data", req.queryParams("data"));
                    Map result = database.PrendiMacronutrienteResoconto(dati);
                    String ScriviJson = js.ScriviJson(result);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
                
                get("/obiettiviPasti", (req, res) -> {
                    Map dati = new HashMap();
                    dati.put("nome_utente", req.queryParams("nome_utente"));
                    dati.put("data", req.queryParams("data"));
                    String tabella = req.queryParams("tabella");
                    Map result = database.PrendiObiettivoPasto(dati, tabella);
                    String ScriviJson = js.ScriviJson(result);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
                
                get("/alimento", (req, res) -> {
                    String nome_utente = req.queryParams("nome_utente");
                    String data = req.queryParams("data");
                    Map result = database.PrendiAlimentiAssunti(nome_utente, data);
                    String ScriviJson = js.ScriviJson(result);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
                
                get("/ricercaAlimenti", (req, res) -> {
                    String chiave = req.queryParams("chiave");
                    Map result = database.RicercaAlimenti(chiave);
                    String ScriviJson = js.ScriviJson(result);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
                
                get("/prendiMacronutrientiAlimento", (req, res) -> {
                    String alimento = req.queryParams("alimento");
                    Map result = database.PrendiMacronutrientiAlimento(alimento);
                    String ScriviJson = js.ScriviJson(result);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
                
                get("/inserisciAlimentoAssunto", (req, res) -> {
                    Map dati = new HashMap();
                    dati.put("data", req.queryParams("data"));
                    dati.put("nome_utente", req.queryParams("nome_utente"));
                    dati.put("pasto", req.queryParams("pasto"));
                    dati.put("alimento", req.queryParams("alimento"));
                    dati.put("grammi", req.queryParams("grammi"));
                    dati.put("calorie", req.queryParams("calorie"));
                    dati.put("carboidrati", req.queryParams("carboidrati"));
                    dati.put("proteine", req.queryParams("proteine"));
                    dati.put("grassi", req.queryParams("grassi"));
                    int calorie_assunte = database.PrendiCalorieAssuntePasto(dati);
                    System.out.println(calorie_assunte);
                    if(calorie_assunte != -1) {
                        database.InserisciAlimentoAssunto(dati);
                        int calorie_totali_assunte = Integer.valueOf(""+dati.get("calorie"));
                        calorie_totali_assunte += calorie_assunte;
                        dati.put("calorie", ""+calorie_totali_assunte);
                        database.AggiornaObiettivoPasto(dati);
                        int[] macronutrienti = database.PrendiMacronutrienti(dati);
                        if(macronutrienti.length != 0) {
                            int carboidrati_assunti = Integer.valueOf(""+dati.get("carboidrati"));
                            int proteine_assunte = Integer.valueOf(""+dati.get("proteine"));
                            int grassi_assunti = Integer.valueOf(""+dati.get("grassi"));
                            carboidrati_assunti += macronutrienti[0];
                            proteine_assunte += macronutrienti[1];
                            grassi_assunti += macronutrienti[2];
                            dati.put("carboidrati", ""+carboidrati_assunti);
                            dati.put("proteine", ""+proteine_assunte);
                            dati.put("grassi", ""+grassi_assunti);
                            database.AggiornaResocontoMacronutrienti(dati);
                            System.out.println("dati 2: " + dati);
                        }
                    }
                    return "ok";
                });
                
                get("/eliminaAlimentoAssunto", (req, res) -> {
                    Map dati = new HashMap();
                    dati.put("nome_utente", req.queryParams("nome_utente"));
                    dati.put("data", req.queryParams("data"));
                    dati.put("alimento", req.queryParams("alimento"));
                    dati.put("grammi", req.queryParams("grammi"));
                    dati.put("pasto", req.queryParams("pasto"));
                    dati.put("calorie", req.queryParams("calorie"));
                    dati.put("carboidrati", req.queryParams("carboidrati"));
                    dati.put("proteine", req.queryParams("proteine"));
                    dati.put("grassi", req.queryParams("grassi"));
                    int calorie_assunte = database.PrendiCalorieAssuntePasto(dati);
                    if(calorie_assunte != -1) {
                        database.EliminaAlimentoAssunto(dati);
                        int calorie_da_eliminare = Integer.valueOf(""+dati.get("calorie"));
                        calorie_assunte -= calorie_da_eliminare;
                        dati.put("calorie", ""+calorie_assunte);
                        database.AggiornaObiettivoPasto(dati);
                        int[] macronutrienti = database.PrendiMacronutrienti(dati);
                        if(macronutrienti.length != 0) {
                            int carboidrati_assunti = Integer.valueOf(""+dati.get("carboidrati"));
                            int proteine_assunte = Integer.valueOf(""+dati.get("proteine"));
                            int grassi_assunti = Integer.valueOf(""+dati.get("grassi"));
                            carboidrati_assunti = macronutrienti[0] - carboidrati_assunti;
                            proteine_assunte = macronutrienti[1] - proteine_assunte;
                            grassi_assunti = macronutrienti[2] - grassi_assunti;
                            dati.put("carboidrati", ""+carboidrati_assunti);
                            dati.put("proteine", ""+proteine_assunte);
                            dati.put("grassi", ""+grassi_assunti);
                            database.AggiornaResocontoMacronutrienti(dati);
                        }
                    }
                    return "ok";
                });
                
                get("/cambioPassword", (req, res) -> {
                    String nome_utente = req.queryParams("nome_utente");
                    String nuovapassword = req.queryParams("nuova");
                    String vecchiapassword = req.queryParams("vecchiapassword");
                    String tabella = req.queryParams("tipologia");
                    Map dati = new HashMap();
                    if(database.EsistePassword(nome_utente, vecchiapassword, tabella)) {
                        if(database.UpdatePassword(nome_utente, nuovapassword, tabella)) {
                            dati.put("stato", "Password aggiornata");
                        } else {
                            dati.put("stato", "Password non aggiornata");
                        }
                    } else {
                        dati.put("stato", "Password attuale errata");
                    }
                    String ScriviJson = js.ScriviJson(dati);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
                
                get("/cambioEta", (req, res) -> {
                    String nome_utente = req.queryParams("nome_utente");
                    String eta = req.queryParams("eta");
                    String tabella = req.queryParams("tipologia");
                    Map dati = new HashMap();
                    if(database.UpdateEta(nome_utente, eta, tabella)) {
                        dati.put("stato", "Età aggiornata correttamente");
                    } else {
                        dati.put("stato", "Età non aggiornata");
                    }
                    String ScriviJson = js.ScriviJson(dati);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
                
                get("/inserisciNuovoPeso", (req, res) -> {
                    Map dati = new HashMap();
                    dati.put("nome_utente", req.queryParams("nome_utente"));
                    dati.put("peso", req.queryParams("peso"));
                    dati.put("data", req.queryParams("data"));
                    if(database.ControllaDataPeso(dati)) {
                        database.AggiornaPeso(dati);
                        System.out.println("sono qua");
                    } else {
                        database.InserisciPeso(dati);
                    }
                    return "ok";
                });
                
                get("/visualizzaRisposte", (req, res) -> {
                    String nome_utente = req.queryParams("nome_utente");
                    Map dati = database.visualizzaRisposte(nome_utente);
                    String ScriviJson = js.ScriviJson(dati);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
                
                get("/inviaValutazione", (req, res) -> {
                    String id = req.queryParams("id");
                    String valutazione = req.queryParams("valutazione");
                    database.inviaValutazione(id, valutazione);
                    return "ok";
                });
            });
            
            path("dottore", () -> {
                get("/inserisciAlimento", (req, res) -> {
                    Map dati = new HashMap();
                    dati.put("alimento", req.queryParams("alimento"));
                    dati.put("calorie", req.queryParams("calorie"));
                    dati.put("carboidrati", req.queryParams("carboidrati"));
                    dati.put("proteine", req.queryParams("proteine"));
                    dati.put("grassi", req.queryParams("grassi"));
                    database.InserisciNuovoAlimento(dati);
                    return "ok";
                });
                
                get("/eliminaAlimento", (req, res) -> {
                    String alimento = req.queryParams("alimento");
                    database.EliminaAlimento(alimento);
                    return "ok";
                });
                
                get("/leggiMessaggi", (req, res) -> {
                    Map dati = database.PrendiMessaggi();
                    String ScriviJson = js.ScriviJson(dati);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
            });
            
            path("admin", () -> {
                get("/inserisciDottore", (req, res) -> {
                    Map dati = new HashMap();
                    dati.put("nome", req.queryParams("nome"));
                    dati.put("cognome", req.queryParams("cognome"));
                    dati.put("nome_utente", req.queryParams("nome_utente"));
                    dati.put("genere", req.queryParams("genere"));
                    dati.put("eta", req.queryParams("eta"));
                    dati.put("password", req.queryParams("password"));
                    if(!database.VerificaNomeUtenteDottore(dati)) {
                        database.InserisciNuovoDottore(dati);
                        dati.clear();
                        dati.put("stato", "Nuovo dottore inserito correttamente");
                    } else {
                        dati.clear();
                        dati.put("stato", "Nome utente già esistente.");
                    }
                    String ScriviJson = js.ScriviJson(dati);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
                
                get("/eliminaUtenti", (req, res) -> {
                    String nome_utente = req.queryParams("nome_utente");
                    String tipologia = req.queryParams("tipologia");
                    Map dati = database.EliminaUtente(nome_utente, tipologia);
                    String ScriviJson = js.ScriviJson(dati);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
                
                get("/prendiDottori", (req, res) -> {
                    Map dati = database.PrendiDottori();
                    String ScriviJson = js.ScriviJson(dati);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
                
                get("/prendiDomandeRisposte", (req, res) -> {
                    String nome_utente = req.queryParams("nome_utente");
                    Map dati = new HashMap();
                    dati.put("risposte_dottore", "" + database.contaRisposteDottore(nome_utente));
                    dati.put("domande_con_risposta", "" + database.contaDomandeConRisposta());
                    dati.put("domande_senza_risposta", "" + database.contaDomandeSenzaRisposta());
                    dati.put("media_valutazione", "" + database.mediaValutazione(nome_utente));
                    String ScriviJson = js.ScriviJson(dati);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
            });
            
            path("chat", () -> {
                get("/inviaMessaggio", (req, res) -> {
                    Map dati = new HashMap();
                    dati.put("username", req.queryParams("username"));
                    dati.put("messaggio", req.queryParams("messaggio"));
                    dati.put("user", req.queryParams("user"));
                    dati.put("categoria", req.queryParams("categoria"));
                    String user = (String) dati.get("user");
                    if(user.equals("Dottore")) {
                        dati.put("id", req.queryParams("id"));
                    }
                    database.InviaMessaggio(dati);
                    return "ok";
                });
            });
            
            
            path("statistiche", () -> {
                get("/prendiCampionamenti", (req, res) -> {
                    String utente = req.queryParams("nome_utente");
                    Map result = database.prendiCampionamenti(utente);
                    String ScriviJson = js.ScriviJson(result);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
                
                get("/prendiCampionamentiPeso", (req, res) -> {
                    String utente = req.queryParams("nome_utente");
                    Map result = database.prendiCampionamentiPeso(utente);
                    System.out.println(result);
                    String ScriviJson = js.ScriviJson(result);
                    ServletOutputStream outputStream = res.raw().getOutputStream();
                    outputStream.print(ScriviJson);
                    outputStream.flush();
                    return "ok";
                });
            });
        });
   }
}
