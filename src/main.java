import java.util.List;

public class main {
    public static void main(String [] Args)  {
//        CustomerReserveOrRemove.guiTestCustomerReserveOrRemove();         //executes the guiTestCustomerReserveOrRemove to see if it works
//        BrowseStorageUnits.guiTestBrowseStorageUnits();                   //executes the guiTestBrowseStorageUnits to see if works
//
//        if(mySQL.isUsernameAndPassword("test", "password")) {             //checks if username and password are correct
//            System.out.println("yay");                                    //returns yay if the isUsernameAndPassword method returns true
//        }
//
//        List<Object> hello = mySQL.getStorageID();                       // creates the dynamic array list named hello as this will be changed
//
//        for (Object i : hello) { System.out.println(i); }                // prints out all the elements in the array hello
//        MyGUI myGUI = new MyGUI();
//        mySQL.setEmail("test3", "email.gmail.com");
        List<Object> hello = mySQL.getStorageInformation(11223124);
        for (Object i : hello) { System.out.println(i); }


    }
}
