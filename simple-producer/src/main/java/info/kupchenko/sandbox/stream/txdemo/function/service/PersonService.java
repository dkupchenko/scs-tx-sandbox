package info.kupchenko.sandbox.stream.txdemo.function.service;

import info.kupchenko.sandbox.stream.txdemo.function.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    private final Sender sender;

    @Scheduled(fixedDelayString = "PT10S", initialDelay = 1000)
    public void sendNew() {
        log.info("[BEGIN]");
        sender.send(new Person(null, "John Smith"));
        log.info("[END]");
    }

}
