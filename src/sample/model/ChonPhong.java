package sample.model;

public class ChonPhong {
    String maphong,tenlp,dg;

    public ChonPhong(String maphong, String tenlp, String dg) {
        this.maphong = maphong;
        this.tenlp = tenlp;
        this.dg = dg;
    }

    public String getMaphong() {
        return maphong;
    }

    public void setMaphong(String maphong) {
        this.maphong = maphong;
    }

    public String getTenlp() {
        return tenlp;
    }

    public void setTenlp(String tenlp) {
        this.tenlp = tenlp;
    }

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }
}

