package kudos.model;


public class TeamMember {
    private String memberEmail;
    private Boolean isAccepted;

    public TeamMember(String memberEmail, Boolean isAccepted) {
        this.memberEmail = memberEmail;
        this.isAccepted = isAccepted;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setMemberEmail(String member) {
        this.memberEmail = member;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }
}
