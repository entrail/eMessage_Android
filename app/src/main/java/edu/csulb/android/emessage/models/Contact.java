package edu.csulb.android.emessage.models;

public class Contact {
    public String email;
    public String firstName;
    public String lastName;

    public Contact() { this("", "", ""); }

    public Contact(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
