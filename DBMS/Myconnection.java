package DBMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static Model.Color.CORAL_RED;
import static Model.Color.RESET;

public class Myconnection {
        Connection con1;
        public Connection MyConnection(Connection con) {
            String db_url = "jdbc:mysql://localhost:3306/module";
            String db_user = "root";
            String db_pass = "";
            String driver = "com.mysql.cj.jdbc.Driver";
            try{
                Class.forName(driver);
                con1 = DriverManager.getConnection(db_url, db_user, db_pass);
                return con1;
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+CORAL_RED+e+" 🙄😐"+RESET);
            }
            return con1;
        }
}
