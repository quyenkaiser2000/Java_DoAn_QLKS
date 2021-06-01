package sample.model;


public class NhanVien {
    String manv, macv, tennv, ns,gt,dc,sdt;

    public NhanVien(String manv, String macv, String tennv, String ns, String gt, String dc, String sdt) {
        this.manv = manv;
        this.macv = macv;
        this.tennv = tennv;
        this.ns = ns;
        this.gt = gt;
        this.dc = dc;
        this.sdt = sdt;
    }

    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

    public String getMacv() {
        return macv;
    }

    public void setMacv(String macv) {
        this.macv = macv;
    }

    public String getTennv() {
        return tennv;
    }

    public void setTennv(String tennv) {
        this.tennv = tennv;
    }

    public String getNs() {
        return ns;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getGt() {
        return gt;
    }

    public void setGt(String gt) {
        this.gt = gt;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}

