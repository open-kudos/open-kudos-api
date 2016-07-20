package kudos.services;

import kudos.model.Challenge;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.repositories.ChallengeRepository;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HistoryServiceTest {

    private HistoryService historyService;

    private TransactionRepository transactionRepository;
    private ChallengeRepository challengeRepository;
    private UserRepository userRepository;

    private Transaction mockedTransaction;
    private Challenge mockedChallenge;

    private static List<Transaction> mockedTransactions = new ArrayList<>();
    private static List<Challenge> mockedChallenges = new ArrayList<>();

    private static final User testUser = new User("test@test.lt", "", "", "");;

    static {
        mockedTransactions.add(new Transaction(testUser, testUser, 1, "", Transaction.Status.COMPLETED));
        mockedTransactions.add(new Transaction(testUser, testUser, 1, "", Transaction.Status.COMPLETED));
        mockedTransactions.add(new Transaction(testUser, testUser, 1, "", Transaction.Status.COMPLETED));
    }

    static {
        mockedChallenges.add(new Challenge(testUser, testUser, "", "", "", "", 1, Challenge.Status.ACCOMPLISHED));
        mockedChallenges.add(new Challenge(testUser, testUser, "", "", "", "", 1, Challenge.Status.ACCOMPLISHED));
        mockedChallenges.add(new Challenge(testUser, testUser, "", "", "", "", 1, Challenge.Status.ACCOMPLISHED));
    }

    @Before
    public void before() {
        transactionRepository = mock(TransactionRepository.class);
        challengeRepository = mock(ChallengeRepository.class);
        userRepository = mock(UserRepository.class);

        historyService = mock(HistoryService.class);

        mockedTransaction = mock(Transaction.class);
        mockedChallenge = mock(Challenge.class);

        when(transactionRepository.save(mockedTransaction)).thenReturn(mockedTransaction);
        when(userRepository.findByEmail(any(String.class))).thenReturn(new User("test", "test", "", "test@test.lt"));
        when(transactionRepository.findTransactionsBySenderAndStatus(any(User.class), any(Transaction.Status.class))).thenReturn(mockedTransactions);
        when(challengeRepository.findAllChallengesByCreatorUserAndStatus(any(User.class), any(Challenge.Status.class))).thenReturn(mockedChallenges);
    }

}
