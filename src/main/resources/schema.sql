-- 创建交易表
CREATE TABLE IF NOT EXISTS transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_no VARCHAR(50) NOT NULL UNIQUE COMMENT '交易编号',
    amount DECIMAL(19,2) NOT NULL COMMENT '交易金额',
    transaction_type VARCHAR(20) NOT NULL COMMENT '交易类型：PAYMENT/REFUND',
    status VARCHAR(20) NOT NULL COMMENT '交易状态：PENDING/SUCCESS/FAILED',
    payer_id BIGINT NOT NULL COMMENT '付款方ID',
    payee_id BIGINT NOT NULL COMMENT '收款方ID',
    created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '交易时间',
    updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'

); 

-- 为 transaction_id 创建唯一索引（如果未在表定义中指定唯一约束）
CREATE UNIQUE INDEX idx_transaction_no ON transaction (transaction_no);

-- 为 timestamp 创建索引，方便按时间查询
CREATE INDEX idx_created_time ON transaction (created_time);