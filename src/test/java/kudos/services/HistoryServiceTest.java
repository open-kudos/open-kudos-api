package kudos.services;

import kudos.model.Challenge;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.model.history.History;
import kudos.repositories.ChallengeRepository;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by vytautassugintas on 22/06/16.
 */
public class HistoryServiceTest {

    private HistoryService historyService;

    private TransactionRepository transactionRepository;
    private ChallengeRepository challengeRepository;
    private UserRepository userRepository;

    private History mockedHistory;
    private Transaction mockedTransaction;
    private Challenge mockedChallenge;
    private User mockedUser;

    private List<History> mockedHistories;
    private static List<Transaction> mockedTransactions = new ArrayList<>();
    private static List<Challenge> mockedChallenges = new ArrayList<>();

    static {
        mockedTransactions.add(new Transaction("test@test.lt", "", "test@test.lt", "", 1, "", Transaction.Status.COMPLETED));
        mockedTransactions.add(new Transaction("test1@test1.lt", "", "test@test.lt", "", 1, "", Transaction.Status.CANCELED_CHALLENGE));
        mockedTransactions.add(new Transaction("test2@test2.lt", "", "test1@test1.lt", "", 1, "", Transaction.Status.COMPLETED_CHALLENGE));
    }

    static {
        mockedChallenges.add(new Challenge("test@test.lt", "", "test@test.lt", "", "", "", 1, Challenge.Status.ACCOMPLISHED));
        mockedChallenges.add(new Challenge("test1@test1.lt", "", "test@test.lt", "", "", "", 1, Challenge.Status.CANCELED));
        mockedChallenges.add(new Challenge("test2@test2.lt", "", "test1@test1.lt", "", "", "", 1, Challenge.Status.CREATED));
    }

    @Before
    public void before() {
        transactionRepository = mock(TransactionRepository.class);
        challengeRepository = mock(ChallengeRepository.class);
        userRepository = mock(UserRepository.class);

        historyService = mock(HistoryService.class);

        mockedUser = mock(User.class);
        mockedHistory = mock(History.class);
        mockedTransaction = mock(Transaction.class);
        mockedChallenge = mock(Challenge.class);

        when(transactionRepository.save(mockedTransaction)).thenReturn(mockedTransaction);
        when(userRepository.findByEmail(any(String.class))).thenReturn(new User("test", "test", "", "test@test.lt"));
        when(transactionRepository.findTransactionsByReceiverEmailAndStatus(any(String.class), any(Transaction.Status.class))).thenReturn(mockedTransactions);
        when(transactionRepository.findTransactionsBySenderEmailAndStatus(any(String.class), any(Transaction.Status.class))).thenReturn(mockedTransactions);
        when(challengeRepository.findAllChallengesByCreatorAndStatus(any(String.class), any(Challenge.Status.class))).thenReturn(mockedChallenges);
        when(challengeRepository.findAllChallengesByParticipantAndStatus(any(String.class), any(Challenge.Status.class))).thenReturn(mockedChallenges);
    }

    @Test
    public void testIfTransactionModelCanBeTransformedToHistoryModel(){
        History history = historyService.transformTransactionModelToHistory(mockedTransaction);
        assertSame(history, history);
    }

    @Test
    public void testIfChallengeModelCanBeTransformedToHistoryModel(){
        History history = historyService.transformChallengeModelToHistory(mockedChallenge);
        assertSame(history, history);
    }

}
