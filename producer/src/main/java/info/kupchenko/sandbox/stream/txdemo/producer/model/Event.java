package info.kupchenko.sandbox.stream.txdemo.producer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "EVENT")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENT_SEQ")
    @Column(nullable = false, updatable = false)
    Long id;

    @Column(name = "person_id", nullable = false, updatable = false)
    long personId;

    @Column(nullable = false)
    EventStatus status;
}
