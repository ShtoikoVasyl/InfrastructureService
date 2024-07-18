package edu.shtoiko.infrastructureservice.model;

import edu.shtoiko.infrastructureservice.model.enums.TransactionStatus;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document
@Data
//@Table(name = "transaction")
@Builder
public class Transaction {
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
