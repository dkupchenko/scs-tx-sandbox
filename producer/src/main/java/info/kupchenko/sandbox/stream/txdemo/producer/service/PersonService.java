package info.kupchenko.sandbox.stream.txdemo.producer.service;

import info.kupchenko.sandbox.stream.txdemo.producer.model.Person;
import info.kupchenko.sandbox.stream.txdemo.producer.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository persons;
    private final Sender sender;

    @Scheduled(fixedDelayString = "PT10S", initialDelay = 1000)
    public void sendNew() {
        log.info("[BEGIN]");
        sender.send(new Person(null, "John Smith"));
        log.info("[END]");
    }

    public Person get(long id) {
        return persons.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public Person save(Person person) {
        return persons.save(person);
    }

}
