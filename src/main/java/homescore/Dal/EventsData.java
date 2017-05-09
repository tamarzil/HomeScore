package homescore.Dal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tamar on 5/1/2017.
 */
public class EventsData {
    // private List<String> names;
    private List<HomeEvent> events;

    public EventsData() {
        this.events = new ArrayList();
    }

    @JsonProperty
    public List<HomeEvent> getEvents() {
        return events;
    }

    public void setEvents(List<HomeEvent> events) {
        this.events = events;
    }
}
