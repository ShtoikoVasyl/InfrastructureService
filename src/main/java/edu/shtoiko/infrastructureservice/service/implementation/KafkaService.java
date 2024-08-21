package edu.shtoiko.infrastructureservice.service.implementation;

import edu.shtoiko.infrastructureservice.model.WithdrawResult;
import edu.shtoiko.infrastructureservice.model.WithdrawalTransaction;
import edu.shtoiko.infrastructureservice.service.MessageConsumerService;
import edu.shtoiko.infrastructureservice.service.MessageProducerService;
import edu.shtoiko.infrastructureservice.terminalcontroller.grpcclient.WithdrawResponseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService implements MessageProducerService, MessageConsumerService {

    @Value("${kafka.withdrawal.topic}")
    private String WITHDRAWAL_TRANSACTIONS_TOPIC;

    private final WithdrawResponseHandler responseHandler;

    private final KafkaTemplate<String, WithdrawalTransaction> kafkaTemplate;

    @Override
    public void sendMessage(WithdrawalTransaction message) {
        kafkaTemplate.send(WITHDRAWAL_TRANSACTIONS_TOPIC, message);
    }

    @Override
    @KafkaListener(topics = "${kafka.withdraw-result.topic}", groupId = "${kafka.infrastructureservice.group-id}")
    public void processTransaction(WithdrawResult transaction) {
        log.info("Received withdraw result : {}", transaction.toString());
        responseHandler.sendWithdrawResponse(transaction);
    }
}
