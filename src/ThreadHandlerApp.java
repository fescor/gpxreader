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

public class ThreadHandlerApp extends Thread {
    int port;
    HashMap< String, ArrayList<String> > gpx = new HashMap<String , ArrayList<String>>();
    public Result result = null;
    



    public ThreadHandlerApp(int port , HashMap< String, ArrayList<String> > gpx){
        this.port = port;
        this.gpx = gpx;
    }
   

    public void run(){
        try{
            Thread.sleep(100);
            Socket connection = new Socket("localhost", port );
            OutputStream outputStream = connection.getOutputStream();
            InputStream inputStream = connection.getInputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.flush();
            System.out.println( "USER SENDING GPX TO MASTER ........");
            oos.writeObject(gpx);
            oos.flush();
            //oos.close(); moved to line 46
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            result = (Result)ois.readObject();
            
            
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
