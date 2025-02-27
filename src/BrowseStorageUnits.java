import java.util.List;
import java.util.Scanner;

public class BrowseStorageUnits {

    public static void guiTestBrowseStorageUnits() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== Browse Storage Units =====");
        System.out.println("1. View ALL units");
        System.out.println("2. View only AVAILABLE units");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        if (choice == 1) {
            displayAllUnits();
        } else if (choice == 2) {
            displayAvailableUnits();
        } else {
            System.out.println("Invalid choice. Exiting...");
        }

        scanner.close();
    }

    // Option 1: Show All Units (Reserved + Available)
    private static void displayAllUnits() {
        // Reuse your existing method that returns all IDs from the 'storage' table
        List<Integer> allIds = mySQL.getStorageID();

        System.out.println("\n=== ALL STORAGE UNITS ===");
        for (Integer id : allIds) {
            // Check if reserved
            boolean reserved = mySQL.isUnitReserved(id);
            System.out.println("Storage ID: " + id + " - " + (reserved ? "RESERVED" : "AVAILABLE"));
        }
    }

    // Option 2: Show Only Available Units
    private static void displayAvailableUnits() {
        List<Integer> allIds = mySQL.getStorageID();

        System.out.println("\n=== AVAILABLE STORAGE UNITS ===");
        for (Integer id : allIds) {
            // Print only if it's NOT reserved
            if (!mySQL.isUnitReserved(id)) {
                System.out.println("Storage ID: " + id);
            }
        }
    }
}
