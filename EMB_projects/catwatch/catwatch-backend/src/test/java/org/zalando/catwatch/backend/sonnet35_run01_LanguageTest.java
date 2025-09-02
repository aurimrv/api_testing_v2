package org.zalando.catwatch.backend;

import org.junit.Test;
import static org.junit.Assert.*;

public class sonnet35_run01_LanguageTest {

    public class Language {
        private String name;
        private Integer projectsCount;
        private Integer percentage;

        public Language() {}

        public Language(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getProjectsCount() {
            return projectsCount;
        }

        public void setProjectsCount(Integer projectsCount) {
            this.projectsCount = projectsCount;
        }

        public Integer getPercentage() {
            return percentage;
        }

        public void setPercentage(Integer percentage) {
            this.percentage = percentage;
        }

        @Override
        public String toString() {
            return "class Language {\n" +
                   "  name: " + name + "\n" +
                   "  projectsCount: " + projectsCount + "\n" +
                   "  percentage: " + percentage + "\n" +
                   "}\n";
        }
    }

    @Test
    public void testConstructor() {
        Language language = new Language();
        assertNotNull(language);
    }

    @Test
    public void testConstructorWithName() {
        String testName = "Java";
        Language language = new Language(testName);
        assertEquals(testName, language.getName());
    }

    @Test
    public void testGetSetName() {
        Language language = new Language();
        String testName = "Python";
        language.setName(testName);
        assertEquals(testName, language.getName());
    }

    @Test
    public void testGetSetProjectsCount() {
        Language language = new Language();
        Integer testCount = 10;
        language.setProjectsCount(testCount);
        assertEquals(testCount, language.getProjectsCount());
    }

    @Test
    public void testGetSetPercentage() {
        Language language = new Language();
        Integer testPercentage = 25;
        language.setPercentage(testPercentage);
        assertEquals(testPercentage, language.getPercentage());
    }

    @Test
    public void testToString() {
        Language language = new Language("JavaScript");
        language.setProjectsCount(5);
        language.setPercentage(15);
        String expected = "class Language {\n  name: JavaScript\n  projectsCount: 5\n  percentage: 15\n}\n";
        assertEquals(expected, language.toString());
    }

    @Test
    public void testToStringWithNullValues() {
        Language language = new Language();
        String expected = "class Language {\n  name: null\n  projectsCount: null\n  percentage: null\n}\n";
        assertEquals(expected, language.toString());
    }
}
