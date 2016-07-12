package kudos.services;

import kudos.exceptions.TransactionException;
import kudos.model.Transaction;
import kudos.model.User;
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
    private static final User mockedUser =  new User("test@test.lt", "test", "test", "");

    static {
        transactions.add(new Transaction(mockedUser, mockedUser, 1, "", Transaction.Status.COMPLETED));
        transactions.add(new Transaction(mockedUser, mockedUser, 1, "", Transaction.Status.COMPLETED));
        transactions.add(new Transaction(mockedUser, mockedUser, 1, "", Transaction.Status.COMPLETED));
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
        when(transactionRepository.findTransactionsByReceiver(any(User.class))).thenReturn(transactions);
        assertEquals("test@test.lt", transactionRepository.findTransactionsByReceiver(mockedUser).get(0));
    }

    @Test
    public void testIfServiceFindsTransactionsBySenderEmail() throws TransactionException{
        when(transactionRepository.findTransactionsBySender(any(User.class))).thenReturn(transactions);
        assertEquals("test@test.lt", transactionRepository.findTransactionsBySender(mockedUser).get(0).getSender().getEmail());
    }

    @Test
    public void testIfServiceFindsLastChangedTransaction() throws TransactionException, UserException{
        when(transactionRepository.findTransactionsBySender(any(User.class))).thenReturn(transactions);
        assertEquals(false, transactionService.isLastTransactionChanged(""));
    }


}
