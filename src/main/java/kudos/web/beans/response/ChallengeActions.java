package kudos.web.beans.response;

public class ChallengeActions extends Response {

    private boolean cancelAllowed;
    private boolean acceptAllowed;
    private boolean declineAllowed;
    private boolean markAsCompletedAllowed;
    private boolean markAsFailedAllowed;

    public ChallengeActions(boolean cancelAllowed, boolean acceptAllowed, boolean declineAllowed, boolean markAsCompletedAllowed,
                            boolean markAsFailedAllowed){
        this.cancelAllowed = cancelAllowed;
        this.acceptAllowed = acceptAllowed;
        this.declineAllowed = declineAllowed;
        this.markAsCompletedAllowed = markAsCompletedAllowed;
        this.markAsFailedAllowed = markAsFailedAllowed;
    }

    public boolean isCancelAllowed() {
        return cancelAllowed;
    }

    public void setCancelAllowed(boolean cancelAllowed) {
        this.cancelAllowed = cancelAllowed;
    }

    public boolean isAcceptAllowed() {
        return acceptAllowed;
    }

    public void setAcceptAllowed(boolean acceptAllowed) {
        this.acceptAllowed = acceptAllowed;
    }

    public boolean isDeclineAllowed() {
        return declineAllowed;
    }

    public void setDeclineAllowed(boolean declineAllowed) {
        this.declineAllowed = declineAllowed;
    }

    public boolean isMarkAsCompletedAllowed() {
        return markAsCompletedAllowed;
    }

    public void setMarkAsCompletedAllowed(boolean markAsCompletedAllowed) {
        this.markAsCompletedAllowed = markAsCompletedAllowed;
    }

    public boolean isMarkAsFailedAllowed() {
        return markAsFailedAllowed;
    }

    public void setMarkAsFailedAllowed(boolean markAsFailedAllowed) {
        this.markAsFailedAllowed = markAsFailedAllowed;
    }

    public boolean isCanCancel() {
        return canCancel;
    }

    public boolean isCanAccept() {
        return canAccept;
    }

    public boolean isCanDecline() {
        return canDecline;
    }

    public boolean isCanMarkAsCompleted() {
        return canMarkAsCompleted;
    }

    public boolean isCanMarkAsFailed() {
        return canMarkAsFailed;
    }
}
