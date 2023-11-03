package info.kupchenko.sandbox.stream.txdemo.function.repository;

import info.kupchenko.sandbox.stream.txdemo.function.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
