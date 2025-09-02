
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestSubject {

    @Test
    public void testSubject() {
        String txt = "abcdeedcbaxcd";
        String pat = "cde";
        
        // Case where pat is found first and then patrev is found
        String result1 = Pat.subject(txt, pat);
        assertEquals("1", result1);

        // Case where patrev is found first and then pat is found
        txt = "edcbaxcdabcde";
        String result2 = Pat.subject(txt, pat);
        assertEquals("2", result2);

        // Case where both pat and its reverse occur in order
        txt = "abcdecde";
        String result3 = Pat.subject(txt, pat);
        assertEquals("3", result3);

        // Case where pat and reverse of pat occur as a palindrome in order
        txt = "abccdedcba";
        String result4 = Pat.subject(txt, pat);
        assertEquals("4", result4);

        // Case where reverse of pat and pat occur as a palindrome in order
        txt = "abcdedccbzz";
        String result5 = Pat.subject(txt, pat);
        assertEquals("5", result5);

        // Case where patlen <= 2
        pat = "ab";
        txt = "abcdef";
        String result6 = Pat.subject(txt, pat);
        assertEquals("0", result6);
    }
}
