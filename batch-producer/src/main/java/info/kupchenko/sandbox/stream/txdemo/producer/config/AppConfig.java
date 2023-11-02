package info.kupchenko.sandbox.stream.txdemo.producer.config;

import org.springframework.cloud.stream.binder.DefaultBinderFactory;
import org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.messaging.MessageChannel;

@Configuration
@SuppressWarnings("rawtypes")
public class AppConfig {

    @Bean
    KafkaTransactionManager customKafkaTransactionManager(DefaultBinderFactory binderFactory) {
        KafkaMessageChannelBinder kafka = (KafkaMessageChannelBinder)binderFactory.getBinder("kafka", MessageChannel.class);
        ProducerFactory<byte[], byte[]> transactionalProducerFactory = kafka.getTransactionalProducerFactory();
        KafkaTransactionManager kafkaTransactionManager = new KafkaTransactionManager(transactionalProducerFactory);
        return kafkaTransactionManager;
    }
}
