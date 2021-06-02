package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.Printer;
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
import sample.model.QLPhong;

import javax.swing.*;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller_DichVu implements Initializable{

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<DichVu> data;

    @FXML
    private TextField txt_madv;


    @FXML
    private TextField txt_tendv;

    @FXML
    private TextField txt_dg;

    @FXML
    private ComboBox<String> ComboBox_dvt;
    @FXML
    private TextField txt_search;
    @FXML
    private Button btn_exit;
    @FXML
    private AnchorPane anchorpane;
    @FXML
    private Label error_madv;

    @FXML
    private Label error_tendv;

    @FXML
    private Label error_dg;



    @FXML
    private TableView<DichVu> table_info;

    @FXML
    private TableColumn<?, ?> col_madv;

    @FXML
    private TableColumn<?, ?> col_tendv;

    @FXML
    private TableColumn<?, ?> col_dvt;

    @FXML
    private TableColumn<?, ?> col_dg;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = sample.DBConnection.qlksConnection();
        data = FXCollections.observableArrayList();
        setCellTable();
        LoadDataTableView();
        loadCBDVT();
        search_DichVu();
        setcellvalueformtableview();
    }

    private void setCellTable(){

        col_madv.setCellValueFactory(new PropertyValueFactory<>("madv"));
        col_tendv.setCellValueFactory(new PropertyValueFactory<>("tendv"));
        col_dvt.setCellValueFactory(new PropertyValueFactory<>("dvt"));
        col_dg.setCellValueFactory(new PropertyValueFactory<>("dg"));
    }

    private void LoadDataTableView(){
        try{
            pst = con.prepareStatement("SELECT * FROM DICH_VU WHERE Status = N'True'");
            rs = pst.executeQuery();
            while(rs.next()){
                data.add(new DichVu(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));

            }
        }catch(SQLException ex){
            Logger.getLogger(Controller_DichVu.class.getName()).log(Level.SEVERE,null,ex);
        }

        table_info.setItems(data);

    }
    private void setcellvalueformtableview() {
        table_info.setOnMouseClicked(e -> {
            DichVu p1 = table_info.getItems().get(table_info.getSelectionModel().getFocusedIndex());
            txt_madv.setText(p1.getMadv());
            txt_tendv.setText(p1.getTendv());
            txt_dg.setText(p1.getDg());
            ComboBox_dvt.setValue(p1.getDvt());
        });
    }
    private void loadCBDVT(){
        try{
            List<String> options = new ArrayList<>();
            pst = con.prepareStatement("SELECT DISTINCT DonViTinh FROM DICH_VU");
            rs = pst.executeQuery();
            while(rs.next()){
                String name = rs.getString("DonViTinh");
                options.add(name);
            }

            ComboBox_dvt.setItems(FXCollections.observableArrayList(options));
        }catch(SQLException ex){
            Logger.getLogger(Controller_DichVu.class.getName()).log(Level.SEVERE,null,ex);
        }

    }
    private void cleardata() {
        txt_madv.clear();
        txt_dg.clear();
        txt_tendv.clear();
        ComboBox_dvt.setPromptText("");
    }
    private void search_DichVu(){
        txt_search.setOnKeyReleased(e->{
            if(txt_search.getText().equals(""))    {
                data.clear();
                LoadDataTableView();
            }
            else{
                data.clear();
                String sql = "select *   from DICH_VU where Status = 'True' AND MaDichVu LIKE N'%"+txt_search.getText()+"%'"
                        + "UNION Select *   from DICH_VU where Status = 'True' AND TenDichVu LIKE N'%"+txt_search.getText()+"%'" ;
                try{
                    pst = con.prepareStatement(sql);
                    rs= pst.executeQuery();
                    while(rs.next()) {
                        data.add(new DichVu(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
                    }

                    table_info.setItems(data);
                }catch (SQLException ex){
                    Logger.getLogger(Controller_DichVu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
    }

    public void handleAddDichVu(ActionEvent event) throws SQLException {
        boolean ismadv = Validation_TextField.isTextFieldNotEmpty(txt_madv, error_madv,"Nhập Mã!");
        boolean istendv = Validation_TextField.isTextFieldNotEmpty(txt_tendv, error_tendv,"Nhập Tên!");
        boolean isdg = Validation_TextField.isTextFieldNotEmpty(txt_dg, error_dg,"Nhập DG!");
        if(ismadv && isdg && istendv){
            String sql = "Insert Into DICH_VU(MaDichVu, TenDichVu, DonViTinh, DonGia,Status) Values(?,?,?,?,?)";
            String madv = txt_madv.getText();
            String tendv = txt_tendv.getText();
            String dg = txt_dg.getText();
            String dvt = ComboBox_dvt.getValue();

                try {
                    pst = con.prepareStatement(sql);
                    pst.setString(1,madv);
                    pst.setString(2,tendv);
                    pst.setString(3,dvt);
                    pst.setString(4,dg);
                    pst.setString(5,"True");
                    int i = pst.executeUpdate();
                    if(i==1){
                        System.out.println("Data Insert Successfully");
                        AlertDialog.display("Thông báo","Thêm thành công");
                        data.clear();
                        LoadDataTableView();
                        cleardata();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Controller_DichVu.class.getName()).log(Level.SEVERE,null,ex);
                }
                finally {
                    pst.close();
                }
        }
    }

    public void handleDeleteDichVu(ActionEvent event) {
        String sql = "Update DICH_VU  set Status = ? WHERE MaDichVu = ? ";
        try {
            String madv = txt_madv.getText();
            pst = con.prepareStatement(sql);
            pst.setString(1,"false");
            pst.setString(2, madv);
            int i = pst.executeUpdate();
            if (i == 1) {
                System.out.println("Data Update Successfully");
                data.clear();
                LoadDataTableView();
                AlertDialog.display("Thông báo","Xóa thành công");
                //done - them mot cai nua la khi update thanh cong, update lai tableview de hien thi data nhan vien

            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller_DichVu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void handleUpdateDichVu(ActionEvent event) {
        String sql = "Update DICH_VU  set TenDichVu = ?, DonViTinh = ?, DonGia = ? WHERE MaDichVu = ? ";
        try {
            String tendv = txt_tendv.getText();
            String madv = txt_madv.getText();
            String dg = txt_dg.getText();
            String dvt = ComboBox_dvt.getValue();

            pst = con.prepareStatement(sql);


            pst.setString(1,tendv);
            pst.setString(2, dvt);
            pst.setString(3, dg);
            pst.setString(4, madv);
            int i = pst.executeUpdate();
            if (i == 1) {
                System.out.println("Data Update Successfully");
                data.clear();
                LoadDataTableView();
                AlertDialog.display("Thông báo","Update thành công");
                //done - them mot cai nua la khi update thanh cong, update lai tableview de hien thi data nhan vien

            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller_DichVu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Exit(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/trangchu.fxml"));
        Stage stage =   (Stage) anchorpane.getScene().getWindow();
        stage.close();
    }

    public void handleInDichVu(ActionEvent event) {
        MessageFormat header = new MessageFormat("IN DANH SACH DICH VU");
        MessageFormat footer = new MessageFormat("Page {0, number , integer}");

        Printer myPrinter;
        ObservableSet<Printer> printers = Printer.getAllPrinters();
        for(Printer printer : printers){
            if(printer.getName().matches("spefic printer name")){
                myPrinter = printer;
            }
        }

    }


    private boolean validateFields (){
        if(txt_madv.getText().isEmpty() | txt_tendv.getText().isEmpty() | ComboBox_dvt.getSelectionModel().isEmpty()
        | txt_dg.getText().isEmpty() | txt_dg.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Fields");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Into the Fields");
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


