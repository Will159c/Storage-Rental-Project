public class Customer {
    String username;
    String password;
    String email;
    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
        MySQL.insertUser(username, password); //adds the user to the database

    }

    public void setEmailCustomer(String email) {  //sets an email to a specific customer object and puts it into the database
        if(MySQL.isEmail(email)) {
            System.out.println("Email Already Taken");
            return;
        }
        MySQL.setEmail(username, email);
        this.email = email;
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
