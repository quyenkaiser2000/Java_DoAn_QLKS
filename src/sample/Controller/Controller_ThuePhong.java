package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Dialog.AlertDialog;
import sample.model.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import sample.model.Namelogin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller_ThuePhong implements Initializable {

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<ThuePhong> data;
    private String GioiTinh;
    private ObservableList<Oder_DichVu> data1;

    @FXML
    private AnchorPane anchorpane;

    @FXML
    private TableView<ThuePhong> table_infor;

    @FXML
    private TableColumn<?, ?> col_matp;

    @FXML
    private TableColumn<?, ?> col_manv;

    @FXML
    private TableColumn<?, ?> col_tenkh;

    @FXML
    private TableColumn<?, ?> col_ngaylap;

    @FXML
    private TextField txt_tenkh;

    @FXML
    private TextField txt_cmndkh;

    @FXML
    private CheckBox cb_nam;

    @FXML
    private CheckBox cb_nu;

    @FXML
    private DatePicker date_nskh;

    @FXML
    private TextField txt_dckh;

    @FXML
    private TextField txt_sdtkh;

    @FXML
    private TextField txt_makh;
    @FXML
    private TextField txt_maphong;
    @FXML
    private Button btn_addDV;
    @FXML
    private TextField txt_matp;

    @FXML
    private Label lable_test;
    @FXML
    private TextField txt_mnv;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = sample.DBConnection.qlksConnection();
        data = FXCollections.observableArrayList();
        setCellTable();
        LoadDataTableView();
        try {
            autoMaKH();
            autoMaTP();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //loadCBCV();
        setcellvalueformtableview();
        //search_NhanVien();
    }

    private void setCellTable() {

        col_matp.setCellValueFactory(new PropertyValueFactory<>("matp"));
        col_manv.setCellValueFactory(new PropertyValueFactory<>("manv"));
        col_tenkh.setCellValueFactory(new PropertyValueFactory<>("tenkh"));
        col_ngaylap.setCellValueFactory(new PropertyValueFactory<>("ngaylap"));

    }
    private void setcellvalueformtableview() {
        table_infor.setOnMouseClicked(e -> {
            ThuePhong p1 = table_infor.getItems().get(table_infor.getSelectionModel().getFocusedIndex());
            txt_tenkh.setText(p1.getTenkh());
            txt_matp.setText(p1.getMatp());
            String tenkhachhang = txt_tenkh.getText();
            String makh= null;
            String gt = null;
            String cmnd = null;
            String dc = null;
            String dt = null;
            String sqlmakh = "SELECT * FROM KHACH_HANG WHERE TenKhachHang LIKE N'"+tenkhachhang+"'";
            try{
                pst = con.prepareStatement(sqlmakh);
                rs= pst.executeQuery();
                if(rs.next()) {
                    makh = rs.getString(1);
                    gt = rs.getString(3);
                    cmnd= rs.getString(4);
                    dc=rs.getString(5);
                    dt=rs.getString(6);
                    txt_makh.setText(makh);
                    txt_cmndkh.setText(cmnd);
                    if (gt.equals("Nam")) {
                        cb_nam.setSelected(true);
                        cb_nu.setSelected(false);
                    } else {
                        cb_nam.setSelected(false);
                        cb_nu.setSelected(true);
                    }
                    txt_dckh.setText(dc);
                    txt_sdtkh.setText(dt);

                }
            }catch (SQLException ex) {
                Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
            }
            String map = null;
            String sqlmap = "SELECT MaPhong FROM CHI_TIET_PHIEU_THUE_PHONG WHERE MaThuePhong LIKE N'"+txt_matp.getText()+"'";
            try{
                pst = con.prepareStatement(sqlmap);
                rs= pst.executeQuery();
                if(rs.next()) {
                    map = rs.getString(1);
                    txt_maphong.setText(map);

                }
            }catch (SQLException ex) {
                Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
            }
        });


    }
    private void LoadDataTableView() {
        try {
            pst = con.prepareStatement("SELECT MaThuePhong, MaNhanVien, TenKhachHang, FORMAT (NgayLap, 'dd/MM/yyyy') FROM PHIEU_THUE_PHONG A, KHACH_HANG B   WHERE A.MaKhachHang = B.MaKhachHang");
            rs = pst.executeQuery();
            while (rs.next()) {
                data.add(new ThuePhong(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));

            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_infor.setItems(data);

    }

    public void male(ActionEvent event) {
        GioiTinh = "Nam";
    }

    public void female(ActionEvent event) {
        GioiTinh = "Nữ";
    }

    public void handleExitThuePhong(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/trangchu.fxml"));
        Stage stage =   (Stage) anchorpane.getScene().getWindow();

        stage.close();
    }

    public void handleAddDV(ActionEvent event) throws IOException {
        /*Parent root = FXMLLoader.load(getClass().getResource("../fxml/Oder_DichVu.fxml"));
        Stage stage =   new Stage();
        Scene scene =   new Scene(root);
        stage.setScene(scene);
        stage.show();*/
        try {
            Stage stage = new Stage();
            stage.setTitle("Thêm Dịch Vu");
            FXMLLoader loader =new FXMLLoader();
            loader.setLocation(getClass().getResource("../fxml/Oder_DichVu.fxml"));
            Parent Oder_DichVu = loader.load();
            Scene scene = new Scene(Oder_DichVu);
            Controller_OderDichVu controller = loader.getController();
            ThuePhong selected = table_infor.getSelectionModel().getSelectedItem();
            controller.Adddichvu(selected);
            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
    private void cleardata() {
        txt_makh.clear();
        txt_dckh.clear();
        txt_sdtkh.clear();
        cb_nu.setSelected(false);
        cb_nam.setSelected(false);
        txt_tenkh.clear();
        txt_cmndkh.clear();
        txt_matp.clear();
    }
    public void handleUpdateThuePhong(ActionEvent event) {
        String sql = "Update KHACH_HANG  set  TenKhachHang = ?, GioiTinh = ?, CMND = ?, DiaChi = ?, DienThoai = ? WHERE MaKhachHang = ? ";
        try {
            String makh = txt_makh.getText();
            String tenkh = txt_tenkh.getText();
            String dc = txt_dckh.getText();
            String cmnd = txt_cmndkh.getText();
            String sdt = txt_sdtkh.getText();

            pst = con.prepareStatement(sql);
            pst.setString(1,tenkh);
            pst.setString(2, GioiTinh);
            pst.setString(3,cmnd);
            pst.setString(4, dc);
            pst.setString(5, sdt);
            pst.setString(6, makh);
            int i = pst.executeUpdate();
            if (i == 1) {
                System.out.println("Data Insert Successfully");
                data.clear();

                LoadDataTableView();
                cleardata();
                AlertDialog.display("Thông báo","Update thành công");

                //done - them mot cai nua la khi update thanh cong, update lai tableview de hien thi data nhan vien

            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void autoMaKH() throws SQLException {
        pst = con.prepareStatement("SELECT count(1) as MaKhachHang FROM KHACH_HANG");
        rs = pst.executeQuery();
        while(rs.next()){
            String s = String.valueOf(rs.getInt(1)+1);
            txt_makh.setText("KH0"+s);
        }
    }
    private void autoMaTP() throws SQLException {
        pst = con.prepareStatement("SELECT count(1) as MaThuePhong FROM PHIEU_THUE_PHONG");
        rs = pst.executeQuery();
        while(rs.next()){
            String s = String.valueOf(rs.getInt(1)+1);
            txt_matp.setText("TP0"+s);
        }
    }
    public void handleAddThuePhong(ActionEvent event) {
        String sqlKH = "Insert Into KHACH_HANG(MaKhachHang,TenKhachHang,GioiTinh,CMND,DiaChi,DienThoai) Values(?,?,?,?,?,?)";
        String makh = txt_makh.getText();
        String tenkh = txt_tenkh.getText();
        String cmnd = txt_cmndkh.getText();
        String dc = txt_dckh.getText();
        String sdt = txt_sdtkh.getText();
        try{
            pst = con.prepareStatement(sqlKH);
            pst.setString(1,makh);
            pst.setString(2,tenkh);
            pst.setString(3,GioiTinh);
            pst.setString(4,cmnd);
            pst.setString(5,dc);
            pst.setString(6,sdt);
            int i = pst.executeUpdate();
            if(i==1){
                System.out.println("Data Insert Successfully");
                data.clear();
                LoadDataTableView();
            }
        }catch (SQLException ex) {
            Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE,null,ex);
        }

        String manv1 = null;
        String mlp = "select MaNhanVien from NHAN_VIEN where HoTen LIKE N'%"+txt_mnv.getText()+"%'";
        try{
            pst = con.prepareStatement(mlp);
            rs= pst.executeQuery();
            if(rs.next()) {
                manv1 = rs.getString(1);
            }
        }catch (SQLException ex){
            Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sqlphieuthue = "Insert Into PHIEU_THUE_PHONG(MaThuePhong,MaNhanVien,MaKhachHang,NgayLap) Values(?,?,?,?)";
        java.util.Date date=new java.util.Date();
        String matp1 = txt_matp.getText();
        String makh1 = txt_makh.getText();
        LocalDateTime now = LocalDateTime.now();

        try {

            pst = con.prepareStatement(sqlphieuthue);
            pst.setString(1,matp1);
            pst.setString(2,manv1);
            pst.setString(3,makh1);
            pst.setString(4, String.valueOf(now));

            int i = pst.executeUpdate();
            if(i==1){
                System.out.println("Data Insert Successfully");
                data.clear();
                LoadDataTableView();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE,null,ex);
        }
        String sqlphieutp = "Insert Into CHI_TIET_PHIEU_THUE_PHONG(MaThuePhong,MaPhong) Values(?,?)";
        String matp = txt_matp.getText();
        String map = txt_maphong.getText();
        try{
            pst = con.prepareStatement(sqlphieutp);
            pst.setString(1,matp);
            pst.setString(2,map);
            int i = pst.executeUpdate();
            if(i==1){
                System.out.println("Data Insert Successfully");
                data.clear();
                LoadDataTableView();
            }
        }catch (SQLException ex) {
            Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE,null,ex);
        }
        String maphong = txt_maphong.getText();
        String sql = "Update PHONG  set MaLoaiTinhTrangPhong = ? WHERE MaPhong = ? ";
        try{
            pst = con.prepareStatement(sql);
            pst.setString(1,"TP003");
            pst.setString(2,maphong);
            int i = pst.executeUpdate();
            if(i==1){
                System.out.println("Data Insert Successfully");
                AlertDialog.display("Thông báo","Thêm thành công");
                data.clear();
                LoadDataTableView();
            }
        }catch (SQLException ex) {
            Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE,null,ex);
        }
    }

    public void handleChonPhong(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/ChonPhong.fxml"));
        Stage stage =   new Stage();
        stage.setTitle("Chọn Phòng");
        Scene scene =   new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /*public void chonphong(ChonPhong chonPhong ) {
        txt_maphong.setText(chonPhong.getMaphong());

    }*/
    public void manhanvien(Namelogin namelogin){
        txt_mnv.setText(namelogin.getManv());
    }

}


