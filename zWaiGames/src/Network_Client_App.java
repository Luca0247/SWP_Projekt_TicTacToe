import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import Threads.*;

public class Network_Client_App {

    private static Scanner reader = new Scanner(System.in);

    public static void main(String[] args) {

        String input;


        try {

            Socket s = new Socket("localhost", 11000);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            Daten d2 = new Daten();
            /*
            Threads t = new Threads(d2, false);
            t.start();

             */

            do{

                System.out.print("Ihr Text (beenden mit END): ");
                input = reader.nextLine();
                reader = new Scanner(System.in);

                if(!input.toUpperCase().equals("END")){
                    dos.writeUTF(input);
                    dos.flush();
                }


            }while(!input.toUpperCase().equals("END"));


            dos.close();
            s.close();


        } catch (IOException e) {
            System.out.println("Es trat ein Fehler beim Client auf!");
            System.out.println(e.getMessage());
        }

    }

}
