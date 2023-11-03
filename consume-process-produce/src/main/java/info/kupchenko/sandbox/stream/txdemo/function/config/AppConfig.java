package info.kupchenko.sandbox.stream.txdemo.function.config;

import info.kupchenko.sandbox.stream.txdemo.function.model.Event;
import info.kupchenko.sandbox.stream.txdemo.function.model.Person;
import info.kupchenko.sandbox.stream.txdemo.function.service.LoggingConsumer;
import info.kupchenko.sandbox.stream.txdemo.function.service.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class AppConfig {

    @Bean
    Consumer<Person> processPerson(PersonService persons) {
        return persons::process;
        //return person -> persons.process(person);
    }

    @Bean
    Consumer<Event> ackSendEvent(LoggingConsumer logger) {
        return logger::consume;
    }
}
