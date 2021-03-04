package Impostazioni;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 *
 * @author paolo
 */
public class Xml_pars {

    //attributi 
    private SAXReader parser;
    private Document document;
    private String percorso;
    private HashMap defaultimpo;

    public Xml_pars(String nomedelfile) {
        this.percorso = System.getProperty("user.dir") + File.separator + nomedelfile;
        this.defaultimpo = new HashMap();
        parser = new SAXReader();

    }
    // crea le impostazioni di default 

    /**
     *
     * @param nomedelfile
     * @return
     */
    public boolean ImpoDefault() {
        setDocument(DocumentHelper.createDocument());
        getDocument().addElement("IMPOSTAZIONI");
        popolaimpo();
        Iterator it = getDefaultimpo().entrySet().iterator(); //cerca su internet
        Element rootElement = getDocument().getRootElement();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next(); //chiave - valore 
            String key = (String) pair.getKey();
            String value = (String) pair.getValue();
            Element addElement = rootElement.addElement(key);
            addElement.addText(value);
            it.remove();
        }
        try {
            FileOutputStream fus = new FileOutputStream(new File(this.percorso));
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(fus, format);
            writer.write(getDocument());
            writer.flush();
            fus.close();
            return true;
        } catch (FileNotFoundException ex) {
            System.out.println("Impossibile FILE non trovato  " + ex);
        } catch (UnsupportedEncodingException ex) {
            System.out.println("File non supporta la codifica " + ex);
        } catch (IOException ex) {
            System.out.println("Errore generico " + ex);
        }
        return false;
    }
    public boolean ScriviXML(String root,Map dati ){
        setDocument(DocumentHelper.createDocument());
        getDocument().addElement(root);
        Iterator it = dati.entrySet().iterator(); //cerca su internet
        Element rootElement = getDocument().getRootElement();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next(); //chiave - valore 
            String key = (String) pair.getKey();
            String value = (String) pair.getValue();
            Element addElement = rootElement.addElement(key);
            addElement.addText(value);
            
        }
        try {
            FileOutputStream fus = new FileOutputStream(new File(this.percorso ));
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(fus, format);
            writer.write(getDocument());
            writer.flush();
            fus.close();
            return true;
        } catch (FileNotFoundException ex) {
            System.out.println("Impossibile FILE non trovato  " + ex);
        } catch (UnsupportedEncodingException ex) {
            System.out.println("File non supporta la codifica " + ex);
        } catch (IOException ex) {
            System.out.println("Errore generico " + ex);
        }
        
    
    return false;
    }
    //legge

    /**
     *
     * @param nomefile
     * @param key
     * @return
     */
    public String getElement(String key) {
        List<Element> el;
        File file = new File(this.percorso);
        if (file.exists()) {
            try {
                Document read = getParser().read(file);
                Element rootElement = read.getRootElement();
                el = rootElement.elements(); //contiene tutti i nodi scritti precedentemente
                for (Element e : el) {
                    QName qName = e.getQName();
                    if (qName.getName().equals(key)) {
                        return e.getText(); //value contenuto nel nodo
                    }                   // System.out.println(qName.getName() + ":" + e.getText());
                }

            } catch (DocumentException ex) {
                System.out.println("Errore documento non trovato ");
            }

            return null;
        } else {
            return null;
        }
    }

    public Map GetImpostazioni() {
        Map mappa = new HashMap();
        List<Element> el;
        File file = new File(this.percorso);
        if (file.exists()) {
            try {
                Document read = getParser().read(file);
                Element rootElement = read.getRootElement();
                el = rootElement.elements(); //contiene tutti i nodi scritti precedentemente
                for (Element e : el) {
                    mappa.put(e.getQName().getName(), e.getText());
                }
                return mappa;

            } catch (DocumentException ex) {
                System.out.println("Errore documento non trovato ");
            }

            return null;
        }
        return null;
    }
    // modifica le impostazioni 

    /**
     *
     * @param key
     * @param newValue
     * @return
     */
    public boolean ChangeSetting(String key, String newValue) {
        List<Element> el;
        Document read;
        File file = new File(this.percorso);
        if (file.exists()) {
            try {
                read = getParser().read(file);
                Element rootElement = read.getRootElement();
                el = rootElement.elements(); //contiene tutti i nodi scritti precedentemente
                for (Element e : el) {
                    QName qName = e.getQName();
                    if (qName.getName().equals(key)) {
                        e.setText(newValue); //value contenuto nel nodo
                    }                   // System.out.println(qName.getName() + ":" + e.getText());
                }
                FileOutputStream fus = new FileOutputStream(new File(this.percorso));
                OutputFormat format = OutputFormat.createPrettyPrint();
                XMLWriter writer = new XMLWriter(fus, format);
                writer.write(read);
                writer.flush();
                fus.close();
                return true;

            } catch (DocumentException ex) {
                System.out.println("Errore documento non trovato ");
                return false;
            } catch (FileNotFoundException ex) {
                System.out.println("Errore documento non trovato ");
                return false;
            } catch (UnsupportedEncodingException ex) {
                System.out.println("Errore documento non trovato ");
                return false;
            } catch (IOException ex) {
                System.out.println("Errore documento non trovato ");
                return false;
            }

        } else {
            return false;
        }

    }

    private void popolaimpo() {
        // impostazioni server
        getDefaultimpo().put("SERVER_ADDRES","localhost");
        getDefaultimpo().put("SERVER_PORTA","8085");
        getDefaultimpo().put("PROTOCOL","http");
        //impostazioni database 
       

    }

    public boolean exists() {
        File xml = new File(percorso);
        return xml.exists();
    }

    /**
     * @return the parser
     */
    public SAXReader getParser() {
        return parser;
    }

    /**
     * @param parser the parser to set
     */
    public void setParser(SAXReader parser) {
        this.parser = parser;
    }

    /**
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(Document document) {
        this.document = document;
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
     * @return the defaultimpo
     */
    public HashMap getDefaultimpo() {
        return defaultimpo;
    }

    /**
     * @param defaultimpo the defaultimpo to set
     */
    public void setDefaultimpo(HashMap defaultimpo) {
        this.defaultimpo = defaultimpo;
    }

}
