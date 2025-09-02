
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Collection;
import java.util.Collections;
import java.util.Arrays; // Add this import statement
import java.util.Date;
import java.text.ParseException;

public class gpt35_run01_StringParserTest {

    @Test
    public void testParseStringList() {
        // Test parseStringList method with null stringList
        Collection<String> result = org.zalando.catwatch.backend.util.StringParser.parseStringList(null, ",");
        assertNotNull(result);
        assertEquals(Collections.emptyList(), result);

        // Test parseStringList method with single string
        result = org.zalando.catwatch.backend.util.StringParser.parseStringList("test", ",");
        assertNotNull(result);
        assertEquals(Arrays.asList("test"), result);

        // Test parseStringList method with multiple strings separated by comma
        result = org.zalando.catwatch.backend.util.StringParser.parseStringList("foo, bar, lock", ",");
        assertNotNull(result);
        assertEquals(Arrays.asList("foo", "bar", "lock"), result);
    }

    @Test
    public void testGetISO8601StringForDate() {
        // Test getISO8601StringForDate method with a specific date
        Date date = new Date(1627862400000L); // July 3, 2021, 12:00:00 AM UTC
        String result = org.zalando.catwatch.backend.util.StringParser.getISO8601StringForDate(date);
        assertNotNull(result);
        assertEquals("2021-08-02T00:00:00Z", result);
    }

    @Test
    public void testParseIso8601Date() {
        // Test parseIso8601Date method with a valid ISO 8601 date
        try {
            Date result = org.zalando.catwatch.backend.util.StringParser.parseIso8601Date("2021-08-02T00:00:00Z");
            assertNotNull(result);
            assertEquals(1627862400000L, result.getTime()); // July 3, 2021, 12:00:00 AM UTC
        } catch (ParseException e) {
            fail("Unexpected ParseException for valid ISO 8601 date");
        }
    }
}
