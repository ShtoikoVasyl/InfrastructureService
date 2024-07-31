package edu.shtoiko.infrastructureservice.service.implementation;

import edu.shtoiko.infrastructureservice.model.WithdrawResult;
import edu.shtoiko.infrastructureservice.service.MessageConsumerService;
import edu.shtoiko.infrastructureservice.service.MessageProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService implements MessageProducerService, MessageConsumerService {
    private static final String WITHDRAWAL_TRANSACTIONS_TOPIC = "withdrawal_transactions";
    private final static String WITHDRAWAL_RESULT_TOPIC = "withdraw_results";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendMessage(Object message) {
        kafkaTemplate.send(WITHDRAWAL_TRANSACTIONS_TOPIC, message);
    }

    @Override
    @KafkaListener(topics = WITHDRAWAL_RESULT_TOPIC, groupId = "${kafka.consumer.group-id}")
    public void processTransaction(WithdrawResult transaction) {
        log.info("Received withdraw result : {}", transaction.toString());
    }
}
