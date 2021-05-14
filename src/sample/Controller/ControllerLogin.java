package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.model.NhanVien;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = sample.DBConnection.qlksConnection();
    }
    @FXML
    private void handleLogin(ActionEvent event){

    }
    /*private String getUserName(){
        try{
            String username = "";
            pst = con.prepareStatement("SELECT UserName FROM TAI_KHOAN WHERE UserName = ?");
            pst.setString(1,txt_username.getText());
            rs = pst.executeQuery();
            if(rs.next())
                username=rs.getString(1);
            return username;

        }catch(SQLException ex){
            Logger.getLogger(ControllerLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}


