package edu.shtoiko.infrastructureservice.service;

import edu.shtoiko.infrastructureservice.model.CurrentAccount;

public interface AccountService {
    CurrentAccount findAccountByAccountNumber(long accountNumber);
}
