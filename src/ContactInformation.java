public class ContactInformation {
    String phoneNumber;
    String email;
    public ContactInformation(String phoneNumber, String email) {
        this.phoneNumber = phoneNumber;
        this.email = email;

        MySQL.insertContactInfo(email, phoneNumber);  //inserts info into the database
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getEmail(){
        return email;
    }
}
