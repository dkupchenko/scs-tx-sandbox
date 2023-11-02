package info.kupchenko.sandbox.stream.txdemo.consumer.controller;

import info.kupchenko.sandbox.stream.txdemo.consumer.model.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PersonController {

    @GetMapping("person/{id}")
    public Person get(@PathVariable long id) {
        return null;
    }

}
