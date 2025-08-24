package DBMS;

import Model.Student;

import java.sql.*;
import java.util.Scanner;

import static Model.Color.*;

public class StudentDB {
    Connection con;
    Scanner sc=new Scanner(System.in);
    public static long enroll = 0;

    public StudentDB(){
        try {
            Myconnection my = new Myconnection();
            con = my.MyConnection(con);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public long getEnroll() {
        return enroll;
    }
    public boolean checkStudent(){
        try {
            System.out.print(SKY_TURQUOISE+"\t\t\t\t\t\t\t\t\t\tEnter Student Name: "+RESET);
            String studName = sc.nextLine();
            System.out.print(SKY_TURQUOISE+"\t\t\t\t\t\t\t\t\t\tEnter Enrollment-No: "+RESET);
            enroll=sc.nextLong();
            sc.nextLine();
            System.out.print(SKY_TURQUOISE+"\t\t\t\t\t\t\t\t\t\tEnter Password: "+RESET);
            String studPass = sc.nextLine();
            String sSearch = " select * from student where studentName='" + studName + "' and enrollmentNo=" + enroll + " and" +
                    " studentPassword='" + studPass + "' ;";
            Statement st = con.createStatement();
            ResultSet r = st.executeQuery(sSearch);

            boolean fix2 = r.next();
            if(!fix2){
                return false;
            }
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select * from notificationstudent  where enrollmentNo="+enroll);
            boolean fix = true;
            while (rs.next()){
                System.out.println(LIME_GREEN_GLOW+"\t\t\t\t\t\t\t\t\t\tnotification : "+rs.getString(3)+" !!!!!!!"+RESET);
                fix = false;
            }
            if(fix){
                System.out.println(BRIGHT_CYAN+"\t\t\t\t\t\t\t\t\t\tno notifications !!"+RESET);
            }
            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public void addStudentDB(Student s) throws Exception{
        String sInsert=" insert into student" +
                " values(?,?,?,?,?,?,?,?)";
        PreparedStatement pst=con.prepareStatement(sInsert);
        pst.setString(2, s.getName());
        pst.setLong(1, s.getEnrollNo());
        pst.setString(3, s.getPassword());
        pst.setString(4, s.getContact());
        pst.setString(5, s.getEmail());
        pst.setString(6, s.getAddress());
        pst.setInt(7, s.getDeptId());
        pst.setInt(8, s.getCourseId());
        int r=pst.executeUpdate();

        System.out.println("\t\t\t\t\t\t\t\t\t\t"+(r > 0 ? LIME_GREEN_GLOW+"Insertion of " + r + " row is done :)"+RESET : CORAL_RED+"Insertion failed"+RESET));
    }
}
