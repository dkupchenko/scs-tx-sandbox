package info.kupchenko.sandbox.stream.txdemo.producer.service;

import info.kupchenko.sandbox.stream.txdemo.producer.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    private final Sender sender;

    @Transactional
    @Scheduled(fixedDelayString = "PT10S", initialDelay = 1000L)
    public void createAndSend() {
        log.info("[BEGIN]");
        sender.send(new Person(null, "John Smith"));
        sender.send(new Person(null, "Mary Smith"));
        sender.send(new Person(null, "Daniel Jones"));
        sender.send(new Person(null, "Lisa Jones"));
        log.info("[END]");
    }

}
