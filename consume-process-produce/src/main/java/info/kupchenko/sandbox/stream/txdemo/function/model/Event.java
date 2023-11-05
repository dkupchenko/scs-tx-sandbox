package info.kupchenko.sandbox.stream.txdemo.function.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    long personId;
    String info;

    public static Event success(Person person) {
        return new Event(person.getId(), person.toString());
    }

    public static Event error(Person person, int maxRequestSize) {
        var pattern = person.toString() + ",";
        String tooLongData = "Too long data: " + pattern.repeat(maxRequestSize / pattern.length() + 1);
        return new Event(person.getId(), tooLongData);
    }
}
