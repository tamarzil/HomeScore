package homescore;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;

import homescore.Exception.HomeScoreMissingActionException;
import homescore.Exception.HomeScoreMissingInitialData;
import org.slf4j.Logger;

public class ParsedRequest {

    private static final String SLOT_NAME = "Name";
    private static final String SLOT_ACTION = "Action";
    private static final String SLOT_RANGE = "Range";

    private String intentName;
    private String customerId;

    private String name;
    private String action;
    private DateRange range;

    public ParsedRequest(IntentRequest request, Session session, Logger log)
            throws HomeScoreMissingInitialData, HomeScoreMissingActionException {
        log.info("creating ParsedRequest...");

        Intent intent;
        try {
            this.customerId = session.getUser().getUserId();
            intent = request.getIntent();
            this.intentName = (intent != null) ? intent.getName() : null;
        } catch (Exception ex) {
            throw new HomeScoreMissingInitialData("session or intent name missing");
        }

        if (this.intentName == null || this.getCustomerId()== null) {
            throw new HomeScoreMissingInitialData("session or intent name missing");
        }

        if (this.intentName.equalsIgnoreCase("AMAZON.HelpIntent")
                || this.intentName.equals("AMAZON.StopIntent")
                || this.intentName.equals("AMAZON.CancelIntent"))
            return;

        this.action = intent.getSlot(SLOT_ACTION).getValue();
        if (this.action == null )
            throw new HomeScoreMissingActionException("action missing");

        switch (this.intentName) {
            case "SaveEventIntent":
            case "LastTimeIntent":
                this.name = intent.getSlot(SLOT_NAME).getValue();
                break;
            case "HowManyTimesIntent":
                this.name = intent.getSlot(SLOT_NAME).getValue();
                this.range = DateTimeHelper.parseDateRange(intent.getSlot(SLOT_RANGE).getValue());
                break;
            case "HowManyTimesGeneralIntent":
                this.range = DateTimeHelper.parseDateRange(intent.getSlot(SLOT_RANGE).getValue());
                break;
        }

        log.info(String.format("finished creating the parsed request. Intent: %s, Name: %s, Action: %s", this.intentName, this.name, this.action));
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getIntentName() {
        return intentName;
    }

    public String getName() {
        return name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public DateRange getRange() {
        return range;
    }
}
