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

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Dialog.AlertDialog;
import sample.Validation.Validation_TextField;
import sample.model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller_QLPhong implements Initializable{

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<QLPhong> data;
    private ObservableList<LoaiPhong> datalp;
    private ObservableList<LoaiTinhTrangPhong> datalttp;


    @FXML
    private ComboBox<String> combobox_tinhtrangphong;

    @FXML
    private ComboBox<String> combobox_loaiphong;
    @FXML
    private TextField txt_search;
    @FXML
    private TextField txt_maphong;
    @FXML
    private AnchorPane anchorpane;
    @FXML
    private Label error_map;



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
        datalp= FXCollections.observableArrayList();
        setCellTable();
        LoadDataTableView();
        //loadCBCV();
        loadCBTinhTrangPhong();
        loadCBLoaiPhong();
        search_QLPhong();
        setcellvalueformtableview();
        try {
            autoMaPhong();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
            pst = con.prepareStatement("SELECT * FROM PHONG A, LOAI_PHONG B, LOAI_TINH_TRANG C WHERE A.MaLoaiPhong=B.MaLoaiPhong And A.MaLoaiTinhTrangPhong = C.MaLoaiTinhTrangPhong");
            rs = pst.executeQuery();
            while(rs.next()){
                data.add(new QLPhong(rs.getString(1),rs.getString(6),rs.getString(7),rs.getString(9)));

            }
        }catch(SQLException ex){
            Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE,null,ex);
        }

        table_info.setItems(data);

    }
    private void loadCBLoaiPhong() {
        try {
            List<String> options = new ArrayList<>();
            pst = con.prepareStatement("SELECT * FROM LOAI_PHONG");
            rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("TenLoaiPhong");
                options.add(name);
            }

            combobox_loaiphong.setItems(FXCollections.observableArrayList(options));

        } catch (SQLException ex) {
            Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private void loadCBTinhTrangPhong() {
        try {
            List<String> options = new ArrayList<>();
            pst = con.prepareStatement("SELECT * FROM LOAI_TINH_TRANG");
            rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("TenLoaiTinhTrangPhong");
                options.add(name);
            }

            combobox_tinhtrangphong.setItems(FXCollections.observableArrayList(options));

        } catch (SQLException ex) {
            Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setcellvalueformtableview() {
        table_info.setOnMouseClicked(e -> {
            QLPhong p1 = table_info.getItems().get(table_info.getSelectionModel().getFocusedIndex());
            txt_maphong.setText(p1.getMap());

            // txt_nam.setSelected(p1.getGt());
            // txt_nu.setSelected(p1.getGt());
            combobox_loaiphong.setValue(p1.getTlp());
            combobox_tinhtrangphong.setValue(p1.getTtp());
        });
    }

    private void search_QLPhong(){
        txt_search.setOnKeyReleased(e->{
            if(txt_search.getText().equals(""))    {
                data.clear();
                LoadDataTableView();
            }
            else{
                data.clear();
                String sql = "SELECT * FROM PHONG A, LOAI_PHONG B, LOAI_TINH_TRANG C WHERE A.MaLoaiPhong=B.MaLoaiPhong And A.MaLoaiTinhTrangPhong = C.MaLoaiTinhTrangPhong AND MaPhong LIKE N'%"+txt_search.getText()+"%'"
                        +"UNION SELECT * FROM PHONG A, LOAI_PHONG B, LOAI_TINH_TRANG C WHERE A.MaLoaiPhong=B.MaLoaiPhong And A.MaLoaiTinhTrangPhong = C.MaLoaiTinhTrangPhong AND TenLoaiPhong LIKE N'%"+txt_search.getText()+"%'"
                        +"UNION SELECT * FROM PHONG A, LOAI_PHONG B, LOAI_TINH_TRANG C WHERE A.MaLoaiPhong=B.MaLoaiPhong And A.MaLoaiTinhTrangPhong = C.MaLoaiTinhTrangPhong AND TenLoaiTinhTrangPhong LIKE N'%"+txt_search.getText()+"%'";
                try{
                    pst = con.prepareStatement(sql);
                    rs= pst.executeQuery();
                    while(rs.next()) {
                        data.add(new QLPhong(rs.getString(1),rs.getString(6),rs.getString(7),rs.getString(9)));

                    }

                    table_info.setItems(data);
                }catch (SQLException ex){
                    Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
    }
    private void autoMaPhong() throws SQLException {
        pst = con.prepareStatement("SELECT count(1) as MaPhong FROM PHONG");
        rs = pst.executeQuery();
        while(rs.next()){
            String s = String.valueOf(rs.getInt(1)+1);
            txt_maphong.setText("15"+s);
        }
    }
    public void handleAddPhong(ActionEvent event) throws SQLException {
        boolean ismaphong = Validation_TextField.isTextFieldNotEmpty(txt_maphong, error_map,"Nhập Mã!");
        if(ismaphong) {
            String sql = "Insert Into PHONG(MaPhong,MaLoaiPhong,MaLoaiTinhTrangPhong) Values(?,?,?)";
            //String sql1 = "Insert Into PHONG(MaLoaiPhong) Values(?)";
            // String sql2 = "Insert Into PHONG(MaLoaiTinhTrangPhong) Values(?)";
            String mp = txt_maphong.getText();
            String malp = null;
            String mattlp = null;

            String mlp = "select MaLoaiPhong from LOAI_PHONG where TenLoaiPhong LIKE N'%" + combobox_loaiphong.getValue() + "%'";

            String mlttp = "select MaLoaiTinhTrangPhong from LOAI_TINH_TRANG where TenLoaiTinhTrangPhong LIKE N'%" + combobox_tinhtrangphong.getValue() + "%'";

            try {
                pst = con.prepareStatement(mlp);
                rs = pst.executeQuery();
                if (rs.next()) {
                    malp = rs.getString(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                pst = con.prepareStatement(mlttp);
                rs = pst.executeQuery();
                if (rs.next()) {
                    mattlp = rs.getString(1);
                }

            } catch (SQLException ex) {
                Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, mp);
                pst.setString(2, malp);
                pst.setString(3, mattlp);
                int i = pst.executeUpdate();
                if (i == 1) {
                    System.out.println("Data Insert Successfully");
                    AlertDialog.display("Thông báo", "Thêm thành công");
                    data.clear();
                    LoadDataTableView();
                    autoMaPhong();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally {
                pst.close();
            }
        }




    }

    public void handleUpdatePhong(ActionEvent event) {
        String sql = "Update PHONG  set MaLoaiPhong = ?, MaLoaiTinhTrangPhong = ? WHERE MaPhong = ? ";
        String map = txt_maphong.getText();
        String malp = null;
        String mattlp = null;
        String mlp = "select MaLoaiPhong from LOAI_PHONG where TenLoaiPhong LIKE N'%"+combobox_loaiphong.getValue()+"%'";

        String mlttp = "select MaLoaiTinhTrangPhong from LOAI_TINH_TRANG where TenLoaiTinhTrangPhong LIKE N'%"+combobox_tinhtrangphong.getValue()+"%'";
        try{
            pst = con.prepareStatement(mlp);
            rs= pst.executeQuery();
            if(rs.next()) {
                malp = rs.getString(1);
            }
        }catch (SQLException ex){
            Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            pst = con.prepareStatement(mlttp);
            rs= pst.executeQuery();
            if(rs.next()) {
                mattlp = rs.getString(1);
            }

        }catch (SQLException ex){
            Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            pst = con.prepareStatement(sql);
            pst.setString(1,malp);
            pst.setString(2, mattlp);
            pst.setString(3, map);
            int i = pst.executeUpdate();
            if (i == 1) {
                System.out.println("Data Insert Successfully");
                data.clear();
                LoadDataTableView();
                AlertDialog.display("Thông báo","Update thành công");
                //done - them mot cai nua la khi update thanh cong, update lai tableview de hien thi data nhan vien

            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller_QLPhong.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleExitPhong(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/trangchu.fxml"));
        Stage stage =   (Stage) anchorpane.getScene().getWindow();

        stage.close();
    }
}
//select*from PHONG A, LOAI_PHONG B, LOAI_TINH_TRANG C
//where A.MaLoaiPhong=B.MaLoaiPhong And A.MaLoaiTinhTrangPhong = C.MaLoaiTinhTrangPhong

//select*from PHIEU_THUE_PHONG A, NHAN_VIEN B,KHACH_HANG C
//where A.MaNhanVien=B.MaNhanVien and A.MaKhachHang=C.MaKhachHang
