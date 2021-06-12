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

public class Controller_ChangePassword implements Initializable {

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;

    @FXML
    private AnchorPane anchorpane;
    @FXML
    private TextField txt_manv;

    @FXML
    private PasswordField txt_password;

    @FXML
    private TextField txt_username;

    @FXML
    private Button btn_doimak;

    @FXML
    private Label error_manv;

    @FXML
    private Label error_username;

    @FXML
    private Label error_newpas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = sample.DBConnection.qlksConnection();
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
            Logger.getLogger(Controller_ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
        }
        return username;
    }
    public String getManv(){
        String manv = "";
        try{

            pst = con.prepareStatement("SELECT MaNhanVien FROM TAI_KHOAN WHERE MaNhanVien = '"+txt_manv.getText()+"'");
            rs = pst.executeQuery();
            if(rs.next())
                manv=rs.getString(1);
            rs.close();

        }catch(SQLException ex){
            Logger.getLogger(Controller_ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
        }
        return manv;
    }

    public void handleDoiMatKhau(ActionEvent event) {
        boolean isPassWord = Validation_TextField.isTextFieldNotEmpty(txt_password, error_newpas,"Nhập Password!!");
        boolean isUserName = Validation_TextField.isTextFieldNotEmpty(txt_username, error_username,"Nhập UserName!!");
        boolean ismanv = Validation_TextField.isTextFieldNotEmpty(txt_manv, error_manv,"Nhập MaNV!!");
        if(ismanv && isUserName && isPassWord)
        {
            if (txt_username.getText().equals(getUserName()) && txt_manv.getText().equals(getManv()))
            {
                String sql = "Update TAI_KHOAN  set  Password = ? WHERE UserName = ? ";

                try {
                    String username = txt_username.getText();
                    String pass = txt_password.getText();

                    pst = con.prepareStatement(sql);


                    pst.setString(1, pass);
                    pst.setString(2, username);
                    int i = pst.executeUpdate();
                    if (i == 1) {
                        System.out.println("Data Insert Successfully");
                        AlertDialog.display("Thông báo","Đổi mật khẩu thành công");
                        Stage stage =   (Stage) anchorpane.getScene().getWindow();
                        stage.close();
                    }
                } catch (SQLException  ex) {
                    Logger.getLogger(Controller_NhanVien.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                AlertDialog.display("Thông báo", "Mã nhân viên hoặc username lỗi!!");
                txt_password.clear();
                txt_manv.clear();
                txt_username.clear();
            }
        }
    }
}


