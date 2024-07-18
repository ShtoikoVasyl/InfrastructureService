package edu.shtoiko.infrastructureservice.service.implementation;

import edu.shtoiko.infrastructureservice.exeptions.WithdrawalException;
import edu.shtoiko.infrastructureservice.model.CurrentAccount;
import edu.shtoiko.infrastructureservice.model.Transaction;
import edu.shtoiko.infrastructureservice.model.enums.TransactionStatus;
import edu.shtoiko.infrastructureservice.repository.TransactionRepository;
import edu.shtoiko.infrastructureservice.service.AccountService;
import edu.shtoiko.infrastructureservice.service.WithdrawalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class WithdrawalServiceImpl implements WithdrawalService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    @Value("${withdrawalservice.deafultaccountnumber}")
    private long bankAccount;

    public String provideWithdraw(long accountNumber, int pinCode,String currencyCode, long amount) throws WithdrawalException {
        CurrentAccount account = accountService.findAccountByAccountNumber(accountNumber);
        if(account.getPinCode() != pinCode){
            throw new WithdrawalException("Access is denied");
        }
        if(account.getAmount().compareTo(new BigDecimal(amount)) <= 0){
            throw new WithdrawalException("Not enough money");
        }
        createWithdrawRequest(accountNumber,currencyCode, amount);
        return "Operation is allowed";
    }

    private boolean createWithdrawRequest(long accountNumber,String currencyCode, long amount) {
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
        return true;
    }
}
