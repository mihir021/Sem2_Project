package Model;
import DBMS.BackUp;
import DBMS.Myconnection;
import DBMS.FacultyDB;
import DBMS.StudentDB;
import DS.Action;
import DS.FacultyBST;
import DS.HistoryStack;
import DS.StudentBST;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

import static Model.Color.*;

public class Admin {
    Connection con;
    public static HistoryStack<Action> historyStack=new HistoryStack<>();
    Scanner sc = new Scanner(System.in);
    static StudentBST studentBst = new StudentBST();
    static FacultyBST facultyBST = new FacultyBST();
    static class courseInfo{
        int id;
        int did;
        String courseName;

        public courseInfo(int id, int did, String courseName) {
            this.id = id;
            this.did = did;
            this.courseName = courseName;
        }

        public int getId() {
            return id;
        }

        public int getDid() {
            return did;
        }

        public String getCourseName() {
            return courseName;
        }
    }
    static class EventInfo{
        int eventNo;
        String eventName;
        LocalDate date;

        String aboutEvent;

        public EventInfo(int eventNo, String eventName, LocalDate date, String aboutEvent) {
            this.eventNo = eventNo;
            this.eventName = eventName;

            this.date = date;
            this.aboutEvent = aboutEvent;
        }

        public int getEventNo() {
            return eventNo;
        }

        public String getEventName() {
            return eventName;
        }

        public LocalDate getDate() {
            return date;
        }
        public String getAboutEvent() {
            return aboutEvent;
        }

    }
    static class FeeInfo{

        int feeId,courseId; double feeAmount;

        public FeeInfo(int feeId, int courseId, double feeAmount) {
            this.feeId = feeId;
            this.courseId = courseId;
            this.feeAmount = feeAmount;
        }

        public int getFeeId() {
            return feeId;
        }

        public int getCourseId() {
            return courseId;
        }
        public double getFeeAmount() {
            return feeAmount;
        }

    }
    static class DepartmentInfo{
        int depId;
        String depName;

        public DepartmentInfo(int depId, String depName) {
            this.depId = depId;
            this.depName = depName;
        }

        public int getDepId() {
            return depId;
        }

        public String getDepName() {
            return depName;
        }

    }
    static class SubjectInfo{
        int subjectId;
        String subjectName;
        int credit;
        int courseId;

        public SubjectInfo(int subjectId, String subjectName, int credit, int courseId) {
            this.subjectId = subjectId;
            this.subjectName = subjectName;
            this.credit = credit;
            this.courseId = courseId;
        }

        public int getSubjectId() {
            return subjectId;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public int getCredit() {
            return credit;
        }

        public int getCourseId() {
            return courseId;
        }
    }

    public Admin(){
        Myconnection my = new Myconnection();
        con = my.MyConnection(con);
    }
    boolean checkAdmin(){
        System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.BRIGHT_YELLOW+"Enter Admin Name: ");
        String name=sc.nextLine();
        System.out.print("\t\t\t\t\t\t\t\t\t\t"+"Enter Admin Id: ");
        String id =sc.nextLine();
        System.out.print("\t\t\t\t\t\t\t\t\t\t"+"Enter Password: " + RESET);
        String password=sc.nextLine();
        return name.equals("Java") && id.equals("1234") && password.equals("Java1234");
    }

