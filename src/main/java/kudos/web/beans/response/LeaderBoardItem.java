package kudos.web.beans.response;

public class LeaderBoardItem extends Response {

    private String firstName;
    private String lastName;
    private Integer kudosAmount;
    private String userId;

    public LeaderBoardItem(String firstName, String lastName, String userId, Integer kudosAmount){
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.kudosAmount = kudosAmount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getKudosAmount() {
        return kudosAmount;
    }

    public void setKudosAmount(Integer kudosAmount) {
        this.kudosAmount = kudosAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
