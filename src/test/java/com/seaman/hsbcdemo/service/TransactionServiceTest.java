package com.seaman.hsbcdemo.service;

import com.seaman.hsbcdemo.repository.TransactionRepository;
import com.seaman.hsbcdemo.model.Transaction;
import com.seaman.hsbcdemo.model.TransactionStatus;
import com.seaman.hsbcdemo.model.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.sql.Timestamp;

//import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionServiceTest {
//    @Mock
    @Autowired
    private TransactionRepository transactionRepository;

//    @InjectMocks
    @Autowired
    private TransactionService transactionService;

    @Test
    public void testCreateTransaction() {
        Transaction transaction = createTransactionVo(TransactionType.PAYMENT);
        Transaction created = transactionService.createTransaction(transaction);

        // Verify that the transaction was saved in the H2 database
        Transaction savedTransaction = transactionRepository.findById(created.getId()).orElse(null);
        assertNotNull(savedTransaction);
        assertEquals(transaction.getAmount(), savedTransaction.getAmount());
        assertEquals(transaction.getTransactionNo(), savedTransaction.getTransactionNo());
        assertEquals(transaction.getType(), savedTransaction.getType());
        assertEquals(transaction.getStatus(), savedTransaction.getStatus());
        assertEquals(transaction.getPayerId(), savedTransaction.getPayerId());
        assertEquals(transaction.getPayeeId(), savedTransaction.getPayeeId());

        // Test transaction with duplicate transactionNo
        assertThrows(RuntimeException.class, () -> {
            Transaction transaction2 = createTransactionVo(TransactionType.PAYMENT);
            transaction2.setTransactionNo(savedTransaction.getTransactionNo());
            transactionService.createTransaction(transaction2);
        });
    }

    @Test
    void deleteTransaction() {
        Transaction transaction = createTransactionVo(TransactionType.PAYMENT);
        Transaction created = transactionService.createTransaction(transaction);
        assertNotNull(created);
        transactionService.deleteTransaction(created.getId());
        Transaction deletedTransaction = transactionRepository.findById(created.getId()).orElse(null);
        assertNull(deletedTransaction);
    }

    @Test
    void updateTransaction() {
        Transaction transaction = createTransactionVo(TransactionType.PAYMENT);
        Transaction created = transactionService.createTransaction(transaction);
        assertNotNull(created);

        Transaction toUpdate = transactionRepository.findById(created.getId()).orElse(null);
        toUpdate.setAmount(BigDecimal.valueOf(10000));
        toUpdate.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
        transactionService.updateTransaction(created.getId(), toUpdate);

        Transaction updatedTransaction = transactionRepository.findById(created.getId()).orElse(null);
        assertEquals(created.getId(), updatedTransaction.getId());
        assertNotEquals(created.getAmount(), updatedTransaction.getAmount());
        assertNotEquals(created.getUpdatedTime(), updatedTransaction.getUpdatedTime());
    }

    @Test
    void getTransactionsByPage() {
        for (int i = 0; i < 15; i++) {
            Transaction transaction = createTransactionVo(TransactionType.PAYMENT);
            transactionService.createTransaction(transaction);
        }
        List<Transaction> list1 = transactionService.getTransactionsByPage(0, 10);
        assertEquals(10, list1.size());
        List<Transaction> list2 = transactionService.getTransactionsByPage(1, 10);
        assertEquals(5, list2.size());

    }

    @Test
    void getTransactionById() {
        Transaction transaction = createTransactionVo(TransactionType.PAYMENT);
        Transaction created = transactionService.createTransaction(transaction);

        Transaction savedTransaction = transactionRepository.findById(created.getId()).orElse(null);
        assertNotNull(savedTransaction);
    }

    @Test
    void testTransactionTypes() {
        // Test PAYMENT
        Transaction payment = createTransactionVo(TransactionType.PAYMENT);
        Transaction savedPayment = transactionService.createTransaction(payment);
        assertEquals(TransactionType.PAYMENT, savedPayment.getType());
        
        // Test REFUND
        Transaction refund = createTransactionVo(TransactionType.REFUND);
        Transaction savedRefund = transactionService.createTransaction(refund);
        assertEquals(TransactionType.REFUND, savedRefund.getType());
        
        // Test invalid type update
        assertThrows(IllegalArgumentException.class, () -> {
            savedPayment.setType(null);
            transactionService.updateTransaction(savedPayment.getId(), savedPayment);
        });
    }


    /**
     * create a transaction pojo for testing
     * @return
     */
    private Transaction createTransactionVo(TransactionType type) {
        Transaction transaction = new Transaction();
        Random random = new Random();
        BigDecimal amount = BigDecimal.valueOf(random.nextDouble() * 10000).setScale(2, BigDecimal.ROUND_HALF_UP);
        transaction.setAmount(amount);
        transaction.setTransactionNo(UUID.randomUUID().toString());
        transaction.setType(type);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setPayerId((long) random.nextInt(10000));
        transaction.setPayeeId((long) random.nextInt(10000));
        return transaction;
    }
}