package Impostazioni;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DataBase {


    private Statement st = null;
    private String  username, password,db, indirizzo;
    private Integer porta;
    private MysqlDataSource datasource = new MysqlDataSource();
    Connection con;

    /**
     *
     * @param dataSource
     */
    public DataBase(MysqlDataSource dataSource) {
        this.datasource = dataSource;

    }

    /**
     *
     * @param url
     * @param username
     * @param password
     */
    public DataBase(String indirizzo, Integer porta, String username, String password, String db) {
         
            this.indirizzo=indirizzo;
            this.porta= porta;        
            this.username=username;
            this.password=password;
            this.db=db;
            System.out.println(indirizzo + porta + username + password + db );   
    }
    
    //NUOVE CHIAMATE AL DB
    public Map PrendiDati(String nome_utente, String tabella) {
        Map result = new HashMap();
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM " + tabella +" WHERE Nome_utente = ?");
            prepareStatement.setString(1, nome_utente);
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();
            
            if(executeQuery.getRow() > 0) {
                if(tabella.equals("utente")) {
                    result.put("Genere", executeQuery.getString("Genere"));
                    result.put("Peso", executeQuery.getString("Peso"));
                    result.put("Altezza", executeQuery.getString("Altezza"));
                    result.put("Eta", executeQuery.getString("Eta"));
                } else if (tabella.equals("dottore")) {
                    result.put("Genere", executeQuery.getString("Genere"));
                    result.put("Eta", executeQuery.getString("Eta"));
                }
                result.put("Nome_utente", executeQuery.getString("Nome_utente"));
                result.put("Nome", executeQuery.getString("Nome"));
                result.put("Cognome", executeQuery.getString("Cognome"));
            }
        } catch(SQLException e) {
            System.out.println("errore query");
        }
        return result;
    }
    
    public Map PrendiDottori() {
        Map dati = new HashMap();
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM dottore");
            ResultSet executeQuery = prepareStatement.executeQuery();
            
            int i = 0;
            while(executeQuery.next()) {
                dati.put("dottore"+i, executeQuery.getString("Nome_utente"));
                i++;
            }
            
            if(i != 0) {
                dati.put("stato", "Dottori trovati");
            } else {
                dati.put("stato", "Dottori non trovati");
            }
        } catch(SQLException e) {
            System.out.println("errore query");
        }
         return dati;
    }
    
    public int contaRisposteDottore(String nome_utente) {
        int i = 0;
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM chat WHERE Dottore = ?");
            prepareStatement.setString(1, nome_utente);
            ResultSet executeQuery = prepareStatement.executeQuery();
            
            while(executeQuery.next()) {
                i++;
            }
        } catch(SQLException e) {
            System.out.println("errore query");
        }
        return i;
    }
    
    public int contaDomandeSenzaRisposta() {
        int i = 0;
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM chat WHERE Risposta IS NULL");
            ResultSet executeQuery = prepareStatement.executeQuery();
            
            while(executeQuery.next()) {
                i++;
            }
        } catch(SQLException e) {
            System.out.println("errore query");
        }
        return i;
    }
    
    public int contaDomandeConRisposta() {
        int i = 0;
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM chat WHERE Risposta IS NOT NULL");
            ResultSet executeQuery = prepareStatement.executeQuery();
            
            while(executeQuery.next()) {
                i++;
            }
        } catch(SQLException e) {
            System.out.println("errore query");
        }
        return i;
    }
    
    public float mediaValutazione(String nome_utente) {
        float i = 0;
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT AVG(Valutazione) FROM chat WHERE Dottore = ?");
            prepareStatement.setString(1, nome_utente);
            ResultSet executeQuery = prepareStatement.executeQuery();
            
            if(executeQuery.next()) {
                i = executeQuery.getFloat(1);
            }
        } catch(SQLException e) {
            System.out.println("errore query");
        }
        return i;
    }
    
    public Map visualizzaRisposte(String nome_utente) {
        Map dati = new HashMap();
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM chat WHERE Utente = ? AND Dottore IS NOT NULL ORDER BY ID_Chat DESC");
            prepareStatement.setString(1, nome_utente);
            ResultSet executeQuery = prepareStatement.executeQuery();
            
            int i = 0;
            while(executeQuery.next()) {
                dati.put("id"+i, executeQuery.getString("ID_Chat"));
                dati.put("dottore"+i, executeQuery.getString("Dottore"));
                dati.put("domanda"+i, executeQuery.getString("Domanda"));
                dati.put("risposta"+i, executeQuery.getString("Risposta"));
                i++;
            }
            System.out.println(dati);
            if(i != 0) {
                dati.put("stato", "Risposte trovate");
            } else {
                dati.put("stato", "Risposte non trovate");
            }
        } catch(SQLException e) {
            System.out.println("errore query");
        }
        return dati;
    }
    
    public boolean EsistePassword(String nome_utente, String password, String tabella) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM " + tabella + " WHERE Nome_utente = ? AND Password = MD5(?)");
            prepareStatement.setString(1, nome_utente);
            prepareStatement.setString(2, password);
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();
            if(executeQuery.getRow() > 0) {
                return true;
            }
        } catch(SQLException e) {
            System.out.println("errore query");
        }
        return false;
    }
    
    public boolean UpdatePassword(String nome_utente, String nuovapassword, String tabella) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("UPDATE " + tabella + " SET Password = MD5(?) WHERE Nome_utente = ?");
            prepareStatement.setString(1, nuovapassword);
            prepareStatement.setString(2, nome_utente);
            
            int stato = prepareStatement.executeUpdate();
            
            System.out.println(prepareStatement.toString());
            System.out.println(nuovapassword);
            
            
            
            if(stato != 0) {
                return true;
            }
        } catch(SQLException e) {
            System.out.println("errore query");
        }
        return false;
    }
    
    public boolean UpdateEta(String nome_utente, String eta, String tabella) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("UPDATE " + tabella + " SET Eta = ? WHERE Nome_utente = ?");
            prepareStatement.setString(1, eta);
            prepareStatement.setString(2, nome_utente);
            int stato = prepareStatement.executeUpdate();
            if(stato != 0) {
                return true;
            }
        } catch(SQLException e) {
            System.out.println("errore query");
        }
        return false;
    }
    
    public void inviaValutazione(String id, String valutazione) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("UPDATE chat SET Valutazione = ? WHERE ID_Chat = ?");
            prepareStatement.setString(1, valutazione);
            prepareStatement.setString(2, id);
            prepareStatement.executeUpdate();

        } catch(SQLException e) {
            System.out.println("errore query");
        }
    }
    
    public boolean VerificaNomeUtente(String nome_utente) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM utente WHERE Nome_utente = ?");
            prepareStatement.setString(1, nome_utente);
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();
            
            if(executeQuery.getRow() > 0) {
                return true;
            }
        } catch(SQLException e) {
            System.out.println("errore query");
        }
         return false;
    }
    
    public boolean VerificaNomeUtenteDottore(Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM dottore WHERE Nome_utente = ?");
            prepareStatement.setString(1, (String) dati.get("nome_utente"));
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();
            
            if(executeQuery.getRow() > 0) {
                return true;
            }
        } catch(SQLException e) {
            System.out.println("errore query");
        }
         return false;
    }
    
    public void InserisciNuovoDottore(Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("INSERT INTO dottore (Nome_utente, Nome, Cognome, Genere, Password, Eta) VALUES (?,?,?,?,MD5(?),?)");
            prepareStatement.setString(1, (String) dati.get("nome_utente"));
            prepareStatement.setString(2, (String) dati.get("nome"));
            prepareStatement.setString(3, (String) dati.get("cognome"));
            prepareStatement.setString(4, (String) dati.get("genere"));
            prepareStatement.setString(5, (String) dati.get("password"));
            prepareStatement.setString(6, (String) dati.get("eta"));
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Errore impossibile aggiungere " + e);
        }
    }
    
    public void InserisciNuovoUtente(Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("INSERT INTO utente (Nome_utente, Nome, Cognome, Genere, Password, Peso, Altezza, Eta) VALUES (?,?,?,?,MD5(?),?,?,?)");
            prepareStatement.setString(1, (String) dati.get("nome_utente"));
            prepareStatement.setString(2, (String) dati.get("nome"));
            prepareStatement.setString(3, (String) dati.get("cognome"));
            prepareStatement.setString(4, (String) dati.get("genere"));
            prepareStatement.setString(5, (String) dati.get("password"));
            prepareStatement.setString(6, (String) dati.get("peso"));
            prepareStatement.setString(7, (String) dati.get("altezza"));
            prepareStatement.setString(8, (String) dati.get("eta"));
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Errore impossibile aggiungere " + e);
        }
    }
    
    public Map EliminaUtente(String nome_utente, String tabella) {
        Map dati = new HashMap();
        try {
            PreparedStatement prepareStatement = con.prepareStatement("DELETE FROM " + tabella + " WHERE Nome_utente = ?");
            prepareStatement.setString(1, nome_utente);
            int stato = prepareStatement.executeUpdate();
            
            if(stato == 0) {
                dati.put("stato", "niente");
            } else {
                dati.put("stato", "eliminato");
            }
        } catch(SQLException e) {
            System.out.println("errore query: " + e);
        }
        return dati;
    }
    
    public Map PrendiUltimoObiettivo(Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM resoconto WHERE Nome_utente = ?");
            prepareStatement.setString(1, (String) dati.get("nome_utente"));
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();

            dati.put("nome_utente", executeQuery.getString("Nome_utente"));
            dati.put("fabbisognoCalorico", executeQuery.getString("Calorie_totali"));
            dati.put("carboidrati", executeQuery.getString("Carboidrati_totali"));
            dati.put("proteine", executeQuery.getString("Proteine_totali"));
            dati.put("grassi", executeQuery.getString("Grassi_totali"));
            
        } catch(SQLException e) {
            System.out.println("errore query ultimo obiettivo: " + e);
        }
         return dati;
    }
    
    public Map PrendiUltimoObiettivoPasto(Map dati, String pasto) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM " + pasto + " WHERE Nome_utente = ?");
            prepareStatement.setString(1, (String) dati.get("nome_utente"));
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();

            dati.put("nome_utente", executeQuery.getString("Nome_utente"));
            dati.put(pasto, executeQuery.getString("Calorie_totali"));
            
        } catch(SQLException e) {
            System.out.println("errore query: " + e);
        }
         return dati;
    }
    
    public boolean Autenticazione(Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM " + dati.get("tabella") + " WHERE Nome_utente = ? AND Password = MD5(?)");
            prepareStatement.setString(1, (String) dati.get("nome_utente"));
            prepareStatement.setString(2, (String) dati.get("password"));
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();
            
            if(executeQuery.getRow() > 0) {
                return true;
            }
        } catch(SQLException e) {
            System.out.println("errore query: " + e);
        }
         return false;
    }
    
    public boolean EsisteObiettivo(Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM resoconto WHERE Nome_utente = ? AND Data = ?");
            prepareStatement.setString(1, (String) dati.get("nome_utente"));
            prepareStatement.setString(2, (String) dati.get("data"));
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();
            
            if(executeQuery.getRow() > 0) {
                return true;
            }
        } catch(SQLException e) {
            System.out.println("errore query esisteObiettivo: " + e);
        }
         return false;
    }
    
    public void InserisciObiettivo(Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("INSERT INTO resoconto (Nome_utente, Calorie_totali, Calorie_assunte, Carboidrati_totali, Carboidrati_assunti, Proteine_totali, Proteine_assunte, Grassi_totali, Grassi_assunti) VALUES (?,?,0,?,0,?,0,?,0)");
            prepareStatement.setString(1, (String) dati.get("nome_utente"));
            prepareStatement.setString(2, (String) dati.get("fabbisognoCalorico"));
            prepareStatement.setString(3, (String) dati.get("carboidrati"));
            prepareStatement.setString(4, (String) dati.get("proteine"));
            prepareStatement.setString(5, (String) dati.get("grassi"));
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Aggiungi:Errore impossibile aggiungere " + e);
        }
    }
    
    public void AggiornaObiettivo(Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("UPDATE resoconto SET Calorie_totali = ?, Calorie_assunte = 0, Carboidrati_totali = ?, Carboidrati_assunti = 0, Proteine_totali = ?, Proteine_assunte = 0, Grassi_totali = ?, Grassi_assunti = 0 WHERE Data = ? AND Nome_utente = ?");
            prepareStatement.setString(1, (String) dati.get("fabbisognoCalorico"));
            prepareStatement.setString(2, (String) dati.get("carboidrati"));
            prepareStatement.setString(3, (String) dati.get("proteine"));
            prepareStatement.setString(4, (String) dati.get("grassi"));
            prepareStatement.setString(5, (String) dati.get("data"));
            prepareStatement.setString(6, (String) dati.get("nome_utente"));
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Aggiorna:Errore impossibile aggiungere " + e);
        }
    } 
    
    public void InserisciObiettivoPasto(String pasto, Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("INSERT INTO " + pasto + " (Nome_utente, Calorie_totali, Calorie_assunte) VALUES (?,?,0)");
            prepareStatement.setString(1, (String) dati.get("nome_utente"));
            prepareStatement.setString(2, (String) dati.get(pasto));
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Errore impossibile aggiungere " + e);
        }
    }
    
    public void AggiornaObiettivoPasto(String pasto, Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("UPDATE " + pasto + " SET Calorie_totali = ?, Calorie_assunte = 0 WHERE Data = ? AND Nome_utente = ?");
            prepareStatement.setString(1, (String) dati.get(pasto));
            prepareStatement.setString(2, (String) dati.get("data"));
            prepareStatement.setString(3, (String) dati.get("nome_utente"));
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("AGGIORNA OBIETTIVO PASTO: Errore impossibile aggiungere " + e);
        }
    }
    
    public Map PrendiObiettivoPasto(Map dati, String tabella) {
        Map result = new HashMap();
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM " + tabella + " WHERE Nome_utente = ? AND Data = ?");
            prepareStatement.setString(1, (String) dati.get("nome_utente"));
            prepareStatement.setString(2, (String) dati.get("data"));
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();
            
            if(executeQuery.getRow() > 0) {
                result.put("calorie_totali", executeQuery.getString("Calorie_totali"));
                result.put("calorie_assunte", executeQuery.getString("Calorie_assunte"));
                result.put("stato", "obiettivi trovati");
            } else {
                result.put("stato", "nessun obiettivo trovato");
            }
        } catch(SQLException e) {
            System.out.println("errore query " + e);
        }
        return result;
    }
    
    public Map PrendiMacronutrienteResoconto(Map dati) {
        Map result = new HashMap();
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM resoconto WHERE Nome_utente = ? AND Data = ?");
            prepareStatement.setString(1, (String) dati.get("nome_utente"));
            prepareStatement.setString(2, (String) dati.get("data"));
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();
            
            if(executeQuery.getRow() > 0) {
                result.put("calorie_totali", executeQuery.getString("Calorie_totali"));
                result.put("calorie_assunte", executeQuery.getString("Calorie_assunte"));
                result.put("carboidrati_totali", executeQuery.getString("Carboidrati_totali"));
                result.put("carboidrati_assunti", executeQuery.getString("Carboidrati_assunti"));
                result.put("proteine_totali", executeQuery.getString("proteine_totali"));
                result.put("proteine_assunte", executeQuery.getString("Proteine_assunte"));
                result.put("grassi_totali", executeQuery.getString("Grassi_totali"));
                result.put("grassi_assunti", executeQuery.getString("Grassi_assunti"));
                result.put("stato", "obiettivi trovati");
            } else {
                result.put("stato", "nessun obiettivo trovato");
            }
        } catch(SQLException e) {
            System.out.println("errore query:: " + e);
        }
        return result;
    }
    
    public Map PrendiAlimentiAssunti(String nome_utente, String data) {
        Map result = new HashMap();
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM assunto WHERE Nome_utente = ? AND Data = ?");
            prepareStatement.setString(1, nome_utente);
            prepareStatement.setString(2, data);
            ResultSet executeQuery = prepareStatement.executeQuery();
            
            int i = 0;
            while(executeQuery.next()) {
                result.put("alimento"+i, executeQuery.getString("Alimento"));
                result.put("calorie"+i, executeQuery.getString("Calorie"));
                result.put("pasto"+i, executeQuery.getString("Pasto"));
                result.put("grammi"+i, executeQuery.getString("Grammi"));
                result.put("carboidrati"+i, executeQuery.getString("Carboidrati"));
                result.put("proteine"+i, executeQuery.getString("Proteine"));
                result.put("grassi"+i, executeQuery.getString("Grassi"));
                i++;
            }
            result.put("stato", "Alimento trovato");
            result.put("len", ""+i);
        } catch(SQLException e) {
            System.out.println("errore query");
        }
        return result;
    }
    
    public Map RicercaAlimenti(String chiave) {
        Map result = new HashMap();
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM alimento WHERE Nome LIKE ? ORDER BY Nome ASC");
            prepareStatement.setString(1, chiave+"%");
            ResultSet executeQuery = prepareStatement.executeQuery();
            
            int i = 0;
            while(executeQuery.next()) {
                result.put("alimento"+i, executeQuery.getString("Nome"));
                result.put("calorie"+i, executeQuery.getString("Calorie"));
                result.put("carboidrati"+i, executeQuery.getString("Carboidrati"));
                result.put("proteine"+i, executeQuery.getString("Proteine"));
                result.put("grassi"+i, executeQuery.getString("Grassi"));
                i++;
            }
            result.put("stato", "Alimenti trovati");
            result.put("len", ""+i);
        } catch(SQLException e) {
            System.out.println("errore query");
        }
        return result;
    }
    
    public Map PrendiMacronutrientiAlimento(String alimento) {
        Map result = new HashMap();
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT DISTINCT * FROM alimento WHERE Nome = ?");
            prepareStatement.setString(1, alimento);
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();
            if(executeQuery.getRow() > 0) {
                result.put("alimento", executeQuery.getString("Nome"));
                result.put("calorie", executeQuery.getString("Calorie"));
                result.put("carboidrati", executeQuery.getString("Carboidrati"));
                result.put("proteine", executeQuery.getString("Proteine"));
                result.put("grassi", executeQuery.getString("Grassi"));
                result.put("stato", "Macronutrienti presi correttamente");
            } else {
                result.put("stato", "Alimento non trovato");
            }
        } catch(SQLException e) {
            System.out.println("errore query");
        }
        return result;
    }
    
    public void InserisciAlimentoAssunto(Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("INSERT INTO assunto (Nome_utente, Pasto, Alimento, Grammi, Calorie, Carboidrati, Proteine, Grassi) VALUES (?,?,?,?,?,?,?,?)");
            prepareStatement.setString(1, (String) dati.get("nome_utente"));
            prepareStatement.setString(2, (String) dati.get("pasto"));
            prepareStatement.setString(3, (String) dati.get("alimento"));
            prepareStatement.setString(4, (String) dati.get("grammi"));
            prepareStatement.setString(5, (String) dati.get("calorie"));
            prepareStatement.setString(6, (String) dati.get("carboidrati"));
            prepareStatement.setString(7, (String) dati.get("proteine"));
            prepareStatement.setString(8, (String) dati.get("grassi"));
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("INSERISCI ALIMENTO: Errore impossibile aggiungere " + e);
        }
    }
    
    public void EliminaAlimentoAssunto(Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("DELETE FROM assunto WHERE Nome_utente = ? AND Pasto = ? AND Alimento = ? AND Grammi = ? AND Data = ?");
            prepareStatement.setString(1, (String) dati.get("nome_utente"));
            prepareStatement.setString(2, (String) dati.get("pasto"));
            prepareStatement.setString(3, (String) dati.get("alimento"));
            prepareStatement.setString(4, (String) dati.get("grammi"));
            prepareStatement.setString(5, (String) dati.get("data"));
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ELIMINA ALIMENTO: Errore impossibile aggiungere " + e);
        }
    }
    
    public void EliminaAlimentiAssunti(Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("DELETE FROM assunto WHERE Nome_utente = ? AND Data = ?");
            prepareStatement.setString(1, (String) dati.get("nome_utente"));
            prepareStatement.setString(2, (String) dati.get("data"));
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Errore impossibile aggiungere " + e);
        }
    }
    
    public int PrendiCalorieAssuntePasto(Map dati) {
        String pasto = (String) dati.get("pasto");
        pasto = pasto.toLowerCase();
        System.out.println(dati + "     " + pasto);
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT Calorie_assunte FROM " + pasto + " WHERE Data = ? AND Nome_utente = ?");
            prepareStatement.setString(1, (String) dati.get("data"));
            prepareStatement.setString(2, (String) dati.get("nome_utente"));
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();
            if(executeQuery.getRow() > 0) {
                return executeQuery.getInt("Calorie_assunte");
            }
        } catch (SQLException e) {
            System.out.println("1.Errore impossibile aggiungere " + e);
        }
        return -1;
    }
    
    public void AggiornaObiettivoPasto(Map dati) {
        String pasto = (String) dati.get("pasto");
        pasto = pasto.toLowerCase();
        try {
            PreparedStatement prepareStatement = con.prepareStatement("UPDATE " + pasto + " SET Calorie_assunte = ? WHERE Data = ? AND Nome_utente = ?");
            prepareStatement.setString(1, (String) dati.get("calorie"));
            prepareStatement.setString(2, (String) dati.get("data"));
            prepareStatement.setString(3, (String) dati.get("nome_utente"));
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("2.Errore impossibile aggiungere " + e);
        }
    }
    
    public int[] PrendiMacronutrienti(Map dati) {
        int[] macronutrienti = new int[3];
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT Carboidrati_assunti, Proteine_assunte, Grassi_assunti FROM resoconto WHERE Data = ? AND Nome_utente = ?");
            prepareStatement.setString(1, (String) dati.get("data"));
            prepareStatement.setString(2, (String) dati.get("nome_utente"));
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();
            if(executeQuery.getRow() > 0) {
                macronutrienti[0] = executeQuery.getInt("Carboidrati_assunti");
                macronutrienti[1] = executeQuery.getInt("Proteine_assunte");
                macronutrienti[2] = executeQuery.getInt("Grassi_assunti");
            }
        } catch (SQLException e) {
            System.out.println("1.Errore impossibile aggiungere " + e);
        }
        return macronutrienti;
    }
    
    public void AggiornaResocontoMacronutrienti(Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("UPDATE resoconto SET Carboidrati_assunti = ?, Proteine_assunte = ?, Grassi_assunti = ? WHERE Data = ? AND Nome_utente = ?");
            prepareStatement.setString(1, (String) dati.get("carboidrati"));
            prepareStatement.setString(2, (String) dati.get("proteine"));
            prepareStatement.setString(3, (String) dati.get("grassi"));
            prepareStatement.setString(4, (String) dati.get("data"));
            prepareStatement.setString(5, (String) dati.get("nome_utente"));
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("2.Errore impossibile aggiungere " + e);
        }
    }
    
    public void InviaMessaggio(Map dati) {
        String user = (String) dati.get("user");
        try {
            if(user.equals("Dottore")) {
                PreparedStatement prepareStatement = con.prepareStatement("UPDATE chat SET Dottore = ?, Risposta = ? WHERE ID_Chat = ?");
                prepareStatement.setString(1, (String) dati.get("username"));
                prepareStatement.setString(2, (String) dati.get("messaggio"));
                prepareStatement.setString(3, (String) dati.get("id"));
                prepareStatement.executeUpdate();
            } else {
                PreparedStatement prepareStatement = con.prepareStatement("INSERT INTO chat (Utente, Categoria, Domanda) VALUES (?,?,?)");
                prepareStatement.setString(1, (String) dati.get("username"));
                prepareStatement.setString(2, (String) dati.get("categoria"));
                prepareStatement.setString(3, (String) dati.get("messaggio"));
                prepareStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Errore impossibile aggiungere " + e);
        }
    }
    
    public Map PrendiMessaggi() {
        Map dati = new HashMap();
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM chat WHERE Dottore IS NULL ORDER BY ID_Chat ASC");
            ResultSet executeQuery = prepareStatement.executeQuery();
            int i = 0;
            while(executeQuery.next() && dati.size() < 30) {
                dati.put("id"+i, executeQuery.getString("ID_Chat"));
                dati.put("utente"+i, executeQuery.getString("Utente"));
                dati.put("categoria"+i, executeQuery.getString("Categoria"));
                dati.put("domanda"+i, executeQuery.getString("Domanda"));
                i++;
            }
            
            if(i == 0) {
                dati.put("stato", "Messaggi non trovati");
            } else {
                dati.put("stato", "Messaggi trovati");
            }
        } catch (SQLException e) {
            System.out.println("1.Errore impossibile aggiungere " + e);
        }
        return dati;
    }
    
    public Map prendiCampionamenti(String utente) {
        Map dati = new HashMap();
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT Carboidrati_assunti, Proteine_assunte, Grassi_assunti FROM resoconto WHERE Nome_utente = ? ORDER BY Data DESC");
            prepareStatement.setString(1, utente);
            ResultSet executeQuery = prepareStatement.executeQuery();
            int i = 0;
            while(executeQuery.next() && dati.size() < 30) {
                dati.put("carboidrati"+i, executeQuery.getString("Carboidrati_assunti"));
                dati.put("proteine"+i, executeQuery.getString("Proteine_assunte"));
                dati.put("grassi"+i, executeQuery.getString("Grassi_assunti"));
                i++;
            }
            
            if(i == 0) {
                dati.put("stato", "Campionamenti non trovati");
            } else {
                dati.put("stato", "Campionamenti trovati");
            }
        } catch (SQLException e) {
            System.out.println("1.Errore impossibile aggiungere " + e);
        }
        return dati;
    }
    
    public Map prendiCampionamentiPeso(String utente) {
        Map dati = new HashMap();
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT Peso FROM peso WHERE Nome_utente = ? ORDER BY Data DESC");
            prepareStatement.setString(1, utente);
            ResultSet executeQuery = prepareStatement.executeQuery();
            int i = 0;
            while(executeQuery.next() && dati.size() < 10) {
                dati.put("peso"+i, executeQuery.getString("Peso"));
                i++;
            }
            
            if(i == 0) {
                dati.put("stato", "Campionamenti non trovati");
            } else {
                dati.put("stato", "Campionamenti trovati");
            }
        } catch (SQLException e) {
            System.out.println("1.Errore impossibile aggiungere " + e);
        }
        return dati;
    }    
    
    public boolean ControllaDataPeso(Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM peso WHERE Nome_utente = ? ORDER BY Data DESC");
            prepareStatement.setString(1, (String) dati.get("nome_utente"));
            ResultSet executeQuery = prepareStatement.executeQuery();
            String data = (String) dati.get("data");
            System.out.println(data);
            while(executeQuery.next()) {
                if(data.equalsIgnoreCase(executeQuery.getString("Data"))) {
                    System.out.println("data: " + data);
                    System.out.println("data db: " + executeQuery.getString("Data"));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("1.Errore impossibile aggiungere " + e);
        }
        return false;
    }
    
    public void InserisciPeso(Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("INSERT INTO peso (Nome_utente, Peso, Data) VALUES (?, ?, ?)");
            prepareStatement.setString(1, (String) dati.get("nome_utente"));
            prepareStatement.setString(2, (String) dati.get("peso"));
            prepareStatement.setString(3, (String) dati.get("data"));
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("1.Errore impossibile aggiungere " + e);
        }
    }

    public void AggiornaPeso(Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("UPDATE peso SET Peso = ? WHERE Nome_utente = ? AND Data = ?");
            prepareStatement.setString(1, (String) dati.get("peso"));
            prepareStatement.setString(2, (String) dati.get("nome_utente"));
            prepareStatement.setString(3, (String) dati.get("data"));
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("1.Errore impossibile aggiungere " + e);
        }
    }
    
    public void InserisciNuovoAlimento(Map dati) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("INSERT INTO alimento (Nome, Calorie, Carboidrati, Proteine, Grassi) VALUES (?, ?, ?, ?, ?)");
            prepareStatement.setString(1, (String) dati.get("alimento"));
            prepareStatement.setString(2, (String) dati.get("calorie"));
            prepareStatement.setString(3, (String) dati.get("carboidrati"));
            prepareStatement.setString(4, (String) dati.get("proteine"));
            prepareStatement.setString(5, (String) dati.get("grassi"));
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("1.Errore impossibile aggiungere " + e);
        }
    }
    
    public void EliminaAlimento(String alimento) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("DELETE FROM alimento WHERE Nome = ?");
            prepareStatement.setString(1, alimento);
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Errore impossibile aggiungere " + e);
        }
    }
    
    //CONTROLLA SE LA DATA E' GIA' PRESENTE NELLA TABELLA
    public boolean ExistDataDb(String tabella, Map map) {  
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM " + tabella + " WHERE ID = ? && data = ?");
            prepareStatement.setString(1, (String) map.get("id"));
            prepareStatement.setString(2, (String) map.get("data"));
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();
            
            if(executeQuery.getRow() > 0) {
                return false;
            }
        } catch(SQLException e) {
            System.out.println("errore query");
        }
         return true;
    }
    
    //CONTROLLA SE L'OBIETTIVO DELL'ACQUA E' GIA' STATO SETTATO
    public boolean ExistWater(String tabella, Map map) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM " + tabella + " WHERE data = ? && ID = ?");
            prepareStatement.setString(1, (String) map.get("data"));
            prepareStatement.setString(2, (String) map.get("id"));
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();
            
            if(executeQuery.getRow() == 1) {
                return true;
            }
        } catch(SQLException e) {
            System.out.println("Impossibile vedere se esiste " + e);
        }
         return false;
    }
    
    //CONTROLLA SE L'UTENTE HA GIA' INSERITO L'ALTEZZA
    public boolean ExistHeight(String id) {
        System.out.println(id);
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT Altezza FROM utente WHERE ID_linkedin = ?");
            prepareStatement.setString(1, id);
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();
            if(executeQuery.getRow() == 1) {
                return true;
            }
        } catch(SQLException e) {
            System.out.println("Impossibile vedere se esiste " + e);
        }
        return false;
    }
    
    //INSERISCE L'OBIETTIVO DI BERE ACQUA PER UNA SETTIMANA
    public boolean InsertWater(Map dati) throws SQLException {
        int executeUpdate = 0;
        try {
            PreparedStatement prepareStatement = con.prepareStatement("INSERT INTO acqua (ID, Litri, Bicchieri, Data, Stato) VALUES (?,?,?,?,?)");
            for(int i = 0; i < 7; i++) {
                prepareStatement.setString(1, (String) dati.get("id"));
                prepareStatement.setString(2, (String) dati.get("litri"));
                prepareStatement.setString(3, (String) dati.get("bicchieri"));
                prepareStatement.setString(4, (String) dati.get("" + i));
                prepareStatement.setString(5, "0");
                executeUpdate = prepareStatement.executeUpdate();
                if(executeUpdate == 0) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Errore impossibile aggiungere " + e);
        }
        return false;
    }
    
    //PRENDE L'OBIETTIVO SELEZIONATO DALL'UTENTE
    public Map TakeWater(String id, String data) throws SQLException {
        Map dati = new HashMap();
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT Litri, Stato FROM acqua WHERE ID = ? AND Data = ?");
            prepareStatement.setString(1, id);
            prepareStatement.setString(2, data);
            
            ResultSet executeQuery = prepareStatement.executeQuery();
           
            if(executeQuery.next()) {
                dati.put("litri", executeQuery.getString("Litri"));
                dati.put("stato", executeQuery.getString("Stato"));
            }
            
            if(dati.isEmpty()) {
                dati.put("0", "nessun elemento");
                return dati;
            }
            else {
                return dati;
            }
        } catch (SQLException e) {
            System.out.println("Error impossibile prendere " + e);
        }
        dati.put("0", "nessun elemento");
        return dati;
    }
    
    public boolean UpdateDrunk(Map dati) {
        int executeUpdate = 0;
        try {
            PreparedStatement prepareStatement = con.prepareStatement("UPDATE acqua SET Stato = ? WHERE ID = ? AND Data = ?");
            for(int i = 0; i < 7; i++) {
                prepareStatement.setString(1, (String) dati.get("drunk"));
                prepareStatement.setString(2, (String) dati.get("id"));
                prepareStatement.setString(3, (String) dati.get("data"));
                executeUpdate = prepareStatement.executeUpdate();
                if(executeUpdate == 0) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Errore impossibile aggiungere " + e);
        }
        return false;
    }

    //INSERISCE UN NUOVO UTENTE
    public boolean InsertUser(Map dati) {
        
        try {
            PreparedStatement prepareStatement = con.prepareStatement("INSERT INTO utente (Nome_utente,Nome,Cognome)  VALUES (?,?,?)");
            prepareStatement.setString(1, (String) dati.get("id"));
            prepareStatement.setString(2, (String) dati.get("localizedFirstName"));
            prepareStatement.setString(3, (String) dati.get("localizedLastName"));
        
            
            int executeUpdate = prepareStatement.executeUpdate();
            if (executeUpdate == 0) {
                return false;
            }
            
            return true;
        } catch (SQLException ex) {
            System.out.print("Errore impossibile aggiungere" + ex);
        }
        return false;
    }
    
    //ELIMINA TUTTI GLI ELEMENTI DELLA TABELLA dieta CON QUELL'ID
    public boolean DeleteDiet(String id) throws SQLException {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("DELETE FROM dieta WHERE ID = ?");
            prepareStatement.setString(1, id);
            prepareStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            System.out.println("Impossibile eliminare... " + e);
            return false;
        }
    } 
    
    //RESTITUISCE TUTTI GLI ALIMENTI CHE DEVE MANGIARE L'UTENTE
    public Map TakeAlimenti(Map map) throws SQLException {
        Map dati = new HashMap();
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT Alimento FROM dieta WHERE ID = ? && Data = ? && Pasto = ?");
            prepareStatement.setString(1, (String) map.get("id"));
            prepareStatement.setString(2, (String) map.get("data"));
            prepareStatement.setString(3, (String) map.get("pasto"));
            ResultSet executeQuery = prepareStatement.executeQuery();
            
            int i = 0;
            while(executeQuery.next()) {
                dati.put("" + i, executeQuery.getString("Alimento"));
                i++;
            }
          
        } catch (SQLException ex) {
            System.out.println("Impossibile creare lo stat... " + ex);
        }
        
        return dati;
    }
    
    //PRENDE I DATI DELL'UTENTE
    public Map TakeUser(String id) {
        Map dati = new HashMap();
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT Nome, Cognome, Altezza, Peso FROM utente WHERE Nome_utente = ?");
            prepareStatement.setString(1, id);
            ResultSet executeQuery = prepareStatement.executeQuery();
            
            if(executeQuery.last()) {
                dati.put("nome", executeQuery.getString("Nome"));
                dati.put("cognome", executeQuery.getString("Cognome"));
                dati.put("altezza", executeQuery.getString("Altezza"));
                dati.put("peso", executeQuery.getString("Peso"));
                return dati;
            }
        } catch (SQLException ex) {
            System.out.println("Impossibile creare lo stat... " + ex);
        }
        
        return dati;
    }
    
    //CONTROLLO SE ESISTE GIA' L'UTENTE
    public boolean ExistUserDb(String primarykey) {
        try {
            PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM utente WHERE Nome_utente = ?");
            prepareStatement.setString(1, primarykey);
            ResultSet executeQuery = prepareStatement.executeQuery();
            executeQuery.last();
            return executeQuery.getRow() > 0 ? true : false;
            
        } catch (SQLException ex) {
            System.out.println("Impossibile creare lo stat... " + ex);
        }
        return false;
    }


    public Connection getConnection()  {
         
        datasource.setServerName(this.indirizzo);
        datasource.setUser(this.username);
        datasource.setPassword(this.password);
        datasource.setDatabaseName(this.db);
        datasource.setPortNumber(this.porta);
        
        try {
            con = datasource.getConnection();
            return con;
        } catch (SQLException ex) {
           System.out.println(ex);
        }
        return null;
    }

    /**
     * @return the datasource
     */
    public MysqlDataSource getDatasource() {
        return datasource;
    }

    /**
     * @param datasource the datasource to set
     */
    public void setDatasource(MysqlDataSource datasource) {
        this.datasource = datasource;
    }

    /**
     * @return the st
     */
    public Statement getSt() {
        return st;
    }

    /**
     * @param st the st to set
     */
    public void setSt(Statement st) {
        this.st = st;
    }

    

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
