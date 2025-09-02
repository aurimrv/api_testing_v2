package org.zalando.catwatch.backend;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.zalando.catwatch.backend.repo.builder.StatisticsBuilder;
import org.zalando.catwatch.backend.repo.StatisticsRepository;
import org.zalando.catwatch.backend.model.Statistics;
import java.util.Date;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class sonnet35_run01_StatisticsBuilderTest {

    private StatisticsRepository mockRepository;
    private StatisticsBuilder builder;

    @Before
    public void initTest() {
        mockRepository = Mockito.mock(StatisticsRepository.class);
        builder = new StatisticsBuilder(mockRepository);
    }

    @Test
    public void testConstructor() {
        assertNotNull(builder);
        assertNotNull(builder.create());
    }

    @Test
    public void testOrganizationName() {
        String orgName = "TestOrg";
        builder.organizationName(orgName);
        assertEquals(orgName, builder.create().getOrganizationName());
    }

    @Test
    public void testPublicProjectCount() {
        int count = 10;
        builder.publicProjectCount(count);
        assertEquals((long)count, (long)builder.create().getPublicProjectCount());
    }

    @Test
    public void testAllStarsCount() {
        int count = 100;
        builder.allStarsCount(count);
        assertEquals((long)count, (long)builder.create().getAllStarsCount());
    }

    @Test
    public void testAllForksCount() {
        int count = 50;
        builder.allForksCount(count);
        assertEquals((long)count, (long)builder.create().getAllForksCount());
    }

    @Test
    public void testProgramLanguagesCount() {
        int count = 5;
        builder.programLanguagesCount(count);
        assertEquals((long)count, (long)builder.create().getProgramLanguagesCount());
    }

    @Test
    public void testAllContributersCount() {
        int count = 20;
        builder.allContributersCount(count);
        assertEquals((long)count, (long)builder.create().getAllContributorsCount());
    }

    @Test
    public void testExternalContributorsCount() {
        int count = 15;
        builder.externalContributorsCount(count);
        assertEquals((long)count, (long)builder.create().getExternalContributorsCount());
    }

    @Test
    public void testAllSize() {
        int size = 1000;
        builder.allSize(size);
        assertEquals((long)size, (long)builder.create().getAllSizeCount());
    }

    @Test
    public void testMembersCount() {
        int count = 30;
        builder.membersCount(count);
        assertEquals((long)count, (long)builder.create().getMembersCount());
    }

    @Test
    public void testPrivateProjectCount() {
        int count = 5;
        builder.privateProjectCount(count);
        assertEquals((long)count, (long)builder.create().getPrivateProjectCount());
    }

    @Test
    public void testTagsCount() {
        int count = 25;
        builder.tagsCount(count);
        assertEquals((long)count, (long)builder.create().getTagsCount());
    }

    @Test
    public void testTeamsCount() {
        int count = 8;
        builder.teamsCount(count);
        assertEquals((long)count, (long)builder.create().getTeamsCount());
    }

    @Test
    public void testSnapshotDate() {
        Date date = new Date();
        builder.snapshotDate(date);
        assertEquals(date, builder.create().getSnapshotDate());
    }

    @Test
    public void testDays() {
        int days = 7;
        builder.days(days);
        assertNotNull(builder.create().getSnapshotDate());
    }

    @Test
    public void testCreate() {
        Statistics stats = builder
            .organizationName("TestOrg")
            .publicProjectCount(10)
            .privateProjectCount(5)
            .allStarsCount(100)
            .allForksCount(50)
            .programLanguagesCount(5)
            .allContributersCount(20)
            .externalContributorsCount(15)
            .allSize(1000)
            .membersCount(30)
            .tagsCount(25)
            .teamsCount(8)
            .create();

        assertNotNull(stats);
        assertEquals("TestOrg", stats.getOrganizationName());
        assertEquals(10L, (long)stats.getPublicProjectCount());
        assertEquals(5L, (long)stats.getPrivateProjectCount());
        assertEquals(100L, (long)stats.getAllStarsCount());
        assertEquals(50L, (long)stats.getAllForksCount());
        assertEquals(5L, (long)stats.getProgramLanguagesCount());
        assertEquals(20L, (long)stats.getAllContributorsCount());
        assertEquals(15L, (long)stats.getExternalContributorsCount());
        assertEquals(1000L, (long)stats.getAllSizeCount());
        assertEquals(30L, (long)stats.getMembersCount());
        assertEquals(25L, (long)stats.getTagsCount());
        assertEquals(8L, (long)stats.getTeamsCount());
    }

    @Test
    public void testSave() {
        Statistics stats = builder.create();
        when(mockRepository.save(any(Statistics.class))).thenReturn(stats);
        
        Statistics savedStats = builder.save();
        
        assertNotNull(savedStats);
        verify(mockRepository).save(any(Statistics.class));
    }
}
