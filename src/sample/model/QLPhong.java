package sample.model;

public class QLPhong {
    String map, tlp, dg, ttp;

    public QLPhong(String map, String tlp, String dg, String ttp) {
        this.map = map;
        this.tlp = tlp;
        this.dg = dg;
        this.ttp = ttp;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getTlp() {
        return tlp;
    }

    public void setTlp(String tlp) {
        this.tlp = tlp;
    }

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }

    public String getTtp() {
        return ttp;
    }

    public void setTtp(String ttp) {
        this.ttp = ttp;
    }

}