    void adminMenu(){

        String choice;
        do {
            System.out.println(SKY_BLUE+"\t\t\t\t\t\t\t\t\t\t--------------------------------------------------------------------");
            System.out.println("\t\t\t\t\t\t\t\t\t\t                             ADMIN MENU ");
            System.out.println("\t\t\t\t\t\t\t\t\t\t--------------------------------------------------------------------"+RESET);
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.PASTEL_YELLOW+"[1]. Add ");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[2]. Delete");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[3]. Update");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[4]. Add Notice");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[5]. Assign subject's to faculty");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[6]. View Details");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[7]. Undo last operation");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[8]. Send notifications ");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[9]. Backup database ");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[0]. Exit" + RESET);
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.AQUA_MINT+"👉🏻 Enter Your Choice : "+ RESET);
            choice=sc.nextLine();
            switch (choice){
                case "1": addMenu();break;
                case "2": deleteMenu(); break;
                case "3": updateMenu(); break;
                case "4": addNotice(); break;
                case "5": assignSubject(); break;
                case "6": viewMenu(); break;
                case "7": undoLastAction(); break;
                case "8" : notification();break;
                case "9": BackUp.backupTablesToSQLFile(con,"backup.sql");
                    System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.LIME_GREEN_GLOW+"BackUp done 😇"+ RESET);
                    break;
                case "0":
                    System.out.println("\t\t\t\t\t\t\t\t\t\t"+ PASTEL_PURPLE+"Exiting from Main Menu.....😇 "+ RESET);
                    break;
                default:
                    System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Invalid Choice 😕"+ RESET);
            }
        }while (!choice.equals("0"));
    }
    void addMenu(){
        String choice;
        do {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.PASTEL_YELLOW+"[1]. Add student ");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[2]. Add faculty");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[3]. Add department");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[4]. Add subject");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[5]. Add course");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[6]. Add fees");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[7]. Add event");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[0]. Exit"+RESET);
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.AQUA_MINT+"👉🏻 Enter Your Choice : " + RESET);
            choice=sc.nextLine();
            switch (choice){
                case "1" : addStudent(); break;
                case "2": addFaculty(); break;
                case "3": addDepartment(); break;
                case "4": addSubject(); break;
                case "5": addCourse(); break;
                case "6": addFees(); break;
                case "7": addEvents(); break;
                case "0":  System.out.println("\t\t\t\t\t\t\t\t\t\t"+ PASTEL_PURPLE+"Exiting from Add Menu.....😇 "+Color.RESET); break;
                default : System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Invalid Choice 😕"+ RESET);
            }
        }while (!choice.equals("0"));
    }
    void viewMenu() {
        try {
            String choice;
            do {
                System.out.println("\t\t\t\t\t\t\t\t\t\t" + Color.PASTEL_YELLOW + "[1] View LeaderBoard");
                System.out.println("\t\t\t\t\t\t\t\t\t\t" + "[2] View Attendance Record");
                System.out.println("\t\t\t\t\t\t\t\t\t\t" + "[3] View Marks");
                System.out.println("\t\t\t\t\t\t\t\t\t\t" + "[4] View Faculty");
                System.out.println("\t\t\t\t\t\t\t\t\t\t" + "[5] View Student");
                System.out.println("\t\t\t\t\t\t\t\t\t\t" + "[0] Exit");
                System.out.print("\t\t\t\t\t\t\t\t\t\t" + Color.AQUA_MINT + "👉🏻 Enter Your Choice : " + RESET);
                choice = sc.nextLine();
                switch (choice) {
                    case "1":
                        Faculty.viewLeaderBoard(con);
                        break;
                    case "2":
                        Faculty.viewAttendance(con);
                        break;
                    case "3":
                        Faculty.viewMarks(con);
                        break;
                    case "4":
                        viewFaculty();
                        break;
                    case "5":
                        Faculty.viewStudent(con);
                        break;
                    case "0":
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + Color.PASTEL_PURPLE + "Exiting from View Menu.....😇 " + RESET);
                        break;
                    default:
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Invalid Choice 😕" + RESET);
                }
            } while (!choice.equals("0"));
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e+"😕" + RESET);
        }
    }
    void deleteMenu() {
        String choice;
        do {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.PASTEL_YELLOW+"[1] Delete Student ");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[2] Delete Faculty");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[3] Delete department");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[4] Delete subject");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[5] Delete course");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[6] Delete event");
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+"[0] Exit"+ RESET);
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.AQUA_MINT+"👉🏻 Enter Your Choice : "+ RESET);
            choice=sc.nextLine();
            switch (choice){
                case "1" : deleteStudent(); break;
                case "2": deleteFaculty(); break;
                case "3": deleteDepartment(); break;
                case "4": deleteSubject(); break;
                case "5": deleteCourse(); break;
                case "6": deleteEvent(); break;
                case "0": System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.PASTEL_PURPLE+"Exiting from Delete Menu.....😇 "+Color.RESET); break;
                default : System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Enter valid Choice 😕"+ RESET); break;
            }
        }while (!choice.equals("0"));
    }
    void updateMenu()  {
        String choice;
        do {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.PASTEL_YELLOW+"1. Update Student ");
            System.out.println("\t\t\t\t\t\t\t\t\t\t2. Update Faculty");
            System.out.println("\t\t\t\t\t\t\t\t\t\t0. Exit"+ RESET);
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.AQUA_MINT+"👉🏻 Enter Your Choice : "+ RESET);
            choice=sc.nextLine();
            switch (choice){
                case "1" : editStudent(); break;
                case "2": editFaculty(); break;
                case "0":System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.PASTEL_PURPLE+"Exiting from Update Menu.....😇 "+Color.RESET); break;
                default : System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Enter valid Choice 😕"+ RESET); break;
            }
        }while (!choice.equals("0"));
    }

    void addEvents() {
        System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Event Name: "+ RESET);
        String name = sc.nextLine();
        System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter a date (dd-MM-yyyy): "+ RESET);
        String inputDate;
        LocalDate sqlDate;
        try {
            inputDate = sc.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            sqlDate = LocalDate.parse(inputDate, formatter);
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Enter valid date 😕"+ RESET);
            return;
        }
        System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter aboutEvent: "+ RESET);
        String aboutEvent = sc.nextLine();
        String sql = "insert into event(eventName,date,aboutEvent) values(?,?,?)";
        if (name.isEmpty() || aboutEvent.isEmpty()) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Enter valid values 😕"+ RESET);
            return;
        }
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setDate(2, java.sql.Date.valueOf(sqlDate));
            pst.setString(3, aboutEvent);
            pst.executeUpdate();
            sql = "select eventNo from event order by eventNo desc limit 1 ";
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sql);
            rs.next();
            int eventNo = rs.getInt(1);
            EventInfo eObj = new EventInfo(eventNo, name, sqlDate, aboutEvent);

            Action a = new Action("addEvent", eObj);
            historyStack.push(a);
            a.setTableName("event");

        } catch (SQLException e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Duplication of primary key not allowed !"+ RESET);
        }
    }
    void addFees(){
        int feeId,courseId; double feeAmount;
        try {
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter FeeId: "+ RESET);
            feeId=sc.nextInt();
            if(feeId <= 0){
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Enter valid values 😕"+ RESET);
                sc.nextLine();
                return;
            }
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Fee Amount: "+ RESET);
            feeAmount=sc.nextDouble();
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Course-Id: "+ RESET);
            courseId=sc.nextInt();
            sc.nextLine();
            String getCourseName;
            try {
                getCourseName = "select courseName from course where courseId = "+courseId ;
                Statement s = con.createStatement();
                ResultSet rs = s.executeQuery(getCourseName);
                rs.next();
                // getCourseName = rs.getString(1);

            } catch (SQLException e) {
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"course Id or department Id does not exists 😕"+ RESET);
                return;
            }
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"input mismatch exception 😕"+ RESET);
            sc.nextLine();
            return;
        }
        String sql="select courseID from course where courseID="+courseId;
        try {
            Statement st = con.createStatement();
            ResultSet r=st.executeQuery(sql);
            if(r.next()){
                String sInsert="insert into fees values(?,?,?)";
                PreparedStatement pst=con.prepareStatement(sInsert);
                pst.setInt(1,feeId);
                pst.setDouble(2,feeAmount);
                pst.setInt(3, courseId);
                int r1=pst.executeUpdate();
                System.out.println( r1>0 ? ("\t\t\t\t\t\t\t\t\t\t"+Color.LIME_GREEN_GLOW+"Insertion of "+r1+" row is done ✅"+ RESET) : ("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Insertion failed 😕"+ RESET));
                FeeInfo obj = new FeeInfo(feeId, courseId, feeAmount);
                Action a = new Action("addFees",obj);
                historyStack.push(a);
            }
            else{
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.LIME_GREEN_GLOW+"Course doesn't exists 😕"+ RESET);
            }
        } catch (SQLException e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+e+" 🙄😐"+ RESET);
        }
    }
    void addStudent(){

        /*
            Retrieve license BLOB to file:
            InputStream is = rs.getBinaryStream("license_document");
            Files.copy(is, Paths.get("output_license.pdf"), StandardCopyOption.REPLACE_EXISTING);*/
        //

        String name,password,contactNo,address,email; int deptId,courseId; long enroll;
        try {
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Student's Enrollment Number: "+ RESET);
            enroll = sc.nextLong();
            sc.nextLine();

            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Student's Name: "+ RESET);
            name = sc.nextLine();
            if(name.isEmpty()){
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter valid name !"+ RESET);
                return;
            }
            String ch = name.charAt(0) + "";
            ch = (ch).toUpperCase();
            name = name.substring(1);
            name = ch + name;

            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter password for " + name + ": "+ RESET);
            password = sc.nextLine();
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Student's Contact no. :"+ RESET);
            contactNo = sc.nextLine();
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Student's Address: "+ RESET);
            address = sc.nextLine();
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"enter your email (it should contain atleast 8 characters long and must contain " );
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ "at least 1 capital alphabet, 1 digit and 1 special character) - "+ RESET);
            email = sc.nextLine();
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Department-id for student:"+ RESET);
            deptId = sc.nextInt();
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Course-id for student: "+ RESET);
            courseId = sc.nextInt();
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Input mismatch exception 😕"+ RESET);
            sc.nextLine();
            return;
        }
        String getCourseName;
        try {
            getCourseName = "select courseName from course where courseId = "+courseId ;
            String checkDId = "select * from department where deptId = "+deptId;
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(getCourseName);
            rs.next();
            getCourseName = rs.getString(1);

            rs = s.executeQuery(checkDId);
            rs.next();
            rs.getString(2);
        } catch (SQLException e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"course Id or department Id does not exists 😕"+ RESET); return;
        }
        Student student=new Student(name,email,password,contactNo,address,enroll,deptId,courseId);
        StudentDB obj = new StudentDB();
        studentBst.addNode(student);
        Action a=new Action("addStudent", student);
        a.setTableName("student");
        historyStack.push(a);
        try {
            obj.addStudentDB(student);
            Student.generateStudentPdf(student,con);
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+e+" 🙄😐"+ RESET);
            return;
        }
        System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+ "add path for student photo : " + RESET);
        String pathOfPhoto = sc.nextLine();
        try {
            // Step 1: Create document with no margins
            Document document = new Document(new Rectangle(200, 150), 0, 0, 0, 0); // No page margins
            PdfWriter.getInstance(document, new FileOutputStream("ID_Card" + enroll + ".pdf"));
            document.open();

            // Step 2: Create the ID Card (bordered) table
            PdfPTable idCardTable = new PdfPTable(1);
            idCardTable.setTotalWidth(160); // Adjusted width to remove left gap
            idCardTable.setLockedWidth(true);

            PdfPCell borderCell = new PdfPCell();
            borderCell.setBorder(Rectangle.BOX);
            borderCell.setBorderWidth(1f);
            borderCell.setPadding(3f); // Tight padding inside the border

            // Content section
            Paragraph content = new Paragraph();
            content.setLeading(8);
            content.add(new Phrase("Student ID Card\n\n", new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD)));
            content.add(new Phrase("Name : " + name + "\n", new Font(Font.FontFamily.HELVETICA, 4)));
            content.add(new Phrase("Enrollment number : " + enroll + "\n", new Font(Font.FontFamily.HELVETICA, 4)));
            content.add(new Phrase("Course : " + getCourseName + "\n", new Font(Font.FontFamily.HELVETICA, 4)));
            content.add(new Phrase("Email-id : " + email + "\n", new Font(Font.FontFamily.HELVETICA, 4)));
            content.add(new Phrase("Contact no. : " + contactNo + "\n", new Font(Font.FontFamily.HELVETICA, 4)));
            content.add(new Phrase("Address : " + address + "\n", new Font(Font.FontFamily.HELVETICA, 4)));

            // Image section
            Image img = Image.getInstance(pathOfPhoto);
            img.scaleToFit(40, 40);

            PdfPTable contentTable = getPdfPTable(content, img);

            // Add contentTable inside the border cell
            borderCell.addElement(contentTable);
            idCardTable.addCell(borderCell);

            // Step 3: Wrapper table to center the ID card both vertically and horizontally
            PdfPTable wrapper = new PdfPTable(1);
            wrapper.setWidthPercentage(100);

            PdfPCell wrapperCell = new PdfPCell();
            wrapperCell.setBorder(Rectangle.NO_BORDER);
            wrapperCell.setFixedHeight(150); // match page height
            wrapperCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            wrapperCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            wrapperCell.addElement(idCardTable);

            wrapper.addCell(wrapperCell);

            // Add wrapper to the document
            document.add(wrapper);

            document.close();
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"ID Card created successfully for " + name+ RESET);
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"enter valid path for photo 😕"+ RESET);
        }// code for id card generation ends here
        //return true;
    }
    private static PdfPTable getPdfPTable(Paragraph content, Image img) throws DocumentException {
        PdfPTable contentTable = new PdfPTable(2);
        contentTable.setWidths(new float[]{3, 1});
        contentTable.setWidthPercentage(100);

        PdfPCell textCell = new PdfPCell(content);
        textCell.setBorder(Rectangle.NO_BORDER);
        textCell.setPaddingLeft(0f);
        textCell.setPaddingRight(6f); // Space between text and image
        textCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell imageCell = new PdfPCell(img);
        imageCell.setBorder(Rectangle.NO_BORDER);
        imageCell.setPadding(0f); // No extra padding
        imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        imageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // Add cells to contentTable
        contentTable.addCell(textCell);
        contentTable.addCell(imageCell);
        return contentTable;
    }
    void addFaculty(){
        // boolean check = false;
        int id,deptId; String name,email,password,contactNo,address,specialization;
        try{
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+ "Enter Faculty's Id: ");
            id=sc.nextInt();
            sc.nextLine();
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ "Enter Faculty's Name: ");
            name = sc.nextLine();
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ "Enter Password for " + name + ": ");
            password = sc.nextLine();
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ "Enter Faculty's Contact no.: ");
            contactNo = sc.nextLine();
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ "Enter Faculty's Email: ");
            email = sc.nextLine();
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ "Enter Faculty's Address: ");
            address = sc.nextLine();
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ "Enter Faculty's Specialization: ");
            specialization = sc.nextLine();
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ "Enter Department-id for faculty: "+ RESET);
            deptId = sc.nextInt();
            sc.nextLine();

            if(id == 0 || name.isEmpty() || password.isEmpty() || contactNo.isEmpty()
                    || email.isEmpty() || address.isEmpty() || specialization.isEmpty() ||
                    deptId == 0){
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Enter valid values 😕"+ RESET);
                return;
            }
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Input mismatch exception 😕"+ RESET);
            sc.nextLine();
            return;
        }
        try {
            String checkDId = "select * from department where deptId = " + deptId;
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(checkDId);
            rs.next();
            rs.getString(2);
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"department id doesn't exists 😕"+ RESET);
            return;
        }

        Faculty faculty=new Faculty(id,deptId,name,email,password,contactNo,address,specialization);
        FacultyDB obj = new FacultyDB();
        facultyBST.addNode(faculty);
        Action a=new Action("addFaculty", faculty);
        a.setTableName("faculty");
        historyStack.push(a);
        try {
            obj.addFacultyDB(faculty);
            Faculty.generateFacultyPdf(faculty,con);
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+e+" 🙄😐"+Color.RESET);
        }
        //return true;
    }
    void addDepartment() {
        int deptId;
        try {
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Department-Id: "+ RESET);
            deptId = sc.nextInt();
            if(deptId <= 0){
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Enter valid values 😕"+ RESET);
                sc.nextLine();
                return;
            }
        }
        catch (InputMismatchException e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Input mismatch exception 😕"+ RESET);
            sc.nextLine();
            return;
        }
        sc.nextLine();
        System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Department Name: "+ RESET);
        String deptName=sc.nextLine();
        if(deptName.isEmpty()){
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Enter valid values 😕"+ RESET);
            return;
        }
        String sInsert=" insert into department values(?,?)";
        int r;
        try {
            PreparedStatement pst=con.prepareStatement(sInsert);
            pst.setInt(1,deptId);
            pst.setString(2,deptName);
            r=pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"department id already exists 😕"+ RESET);
            return;        }
        System.out.println( r>0 ? ("\t\t\t\t\t\t\t\t\t\t"+Color.LIME_GREEN_GLOW+"Insertion of "+r+" row is done ✅"+ RESET) : ("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Insertion failed 😕"+ RESET));
        if(r>0){

            Action a=new Action("addDepartment", deptId);
            a.setTableName("department");
            historyStack.push(a);
        }
        //return true;
    }
    void addCourse(){
        int courseId;
        try {
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Course-Id: "+ RESET);
            courseId=sc.nextInt();
            if(courseId <= 0){
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Enter valid values 😕"+ RESET);
                sc.nextLine();
                return;
            }
        }
        catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Input mismatch exception 😕"+ RESET);
            sc.nextLine();
            return;
        }
        sc.nextLine();
        System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Course name: "+ RESET);
        String courseName=sc.nextLine();
        if(courseName.isEmpty()){
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Enter valid values 😕"+ RESET);
            return;
        }
        System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Department-Id: "+ RESET);
        int deptId=sc.nextInt();
        sc.nextLine();
        try {
            String checkDId = "select * from department where deptId = " + deptId;
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(checkDId);
            rs.next();
            rs.getString(2);
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"department id doesn't exists 😕"+ RESET);
            return;
        }
        courseInfo cObj = new courseInfo(courseId,deptId,courseName);
        try {
            Statement st = con.createStatement();
            //ResultSet exists=st.executeQuery(sql);
            //if(exists.next()) {
            String sInsert = " insert into course" +
                    " values(?,?,?)";
            int r;
            PreparedStatement pst = con.prepareStatement(sInsert);
            pst.setInt(1, courseId);
            pst.setString(2, courseName);
            pst.setInt(3, deptId);
            r = pst.executeUpdate();
            System.out.println( r>0 ? ("\t\t\t\t\t\t\t\t\t\t"+Color.LIME_GREEN_GLOW+"Insertion of "+r+" row is done ✅"+ RESET) : ("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Insertion failed 😕"+ RESET));
            if(r>0){
                Action a=new Action("addCourse", cObj);
                historyStack.push(a);
                a.setTableName("course");
            } else{
                System.out.println("Department doesn't exists");
            }
        } catch (SQLException e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+e+" 🙄😐"+Color.RESET);
        }
        //return true;
    }
    void addSubject(){
        int courseId,subId,credit; String subName;
        try {
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Subject-Id: ");
            subId=sc.nextInt();
            sc.nextLine();
            if(subId <= 0){
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Enter valid values 😕"+ RESET);
                return;
            }
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+"Enter Subject Name: "+ RESET);
            subName=sc.nextLine();
            if(subName.isEmpty()){
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Enter valid values 😕"+ RESET);
                return;
            }
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Subject's Credit:");
            credit=sc.nextInt();
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+"Enter Course-Id: "+ RESET);
            courseId=sc.nextInt();
            sc.nextLine();
        }
        catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Input mismatch exception 😕"+ RESET);
            sc.nextLine();
            return;
        }
        String getCourseName;
        try {
            getCourseName = "select courseName from course where courseId = "+courseId ;
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(getCourseName);
            rs.next();
            rs.getString(1);
        } catch (SQLException e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Course Id doesn't exists 😕"+ RESET);
            return;
        }
        //String sql="select courseID from course where courseID="+courseId;

        try {
            String sInsert = " insert into subject" +
                    " values(?,?,?,?)";
            int r;
            PreparedStatement pst = con.prepareStatement(sInsert);
            pst.setInt(1, subId);
            pst.setString(2, subName);
            pst.setInt(3, credit);
            pst.setInt(4, courseId);
            r = pst.executeUpdate();
            System.out.println( r>0 ? ("\t\t\t\t\t\t\t\t\t\t"+Color.LIME_GREEN_GLOW+"Insertion of "+r+" row is done ✅"+ RESET) : ("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Insertion failed 😕"+ RESET));
            if(r>0){
                Action a=new Action("addSubject", subId);
                historyStack.push(a);
                a.setTableName("subject");
            }
        } catch (SQLException e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"subject id already exists 😕"+ RESET);
        }
    }
    void addNotice(){
        int noticeNo;
        try {
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Notice no. :"+ RESET);
            noticeNo=sc.nextInt();
            sc.nextLine();
            if(noticeNo <= 0){
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Enter valid values 😕"+ RESET);
                return;
            }
        }catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Input mismatch exception 😕"+ RESET);
            sc.nextLine();
            return;
        }
        System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Notice Description: "+ RESET);
        String description=sc.nextLine();
        if(description.isEmpty()){
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Enter valid values 😕"+ RESET);
            return;
        }
        String sInsert="insert into notice values(?,?)";
        try {
            PreparedStatement pst = con.prepareStatement(sInsert);
            pst.setInt(1,noticeNo);
            pst.setString(2, description);
            int r=pst.executeUpdate();
            System.out.println( r>0 ? ("\t\t\t\t\t\t\t\t\t\t"+Color.LIME_GREEN_GLOW+"Insertion of "+r+" row is done ✅"+ RESET) : ("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Insertion failed 😕"+ RESET));
            if(r>0){
                Action a=new Action("addNotice", noticeNo);
                historyStack.push(a);
            }
        }
        catch (Exception e){
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Duplication of primary key not allowed 😐"+ RESET);
        }
    }
    int assignSubject() {
        int id;
        try {
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Faculty's Id: "+ RESET);
            id = sc.nextInt();
        }catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Input mismatch exception 😕"+ RESET);
            sc.nextLine();
            return -1;
        }
        String sql="select * from faculty where facultyId="+id;
        int subjectId = 0;
        try {
            Statement st = con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            if(rs.next()){
                Faculty faculty=facultyBST.searchPreOrder(id);
                System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Subject Id: "+ RESET);
                subjectId =sc.nextInt();
                sql="select * from subject where subjectID="+subjectId;
                rs=st.executeQuery(sql);
                if(rs.next()){
                    sql="update faculty set subjectID="+subjectId+" where facultyID="+id;
                    int r=st.executeUpdate(sql);
                    System.out.println( r>0 ? ("\t\t\t\t\t\t\t\t\t\t"+Color.LIME_GREEN_GLOW+"Subject is assigned ✅"+ RESET) : ("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Subject is not assigned 😕"+ RESET));
                    if(r>0){
                        faculty.subjectId=subjectId;
                        Action a=new Action("assignSubject", faculty);
                        historyStack.push(a);
                    }
                }
                else{
                    System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Subject doesn't exists 😕"+ RESET);
                }
            }
            else{
                System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Faculty doesn't exists 😕"+ RESET);
            }
        } catch (SQLException e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Duplication of primary key not allowed 😐"+ RESET);
        }
        sc.nextLine();
        return subjectId;
    }

    void editStudent() {
        try {
            long enrollNum;
            try {
                System.out.print("\t\t\t\t\t\t\t\t\t\t" + Color.NEON_PINK + "Enter Enrollment number : " + RESET);
                enrollNum = sc.nextLong();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Input mismatch exception 😕" + RESET);
                sc.nextLine();
                return;
            }
            String choice;
            String sql = "{call updateStudent(?,?,?,?)}";

            Student sNonUpdated = StudentBST.searchPreOrder(enrollNum);
            Action a = new Action("editStudent", sNonUpdated);
            a.setTableName("student");
            historyStack.push(a);

            System.out.println("\t\t\t\t\t\t\t\t\t\t" + PASTEL_YELLOW + "choose one of the following to edit :");
            System.out.println("\t\t\t\t\t\t\t\t\t\t" + "[1]. Student name");
            System.out.println("\t\t\t\t\t\t\t\t\t\t" + "[2]. Student password");
            System.out.println("\t\t\t\t\t\t\t\t\t\t" + "[3]. Student contact number");
            System.out.println("\t\t\t\t\t\t\t\t\t\t" + "[4]. Student Address");
            System.out.println("\t\t\t\t\t\t\t\t\t\t" + "[5]. Student email-id");
            System.out.println("\t\t\t\t\t\t\t\t\t\t" + "[6]. Student Dept id");
            System.out.println("\t\t\t\t\t\t\t\t\t\t" + "[7]. Student course-id" + RESET);
            System.out.print("\t\t\t\t\t\t\t\t\t\t" + Color.AQUA_MINT + "👉🏻 Enter Your Choice : " + Color.RESET);
            choice = sc.nextLine();

            switch (choice) {

                case "1":
                    System.out.print("\t\t\t\t\t\t\t\t\t\t" + Color.NEON_PINK + "Enter new student name: " + RESET);
                    String studentName = sc.nextLine();
                    if (studentName.isEmpty()) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Enter valid values 😕" + RESET);
                        return;
                    }
                    PreparedStatement ps1 = con.prepareStatement(sql);
                    ps1.setLong(1, enrollNum);
                    ps1.setString(2, choice);
                    ps1.setString(3, studentName);
                    ps1.setInt(4, 0);
                    int r1 = ps1.executeUpdate();
                    System.out.println(r1 > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Not done 😕" + RESET));
                    break;

                case "2":
                    System.out.print("\t\t\t\t\t\t\t\t\t\t" + Color.NEON_PINK + "Enter new student password : " + RESET);
                    String stuPassword = sc.nextLine();
                    if (stuPassword.isEmpty()) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Enter valid values 😕" + RESET);
                        return;
                    }
                    PreparedStatement ps2 = con.prepareStatement(sql);
                    ps2.setLong(1, enrollNum);
                    ps2.setString(2, choice);
                    ps2.setString(3, stuPassword);
                    ps2.setInt(4, 0);
                    int r2 = ps2.executeUpdate();
                    System.out.println(r2 > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Not done 😕" + RESET));
                    break;

                case "3":
                    System.out.print("\t\t\t\t\t\t\t\t\t\t" + Color.NEON_PINK + "Enter new student contact number(of length 10) : " + RESET);
                    String stuContact = sc.nextLine();
                    if (stuContact.length() != 10) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Enter valid values 😕" + RESET);
                        return;
                    }
                    PreparedStatement ps3 = con.prepareStatement(sql);
                    ps3.setLong(1, enrollNum);
                    ps3.setString(2, choice);
                    ps3.setString(3, stuContact);
                    ps3.setInt(4, 0);
                    int r3 = ps3.executeUpdate();
                    System.out.println(r3 > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Not done 😕" + RESET));
                    break;

                case "4":
                    System.out.print("\t\t\t\t\t\t\t\t\t\t" + Color.NEON_PINK + "Enter new student address : " + RESET);
                    String stuAddress = sc.nextLine();
                    if (stuAddress.isEmpty()) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Enter valid values 😕" + RESET);
                        return;
                    }
                    PreparedStatement ps4 = con.prepareStatement(sql);
                    ps4.setLong(1, enrollNum);
                    ps4.setString(2, choice);
                    ps4.setString(3, stuAddress);
                    ps4.setInt(4, 0);
                    int r4 = ps4.executeUpdate();
                    System.out.println(r4 > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Not done 😕" + RESET));
                    break;

                case "5":
                    System.out.print("\t\t\t\t\t\t\t\t\t\t" + Color.NEON_PINK + "Enter new student email-id : " + RESET);
                    String stuEmail = sc.nextLine();
                    if (!(stuEmail.endsWith("@ljku.edu.in"))) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Enter valid values 😕" + RESET);
                        return;
                    }
                    PreparedStatement ps5 = con.prepareStatement(sql);
                    ps5.setLong(1, enrollNum);
                    ps5.setString(2, choice);
                    ps5.setString(3, stuEmail);
                    ps5.setInt(4, 0);
                    int r5 = ps5.executeUpdate();
                    System.out.println(r5 > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Not done 😕" + RESET));
                    break;

                case "6":
                    System.out.print("\t\t\t\t\t\t\t\t\t\t" + Color.NEON_PINK + "enter new dep id = " + RESET);
                    int newID = sc.nextInt();
                    if (newID <= 0) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Enter valid values 😕" + RESET);
                        return;
                    }
                    try {
                        String checkDId = "select * from department where deptId = " + newID;
                        Statement s = con.createStatement();
                        ResultSet rs = s.executeQuery(checkDId);
                        rs.next();
                        rs.getString(2);
                    } catch (SQLException e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "department Id does not exists 😕" + RESET);
                        sc.nextLine();
                        return;
                    }
                    ps5 = con.prepareStatement(sql);
                    ps5.setLong(1, enrollNum);
                    ps5.setString(2, choice);
                    ps5.setString(3, "");
                    ps5.setInt(4, newID);
                    r5 = ps5.executeUpdate();
                    System.out.println(r5 > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Not done 😕" + RESET));
                    sc.nextLine();
                    break;

                case "7":
                    System.out.print("\t\t\t\t\t\t\t\t\t\t" + Color.NEON_PINK + "enter new course id = " + RESET);
                    int CID = sc.nextInt();
                    if (CID <= 0) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Enter valid values 😕" + RESET);
                        return;
                    }
                    String getCourseName;
                    try {
                        getCourseName = "select courseName from course where courseId = " + CID;
                        Statement s = con.createStatement();
                        ResultSet rs = s.executeQuery(getCourseName);
                        rs.next();
                        rs.getString(1);
                    } catch (SQLException e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "course Id does not exists 😕" + RESET);
                        sc.nextLine();
                        return;
                    }
                    ps5 = con.prepareStatement(sql);
                    ps5.setLong(1, enrollNum);
                    ps5.setString(2, choice);
                    ps5.setString(3, "");
                    ps5.setInt(4, CID);
                    r5 = ps5.executeUpdate();
                    System.out.println(r5 > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Not done 😕" + RESET));
                    sc.nextLine();
                    break;
                case "0":
                    System.out.println("\t\t\t\t\t\t\t\t\t\t" + PASTEL_PURPLE + "Exiting from This Menu.....😇 " + RESET);
                    break;
                default:
                    System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Enter valid option 😕" + RESET);
                    break;
            }
            StudentBST.root = null;
            BSTThread th = new BSTThread();
            th.start();
            th.join();
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+e+"😐"+ RESET);
        }
    }
    void editFaculty(){
        try {
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+ SKY_TURQUOISE+"Enter Faculty id : ");
            int facultyId = sc.nextInt();
            sc.nextLine();
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+"Enter faculty Name: "+ RESET);
            String facultyName=sc.nextLine();
            FacultyDB fDB=new FacultyDB();
            fDB.editFaculty(facultyId, facultyName);
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Input mismatch exception 😕"+ RESET);
            sc.nextLine();
        }
    }
    void undoLastAction() {
        try {
            if (historyStack.isEmpty()) {
                System.out.println(CORAL_RED + "\t\t\t\t\t\t\t\t\t\t\t\tNo actions to undo." + RESET);
                return;
            }
            Action lastAction = historyStack.pop();
            String type = lastAction.getActionType();
            String sql;
            int r;
            Faculty f;
            Student s;
            switch (type) {
                case "addStudent":
                    s = (Student) lastAction.getData();
                    sql = " delete from student" +
                            " where enrollmentNo= ? and studentName= ?";
                    try {
                        PreparedStatement pst = con.prepareStatement(sql);
                        pst.setLong(1, s.getEnrollNo());
                        pst.setString(2, s.getName());
                        r = pst.executeUpdate();
                        System.out.println(r > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Undoing done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Undoing failed 😕" + RESET));
                        StudentBST.root = null;
                        BSTThread th = new BSTThread();
                        th.join();
                        th.start();
                    } catch (Exception e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e + " 🙄😐" + RESET);

                    }
                    break;

                case "addFaculty":
                    f = (Faculty) lastAction.getData();
                    sql = " delete from faculty" +
                            " where facultyID= ? and facultyName= ?";
                    try {
                        PreparedStatement pst = con.prepareStatement(sql);
                        pst.setInt(1, f.getFacultyId());
                        pst.setString(2, f.getName());
                        r = pst.executeUpdate();
                        System.out.println(r > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Undoing done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Undoing failed 😕" + RESET));
                        FacultyBST.root = null;
                        BSTThread th = new BSTThread();
                        th.start();
                        th.join();
                    } catch (Exception e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e + " 🙄😐" + RESET);

                    }
                    break;
                case "addCourse":
                    courseInfo courseId =  (courseInfo) lastAction.getData();
                    sql = " delete from course" +
                            " where courseID= ?";
                    try {
                        PreparedStatement pst = con.prepareStatement(sql);
                        pst.setInt(1, courseId.getId());
                        r = pst.executeUpdate();
                        System.out.println(r > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Undoing done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Undoing failed 😕" + RESET));
                    } catch (SQLException e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e + " 🙄😐" + RESET);

                    }
                    break;

                case "addDepartment":
                    try {
                        int deptObj = (int) lastAction.getData();
                        sql = " delete from department" +
                                " where deptID= ? ";
                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setInt(1,deptObj);
                        int r3 = ps.executeUpdate();
                        System.out.println(r3 > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Undoing done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Undoing failed 😕" + RESET));
                    } catch (Exception e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e + " 🙄😐" + RESET);
                        return;
                    }
                    break;

                case "addSubject":
                    sql = " delete from subject where subjectID = ? ";
                    try {
                        PreparedStatement pst = con.prepareStatement(sql);
                        pst.setInt(1, ((int)lastAction.getData()));
                        r = pst.executeUpdate();
                        System.out.println(r > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Undoing done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Undoing failed 😕" + RESET));
                    } catch (Exception e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e + " 🙄😐" + RESET);

                    }
                    break;

                case "addEvent":
                    sql = " delete from event where eventNO = ? ";
                    try {
                        EventInfo eventInfo = (EventInfo) lastAction.getData();
                        PreparedStatement pst = con.prepareStatement(sql);
                        pst.setInt(1, eventInfo.eventNo);
                        r = pst.executeUpdate();
                        System.out.println(r > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Undoing done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Undoing failed 😕" + RESET));
                    } catch (Exception e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e + " 🙄😐" + RESET);

                    }
                    break;

                case "addFees":
                    sql = " delete from fees where feeID = ? ";
                    try {
                        FeeInfo eventInfo = (FeeInfo) lastAction.getData();
                        PreparedStatement pst = con.prepareStatement(sql);
                        pst.setInt(1, eventInfo.getFeeId());
                        r = pst.executeUpdate();
                        System.out.println(r > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Undoing done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Undoing failed 😕" + RESET));
                    } catch (Exception e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e + " 🙄😐" + RESET);

                    }
                    break;

                case "deleteStudent":
                    s = (Student) lastAction.getData();
                    sql = " insert into student" +
                            " values(?,?,?,?,?,?,?,?)";
                    try {
                        PreparedStatement pst = con.prepareCall(sql);
                        pst.setString(2, s.getName());
                        pst.setLong(1, s.getEnrollNo());
                        pst.setString(3, s.getPassword());
                        pst.setString(4, s.getContact());
                        pst.setString(5, s.getEmail());
                        pst.setString(6, s.getAddress());
                        pst.setInt(7, s.getDeptId());
                        pst.setInt(8, s.getCourseId());
                        r = pst.executeUpdate();
                        System.out.println(r > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Undoing done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Undoing failed 😕" + RESET));
                        studentBst.addNode(s);
                    } catch (SQLException e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e + " 🙄😐" + RESET);

                    }
                    break;

                case "deleteFaculty":
                    f = (Faculty) lastAction.getData();

                    sql = " insert into faculty" +
                            " values(?,?,?,?,?,?,?,?,?)";
                    try {
                        PreparedStatement pst = con.prepareCall(sql);
                        pst.setInt(1, f.getFacultyId());
                        pst.setString(2, f.getName());
                        pst.setString(5, f.getEmail());
                        pst.setString(3, f.getPassword());
                        pst.setInt(8, f.getDeptId());
                        pst.setString(4, f.getContact());
                        pst.setString(6, f.getAddress());
                        pst.setString(7, f.getSpecialization());
                        pst.setInt(9, f.getSubjectId());
                        r = pst.executeUpdate();
                        System.out.println(r > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Undoing done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Undoing failed 😕" + RESET));
                        facultyBST.addNode(f);
                    } catch (SQLException e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e + " 🙄😐" + RESET);

                    }
                    break;

                case "deleteCourse":
                    courseInfo courseDeletedObj = (courseInfo) lastAction.getData();
                    int courseIdDeleted = courseDeletedObj.getDid();
                    String courseNameDeleted = courseDeletedObj.getCourseName();
                    int courseDIdDeleted = courseDeletedObj.getDid();

                    sql = "insert into course" +
                            " values(?,?,?)";
                    try {
                        PreparedStatement pst = con.prepareStatement(sql);
                        pst.setInt(1, courseIdDeleted);
                        pst.setString(2, courseNameDeleted);
                        pst.setInt(3, courseDIdDeleted);
                        r = pst.executeUpdate();
                        System.out.println(r > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Undoing done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Undoing failed 😕" + RESET));
                    } catch (SQLException e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e + " 🙄😐" + RESET);

                    }
                    break;

                case "deleteDepartment":
                    DepartmentInfo DeletedObjDep = (DepartmentInfo) lastAction.getData();
                    int departmentIdDeleted = DeletedObjDep.getDepId();
                    String depNameDeleted = DeletedObjDep.getDepName();

                    sql = "insert into Department values(?,?)";
                    try {
                        PreparedStatement pst = con.prepareStatement(sql);
                        pst.setInt(1, departmentIdDeleted);
                        pst.setString(2, depNameDeleted);
                        r = pst.executeUpdate();
                        System.out.println(r > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Undoing done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Undoing failed 😕" + RESET));
                    } catch (SQLException e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e + " 🙄😐" + RESET);

                    }
                    break;

                case "deleteSubject":
                    SubjectInfo DeletedObjSub = (SubjectInfo) lastAction.getData();
                    int sId = DeletedObjSub.getSubjectId();
                    int credit = DeletedObjSub.getCredit();
                    int courseId2 = DeletedObjSub.getCourseId();
                    String subjectName = DeletedObjSub.getSubjectName();

                    sql = "insert into Subject values(?,?,?,?)";
                    try {
                        PreparedStatement pst = con.prepareStatement(sql);
                        pst.setInt(1, sId);
                        pst.setString(2, subjectName);
                        pst.setInt(3, credit);
                        pst.setInt(4, courseId2);
                        r = pst.executeUpdate();
                        System.out.println(r > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Undoing done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Undoing failed 😕" + RESET));
                    } catch (SQLException e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e + " 🙄😐" + RESET);

                    }
                    break;

                case "deleteEvent":
                    System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "For now , we have not implemented the undo operation of 'deleteEvent' in our code !" + RESET);
                    break;

                case "editStudent":
                    s = (Student) lastAction.getData();
                    sql = " update student" +
                            " set studentName = ? " +
                            " , studentPassword = ? , studentContactNo = ? " +
                            " , studentEmail = ? , studentAddress = ?" +
                            " , deptID = ? , courseID = ? where enrollmentNo = " + s.getEnrollNo();
                    try {
                        PreparedStatement pst = con.prepareStatement(sql);
                        pst.setString(1, s.getName());
                        pst.setString(2, s.getPassword());
                        pst.setString(3, s.getContact());
                        pst.setString(4, s.getEmail());
                        pst.setString(5, s.getAddress());
                        pst.setInt(6, s.getDeptId());
                        pst.setInt(7, s.getCourseId());

                        r = pst.executeUpdate();
                        System.out.println(r > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Undoing done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Undoing failed 😕" + RESET));

                        StudentBST.root = null;
                        BSTThread th = new BSTThread();
                        th.join();
                        th.start();
                    } catch (Exception e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e + " 🙄😐" + RESET);

                    }
                    break;


                case "editFaculty":
                    f = (Faculty) lastAction.getData();
                    sql = " update faculty" +
                            " set facultyName = ? " +
                            " , facultyPassword = ? , facultyContactNo = ? " +
                            " , facultyEmail = ? , facultyAddress = ?" +
                            " , specialization = ?, deptID = ? , subjectID = ?" +
                            " where facultyId = " + f.getFacultyId();
                    try {
                        PreparedStatement pst = con.prepareStatement(sql);
                        pst.setString(1, f.getName());
                        pst.setString(2, f.getPassword());
                        pst.setString(3, f.getContact());
                        pst.setString(4, f.getEmail());
                        pst.setString(5, f.getAddress());
                        pst.setString(6, f.getSpecialization());
                        pst.setInt(7, f.getDeptId());
                        pst.setInt(8, f.getSubjectId());


                        r = pst.executeUpdate();
                        System.out.println(r > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Undoing done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Undoing failed 😕" + RESET));

                        FacultyBST.root = null;
                        BSTThread th = new BSTThread();
                        th.start();
                        th.join();
                    } catch (Exception e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e + " 🙄😐" + RESET);
                    }
                    break;
            }
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e + " 🙄😐" + RESET);
        }
    }

    void deleteStudent(){
        long enrollNum;
        try {
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.VIVID_ORANGE+"Enter Enrollment number : "+ RESET);
            enrollNum = sc.nextLong();
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Input mismatch exception 😕"+ RESET);
            sc.nextLine();
            return;
        }
        System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.VIVID_ORANGE+"Enter Student Name: "+ RESET);
        String studName=sc.nextLine();
        String sDelete=" delete from student where enrollmentNo = ? and studentName= ?";
        int r;
        try {
            PreparedStatement pst=con.prepareStatement(sDelete);
            pst.setLong(1,enrollNum);
            pst.setString(2,studName);
            r=pst.executeUpdate();
            System.out.println( r>0 ? ("\t\t\t\t\t\t\t\t\t\t"+Color.LIME_GREEN_GLOW+"Deletion of "+r+" row is done ✅"+ RESET) : ("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Deletion failed 😕"+ RESET));
            StudentBST sb = new StudentBST();
            Student s = StudentBST.searchPreOrder(enrollNum);
            StudentBST.root = null;
            BSTThread th = new BSTThread();
            th.start();
            th.join();
            Action a=new Action("deleteStudent", s);
            a.setTableName("student");
            historyStack.push(a);
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Duplication of primary key not allowed 😐"+ RESET);
        }
    }
    void deleteFaculty(){
        int facultyId;
        try {
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.VIVID_ORANGE+"Enter Faculty id : ");
            facultyId = sc.nextInt();
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Input mismatch exception 😕"+ RESET);
            sc.nextLine();
            return;
        }
        System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.VIVID_ORANGE+"Enter faculty Name: ");
        String facultyName=sc.nextLine();

        String sDelete=" delete from faculty where facultyID= ? and facultyName= ?";
        int r;
        try {
            PreparedStatement pst=con.prepareStatement(sDelete);
            pst.setInt(1,facultyId);
            pst.setString(2,facultyName);
            r=pst.executeUpdate();
            FacultyBST f = new FacultyBST();
            Action a=new Action("deleteFaculty", FacultyBST.searchPreOrder(facultyId));
            a.setTableName("faculty");
            historyStack.push(a);
            FacultyBST.root = null;
            BSTThread th = new BSTThread();
            th.start();
            th.join();

        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Duplication of primary key not allowed 😐"+ RESET);
            return;
        }
        System.out.println( r>0 ? ("\t\t\t\t\t\t\t\t\t\t"+Color.LIME_GREEN_GLOW+"Deletion of "+r+" row is done ✅"+ RESET) : ("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Deletion failed 😕"+ RESET));
    }
    void deleteDepartment(){
        int deptId;
        try {
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.VIVID_ORANGE+"Enter department id : "+ RESET);
            deptId = sc.nextInt();
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Input mismatch exception 😕"+ RESET);
            sc.nextLine();
            return;
        }
        System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.VIVID_ORANGE+"Enter department Name: "+ RESET);
        String deptName=sc.nextLine();

        DepartmentInfo dObj = new DepartmentInfo(deptId,deptName);
        String sDelete=" delete from department" +
                " where deptID= ? and deptName= ?";
        int r;
        try {
            PreparedStatement pst=con.prepareStatement(sDelete);
            pst.setInt(1,deptId);
            pst.setString(2,deptName);
            r=pst.executeUpdate();
            Action a = new Action("deleteDepartment", dObj);
            a.setTableName("Department");
            historyStack.push(a);
        } catch (SQLException e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Duplication of primary key not allowed 😐"+ RESET);
            return;
        }
        System.out.println( r>0 ? ("\t\t\t\t\t\t\t\t\t\t"+Color.LIME_GREEN_GLOW+"Deletion of "+r+" row is done ✅"+ RESET) : ("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Deletion failed 😕"+ RESET));
    }
    void deleteCourse() {
        try {
            int courseId;
            try {
                System.out.print("\t\t\t\t\t\t\t\t\t\t" + Color.VIVID_ORANGE + "Enter course id : " + RESET);
                courseId = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Input mismatch exception 😕" + RESET);
                sc.nextLine();
                return;
            }
            System.out.print("\t\t\t\t\t\t\t\t\t\t" + Color.VIVID_ORANGE + "Enter course Name: " + RESET);
            String courseName = sc.nextLine();
            String Sql = "select deptID from course where courseID=  " + courseId;
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(Sql);
            rs.next();
            int did = rs.getInt(1);
            courseInfo cObj = new courseInfo(courseId, did, courseName);
            String sDelete = " delete from course" +
                    " where courseID= ? and courseName= ?";
            int r;
            try {
                PreparedStatement pst = con.prepareStatement(sDelete);
                pst.setInt(1, courseId);
                pst.setString(2, courseName);
                r = pst.executeUpdate();
                Action a = new Action("deleteCourse", cObj);
                a.setTableName("Course");
                historyStack.push(a);

            } catch (SQLException e) {
                System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Duplication of primary key not allowed 😐" + RESET);
                return;
            }
            System.out.println(r > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Deletion of " + r + " row is done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Deletion failed 😕" + RESET));
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+e+"😐"+ RESET);
        }
    }
    void deleteSubject()  {
        int subjectId;
        try{
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.VIVID_ORANGE+"Enter subject id : "+ RESET);
            subjectId = sc.nextInt();
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Input mismatch exception 😕"+ RESET);
            sc.nextLine();
            return;
        }
        System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.VIVID_ORANGE+"Enter subject Name: "+ RESET);
        String subjectName=sc.nextLine();
        String sql = "select * from subject where subjectId = "+subjectId;
        SubjectInfo sObj;
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sql);
            rs.next();
            sObj = new SubjectInfo(subjectId,subjectName,rs.getInt(3),rs.getInt(4));

        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Enter valid values 😕"+ RESET);
            return;
        }

        String sDelete=" delete from subject where subjectID= ? and subjectName= ?";
        int r;
        try {
            PreparedStatement pst=con.prepareStatement(sDelete);
            pst.setInt(1,subjectId);
            pst.setString(2,subjectName);
            r=pst.executeUpdate();
            Action a = new Action("deleteSubject", sObj);
            a.setTableName("Subject");
            historyStack.push(a);
        } catch (SQLException e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Duplication of primary key not allowed 😐"+ RESET);            return;
        }
        System.out.println( r>0 ? ("\t\t\t\t\t\t\t\t\t\t"+Color.LIME_GREEN_GLOW+"Deletion of "+r+" row is done ✅"+ RESET) : ("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Deletion failed 😕"+ RESET));
    }
    void deleteEvent() {
        try{
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.VIVID_ORANGE+"Enter event id for deletion :"+ RESET);
            int eventId = sc.nextInt();
            String sql = "delete from event where eventNo = "+eventId;
            Statement s = con.createStatement();
            int r1 = s.executeUpdate(sql);
            System.out.println( r1>0 ? ("\t\t\t\t\t\t\t\t\t\t"+Color.LIME_GREEN_GLOW+"Deletion of "+r1+" row is done ✅"+Color.RESET) : ("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"Deletion failed 😕"+Color.RESET));
            Action a = new Action("deleteEvent",null);
            a.setTableName("Event");
            historyStack.push(a);
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+"Input mismatch exception 😕"+ RESET);
            sc.nextLine();
        }
    }

    void notification() {

        try {
            System.out.println(PASTEL_YELLOW + "\t\t\t\t\t\t\t\t\t\t[1] To Faculty");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[2] To Student" + RESET);
            System.out.print("\t\t\t\t\t\t\t\t\t\t" + Color.AQUA_MINT + "👉🏻 Enter Your Choice : " + Color.RESET);
            String choice = sc.nextLine();
            if (choice.equals("1")) {
                int facNotifyId;
                try {
                    System.out.print(SKY_TURQUOISE + "\t\t\t\t\t\t\t\t\t\tenter faculty id to notify = " + RESET);
                    facNotifyId = sc.nextInt();
                    sc.nextLine();
                    String sql = "select * from faculty where facultyId = " + facNotifyId;
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(sql);
                    rs.next();
                    try {
                        rs.getInt(1);
                    } catch (Exception e) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "enter valid faculty id !" + " 🙄😐" + RESET);
                        return;
                    }

                } catch (Exception e) {
                    System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Input mismatch exception 😕" + RESET);
                    sc.nextLine();
                    return;
                }
                System.out.print(SKY_TURQUOISE + "\t\t\t\t\t\t\t\t\t\tenter the notification text = " + RESET);
                String notification = sc.nextLine();
                String sql = "insert into notificationFaculty values(null,?,?)";
                PreparedStatement ps1 = con.prepareStatement(sql);
                ps1.setInt(1, facNotifyId);
                ps1.setString(2, notification);
                int r = ps1.executeUpdate();
                System.out.println(r > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + r + " rows affected ✅" + Color.RESET) : ("\t\t\t\t\t\t\t\t\t\t" + Color.CORAL_RED + "insertion failed 😕" + Color.RESET));
                Action a = new Action("notifyFaculty", facNotifyId);
                a.setTableName("notificationFaculty");
                historyStack.push(a);
            } else if (choice.equals("2")) {
                long stuNotifyId;
                try {
                    System.out.print(SKY_TURQUOISE + "\t\t\t\t\t\t\t\t\t\tenter student Enrollment no to notify = " + RESET);
                    stuNotifyId = sc.nextLong();
                    sc.nextLine();
                } catch (Exception e) {
                    System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Input mismatch exception 😕" + RESET);
                    sc.nextLine();
                    return;
                }
                System.out.print(SKY_TURQUOISE + "\t\t\t\t\t\t\t\t\t\tenter the notification text = " + RESET);
                String notification = sc.nextLine();

                String sql = "select * from student where enrollmentNo = " + stuNotifyId;
                Statement s = con.createStatement();
                ResultSet rs = s.executeQuery(sql);
                rs.next();
                try {
                    rs.getLong(1);
                } catch (Exception e) {
                    System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "enter valid student id  😕" + RESET);
                    return;
                }

                sql = "insert into notificationStudent values(null,?,?)";
                PreparedStatement ps1 = con.prepareStatement(sql);
                ps1.setLong(1, stuNotifyId);
                ps1.setString(2, notification);
                int r = ps1.executeUpdate();
                System.out.println(r > 0 ? ("\t\t\t\t\t\t\t\t\t\t" + Color.LIME_GREEN_GLOW + "Insertion of " + r + " row is done ✅" + RESET) : ("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Insertion failed 😕" + RESET));
                Action a = new Action("notifyStudent", stuNotifyId);
                a.setTableName("notificationStudent");
                historyStack.push(a);
            } else {
                System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "enter valid option ! 😕" + RESET);
            }
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+ CORAL_RED+e+"😐"+ RESET);
        }
    }
    void viewFaculty() {
        try {
            int id;
            try {
                System.out.print(SKY_TURQUOISE + "\t\t\t\t\t\t\t\t\t\tEnter FacultyId: " + RESET);
                id = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "Input mismatch exception 😕" + RESET);
                sc.nextLine();
                return;
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter("FacultyDetails" + id + ".txt"));
            Faculty f = FacultyBST.searchPreOrder(id);
            if (f == null) {
                System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + "faculty doesnot exists ! 😕" + RESET);
                return;
            }
            bw.write("faculty id = " + f.getFacultyId() + "\nFaculty name = " + f.getName() + "\nEmail-id = " + f.getEmail() + "\nAddress" + f.getAddress()
                    + "\nSpecialization = " + f.getSpecialization() + "\nDept id = " + f.getDeptId() + "contact number = " + f.getContact() + "\n\n");
            // for all faculty
            bw.flush();
            bw.close();
            String sql = "select * from faculty";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            BufferedWriter bw1 = new BufferedWriter(new FileWriter("FacultyDetails.txt"));
            while (rs.next()) {
                bw1.write("faculty id = " + rs.getInt(1) + "\nFaculty name = " + rs.getString(2) + "\nEmail-id = " + rs.getString(5)
                        + "\nAddress" + rs.getString(6) + "\nSpecialization = " + rs.getString(7) + "\ncontact number = " + rs.getString(4)
                        + "\nDept id = " + rs.getInt(8) + "\n\n");

            }
            bw1.flush();
            bw1.close();
            System.out.println(LIME_GREEN_GLOW + "\t\t\t\t\t\t\t\t\t\tFaculty details txt file has been downloaded --> FacultyDetails" + id + ".txt" + " :) " + RESET);
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t" + CORAL_RED + e+"! 😕" + RESET);
        }
    }
}