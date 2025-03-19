public class StorageUnit {
    String size;
    int price;
    String location;
    public StorageUnit(String size, int price, String location) {
        this.size = size;
        this.price = price;
        this.location = location;

        MySQL.createNewStorageUnit(size, price, location); //adds the object to the database


    }
    public String getSize(){ //gets the size of the object and returns it
        return size;
    }

    public int getPrice(){ //gets the price of the object and returns it
        return price;
    }

    public String getLocation(){ //gets the location of the object and returns it
        return location;
    }


}
