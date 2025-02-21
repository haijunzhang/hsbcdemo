package com.seaman.hsbcdemo.repository;

import com.seaman.hsbcdemo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByTransactionNo(String transactionNo);
}
