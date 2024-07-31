package edu.shtoiko.infrastructureservice.service;

import edu.shtoiko.infrastructureservice.model.WithdrawResult;

public interface MessageConsumerService {
    void processTransaction(WithdrawResult transaction);
}
