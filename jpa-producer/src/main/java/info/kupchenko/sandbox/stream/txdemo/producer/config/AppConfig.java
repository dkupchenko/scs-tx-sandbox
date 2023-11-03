package info.kupchenko.sandbox.stream.txdemo.producer.config;

import info.kupchenko.sandbox.stream.txdemo.producer.model.Person;
import info.kupchenko.sandbox.stream.txdemo.producer.service.LoggingConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class AppConfig {

    @Bean
    Consumer<Person> ackSendPerson(LoggingConsumer logger) {
        return logger::consume;
    }
}
