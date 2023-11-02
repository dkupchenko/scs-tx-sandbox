package info.kupchenko.sandbox.stream.txdemo.producer.repository;

import info.kupchenko.sandbox.stream.txdemo.producer.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
