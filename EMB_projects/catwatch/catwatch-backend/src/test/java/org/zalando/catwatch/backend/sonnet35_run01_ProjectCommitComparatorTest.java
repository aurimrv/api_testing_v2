package org.zalando.catwatch.backend;

import org.junit.Test;
import static org.junit.Assert.*;
import org.zalando.catwatch.backend.model.Project;
import org.zalando.catwatch.backend.service.comparator.ProjectCommitComparator;

public class sonnet35_run01_ProjectCommitComparatorTest {

    @Test
    public void testCompareWithDifferentCommitCounts() {
        ProjectCommitComparator comparator = new ProjectCommitComparator();
        
        Project p1 = new Project();
        p1.setCommitsCount(10);
        
        Project p2 = new Project();
        p2.setCommitsCount(20);
        
        int result = comparator.compare(p1, p2);
        assertTrue(result < 0);
        
        result = comparator.compare(p2, p1);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareWithEqualCommitCounts() {
        ProjectCommitComparator comparator = new ProjectCommitComparator();
        
        Project p1 = new Project();
        p1.setCommitsCount(10);
        
        Project p2 = new Project();
        p2.setCommitsCount(10);
        
        int result = comparator.compare(p1, p2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareWithZeroCommitCounts() {
        ProjectCommitComparator comparator = new ProjectCommitComparator();
        
        Project p1 = new Project();
        p1.setCommitsCount(0);
        
        Project p2 = new Project();
        p2.setCommitsCount(0);
        
        int result = comparator.compare(p1, p2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareWithNegativeCommitCounts() {
        ProjectCommitComparator comparator = new ProjectCommitComparator();
        
        Project p1 = new Project();
        p1.setCommitsCount(-5);
        
        Project p2 = new Project();
        p2.setCommitsCount(-10);
        
        int result = comparator.compare(p1, p2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareWithMaxIntegerValue() {
        ProjectCommitComparator comparator = new ProjectCommitComparator();
        
        Project p1 = new Project();
        p1.setCommitsCount(Integer.MAX_VALUE);
        
        Project p2 = new Project();
        p2.setCommitsCount(Integer.MAX_VALUE - 1);
        
        int result = comparator.compare(p1, p2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareWithMinIntegerValue() {
        ProjectCommitComparator comparator = new ProjectCommitComparator();
        
        Project p1 = new Project();
        p1.setCommitsCount(Integer.MIN_VALUE);
        
        Project p2 = new Project();
        p2.setCommitsCount(Integer.MIN_VALUE + 1);
        
        int result = comparator.compare(p1, p2);
        assertTrue(result < 0);
    }

    @Test
    public void testConstructor() {
        ProjectCommitComparator comparator = new ProjectCommitComparator();
        assertNotNull(comparator);
    }
}