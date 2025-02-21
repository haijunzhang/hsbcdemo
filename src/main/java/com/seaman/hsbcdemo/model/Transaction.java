package com.seaman.hsbcdemo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Number of transaction
     */
    @Column(unique = true,nullable = false)
    private String transactionNo;
    /**
     * the amount of transaction
     */
    @Column(nullable = false)
    private BigDecimal amount;
    /**
     * type of transaction:PAYMENT/REFUND/TRANSFER
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    /**
     * status of transaction:SUCCESS/FAILED
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;


    /**
     * the id of payer
     */
    @Column(nullable = false)
    private Long payerId;
    /**
     * the id of payee
     */
    @Column(nullable = false)
    private Long payeeId;
    /**
     * time of transaction, defaults to current time
     */
     @Column(nullable = false)
     private Timestamp createdTime = new Timestamp(System.currentTimeMillis());
    /**
     * time of updated
     */
    @Column(nullable = false)
    private Timestamp updatedTime = new Timestamp(System.currentTimeMillis());

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }
}
