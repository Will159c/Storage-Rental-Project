import java.util.InputMismatchException;
import java.util.Scanner;

public class CustomerReserveOrRemove {

    //  RESERVES STORAGE UNIT
    public static void reserveStorageUnit(int storageID, String customerName) {
        boolean success = mySQL.reserveStorageUnit(storageID, customerName);
        if (success) {
            System.out.println("Storage unit " + storageID + " reserved successfully for " + customerName + "!");
        } else {
            System.out.println("Failed to reserve storage unit " + storageID + ". It may already be reserved.");
        }
    }

    // CANCEL RESERVATION
    public static void cancelReservation(int storageID, String customerName) {
        boolean success = mySQL.cancelReservation(storageID, customerName);
        if (success) {
            System.out.println("Reservation for storage unit " + storageID + " has been cancelled.");
        } else {
            System.out.println("No reservation found for storage unit " + storageID + " under " + customerName + ".");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Java Program Running Successfully!");
        try {
            System.out.println("Storage Reservation System");
            System.out.println("1. Reserve a Storage Unit");
            System.out.println("2. Cancel a Reservation");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            // Get customer name
            System.out.print("Enter your name: ");
            String customerName = scanner.nextLine().trim();

            //  Validate customer name
            if (customerName.isEmpty()) {
                System.out.println("Error: Customer name cannot be empty.");
                return;
            }

            System.out.print("Enter Storage ID: ");
            int storageID = scanner.nextInt();

            if (choice == 1) {
                reserveStorageUnit(storageID, customerName);
            } else if (choice == 2) {
                cancelReservation(storageID, customerName);
            } else {
                System.out.println("Invalid choice.");
            }

        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input. Please enter a number.");
        } finally {
            scanner.close();
        }
    }
}
