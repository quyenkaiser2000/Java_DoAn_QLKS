package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
    public static Connection qlksConnection(){
        Connection con = null;
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=QLKSLatest;username=sa;password=123";
            con = DriverManager.getConnection(url);
        }catch( ClassNotFoundException | SQLException ex){
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null,ex);
        }
        return con;
    }
}
