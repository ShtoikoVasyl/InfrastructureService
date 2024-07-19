package edu.shtoiko.infrastructureservice.service.implementation;

import edu.shtoiko.infrastructureservice.model.CurrentAccount;
import edu.shtoiko.infrastructureservice.repository.AccountRepository;
import edu.shtoiko.infrastructureservice.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public CurrentAccount findAccountByAccountNumber(long accountNumber){
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> {
            log.error("Account with accountNumber={} not found", accountNumber);
            return new EntityNotFoundException("Account with accountNumber=" + accountNumber + " not found");
        });
    }

}
