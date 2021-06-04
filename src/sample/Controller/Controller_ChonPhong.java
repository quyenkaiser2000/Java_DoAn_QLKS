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

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.model.ChonPhong;
import sample.model.Namelogin;
import sample.model.Oder_DichVu;
import sample.model.QLPhong;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller_ChonPhong implements Initializable {

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<ChonPhong> data;
    @FXML
    private AnchorPane anchorpane;

    @FXML
    private TableView<ChonPhong> table_info;

    @FXML
    private TableColumn<?, ?> col_maphong;

    @FXML
    private TableColumn<?, ?> col_tenloaiphong;

    @FXML
    private TableColumn<?, ?> col_dongia;

    @FXML
    private TextField txt_maphong;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = sample.DBConnection.qlksConnection();
        data = FXCollections.observableArrayList();
        setCellTable();
        LoadDataTableView();
    }

    private void setCellTable() {

        col_maphong.setCellValueFactory(new PropertyValueFactory<>("maphong"));
        col_tenloaiphong.setCellValueFactory(new PropertyValueFactory<>("tenlp"));
        col_dongia.setCellValueFactory(new PropertyValueFactory<>("dg"));
    }

    private void LoadDataTableView() {
        try {
            pst = con.prepareStatement("SELECT * FROM PHONG A, LOAI_PHONG B, LOAI_TINH_TRANG C WHERE A.MaLoaiPhong=B.MaLoaiPhong And A.MaLoaiTinhTrangPhong = C.MaLoaiTinhTrangPhong AND TenLoaiTinhTrangPhong LIKE N'%Phòng trống%'");
            rs = pst.executeQuery();
            while (rs.next()) {
                data.add(new ChonPhong(rs.getString(1), rs.getString(6), rs.getString(7)));

            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_info.setItems(data);

    }


    public void handleAddchonphong(ActionEvent event) {
        /*try {
            Stage stage1 =   (Stage) anchorpane.getScene().getWindow();
            stage1.close();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../fxml/ThuePhong.fxml"));
            Parent chonphong = loader.load();
            Scene scene = new Scene(chonphong);
            Controller_ThuePhong controller = loader.getController();
            ChonPhong selected = table_info.getSelectionModel().getSelectedItem();
            controller.chonphong(selected);
            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }*/
    }
   /* public void chonphong(ChonPhong chonPhong ){
        txt_maphong.setText(chonPhong.getMaphong());
    }*/
}

/*try {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxml/ThuePhong.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/ThuePhong.fxml"));
        Parent chonphong = loader.load();
        Scene scene = new Scene(chonphong);
        Controller_ThuePhong controller = loader.getController();
        ChonPhong selected = table_info.getSelectionModel().getSelectedItem();
        controller.chonphong(selected);

        Stage stage1 =   (Stage) anchorpane.getScene().getWindow();
        stage1.close();
        stage1.setScene(scene);
        stage1.show();
        }catch(Exception e){
        e.printStackTrace();
        e.getCause();
        }*/


