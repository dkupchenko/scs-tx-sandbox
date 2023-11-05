package info.kupchenko.sandbox.stream.txdemo.function.service;

import info.kupchenko.sandbox.stream.txdemo.function.model.Event;
import info.kupchenko.sandbox.stream.txdemo.function.model.Person;
import info.kupchenko.sandbox.stream.txdemo.function.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PersonService {

    private static final int MAX_REQUEST_SIZE = 1024;

    private final PersonRepository persons;
    private final StreamBridgeSender sender;

    private final IterationResolver resolver = new IterationResolver();

    public PersonService(final PersonRepository persons,
                         final StreamBridgeSender sender) {
        this.persons = persons;
        this.sender = sender;
    }

    @Transactional
    public void process(Person person) {
        log.info("[BEGIN] Incoming {}", person);
        dump();
        person = persons.save(person);
        switch (resolver.resolve(person)) {
            case SUCCESS -> {
                sender.send(Event.success(person));
                log.info("[END] Saved {}", person);
            }
            case INTERNAL_EXCEPTION -> {
                sender.send(Event.success(person));
                log.warn("[EXCEPTION] Some error is happen");
                throw new RuntimeException();
            }
            case KAFKA_ERROR -> {
                log.warn("[ERROR] Some kafka error happen");
                sender.send(Event.error(person, MAX_REQUEST_SIZE));
            }
        }
    }

    private void dump() {
        var items = persons.findAll();
        log.info("[DUMP] db size={}", items.size());
        for (var item : items) {
            log.info("[DUMP] {}", item);
        }
    }

    enum IterationType {
        SUCCESS, INTERNAL_EXCEPTION, KAFKA_ERROR;
    }

    static class IterationResolver {

        public IterationType resolve(Person person) {
            var typesCount = IterationType.values().length;
            int rest = (int) (person.getInstanceId() % typesCount);
            return IterationType.values()[rest];
        }
    }
}
