package org.zalando.catwatch.backend;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class sonnet35_run01_DebugBeanTest {

    private DebugBean debugBean;
    private Environment mockEnv;
    private ApplicationContext mockContext;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @Before
    public void initTest() {
        mockEnv = mock(Environment.class);
        mockContext = mock(ApplicationContext.class);
        debugBean = new DebugBean(mockEnv, mockContext);

        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testPostConstruct() {
        // Arrange
        when(mockEnv.getActiveProfiles()).thenReturn(new String[]{"test", "dev"});
        when(mockEnv.getDefaultProfiles()).thenReturn(new String[]{"default"});
        when(mockEnv.getProperty("spring.database.driverClassName")).thenReturn("org.h2.Driver");
        when(mockEnv.getProperty("spring.jpa.hibernate.ddl-auto")).thenReturn("create-drop");
        when(mockContext.getBeanDefinitionNames()).thenReturn(new String[]{"bean1", "bean2"});

        // Act
        debugBean.postConstruct();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("================== " + DebugBean.class.getName() + "================================="));
        assertTrue(output.contains("active  profiles: test,dev"));
        assertTrue(output.contains("default profiles: default"));
        assertTrue(output.contains("spring.database.driverClassName: org.h2.Driver"));
        assertTrue(output.contains("spring.jpa.hibernate.ddl-auto: create-drop"));
        assertTrue(output.contains("all beans:"));
        assertTrue(output.contains("bean1"));
        assertTrue(output.contains("bean2"));
    }

    @Test
    public void testPostConstructWithNoProfiles() {
        // Arrange
        when(mockEnv.getActiveProfiles()).thenReturn(new String[]{});
        when(mockEnv.getDefaultProfiles()).thenReturn(new String[]{});
        when(mockEnv.getProperty("spring.database.driverClassName")).thenReturn(null);
        when(mockEnv.getProperty("spring.jpa.hibernate.ddl-auto")).thenReturn(null);
        when(mockContext.getBeanDefinitionNames()).thenReturn(new String[]{});

        // Act
        debugBean.postConstruct();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("active  profiles: "));
        assertTrue(output.contains("default profiles: "));
        assertTrue(output.contains("spring.database.driverClassName: null"));
        assertTrue(output.contains("spring.jpa.hibernate.ddl-auto: null"));
        assertTrue(output.contains("all beans:"));
    }

    @Test
    public void testConstructor() {
        // This test ensures the constructor is executed
        assertNotNull(debugBean);
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }
}
