package homescore;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Created by Tamar on 5/6/2017.
 */
public class TenseConverter {
    private static final String POS_TAG_VERB_PAST = "VBD";
    private static final String POS_TAG_VERB_BASE = "VB";

    private static final Logger log = LoggerFactory.getLogger(TenseConverter.class);

    private Tokenizer tokenizer;
    private POSTaggerME posTagger;
    private Realiser realiser;
    private NLGFactory nlgFactory;

    public TenseConverter() throws Exception {

        InputStream tokenizerIn = null;
        InputStream posTaggerIn = null;
        try {
            tokenizerIn = TenseConverter.class.getClassLoader().getResourceAsStream("en-token.bin");
            posTaggerIn = TenseConverter.class.getClassLoader().getResourceAsStream("en-pos-maxent.bin");

            TokenizerModel tokenizerModel = new TokenizerModel(tokenizerIn);
            tokenizer = new TokenizerME(tokenizerModel);
            POSModel posModel = new POSModel(posTaggerIn);
            posTagger = new POSTaggerME(posModel);

            Lexicon lexicon = Lexicon.getDefaultLexicon();
            nlgFactory = new NLGFactory(lexicon);
            realiser = new Realiser(lexicon);
        } catch(IOException ioex) {
            String msg = String.format("Failed to load models for NLG components. %s", ioex.getMessage());
            log.error(msg);
            throw new IOException(msg);
        } catch(Exception ex) {
            String msg = String.format("Failed to create NLG components. %s", ex.getMessage());
            log.error(msg);
            throw new Exception(msg);
        } finally {
            if (tokenizerIn != null)
                tokenizerIn.close();
            if (posTaggerIn != null)
                posTaggerIn.close();
        }
    }

    public String convertToBaseForm(String sentence) {
        return convertToTense(sentence, POS_TAG_VERB_BASE, this::convertVerbToBase);
    }

    public String convertToPastTense(String sentence) {
        return convertToTense(sentence, POS_TAG_VERB_PAST, this::convertVerbToPast);
    }

    public String convertToTense(String sentence, String targetTensePosTag, Function<String, String> verbConverter) {

        VerbContext verbContext = getVerbContext(sentence);

        // in sentences without subject sometimes the verb is mistaken for a noun - add a subject and try again
        if (verbContext == null)
            verbContext = getVerbContext(String.format("he %s", sentence));

        if (verbContext == null)
            throw new IllegalArgumentException("sentence does not contain a verb");

        // check if already target tense. if so - return action unchanged
        if (verbContext.getTag().equals(targetTensePosTag))
            return sentence;

        // convert action sentence to target tense
        String originalVerb = verbContext.getTokens()[verbContext.getIndex()];
        String convertedVerb = verbConverter.apply(originalVerb);

        return sentence.replace(originalVerb, convertedVerb);
    }

    private VerbContext getVerbContext(String sentence) {
        // tokenize sentence
        String[] tokens = tokenizer.tokenize(sentence);

        // get pos tags
        String[] tags = posTagger.tag(tokens);

        // find verb
        int n = tags.length;
        int i = 0;
        for (i = 0; i < n; i++) {
            if (Pattern.matches("^VBD*", tags[i]))
                break;
        }

        return (i < n) ? new VerbContext(tokens, i, tags[i]) : null;
}

    private String convertVerbToPast(String baseVerb) {
        return convertVerb(baseVerb, Tense.PAST);
    }

    private String convertVerbToBase(String baseVerb) {
        return convertForm(baseVerb, Form.BARE_INFINITIVE);
    }

    private String convertVerb(String verb, Tense targetTense) {
        SPhraseSpec p = nlgFactory.createClause();
        p.setVerb(verb);
        p.setFeature(Feature.TENSE, targetTense);
        String pastVerb = realiser.realiseSentence(p);
        pastVerb = pastVerb.replace(".", "");
        return pastVerb.toLowerCase();
    }

    private String convertForm(String verb, Form targetForm) {
        SPhraseSpec p = nlgFactory.createClause();
        p.setVerb(verb);
        p.setFeature(Feature.FORM, targetForm);
        String pastVerb = realiser.realiseSentence(p);
        pastVerb = pastVerb.replace(".", "");
        return pastVerb.toLowerCase();
    }


private class VerbContext {
    private String[] tokens;
    private int index;
    private String tag;

    public VerbContext(String[] tokens, int index, String tag) {
        this.tokens = tokens;
        this.index = index;
        this.tag = tag;
    }

    public String[] getTokens() {
        return tokens;
    }

    public int getIndex() {
        return index;
    }

    public String getTag() {
        return tag;
    }
}
}
