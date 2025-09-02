package org.zalando.catwatch.backend;

import org.junit.Test;
import static org.junit.Assert.*;
import org.zalando.catwatch.backend.model.Project;
import org.zalando.catwatch.backend.service.comparator.ProjectForkComparator;

public class sonnet35_run01_ProjectForkComparatorTest {

    @Test
    public void testCompareWithDifferentForkCounts() {
        ProjectForkComparator comparator = new ProjectForkComparator();
        Project p1 = new Project();
        Project p2 = new Project();
        
        p1.setForksCount(10);
        p2.setForksCount(20);
        
        int result = comparator.compare(p1, p2);
        assertTrue(result < 0);
        
        result = comparator.compare(p2, p1);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareWithEqualForkCounts() {
        ProjectForkComparator comparator = new ProjectForkComparator();
        Project p1 = new Project();
        Project p2 = new Project();
        
        p1.setForksCount(15);
        p2.setForksCount(15);
        
        int result = comparator.compare(p1, p2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareWithZeroForkCounts() {
        ProjectForkComparator comparator = new ProjectForkComparator();
        Project p1 = new Project();
        Project p2 = new Project();
        
        p1.setForksCount(0);
        p2.setForksCount(0);
        
        int result = comparator.compare(p1, p2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareWithNegativeForkCounts() {
        ProjectForkComparator comparator = new ProjectForkComparator();
        Project p1 = new Project();
        Project p2 = new Project();
        
        p1.setForksCount(-5);
        p2.setForksCount(-10);
        
        int result = comparator.compare(p1, p2);
        assertTrue(result > 0);
    }

    @Test
    public void testConstructor() {
        ProjectForkComparator comparator = new ProjectForkComparator();
        assertNotNull(comparator);
    }
}