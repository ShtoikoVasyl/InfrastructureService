package edu.shtoiko.infrastructureservice.service.implementation;

import edu.shtoiko.infrastructureservice.exeptions.WithdrawalException;
import edu.shtoiko.infrastructureservice.model.CurrentAccount;
import edu.shtoiko.infrastructureservice.model.Transaction;
import edu.shtoiko.infrastructureservice.model.enums.TransactionStatus;
import edu.shtoiko.infrastructureservice.repository.TransactionRepository;
import edu.shtoiko.infrastructureservice.service.AccountService;
import edu.shtoiko.infrastructureservice.service.WithdrawalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawalServiceImpl implements WithdrawalService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    @Value("${withdrawalservice.deafultaccountnumber}")
    private long bankAccount;

    //todo it should be improved
    public String provideWithdraw(long accountNumber, int pinCode,String currencyCode, long amount) throws WithdrawalException {
        CurrentAccount account = accountService.findAccountByAccountNumber(accountNumber);
        if(account.getPinCode() != pinCode){
            log.error("AccountNumber={} : Access is denied because pinCodes do not match", account.getAccountNumber());
            throw new WithdrawalException("Access is denied");
        }
        if(account.getAmount().compareTo(new BigDecimal(amount)) <= 0){
            log.error("AccountNumber={} : Not enough money", account.getAccountNumber());
            throw new WithdrawalException("Not enough money");
        }
        createWithdrawRequest(accountNumber,currencyCode, amount);
        log.info("AccountNumber={} : Operation is allowed", account.getAccountNumber());
        return "Operation is allowed";
    }

    private void createWithdrawRequest(long accountNumber, String currencyCode, long amount) {
        Transaction transaction = Transaction.builder()
                .date(Instant.now())
                .currencyCode(currencyCode)
                .transactionStatus(TransactionStatus.NEW)
                .amount(new BigDecimal(amount))
                .senderAccountNumber(accountNumber)
                .receiverAccountNumber(bankAccount)
                .systemComment("Withdraw by terminal")
                .description("")
                .build();
        transactionRepository.save(transaction);
        log.info("AccountNumber={} : transaction saved", accountNumber);
    }
}
