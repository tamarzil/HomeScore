
package homescore;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

public final class HomeScoreSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {

    private static final Set<String> supportedApplicationIds = new HashSet<String>();
    static {
       supportedApplicationIds.add("amzn1.ask.skill.ae037b22-1caa-4cd2-8834-c822ef9facb4"); // get from configuration
    }

    public HomeScoreSpeechletRequestStreamHandler() {
        super(new HomeScoreSpeechlet(), supportedApplicationIds);
    }
}
