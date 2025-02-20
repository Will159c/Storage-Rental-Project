import java.util.List;

public class main {
    public static void main(String [] Args)  {

//        //inserts a user into the database
//        mySQL.insertUser("test", "password");
//
//        //removes a user from the database based on the username
//        mySQL.deleteUser("test");

        List<Integer> hello = mySQL.getStorageID();  // creates the dynamic array list named hello as this will be changed

        for (Integer i : hello) { System.out.println(i); } // prints out all the elements in the array hello
    }
}
