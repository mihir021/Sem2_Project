package Model;

import DBMS.Myconnection;
import java.sql.*;

import static Model.Color.CORAL_RED;
import static Model.Color.RESET;

public class BSTThread extends Thread{
    static Connection con;
    public BSTThread(){
        Myconnection my = new Myconnection();
        con = my.MyConnection(con);
    }
    @Override
    public void run() {
        synchronized (this) {
            String sql = "select * from student ";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Admin.studentBst.addNode(new Student(rs.getString(2), rs.getString(5),
                            rs.getString(3), rs.getString(4), rs.getString(6),
                            rs.getLong(1), rs.getInt(7), rs.getInt(8)));

                }
                this.notify();
            } catch (SQLException e) {
                System.out.println(CORAL_RED+"\t\t\t\t\t\t\t"+e+RESET);
            }
        }
        String sql = "select * from faculty";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Admin.facultyBST.addNode(new Faculty(rs.getInt(1),rs.getInt(8),rs.getString(2)
                ,rs.getString(5),rs.getString(3),rs.getString(4)
                ,rs.getString(6),rs.getString(7)));

            }
        } catch (SQLException e) {
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t"+e+RESET);

        }
        sql="select * from marks";
        try{
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                Faculty.hm.put(rs.getLong(1),rs.getDouble(2));
            }
        } catch (SQLException e) {
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t"+e+RESET);
        }
    }
}
