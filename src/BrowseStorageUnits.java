import java.util.List;
import java.util.Scanner;

/**
 * (a) Module/Class Name: BrowseStorageUnits
 * (b) Date: April 15, 2025
 * (c) Programmer's Name: Juan Acevedo
 *
 * (d) Description: CLI to check the connection of the mySQL DB to check for units
 *     and reservation status
 * (e) Key Functions:
 *     - guiTestBrowseStorageUnits(): Presents the menu and manages user input.
 *     - displayAllUnits(): Displays all storage units and their reservation status.
 *     - displayAvailableUnits(): Displays only the available storage units.
 * (f) Important Data/Structures:
 *     - List<Integer>: Stores storage unit IDs retrieved from the database.
 *     - Scanner: Reads user input from the command line.
 * (g) Algorithm Notes:
 *     simple iteration (for-each loops) to display storage unit information.
 */
public class BrowseStorageUnits {

    /**
     * Launches the browsing interface for storage units.
     * <p>
     * The menu has the options
     * - View all storage units (both reserved and available).</li>
     * - View only available storage units (non-reserved).</li>
     */
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

    /**
     * Displays all storage units.
     * Retrieves all storage unit IDs from the database using {@code MySQL.getStorageID()}
     * and iterates through the list. For each storage unit, it checks whether the unit
     * is reserved by calling {@code MySQL.isUnitReserved(id)} and prints the reservation status.
     */
    private static void displayAllUnits() {
        List<Integer> allIds = MySQL.getStorageID();

        System.out.println("\n=== ALL STORAGE UNITS ===");
        for (Integer id : allIds) {
            boolean reserved = MySQL.isUnitReserved(id);
            System.out.println("Storage ID: " + id + " - " + (reserved ? "RESERVED" : "AVAILABLE"));
        }
    }

    /**
     * Displays only the available storage units.
     * Retrieves the list of all storage unit IDs from the database using {@code MySQL.getStorageID()}.
     * It then iterates over the list and prints only those IDs for which the reservation
     */
    private static void displayAvailableUnits() {
        List<Integer> allIds = MySQL.getStorageID();

        System.out.println("\n=== AVAILABLE STORAGE UNITS ===");
        for (Integer id : allIds) {
            if (!MySQL.isUnitReserved(id)) {
                System.out.println("Storage ID: " + id);
            }
        }
    }
}
