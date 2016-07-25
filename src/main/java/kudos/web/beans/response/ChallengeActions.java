package kudos.web.beans.response;

public class ChallengeActions {

    private boolean canCancel;
    private boolean canAccept;
    private boolean canDecline;
    private boolean canMarkAsCompleted;
    private boolean canMarkAsFailed;

    public ChallengeActions(boolean canCancel, boolean canAccept, boolean canDecline, boolean canMarkAsCompleted,
                            boolean canMarkAsFailed){
        this.canCancel = canCancel;
        this.canAccept = canAccept;
        this.canDecline = canDecline;
        this.canMarkAsCompleted = canMarkAsCompleted;
        this.canMarkAsFailed = canMarkAsFailed;
    }

    public boolean canCancel() {
        return canCancel;
    }

    public void setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
    }

    public boolean canAccept() {
        return canAccept;
    }

    public void setCanAccept(boolean canAccept) {
        this.canAccept = canAccept;
    }

    public boolean canDecline() {
        return canDecline;
    }

    public void setCanDecline(boolean canDecline) {
        this.canDecline = canDecline;
    }

    public boolean canMarkAsCompleted() {
        return canMarkAsCompleted;
    }

    public void setCanMarkAsCompleted(boolean canMarkAsCompleted) {
        this.canMarkAsCompleted = canMarkAsCompleted;
    }

    public boolean canMarkAsFailed() {
        return canMarkAsFailed;
    }

    public void setCanMarkAsFailed(boolean canMarkAsFailed) {
        this.canMarkAsFailed = canMarkAsFailed;
    }
}
