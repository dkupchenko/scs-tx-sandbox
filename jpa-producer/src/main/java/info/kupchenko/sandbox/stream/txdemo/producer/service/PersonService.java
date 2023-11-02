package info.kupchenko.sandbox.stream.txdemo.producer.service;

import info.kupchenko.sandbox.stream.txdemo.producer.model.Person;
import info.kupchenko.sandbox.stream.txdemo.producer.model.PersonStatus;
import info.kupchenko.sandbox.stream.txdemo.producer.repository.PersonRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NamingEnumeration;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository persons;
    private final Sender sender;

    @PostConstruct
    @Transactional
    void init() {
        persons.save(new Person(null, "John Smith", PersonStatus.NEW));
        persons.save(new Person(null, "Mary Smith", PersonStatus.NEW));
        persons.save(new Person(null, "Daniel Jones", PersonStatus.NEW));
        persons.save(new Person(null, "Lisa Jones", PersonStatus.NEW));
    }

    @Scheduled(fixedDelayString = "PT10S", initialDelay = 1000L)
    @Transactional
    public void updateNew() {
        persons.findAll().forEach(person -> {
            if (person.getStatus().equals(PersonStatus.NEW)) {
                person.setStatus(PersonStatus.UPDATED);
                sender.send(persons.save(person));
            }
        });
    }

}
