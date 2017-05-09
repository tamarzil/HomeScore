package homescore.Dal;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

/**
 * Created by Tamar on 5/2/2017.
 */
public class HomeScoreDal {

    private DynamoDBMapper mapper;

    public HomeScoreDal() {
        AmazonDynamoDB dbClient = AmazonDynamoDBClientBuilder.defaultClient();
        this.mapper = new DynamoDBMapper(dbClient);
    }

    public boolean saveEventsData(EventsDataItem item) {
        mapper.save(item);
        return true;
    }

    public EventsDataItem loadEventsData(String customerId) {
        return mapper.load(EventsDataItem.class, customerId);
    }
}
