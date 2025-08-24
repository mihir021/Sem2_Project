package DS;
import Model.Student;

import static Model.Color.CORAL_RED;
import static Model.Color.RESET;

public class StudentBST {
    public static Student root;
    public void addNode(Student obj) {
        if (root == null) {
            root = obj;
            return;
        }
        Student temp = root;
        while (true) {
            if (obj.getEnrollNo() < temp.getEnrollNo()) {
                if (temp.left == null) {
                    temp.left = obj;
                    break;
                } else {
                    temp = temp.left;
                }
            } else {
                if (temp.right == null) {
                    temp.right = obj;
                    break;
                } else {
                    temp = temp.right;
                }
            }
        }
    }
    public static Student searchPreOrder(long id){
        if(root==null){
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tBST is empty !"+RESET);
            return null;
        }
        Student temp = root;
        if(id==temp.getEnrollNo()){
            return temp;
        }
        while (true){
            if(id > temp.getEnrollNo()){
                temp = temp.right;
            } else {
                temp = temp.left;
            }
            if(temp == null){
                return null;
            }
            if(temp.getEnrollNo()==id){
                return temp;
            }
        }
    }
}