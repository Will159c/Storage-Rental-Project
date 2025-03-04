import java.util.InputMismatchException;
import java.util.Scanner;

public class CustomerReserveOrRemove {

    //  RESERVES STORAGE UNIT
    public static void reserveStorageUnit(int storageID, String email, String password) {
        boolean success = mySQL.reserveStorageUnit(storageID, email, password);
        if (success) {
            System.out.println("Storage unit " + storageID + " reserved successfully for " + email + "!");
        } else {
            System.out.println("Failed to reserve storage unit " + storageID + ". It may already be reserved.");
        }
    }

    // CANCEL RESERVATION
    public static void cancelReservation(int storageID, String email, String password) {
        boolean success = mySQL.cancelReservation(storageID, email, password);
        if (success) {
            System.out.println("Reservation for storage unit " + storageID + " has been cancelled.");
        } else {
            System.out.println("No reservation found for storage unit " + storageID + " under " + email + ".");
        }
    }

}
