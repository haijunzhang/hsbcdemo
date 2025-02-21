package com.seaman.hsbcdemo.service;


import com.seaman.hsbcdemo.model.Transaction;
import com.seaman.hsbcdemo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.cache.annotation.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    Logger logger = LoggerFactory.getLogger(TransactionService.class);


    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        logger.info("Creating a new transaction with details: {}", transaction);
        if (transaction.getType() == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        logger.info("Deleting transaction with ID: {}", id);
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found");
        }
        transactionRepository.deleteById(id);
    }


    @Transactional
    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        logger.info("Updating transaction with ID: {}", id);
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));
        
        if (transactionDetails.getType() == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        
        transaction.setType(transactionDetails.getType());
        transaction.setAmount(transactionDetails.getAmount());
        transaction.setStatus(transactionDetails.getStatus());
        transaction.setPayerId(transactionDetails.getPayerId());
        transaction.setPayeeId(transactionDetails.getPayeeId());
        transaction.setUpdatedTime(transactionDetails.getUpdatedTime());
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "myCache", key = "'page_' + #page + '_size_' + #size")
    public List<Transaction> getTransactionsByPage(int page, int size) {
        logger.info("Getting transactions by page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
        return transactionPage.getContent();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "myCache", key = "#id")
    public Transaction getTransactionById(Long id) {
        logger.info("Getting transaction by ID: {}", id);
        return transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }
}
