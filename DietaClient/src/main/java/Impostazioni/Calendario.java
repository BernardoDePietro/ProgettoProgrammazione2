package Impostazioni;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Bernardo and Paolo
 */
public class Calendario {
    private DateTimeFormatter dtf;
    private LocalDateTime now;
    private LocalDateTime change;

    public Calendario() {
        dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
        now = LocalDateTime.now();
        change = now;
    }
    
    public String getGiorno() {
        return dtf.format(now);
    }
    
    public String getChange() {
        return dtf.format(change);
    }
    
    public boolean comparaDate() {
        if(change.isAfter(now)) {
            return false;
        } else {
            return true;
        }
    }
    
    public String getGiornoPrecedente() {
        change = change.minusDays(1);
        return dtf.format(change);
    }
    
    public String getGiornoSuccessivo() {
        change = change.plusDays(1);
        return dtf.format(change);
    }
}
