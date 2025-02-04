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



public class ThreadHandler extends Thread {
    int port;
    String key;
    long masterID;
    ArrayList<String> chunk = new ArrayList<String>();
    public Result result = null;
    
    

    public ThreadHandler(int port , ArrayList<String> chunk, String key, long masterID){
        this.port = port;
        this.chunk = chunk;
        this.key = key;
        this.masterID = masterID;
    }
   

    public void run(){
        try{
            Socket connection = new Socket("localhost", port );
            OutputStream outputStream = connection.getOutputStream();
            InputStream inputStream = connection.getInputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.flush();
            System.out.println( "THREAD " +  masterID + "SENDING CHUNK TO WORKER ON PORT :" + port);
            oos.writeObject(chunk);
            oos.flush();
            //oos.close(); moved to line 50
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            result = (Result)ois.readObject();
            result.setKey(key);

            
            oos.close();
            connection.close();

        }catch(Exception e){
            System.out.println(e);

        }


    }
    public Result getResult(){

        return result;

    }
    
}
