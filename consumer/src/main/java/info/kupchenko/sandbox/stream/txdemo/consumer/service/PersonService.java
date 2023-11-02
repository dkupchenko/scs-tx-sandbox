package info.kupchenko.sandbox.stream.txdemo.consumer.service;

import info.kupchenko.sandbox.stream.txdemo.consumer.model.Person;
import info.kupchenko.sandbox.stream.txdemo.consumer.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository persons;

    public Person get(long id) {
        return persons.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public Person save(Person person) {
        return persons.save(person);
    }

}
