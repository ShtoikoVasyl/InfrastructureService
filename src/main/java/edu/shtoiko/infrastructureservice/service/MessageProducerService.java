package edu.shtoiko.infrastructureservice.service;

import edu.shtoiko.infrastructureservice.model.WithdrawalTransaction;

public interface MessageProducerService {
    void sendMessage(WithdrawalTransaction message);
}
