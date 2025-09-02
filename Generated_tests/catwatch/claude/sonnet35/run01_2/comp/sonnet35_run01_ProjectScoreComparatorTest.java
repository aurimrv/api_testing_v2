package org.zalando.catwatch.backend;

import org.junit.Test;
import static org.junit.Assert.*;
import org.zalando.catwatch.backend.model.Project;
import org.zalando.catwatch.backend.service.comparator.ProjectScoreComparator;

public class sonnet35_run01_ProjectScoreComparatorTest {

    @Test
    public void testCompareWithDifferentScores() {
        ProjectScoreComparator comparator = new ProjectScoreComparator();
        Project p1 = new Project();
        Project p2 = new Project();
        
        p1.setScore(10);
        p2.setScore(20);
        
        int result = comparator.compare(p1, p2);
        assertTrue(result < 0);
        
        result = comparator.compare(p2, p1);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareWithEqualScores() {
        ProjectScoreComparator comparator = new ProjectScoreComparator();
        Project p1 = new Project();
        Project p2 = new Project();
        
        p1.setScore(15);
        p2.setScore(15);
        
        int result = comparator.compare(p1, p2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareWithZeroScores() {
        ProjectScoreComparator comparator = new ProjectScoreComparator();
        Project p1 = new Project();
        Project p2 = new Project();
        
        p1.setScore(0);
        p2.setScore(0);
        
        int result = comparator.compare(p1, p2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareWithNegativeScores() {
        ProjectScoreComparator comparator = new ProjectScoreComparator();
        Project p1 = new Project();
        Project p2 = new Project();
        
        p1.setScore(-5);
        p2.setScore(-10);
        
        int result = comparator.compare(p1, p2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareWithMaxIntegerValue() {
        ProjectScoreComparator comparator = new ProjectScoreComparator();
        Project p1 = new Project();
        Project p2 = new Project();
        
        p1.setScore(Integer.MAX_VALUE);
        p2.setScore(Integer.MAX_VALUE - 1);
        
        int result = comparator.compare(p1, p2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareWithMinIntegerValue() {
        ProjectScoreComparator comparator = new ProjectScoreComparator();
        Project p1 = new Project();
        Project p2 = new Project();
        
        p1.setScore(Integer.MIN_VALUE);
        p2.setScore(Integer.MIN_VALUE + 1);
        
        int result = comparator.compare(p1, p2);
        assertTrue(result < 0);
    }

    @Test
    public void testConstructor() {
        ProjectScoreComparator comparator = new ProjectScoreComparator();
        assertNotNull(comparator);
    }
}
