package Models;

public class VIP extends User{

    private int _score;

    public int getScore() {
        return _score;
    }

    public void setScore(int score) {
        this._score = score;
    }

    public VIP() {
        this(0, "", "", "", 0);
    }

    public VIP(int id, String un, String pw, String IP, int score) {
        super(id, un, pw, IP);
        this._score = score;
    }

}
