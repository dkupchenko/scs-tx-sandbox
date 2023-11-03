package info.kupchenko.sandbox.stream.txdemo.function.service;

import info.kupchenko.sandbox.stream.txdemo.function.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingConsumer {

    public void consume(Person person) {
        log.info("[Received] {}", person);
    }
}
