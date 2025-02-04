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
import java.util.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.lang.Math;
import java.util.concurrent.TimeUnit;
import java.util.Map.Entry;


public class Master extends Thread {
    private FileInputStream in;
    private HashMap<String , User> Users;
    private ArrayList<Integer> ports;
    private User allUserData;
    Socket connectionSocket;
    

    public Master(Socket socket, HashMap<String , User> Users, ArrayList<Integer> ports, User allUserData){
        this.connectionSocket = socket;
        this.Users = Users;
        this.ports = ports;
        this.allUserData = allUserData;
        
        
    }
    

    public void run(){

    
        try{
            OutputStream outputStream = connectionSocket.getOutputStream();
            InputStream inputStream = connectionSocket.getInputStream();

            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            HashMap< String, ArrayList<String> > gpx = new HashMap<>();
            gpx = (HashMap< String, ArrayList<String> >)ois.readObject();

           

            String key  = null;
           
            for(String user : gpx.keySet()){
                key = user;
            }
    
            activities(Users, key, allUserData);

            ArrayList<String>  waypointString= gpx.get(key);
            

            
            
            

            

            int size = (waypointString.size()/4) +1;
            ArrayList<String> chunk1 = new ArrayList<String>();
            ArrayList<String> chunk2 = new ArrayList<String>();
            ArrayList<String> chunk3 = new ArrayList<String>();
            ArrayList<String> chunk4 = new ArrayList<String>();

            for ( int i = 0 ; i < waypointString.size() ; i ++ ){
                int index = i / size;
                switch(index){
                    case 0:
                        chunk1.add(waypointString.get(i));

                    break;
                    case 1:
                        chunk2.add(waypointString.get(i));


                    break;
                    case 2:
                        chunk3.add(waypointString.get(i));

                    break;
                    case 3:
                        chunk4.add(waypointString.get(i));

                    break;
                    
                }
            }  
            chunk1.add(chunk2.get(0));
            chunk2.add(chunk3.get(0));
            chunk3.add(chunk4.get(0));
            ArrayList<ArrayList<String>> chunks = new ArrayList<>();
            chunks.add(chunk1);
            chunks.add(chunk2);
            chunks.add(chunk3);
            chunks.add(chunk4);

            
            
         



            
            
            ArrayList<ThreadHandler> handlers = new ArrayList<ThreadHandler>();
            ArrayList<Result> results = new ArrayList<Result>();
            for(int i =0 ; i < 4 ; i++){
                
                
                int index = i%3;

                   
                handlers.add(new ThreadHandler(ports.get(index), chunks.get(i) , key, Thread.currentThread().getId())); 

                handlers.get(i).start();

                Thread.sleep(2000);
                

                

            }
            
            for( int i = 0 ; i < 4 ; i++){


                handlers.get(i).join();


                results.add(handlers.get(i).getResult());


            }
            double distance =0;
            double speed = 0;
            long time = 0;
            double elevetion = 0;
            for(Result i : results){
                distance += i.getDistance();
                speed += i.getSpeed();
                time += i.getTime();
                elevetion += i.getElevation();

                
            }
            Result result = new Result(distance, speed, elevetion, time , key);

            
               
           
            //print(results);

            userData(Users, key, results, allUserData);
                  
                  

            
            
            
            

            
           


            //String read = ois.readUTF();
            



            
            oos.writeObject(result);
            oos.flush();
            ois.close();
            oos.close();
            connectionSocket.close();

        }catch(Exception e){
            System.out.println(e);

        }
        
    }
    public static synchronized void print(ArrayList<Result> results) {
        for(Result i : results){
            System.out.println( " THREAD  : "  + Thread.currentThread().getId());
            System.out.println("RESULT " );
            System.out.println("DISTANCE : " + i.getDistance());
            System.out.println("ELEVETION : " + i.getElevation());
            System.out.println("SPEED : " + i.getSpeed());
            System.out.println("TIME : " + i.getTime());
            System.out.println();
            System.out.println();
            System.out.println();
                
            

        }
        
        //try{
           // Thread.sleep(1000);
        //}catch(Exception e){
           // System.out.println(e);
        //}
       

    }
    public static synchronized void activities(HashMap<String , User> Users , String key,User allUserData){
        boolean flag = true;
        allUserData.setActivities(allUserData.getActivities() + 1);
        for( String i : Users.keySet()){
            if(i.equals(key)){
                flag = false;
                Users.get(i).setActivities(Users.get(i).getActivities() + 1); // sync thread
                
            }
        }
        if(flag){
            
            
            Users.put(key, new User());
            Users.get(key).setid(key);
            Users.get(key).setActivities(1); // sync thread
            
        }

    }
    public static synchronized void userData(HashMap<String , User> Users , String key, ArrayList<Result> results,User allUserData){
        
        for(Result i : results){
            Users.get(key).setTotallDistance(Users.get(key).getTotallDistance() +  i.getDistance());
            Users.get(key).setTotallElevation(Users.get(key).getTotallElevation() + i.getElevation());
            Users.get(key).setTotallTime(Users.get(key).getTotallTime() + i.getTime());
            allUserData.setTotallDistance(allUserData.getTotallDistance() + i.getDistance());
            allUserData.setTotallElevation(allUserData.getTotallElevation() + i.getElevation());
            allUserData.setTotallTime(allUserData.getTotallTime() + i.getTime());
        }
        Users.get(key).setAvgDistance(Users.get(key).getTotallDistance() / Users.get(key).getActivities());
        Users.get(key).setAvgElevetion(Users.get(key).getTotallElevation() / Users.get(key).getActivities());
        Users.get(key).setAvgTime(Users.get(key).getTotallTime() / Users.get(key).getActivities());
        allUserData.setAvgDistance(allUserData.getTotallDistance() / allUserData.getActivities());
        allUserData.setAvgElevetion(allUserData.getTotallElevation() / allUserData.getActivities());
        allUserData.setAvgTime(allUserData.getTotallTime() / allUserData.getActivities());





    }
    public Waypoint stringTOwaypoint(String waypointString){
        Waypoint waypoint = new Waypoint();
        String a[] =  waypointString.split(" ");
        for(String i : a){
           String field[] =  i.split(":");
           switch(field[0]){
                case "lat":
                    //System.out.println(field[1]);
                    double lat =Double.parseDouble(field[1]);
                    waypoint.setLatitude(lat);  
                    break;
                case "lon":
                    //System.out.println(field[1]);
                    double lon =Double.parseDouble(field[1]);
                    waypoint.setLongitude(lon);
                    break;
                case "elv":
                    //System.out.println(field[1]);
                    double ele =Double.parseDouble(field[1]);
                    waypoint.setElevation(ele);
                    break;
                case "time":
                    //System.out.println(field[1]);
                    //System.out.println(a[5]);
                    String timeString  = field[1] + ' ' + a[5];
                    try{
                        Date time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeString);
                        waypoint.setTime(time);
                        break;

                    }catch(Exception e){
                        System.out.println(e);
                    }
                  
                    
            }   

        }
        //System.out.println(waypoint); 
        return waypoint;
    }



    
}
