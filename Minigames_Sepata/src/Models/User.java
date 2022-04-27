package Models;

public class User {

    private int _userId;
    private String _username;
    private String _password;
    private String _IP;


    public int getUserId() {
        return _userId;
    }
    public void setUserId(int userId) {
        this._userId = userId;
    }
    public String getUsername() {
        return _username;
    }
    public void setUsername(String un) {
        this._username = un;
    }
    public String getPassword() {
        return _password;
    }
    public void setPassword(String pw) {
        this._password = pw;
    }
    public String getIP(){
        return _IP;
    }
    public void setIP(String IP){
        this._IP=IP;
    }

    public User(){
        this(0,"","", "");
    }
    public User(int id, String un, String pw, String IP){
        this.setUserId(id);
        this.setUsername(un);
        this.setPassword(pw);
        this.setIP(IP);
    }

    @Override
    public String toString(){
        return this.getUserId() + " " + this.getUsername() + " " + this.getPassword() + " " + this.getPassword() + "\n";
    }
}
