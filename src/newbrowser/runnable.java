package newbrowser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;


public class runnable implements Runnable {
 
    
    Thread th;
    String filename;
    
     runnable(int url, int priority) {
        th = new Thread(this);
        th.setName("Thread" + url);
        th.setPriority(priority);
        System.out.println("Creating thread: " + th.getName());
        
        th.start();
    }

 
     @Override
    public void run() {
        
        try {
            System.out.println("Running thread: " + th.getName());
            
            Thread.sleep(300);
        }catch(InterruptedException e) {
            e.printStackTrace ();
        }
    }
    
     public String socket(String url) throws Exception {
        System.out.println("Running Socket - " + th.getName());
        
        String FILENAME = "cache/" + url;
          FILENAME = FILENAME.replace('.', '_');
      FILENAME += ".html";
        
        String html = "";

        File f = new File(FILENAME);
        if(f.exists() && !f.isDirectory()) { 
            BufferedReader in = new BufferedReader(new FileReader(FILENAME));

            String inputLine;
            while((inputLine = in.readLine()) != null) {
                html += inputLine;
            }
             in.close();
            return(html);
         }else{       
            URL aux = new URL("http://"+url);
            URLConnection connection = aux.openConnection();

            String redirect = connection.getHeaderField("Location");

            while(redirect != null) {
                connection = new URL(redirect).openConnection();
                redirect = connection.getHeaderField("Location");
            }

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                FileWriter filewriter = new FileWriter(FILENAME);
                PrintWriter printWriter = new PrintWriter(FILENAME);
                String inputLine;
                while((inputLine = in.readLine()) != null) {
                    printWriter.print(inputLine + "\n");
                    html += inputLine;
                }
                printWriter.close();

                System.out.println(FILENAME);
                setFilename(FILENAME);

                return(html);
            }catch(Exception e) {
                return("ERROR");    
            }
        }
    }
    
    
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getFilename() {
        return(filename);
    }
    

}
