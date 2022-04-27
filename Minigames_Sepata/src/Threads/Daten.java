package Threads;

public class Daten {

    //fields
    private String _data;

    public String getData() {
        return _data;
    }
    public void setData(String _data) {
        this._data = _data;
    }

    public Daten(){this(" ");}
    public Daten(String data){
        this.setData(data);
    }

    @Override
    public String toString(){
        String s = this.getData() + "\n";

        return s;
    }
}
