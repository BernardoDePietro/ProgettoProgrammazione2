/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Impostazioni;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger {
    private String percorso ;
    
    /**
     *
     */
    public FileLogger(){
        this.percorso = System.getProperty("user.dir") + File.separator+"log4j.properties";
    }

    /**
     *
     * @return
     */
    public boolean Scrivi(){
        try {
            FileWriter w = new FileWriter(new File(getPercorso()));
            w.write("log4j.rootLogger=INFO, stdout \n"+"log4j.appender.stdout=org.apache.log4j.ConsoleAppender \n"
                    +"log4j.appender.stdout.Target=System.out \n"+"log4j.appender.stdout.layout=org.apache.log4j.PatternLayout \n"+"log4j.appender.stdout.layout.ConversionPattern=%d{yy/MM/dd HH:mm:ss} %p %c{2}: %m%n \n");


           w.close();
           return true;
           
        } catch (IOException ex) {
           System.out.println("Impossibile scrivere il file"+ex);
        }
        
    return false;
    }

    /**
     * @return the percorso
     */
    public String getPercorso() {
        return percorso;
    }

    /**
     * @param percorso the percorso to set
     */
    public void setPercorso(String percorso) {
        this.percorso = percorso;
    }

    /**
     *
     * @return
     */
    public boolean exists(){
    File log = new File(getPercorso());
    return log.exists();
    }    
}
