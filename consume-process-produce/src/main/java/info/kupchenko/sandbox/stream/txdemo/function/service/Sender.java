package info.kupchenko.sandbox.stream.txdemo.function.service;

import info.kupchenko.sandbox.stream.txdemo.function.model.Event;
import info.kupchenko.sandbox.stream.txdemo.function.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Sender {

    private static final String PERSON_BINDING_NAME = "sendPerson-out-0";
    private static final String EVENT_BINDING_NAME = "sendEvent-out-0";

    private final StreamBridge streamBridge;

    public void send(Person person) {
        streamBridge.send(PERSON_BINDING_NAME, person);
    }

    public void send(Event event) {
        streamBridge.send(EVENT_BINDING_NAME, event);
    }
}
