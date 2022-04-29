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

public class ThreadsClient extends Thread {

    private Daten _dataClient;
    private boolean _finished;

    //getter/setter
    public Daten getDataThread(){
        return this._dataClient;
    }
    public void setDataThread(Daten dataClient){
        this._dataClient = dataClient;
    }

    public boolean IsFinished(){
        return this._finished;
    }
    public void setFinished(boolean finished){
        this._finished = finished;
    }

    //ctors
    public ThreadsClient(){this(null, false);}
    public ThreadsClient(Daten dataClient, boolean finished){
        this.setDataThread(dataClient);
        this.setFinished(finished);
    }

    public void run() {
        String pathTC =  "C:/Users/timst_hsvelxb/Desktop/Projekt/Year 3/Intelij/Minigames_Sepata/src/Config.txt";
        HashMap<String,String> hashTC;
        hashTC = readSetup(pathTC);
        DataInputStream dis;
        Socket s;
        System.out.println("Thread: Client verbunden");
        try {
           s = new Socket("localhost", Integer.parseInt(hashTC.get("portServer")));
            dis = new DataInputStream(s.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Es trat ein Fehler beim Verbinden auf! Error: ThreadsClient");
            s = null;
            dis = null;
        }
        do{
            try {

                String u = dis.readUTF();

                if (u.length() > 0) {
                    this._dataClient.setData(u);
                    System.out.printf("Thread: Client --> %s", u);
                }

            } catch (IOException e) {
                System.out.println("Es trat ein Fehler beim Lesen auf!");
                System.out.println(e.getMessage());
                _finished = true;
            }
        }while(!this._finished);
        try {
            s.close();
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

