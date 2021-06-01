package sample.Controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.model.Namelogin;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller_TrangChu implements Initializable {

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<Namelogin> data;
    @FXML
    private Label lable_setname;

    @FXML
    private AnchorPane anchorpane;
    @FXML
    private TextField txt_manv;
    @FXML
    private Label lable_setma;
    @FXML
    private Label lable_test;
    @FXML
    private ImageView img_br;

    public void manhanvien(Namelogin namelogin){
        lable_test.setText(namelogin.getManv());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = sample.DBConnection.qlksConnection();

    }


    public void handleNhanVien(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/NhanVien.fxml"));

        Stage stage =   new Stage();
        stage.setTitle("NhanVien");
        Scene scene =   new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleDichVu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/DichVu.fxml"));
        Stage stage =   new Stage();
        stage.setTitle("DichVu");
        Scene scene =   new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleQLPhong(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/QLPhong.fxml"));
        Stage stage =   new Stage();
        stage.setTitle("Quản Lý Phòng");
        Scene scene =   new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handlePhieuThue(ActionEvent event) throws IOException {
        try{


            Stage stage =   new Stage();
            stage.setTitle("Phiếu Thuê");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../fxml/ThuePhong.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Controller_ThuePhong controller = loader.getController();
            Namelogin nnv = new Namelogin();
            nnv.setManv(lable_setname.getText());
            controller.manhanvien(nnv);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }


    }
    public void setnamelogin(Namelogin namelogin ){
        lable_setname.setText(namelogin.getUsername());
    }



    public void handle_logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/sample.fxml"));
        Stage stage =   (Stage) anchorpane.getScene().getWindow();
        stage.close();
        Scene scene =   new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}


