package homescore;

import homescore.Dal.EventsData;
import homescore.Dal.EventsDataItem;
import homescore.Dal.HomeEvent;
import homescore.Dal.HomeScoreDal;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Tamar on 5/2/2017.
 */
public class HomeScoreManager {

    private static final Logger log = LoggerFactory.getLogger(HomeScoreSpeechlet.class);

    private HomeScoreDal dal;

    public HomeScoreManager() {
        dal = new HomeScoreDal();
    }

    public HomeEvent handleSaveEvent(String customerId, String name, String action) {

        HomeEvent newEvent = new HomeEvent();
        newEvent.setName(name);
        newEvent.setAction(action);
        newEvent.setDatetime(DateTime.now());

        boolean result;
        try {
            EventsDataItem dataItem = dal.loadEventsData(customerId);
            if (dataItem == null) {
                dataItem = new EventsDataItem(customerId, new EventsData());
            }

            dataItem.getData().getEvents().add(newEvent);
            result = dal.saveEventsData(dataItem);

        } catch (Exception ex) {
            log.error("failed to save new event for customer ID: %s, name: %s, action: %s", customerId, name, action);
            return null;
        }

        if (!result) {
            log.error("Failed to save new event for customer ID: %s, name: %s, action: %s", customerId, name, action);
            return null;
        }

        return newEvent;
    }

    public HomeEvent handleLastEventQuery(String customerId, String action) {
        return handleLastEventQuery(customerId, action, null);
    }

    public HomeEvent handleLastEventQuery(String customerId, String action, String name) {
        List<HomeEvent> list = getEventsOfAction(customerId, action);
        if (list == null)
            return null;

        list = list.stream().filter(e -> name == null || e.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        list.sort((e1, e2) -> e2.getDatetime().compareTo(e1.getDatetime()));
        return list.isEmpty() ? null : list.get(0);
    }

    public int handleHowManyTimesQuery(String customerId, String action, String name, DateRange range) {
        List<HomeEvent> list = getEventsOfAction(customerId, action);
        if (list == null)
            return 0;
        if (name == null && range == null)
            return list.size();

        list = list.stream()
                .filter(e -> (range == null || range.getInterval().contains(e.getDatetime())) &&
                        (name == null || e.getName().equalsIgnoreCase(name)))
                .collect(Collectors.toList());

        return list.size();
    }

    public int handleHowManyTimesQuery(String customerId, String action, DateRange range) {
        return handleHowManyTimesQuery(customerId, action, null, range);
    }

    private List<HomeEvent> getEventsOfAction(String customerId, String action) {
        EventsDataItem dataItem = dal.loadEventsData(customerId);

        if (dataItem == null || dataItem.getData() == null || dataItem.getData().getEvents().isEmpty())
            return null;

        List<HomeEvent> list = dataItem.getData().getEvents().stream()
                .filter(e -> e.getAction().equalsIgnoreCase(action))
                .collect(Collectors.toList());

        if (list.isEmpty())
            return null;

        return list;
    }
}
