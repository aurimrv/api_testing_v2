package org.zalando.catwatch.backend;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import org.zalando.catwatch.backend.model.Contributor;
import org.zalando.catwatch.backend.model.Language;
import org.zalando.catwatch.backend.model.Project;
import org.zalando.catwatch.backend.model.Statistics;
import org.zalando.catwatch.backend.github.Snapshot;

public class sonnet35_run01_SnapshotTest {

    @Test
    public void testSnapshotConstructorAndGetters() {
        Statistics statistics = new Statistics();
        Collection<Project> projects = Arrays.asList(new Project());
        Collection<Contributor> contributors = Arrays.asList(new Contributor());
        Collection<Language> languages = Arrays.asList(new Language());

        Snapshot snapshot = new Snapshot(statistics, projects, contributors, languages);

        assertSame(statistics, snapshot.getStatistics());
        assertSame(projects, snapshot.getProjects());
        assertSame(contributors, snapshot.getContributors());
        assertSame(languages, snapshot.getLanguages());
    }

    @Test
    public void testSnapshotConstructorWithNullValues() {
        Snapshot snapshot = new Snapshot(null, null, null, null);

        assertNull(snapshot.getStatistics());
        assertNull(snapshot.getProjects());
        assertNull(snapshot.getContributors());
        assertNull(snapshot.getLanguages());
    }

    @Test
    public void testSnapshotConstructorWithEmptyCollections() {
        Statistics statistics = new Statistics();
        Collection<Project> projects = Arrays.asList();
        Collection<Contributor> contributors = Arrays.asList();
        Collection<Language> languages = Arrays.asList();

        Snapshot snapshot = new Snapshot(statistics, projects, contributors, languages);

        assertSame(statistics, snapshot.getStatistics());
        assertTrue(snapshot.getProjects().isEmpty());
        assertTrue(snapshot.getContributors().isEmpty());
        assertTrue(snapshot.getLanguages().isEmpty());
    }

    @Test
    public void testSnapshotGettersReturnCorrectTypes() {
        Snapshot snapshot = new Snapshot(new Statistics(), Arrays.asList(new Project()), Arrays.asList(new Contributor()), Arrays.asList(new Language()));

        assertTrue(snapshot.getStatistics() instanceof Statistics);
        assertTrue(snapshot.getProjects() instanceof Collection);
        assertTrue(snapshot.getContributors() instanceof Collection);
        assertTrue(snapshot.getLanguages() instanceof Collection);
    }
}
