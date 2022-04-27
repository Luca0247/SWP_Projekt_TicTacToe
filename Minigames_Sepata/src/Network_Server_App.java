import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Scanner;
import Threads.*;

public class Network_Server_App {


    private static Scanner reader = new Scanner(System.in);
    public static void main(String[] args) {
        String input = null;


        Daten d2 = new Daten();
        ThreadsServer t = new ThreadsServer(d2, false);
        t.start();


        try {

            ServerSocket server = new ServerSocket(10000);

            System.out.println("SERVER gestartet");

            System.out.println("Server-Port: " + server.getLocalPort());
            System.out.println("Socket-Adresse: " + server.getLocalSocketAddress());

            Socket client = server.accept();

            DataOutputStream dos = new DataOutputStream(client.getOutputStream());

            System.out.println("Threads.Daten vom Client\n");
            while(client.isConnected() || !input.toUpperCase().equals("END")){

                System.out.print("Ihr Text (beenden mit END): ");
                input = reader.nextLine();
                reader = new Scanner(System.in);

                if(!input.toUpperCase().equals("END")){
                    dos.writeUTF(input);
                    dos.flush();
                }
            }



            dos.close();
            client.close();

        }
        catch (IOException e) {
            System.out.println("Es trat ein Problem auf dem Server auf!");
        }

    }

}
