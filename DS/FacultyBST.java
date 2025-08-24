package DS;
import Model.Faculty;

import static Model.Color.CORAL_RED;
import static Model.Color.RESET;

public class FacultyBST {
    public static Faculty root;
    public  void addNode(Faculty obj) {
        if (root == null) {
            root = obj;
            return;
        }
        Faculty temp = root;
        while (true) {
            if (obj.getFacultyId() < temp.getFacultyId()) {
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
    public static Faculty searchPreOrder(int id){

        if(root==null){
            System.out.println(CORAL_RED+"\t\t\t\t\t\t\t\t\t\tBST is empty !"+RESET);
            return null;
        }
        Faculty temp = root;
        if(id==temp.getFacultyId()){
            return temp;
        }
        while (true){
            if(id > temp.getFacultyId()){
                temp = temp.right;
                if(temp == null){
                    return null;
                }
                if(temp.getFacultyId()==id){
                    return temp;
                }
            } else {
                temp = temp.left;
                if(temp == null){
                    return null;
                }
                if (temp.getFacultyId()==id) {
                    return temp;
                }
            }
        }
    }
}
