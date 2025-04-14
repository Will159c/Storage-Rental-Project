import storagerental.StorageRentalApplication;

public class Main {
    public static void main(String [] Args)  {

        new Thread(() -> {
            StorageRentalApplication.main(new String[]{});
        }).start();

        MyGUI myGUI = new MyGUI();

    }
}
