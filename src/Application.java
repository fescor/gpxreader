import org.xml.sax.helpers.DefaultHandler;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import com.google.common.collect.Lists;
import org.alternativevision.gpx.*;
import org.alternativevision.gpx.beans.*;
import org.alternativevision.gpx.extensions.*;
import org.alternativevision.gpx.log.*;
import org.alternativevision.gpx.types.*;
import java.util.List;
import java.io.*;
import java.net.*;
import java.util.HashMap;




public class Application {
    public static void main(String[] args){
        try{
            ArrayList<Result> results = new ArrayList<Result>();
            ArrayList< HashMap<String , ArrayList<String>>> gpxfiles = new ArrayList<HashMap<String, ArrayList<String>>>();
            FileInputStream a = new FileInputStream("route1.gpx"); //valto se loopa
            FileInputStream b = new FileInputStream("route2.gpx");
            FileInputStream c = new FileInputStream("route3.gpx");
            FileInputStream d = new FileInputStream("route4.gpx");
            FileInputStream e = new FileInputStream("route5.gpx");
            FileInputStream f = new FileInputStream("route6.gpx");
          /*  FileInputStream g = new FileInputStream("route1.gpx"); //valto se loopa
            FileInputStream o = new FileInputStream("route2.gpx");
            FileInputStream k = new FileInputStream("route3.gpx");
            FileInputStream p = new FileInputStream("route4.gpx");
            FileInputStream w = new FileInputStream("route5.gpx");
            FileInputStream l = new FileInputStream("route6.gpx");*/ 


            gpxfiles.add(addToList(a));
            gpxfiles.add(addToList(b));
            gpxfiles.add(addToList(c));
            gpxfiles.add(addToList(d));
            gpxfiles.add(addToList(e));
            gpxfiles.add(addToList(f));
            /*gpxfiles.add(addToList(g));
            gpxfiles.add(addToList(o));
            gpxfiles.add(addToList(k));
            gpxfiles.add(addToList(p));
            gpxfiles.add(addToList(w));
            gpxfiles.add(addToList(l));*/
            
           
  
          

            System.out.println(gpxfiles.size());

            ArrayList<ThreadHandlerApp> handlers = new ArrayList<ThreadHandlerApp>();
            for( int i = 0 ; i < gpxfiles.size() ; i ++ ){

                handlers.add(new ThreadHandlerApp( 4337, gpxfiles.get(i))); 

                handlers.get(i).start();

                

            }
            
            for(int i = 0 ; i < gpxfiles.size() ; i ++ ){
                
                
                
                handlers.get(i).join();


                results.add((handlers.get(i).getResult()));
            }
            for(int i = 0 ; i < results.size() ; i ++){
                print(results.get(i));
            }

        }catch(Exception e){
            System.out.println(e);

        }
        

    }
    public  static HashMap<String, ArrayList<String>>  addToList(FileInputStream a ){
        try{
            GPXParser p = new GPXParser();
            GPX gpx1 = p.parseGPX(a);
            String key = gpx1.getCreator();


       
        
        
            HashSet<Waypoint> waypoints = gpx1.getWaypoints();
            ArrayList<Waypoint> waypointArrayList = new ArrayList<Waypoint>(waypoints);
            waypointArrayList.sort( new Comparewaypoints());
            ArrayList<String> waypointString = new ArrayList<String>();

            for (Waypoint i : waypointArrayList){
                waypointString.add(i.toString());
            }
            HashMap< String, ArrayList<String> > gpx = new HashMap<>();
            gpx.put(key, waypointString);
            return gpx;
        }catch(Exception e){
            System.out.println(e);
            return null;

        } 
        
    }

    public static void print(Result  result) {
        
        System.out.println( " USER  : "  + result.getKey());
        System.out.println("RESULT " );
        System.out.println("DISTANCE : " + result.getDistance());
        System.out.println("ELEVETION : " + result.getElevation());
        System.out.println("SPEED : " + result.getSpeed());
        System.out.println("TIME : " + result.getTime());
        System.out.println();
        System.out.println();
        System.out.println();
                
            

        
        
       

    }

    
}
