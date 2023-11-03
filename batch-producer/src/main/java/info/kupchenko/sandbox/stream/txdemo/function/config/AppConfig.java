package info.kupchenko.sandbox.stream.txdemo.function.config;

import info.kupchenko.sandbox.stream.txdemo.function.model.Person;
import info.kupchenko.sandbox.stream.txdemo.function.service.LoggingConsumer;
import org.springframework.cloud.stream.binder.DefaultBinderFactory;
import org.springframework.cloud.stream.binder.kafka.KafkaMessageChannelBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.messaging.MessageChannel;

import java.util.function.Consumer;

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

    @Bean
    Consumer<Person> ackSendPerson(LoggingConsumer logger) {
        return logger::consume;
    }
}
