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
    private int _port;

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

    public int getPort(){
        return this._port;
    }
    public void setPort(int port){
        this._port=port;
    }

    //ctors
    public ThreadsServer(){this(null, false, 0);}
    public ThreadsServer(Daten dataThread, boolean finished, int port){
        this.setDataThread(dataThread);
        this.setFinished(finished);
        this.setPort(port);
    }


    public void run() {
        ServerSocket server;
        DataInputStream dis;
        Socket client;
        try {
            server = new ServerSocket(getPort());
            client = server.accept();
            dis = new DataInputStream(client.getInputStream());
            System.out.printf("Thread: Port: %d", getPort());

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
}