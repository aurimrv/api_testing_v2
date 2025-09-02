
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ExampleTest {

    @Test
    public void testExample() {
        given()
            .when()
            .get("https://api.example.com/data")
            .then()
            .statusCode(200)
            .body("resultAsDouble", equalTo(0.89799772335573));
    }
}
