
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RegexTest {

    @Test
    public void testRegexSubjectUrlMatch() {
        String url = "http://example.com";
        String result = org.restscs.imp.Regex.subject(url);
        assertEquals("url", result);
    }

    @Test
    public void testRegexSubjectDateMatch() {
        String date = "mon12nov";
        String result = org.restscs.imp.Regex.subject(date);
        assertEquals("date", result);
    }

    @Test
    public void testRegexSubjectFpeMatch() {
        String fpe = "123.456e+789";
        String result = org.restscs.imp.Regex.subject(fpe);
        assertEquals("fpe", result);
    }

    @Test
    public void testRegexSubjectNoMatch() {
        String noMatch = "randomtext";
        String result = org.restscs.imp.Regex.subject(noMatch);
        assertEquals("none", result);
    }
}
  