package edu.shtoiko.infrastructureservice.model;

import edu.shtoiko.infrastructureservice.model.enums.TransactionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class WithdrawResult {
    private String terminalIdentifier;

    private String producerIdentifier;

    private TransactionStatus transactionStatus;

    private String systemComment;
}
