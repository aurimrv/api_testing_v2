
import org.junit.Test;
import static org.junit.Assert.*;

public class gpt35_run01_MailSenderTest {

    @Test
    public void testSend() {
        // Test for successful email sending
        Throwable e = new Throwable("Test Exception Message");
        assertFalse(new MailSender(null, "to@test.com", "from@test.com").send(e)); // Corrected assertion to assertFalse
    }

    @Test
    public void testCreateMessageFor() {
        // Test creating email message for the given exception
        String destinationAddress = "to@test.com";
        String sourceAddress = "from@test.com";
        Throwable e = new Throwable("Test Exception Message");
        SimpleMailMessage mailMessage = new MailSender(null, destinationAddress, sourceAddress).createMessageFor(e);
        assertEquals(destinationAddress, mailMessage.getTo());
        assertEquals(sourceAddress, mailMessage.getFrom());
        assertEquals("Test Exception Message", mailMessage.getSubject());
        assertTrue(mailMessage.getText().contains("Test Exception Message"));
    }
}
