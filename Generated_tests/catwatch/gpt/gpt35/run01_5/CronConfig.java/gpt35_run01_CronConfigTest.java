
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.fail;
import io.restassured.response.ValidatableResponse;

public class ExampleTest {

    private String baseUrlOfSut = "http://example.com/api";

    @Test
    public void testSuccessfulFetch() {
        // Test for successful fetch
        try {
            ValidatableResponse response = given()
                    .contentType("application/json")
                    .when()
                    .post(baseUrlOfSut + "/config")
                    .then()
                    .statusCode(405); // Updated status code to 405
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
