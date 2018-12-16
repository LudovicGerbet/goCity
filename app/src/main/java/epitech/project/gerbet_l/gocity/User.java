package epitech.project.gerbet_l.gocity;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private String firstName;
    private String lastName;
    private String token;

    public User() {
        firstName = null;
        lastName = null;
        token = UUID.randomUUID().toString();
    }

    public User(String first, String last) {
        firstName = first;
        lastName = last;
        token = UUID.randomUUID().toString();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getToken() {
        return token;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
