
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RemainderTest {

    @Test
    public void testRemainder() {
        int result1 = org.restncs.imp.Remainder.exe(10, 3);
        assertEquals(1, result1);

        int result2 = org.restncs.imp.Remainder.exe(10, -3);
        assertEquals(1, result2);

        int result3 = org.restncs.imp.Remainder.exe(-10, 3);
        assertEquals(-1, result3);

        int result4 = org.restncs.imp.Remainder.exe(-10, -3);
        assertEquals(-1, result4);

        int result5 = org.restncs.imp.Remainder.exe(0, 5);
        assertEquals(0, result5);

        int result6 = org.restncs.imp.Remainder.exe(10, 0);
        assertEquals(-1, result6);

        int result7 = org.restncs.imp.Remainder.exe(0, 0);
        assertEquals(-1, result7);
    }
}
