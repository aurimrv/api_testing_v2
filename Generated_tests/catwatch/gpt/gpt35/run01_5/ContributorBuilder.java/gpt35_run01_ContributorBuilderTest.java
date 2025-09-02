
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ContributorBuilderTest {

    @Test
    public void testId() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.id(1234L);
        assertNotNull(result);
    }

    @Test
    public void testUrl() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.url("https://test.com");
        assertNotNull(result);
    }

    @Test
    public void testName() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.name("Test Name");
        assertNotNull(result);
    }

    @Test
    public void testDays() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.days(5);
        assertNotNull(result);
    }

    @Test
    public void testOrganizationName() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.organizationName("Test Organization");
        assertNotNull(result);
    }

    @Test
    public void testOrganizationId() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.organizationId(9876L);
        assertNotNull(result);
    }

    @Test
    public void testOrgCommits() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.orgCommits(50);
        assertNotNull(result);
    }

    @Test
    public void testOrgProjects() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.orgProjects(5);
        assertNotNull(result);
    }

    @Test
    public void testPersProjects() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.persProjects(3);
        assertNotNull(result);
    }

    @Test
    public void testPersCommits() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.persCommits(6);
        assertNotNull(result);
    }

    @Test
    public void testSnapshotDate() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.snapshotDate(new Date());
        assertNotNull(result);
    }

    @Test
    public void testCreate() {
        ContributorBuilder cb = new ContributorBuilder();
        Contributor result = cb.create();
        assertNotNull(result);
    }

    @Test
    public void testSave() {
        ContributorBuilder cb = new ContributorBuilder();
        Contributor result = cb.save();
        assertNotNull(result);
    }
}
