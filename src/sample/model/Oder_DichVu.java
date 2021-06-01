package sample.model;

public class Oder_DichVu {
    String tendv,dvt,sl,dg;

    public Oder_DichVu(String tendv, String dvt, String sl, String dg) {
        this.tendv = tendv;
        this.dvt = dvt;
        this.sl = sl;
        this.dg = dg;
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

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }
}
