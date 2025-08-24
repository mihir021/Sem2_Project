package Model;
import DBMS.Myconnection;
import DBMS.StudentDB;
import DS.StudentBST;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;

import static Model.Color.*;

// this would be filled by admin
public class Student {
    public Student left;
    public Student right;
    String name, email, password, contact, address;
    long enrollNo;
    //String mentor_name;
    int deptId, courseId;
    boolean studentVerification;
    static Connection con;
    Scanner sc=new Scanner(System.in);

    public Student(String name, String email, String password, String contact, String address, long enrollNo,
                   int deptId, int courseId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.contact = contact;
        this.address = address;
        this.enrollNo = enrollNo;
        this.deptId = deptId;
        this.courseId = courseId;
    }

    StudentDB sDB;
    public Student(){
        try {
            sDB = new StudentDB();
            studentVerification=sDB.checkStudent();
            Myconnection my = new Myconnection();
            con = my.MyConnection(con);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    void studentMenu(){
        String choice;
        do {
            System.out.println(PASTEL_YELLOW+"\t\t\t\t\t\t\t\t\t\tView Menu:");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[1]. Profile");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[2]. Marks");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[3]. Attendance");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[4]. Leaderboard");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[5]. Events");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[6]. Exams");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[0]. Exit"+RESET);
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.AQUA_MINT+"👉🏻 Enter Your Choice : "+Color.RESET);
            choice=sc.nextLine();

