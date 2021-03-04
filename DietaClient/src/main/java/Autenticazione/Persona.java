package Autenticazione;

/**
 *
 * @author Bernardo and Paolo
 */

public abstract class Persona {
    private static String nome;
    private static String cognome;
    private static String genere;
    private static int altezza;
    private static int peso;
    private static int eta;
    
    public abstract String login(String nome_utente, String password);
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    
    public void setGenere(String genere) {
        this.genere = genere;
    }

    public void setAltezza(int altezza) {
        this.altezza = altezza;
    }
    
    public void setPeso(int peso) {
        this.peso = peso;
    }
    
    public void setEta(int eta) {
        this.eta = eta;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getCognome() {
        return cognome;
    }
    
    public String getGenere() {
        return genere;
    }
    
    public int getAltezza() {
        return altezza;
    }
    
    public int getPeso() {
        return peso;
    }
    
    public int getEta() {
        return eta;
    }
}
