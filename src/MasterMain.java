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



public class MasterMain {
    static  ArrayList<FileInputStream> inputs = new ArrayList<FileInputStream>();
    static HashMap<String , User> Users = new HashMap<String , User>();
    static ArrayList<Master> Masters = new ArrayList<Master>();
    static ArrayList<Integer> ports = new ArrayList<Integer>();
    static User allUserData = new User();
    
    
    
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = null;
		Socket connectionSocket = null;
        allUserData.setid("ALL");
        ports.add(4321);
        ports.add(4325);
        ports.add(4328);

		
		try {
			serverSocket = new ServerSocket(4337);

			while (true) {
				connectionSocket = serverSocket.accept();
                System.out.println("GETTING GPX FILE .....");
                Master thread = new Master(connectionSocket, Users , ports, allUserData);
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

