import org.junit.Test;
import static org.junit.Assert.*;
import org.zalando.catwatch.backend.postgres.OptionalFlywayMigrationInitializer;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.mockito.Mockito.*;
import org.zalando.catwatch.backend.repo.util.DatabasePing;
import java.lang.reflect.Field;

public class sonnet35_run01_OptionalFlywayMigrationInitializerTest {

    @Test
    public void testConstructor() {
        Flyway flyway = mock(Flyway.class);
        FlywayMigrationStrategy migrationStrategy = mock(FlywayMigrationStrategy.class);
        OptionalFlywayMigrationInitializer initializer = new OptionalFlywayMigrationInitializer(flyway, migrationStrategy);
        assertNotNull(initializer);
    }

    @Test
    public void testAfterPropertiesSetWhenDatabaseAvailable() throws Exception {
        Flyway flyway = mock(Flyway.class);
        FlywayMigrationStrategy migrationStrategy = mock(FlywayMigrationStrategy.class);
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

        OptionalFlywayMigrationInitializer initializer = new OptionalFlywayMigrationInitializer(flyway, migrationStrategy);
        setPrivateField(initializer, "jdbcTemplate", jdbcTemplate);

        DatabasePing.isDatabaseAvailable(jdbcTemplate);
        when(DatabasePing.isDatabaseAvailable(jdbcTemplate)).thenReturn(true);

        initializer.afterPropertiesSet();

        verify(flyway).migrate();
    }

    @Test
    public void testAfterPropertiesSetWhenDatabaseNotAvailable() throws Exception {
        Flyway flyway = mock(Flyway.class);
        FlywayMigrationStrategy migrationStrategy = mock(FlywayMigrationStrategy.class);
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

        OptionalFlywayMigrationInitializer initializer = new OptionalFlywayMigrationInitializer(flyway, migrationStrategy);
        setPrivateField(initializer, "jdbcTemplate", jdbcTemplate);

        DatabasePing.isDatabaseAvailable(jdbcTemplate);
        when(DatabasePing.isDatabaseAvailable(jdbcTemplate)).thenReturn(false);

        initializer.afterPropertiesSet();

        verify(flyway, never()).migrate();
    }

    private void setPrivateField(Object object, String fieldName, Object fieldValue) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, fieldValue);
    }
}