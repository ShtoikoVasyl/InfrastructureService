package edu.shtoiko.infrastructureservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.shtoiko.infrastructureservice.model.WithdrawResult;
import edu.shtoiko.infrastructureservice.model.WithdrawalTransaction;
import edu.shtoiko.infrastructureservice.utils.JacksonDeserializer;
import edu.shtoiko.infrastructureservice.utils.JacksonSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.JacksonUtils;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.infrastructure-service.group-id}")
    private String groupId;

    @Value("${spring.kafka.properties.sasl.jaas.config:}")
    private String jaasConfig;

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    private Map<String, Object> commonConfigProps() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonSerializer.class.getName());
        return configProps;
    }

    @Bean
    @Profile("prod")
    public ProducerFactory<String, WithdrawalTransaction> cloudProducerFactory() {
        Map<String, Object> configProps = commonConfigProps();
        configProps.put("security.protocol", "SASL_SSL");
        configProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);

        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(),
            new JacksonSerializer<>(objectMapper()));
    }

    @Bean
    @Profile("dev")
    public ProducerFactory<String, WithdrawalTransaction> localProducerFactory() {
        Map<String, Object> configProps = commonConfigProps();
        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(),
            new JacksonSerializer<>(objectMapper()));
    }

    @Bean
    public KafkaTemplate<String, WithdrawalTransaction> kafkaTemplate(
        ProducerFactory<String, WithdrawalTransaction> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @Profile("prod")
    public ConsumerFactory<String, WithdrawResult> cloudConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonDeserializer.class.getName());

        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        configProps.put("security.protocol", "SASL_SSL");
        configProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(),
            new JacksonDeserializer<>(objectMapper(), WithdrawResult.class));
    }

    @Bean
    @Profile("dev")
    public ConsumerFactory<String, WithdrawResult> localConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonDeserializer.class.getName());

        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(),
            new JacksonDeserializer<>(objectMapper(), WithdrawResult.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WithdrawResult> kafkaListenerContainerFactory(
        ConsumerFactory<String, WithdrawResult> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, WithdrawResult> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(3);
        return factory;
    }
}