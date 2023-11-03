package info.kupchenko.sandbox.stream.txdemo.producer.service;

import info.kupchenko.sandbox.stream.txdemo.producer.model.Person;
import info.kupchenko.sandbox.stream.txdemo.producer.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    private static final String PERSON_NAME = "John Smith";

    private final PersonRepository persons;
    private final Sender sender;

    private final AtomicLong counter = new AtomicLong(0);

    @Transactional
    @Scheduled(fixedDelayString = "PT10S", initialDelay = 1000L)
    public void createAndSend() {
        log.info("[BEGIN]");
        var person = persons.findAll().stream()
                .filter(p -> PERSON_NAME.equals(p.getName()))
                .findFirst()
                .orElse(new Person());
        person = persons.save(update(person));
        sender.send(person);
        log.info("[END]");
        if(counter.get() % 2 == 0) {
            log.warn("[EXCEPTION] Some error is happen");
            throw new RuntimeException();
        }
    }

    private Person update(Person person) {
        person.setName(PERSON_NAME + " - " + counter.getAndIncrement());
        return person;
    }
}
