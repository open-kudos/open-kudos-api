package kudos.web.beans.response;

import kudos.model.Challenge;
import kudos.model.Transaction;

public class HistoryResponse {

        private String receiverEmail;
        private String receiverFullName;
        private String senderEmail;
        private String senderFullName;
        private int amount;
        private String comment;
        private String timestamp;
        private Transaction.Status status;

        public HistoryResponse(String receiverEmail, String receiverFullName, String senderEmail, String senderFullName, int amount, String comment, String timestamp, Transaction.Status status) {
            this.receiverEmail = receiverEmail;
            this.receiverFullName = receiverFullName;
            this.senderEmail = senderEmail;
            this.senderFullName = senderFullName;
            this.amount = amount;
            this.comment = comment;
            this.timestamp = timestamp;
            this.status = status;
        }

    public HistoryResponse(Transaction transaction) {
        if (!transaction.getSender().getEmail().equals("master@of.kudos")) {
            this.receiverEmail = transaction.getReceiver().getEmail();
            this.receiverFullName = transaction.getReceiver().getFirstName() + " " + transaction.getReceiver().getLastName();
            this.senderEmail = transaction.getSender().getEmail();
            this.senderFullName = transaction.getSender().getFirstName() + " " + transaction.getSender().getLastName();
            this.amount = transaction.getAmount();
            this.comment = transaction.getMessage();
            this.timestamp = transaction.getTimestamp();
            this.status = transaction.getStatus();
        }
    }

    public HistoryResponse(Challenge challenge) {
        this.receiverEmail = challenge.getParticipantUser().getEmail();
        this.receiverFullName = challenge.getParticipantUser().getFirstName() + " " + challenge.getParticipantUser().getLastName();
        this.senderEmail = challenge.getCreatorUser().getEmail();
        this.senderFullName = challenge.getCreatorUser().getFirstName() + " " + challenge.getCreatorUser().getLastName();
        this.amount = challenge.getAmount() * 2;
        this.comment = challenge.getDescription();
        this.timestamp = challenge.getCreateDateDate();
        this.status = challengeStatus(challenge.getCreatorStatus(), challenge.getParticipantStatus());
    }

    private Transaction.Status challengeStatus(Boolean creatorStatus, Boolean participantStatus){
        if (creatorStatus != null)
            if (creatorStatus) return Transaction.Status.COMPLETED_CHALLENGE_CREATOR;
            else return Transaction.Status.COMPLETED_CHALLENGE_PARTICIPANT;

        if (participantStatus != null)
            if (participantStatus) return Transaction.Status.COMPLETED_CHALLENGE_PARTICIPANT;
            else return Transaction.Status.COMPLETED_CHALLENGE_CREATOR;

        return Transaction.Status.COMPLETED_CHALLENGE;
    }

        public String getReceiverEmail() {
            return receiverEmail;
        }

        public void setReceiverEmail(String receiverEmail) {
            this.receiverEmail = receiverEmail;
        }

        public String getReceiverFullName() {
            return receiverFullName;
        }

        public void setReceiverFullName(String receiverFullName) {
            this.receiverFullName = receiverFullName;
        }

        public String getSenderEmail() {
            return senderEmail;
        }

        public void setSenderEmail(String senderEmail) {
            this.senderEmail = senderEmail;
        }

        public String getSenderFullName() {
            return senderFullName;
        }

        public void setSenderFullName(String senderFullName) {
            this.senderFullName = senderFullName;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public Transaction.Status getStatus() {
            return status;
        }

        public void setStatus(Transaction.Status status) {
            this.status = status;
        }
}
