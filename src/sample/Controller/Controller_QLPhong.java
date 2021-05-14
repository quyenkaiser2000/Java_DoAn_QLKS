package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;

import javafx.scene.control.cell.PropertyValueFactory;
import sample.model.ChucVu;
import sample.model.QLPhong;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller_QLPhong implements Initializable{

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<QLPhong> data;
    private ObservableList<ChucVu> cv;

    @FXML
    private TextField txt_manv;

    @FXML
    private Button btn_add;

    @FXML
    private TextField txt_macv;

    @FXML
    private TextField txt_tennv;

    @FXML
    private DatePicker dt_ns;

    @FXML
    private TextField txt_dc;

    @FXML
    private TextField txt_sdt;

    @FXML
    private ComboBox<ChucVu> cb_cv;

    @FXML
    private CheckBox txt_nam;

    @FXML
    private CheckBox txt_nu;



    @FXML
    private TableView<QLPhong> table_info;

    @FXML
    private TableColumn<?, ?> col_map;

    @FXML
    private TableColumn<?, ?> col_tlp;

    @FXML
    private TableColumn<?, ?> col_dg;

    @FXML
    private TableColumn<?, ?> col_ttp;

    @FXML
    private TableColumn<?, ?> col_gc;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = sample.DBConnection.qlksConnection();
        data = FXCollections.observableArrayList();
        cv = FXCollections.observableArrayList();
        setCellTable();
        LoadDataTableView();
        //loadCBCV();
    }

    private void setCellTable(){

        col_map.setCellValueFactory(new PropertyValueFactory<>("map"));
        col_tlp.setCellValueFactory(new PropertyValueFactory<>("tlp"));
        col_dg.setCellValueFactory(new PropertyValueFactory<>("dg"));
        col_ttp.setCellValueFactory(new PropertyValueFactory<>("ttp"));
        col_gc.setCellValueFactory(new PropertyValueFactory<>("gc"));
    }

    private void LoadDataTableView(){
        try{
            pst = con.prepareStatement("SELECT * FROM PHONG");
            rs = pst.executeQuery();
            while(rs.next()){
                data.add(new QLPhong(rs.getString(1)));

            }
        }catch(SQLException ex){
            Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE,null,ex);
        }

        table_info.setItems(data);

    }
    /*public void loadCBCV(){
        try{
            pst = con.prepareStatement("SELECT * FROM CHUC_VU");
            rs = pst.executeQuery();
            while(rs.next()){

                cv.add(new ChucVu(rs.getString(2).toString()));
            }
        }catch(SQLException ex){
            Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE,null,ex);
        }
        cb_cv.setItems(cv);

    }*/
    private void handleAddNhanVien(ActionEvent event) throws SQLException {
        String sql = "Insert Into NHAN_VIEN(MaNhanVien, MaChucVu, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, ChucVu) Values(?,?,?,?,?,?,?,?)";
        String manv = txt_manv.getText();
        String macv = txt_macv.getText();
        String tennv = txt_tennv.getText();
        String ns = dt_ns.getValue().toString();
        if(txt_nam.isSelected()){
            String gt =txt_nam.getText();
        }
        if(txt_nu.isSelected()){
            String gt =txt_nu.getText();
        }
        String dc = txt_dc.getText();
        String sdt = txt_sdt.getText();
        /*String cv = txt_cv.getText();*/
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1,manv);
            pst.setString(2,macv);
            pst.setString(3,tennv);
            pst.setString(4,ns);
            //pst.setString(5,);
            pst.setString(6,dc);
            pst.setString(7,sdt);
            //pst.setString(1,manv);
            int i = pst.executeUpdate();
            if(i==1){
                System.out.println("Data Insert Successfully");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE,null,ex);
        }
        finally {
            pst.close();
        }
    }
    /*public void checkbox(ActionEvent event){
        if(txt_nam.isSelected()){
            String gt = txt_nam.getText();
        }

    }*/
}
//select*from PHONG A, LOAI_PHONG B, LOAI_TINH_TRANG C
//where A.MaLoaiPhong=B.MaLoaiPhong And A.MaLoaiTinhTrangPhong = C.MaLoaiTinhTrangPhong

//select*from PHIEU_THUE_PHONG A, NHAN_VIEN B,KHACH_HANG C
//where A.MaNhanVien=B.MaNhanVien and A.MaKhachHang=C.MaKhachHang
