package info.kupchenko.sandbox.stream.txdemo.function.config;

import info.kupchenko.sandbox.stream.txdemo.function.model.Person;
import info.kupchenko.sandbox.stream.txdemo.function.service.LoggingConsumer;
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
