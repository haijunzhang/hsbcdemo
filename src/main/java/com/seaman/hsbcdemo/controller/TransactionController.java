package com.seaman.hsbcdemo.controller;

import com.seaman.hsbcdemo.model.Transaction;
import com.seaman.hsbcdemo.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    /**
     * Create a new transaction.
     * 
     * @param transaction the transaction to create
     * @return the created transaction
     */

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        logger.info("Creating transaction: {}", transaction);
        return transactionService.createTransaction(transaction);
    }

    /**
     * Delete a transaction by its ID.
     * 
     * @param id the ID of the transaction to delete
     * @return a response entity with no content
     */ 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        logger.info("Deleting transaction with ID: {}", id);
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable Long id, @RequestBody Transaction transactionDetails) {
        logger.info("Updating transaction with ID: {}", id);
        return transactionService.updateTransaction(id, transactionDetails);
    }

    /**
     * Get transactions by page.
     * 
     * @param page the page number
     * @param size the number of transactions per page
     * @return a list of transactions
     */
    @GetMapping
    public List<Transaction> getTransactions(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        logger.info("Getting transactions by page: {}, size: {}", page, size);
        return transactionService.getTransactionsByPage(page, size);
    }

    /**
     * Get a transaction by its ID.
     * 
     * @param id the ID of the transaction to retrieve
     * @return the transaction
     */
    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        logger.info("Getting transaction by ID: {}", id);
        return transactionService.getTransactionById(id);
    }
}
