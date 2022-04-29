package Threads;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ThreadsServer extends Thread {

    private Daten _dataThread;
    private boolean _finished;

    //getter/setter
    public Daten getDataThread(){
        return this._dataThread;
    }
    public void setDataThread(Daten dataThread){
        this._dataThread = dataThread;
    }

    public boolean IsFinished(){
        return this._finished;
    }
    public void setFinished(boolean finished){
        this._finished = finished;
    }

    //ctors
    public ThreadsServer(){this(null, false);}
    public ThreadsServer(Daten dataThread, boolean finished){
        this.setDataThread(dataThread);
        this.setFinished(finished);
    }


    public void run() {
        String pathTS =  "C:/Users/timst_hsvelxb/Desktop/Projekt/Year 3/Intelij/Minigames_Sepata/src/Config.txt";
        HashMap<String,String> hashTS;
        hashTS = readSetup(pathTS);
        ServerSocket server;
        DataInputStream dis;
        Socket client;
        try {
            server = new ServerSocket(Integer.parseInt(hashTS.get("portThread")));
            client = server.accept();
             dis = new DataInputStream(client.getInputStream());

        } catch (IOException e) {
            System.out.println("Es trat ein Fehler beim Verbinden auf! Error: ThreadsServer");
            System.out.println(e.getMessage());
            dis=null;
            client = null;
        }
        do {
            try {

                String u = dis.readUTF();

                if (u.length() > 0) {
                    this._dataThread.setData(u);
                }



            } catch (IOException e) {
                System.out.println("Es trat ein Fehler beim Lesen auf!");
                System.out.println(e.getMessage());
                _finished = true;
            }
        }while(!this._finished);

        try {
            client.close();
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static HashMap<String, String> readSetup(String p){
        Path path = Path.of(p);
        HashMap<String,String> hoho = new HashMap<>();
        if(Files.exists(path)){
            try{
                List<String> daten = Files.readAllLines(path);
                for(String d : daten){
                    String[] value = d.split("=");
                    hoho.put(value[0], value[1]);
                }
                return hoho;
            }catch(IOException e){
                System.out.println("Error accured");
            }
        }else{
            System.out.println("Error 404");
        }
        return null;
    }
}
