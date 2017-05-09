
package homescore;

import homescore.Dal.HomeEvent;

import homescore.Exception.HomeScoreMissingActionException;
import homescore.Exception.HomeScoreMissingInitialData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;

public class HomeScoreSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(HomeScoreSpeechlet.class);

    private HomeScoreManager mgr;
    private ResponseHandler responseHandler;
    private TenseConverter tenseConverter;

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
        initializeComponents();

        return responseHandler.getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

        initializeComponents();

        ParsedRequest parsed = null;
        try {
            parsed = new ParsedRequest(request, session, log);
        } catch (HomeScoreMissingInitialData | HomeScoreMissingActionException idex) {
            log.error(idex.getMessage());
            return responseHandler.getMissingDataResponse();
        }

        if (parsed.getIntentName().equals("AMAZON.HelpIntent"))
            return responseHandler.getHelpResponse();
        if (parsed.getIntentName().equals("AMAZON.StopIntent") || parsed.getIntentName().equals("AMAZON.CancelIntent"))
            return responseHandler.getExitResponse();

        try {
            if (tenseConverter != null)
                parsed.setAction(tenseConverter.convertToPastTense(parsed.getAction()));
        } catch (IllegalArgumentException ex) {
            log.warn("The action text does not contain a verb: %s", parsed.getAction());
        }

        try {
            HomeEvent event;
            Integer times;
            switch (parsed.getIntentName()) {
                case "SaveEventIntent":
                    if (parsed.getName().equalsIgnoreCase("I") || parsed.getName().equalsIgnoreCase("we"))
                        return responseHandler.getBadNameResponse();
                    event = mgr.handleSaveEvent(parsed.getCustomerId(), parsed.getName(), parsed.getAction());
                    return responseHandler.getNewEventResponse(event);
                case "LastOneIntent":
                    event = mgr.handleLastEventQuery(parsed.getCustomerId(), parsed.getAction());
                    return responseHandler.getLastOneResponse(event, parsed.getAction());
                case "LastTimeIntent":
                    event = mgr.handleLastEventQuery(parsed.getCustomerId(), parsed.getAction(), parsed.getName());
                    return responseHandler.getLastTimeResponse(event, parsed.getAction(), parsed.getName());
                case "LastTimeGeneralIntent":
                    event = mgr.handleLastEventQuery(parsed.getCustomerId(), parsed.getAction());
                    return responseHandler.getLastOneResponse(event, parsed.getAction());
                case "HowManyTimesIntent":
                    times = mgr.handleHowManyTimesQuery(parsed.getCustomerId(), parsed.getAction(), parsed.getName(), parsed.getRange());
                    return responseHandler.getHowManyTimesResponse(times, parsed.getAction(), parsed.getName(), parsed.getRange());
                case "HowManyTimesGeneralIntent":
                    times = mgr.handleHowManyTimesQuery(parsed.getCustomerId(), parsed.getAction(), parsed.getRange());
                    return responseHandler.getHowManyTimesGeneralResponse(times, parsed.getAction(), parsed.getRange());
                default:
                    throw new SpeechletException("Invalid Intent");
            }
        } catch (Exception ex) {
            return responseHandler.getRequestProcessingError();
        }
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }


    private void initializeComponents() {
        if (mgr == null) {
            mgr = new HomeScoreManager();
            try {
                tenseConverter = new TenseConverter();
            } catch (Exception e) {
                log.error("Failed to create TenseConverter. Error: %s", e.getMessage());
                tenseConverter = null;
            }
            responseHandler = new ResponseHandler(tenseConverter);
        }
    }
}

