package org.zalando.catwatch.backend;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;
import java.util.concurrent.Future;

import org.zalando.catwatch.backend.scheduler.Fetcher;
import org.zalando.catwatch.backend.scheduler.RetryableFetcher;
import org.zalando.catwatch.backend.github.Snapshot;
import org.zalando.catwatch.backend.github.SnapshotProvider;
import org.zalando.catwatch.backend.repo.ContributorRepository;
import org.zalando.catwatch.backend.repo.ProjectRepository;
import org.zalando.catwatch.backend.repo.StatisticsRepository;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;

public class sonnet35_run01_FetcherTest {

    @Test
    public void testFetchData() throws Exception {
        ProjectRepository projectRepository = Mockito.mock(ProjectRepository.class);
        StatisticsRepository statisticsRepository = Mockito.mock(StatisticsRepository.class);
        ContributorRepository contributorRepository = Mockito.mock(ContributorRepository.class);
        SnapshotProvider snapshotProvider = Mockito.mock(SnapshotProvider.class);
        String[] organizations = {"org1", "org2"};

        Fetcher fetcher = new Fetcher(projectRepository, statisticsRepository, contributorRepository, snapshotProvider, organizations);

        Snapshot snapshot1 = Mockito.mock(Snapshot.class);
        Snapshot snapshot2 = Mockito.mock(Snapshot.class);

        Future<Snapshot> future1 = Mockito.mock(Future.class);
        Future<Snapshot> future2 = Mockito.mock(Future.class);

        Mockito.when(snapshotProvider.takeSnapshot(Mockito.eq("org1"), Mockito.any(Date.class))).thenReturn(future1);
        Mockito.when(snapshotProvider.takeSnapshot(Mockito.eq("org2"), Mockito.any(Date.class))).thenReturn(future2);

        Mockito.when(future1.get()).thenReturn(snapshot1);
        Mockito.when(future2.get()).thenReturn(snapshot2);

        Mockito.when(snapshot1.getProjects()).thenReturn(Collections.emptyList());
        Mockito.when(snapshot1.getStatistics()).thenReturn(Collections.emptyList());
        Mockito.when(snapshot1.getContributors()).thenReturn(Collections.emptyList());
        Mockito.when(snapshot2.getProjects()).thenReturn(Collections.emptyList());
        Mockito.when(snapshot2.getStatistics()).thenReturn(Collections.emptyList());
        Mockito.when(snapshot2.getContributors()).thenReturn(Collections.emptyList());

        boolean result = fetcher.fetchData();

        assertTrue(result);

        Mockito.verify(statisticsRepository, Mockito.times(2)).save(Mockito.any(Iterable.class));
        Mockito.verify(projectRepository, Mockito.times(2)).save(Mockito.any(Iterable.class));
        Mockito.verify(contributorRepository, Mockito.times(2)).save(Mockito.any(Iterable.class));
    }

    @Test(expected = Exception.class)
    public void testFetchDataWithIOException() throws Exception {
        ProjectRepository projectRepository = Mockito.mock(ProjectRepository.class);
        StatisticsRepository statisticsRepository = Mockito.mock(StatisticsRepository.class);
        ContributorRepository contributorRepository = Mockito.mock(ContributorRepository.class);
        SnapshotProvider snapshotProvider = Mockito.mock(SnapshotProvider.class);
        String[] organizations = {"org1"};

        Fetcher fetcher = new Fetcher(projectRepository, statisticsRepository, contributorRepository, snapshotProvider, organizations);

        Mockito.when(snapshotProvider.takeSnapshot(Mockito.anyString(), Mockito.any(Date.class))).thenThrow(new IOException("Test exception"));

        fetcher.fetchData();
    }

    @Test(expected = Exception.class)
    public void testFetchDataWithExecutionException() throws Exception {
        ProjectRepository projectRepository = Mockito.mock(ProjectRepository.class);
        StatisticsRepository statisticsRepository = Mockito.mock(StatisticsRepository.class);
        ContributorRepository contributorRepository = Mockito.mock(ContributorRepository.class);
        SnapshotProvider snapshotProvider = Mockito.mock(SnapshotProvider.class);
        String[] organizations = {"org1"};

        Fetcher fetcher = new Fetcher(projectRepository, statisticsRepository, contributorRepository, snapshotProvider, organizations);

        Future<Snapshot> future = Mockito.mock(Future.class);
        Mockito.when(snapshotProvider.takeSnapshot(Mockito.anyString(), Mockito.any(Date.class))).thenReturn(future);
        Mockito.when(future.get()).thenThrow(new RuntimeException("Test exception"));

        fetcher.fetchData();
    }

    @Test
    public void testRetryableFetcher() throws Exception {
        Fetcher mockFetcher = Mockito.mock(Fetcher.class);
        RetryableFetcher retryableFetcher = new RetryableFetcher(mockFetcher, 3, 1000, 2000, 1.5, null);

        Mockito.when(mockFetcher.fetchData()).thenReturn(true);

        retryableFetcher.tryFetchData();

        Mockito.verify(mockFetcher, Mockito.times(1)).fetchData();
    }

    @Test
    public void testRetryableFetcherWithRetry() throws Exception {
        Fetcher mockFetcher = Mockito.mock(Fetcher.class);
        RetryableFetcher retryableFetcher = new RetryableFetcher(mockFetcher, 3, 1000, 2000, 1.5, null);

        Mockito.when(mockFetcher.fetchData())
               .thenThrow(new Exception("Test exception"))
               .thenReturn(true);

        retryableFetcher.tryFetchData();

        Mockito.verify(mockFetcher, Mockito.times(2)).fetchData();
    }

    @Test(expected = RuntimeException.class)
    public void testRetryableFetcherWithNonRetryableException() throws Exception {
        Fetcher mockFetcher = Mockito.mock(Fetcher.class);
        RetryableFetcher retryableFetcher = new RetryableFetcher(mockFetcher, 3, 1000, 2000, 1.5, null);

        Mockito.when(mockFetcher.fetchData()).thenThrow(new RuntimeException("Non-retryable exception"));

        retryableFetcher.tryFetchData();
    }
}