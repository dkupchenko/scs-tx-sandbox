package info.kupchenko.sandbox.stream.txdemo.function.service;

import info.kupchenko.sandbox.stream.txdemo.function.model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PersonServiceTest {


    @Test
    void resolver() {
        var resolver = new PersonService.IterationResolver();

        for (long i = 0; i < 100; i = i + 3) {
            Assertions.assertEquals(PersonService.IterationType.SUCCESS, resolver.resolve(new Person(null, i, "name")));
        }
        for (long i = 1; i < 100; i = i + 3) {
            Assertions.assertEquals(PersonService.IterationType.INTERNAL_EXCEPTION, resolver.resolve(new Person(null, i, "name")));
        }
        for (long i = 2; i < 100; i = i + 3) {
            Assertions.assertEquals(PersonService.IterationType.KAFKA_ERROR, resolver.resolve(new Person(null, i, "name")));
        }
    }
}