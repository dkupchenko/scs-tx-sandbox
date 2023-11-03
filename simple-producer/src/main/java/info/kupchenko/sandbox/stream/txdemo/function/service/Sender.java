package info.kupchenko.sandbox.stream.txdemo.function.service;

import info.kupchenko.sandbox.stream.txdemo.function.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Sender {

    private static final String BINDING_NAME = "sendPerson-out-0";

    private final StreamBridge streamBridge;

    public void send(Person person) {
        streamBridge.send(BINDING_NAME, person);
    }
}
