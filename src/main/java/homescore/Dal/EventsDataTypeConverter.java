package homescore.Dal;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import java.io.IOException;

/**
 * Created by Tamar on 5/2/2017.
 */
public class EventsDataTypeConverter implements DynamoDBTypeConverter<String, EventsData> {

    ObjectMapper mapper;

    public EventsDataTypeConverter() {
        super();
        mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
    }

    @Override
    public String convert(EventsData object) {
        String json = null;
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            // TODO: log + handle error
        }
        return json;
    }

    @Override
    public EventsData unconvert(String s) {
        EventsData data = null;
        try {
            data = mapper.readValue(s, EventsData.class);
        } catch (IOException e) {
            // TODO: log + handle error
        }

        return data;
    }
}

