package kudos.web.beans.response;

import kudos.model.LeaderBoardItem;

public class LeaderBoardItemResponse extends Response {

    private String fullName;
    private int kudosAmount;
    private String userId;

    public LeaderBoardItemResponse(LeaderBoardItem item){
        this.userId = item.getUserId();
        this.fullName = item.getFullName();
        this.kudosAmount = item.getKudosAmount();
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
