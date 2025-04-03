public class Customer {
    String username;
    String password;
    String email;
    public Customer(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        MySQL.insertUser(username, password, email); //adds the user to the database

    }

    public void deleteCustomer() { //deletes the user
        MySQL.deleteUser(username);
        username = null;
        password = null;
        email = null;
    }

    public String getUsername() { //returns the username of the object
        return username;
    }

    public String getEmail(){ //returns the email of the object
        return email;
    }

}
