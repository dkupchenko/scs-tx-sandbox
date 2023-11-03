package info.kupchenko.sandbox.stream.txdemo.function.service;

import info.kupchenko.sandbox.stream.txdemo.function.model.Event;
import info.kupchenko.sandbox.stream.txdemo.function.model.Person;
import info.kupchenko.sandbox.stream.txdemo.function.repository.PersonRepository;
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

    @Scheduled(fixedDelayString = "PT10S", initialDelay = 1000L)
    public void createAndSend() {
        var person = update(new Person());
        sender.send(person);
        log.info("[SENT] {}", person);
    }

    @Transactional
    public void process(Person person) {
        log.info("[BEGIN] {}", person);
        person = persons.save(person);
        sender.send(new Event(person));
        log.info("[END] {}", person);
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
