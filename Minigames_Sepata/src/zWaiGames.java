import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    static char[][] tictactoe = new char[3][3];

    public static void main(String[] args) throws IOException {
        String pathC =  "C:/Users/timst_hsvelxb/Desktop/Projekt/Year 3/Intelij/Minigames_Sepata/src/Config.txt";
        String pathA = "C:/Users/timst_hsvelxb/Desktop/Projekt/Year 3/Intelij/Minigames_Sepata/src/Anleitung.txt";
        HashMap<String,String> hashC = new HashMap<>();
        hashC = readSetup(pathC);
        char end;

        User u = new User();
        boolean login = true;
        int choice;
        do {
            System.out.printf("\n\n\n\n\n%50s\n", ANSI_RED +"Anmeldung/Regristration" + ANSI_RESET);
            System.out.printf("%43s", "=========================\n");
            do {
                System.out.println("\n0 ... Anmeldung");
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

        do {
            String w = readFile(pathA);
            hashC.put("Erklärung", w);
            System.out.println("Wie funktioniert das Spiel:\n " + hashC.get("Erklärung"));

            System.out.println("Host oder Client: ");
            char choiceHost = reader.next().toLowerCase().charAt(0);

            if (choiceHost == 'h') {
                AreUHot = true;
                ServerSocket server = new ServerSocket(Integer.parseInt(hashC.get("portServer")));
                client = server.accept();
                dos = new DataOutputStream(client.getOutputStream());
                data = new Daten();
                ThreadsServer s = new ThreadsServer(data, false);
                s.start();

                TicTacToe();

            } else if (choiceHost == 'c') {
                Reposy db = null;
                String ip = null;
                try {
                    do {
                        db = new ReposyDB();
                        db.open();
                        System.out.println("Spielpartner: ");
                        String playpartner = reader.next();
                        ip = db.getIP(playpartner);
                    } while (ip == null);
                } catch (ClassNotFoundException | SQLException e) {
                    System.out.println("Hoho");
                }

                s = new Socket(ip, Integer.parseInt(hashC.get("portThread")));
                dos = new DataOutputStream(s.getOutputStream());
                data = new Daten();
                ThreadsClient c = new ThreadsClient(data, false);
                c.start();

                TicTacToe();

            } else {
                System.out.println("Falscher Buchstabe!");
            }

            System.out.println("Weiterspielen?");
            end = reader.next().toLowerCase().charAt(0);
        }while(end != 'e');


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
        Path path = Path.of("C:/Users/timst_hsvelxb/Desktop/Projekt/Year 3/Intelij/Minigames_Sepata/src/Log.txt");


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
            System.out.println("Sie haben eine falsche Zahl/Zeichen eingegben.\n" +
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
        GameStatus gameStatus = GameStatus.SpielNochNichtFertig;

        char player = TTTconvertPLayerIcon();
        char player2 = player == 'x' ? 'o' : 'x';
        System.out.println(player2);

        boolean youPlay = AreUHot; // Ob du host bisch oda net???


        feldBelegen();


        do {
            if(youPlay) {
                gameStatus = YouPlay(player);
                youPlay = false;
            }
            else {
                gameStatus = OtherOnePlay(player == 'x' ? 'o' : 'x');
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
        boolean again;
        int row, column;
        do {
            again = false;
            tictactoeAnzeigen();

            // Daten die man weitergeben muss!!!!
            row = eingabe("Zeile [1-3]: ");
            column = eingabe("Spalte [1-3]: ");


            // nur wenn das gewählte Feld frei ist wird es belegt
            if (tictactoe[row][column] == '-') {

                transferInput(row, column);

                tictactoe[row][column] = player;

                //überprüfen, ob das Spiel fertig ist
                return checkIfGameIsOver(player);

            } else {
                System.out.println("Das Feld war belegt. Bitte erneut eingeben");
                again=true;
            }
        }while(again);
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
            value = reader.nextInt();
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
            if( checkIfRowFull(i, player)) {
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
        if ((tictactoe[0][0] == player) && (tictactoe[1][1] == player) && (tictactoe[3][3] == player)){
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


        dos.close();
        client.close();
        s.close();


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
        do {
            d = data.getData();
        }while(d.equals(d));
        return d;
    }



}
