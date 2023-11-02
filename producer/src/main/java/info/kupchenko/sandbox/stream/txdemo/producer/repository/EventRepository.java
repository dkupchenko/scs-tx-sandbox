package info.kupchenko.sandbox.stream.txdemo.producer.repository;

import info.kupchenko.sandbox.stream.txdemo.producer.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
