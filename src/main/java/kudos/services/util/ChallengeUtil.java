package kudos.services.util;

import kudos.model.Challenge;
import kudos.model.status.ChallengeStatus;
import kudos.model.Transaction;
import kudos.model.status.TransactionStatus;
import kudos.repositories.ChallengeRepository;
import kudos.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ChallengeUtil {

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    TransactionRepository transactionRepository;

    public Challenge changeStatus(Challenge challenge, ChallengeStatus challengeStatus) {
        Transaction transaction = challenge.getTransaction();
        transaction.setStatus(TransactionStatus.CANCELED);
        transactionRepository.save(transaction);

        challenge.setStatus(challengeStatus);
        challengeRepository.save(challenge);
        return challenge;
    }

}
