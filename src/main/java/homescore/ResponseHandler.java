package homescore;

import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import homescore.Dal.HomeEvent;
import simplenlg.features.Tense;

/**
 * Created by Tamar on 5/5/2017.
 */
public class ResponseHandler {

    private static final String SKILL_NAME = "HomeScore";
    private static final String extraText = "\nHere are some more things you can ask me:\n"
            + "Who was the last one to walk the dog?\n"
            + "How many times did Bob clean the bathroom this year?\n"
            + "How many times did we wash the dishes this week?\n"
            + "Note that David took out the trash.\n";

    private TenseConverter tenseConverter;

    public ResponseHandler(TenseConverter tenseConverter) {
        this.tenseConverter = tenseConverter;
    }

    public SpeechletResponse getNewEventResponse(HomeEvent newEvent) {
        if (newEvent == null)
            return getSaveEventFailedResponse();
        String cardContent = String.format("Noted that %s %s", newEvent.getName(), newEvent.getAction());
        return getSimpleTellResponse("Got it", SKILL_NAME, cardContent);
    }

    public SpeechletResponse getLastOneResponse(HomeEvent lastEvent, String action) {
        String speechText;
        if (lastEvent == null) {
            speechText = String.format("According to my records, no one ever %s", action);
        } else {
            String timePhrase = DateTimeHelper.getTimePhrase(lastEvent.getDatetime());
            if (tenseConverter != null)
                speechText = String.format("%s was the last one to %s, %s", lastEvent.getName(), tenseConverter.convertToBaseForm(lastEvent.getAction()), timePhrase);
            else
                speechText = String.format("%s was the last one who %s, %s", lastEvent.getName(), lastEvent.getAction(), timePhrase);
        }

        return getSimpleTellResponse(speechText, SKILL_NAME, speechText);
    }

    public SpeechletResponse getLastTimeResponse(HomeEvent lastEvent, String action, String name) {
        String speechText;
        if (lastEvent == null) {
            speechText = String.format("According to my records, %s never %s", name, action);
        } else {
            String timePhrase = DateTimeHelper.getTimePhrase(lastEvent.getDatetime());
            speechText = String.format("The last time %s %s was %s", lastEvent.getName(), lastEvent.getAction(), timePhrase);
        }

        return getSimpleTellResponse(speechText, SKILL_NAME, speechText);
    }

    public SpeechletResponse getHowManyTimesResponse(int times, String action, String name, DateRange range) {
        String speechText;
        String rangeStr = (range == null ? "so far" : range.getWords());    // shouldn't happen, but hey...
        if (times == 0) {
            if (tenseConverter != null)
                speechText = String.format("According to my records, %s didn't %s %s", name, tenseConverter.convertToBaseForm(action), rangeStr);
            else
                speechText = String.format("According to my records, %s didn't %s %s", name, action, rangeStr);

        } else {
            speechText = String.format("%s %s %d %s %s", name, action, times, times == 1 ? "time" : "times", rangeStr);
        }

        if (range != null && range.isParseError())
            return getInvalidDateRangeResponse();

        return getSimpleTellResponse(speechText.replace("\n", ""), SKILL_NAME, speechText);
    }

    public SpeechletResponse getHowManyTimesGeneralResponse(int times, String action, DateRange range) {
        String speechText;
        String rangeStr = (range == null ? "so far" : range.getWords());    // shouldn't happen, but hey...
        if (times == 0)
            speechText = String.format("According to my records, no one %s %s", action, rangeStr);
        else {
            speechText = String.format("you %s %d %s %s", action, times, times == 1 ? "time" : "times", rangeStr);
        }

        if (range != null && range.isParseError())
            return getInvalidDateRangeResponse();

        return getSimpleTellResponse(speechText, SKILL_NAME, speechText);
    }

