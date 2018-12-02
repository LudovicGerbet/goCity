package epitech.project.gerbet_l.gocity;

import java.io.Serializable;

public class User implements Serializable {
    private String firstName;
    private String lastName;

    public User() {
        firstName = null;
        lastName = null;
    }

    public User(String first, String last) {
        firstName = first;
        lastName = last;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
