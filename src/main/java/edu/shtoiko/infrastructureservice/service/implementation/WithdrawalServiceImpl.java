package edu.shtoiko.infrastructureservice.service.implementation;

import edu.shtoiko.infrastructureservice.exeptions.WithdrawalException;
import edu.shtoiko.infrastructureservice.model.WithdrawalTransaction;
import edu.shtoiko.infrastructureservice.model.enums.TransactionStatus;
import edu.shtoiko.infrastructureservice.service.MessageProducerService;
import edu.shtoiko.infrastructureservice.service.WithdrawalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawalServiceImpl implements WithdrawalService {
    private final MessageProducerService messageProducerService;

    // todo it should be improved
    public String provideWithdraw(long username, long accountNumber, int pinCode, String currencyCode, long amount)
        throws WithdrawalException {
        sendWithdrawTransaction(createWithdrawTransaction(username, accountNumber, currencyCode, amount, pinCode));
        return "Operation is allowed";
    }

    private void sendWithdrawTransaction(WithdrawalTransaction transaction) {
        messageProducerService.sendMessage(transaction);
        log.info("AccountNumber={} : withdrawalTransaction sent", transaction.getSenderAccountNumber());
    }

    private WithdrawalTransaction createWithdrawTransaction(long username, long accountNumber, String currencyCode,
        long amount, int pinCode) {
        return WithdrawalTransaction.builder()
            .terminalIdentifier(username + "t" + Instant.now())
            .date(Instant.now())
            .currencyCode(currencyCode)
            .transactionStatus(TransactionStatus.NEW)
            .amount(new BigDecimal(amount))
            .senderAccountNumber(accountNumber)
            .systemComment("Withdraw by terminal")
            .description("")
            .pinCode(pinCode)
            .build();
    }
}
