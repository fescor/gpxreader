import java.util.Date;
import java.util.Comparator;

import org.alternativevision.gpx.beans.Waypoint;



public class Comparewaypoints implements Comparator<Waypoint> {
    public int compare(Waypoint a, Waypoint b){
        Date adate = a.getTime();
        Date bdate = b.getTime();



        return adate.compareTo(bdate);
    }
    
}
