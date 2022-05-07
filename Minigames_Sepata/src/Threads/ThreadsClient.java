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
import DB.*;

public class ThreadsClient extends Thread {

    private Daten _dataClient;
    private boolean _finished;
    private String _ip;
    private int _port;

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

    public String getIPC(){
        return this._ip;
    }

    public void setIPC(String ip){
        this._ip=ip;
    }
    public int getPort(){
        return this._port;
    }
    public void setPort(int port){
        this._port=port;
    }

    //ctors
    public ThreadsClient(){this(null, false, " ", 0);}
    public ThreadsClient(Daten dataClient, boolean finished, String ipc, int port){
        this.setDataThread(dataClient);
        this.setFinished(finished);
        this.setIPC(ipc);
        this.setPort(port);
    }

    public void run() {
        DataInputStream dis;
        Socket s;
        System.out.println("Thread: Client verbunden");
        try {
            s = new Socket(_ip, getPort());
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