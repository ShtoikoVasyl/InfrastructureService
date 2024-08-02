package edu.shtoiko.infrastructureservice.service;

import edu.shtoiko.infrastructureservice.exeptions.WithdrawalException;
import edu.shtoiko.infrastructureservice.model.WithdrawResult;
import edu.shtoiko.infrastructureservice.model.WithdrawalTransaction;

public interface WithdrawalService {
    WithdrawalTransaction provideWithdraw(long username, long accountNumber, int pinCode, String currencyCode,
        long amount)
        throws WithdrawalException;
}
