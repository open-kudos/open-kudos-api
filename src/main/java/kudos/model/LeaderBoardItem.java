package kudos.model;

public class LeaderBoardItem {

    private String fullName;
    private Integer kudosAmount;
    private String userId;

    public LeaderBoardItem(String fullName, String userId, int kudosAmount){
        this.userId = userId;
        this.fullName = fullName;
        this.kudosAmount = kudosAmount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getKudosAmount() {
        return kudosAmount;
    }

    public void setKudosAmount(int kudosAmount) {
        this.kudosAmount = kudosAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
