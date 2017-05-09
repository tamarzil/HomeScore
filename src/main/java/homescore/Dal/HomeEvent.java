package homescore.Dal;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.time.LocalDateTime;

/**
 * Created by Tamar on 5/1/2017.
 */
public class HomeEvent {
    private String name;
    private String action;
    private DateTime datetime;

    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @JsonProperty
    public DateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(DateTime datetime) {
        this.datetime = datetime;
    }
}
