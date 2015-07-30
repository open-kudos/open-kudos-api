package kudos.model;

/**
 * Created by chc on 15.7.23.
 */
public class User {

    private String password;
    private String email;
    private String firstName;
    private String lastName;

    public User(String password, String email,String firstName, String lastName){
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getPassword(){
        return this.password;
    }

    public String getEmail(){
        return this.email;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public void setEncryptedPassword(String password){
        this.password = password;
    }

}
