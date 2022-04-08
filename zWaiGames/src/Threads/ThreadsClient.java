package Threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
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
        System.out.println("Thread: Client gestartet!");
        DataInputStream dis;
        Socket s;
        try {
           s = new Socket("localhost", 10000);
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
}

