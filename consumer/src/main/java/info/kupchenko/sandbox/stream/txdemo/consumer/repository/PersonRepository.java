package info.kupchenko.sandbox.stream.txdemo.consumer.repository;

import info.kupchenko.sandbox.stream.txdemo.consumer.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
