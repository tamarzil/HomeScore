package homescore;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Tamar on 5/6/2017.
 */
public class TenseConverterTest {

    private static TenseConverter converter;

    @BeforeClass
    public static void runOnceBeforeClass() throws Exception {
        converter = new TenseConverter();
    }

    @Test
    public void convertToPastTense_pastSentenceRegularVerb1_returnsPastSentence() throws IOException {
        String input = "cleaned the bathroom";
        String result = converter.convertToPastTense(input);
        assertNotNull(result);
        assertEquals(input, result);
    }

    @Test
    public void convertToPastTense_pastSentenceRegularVerb2_returnsPastSentence() throws IOException {
        String input = "washed the dishes";
        String result = converter.convertToPastTense(input);
        assertNotNull(result);
        assertEquals(input, result);
    }

    @Test
    public void convertToPastTense_pastSentenceIrregularVerb1_returnsPastSentence() throws IOException {
        String input = "took out the trash";
        String result = converter.convertToPastTense(input);
        assertNotNull(result);
        assertEquals(input, result);
    }

    @Test
    public void convertToPastTense_pastSentenceIrregularVerb2_returnsPastSentence() throws IOException {
        String input = "went shopping";
        String result = converter.convertToPastTense(input);
        assertNotNull(result);
        assertEquals(input, result);
    }

    @Test
    public void convertToPastTense_pastSentenceIrregularVerb3_returnsPastSentence() throws IOException {
        String input = "did the dishes";
        String result = converter.convertToPastTense(input);
        assertNotNull(result);
        assertEquals(input, result);
    }

    @Test
    public void convertToPastTense_baseSentenceRegularVerb1_returnsPastSentence() throws IOException {
        String input = "clean the bathroom";
        String result = converter.convertToPastTense(input);
        assertNotNull(result);
        assertEquals("cleaned the bathroom", result);
    }

    @Test
    public void convertToPastTense_baseSentenceRegularVerb2_returnsPastSentence() throws IOException {
        String input = "wash the dishes";
        String result = converter.convertToPastTense(input);
        assertNotNull(result);
        assertEquals("washed the dishes", result);
    }

    @Test
    public void convertToPastTense_baseSentenceRegularVerb3_returnsPastSentence() throws IOException {
        String input = "cook dinner";
        String result = converter.convertToPastTense(input);
        assertNotNull(result);
        assertEquals("cooked dinner", result);
    }

    @Test
    public void convertToPastTense_baseSentenceIrregularVerb1_returnsPastSentence() throws IOException {
        String input = "take out the trash";
        String result = converter.convertToPastTense(input);
        assertNotNull(result);
        assertEquals("took out the trash", result);
    }

    @Test
    public void convertToPastTense_baseSentenceIrregularVerb2_returnsPastSentence() throws IOException {
        String input = "go shopping";
        String result = converter.convertToPastTense(input);
        assertNotNull(result);
        assertEquals("went shopping", result);
    }

    @Test
    public void convertToPastTense_baseSentenceIrregularVerb3_returnsPastSentence() throws IOException {
        String input = "do the dishes";
        String result = converter.convertToPastTense(input);
        assertNotNull(result);
        assertEquals("did the dishes", result);
    }

    // TODO: test for future + present simple / progressive, past perfect...

    @Test
    public void convertToBaseForm_baseSentenceRegularVerb1_returnsBaseSentence() throws IOException {
        String input = "clean the bathroom";
        String result = converter.convertToBaseForm(input);
        assertNotNull(result);
        assertEquals(input, result);
    }

    @Test
    public void convertToBaseForm_baseSentenceRegularVerb2_returnsBaseSentence() throws IOException {
        String input = "wash the dishes";
        String result = converter.convertToBaseForm(input);
        assertNotNull(result);
        assertEquals(input, result);
    }

    @Test
    public void convertToBaseForm_baseSentenceIrregularVerb1_returnsBaseSentence() throws IOException {
        String input = "take out the trash";
        String result = converter.convertToBaseForm(input);
        assertNotNull(result);
        assertEquals(input, result);
    }

    @Test
    public void convertToBaseForm_baseSentenceIrregularVerb2_returnsBaseSentence() throws IOException {
        String input = "go shopping";
        String result = converter.convertToBaseForm(input);
        assertNotNull(result);
        assertEquals(input, result);
    }

    @Test
    public void convertToBaseForm_baseSentenceIrregularVerb3_returnsBaseSentence() throws IOException {
        String input = "do the dishes";
        String result = converter.convertToBaseForm(input);
        assertNotNull(result);
        assertEquals(input, result);
    }

    @Test
    public void convertToBaseForm_pastSentenceRegularVerb1_returnsBaseSentence() throws IOException {
        String input = "cleaned the bathroom";
        String result = converter.convertToBaseForm(input);
        assertNotNull(result);
        assertEquals("clean the bathroom", result);
    }

    @Test
    public void convertToBaseForm_pastSentenceRegularVerb2_returnsBaseSentence() throws IOException {
        String input = "washed the dishes";
        String result = converter.convertToBaseForm(input);
        assertNotNull(result);
        assertEquals("wash the dishes", result);
    }

    @Test
    public void convertToBaseForm_pastSentenceIrregularVerb1_returnsBaseSentence() throws IOException {
        String input = "took out the trash";
        String result = converter.convertToBaseForm(input);
        assertNotNull(result);
        assertEquals("take out the trash", result);
    }

    @Test
    public void convertToBaseForm_pastSentenceIrregularVerb2_returnsBaseSentence() throws IOException {
        String input = "went shopping";
        String result = converter.convertToBaseForm(input);
        assertNotNull(result);
        assertEquals("go shopping", result);
    }

    @Test
    public void convertToBaseForm_pastSentenceIrregularVerb3_returnsBaseSentence() throws IOException {
        String input = "did the dishes";
        String result = converter.convertToBaseForm(input);
        assertNotNull(result);
        assertEquals("do the dishes", result);
    }

}
