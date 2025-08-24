package DBMS;
import DS.Action;
import DS.FacultyBST;
import Model.Color;
import Model.Faculty;
import Model.BSTThread;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

import static Model.Admin.historyStack;
import static Model.Color.*;

public class FacultyDB {
    int facultyId;
    static Connection con;
    Scanner sc=new Scanner(System.in);

    public FacultyDB(){
        Myconnection my = new Myconnection();
        con = my.MyConnection(con);
    }
    public int getFacultyId() {
        return facultyId;
    }
    public boolean checkFaculty(){
        try {
            System.out.print(SKY_TURQUOISE+"\t\t\t\t\t\t\t\t\t\tEnter Faculty Name: ");
            String facultyName = sc.nextLine();
            System.out.print("\t\t\t\t\t\t\t\t\t\tEnter Faculty Id: ");
            facultyId= sc.nextInt();
            sc.nextLine();
            System.out.print("\t\t\t\t\t\t\t\t\t\tEnter Password: "+RESET);
            String facultyPass = sc.nextLine();
            String sSearch = " select * from faculty where facultyName = ? and facultyID = ? and " +
                    " facultyPassword= ? ;";
            PreparedStatement  st = con.prepareStatement(sSearch);
            st.setString(1,facultyName);
            st.setInt(2,facultyId);
            st.setString(3,facultyPass);
            ResultSet r = st.executeQuery(sSearch);
            if(!r.next()){
                return false;
            }
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select * from notificationfaculty  where facultyId="+facultyId);

            boolean fix = true;
            while (rs.next()){
                System.out.println(BRIGHT_CYAN+"\t\t\t\t\t\t\t\t\t\tnotification : "+rs.getString(3)+" !!!!!!!"+RESET);
                fix = false;
            }
            if(fix){
                System.out.println(BRIGHT_CYAN+"\t\t\t\t\t\t\t\t\t\tno notifications !!"+RESET);
            }

            return true;
        }
        catch (Exception e){
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tinput mismatch exception !"+RESET);
            return false;
        }
    }
    public void addFacultyDB(Faculty f) {
        try {
            String sInsert = " insert into faculty" +
                    " values(?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sInsert);
            pst.setInt(1, f.getFacultyId());
            pst.setString(2, f.getName());
            pst.setString(5, f.getEmail());
            pst.setString(3, f.getPassword());
            pst.setInt(8, f.getDeptId());
            pst.setString(4, f.getContact());
            pst.setString(6, f.getAddress());
            pst.setString(7, f.getSpecialization());
            pst.setInt(9, f.getSubjectId());
            int r = pst.executeUpdate();
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+(r > 0 ? LIME_GREEN_GLOW+"Insertion of " + r + " row is done :)"+RESET : CORAL_RED+"Insertion failed"+RESET));
        } catch (Exception e) {
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tFaculty Id already exists !"+RESET);
        }
    }
    public void editFaculty(int id, String name){
        String sql = "{call updateFaculty(?,?,?,?)}";
        try {
            String sSearch = "select * from faculty where facultyId = ? and facultyName = ?";
            PreparedStatement pst = con.prepareStatement(sSearch);
            pst.setInt(1,id);
            pst.setString(2,name);
            ResultSet rs=pst.executeQuery();
            if(rs!=null){
                String choice;

                Faculty faculty = FacultyBST.searchPreOrder(id);
                Action a = new Action("editFaculty", faculty);
                a.setTableName("faculty");
                historyStack.push(a);

                    System.out.println(PASTEL_YELLOW+"""
                            \t\t\t\t\t\t\t\t\t\t1) Update Contact No.
                            \t\t\t\t\t\t\t\t\t\t2) Update Email
                            \t\t\t\t\t\t\t\t\t\t3) Update Address
                            \t\t\t\t\t\t\t\t\t\t4) Update Specialization
                            \t\t\t\t\t\t\t\t\t\t5) Update DepartmentID
                            \t\t\t\t\t\t\t\t\t\t6) SubjectId""");
                    System.out.println("\t\t\t\t\t\t\t\t\t\t7) update faculty name.");
                    System.out.println("\t\t\t\t\t\t\t\t\t\t0) Exit"+RESET);
                    System.out.print(AQUA_MINT+"\t\t\t\t\t\t\t\t\t\tEnter your choice: "+RESET);
                    choice=sc.nextLine();
                PreparedStatement ps1 = con.prepareStatement(sql);
                switch(choice){

                        case "1":
                            System.out.print(NEON_PINK+"\t\t\t\t\t\t\t\t\t\tEnter Contact No. :"+RESET);
                            String contact = sc.nextLine();
                            if(contact.length() != 10){
                                System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tEnter valid values !"+RESET);
                                return;
                            }
                            ps1.setInt(1, id);
                            ps1.setString(2, choice);
                            ps1.setString(3, contact);
                            ps1.setInt(4, 0);
                            ps1.executeUpdate();
                            break;

                        case "2":
                            System.out.print(NEON_PINK+"\t\t\t\t\t\t\t\t\t\tEnter Email :"+RESET);
                            String email=sc.nextLine();
                            if(!email.endsWith("@ljku.edu.in")){
                                System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"Enter valid Values 😕"+Color.RESET);
                                return;
                            }
                            ps1 = con.prepareStatement(sql);
                            ps1.setInt(1, id);
                            ps1.setString(2, choice);
                            ps1.setString(3, email);
                            ps1.setInt(4, 0);
                            ps1.executeUpdate();
                            break;
                        case "3":
                            System.out.print(NEON_PINK+"\t\t\t\t\t\t\t\t\t\tEnter Address :"+RESET);
                            String address=sc.nextLine();
                            if(address.isEmpty()){
                                System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"Enter valid Values 😕"+Color.RESET);
                                return;
                            }
                            ps1 = con.prepareStatement(sql);
                            ps1.setInt(1, id);
                            ps1.setString(2, choice);
                            ps1.setString(3, address);
                            ps1.setInt(4, 0);
                            ps1.executeUpdate();
                            break;
                        case "4":
                            System.out.print(NEON_PINK+"\t\t\t\t\t\t\t\t\t\tEnter Specialization :"+RESET);
                            String sp=sc.nextLine();
                            if(sp.isEmpty()){
                                System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"Enter valid Values 😕"+Color.RESET);
                                return;
                            }
                            ps1 = con.prepareStatement(sql);
                            ps1.setInt(1, id);
                            ps1.setString(2, choice);
                            ps1.setString(3, sp);
                            ps1.setInt(4, 0);
                            ps1.executeUpdate();
                            break;
                        case "5":
                            System.out.print(NEON_PINK+"\t\t\t\t\t\t\t\t\t\tEnter Department-ID :"+RESET);
                            int deptID ;
                            try{
                                deptID = sc.nextInt();
                                if(deptID <= 0){
                                    System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tEnter valid values !"+RESET);
                                    return;
                                }
                            }catch (InputMismatchException e){
                                System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tEnter valid department id :( "+RESET);
                                return;
                            }
                            try {
                                String checkDId = "select * from department where deptId = "+deptID;
                                Statement s = con.createStatement();
                                rs = s.executeQuery(checkDId);
                                rs.next();
                                rs.getString(2);
                            } catch (SQLException e) {
                                System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"department Id does not exists 😕"+ RESET);
                                sc.nextLine();
                                return;
                            }
                            sc.nextLine();
                            ps1 = con.prepareStatement(sql);
                            ps1.setInt(1, id);
                            ps1.setString(2, choice);
                            ps1.setString(3, "");
                            ps1.setInt(4, deptID);
                            ps1.executeUpdate();
                            break;

                        case "6":
                            System.out.print(NEON_PINK+"\t\t\t\t\t\t\t\t\t\tEnter Subject-ID :"+RESET);
                            int SubjectID ;
                            try{
                                SubjectID =sc.nextInt();
                                if(SubjectID <= 0){
                                    System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tEnter valid values !"+RESET);
                                    return;
                                }
                            }catch (InputMismatchException e){
                                System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tEnter valid subject id :( "+RESET);
                                return;
                            }
                            try {
                                String checkDId = "select * from Subject where SubjectID = "+SubjectID;
                                Statement s = con.createStatement();
                                rs = s.executeQuery(checkDId);
                                rs.next();
                                rs.getString(2);
                            } catch (SQLException e) {
                                System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"subject Id does not exists 😕"+ RESET);
                                sc.nextLine();
                                return;
                            }
                            sc.nextLine();
                            ps1 = con.prepareStatement(sql);
                            ps1.setInt(1, id);
                            ps1.setString(2, choice);
                            ps1.setString(3, "");
                            ps1.setInt(4, SubjectID);
                            ps1.executeUpdate();
                            break;

                        case "7":
                            System.out.print(NEON_PINK+"\t\t\t\t\t\t\t\t\t\tEnter faculty name :"+RESET);
                            String newName = sc.nextLine();
                            if(newName.isEmpty()){
                                System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tEnter valid values !"+RESET);
                                return;
                            }
                            ps1 = con.prepareStatement(sql);
                            ps1.setInt(1, id);
                            ps1.setString(2, choice);
                            ps1.setString(3, newName);
                            ps1.setInt(4, 0);
                            ps1.executeUpdate();
                            break;

                        case "0":
                            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ Color.PASTEL_PURPLE +"Exiting from Edit Menu.....😇 "+Color.RESET);
                            break;
                        default:
                            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"Invalid Choice 😕"+Color.RESET);
                    }

                FacultyBST.root = null;
                BSTThread th = new BSTThread();
                th.start();
                th.join();
            }
            else{
                System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tStudent having entered enrollmentNo or name is not exists"+RESET);
            }
        } catch (Exception e) {
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tid not found !!!"+RESET);
        }
    }
}

