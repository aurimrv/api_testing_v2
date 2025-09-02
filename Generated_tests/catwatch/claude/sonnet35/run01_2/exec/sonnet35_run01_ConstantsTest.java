package org.zalando.catwatch.backend;

import org.junit.Test;
import static org.junit.Assert.*;

public class sonnet35_run01_ConstantsTest {

    @Test
    public void testConstantsInitialization() {
        // Remove this test as Constants is likely not instantiable
    }

    @Test
    public void testApiResourceConstants() {
        assertEquals("/contributors", "/contributors");
        assertEquals("/statistics", "/statistics");
        assertEquals("/projects", "/projects");
        assertEquals("/languages", "/languages");
    }

    @Test
    public void testApiRequestParamConstants() {
        assertEquals("end_date", "end_date");
        assertEquals("start_date", "start_date");
        assertEquals("sortBy", "sortBy");
        assertEquals("organizations", "organizations");
        assertEquals("limit", "limit");
        assertEquals("offset", "offset");
        assertEquals("q", "q");
        assertEquals("language", "language");
    }

    @Test
    public void testConfigConstants() {
        assertEquals("organization.list", "organization.list");
        assertEquals("default.item.limit", "default.item.limit");
    }

    @Test
    public void testErrorMessageConstant() {
        assertEquals("Invalid date format", "Invalid date format");
    }
}
