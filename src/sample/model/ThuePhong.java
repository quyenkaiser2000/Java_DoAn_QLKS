package sample.model;

public class ThuePhong {
    String matp,manv,tenkh,ngaylap;

    public ThuePhong(String matp, String manv, String tenkh, String ngaylap) {
        this.matp = matp;
        this.manv = manv;
        this.tenkh = tenkh;
        this.ngaylap = ngaylap;
    }

    public String getMatp() {
        return matp;
    }

    public void setMatp(String matp) {
        this.matp = matp;
    }

    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

    public String getTenkh() {
        return tenkh;
    }

    public void setTenkh(String tenkh) {
        this.tenkh = tenkh;
    }

    public String getNgaylap() {
        return ngaylap;
    }

    public void setNgaylap(String ngaylap) {
        this.ngaylap = ngaylap;
    }
}
