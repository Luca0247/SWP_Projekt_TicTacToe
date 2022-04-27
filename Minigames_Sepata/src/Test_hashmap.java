import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Models.User;


public class Test_hashmap {



    public static void main(String[] args) {
        String path = "C:/Users/timst_hsvelxb/Desktop/Projekt/Year 3/Intelij/Minigames_Sepata/src/Anleitung.txt";
        HashMap<String,String> hash = new HashMap<>();
        HashMap<String,String> hashC = new HashMap<>();
        String g = readFile(path);
        hash.put("Erklärung", g);
        System.out.println("Wie funktioniert das Spiel:\n "+ hash.get("Erklärung"));

        String pa1 = "C:/Users/timst_hsvelxb/Desktop/Projekt/Year 3/Intelij/Minigames_Sepata/src/Setup.txt";
        String pa2 =  "C:/Users/timst_hsvelxb/Desktop/Projekt/Year 3/Intelij/Minigames_Sepata/src/Config.txt";

        hash = readSetup(pa1);
        hashC = readSetup(pa2);
        System.out.println( hash.get("url"));
        System.out.println( hash.get("db"));
        System.out.println( hash.get("pwd"));
        System.out.println( hash.get("user"));
        System.out.println( hashC.get("ipHost"));
        System.out.println( hashC.get("portServer"));
        System.out.println( hashC.get("portThread"));








       // "\n[" + time.getHour() + time.getMinute() + time.getSecond() + "]"  +
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
    


}
