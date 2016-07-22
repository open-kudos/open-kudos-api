package kudos.web.beans.response;

public class LeaderBoardItemResponse extends Response {

    private String fullName;
    private Integer kudosAmount;
    private String userId;

    public LeaderBoardItemResponse(String fullName, String userId, Integer kudosAmount){
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