            switch(choice){
                case "1": viewProfile(); break;
                case "2": viewMarks(); break;
                case "3": viewAttendance(StudentDB.enroll); break;
                case "4": viewLeaderBoard(); break;
                case "5": viewEvent(); break;
                case "6": viewExam(); break;
                case "0":
                    System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.PASTEL_PURPLE +"Exiting from Student Main Menu.....😇 "+Color.RESET);
                    break;
                default:
                    System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"Invalid Choice 😕"+Color.RESET);
            }
        }while(!choice.equals("0"));
    }

    void viewEvent(){
        String sql="select * from event";
        try {
            Statement st = con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                System.out.println(BRIGHT_CYAN+"\t\t\t\t\t\t\t\t\t\tEventNo: "+rs.getInt(1));
                System.out.println("\t\t\t\t\t\t\t\t\t\tEvent Name: "+rs.getString(2));
                System.out.println("\t\t\t\t\t\t\t\t\t\tEvent Date: "+rs.getDate(3));
                System.out.println("\t\t\t\t\t\t\t\t\t\tAbout Event: "+rs.getString(4)+"\n"+RESET);
            }
        } catch (SQLException e) {
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tno events at this time :( "+RESET);
        }
    }
    void viewExam(){
        String sql="select * from exam";
        try {
            Statement st = con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                System.out.println(BRIGHT_CYAN+"\t\t\t\t\t\t\t\t\t\tEventNo: "+"SubjectID: "+rs.getInt(1));
                System.out.println("\t\t\t\t\t\t\t\t\t\tSubject Name: "+rs.getString(2));
                System.out.println("\t\t\t\t\t\t\t\t\t\tExam Date: "+rs.getDate(3));
                System.out.println("\t\t\t\t\t\t\t\t\t\tExam Time: "+rs.getTime(4)+"\n"+RESET);
            }
        } catch (SQLException e) {
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tno exam at this time :( "+RESET);
        }
    }
    void viewProfile(){
        Student student= StudentBST.searchPreOrder(sDB.getEnroll());
        System.out.println(LIME_GREEN_GLOW+"\t\t\t\t\t\t\t\t\t\t"+student+RESET);
    }
    void viewAttendance(long enrollNo){
        String sql = "SELECT * FROM attendance a " +
                "INNER JOIN subject sb ON a.subjectID = sb.subjectID " +
                "WHERE a.enrollmentNo = ?";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setLong(1, enrollNo);
            ResultSet rs = pst.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+(BRIGHT_CYAN+enrollNo + " was " + rs.getString("status") +
                        " in " + rs.getString("subjectName") +
                        " on " + rs.getDate("date")+RESET) );
            }
            if (!found) {
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+BRIGHT_CYAN+"No attendance records found."+RESET);
            }
        } catch (SQLException e) {
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tError fetching attendance: " + e.getMessage()+RESET);
        }
    }
    void viewLeaderBoard(){
        ArrayList<Map.Entry<Long,Double>> sortList=new ArrayList<>(Faculty.hm.entrySet());
        sortList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        int rank=1;
        for(Map.Entry<Long, Double> e: sortList){
            long enrollNo=e.getKey();
            double marks=e.getValue();
            Student student= StudentBST.searchPreOrder(enrollNo);
            if(student == null){
                System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tdata not found !"+RESET);
                return;
            }
            System.out.println(BRIGHT_CYAN+"\t\t\t\t\t\t\t\t\t\t"+("Rank: "+rank+") Enrollment No.: "+enrollNo+", Name: "+student.getName()+", Marks:"+marks)+RESET);
            rank++;
        }
        if(rank == 1){
            System.out.println(BRIGHT_CYAN+"\t\t\t\t\t\t\t\t\t\tmarks are not added !"+RESET);
        }
    }
    void viewMarks(){
        String sql="select * from marks where enrollmentNo="+sDB.getEnroll();
        Student student= StudentBST.searchPreOrder(sDB.getEnroll());
        try {
            Statement st = con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            if(student == null){
                System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tdata not found !"+RESET);
                return;
            }
            if(rs.next()){
                System.out.println(BRIGHT_CYAN+"\t\t\t\t\t\t\t\t\t\t"+("Enrollment No.: "+student.getEnrollNo()+", Name: "+student.getName()+", Total Marks: "+rs.getDouble(2))+RESET);
            }
            else{
                System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tMarks are not present for entered enrollmentNo"+RESET);
            }
        } catch (SQLException e) {
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tmarks does not assign "+RESET);
        }
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getContact() {
        return contact;
    }
    public String getAddress() {
        return address;
    }
    public long getEnrollNo() {
        return enrollNo;
    }
    public int getDeptId() {
        return deptId;
    }
    public int getCourseId() {
        return courseId;
    }

    static void generateStudentPdf(Student s, Connection con){
        Document document=new Document();
        try {
            Statement st= con.createStatement();
            ResultSet rs=st.executeQuery("select * from studentFiles where enrollmentNo="+s.enrollNo);
            if(!rs.next()) {
                PdfWriter.getInstance(document, new FileOutputStream("student" + s.enrollNo + ".pdf"));
                document.open();
                document.add(new Paragraph("Student Details"));
                document.add(new Paragraph("EnrollmentNo: " + s.enrollNo));
                document.add(new Paragraph("Name: " + s.name));
                document.add(new Paragraph("Password: " + s.password));
                document.add(new Paragraph("Contact: " + s.contact));
                document.add(new Paragraph("Email: " + s.email));
                document.add(new Paragraph("Address: " + s.address));
                document.add(new Paragraph("Department-ID: " + s.deptId));
                document.add(new Paragraph("Course-ID: " + s.courseId));
                document.close();
                String sql = "insert into studentFiles values(?,?,?)";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setLong(1, s.enrollNo);
                //"C:\PROJECT_SEM2\student24002170210035.pdf"
                String file = s.enrollNo + ".pdf";
                pst.setString(2, file);
                pst.setBinaryStream(3, new FileInputStream("student" + file));
                int r = pst.executeUpdate();
                //System.out.println(r>0? "File Uploaded": "File is not uploaded");
            }
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+e+" 🙄😐"+Color.RESET);
        }
    }
    static void downloadStudentPdf(long enrollNo, Connection con){
        String sql="select * from studentFiles where enrollmentNo="+enrollNo;
        try {
            Statement st = con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            if(rs.next()){
                String fileName=rs.getString(2);
                Blob b=rs.getBlob(3);
                byte[] data=b.getBytes(1,(int)b.length());
                FileOutputStream fos=new FileOutputStream("C:\\Sem2_Project\\DownloadedPDF\\"+fileName);
                fos.write(data);
                fos.close();
            }
            else{
                System.out.println("EnrollmentNo Not Found :(");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String toString() {
        return "Student Information :\n" +
                "\t\t\t\t\t\t\t\t\t\t courseId =" + courseId +"\n"+
                "\t\t\t\t\t\t\t\t\t\t deptId =" + deptId +"\n"+
                "\t\t\t\t\t\t\t\t\t\t enrollNo =" + enrollNo +"\n"+
                "\t\t\t\t\t\t\t\t\t\t address ='" + address + '\'' +"\n"+
                "\t\t\t\t\t\t\t\t\t\t contact ='" + contact + '\'' +"\n"+
                "\t\t\t\t\t\t\t\t\t\t password ='" + password + '\'' +"\n"+
                "\t\t\t\t\t\t\t\t\t\t email ='" + email + '\'' +"\n"+
                "\t\t\t\t\t\t\t\t\t\t name ='" + name + '\'' +"\n";
    }
}
