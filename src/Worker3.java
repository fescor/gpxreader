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




public class Worker3 extends Thread {
    
    Socket connectionSocket;
    public Worker3(Socket socket){
        
        this.connectionSocket = socket;
    
    }
    
    
    public void run(){

        try{
            OutputStream outputStream = connectionSocket.getOutputStream();
            InputStream inputStream = connectionSocket.getInputStream();

            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            

            //oos.writeUTF("Connection is ok!");

            

            ArrayList<String> read = (ArrayList<String>)ois.readObject();
            ArrayList<Waypoint> chunk = new ArrayList<Waypoint>();
            for(String i : read){
                chunk.add(stringTOwaypoint(i));
            }
            
            //Waypoint test = stringTOwaypoint(read.get(0));
            //System.out.println(read);
            //List<Waypoint> chunk = (List<Waypoint>)ois.readObject(); //chunk
            //System.out.println("WORKER :  " + chunk);
            Result result = calculate(chunk);
            System.out.println();
            System.out.println();
            System.out.println("DISTANCE :" + result.getDistance());
            System.out.println("SPEED :" + result.getSpeed());
            System.out.println("ELEVATION :" + result.getElevation());
            System.out.println("TIME :" + result.getTime());
            // epeksergasia chunk
            //System.out.println("WORKER :" + result);
            
            oos.writeObject(result); //double 
            //oos.writeUTF("sending to master peos");
            oos.flush();
            ois.close();
            oos.close();
            connectionSocket.close();
        }catch(Exception e){

        }finally{
            
        }
        
    }
    public double calculateDistance(double lat1 , double lat2, double lon1, double lon2){
        
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
       
        dist = dist * 1.609344;
        
            
        
        return (dist);
    }
    public long timeDif(Date date1, Date date2, TimeUnit timeUnit){
        System.out.println( "        " + date1.getTime() + "       " + date2.getTime());
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }



    public Result calculate(List<Waypoint> chunk){
        double lat =0;
        double lon = 0;
        double ele =0;
        
        Date starttime  = new Date();
        double startlat = 0;
        double startlon = 0;
        double startele = 0;
        double distance = 0;
        Result result;
        double finalEle = 0;
        double speed;
        long time = 0;
        
        for( int i = 0 ; i < chunk.size() ; i++){
            if( i == 0){
                
                
                ele = chunk.get(i).getElevation();
                lat = chunk.get(i).getLatitude();
                startlat = chunk.get(i).getLatitude();
                startlon = chunk.get(i).getLongitude();
                startele = chunk.get(i).getElevation();
                lon = chunk.get(i).getLongitude();
                starttime = chunk.get(i).getTime();
                System.out.println("START TIME :" + starttime);
            }else if ( i == chunk.size() - 1){
                distance = calculateDistance(startlat, chunk.get(i).getLatitude(), startlon, chunk.get(i).getLongitude());
                if(ele < chunk.get(i).getElevation()){
                    if(ele < 0){
                        finalEle += (-1 *(ele)) + chunk.get(i).getElevation();
                    }else{
                        finalEle += chunk.get(i).getElevation() - ele;
                    }
                }
                System.out.println("END TIME :" + chunk.get(i).getTime());
                time = timeDif(starttime, chunk.get(i).getTime(), TimeUnit.SECONDS); //diafora xronou apto proto waypoint mexri to telefteo se defterolepta
                
            }else{
                
                if(ele < chunk.get(i).getElevation()){
                    if(ele < 0){
                        finalEle += (-1 *(ele)) + chunk.get(i).getElevation();
                    }else{
                        finalEle += chunk.get(i).getElevation() - ele;
                    }
                }
                
                
                
                
                ele = chunk.get(i).getElevation();
                

            }


        }
        speed = distance/time;        
        result = new Result(distance, speed, finalEle ,time, null);

        return result;
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
     
    public static void main(String[] args){
        ServerSocket serverSocket = null;
		Socket connectionSocket = null;

		// String message = null;
		try {
			serverSocket = new ServerSocket(4328);

			while (true) {
				connectionSocket = serverSocket.accept();
                System.out.println("Opening conection with Master....");

				Worker thread = new Worker(connectionSocket);
                

				thread.start();
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
    } 
        
    
}