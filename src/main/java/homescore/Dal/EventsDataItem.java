package homescore.Dal;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

/**
 * Created by Tamar on 5/1/2017.
 */

@DynamoDBTable(tableName = "HomeScoreData")
public class EventsDataItem {

    private String customerId;
    private EventsData data;

    public EventsDataItem() {}

    public EventsDataItem(String customerId, EventsData data) {
        this.customerId = customerId;
        this.data = data;
    }

    @DynamoDBHashKey(attributeName="CustomerId")
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @DynamoDBAttribute(attributeName="Data")
    @DynamoDBTypeConverted(converter = EventsDataTypeConverter.class)
    public EventsData getData() {
        return data;
    }

    public void setData(EventsData data) {
        this.data = data;
    }
}