    public SpeechletResponse getMissingDataResponse() {
        String speech = "I'm sorry, I didn't get that. Please repeat your request clearly, using phrases similar to the ones in HomeScore's skill page";
        String reprompt = "Please repeat your request clearly";
        return getTempErrorResponse(speech, reprompt);
    }

    private SpeechletResponse getInvalidDateRangeResponse() {
        String speech = "The time frame you asked for was invalid. This kind of question only works for today, yesterday, this week, this month or this year. Please ask again using one of these time frames, or without a time frame to get the result since the beginning of time";
        String reprompt = "Please ask again using one of these time frames: today, yesterday, this week, this month or this year. You can also ask without adding a time frame to get the results since the beginning of time";
        return getTempErrorResponse(speech, reprompt);
    }

    public SpeechletResponse getBadNameResponse() {
        String speech = "Please say it again using your name, not I or we";
        String reprompt = "Do you still want me to remember something you did?";
        return getTempErrorResponse(speech, reprompt);
    }

    private SpeechletResponse getSaveEventFailedResponse() {
        String speech = "I'm sorry, something went wrong. Could you please repeat that?";
        String reprompt = "Do you still want me to remember something you did? Please say something like: remember that Lisa washed the dishes";
        return getTempErrorResponse(speech, reprompt);
    }

    public SpeechletResponse getRequestProcessingError() {
        String speech = "I'm sorry, something went wrong. Could you please repeat that?";
        String reprompt = "Please repeat your request";
        return getTempErrorResponse(speech, reprompt);
    }

    private SpeechletResponse getTempErrorResponse(String speechText, String repromptText) {
        return getSimpleAskResponse(speechText, String.format("%s - Error", SKILL_NAME), speechText, repromptText);
    }

    public SpeechletResponse getSimpleTellResponse(String speechText, String cardTitle, String cardContent) {

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle(cardTitle);
        card.setContent(cardContent);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

//        SsmlOutputSpeech speech = new SsmlOutputSpeech();
//        speech.setSsml("<speak>This output speech uses SSML.</speak>");

        return SpeechletResponse.newTellResponse(speech, card);
    }

    public SpeechletResponse getSimpleAskResponse(String speechText, String cardTitle, String cardContent, String repromptText) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle(cardTitle);
        card.setContent(cardContent);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create re-prompt
        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText(repromptText);
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    public SpeechletResponse getWelcomeResponse() {
        String speechText = "This is HomeScore. No more arguing about who does more around the house. Now you can tell me to remember every task you did, and later ask me who was the last to do something.";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle(SKILL_NAME);
        card.setContent(speechText + extraText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create re-prompt
        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText("You can ask me to remember that you did something, or ask me who was the last one to do something.");
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    public SpeechletResponse getHelpResponse() {
        String speechText = "Home Score is very simple to use. You can start by telling me to remember that you did something. Later, ask me who was the last one to do that thing.";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle(SKILL_NAME);
        card.setContent(speechText + extraText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create re-prompt
        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText("You can ask me to remember that you did something, or ask me who was the last one to do something.");
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    public SpeechletResponse getExitResponse() {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText("Goodbye");
        return SpeechletResponse.newTellResponse(speech);
    }

    public SpeechletResponse getNewEventConfirmResponse(ParsedRequest request) {
        String speechText = String.format("I heard: %s %s. Is that correct?", request.getName(), request.getAction());

        // Create the Simple card content
        SimpleCard card = new SimpleCard();
        card.setTitle(SKILL_NAME);
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create re-prompt
        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText("I heard: %s %s. Is that correct? say yes or no.");
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    public SpeechletResponse getNewEventIncorrectResponse() {
        String speechText = "Oops, my bad. Can you say it again please?";
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText("Do you still want me to remember something you did?");
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);

        return SpeechletResponse.newAskResponse(speech, reprompt);
    }

    public SpeechletResponse getIrrelevantResponse(String phrase) {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(String.format("%s is irrelevant here.", phrase));
        return SpeechletResponse.newTellResponse(speech);
    }
}