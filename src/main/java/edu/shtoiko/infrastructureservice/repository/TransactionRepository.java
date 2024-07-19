package edu.shtoiko.infrastructureservice.repository;

import edu.shtoiko.infrastructureservice.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    public List<Transaction> findAllByReceiverAccountNumberOrSenderAccountNumber(Long receiverAccountId, Long senderAccountId);
}