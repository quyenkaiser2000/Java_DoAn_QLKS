package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Dialog.AlertDialog;
import sample.Validation.Validation_TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import sample.model.Namelogin;

public class ControllerLogin implements Initializable {

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;

    @FXML
    private TextField txt_username;

    @FXML
    private Button btn_login;

    @FXML
    private PasswordField txt_password;

    @FXML
    private AnchorPane anchorpane;
    @FXML
    private Label error_username;

    @FXML
    private Label error_password;
    @FXML
    private Label lable_setname;


    public void setnamelogin(Namelogin namelogin ){
        lable_setname.setText(namelogin.getManv());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = sample.DBConnection.qlksConnection();
    }
    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        boolean isUserName = Validation_TextField.isTextFieldNotEmpty(txt_username, error_username,"Sai UserName kìa !!");
        boolean isPassWord = Validation_TextField.isTextFieldNotEmpty(txt_password, error_password,"Sai PassWord kìa !!");

        if(isUserName  && isPassWord) {
            if (txt_username.getText().equals(getUserName()) && txt_password.getText().equals(getPassword())) {
                String sql = "SELECT * FROM NHAN_VIEN A, TAI_KHOAN B, CHUC_VU C WHERE A.MaNhanVien=B.MaNhanVien And A.MaChucVu = C.MaChucVu AND UserName LIKE N'%"+txt_username.getText()+"%'";
                try {
                    String tennv = null;
                    String manv = null;
                    try{
                        pst = con.prepareStatement(sql);
                        rs= pst.executeQuery();
                        if(rs.next()) {
                            tennv = rs.getString(3);
                            manv = rs.getString(1);
                        }
                    }catch (SQLException ex){
                        Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    stage.setTitle("Trang Chủ");
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("../fxml/trangchu.fxml"));
                    Parent setnamelogin = loader.load();
                    Scene scene = new Scene(setnamelogin);
                    Controller_TrangChu controller = loader.getController();
                    Namelogin name = new Namelogin();
                    name.setUsername(tennv);
                    controller.setnamelogin(name);
                    stage.setScene(scene);

                   /* Stage stage1 = (Stage)((Node) event.getSource()).getScene().getWindow();
                    FXMLLoader loader1 = new FXMLLoader();
                    loader1.setLocation(getClass().getResource("../fxml/ThuePhong.fxml"));
                    Parent manhanvien = loader.load();
                    Scene scene1 = new Scene(manhanvien);
                    Controller_ThuePhong controller1 = loader.getController();
                    Namelogin tk1 = new Namelogin();
                    tk.setManv(manv);
                    controller1.manhanvien(tk1);
                    stage1.setScene(scene1);*/
                }catch(Exception e){
                    e.printStackTrace();
                    e.getCause();
                }

            } else {
                AlertDialog.display("Thông báo", "Lỗi đăng nhập");

            }

        }

    }
    public String getUserName(){
        String username = "";
        try{

            pst = con.prepareStatement("SELECT UserName FROM TAI_KHOAN WHERE username = ?");
            pst.setString(1,txt_username.getText());
            rs = pst.executeQuery();
            if(rs.next())
            {
                username=rs.getString(1);
            rs.close();
            }
        }catch(SQLException ex){
            Logger.getLogger(ControllerLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return username;
    }
    public String getPassword(){
        String password = "";
        try{

            pst = con.prepareStatement("SELECT Password FROM TAI_KHOAN WHERE password = ?");
            pst.setString(1,txt_password.getText());
            rs = pst.executeQuery();
            if(rs.next())
                password=rs.getString(1);
            rs.close();

        }catch(SQLException ex){
            Logger.getLogger(ControllerLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return password;
    }

}


