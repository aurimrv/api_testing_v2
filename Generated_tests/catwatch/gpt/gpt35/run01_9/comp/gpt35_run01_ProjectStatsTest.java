
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

class Project {
    public Project(String name, String organization, String url, String description, String language, int commitCount, int forkCount, int contributorsCount, int score, Date date) {
        // Constructor implementation
    }
}

class ProjectStats {
    private String name;
    private String organizationName;
    private String url;
    private String description;
    private String primaryLanguage;
    private List<Integer> commitCounts;
    private List<Integer> forkCounts;
    private List<Integer> contributorsCounts;
    private List<Integer> scores;
    private List<Date> snapshotDates;

    public ProjectStats(List<Project> projects) {
        // Constructor implementation
    }

    public String getName() {
        return name;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public List<Integer> getCommitCounts() {
        return commitCounts;
    }

    public List<Integer> getForkCounts() {
        return forkCounts;
    }

    public List<Integer> getContributorsCounts() {
        return contributorsCounts;
    }

    public List<Integer> getScores() {
        return scores;
    }

    public List<Date> getSnapshotDates() {
        return snapshotDates;
    }
}

public class gpt35_run01_ProjectStatsTest {

    @Test
    public void testProjectStatsConstructor() {
        List<Project> projects = Arrays.asList(new Project("Project1", "Org1", "url1", "desc1", "Java", 10, 5, 3, 8, new Date()),
                                               new Project("Project1", "Org1", "url1", "desc1", "Java", 12, 6, 4, 9, new Date()));

        ProjectStats projectStats = new ProjectStats(projects);

        assertEquals("Project1", projectStats.getName());
        assertEquals("Org1", projectStats.getOrganizationName());
        assertEquals("url1", projectStats.getUrl());
        assertEquals("desc1", projectStats.getDescription());
        assertEquals("Java", projectStats.getPrimaryLanguage());

        List<Integer> commitCounts = projectStats.getCommitCounts();
        List<Integer> forkCounts = projectStats.getForkCounts();
        List<Integer> contributorsCounts = projectStats.getContributorsCounts();
        List<Integer> scores = projectStats.getScores();
        List<Date> snapshotDates = projectStats.getSnapshotDates();

        assertEquals(2, commitCounts.size());
        assertEquals(10, (int) commitCounts.get(0));
        assertEquals(12, (int) commitCounts.get(1));

        assertEquals(2, forkCounts.size());
        assertEquals(5, (int) forkCounts.get(0));
        assertEquals(6, (int) forkCounts.get(1));

        assertEquals(2, contributorsCounts.size());
        assertEquals(3, (int) contributorsCounts.get(0));
        assertEquals(4, (int) contributorsCounts.get(1));

        assertEquals(2, scores.size());
        assertEquals(8, (int) scores.get(0));
        assertEquals(9, (int) scores.get(1));

        assertEquals(2, snapshotDates.size());
    }

    @Test
    public void testProjectStatsGetters() {
        List<Project> projects = Arrays.asList(new Project("Project1", "Org1", "url1", "desc1", "Java", 10, 5, 3, 8, new Date()));
        ProjectStats projectStats = new ProjectStats(projects);

        assertEquals("Project1", projectStats.getName());
        assertEquals("Org1", projectStats.getOrganizationName());
        assertEquals("url1", projectStats.getUrl());
        assertEquals("Java", projectStats.getPrimaryLanguage());
        assertEquals("desc1", projectStats.getDescription());

        List<Integer> commitCounts = projectStats.getCommitCounts();
        assertEquals(1, commitCounts.size());
        assertEquals(10, (int) commitCounts.get(0));

        List<Integer> forkCounts = projectStats.getForkCounts();
        assertEquals(1, forkCounts.size());
        assertEquals(5, (int) forkCounts.get(0));

        List<Integer> contributorsCounts = projectStats.getContributorsCounts();
        assertEquals(1, contributorsCounts.size());
        assertEquals(3, (int) contributorsCounts.get(0));

        List<Integer> scores = projectStats.getScores();
        assertEquals(1, scores.size());
        assertEquals(8, (int) scores.get(0));

        List<Date> snapshotDates = projectStats.getSnapshotDates();
        assertEquals(1, snapshotDates.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProjectStatsConstructorWithDifferentNameProjects() {
        List<Project> projects = Arrays.asList(new Project("Project1", "Org1", "url1", "desc1", "Java", 10, 5, 3, 8, new Date()),
                                               new Project("Project2", "Org1", "url1", "desc1", "Java", 12, 6, 4, 9, new Date()));

        new ProjectStats(projects);
    }
}

