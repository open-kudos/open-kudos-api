package kudos.services;

import kudos.exceptions.TransactionException;
import kudos.model.Transaction;
import kudos.repositories.TransactionRepository;
import kudos.web.exceptions.UserException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by vytautassugintas on 02/05/16.
 */
public class TransactionServiceTest {

    private TransactionService transactionService;
    private Transaction mockedTransaction;
    private List<Transaction> mockedTransactions;
    private TransactionRepository transactionRepository;
    private static List<Transaction> transactions = new ArrayList<>();
    static {
        transactions.add(new Transaction("test@test.lt", "", "test@test.lt", "", 1, "", Transaction.Status.COMPLETED));
        transactions.add(new Transaction("test1@test1.lt", "", "test@test.lt", "", 1, "", Transaction.Status.CANCELED_CHALLENGE));
        transactions.add(new Transaction("test2@test2.lt", "", "test1@test1.lt", "", 1, "", Transaction.Status.COMPLETED_CHALLENGE));
    }

    @Before
    public void before() {
        transactionRepository = mock(TransactionRepository.class);
        transactionService = mock(TransactionService.class);
        mockedTransaction = mock(Transaction.class);

        when(transactionRepository.save(mockedTransaction)).thenReturn(mockedTransaction);
    }

    @Test
    public void testIfSaveMethodSavesTransaction() throws TransactionException{
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
    public void testIfServiceFindsLastChangedTransaction() throws TransactionException, UserException{
        when(transactionRepository.findTransactionsBySenderEmail(any(String.class))).thenReturn(transactions);
        assertEquals(false, transactionService.isLastTransactionChanged());
    }


}
