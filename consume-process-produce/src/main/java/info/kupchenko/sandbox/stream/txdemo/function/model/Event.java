package info.kupchenko.sandbox.stream.txdemo.function.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    long personId;

    String status;

    public Event(Person person) {
        this.personId = person.getId();
        this.status = "Processed";
    }
}
