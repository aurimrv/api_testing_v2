
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class MyTest {

    @Test
    public void testStatusCode() {
        Response response = RestAssured.get("https://api.example.com/data");
        assertEquals(406, response.getStatusCode());
    }
}
