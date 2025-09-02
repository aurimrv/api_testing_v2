package org.zalando.catwatch.backend;

import org.junit.Test;
import static org.junit.Assert.*;
import org.zalando.catwatch.backend.postgres.OptionalFlywayMigrationInitializer;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.runner.RunWith;
import org.zalando.catwatch.backend.repo.util.DatabasePing;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabasePing.class})
public class sonnet35_run01_OptionalFlywayMigrationInitializerTest {

    @Test
    public void testConstructor() {
        Flyway flyway = mock(Flyway.class);
        FlywayMigrationStrategy strategy = mock(FlywayMigrationStrategy.class);
        
        OptionalFlywayMigrationInitializer initializer = new OptionalFlywayMigrationInitializer(flyway, strategy);
        
        assertNotNull(initializer);
    }

    @Test
    public void testAfterPropertiesSetWhenDatabaseAvailable() throws Exception {
        Flyway flyway = mock(Flyway.class);
        FlywayMigrationStrategy strategy = mock(FlywayMigrationStrategy.class);
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        
        OptionalFlywayMigrationInitializer initializer = new OptionalFlywayMigrationInitializer(flyway, strategy);
        
        // Use reflection to set the private field
        java.lang.reflect.Field field = OptionalFlywayMigrationInitializer.class.getDeclaredField("jdbcTemplate");
        field.setAccessible(true);
        field.set(initializer, jdbcTemplate);
        
        // Mock the static method
        PowerMockito.mockStatic(DatabasePing.class);
        when(DatabasePing.isDatabaseAvailable(jdbcTemplate)).thenReturn(true);
        
        initializer.afterPropertiesSet();
        
        // Verify that migrate() was called
        verify(flyway).migrate();
    }

    @Test
    public void testAfterPropertiesSetWhenDatabaseNotAvailable() throws Exception {
        Flyway flyway = mock(Flyway.class);
        FlywayMigrationStrategy strategy = mock(FlywayMigrationStrategy.class);
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        
        OptionalFlywayMigrationInitializer initializer = new OptionalFlywayMigrationInitializer(flyway, strategy);
        
        // Use reflection to set the private field
        java.lang.reflect.Field field = OptionalFlywayMigrationInitializer.class.getDeclaredField("jdbcTemplate");
        field.setAccessible(true);
        field.set(initializer, jdbcTemplate);
        
        // Mock the static method
        PowerMockito.mockStatic(DatabasePing.class);
        when(DatabasePing.isDatabaseAvailable(jdbcTemplate)).thenReturn(false);
        
        initializer.afterPropertiesSet();
        
        // Verify that migrate() was not called
        verify(flyway, never()).migrate();
    }
}