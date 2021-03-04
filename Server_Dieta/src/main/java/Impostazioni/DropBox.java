package Impostazioni;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

public class DropBox {

    private DbxRequestConfig config = null;
    private DbxClientV2 client = null;
    private Date calendario;
    private String configstring;
    private String AccessToken;

    public DropBox(String config, String AccessToken) {
        this.configstring = config;
        this.AccessToken = AccessToken;
        this.config = new DbxRequestConfig(config);
        client = new DbxClientV2(this.getConfig(), AccessToken);
    }

    public String uploadFile(InputStream fileContent, String folderName, String fileName) {
        setCalendario(Calendar.getInstance().getTime());
        String dataUpload = new SimpleDateFormat("dd-MM-yyyy").format(getCalendario());
        String format = new SimpleDateFormat("kk:mm:ss").format(getCalendario());

        format = format.replace(":", "-");
        fileName = fileName.replace("à", "a");//controllo sui caratteri speciali
        fileName = fileName.replace("è", "e");
        fileName = fileName.replace("é", "e");
        fileName = fileName.replace("ì", "i");
        fileName = fileName.replace("ò", "o");
        fileName = fileName.replace("ù", "u");

        String namefile = "/" + folderName + "/" + dataUpload + "_" + format + "_" + fileName;
        try {
            FileMetadata uploadAndFinish = getClient().files().uploadBuilder(namefile).withMode(WriteMode.OVERWRITE).uploadAndFinish(fileContent);
            return namefile;
        } catch (DbxException | IOException ex) {
            System.out.println("Errore" + ex);
        }
        return null;

    }

    public String createFolder(String folderName) {
        try {
            FolderMetadata folder = getClient().files().createFolder("/" + folderName);

            return folder.getPathDisplay();
        } catch (DbxException ex) {
            return "";
        }
    }

    public String createFolderByPath(String folderName, String Path) {
        try {
            FolderMetadata folder = getClient().files().createFolder(Path + "/" + folderName);
            return folder.getPathDisplay();

        } catch (DbxException ex) {
            return "";
        }
    }

    public List<Metadata> listFolder(String folderName) {
        try {
            ListFolderResult result = getClient().files().listFolder(folderName);
            return result.getEntries();
        } catch (DbxException ex) {
            System.out.println("Dropbox  errore: " + ex);
        }
        return null;
    }

    public InputStream downloadFile(String Nome) {
        try {
            InputStream is = getClient().files().download(Nome).getInputStream();
            return is;
        } catch (DbxException ex) {
            System.out.println("Dropbox readFile error: " + ex);
        }
        return null;
    }

    public boolean eliminaFile(String Nome) {
        try {
            Metadata is = getClient().files().delete(Nome);
            return true;
        } catch (DbxException ex) {
            System.out.println("Dropbox delete error: " + ex);
        }
        return false;
    }

    public boolean Svuotacartella(String cartella) {
        List<Metadata> listFolder = this.listFolder("/" + cartella);
        ListIterator<Metadata> listIterator = listFolder.listIterator();
        while (listIterator.hasNext()) {
            Metadata next = listIterator.next();
            String file_eliminare = next.getPathDisplay();
            this.eliminaFile(file_eliminare);
        }
        return true;
    }

    public DbxRequestConfig getConfig() {
        return config;
    }

    public void setConfig(DbxRequestConfig config) {
        this.config = config;
    }

    public DbxClientV2 getClient() {
        return client;
    }

    public void setClient(DbxClientV2 client) {
        this.client = client;
    }

    public Date getCalendario() {
        return calendario;
    }

    public void setCalendario(Date calendario) {
        this.calendario = calendario;
    }

    public String getConfigstring() {
        return configstring;
    }

    public void setConfigstring(String configstring) {
        this.configstring = configstring;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String AccessToken) {
        this.AccessToken = AccessToken;
    }

}
