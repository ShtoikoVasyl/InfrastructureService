package edu.shtoiko.infrastructureservice.model;

import edu.shtoiko.infrastructureservice.model.enums.TransactionStatus;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.Instant;

public abstract class FinancialOperation {
    @Id
    private String id;

    private Instant date;

    private Long receiverAccountNumber;

    private Long senderAccountNumber;

    private BigDecimal amount;

    private String currencyCode;

    private String description;

    private TransactionStatus transactionStatus;

    private String systemComment;
}
