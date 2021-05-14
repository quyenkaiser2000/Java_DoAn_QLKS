package sample.model;

public class DichVu {
    String madv, tendv, dvt, dg;

    public DichVu(String madv, String tendv, String dvt, String dg) {
        this.madv = madv;
        this.tendv = tendv;
        this.dvt = dvt;
        this.dg = dg;
    }

    public String getMadv() {
        return madv;
    }

    public void setMadv(String madv) {
        this.madv = madv;
    }

    public String getTendv() {
        return tendv;
    }

    public void setTendv(String tendv) {
        this.tendv = tendv;
    }

    public String getDvt() {
        return dvt;
    }

    public void setDvt(String dvt) {
        this.dvt = dvt;
    }

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }
}

