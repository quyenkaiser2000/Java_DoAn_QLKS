package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Dialog.AlertDialog;
import sample.model.ChucVu;
import sample.model.NhanVien;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller_NhanVien implements Initializable {

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<NhanVien> data;
    private String GioiTinh;

    @FXML
    private TextField txt_manv;

    @FXML
    private Button btn_add;
    @FXML
    private Button btn_thoat;


    @FXML
    private TextField txt_tennv;

    @FXML
    private TextField txt_macv;

    @FXML
    private DatePicker dt_ns;

    @FXML
    private TextField txt_dc;

    @FXML
    private TextField txt_sdt;

    @FXML
    private ComboBox<String> ComboBox_name;

    @FXML
    private CheckBox txt_nam;

    @FXML
    private CheckBox txt_nu;
    @FXML
    private TextField txt_search;


    @FXML
    private TableView<NhanVien> table_info;

    @FXML
    private TableColumn<?, ?> col_manv;

    @FXML
    private TableColumn<?, ?> col_macv;

    @FXML
    private TableColumn<?, ?> col_tennv;

    @FXML
    private TableColumn<?, ?> col_ns;

    @FXML
    private TableColumn<?, ?> col_gt;

    @FXML
    private TableColumn<?, ?> col_dc;

    @FXML
    private TableColumn<?, ?> col_sdt;

    @FXML
    private TableColumn<?, ?> col_cv;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = sample.DBConnection.qlksConnection();
        data = FXCollections.observableArrayList();
        //cv = FXCollections.observableArrayList();
        setCellTable();
        LoadDataTableView();
        loadCBCV();
        setcellvalueformtableview();
        search_NhanVien();
    }

    private void setCellTable() {

        col_manv.setCellValueFactory(new PropertyValueFactory<>("manv"));
        col_macv.setCellValueFactory(new PropertyValueFactory<>("macv"));
        col_tennv.setCellValueFactory(new PropertyValueFactory<>("tennv"));
        col_ns.setCellValueFactory(new PropertyValueFactory<>("ns"));
        col_gt.setCellValueFactory(new PropertyValueFactory<>("gt"));
        col_dc.setCellValueFactory(new PropertyValueFactory<>("dc"));
        col_sdt.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        col_cv.setCellValueFactory(new PropertyValueFactory<>("cv"));
    }

    private void LoadDataTableView() {
        try {
            pst = con.prepareStatement("SELECT MaNhanVien, MaChucVu, HoTen, FORMAT (NgaySinh, 'dd/MM/yyyy'), GioiTinh, DiaChi, SoDienThoai, TenChucVu FROM NHAN_VIEN");
            rs = pst.executeQuery();
            while (rs.next()) {
                data.add(new NhanVien(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),rs.getString(8)));

            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_info.setItems(data);

    }

    private void loadCBCV() {
        try {
            List<String> options = new ArrayList<>();
            pst = con.prepareStatement("SELECT * FROM CHUC_VU");
            rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("TenChucVu");
                options.add(name);
            }

            ComboBox_name.setItems(FXCollections.observableArrayList(options));
        } catch (SQLException ex) {
            Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setcellvalueformtableview() {
        table_info.setOnMouseClicked(e -> {
            NhanVien p1 = table_info.getItems().get(table_info.getSelectionModel().getFocusedIndex());
            txt_macv.setText(p1.getMacv());
            txt_tennv.setText(p1.getTennv());
            txt_sdt.setText(p1.getSdt());
            txt_manv.setText(p1.getManv());
            txt_dc.setText(p1.getDc());

            //ngày tháng năm
            //giới tính

            DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(p1.getNs(), outputFormat);
            dt_ns.setValue(localDate);

            if (p1.getGt().equals("Nam")) {
                txt_nam.setSelected(true);
                txt_nu.setSelected(false);
            } else {
                txt_nam.setSelected(false);
                txt_nu.setSelected(true);
            }

            // txt_nam.setSelected(p1.getGt());
            // txt_nu.setSelected(p1.getGt());
            ComboBox_name.setValue(p1.getCv());
        });
    }
    @FXML
    private void handleAddNhanVien(ActionEvent event) throws SQLException {

        String sql = "Insert Into NHAN_VIEN(MaNhanVien, MaChucVu, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai,TenChucVu) Values(?,?,?,?,?,?,?,?)";
        String manv = txt_manv.getText();
        String macv = txt_macv.getText();
        String tennv = txt_tennv.getText();
        String ns = dt_ns.getEditor().getText();

        String dc = txt_dc.getText();
        String sdt = txt_sdt.getText();
        String cv = ComboBox_name.getValue();
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, manv);
            pst.setString(2, macv);
            pst.setString(3, tennv);
            pst.setString(4, ns);
            pst.setString(5, GioiTinh);
            pst.setString(6, dc);
            pst.setString(7, sdt);
            pst.setString(8, cv);
            int i = pst.executeUpdate();
            if (i == 1) {
                System.out.println("Data Insert Successfully");
                AlertDialog.display("Thông báo","Thêm thành công");
                data.clear();
                LoadDataTableView();
                cleardata();

            }

        } catch (SQLException ex) {
            Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            pst.close();
        }
    }
    private void cleardata() {
        txt_dc.clear();
        txt_manv.clear();
        txt_sdt.clear();
        txt_tennv.clear();
        dt_ns.setValue(LocalDate.now());
        ComboBox_name.setPromptText("");
    }

    public void male(ActionEvent event) {
        GioiTinh = "Nam";
    }

    public void female(ActionEvent event) {
        GioiTinh = "Nữ";
    }

    public void handleUpdateNhanVien(ActionEvent event) {
        String sql = "Update NHAN_VIEN  set  MaChucVu = ?, HoTen = ?, NgaySinh = ?, GioiTinh = ?, DiaChi = ?, SoDienThoai = ?, TenChucVu = ? WHERE MaNhanVien = ? ";
        try {
            String macv = txt_macv.getText();
            String manv = txt_manv.getText();
            String tennv = txt_tennv.getText();
            String ns = String.valueOf(java.sql.Date.valueOf(dt_ns.getValue()));


            String dc = txt_dc.getText();
            String sdt = txt_sdt.getText();
            String cv = ComboBox_name.getValue();

            pst = con.prepareStatement(sql);


            pst.setString(1,macv);
            pst.setString(2, tennv);
            pst.setString(3, ns);
            pst.setString(4, GioiTinh);

           // pst.setDate(4,java.sql.Date.valueOf(dt_ns.getValue()));
            pst.setString(5, dc);
            pst.setString(6, sdt);
            pst.setString(7, cv);
            pst.setString(8, manv);
            int i = pst.executeUpdate();
            if (i == 1) {
                System.out.println("Data Insert Successfully");
                data.clear();
                LoadDataTableView();
                AlertDialog.display("Thông báo","Update thành công");
                //done - them mot cai nua la khi update thanh cong, update lai tableview de hien thi data nhan vien

            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleDeleteNhanVien(ActionEvent event) throws SQLException {
        String sql = "delete from NHAN_VIEN where MaNhanVien = ?";

        try{
            pst = con.prepareStatement(sql);
            pst.setString(1,txt_manv.getText());
            int i=  pst.executeUpdate();
            if(i==1){
                System.out.println("Data Delete Successfully");
                AlertDialog.display("Thông báo","Xóa thành công");
                data.clear();
                LoadDataTableView();
                cleardata();
            }
        }catch(SQLException ex){
            Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void search_NhanVien(ActionEvent event) {

    }
    private void search_NhanVien(){
        txt_search.setOnKeyReleased(e->{
            if(txt_search.getText().equals(""))    {
                LoadDataTableView();
            }
            else{
                data.clear();
                String sql = "select *   from NHAN_VIEN where MaNhanVien LIKE N'%"+txt_search.getText()+"%'"
                        + "UNION Select *   from NHAN_VIEN where HoTen LIKE N'%"+txt_search.getText()+"%'" ;
                try{
                    pst = con.prepareStatement(sql);
                    rs= pst.executeQuery();
                    while(rs.next()) {
                        data.add(new NhanVien(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),rs.getString(8)));

                    }

                    /**
                     *                      if (rs.next()) {
                     *                         data.add(new NhanVien(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
                     *
                     *                     }
                     *                     dong if nay bi thieu, cau truy van load ra duoc nhieu dong data, nhung chi lay
                     *                     duoc dong data dau tien, cac ban sua lai cho dung,
                     *                     recommend su dung vong lap while, do .. while
                     */

                    table_info.setItems(data);
                }catch (SQLException ex){
                    Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        });
    }

    public void handleExitNhanVien(ActionEvent event) {
        Stage stage = (Stage) btn_thoat.getScene().getWindow();
        stage.close();
    }
    /*public void checkbox(ActionEvent event){
        if(txt_nam.isSelected()){
            String gt = txt_nam.getText();
        }

    }*/
}


