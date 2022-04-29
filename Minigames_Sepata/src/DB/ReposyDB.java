package DB;

import Models.*;
import java.sql.*;


public class ReposyDB implements Reposy {

    private String url = "jdbc:mysql://localhost:3307/zWaiGamesDB";
    private String user = "root";
    private String pwd = "nij765G7";
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
        PreparedStatement pStmt = this._connection.prepareStatement("select * from user where username = ? and pw = ?");

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


}


