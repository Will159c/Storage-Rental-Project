import storagerental.StorageRentalApplication;

/**
 * Created by: Will Woodruff
 * The entry point of the application.
 * Launches the backend server in a separate thread and then opens the GUI.
 */
public class Main {
    public static void main(String[] Args) {
        // Start the backend server (StorageRentalApplication) in a separate thread
        new Thread(() -> {
            StorageRentalApplication.main(new String[]{});
        }).start();

        // Launch the GUI interface for user interaction
        MyGUI myGUI = new MyGUI();
    }
}

