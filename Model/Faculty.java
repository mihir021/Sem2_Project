package Model;
import DBMS.Myconnection;
import DBMS.FacultyDB;
import DS.StudentBST;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static Model.Color.*;

public class Faculty {
    public Faculty left;
    public Faculty right;
    static Connection con;
    int facultyId, deptId, subjectId;
    String name, email, password;
    String contact, address, specialization;
    boolean facultyVerification;
    static Scanner sc=new Scanner(System.in);
    FacultyDB fDB;
    static HashMap<Long, Double> hm=new HashMap<>();

    public Faculty(int facultyId, int deptId, String name, String email, String password, String contact, String address, String specialization) {
        this.facultyId = facultyId;
        this.deptId = deptId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.contact = contact;
        this.address = address;
        this.specialization = specialization;
        subjectId=0;
    }
    public Faculty(){
        try {
            fDB = new FacultyDB();
            facultyVerification = fDB.checkFaculty();
            Myconnection my = new Myconnection();
            con = my.MyConnection(con);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    void facultyMenu() throws IOException, SQLException {
        String choice;
        do {
            System.out.println(PASTEL_YELLOW+"\t\t\t\t\t\t\t\t\t\t[1] Mark attendance");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[2] Add marks of student");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[3] Add Exam");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[4] View Details");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[0] Exit");
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.AQUA_MINT+"👉🏻 Enter Your Choice : "+Color.RESET);
            choice=sc.nextLine();

            switch(choice){
                case "1": markAttendance(); break;
                case "2": addMarks(); break;
                case "3": addExam(); break;
                case "4": viewMenu(); break;
                case "0":
                    System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.PASTEL_PURPLE+"Exiting from faculty Main Menu.....😇 "+Color.RESET);
                    break;
                default:
                    System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tInvalid Choice :("+RESET);
            }
        }while(!choice.equals("0"));
    }
    void viewMenu() throws IOException, SQLException {
        String choice;
        do{
            System.out.println(PASTEL_YELLOW+"\t\t\t\t\t\t\t\t\t\t[1] View Student");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[2] View LeaderBoard");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[3] View Attendance");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[4] View Marks by enrollmentNo");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[0] Exit"+RESET);
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.AQUA_MINT+"👉🏻 Enter Your Choice : "+Color.RESET);
            choice=sc.nextLine();
            switch (choice){
                case "1": viewStudent(con); break;
                case "2": viewLeaderBoard(con); break;
                case "3": viewAttendance(con); break;
                case "4": viewMarks(con); break;
                case "0":
                    System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.PASTEL_PURPLE+"Exiting from view Main Menu.....😇 "+Color.RESET);
                    break;
                default:
                    System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"Invalid Choice 😕"+Color.RESET);
            }
        }while(!choice.equals("0"));
    }

    void addMarks(){
        String sql;
        Statement st;
        System.out.print(SKY_TURQUOISE+"\t\t\t\t\t\t\t\t\t\tEnter Student's EnrollmentNo: "+RESET);
        long enrollNo;
        try{
            enrollNo =sc.nextLong();
        }catch (InputMismatchException e){
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tEnter valid enrollment number !!!!!!!!"+RESET);
            sc.nextLine();
            return;
        }
        sql="select * from student where enrollmentNo="+enrollNo;
        try{
            st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            if(rs.next()){
                int courseId=rs.getInt("courseID");
                sql="select * from subject where courseID="+courseId;
                rs= st.executeQuery(sql);
                boolean row=rs.next();
                if(row) {
                    double obtainedMarks=0;
                    while (row) {
                        System.out.print(SKY_TURQUOISE+"\t\t\t\t\t\t\t\t\t\tEnter marks of subject- "+rs.getString("subjectName")+": "+RESET);
                        double marks=sc.nextDouble();
                        obtainedMarks+=marks;
                        row=rs.next();
                    }
                    sc.nextLine();
                    sql="insert into marks values(?,?)";
                    PreparedStatement pst=con.prepareStatement(sql);
                    pst.setLong(1, enrollNo);
                    pst.setDouble(2, obtainedMarks);
                    int r1=pst.executeUpdate();
                    System.out.println( r1>0 ? ("\t\t\t\t\t\t\t\t\t\t"+Color.LIME_GREEN_GLOW+"Insertion of "+r1+" row is done ✅"+Color.RESET) : ("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"Insertion failed 😕"+Color.RESET));
                    hm.put(enrollNo,obtainedMarks);
                }
                else{
                    System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tNo subject's are present in this student's course"+RESET);
                    sc.nextLine();
                }
            }
            else{
                sc.nextLine();
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"EnrollmentNo doesn't exists 😕"+Color.RESET);
            }
        } catch (InputMismatchException e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"Input mismatch exception 😕"+Color.RESET);
            sc.nextLine();
        } catch (SQLException e) {
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tduplicate entry not allowed in primary key !!!"+RESET);
        }catch (Exception e){
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tEnter valid values !"+RESET);
        }
    }
    void addExam(){
        System.out.print(SKY_TURQUOISE+"\t\t\t\t\t\t\t\t\t\tEnter Subject-Id: "+RESET);
        int subId;
        try {
            subId=sc.nextInt();
        }catch (InputMismatchException e){
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tEnter valid number !!!!!!!! "+RESET);
            sc.nextLine();
            return;
        }
        sc.nextLine();
        String sql="select * from subject where subjectID="+subId;
        String timeStr;
        java.sql.Date sqlDate;
        try{
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            if(rs.next()){
                String subName = rs.getString(2);
                try {
                    System.out.print(SKY_TURQUOISE+"\t\t\t\t\t\t\t\t\t\tEnter a date (dd-MM-yyyy): ");
                    String inputDate = sc.nextLine();
                    // Define the format
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate date = LocalDate.parse(inputDate, formatter);
                    sqlDate = java.sql.Date.valueOf(date);

                    System.out.print("\t\t\t\t\t\t\t\t\t\tEnter exam time (HH:mm or HH:mm:ss): "+RESET);
                    timeStr = sc.nextLine();
                    if (timeStr.matches("\\d{2}:\\d{2}")) {
                        timeStr += ":00";
                    }
                }catch (Exception e){
                    System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"Enter valid date !!! 😕"+Color.RESET);
                    return;
                }

                java.sql.Time time = java.sql.Time.valueOf(timeStr);
                sql="insert into exam values(?,?,?,?)";
                PreparedStatement pst=con.prepareStatement(sql);
                pst.setInt(1, subId);
                pst.setString(2, subName);
                pst.setDate(3,sqlDate);
                pst.setTime(4, time);
                pst.executeUpdate();
                System.out.println(LIME_GREEN_GLOW+"\t\t\t\t\t\t\t\t\t\texam added successfully :) "+RESET);
            }
            else{
                System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tSubjectID doesn't exists :("+RESET);
            }
        } catch (SQLException e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+e+" 🙄😐"+Color.RESET);
        }
    }
    void markAttendance(){
        String sql="select subjectID from faculty where facultyID="+fDB.getFacultyId();
        try{
            Statement st = con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            rs.next();
            int subjectID = rs.getInt("subjectID");
            if (subjectID != 0) {
                System.out.print(SKY_TURQUOISE+"\t\t\t\t\t\t\t\t\t\tEnter student enrollmentNo: "+RESET);
                long enrollNo;
                try {
                    enrollNo=sc.nextLong();
                } catch (Exception e) {
                    System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tEnter valid enrollment number !!!!"+RESET);
                    sc.nextLine();
                    return;
                }
                sc.nextLine();
                Student student = StudentBST.searchPreOrder(enrollNo);
                if(student!=null) {
                    System.out.print(SKY_TURQUOISE+"\t\t\t\t\t\t\t\t\t\tEnter a date (dd-MM-yyyy): ");
                    String inputDate = sc.nextLine();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate date = LocalDate.parse(inputDate, formatter);
                    java.sql.Date sqlDate = java.sql.Date.valueOf(date);
                    System.out.print("\t\t\t\t\t\t\t\t\t\tStatus(Present/Absent): "+RESET);
                    String status = sc.nextLine();
                    sql = "insert into attendance values(?,?,?,?)";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setLong(1, enrollNo);
                    pst.setInt(2, subjectID);
                    pst.setDate(3, sqlDate);
                    pst.setString(4, status);
                    pst.executeUpdate();
                }
                else{
                    System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tStudent doesn't exists"+RESET);
                }
            }
            else {
                System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tFaculty doesn't take any subject, so marking attendance not possible"+RESET);
            }
        }
        catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+e+" 🙄😐"+Color.RESET);
        }
    }

    static void viewStudent(Connection con) throws IOException, SQLException {
        // for individual student
        System.out.print(SKY_TURQUOISE+"\t\t\t\t\t\t\t\t\t\tEnter student enrollment number : "+RESET);
        long enroll;
        try {
            enroll=sc.nextLong();
        } catch (Exception e) {
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tEnter valid enrollment number !!!!"+RESET);
            sc.nextLine();
            return;
        }
        sc.nextLine();
        BufferedWriter bw = new BufferedWriter(new FileWriter("StudentDetails"+enroll+".txt"));
        Student f=StudentBST.searchPreOrder(enroll);
        if(f == null){
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tenrollment number not found !!!!"+RESET);
            return;
        }
        bw.write("Student id = "+f.getEnrollNo()+"\nStudent name = "+f.getName()+"\nEmail-id = "+f.getEmail()+"\nAddress ="+f.getAddress()
                +"\nDept id = "+f.getDeptId()+"\ncourse id = "+f.getCourseId()+"\ncontact number = "+f.getContact()+"\n\n");
        bw.close();
        String sql = "select * from Student";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        BufferedWriter bw1 = new BufferedWriter(new FileWriter("StudentDetails.txt"));

        while (rs.next()) {
            bw1.write("Student enroll = " + rs.getLong(1) + "\nStudent name = " + rs.getString(2) + "\nEmail-id = " + rs.getString(5)
                    + "\nAddress =" + rs.getString(6) + "\ncourse id = " + rs.getString(8) + "contact number = " + rs.getString(4)
                    + "\nDept id = " + rs.getInt(7) + "\n\n");
        }

        System.out.println(LIME_GREEN_GLOW+"\t\t\t\t\t\t\t\t\t\tall student details are stored in --> StudentDetails.txt ");
        System.out.println("\t\t\t\t\t\t\t\t\t\tindividual student details are stored in -->  StudentDetails"+enroll+".txt"+RESET);
        bw1.close();
    }
    static void viewLeaderBoard(Connection con){
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter("Leaderboard.txt"));

            ArrayList<Map.Entry<Long,Double>> sortList=new ArrayList<>(hm.entrySet());
            sortList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
            int rank=1;
            for(Map.Entry<Long, Double> e: sortList) {
                long enrollNo = e.getKey();
                double marks = e.getValue();
                Student student = StudentBST.searchPreOrder(enrollNo);
                if(student == null){
                    continue;
                }
                System.out.println(VIVID_ORANGE+"\t\t\t\t\t\t\t\t\t\t"+("Rank: " + rank + ") Enrollment No.: " + enrollNo + ", Name: " + student.getName() + ", Marks:" + marks)+RESET);
                bw.write("Rank: " + rank + ") Enrollment No.: " + enrollNo + ", Name: " + student.getName() + ", Marks:" + marks + "\n");
                bw.flush();
                rank++;
            }
            if(rank == 1){
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED +"add marks 1st ! "+RESET);
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+e+" 🙄😐"+Color.RESET);
        }
    }
    static void viewMarks(Connection con) throws IOException, SQLException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("Marks.txt"));
        System.out.print(SKY_TURQUOISE+"\t\t\t\t\t\t\t\t\t\tEnter EnrollmentNo of student: "+RESET);
        long enroll;
        try {
            enroll=sc.nextLong();
        } catch (Exception e) {
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tEnter valid enrollment number !!!!"+RESET);
            sc.nextLine();
            return;
        }
        String sql = "select * from marks where enrollmentNo="+enroll;
        Statement s = con.createStatement();
        ResultSet rs = s.executeQuery(sql);
        if(!rs.next()){
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tdata not found !!!"+RESET);
            sc.nextLine();
            return;
        }

        Student student= StudentBST.searchPreOrder(enroll);
        try {
            if(student==null){
                System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tstudent not found !"+RESET);
                return;
            }
            rs = s.executeQuery(sql);
            if(rs.next()){
                System.out.println(BRIGHT_CYAN+("\t\t\t\t\t\t\t\t\t\tEnrollment No.: "+student.getEnrollNo()+", Name: "
                        +student.getName()+", Total Marks: "+rs.getDouble(2))+RESET);
                bw.write("Enrollment No.: "+student.getEnrollNo()+", Name: "
                        +student.getName()+", Total Marks: "+rs.getDouble(2));
                sc.nextLine();
            }
            else{
                System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tMarks are not present for entered enrollmentNo"+RESET);
            }
            bw.close();
        } catch (SQLException e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+e+" 🙄😐"+Color.RESET);
        }
    }
    static void viewAttendance(Connection con) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("Attendance.txt"));

        System.out.print(SKY_TURQUOISE+"\t\t\t\t\t\t\t\t\t\tEnter EnrollmentNo: "+RESET);
        long enrollNo;
        try {
            enrollNo=sc.nextLong();
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"Enter valid enrollment number !!!! 😕"+Color.RESET);
            sc.nextLine();
            return;
        }
        sc.nextLine();
        String sql = "SELECT * FROM attendance a " +
                "INNER JOIN subject sb ON a.subjectID = sb.subjectID " +
                "WHERE a.enrollmentNo = ?";
        try {
            if(con==null){
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"con is null ! 😕"+Color.RESET);
                return;
            }
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setLong(1, enrollNo);
            ResultSet rs = pst.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(BRIGHT_CYAN+"\t\t\t\t\t\t\t\t\t\t"+(enrollNo + " was " + rs.getString("status") +
                        " in " + rs.getString("subjectName") +
                        " on " + rs.getDate("date"))+RESET);
                bw.write(enrollNo + " was " + rs.getString("status") +
                        " in " + rs.getString("subjectName") +
                        " on " + rs.getDate("date")+"\n");
            }
            bw.flush();
            if (!found) {
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"No attendance records found. 😕"+Color.RESET);
            }
            bw.close();
        } catch (SQLException e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"Error fetching attendance: \" + e.getMessage() 😕"+Color.RESET);
        }
    }

    static void generateFacultyPdf(Faculty f, Connection con){
        Document document=new Document();
        try {
            Statement st= con.createStatement();
            ResultSet rs=st.executeQuery("select * from facultyFiles where facultyID="+f.facultyId);
            if(!rs.next()) {
                PdfWriter.getInstance(document, new FileOutputStream("faculty" + f.facultyId + ".pdf"));
                document.open();
                document.add(new Paragraph("Faculty Details"));
                document.add(new Paragraph("FacultyID: " + f.facultyId));
                document.add(new Paragraph("Name: " + f.name));
                document.add(new Paragraph("Password: " + f.password));
                document.add(new Paragraph("Contact: " + f.contact));
                document.add(new Paragraph("Email: " + f.email));
                document.add(new Paragraph("Address: " + f.address));
                document.add(new Paragraph("Department-ID: " + f.deptId));
                document.add(new Paragraph("Subject-ID: " + f.subjectId));
                document.close();
                String sql = "insert into facultyFiles values(?,?,?)";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setLong(1, f.facultyId);
                String file = f.facultyId+ ".pdf";
                pst.setString(2, file);
                pst.setBinaryStream(3, new FileInputStream("faculty" + file));

                pst.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+e+" 🙄😐"+Color.RESET);
        }
    }
    static void downloadFacultyPdf(int FacultyID, Connection con){
        String sql="select * from facultyFiles where FacultyID="+FacultyID;
        try {
            Statement st = con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            if(rs.next()){
                String fileName=rs.getString(2);
                Blob b=rs.getBlob(3);
                byte[] data=b.getBytes(1,(int)b.length());
                FileOutputStream fos=new FileOutputStream(fileName+".pdf");
                fos.write(data);
                fos.close();
            }
            else{
                System.out.println("FacultyID Not Found :(");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getFacultyId() {
        return facultyId;
    }
    public int getSubjectId(){
        return subjectId;
    }
    public String getSpecialization() {
        return specialization;
    }
    public int getDeptId() {
        return deptId;
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

    @Override
    public String toString() {
        return "Faculty{" +
                "left=" + left +
                ", right=" + right +
                ", facultyId=" + facultyId +
                ", deptId=" + deptId +
                ", subjectId=" + subjectId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", contact='" + contact + '\'' +
                ", address='" + address + '\'' +
                ", specialization='" + specialization + '\'' +
                ", facultyVerification=" + facultyVerification +
                ", fDB=" + fDB +
                '}';
    }
}
