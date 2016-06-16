package kudos.model;

/**
 * Created by vytautassugintas on 16/06/16.
 */

// HACK FOR HACKATON

public class UserKudos{
    int userRemainingKudos;
    int userReceivedKudos;

    public UserKudos(int userRemainingKudos, int userReceivedKudos) {
        this.userRemainingKudos = userRemainingKudos;
        this.userReceivedKudos = userReceivedKudos;
    }

    public int getUserRemainingKudos() {
        return userRemainingKudos;
    }

    public void setUserRemainingKudos(int userRemainingKudos) {
        this.userRemainingKudos = userRemainingKudos;
    }

    public int getUserReceivedKudos() {
        return userReceivedKudos;
    }

    public void setUserReceivedKudos(int userReceivedKudos) {
        this.userReceivedKudos = userReceivedKudos;
    }
}
