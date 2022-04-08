package Threads;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
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
        System.out.println("Thread: Server gestartet!");
        ServerSocket server;
        DataInputStream dis;
        Socket client;
        try {
            server = new ServerSocket(11000);
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
                    System.out.printf("Thread: Server --> %s",u);
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
