package info.kupchenko.sandbox.stream.txdemo.function.service;

import info.kupchenko.sandbox.stream.txdemo.function.model.Person;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@AllArgsConstructor
public class PersonGenerator {

    private static final String PERSON_NAME = "John Smith";
    private static final AtomicLong INSTANCE_SEQ = new AtomicLong(0);

    private final StreamBridgeSender sender;


    @Scheduled(fixedDelayString = "PT20S", initialDelay = 1000L)
    public void createAndSend() {
        var person = new Person(null, INSTANCE_SEQ.getAndIncrement(), PERSON_NAME);
        sender.send(person);
        log.info("[SENT] {}", person);
    }

}
