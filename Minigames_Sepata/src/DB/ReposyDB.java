package DB;

import Models.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.HashMap;
import java.util.List;


public class ReposyDB implements VIPReposy {

    private String pathS = "C:/Users/lucas/Desktop/Minigames_Sepata/src/Setup.txt";
    private String url = getInfo("url", pathS) + getInfo("db", pathS);
    private String user = getInfo("user", pathS);
    private String pwd = getInfo("pwd", pathS);
    private Connection _connection;



    public ReposyDB() throws ClassNotFoundException {
        Class<?> c = Class.forName("com.mysql.cj.jdbc.Driver");
        if(c != null){
             System.out.println("MySQL-Treiber wurde geladen!");
        }
    }

    @Override
    public void open() throws SQLException {
        this._connection = DriverManager.getConnection(url, user, pwd);
    }

    @Override
    public void close() throws SQLException {
        if((this._connection != null) && (!this._connection.isClosed())){
            this._connection.close();
            // System.out.println("Verbindung wurde geschlossen\n");
        }

    }

    @Override
    public User getUserByName(String username) throws SQLException {
        PreparedStatement pStmt = this._connection.prepareStatement("select * from user where username = ?");
        pStmt.setString(1,username);
        ResultSet result = pStmt.executeQuery();

        if(result.next()){

            User u = new User();
            u.setUserId(result.getInt("user_id"));
            u.setUsername(result.getString("username"));
            u.setPassword(result.getString("password"));
            u.setIP(result.getString("IP"));


            return u;
        }
        else{
            return null;
        }
    }

    @Override
    public boolean register(User user) throws SQLException {
        PreparedStatement pStmt = this._connection.prepareStatement("insert into user values(null, ?, ?, ?)");

        pStmt.setString(1, user.getUsername());
        pStmt.setString(2, user.getPassword());
        pStmt.setString(3, user.getIP());




        return pStmt.executeUpdate() == 1;
    }

    @Override
    public boolean checkIfUserExists(String username) throws SQLException {
        PreparedStatement pStmt = this._connection.prepareStatement("select * from user where username = ?");
        pStmt.setString(1, username);
        ResultSet result = pStmt.executeQuery();

        return result.next();
    }

    @Override
    public boolean login(String username, String password) throws SQLException {
        PreparedStatement pStmt = this._connection.prepareStatement("select * from user where username = ? and password = ?");

        pStmt.setString(1, username);
        pStmt.setString(2, password);

        ResultSet result = pStmt.executeQuery();


        return result.next();
    }

    @Override
    public String getIP(String name) throws SQLException {
        PreparedStatement pStmt = this._connection.prepareStatement("select IP from user where username = ?");

        pStmt.setString(1, name);

        ResultSet result = pStmt.executeQuery();

        if(result.next()){


            return result.getString("IP");
        }
        else{
            return  null;
        }
    }

    @Override
    public boolean insertFriends(User user) throws SQLException {
        PreparedStatement pStmt = this._connection.prepareStatement("insert into user values(null, ?, null, ?)");

        pStmt.setString(1, user.getUsername());
        pStmt.setString(2, user.getIP());


        return pStmt.executeUpdate() == 1;
    }

    @Override
    public int getIdByUsername(String name) throws SQLException {
        PreparedStatement pStmt = this._connection.prepareStatement("select user_id from user where username = ?");

        pStmt.setString(1, name);

        ResultSet result = pStmt.executeQuery();

        if(result.next()){


            return result.getInt("user_id");
        }
        else{
            return 0;
        }
    }

    @Override
    public void updateScore(String name, int score) throws SQLException {
        PreparedStatement pStmt = this._connection.prepareStatement("update VIP set score = ? where vipID= ?");
        pStmt.setInt(1, score);
        pStmt.setInt(2, getIdByUsername(name));
    }

    @Override
    public int getScore(String name) throws SQLException {
        PreparedStatement pStmt = this._connection.prepareStatement("select score from user where vipID = ?");

        pStmt.setInt(1, getIdByUsername(name));

        ResultSet result = pStmt.executeQuery();
        if(result.next()){
            return result.getInt("score");
        }
        else{
            return 0;
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

    private static String getInfo(String key, String path){
        String value;
        HashMap<String, String> hashA = readSetup(path);
        value = hashA.get(key);

        return value;
    }


    // VIPs

}


