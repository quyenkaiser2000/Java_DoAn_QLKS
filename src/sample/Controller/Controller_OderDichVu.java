package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java. math. BigInteger;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Dialog.AlertDialog;
import sample.model.ChonPhong;
import sample.model.Oder_DichVu;
import sample.model.ThuePhong;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller_OderDichVu implements Initializable {

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<Oder_DichVu> data;

    @FXML
    private AnchorPane anchorpane;

    @FXML
    private TableView<Oder_DichVu> table_info;

    @FXML
    private TableColumn<?, ?> col_tendv;

    @FXML
    private TableColumn<?, ?> col_dvt;

    @FXML
    private TableColumn<?, ?> col_sl;

    @FXML
    private TableColumn<?, ?> col_dg;

    @FXML
    private TextField txt_sl;

    @FXML
    private ComboBox<String> combobox_tendv;

    @FXML
    private TextField txt_tongcong;
    @FXML
    private Label lable_matp;
    @FXML
    private Label lable_mahd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = sample.DBConnection.qlksConnection();
        data = FXCollections.observableArrayList();
        setCellTable();
        LoadDataTableView();
        loadCBTenDV();
        try {
            autoMaHD();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setCellTable() {

        col_tendv.setCellValueFactory(new PropertyValueFactory<>("tendv"));
        col_dvt.setCellValueFactory(new PropertyValueFactory<>("dvt"));
        col_sl.setCellValueFactory(new PropertyValueFactory<>("sl"));
        col_dg.setCellValueFactory(new PropertyValueFactory<>("dg"));
    }

    private void LoadDataTableView() {
        try {
            pst = con.prepareStatement("select A.TenDichVu, DonViTinh, SoLuong,DonGia from DICH_VU A, CHI_TIET_HOA_DON_DICH_VU B,HOA_DON_DICH_VU C, CHI_TIET_PHIEU_THUE_PHONG D WHERE A.MaDichVu = B.MaDichVu  AND B.SoHD = C.SoHD AND C.MaThuePhong = D.MaThuePhong AND D.MaThuePhong = N'"+lable_matp.getText()+"'");
            rs = pst.executeQuery();
            while (rs.next()) {
                data.add(new Oder_DichVu(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));

            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_info.setItems(data);

    }



    private void autoMaHD() throws SQLException {
        pst = con.prepareStatement("SELECT count(1) as SoHD FROM HOA_DON_DICH_VU");
        rs = pst.executeQuery();
        while(rs.next()){
            String s = String.valueOf(rs.getInt(1)+1);
            lable_mahd.setText("HD0"+s);
        }
    }
    private void loadCBTenDV() {
        try {
            List<String> options = new ArrayList<>();
            pst = con.prepareStatement("SELECT * FROM DICH_VU WHERE Status='True'");
            rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("TenDichVu");
                options.add(name);
            }

            combobox_tendv.setItems(FXCollections.observableArrayList(options));
        } catch (SQLException ex) {
            Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void handleExitOderDV(ActionEvent event) throws IOException {
        Stage stage =   (Stage) anchorpane.getScene().getWindow();
        stage.close();
    }
    public void Adddichvu(ThuePhong oder_dichVu ){
        lable_matp.setText(oder_dichVu.getMatp());
        LoadDataTableView();
    }






    public void tinhtien(MouseEvent mouseEvent) {
        String tien = null;
        String tendv = combobox_tendv.getValue();
        String sql = "select SUM('"+txt_sl.getText()+"'*A.DonGia) from DICH_VU A where TenDichVu LIKE N'"+tendv+"'";
        try{
            pst = con.prepareStatement(sql);
            rs= pst.executeQuery();
            if(rs.next()) {
                tien = rs.getString(1);

            }
        }catch (SQLException ex){
            Logger.getLogger(Controller_OderDichVu.class.getName()).log(Level.SEVERE, null, ex);
        }
        txt_tongcong.setText(tien);
    }

    public void handleAdd(ActionEvent event) {
        String map = null;
        String mlp = "select MaPhong from CHI_TIET_PHIEU_THUE_PHONG where MaThuePhong LIKE N'%"+lable_matp.getText()+"%'";
        try{
            pst = con.prepareStatement(mlp);
            rs= pst.executeQuery();
            if(rs.next()) {
                map = rs.getString(1);
            }
        }catch (SQLException ex){
            Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sqlphieuthue = "Insert Into HOA_DON_DICH_VU(SoHD,MaPhong,ThanhToan,NgayLapHoaDon,MaThuePhong) Values(?,?,?,?,?)";
        java.util.Date date=new java.util.Date();
        String shd = lable_mahd.getText();
        String mtp = lable_matp.getText();
        LocalDateTime now = LocalDateTime.now();

        try {

            pst = con.prepareStatement(sqlphieuthue);
            pst.setString(1,shd);
            pst.setString(2,map);
            pst.setString(3,"false");
            pst.setString(4, String.valueOf(now));
            pst.setString(5,mtp);

            int i = pst.executeUpdate();
            if(i==1){
                System.out.println("Data Insert Successfully");
                data.clear();
                LoadDataTableView();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE,null,ex);
        }



        String madichvu = null;
        String mdv = "select MaDichVu from DICH_VU where TenDichVu LIKE N'%"+combobox_tendv.getValue()+"%'";
        try{
            pst = con.prepareStatement(mdv);
            rs= pst.executeQuery();
            if(rs.next()) {
                madichvu = rs.getString(1);
            }
        }catch (SQLException ex){
            Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sqlcthddv = "Insert Into CHI_TIET_HOA_DON_DICH_VU(SoHD,MaDichVu,ThanhTien,SoLuong) Values(?,?,?,?)";
        String sohd = lable_mahd.getText();
        String tien = txt_tongcong.getText();
        String thanhtien = txt_tongcong.getText();
        BigInteger bigIntegerStr=new BigInteger(thanhtien);
        String soluong = txt_sl.getText();
        try{
            pst = con.prepareStatement(sqlcthddv);
            pst.setString(1,sohd);
            pst.setString(2,madichvu);
            pst.setString(3,bigIntegerStr.toString());
            pst.setString(4,soluong);
            int i = pst.executeUpdate();
            if(i==1){
                System.out.println("Data Insert Successfully");
                AlertDialog.display("Thông báo","Thêm DV thành công");
                data.clear();
                LoadDataTableView();
            }
        }catch (SQLException ex) {
            Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE,null,ex);
        }



    }
}


