
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DateParseTest {

    @Test
    public void testDateParse() {
        // Test different combinations of day names and month names
        assertEquals("1", DateParse.subject("mon", "jan"));
        assertEquals("7", DateParse.subject("sun", "jul"));
        assertEquals("12", DateParse.subject("wed", "dec"));
        
        // Test lowercase and uppercase day and month names
        assertEquals("4", DateParse.subject("ThUr", "AUG"));
        assertEquals("9", DateParse.subject("fri", "SEP"));
        
        // Test unknown day and month names
        assertEquals("0", DateParse.subject("xyz", "abc"));
    }
}
