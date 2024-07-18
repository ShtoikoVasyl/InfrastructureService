package edu.shtoiko.infrastructureservice.service;

import edu.shtoiko.infrastructureservice.exeptions.WithdrawalException;

public interface WithdrawalService {
    String provideWithdraw(long accountNumber, int pinCode,String currencyCode, long amount) throws WithdrawalException;
}
