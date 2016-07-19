package kudos.model;

public class LeaderboardUser {
    private String firstName;
    private String lastName;
    private String email;
    private Integer amountOfKudos;

    public LeaderboardUser(String firstName, String lastName, String email, Integer amountOfKudos) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.amountOfKudos = amountOfKudos;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAmountOfKudos() {
        return amountOfKudos;
    }
}
