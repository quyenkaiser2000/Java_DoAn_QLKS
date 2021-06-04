package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import sample.model.ChucVu;
import sample.model.DichVu;
import sample.model.NhanVien;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private AnchorPane anchorpane;


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
    @FXML
    private Label error_manv;

    @FXML
    private Label error_tennv;

    @FXML
    private Label error_ns;

    @FXML
    private Label error_dc;

    @FXML
    private Label error_sdt;
    @FXML
    private Label error_gt;
    @FXML
    private Label error_cv;
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
        try {
            autoMa();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setCellTable() {

        col_manv.setCellValueFactory(new PropertyValueFactory<>("manv"));
        col_macv.setCellValueFactory(new PropertyValueFactory<>("macv"));
        col_tennv.setCellValueFactory(new PropertyValueFactory<>("tennv"));
        col_ns.setCellValueFactory(new PropertyValueFactory<>("ns"));
        col_gt.setCellValueFactory(new PropertyValueFactory<>("gt"));
        col_dc.setCellValueFactory(new PropertyValueFactory<>("dc"));
        col_sdt.setCellValueFactory(new PropertyValueFactory<>("sdt"));
    }

    private void LoadDataTableView() {
        try {
            pst = con.prepareStatement("SELECT MaNhanVien, B.TenChucVu , HoTen, FORMAT (NgaySinh, 'dd/MM/yyyy'), GioiTinh, DiaChi, SoDienThoai FROM NHAN_VIEN A, CHUC_VU B Where A.MaChucVu=B.MaChucVu");
            rs = pst.executeQuery();
            while (rs.next()) {
                data.add(new NhanVien(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));

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
            txt_manv.setText(p1.getManv());
            txt_tennv.setText(p1.getTennv());
            txt_sdt.setText(p1.getSdt());
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

            String macv = null;
            String sqlmakh = "SELECT B.TenChucVu FROM NHAN_VIEN A, CHUC_VU B WHERE A.MaChucVu = B.MaChucVu AND MaNhanVien LIKE N'"+txt_manv.getText()+"'";
            try{
                pst = con.prepareStatement(sqlmakh);
                rs= pst.executeQuery();
                if(rs.next()) {
                    macv = rs.getString(1);
                    ComboBox_name.setValue(macv);

                }
            }catch (SQLException ex) {
                Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    private void autoMa() throws SQLException {
        pst = con.prepareStatement("SELECT count(1) as MaNhanVIen FROM NHAN_VIEN");
        rs = pst.executeQuery();
        while(rs.next()){
            String s = String.valueOf(rs.getInt(1)+1);
            txt_manv.setText("NV0"+s);
        }
    }
    @FXML
    private void handleAddNhanVien(ActionEvent event) throws SQLException {
        boolean ismanv = Validation_TextField.isTextFieldNotEmpty(txt_manv, error_manv,"Nhập Mã!");
        boolean istennv = Validation_TextField.isTextFieldNotEmpty(txt_tennv, error_tennv,"Nhập Tên!");
        boolean isns = Validation_TextField.isTextFieldNotEmpty(dt_ns.getEditor(), error_ns,"Nhập NS!");
        boolean isdc = Validation_TextField.isTextFieldNotEmpty(txt_dc, error_dc,"Nhập DC!");
        boolean issdt = Validation_TextField.isTextFieldNotEmpty(txt_sdt, error_sdt,"Nhập SDT!");

        if(ismanv && istennv && isns && isdc && issdt){
            String sql = "Insert Into NHAN_VIEN(MaNhanVien,MaChucVu, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai) Values(?,?,?,?,?,?,?)";
            String manv = txt_manv.getText();
            String tennv = txt_tennv.getText();
            String ns = dt_ns.getEditor().getText();
            String dc = txt_dc.getText();
            String sdt = txt_sdt.getText();

            String macv = null;
            String mlp = "select MaChucVu from CHUC_VU where TENCHUCVU LIKE N'%"+ComboBox_name.getValue()+"%'";
            try{
                pst = con.prepareStatement(mlp);
                rs= pst.executeQuery();
                if(rs.next()) {
                    macv = rs.getString(1);
                }
            }catch (SQLException ex){
                Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
            }

                try {
                    pst = con.prepareStatement(sql);
                    pst.setString(1,manv);
                    pst.setString(2, macv);
                    pst.setString(3, tennv);
                    pst.setString(4, ns);
                    pst.setString(5, GioiTinh);
                    pst.setString(6, dc);
                    pst.setString(7, sdt);
                    int i = pst.executeUpdate();
                    if (i == 1) {
                        System.out.println("Data Insert Successfully");
                        AlertDialog.display("Thông báo","Thêm thành công");
                        data.clear();
                        LoadDataTableView();
                        cleardata();
                        autoMa();

                    }

                } catch (SQLException ex) {
                    Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
                }
                finally {

                    pst.close();
                }
        }


    }
    private void cleardata() {
        txt_dc.clear();
        txt_manv.clear();
        txt_sdt.clear();
        txt_tennv.clear();
        txt_nam.setSelected(false);
        txt_nu.setSelected(false);
        dt_ns.setValue(null);
        dt_ns.getEditor().setText(null);
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
        String macv = null;
        String mcv = "select MaChucVu from CHUC_VU where TENCHUCVU LIKE N'%"+ComboBox_name.getValue()+"%'";
        try{
            pst = con.prepareStatement(mcv);
            rs= pst.executeQuery();
            if(rs.next()) {
                macv = rs.getString(1);
            }
        }catch (SQLException ex){
            Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
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
                cleardata();
                AlertDialog.display("Thông báo","Update thành công");
                //done - them mot cai nua la khi update thanh cong, update lai tableview de hien thi data nhan vien

            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*public void handleDeleteNhanVien(ActionEvent event) throws SQLException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to Delete ?");
        Optional<ButtonType> action = alert.showAndWait();


        if (action.get() == ButtonType.OK) {
            String sql = "delete from NHAN_VIEN where MaNhanVien = ?";
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, txt_manv.getText());
                int i = pst.executeUpdate();
                if (i == 1) {
                    System.out.println("Data Delete Successfully");
                    AlertDialog.display("Thông báo", "Xóa thành công");
                    data.clear();
                    LoadDataTableView();
                    cleardata();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }*/

    public void search_NhanVien(ActionEvent event) {

    }
    private void search_NhanVien(){
        txt_search.setOnKeyReleased(e->{
            if(txt_search.getText().equals(""))    {
                data.clear();
                LoadDataTableView();
            }
            else{
                data.clear();
                String sql = "SELECT MaNhanVien, B.TenChucVu , HoTen, FORMAT (NgaySinh, 'dd/MM/yyyy'), GioiTinh, DiaChi, SoDienThoai FROM NHAN_VIEN A, CHUC_VU B Where A.MaChucVu=B.MaChucVu AND  MaNhanVien LIKE N'%"+txt_search.getText()+"%'"
                        + " UNION SELECT MaNhanVien, B.TenChucVu , HoTen, FORMAT (NgaySinh, 'dd/MM/yyyy'), GioiTinh, DiaChi, SoDienThoai FROM NHAN_VIEN A, CHUC_VU B Where A.MaChucVu=B.MaChucVu AND  B.TenChucVu LIKE N'%"+txt_search.getText()+"%'"
                        + "UNION SELECT MaNhanVien, B.TenChucVu , HoTen, FORMAT (NgaySinh, 'dd/MM/yyyy'), GioiTinh, DiaChi, SoDienThoai FROM NHAN_VIEN A, CHUC_VU B Where A.MaChucVu=B.MaChucVu AND  HoTen LIKE N'%"+txt_search.getText()+"%'";
                try{
                    pst = con.prepareStatement(sql);
                    rs= pst.executeQuery();
                    while(rs.next()) {
                        data.add(new NhanVien(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));
                    }

                    table_info.setItems(data);
                }catch (SQLException ex){
                    Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
    }
    /*private void search_NhanVien() {
        FilteredList<NhanVien> filteredData =  new FilteredList<>(data, e->true);
        txt_search.setOnKeyReleased(e->{
            txt_search.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super NhanVien>) nhanvien ->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (nhanvien.getManv().contains(newValue)) {
                        return true;
                    }else if(nhanvien.getTennv().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(nhanvien.getMacv().toLowerCase().contains(lowerCaseFilter)){
                        return  true;
                    }
                    return false;
                });
            }));
            SortedList<NhanVien> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(table_info.comparatorProperty());
            table_info.setItems(sortedData);
        });
    }*/

    public void handleExitNhanVien(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/trangchu.fxml"));
        Stage stage =   (Stage) anchorpane.getScene().getWindow();
        stage.close();
    }
    private boolean validateMobileNo(){
        Pattern p = Pattern.compile("(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})");
        Matcher m = p.matcher(txt_sdt.getText());
        if(m.find() && m.group().equals(txt_sdt.getText())){
            return true;
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Number");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid Number");
            alert.showAndWait();
            return false;
        }
    }
    private boolean validateFields (){
        if(txt_manv.getText().isEmpty() | txt_macv.getText().isEmpty() | txt_tennv.getText().isEmpty()
                | GioiTinh.isEmpty() | txt_dc.getText().isEmpty()
                | ComboBox_name.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Fields");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Into the Fields");
            alert.showAndWait();
            return false;
        }
        if(dt_ns.getEditor().getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Fields");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter The Date");
            alert.showAndWait();
            return false;
        }
        if(!(txt_nam.isSelected() | txt_nu.isSelected())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Fields");
            alert.setHeaderText(null);
            alert.setContentText("Please Select One of The male or Female");
            alert.showAndWait();
            return false;
        }
        return true;
    }
    private boolean validateName(){
        Pattern p = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher("nguyễn văn A");
        boolean b = m.find();

        if (b){
            System.out.println("chuỗi có ký tự đặc biệt");
        }else{
            System.out.println("ok");
        }
        return false;
    }

    private boolean validateDate(){
        //Pattern p = Pattern.compile("(\\{d4})/(0?[1-9]|1[012])/(0?[1-9]|[12][0-9])");
        Pattern p = Pattern.compile("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9])/(\\{d4})");
       // Pattern p = Pattern.compile("(0?[1-9]|[12][0-9])/(0?[1-9]|1[012])/(\\{d4})");
        Matcher m = p.matcher(dt_ns.getEditor().getText());
        if(m.find() && m.group().equals(dt_ns.getEditor().getText())){
            return true;
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Number");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Valid Date");
            alert.showAndWait();
            return false;
        }
    }
    private boolean validatePass(){
        if( txt_manv.getText().length() < 5){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Fields");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Into the MACV < 5");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /*public void checkbox(ActionEvent event){
        if(txt_nam.isSelected()){
            String gt = txt_nam.getText();
        }

    }*/
}


