import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.*;

import Models.*;
import Threads.*;
import DB.*;

public class zWaiGames {

    static Scanner reader = new Scanner(System.in);
    static String userPw;
    static boolean AreUHot = false;
    static Daten data = null;
    static DataOutputStream dos;
    static Socket client;
    static Socket s;
    static char[][] tictactoe = new char[3][3];
    static String oldReadValue=" ";
    static Clock clock = Clock.systemDefaultZone();
    static long milliSeconds=clock.millis();

    public static void main(String[] args) throws IOException {
        String pathA = "C:/Users/lucas/Desktop/Minigames_Sepata/src/Anleitung.txt";
        HashMap<String,String> hashC = null;


        User u = new User();
        boolean login = true;
        int choice;
        do {
            System.out.printf("\n\n%50s\n", "Anmeldung/Regristration" );
            System.out.printf("%52s",  "=========================\n" );
            do {
                System.out.println("0 ... Anmeldung");
                System.out.println("1 ... Regristration");
                System.out.print("Ihre Wahl: ");
                String input = reader.nextLine();

                choice = checkIfInputIsRightInt(input, 1,0);
            }while(choice == -1);

            switch (choice) {
                case 0:
                    login(u);
                    break;
                case 1:
                    register(u);
                    break;
                default:
                    System.out.println("Sie haben eine falsche Taste gedrückt");
                    login = false;
            }
        } while (!login);

        fillUserIDs(u);
        int hostPort = u.getUserId()+10000+1, threadPort = u.getUserId()+15000+1;
        try{
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n\n\n\n\n\n\n\n\n===================================================================");
        System.out.println("==============!!!Herzlich Willkommen bei TicTacToe!!!==============");
        System.out.println("===================================================================\n");

        System.out.println("Wissen sie wie TicTacToe funktioniert? [j/n]:");
        while(reader.next().toLowerCase().charAt(0) == 'n') {
            String w = readFile(pathA);
            hashC.put("Erklärung", w);
            System.out.println("\nSo funktioniert das Spiel:\n" + hashC.get("Erklärung"));
            System.out.print("Ok? [j/n]: ");
        }

        System.out.println("\n\n\n\nHost oder Client?");
        System.out.println("==================");
        System.out.println("Nun können sie auswählen, ob sie der Host oder der Client sind? [h/c]: ");
        char choiceHost= reader.next().toLowerCase().charAt(0);
        ServerSocket server = null;
       try {
           while ((choiceHost != 'h') && (choiceHost != 'c')) {
               System.out.println("Sie haben leider ein falsches Zeichen eingegeben.");
               System.out.println("\nHost oder Client?");
               System.out.println("==================");
               System.out.println("Nun können sie auswählen, ob sie der Host oder der Client sind? [h/c]: ");
               choiceHost = reader.next().toLowerCase().charAt(0);
           }


           if (choiceHost == 'h') {

               try {
                   AreUHot = true;
                   server = new ServerSocket(hostPort);
                   System.out.println("SERVER gestartet");
                   System.out.println("Server-Port: " + server.getLocalPort());
                   System.out.println("Socket-Adresse: " + server.getLocalSocketAddress());
                   data = new Daten();
                   ThreadsServer ss = new ThreadsServer(data, false, threadPort);
                   ss.start();
                   client = server.accept();
                   dos = new DataOutputStream(client.getOutputStream());

               } catch (Exception e) {
                   System.out.println("Fehler!");
                   System.out.println(e.getMessage());
               }
           } else if (choiceHost == 'c') {
               Reposy db;
               String ip = null;
               try {
                   do {
                       db = new ReposyDB();
                       db.open();
                       System.out.println("Spielpartner: ");
                       String playpartner = reader.next();
                       ip = db.getIP(playpartner);
                       System.out.println(threadPort);
                       System.out.println(hostPort);
                   } while (ip == null);

                   s = new Socket(ip, threadPort);
                   dos = new DataOutputStream(s.getOutputStream());
                   data = new Daten();
                   ThreadsClient c = new ThreadsClient(data, false, ip, hostPort);
                   c.start();
               } catch (ClassNotFoundException | SQLException e) {
                   System.out.println("Hoho");
               }

           } else {
               System.out.println("False");
           }

           do {
               TicTacToe();
               System.out.println("Wollen sie noch eine weitere Runde spielen [j/n]: ");
           } while (reader.next().toLowerCase().charAt(0) == 'j');
       }
       catch(Exception e){
           System.out.println(e.getMessage());
        }
       finally{
           dos.close();

           if(choiceHost == 'h'){
               client.close();
               server.close();
           }else{
               s.close();
           }
       }

        System.out.println("Sie haben das TicTacToe beendet. Kommen sie bald wieder.");
    }

    private static String readFile(String path){
        Path p = Path.of(path);

        if(Files.exists(p)){
            try {
                return Files.readString(p);
            }
            catch(IOException e){
                System.out.println("Error accured");
            }
        }
        else{
            System.out.println("Error 404");
        }
        return null;
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
    private static void writeLog(User p1, User p2, User winner){
        Path path = Path.of("C:/Users/lucas/Desktop/SWP_Project/SWP_Projekt_TicTacToe/Minigames_Sepata/src/Log.txt");


        if(Files.exists(path)){

            try{
                SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy | HH:mm:ss");
                String timeStamp = date.format(new Date());
                String old = Files.readString(path);
                String newText = old + "\n["+ timeStamp + "]" +" Spiel: " + p1.getUsername() + " vs " + p2.getUsername() + "--> Winner: " + winner.getUsername();
                Files.writeString(path, newText);
            } catch (IOException e) {
                System.out.println("Error accured");
            }

        }else{
            System.out.println("Error 404");
        }
    }

    public static void goOnWithEnter(){
        try {
            reader = new Scanner (System.in);
            System.in.read();
            reader = new Scanner (System.in);
        }
        catch (IOException e){
            System.out.print("Ein Programmfelher ist aufgetreten." +
                    "\n Bitte geben sie erneut ENTER ein: ");
        }
    }
    public static int checkIfInputIsRightInt(String input, int highestPosibilty, int lowestPosibilty){
        reader = new Scanner(System.in);
        try {
            int result = Integer.parseInt(input);
            if(result <= highestPosibilty && result >=lowestPosibilty){
                return result;
            }
            else{
                System.out.println("Sie haben eine zu große/kleine Zahl eingegeben.\n" +
                        "Bitte versuchen sie es erneut.\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("Sie haben ein falsche Zeichen eingegeben.\n" +
                    "Bitte versuchen sie es erneut.\n");
            return -1;
        }
        return -1;
    }
    public static double checkIfInputIsRightDouble(String input, double highestPosibilty, double lowestPosibilty){
        reader = new Scanner(System.in);
        try {
            double result = Double.parseDouble(input);
            if(result <= highestPosibilty && result >=lowestPosibilty){
                return result;
            }
            else{
                System.out.println("Sie haben eine zu große/kleine Zahl eingegeben.\n" +
                        "Bitte versuchen sie es erneut.\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("Sie haben eine falsche Zahl/Zeichen eingegben.\n" +
                    "Bitte versuchen sie es erneut.\n");
            return -1;
        }
        return -1;
    }

    public static boolean login (User u){
        Reposy rep = null;
        try {
            rep = new ReposyDB();
            rep.open();
            System.out.println("\n\nAnmeldung:");
            System.out.println("==========");
            System.out.print("Benutzername: ");
            String username = reader.next();
            System.out.print("Passwort: ");
            String password = reader.next();
            userPw = password;

            if (rep.login(username, password)) {
                System.out.println("Sie sind eingelogt.");
                u.setUsername(username);
                u.setPassword(password);
                return true;
            }
            else{
                System.out.println("Der Benutzername oder das Passwort war falsch.\n" +
                        "Bitte versuchen sie es nocheinmal.");
                return login(u);
            }

        }
        catch (ClassNotFoundException e) {
            // System.out.println("MySQL-Treiber konnte nicht geladen werden!");

        }
        catch (SQLException e) {
            //System.out.println("Datenbankfehler!");
        }
        finally {
            try {
                rep.close();
            } catch (SQLException e) {
                //System.out.println("DB-Verbindung konnte nicht beendet werden");
            }

        }
        return false;
    }
    public static void register(User u) {
        Reposy rep = null;
        try {
            rep = new ReposyDB();
            rep.open();
            System.out.println("\n\nRegristration:");
            System.out.print("==============");
            if (rep.register(generateNewUser(u))) {
                System.out.println("Sie wurden erfolgreich regristriert");
            }
        } catch (ClassNotFoundException e) {
            //System.out.println("MySQL-Treiber konnte nicht geladen werden!");

        } catch (SQLException e) {
            // System.out.println("Datenbankfehler!");
        }

        finally{
            try {
                rep.close();
            } catch (SQLException e) {
                // System.out.println("DB-Verbindung konnte nicht beendet werden");
            }

        }
    }
    public static User generateNewUser (User u) {
        String un;
        do {
            System.out.print("\nGeben sie ihren Benutzername ein: ");
            un = reader.next();
        }while(checkIfUsernameAlreadyExists(un));
        u.setUsername(un);
        System.out.print("Geben sie ihr Passwort ein: ");
        u.setPassword(reader.next());
        System.out.print("Geben sie ihre IP ein [CMD --> ipconfig]:  ");
        u.setIP(reader.next());
        return u;
    }
    public static boolean checkIfUsernameAlreadyExists(String un){
        Reposy rep = null;
        try {
            rep = new ReposyDB();
            rep.open();

            if (rep.checkIfUserExists(un)){
                System.out.println("Der Benutzername ist leider schon vergeben.");
                return true;
            }
        }
        catch (ClassNotFoundException e) {
            //System.out.println("MySQL-Treiber konnte nicht geladen werden!");

        }
        catch (SQLException e) {
            //System.out.println("Datenbankfehler!");
        }
        finally {
            try {
                rep.close();
            }
            catch(SQLException e){
                // System.out.println("DB-Verbindung konnte nicht beendet werden");
            }
        }
        return false;
    }


    public static void TicTacToe() throws IOException {


        GameStatus gameStatus;
        char player = TTTconvertPLayerIcon();
        boolean youPlay = AreUHot; // Ob du host bisch oda net???

        feldBelegen();

        do {
            if(youPlay) {
                gameStatus = YouPlay(player);
                youPlay = false;
            }
            else {
                gameStatus = OtherOnePlay(player == 'x' ? 'o' : 'x');
                youPlay = true;
            }
        }while(gameStatus == GameStatus.SpielNochNichtFertig);

        tictactoeAnzeigen();
        showGameStatus(gameStatus);
    }

    public static char TTTconvertPLayerIcon(){
        char icon;
        do {
            System.out.println("Mit welchem Zeichen wollen sie spielen [x|o]: ");
            icon = reader.next().toLowerCase().charAt(0);
        }while((icon != 'x') && (icon != 'o'));
        return icon;
    }

    public static GameStatus YouPlay(char player) throws IOException {
        boolean firstRun=true;
        int row, column;
        do {


            // Daten die man weitergeben muss!!!!
            if(firstRun) {
                System.out.println("Ihr Spielzug: ");
                System.out.println("==============");
            }
            tictactoeAnzeigen();
            System.out.printf("Setzen sie ihr Icon %c: \n", player);
            row = eingabe("Zeile: ");
            column = eingabe("Spalte: ");




            // nur wenn das gewählte Feld frei ist wird es belegt
            if (tictactoe[row][column] == '-') {

                tictactoe[row][column] = player;
                transferInput(row, column);

                //überprüfen, ob das Spiel fertig ist
                return checkIfGameIsOver(player);

            }
            else {
                System.out.println("Ihre Eingabe war nicht korrekt! \n" +
                        "Sie können kein Feld belegen, das schon besetzt wurde." +
                        "\nVersuchen sie es nocheinmal. ");
                firstRun=false;
            }
        }while(tictactoe[row][column] != '-');
        tictactoeAnzeigen();
        return GameStatus.SpielNochNichtFertig;

    }
    public static GameStatus OtherOnePlay(char player){
        String inputPlayer2;
        int row, column;
        inputPlayer2 = readOtherPlayer();

        String[] result = inputPlayer2.split(";");


        row = Integer.parseInt(result[0]);
        column = Integer.parseInt(result[1]);

        tictactoe[row][column] = player;

        return checkIfGameIsOver(player);
    }

    public static void feldBelegen(){
        // bei einem 2dim. Array benötigten wir mormalerweise 2 Schleifen
        // alle Zeilen durchlaufen
        for(int i = 0; i<3; i++) {
            // alle Spalten durchlaufen
            for (int j = 0; j < 3; j++) {
                // Startwert jedem Feld zuweisen
                tictactoe[i][j] = '-';
            }
        }
    }
    public static void tictactoeAnzeigen(){
        System.out.println("\n");
        for(int i = 0; i<3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.printf("%5c", tictactoe[i][j]);
            }
            System.out.println();
        }
    }

    public static int eingabe(String text){
        int value;

        do {
            System.out.print(text);
            value= checkIfInputIsRightInt(reader.next(), 3, 0);
        }while((value<1) || (value>3));
        // da Arrays immer bei 0 beginnen, müssen wir bei der Eingabe
        //      1 abziehen (der Benutzer gibt z.B. 1 ein ->
        //      wir benötigen dann 0)
        return value-1;
    }

    public static GameStatus checkIfGameIsOver(char player){
        GameStatus playerStatus = player == 'x' ? GameStatus.Spieler1HatGewonnen : GameStatus.Spieler2HatGewonnen;
        // 3 gleiche Zeichen in einer der 3 Zeilen
        for(int i=0; i<3;i++){
            if( checkIfRowFull(i, player)) {
                return playerStatus;
            }
        }
        // 3 gleiche zeichen in einer der 3 Spalten
        for(int i=0; i<3;i++){
            if( checkIfColumnFull(i, player)) {
                return playerStatus;
            }
        }
        // 3 Zeichen in einer Diagonale
        if(checkIfDiagFull(player)){
            return playerStatus;
        }

        // alle Felder sind belegt
        if(checkIfAllFieldsAreOccupied()){
            return GameStatus.KeinerHatGewonnen;
        }
        return GameStatus.SpielNochNichtFertig;
    }
    public static boolean checkIfRowFull(int row, char player) {
        if ((tictactoe[row][0] == player) && (tictactoe[row][1] == player) && (tictactoe[row][2] == player)) {
            return true;
        }
        return false;
    }
    public static boolean checkIfColumnFull(int col, char player) {
        if ((tictactoe[0][col] == player) && (tictactoe[1][col] == player) && (tictactoe[2][col] == player)) {
            return true;
        }
        return false;
    }
    public static boolean checkIfDiagFull(char player) {
        // Diag. von li. oben nach re. unten
        if ((tictactoe[0][0] == player) && (tictactoe[1][1] == player) && (tictactoe[2][2] == player)){
            return true;
        }
        // diag. von re. oben nach li. unten
        if ((tictactoe[0][2] == player) && (tictactoe[1][1] == player) && (tictactoe[2][0] == player)){
            return true;
        }
        return false;
    }
    public static boolean checkIfAllFieldsAreOccupied(){
        // zumindest ein Feld ist noch frei
        for(int row=0; row<3; row++){
            for(int col=0; col<3; col++){
                if(tictactoe[row][col] == '-'){
                    return false;
                }
            }
        }
        return true;

    }
    public static void showGameStatus(GameStatus gameStatus) throws IOException {
        if(gameStatus == GameStatus.Spieler1HatGewonnen){
            System.out.println("Spieler 1 hat gewonnen!");
        }
        else if(gameStatus == GameStatus.Spieler2HatGewonnen){
            System.out.println("Spieler 2 hat gewonnen!");
        }
        else if (gameStatus == GameStatus.KeinerHatGewonnen){
            System.out.println("Keiner hat gewonnen!");
        }
    }

    public static void transferInput(int row, int column) throws IOException {
        String output = row + ";" + column;
        TTTWrite(output);

    }
    public static void TTTWrite(String output) throws IOException {
        dos.writeUTF(output);
        dos.flush();
    }
    public static String readOtherPlayer(){
        String d;
        int points=0;
        boolean setPoints=true;



        System.out.print("Waiting for the other Player");
        do {
            d = data.getData();

            if (clock.millis() - milliSeconds > 500) {
                if (setPoints) {
                    System.out.print(".");
                    points++;
                    if(points % 3 == 0){
                        setPoints=false;
                    }
                }
                else {
                    System.out.print("\b\b\b");
                    setPoints=true;
                }
                milliSeconds = clock.millis();
            }
            System.out.print(" \b");
        }while((d.equals(oldReadValue)) || (Objects.equals(d, " ")));
        oldReadValue = d;
        return d;
    }

    public static void fillUserIDs(User u) {
        Reposy rep = null;
        try {
            rep = new ReposyDB();
            rep.open();
            u.setUserId(rep.getIdByUsername(u.getUsername()));
        }

        catch (ClassNotFoundException e) {
            // System.out.println("MySQL-Treiber konnte nicht geladen werden!");

        }
        catch (SQLException e) {
            //System.out.println("Datenbankfehler!");
        }
    }
}