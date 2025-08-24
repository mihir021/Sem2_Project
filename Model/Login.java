package Model;

import java.util.Scanner;

import static Model.Color.*;

public class Login {
    public static void main(String[] args) throws Exception{
        BSTThread th = new BSTThread();
        th.start();
        th.join();
        Scanner sc = new Scanner(System.in);
        hhhhgg();
        String choice;
        do {
            System.out.println(SKY_BLUE+"\t\t\t\t\t\t\t\t\t\t--------------------------------------------------------------------");
            System.out.println("\t\t\t\t\t\t\t\t\t\t                             LOGIN MENU ");
            System.out.println("\t\t\t\t\t\t\t\t\t\t--------------------------------------------------------------------"+RESET);
            System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.PASTEL_YELLOW+"[1]. 🤠 Admin 🤠");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[2]. 👩🏻‍🏫 Faculty 👨🏻‍🏫");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[3]. 👩‍🎓 Student 🧑‍🎓");
            System.out.println("\t\t\t\t\t\t\t\t\t\t[0]. 👋🏻 Exit 👋🏻"+Color.RESET);
            System.out.print("\t\t\t\t\t\t\t\t\t\t"+Color.AQUA_MINT+"👉🏻 Enter Your Choice : "+Color.RESET);
            choice=sc.nextLine();
            switch (choice){
                case "1" :
                    Admin admin = new Admin();
                    boolean adminVerification= admin.checkAdmin();
                    if(adminVerification)
                        admin.adminMenu();
                    else
                        System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"Incorrect Details or Password 😕"+Color.RESET);
                    break;
                case "2":
                    Faculty faculty=new Faculty();
                    if(faculty.facultyVerification)
                        faculty.facultyMenu();
                    else
                        System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"Incorrect Details or Password 😕"+Color.RESET);
                    break;
                case "3":
                    Student student=new Student();
                    if(student.studentVerification)
                        student.studentMenu();
                    else
                        System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"Incorrect Details or Password 😕"+Color.RESET);
                    break;
                case "0":
                    System.out.println("\t\t\t\t\t\t\t\t\t\t"+ PASTEL_PURPLE+"Exiting from Main Menu.....😇 "+Color.RESET);
                    break;
                default:
                    System.out.println("\t\t\t\t\t\t\t\t\t\t"+Color.CORAL_RED+"Invalid Choice 😕"+Color.RESET);
            }
        }while (!choice.equals("0"));
    }

    private static void hhhhgg() {
        System.out.println("\t\t\t\t\t\t\t\t\t\t"+PASTEL_TEAL+"================================================================="+RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t"+PASTEL_VIOLET+"                     ██╗███╗   ███╗███████╗     ");
        System.out.println("\t\t\t\t\t\t\t\t\t\t"+"                     ██║████╗ ████║██╔════╝     ");
        System.out.println("\t\t\t\t\t\t\t\t\t\t"+"                     ██║██╔████╔██║███████╗     ");
        System.out.println("\t\t\t\t\t\t\t\t\t\t"+"                     ██║██║╚██╔╝██║╚════██║     ");
        System.out.println("\t\t\t\t\t\t\t\t\t\t"+"                     ██║██║ ╚═╝ ██║███████║     ");
        System.out.println("\t\t\t\t\t\t\t\t\t\t"+"                     ╚═╝╚═╝     ╚═╝╚══════╝     "+RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t"+PASTEL_TEAL+"================================================================="+RESET);
        System.out.println("\t\t\t\t\t\t\t\t\t\t"+NEON_PINK+"                WELCOME TO INSTITUTE MANAGEMENT SYSTEM           ");
        System.out.println("\t\t\t\t\t\t\t\t\t\t"+PASTEL_TEAL+"================================================================="+RESET);
    }
}