package kudos.services;

import kudos.model.Challenge;
import kudos.model.Transaction;
import kudos.model.history.History;
import kudos.repositories.ChallengeRepository;
import kudos.repositories.TransactionRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vytautassugintas on 22/06/16.
 */
public class HistoryServiceTest {

    private HistoryService historyService;
    private History mockedHistory;
    private List<History> mockedHistories;

    private TransactionRepository transactionRepository;
    private ChallengeRepository challengeRepository;

    private static List<Transaction> mockedTransaction = new ArrayList<>();
    private static List<Challenge> mockedChallenges = new ArrayList<>();
/*
    static {
        mockedTransaction.add(new Transaction("test@test.lt", "", "test@test.lt", "", 1, "", Transaction.Status.COMPLETED));
        mockedTransaction.add(new Transaction("test1@test1.lt", "", "test@test.lt", "", 1, "", Transaction.Status.CANCELED_CHALLENGE));
        mockedTransaction.add(new Transaction("test2@test2.lt", "", "test1@test1.lt", "", 1, "", Transaction.Status.COMPLETED_CHALLENGE));
    }

    @Before
    public void before() {
        transactionRepository = mock(TransactionRepository.class);
        transactionService = mock(TransactionService.class);
        mockedTransaction = mock(Transaction.class);

        when(transactionRepository.save(mockedTransaction)).thenReturn(mockedTransaction);
    }

    @Test
    public void testIfSaveMethodSavesTransaction() throws TransactionException {
        assertEquals(mockedTransaction, transactionRepository.save(mockedTransaction));
    }

    @Test
    public void testIfServiceFindsTransactionsByReceiverEmail() throws TransactionException{
        when(transactionRepository.findTransactionsByReceiverEmail(any(String.class))).thenReturn(transactions);
        assertEquals("test@test.lt", transactionRepository.findTransactionsByReceiverEmail("").get(0).getReceiverEmail());
    }

    @Test
    public void testIfServiceFindsTransactionsBySenderEmail() throws TransactionException{
        when(transactionRepository.findTransactionsBySenderEmail(any(String.class))).thenReturn(transactions);
        assertEquals("test@test.lt", transactionRepository.findTransactionsBySenderEmail("").get(0).getSenderEmail());
    }

    @Test
    public void testIfServiceFindsLastChangedTransaction() throws TransactionException, UserException {
        when(transactionRepository.findTransactionsBySenderEmail(any(String.class))).thenReturn(transactions);
        assertEquals(false, transactionService.isLastTransactionChanged(""));
    }

    */

}
