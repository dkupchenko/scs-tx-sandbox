package info.kupchenko.sandbox.stream.txdemo.consumer.repository;

import info.kupchenko.sandbox.stream.txdemo.consumer.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
