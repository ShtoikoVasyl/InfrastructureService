package edu.shtoiko.infrastructureservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.shtoiko.infrastructureservice.model.WithdrawResult;
import edu.shtoiko.infrastructureservice.model.WithdrawalTransaction;
import edu.shtoiko.infrastructureservice.utils.JacksonDeserializer;
import edu.shtoiko.infrastructureservice.utils.JacksonSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.JacksonUtils;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    @Bean
    public ProducerFactory<String, WithdrawalTransaction> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonSerializer.class.getName());

        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(),
            new JacksonSerializer<>(objectMapper()));
    }

    @Bean
    public KafkaTemplate<String, WithdrawalTransaction> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, WithdrawResult> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonDeserializer.class.getName());

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(),
            new JacksonDeserializer<>(objectMapper(), WithdrawResult.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WithdrawResult> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, WithdrawResult> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}