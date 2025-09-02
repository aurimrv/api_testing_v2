package org.zalando.catwatch.backend;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.List;
import static org.evomaster.client.java.controller.api.EMTestUtils.*;
import org.evomaster.client.java.controller.SutHandler;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.ValidatableResponse;
import static org.evomaster.client.java.sql.dsl.SqlDsl.sql;
import org.evomaster.client.java.controller.api.dto.database.operations.InsertionResultsDto;
import org.evomaster.client.java.controller.api.dto.database.operations.InsertionDto;
import static org.hamcrest.Matchers.*;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static org.evomaster.client.java.controller.contentMatchers.NumberMatcher.*;
import static org.evomaster.client.java.controller.contentMatchers.StringMatcher.*;
import static org.evomaster.client.java.controller.contentMatchers.SubStringMatcher.*;
import static org.evomaster.client.java.controller.expect.ExpectationHandler.expectationHandler;
import org.evomaster.client.java.controller.expect.ExpectationHandler;
import io.restassured.path.json.JsonPath;
import java.util.Arrays;

import org.zalando.catwatch.backend.model.Project;
import org.zalando.catwatch.backend.model.util.Scorer;

public class sonnet35_run01_ScorerTest {

    private static final SutHandler controller = new em.embedded.org.zalando.EmbeddedEvoMasterController();
    private static String baseUrlOfSut;

    private Scorer scorer;

    @BeforeClass
    public static void initClass() {
        controller.setupForGeneratedTest();
        baseUrlOfSut = controller.startSut();
        controller.registerOrExecuteInitSqlCommandsIfNeeded();
        assertNotNull(baseUrlOfSut);
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.urlEncodingEnabled = false;
        RestAssured.config = RestAssured.config()
            .jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE))
            .redirect(redirectConfig().followRedirects(false));
    }

    @AfterClass
    public static void tearDown() {
        controller.stopSut();
    }

    @Before
    public void initTest() {
        controller.resetDatabase(Arrays.asList("CONTRIBUTOR"));
        controller.resetStateOfSUT();
        scorer = new Scorer();
    }

    @Test
    public void testScore() {
        Project project = new Project();
        project.setStarsCount(100);
        project.setForksCount(50);
        project.setCommitsCount(200);

        scorer.setScoringProject("function(project) { return project.starsCount + project.forksCount + project.commitsCount; }");

        int score = scorer.score(project);
        assertEquals(350, score);
    }

    @Test
    public void testScoreWithNullProject() {
        scorer.setScoringProject("function(project) { return 0; }");

        int score = scorer.score(null);
        assertEquals(0, score);
    }

    @Test
    public void testScoreWithComplexLogic() {
        Project project = new Project();
        project.setStarsCount(100);
        project.setForksCount(50);
        project.setCommitsCount(200);

        scorer.setScoringProject("function(project) { " +
                "if (project.starsCount > 50) return project.starsCount * 2; " +
                "else if (project.forksCount > 25) return project.forksCount * 3; " +
                "else return project.commitsCount; " +
                "}");

        int score = scorer.score(project);
        assertEquals(200, score);
    }

    @Test
    public void testSetScoringProject() {
        String scoringFunction = "function(project) { return project.starsCount * 2; }";
        scorer.setScoringProject(scoringFunction);

        Project project = new Project();
        project.setStarsCount(50);

        int score = scorer.score(project);
        assertEquals(100, score);
    }

    @Test(expected = NullPointerException.class)
    public void testSetScoringProjectWithNull() {
        scorer.setScoringProject(null);
    }

    @Test
    public void testScoreWithEmptyScoringProject() {
        scorer.setScoringProject("");

        Project project = new Project();
        project.setStarsCount(100);

        int score = scorer.score(project);
        assertEquals(0, score);
    }
}
