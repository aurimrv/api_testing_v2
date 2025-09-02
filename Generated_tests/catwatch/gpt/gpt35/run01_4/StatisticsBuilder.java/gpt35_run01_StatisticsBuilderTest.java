
@Test
public void testStatisticsBuilderOrganizationName() {
    // Mocking StatisticsRepository and StatisticsBuilder classes for testing
    class StatisticsRepository {}
    class StatisticsBuilder {
        public StatisticsBuilder(StatisticsRepository statisticsRepository) {}
        public StatisticsBuilder organizationName(String organizationName) { return this; }
        public Object create() { return new Object(); }
    }

    StatisticsRepository statisticsRepository = new StatisticsRepository();
    StatisticsBuilder statisticsBuilder = new StatisticsBuilder(statisticsRepository);

    String organizationName = "TestOrg";
    statisticsBuilder.organizationName(organizationName);

    assertEquals(organizationName, ((StatisticsBuilder) statisticsBuilder).create());
}

@Test
public void testStatisticsBuilderPublicProjectCount() {
    // Mocking StatisticsRepository and StatisticsBuilder classes for testing
    class StatisticsRepository {}
    class StatisticsBuilder {
        public StatisticsBuilder(StatisticsRepository statisticsRepository) {}
        public StatisticsBuilder publicProjectCount(int publicProjectCount) { return this; }
        public Object create() { return new Object(); }
    }

    StatisticsRepository statisticsRepository = new StatisticsRepository();
    StatisticsBuilder statisticsBuilder = new StatisticsBuilder(statisticsRepository);

    int publicProjectCount = 100;
    statisticsBuilder.publicProjectCount(publicProjectCount);

    assertEquals(publicProjectCount, ((StatisticsBuilder) statisticsBuilder).create());
}
