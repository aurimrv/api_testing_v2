package org.zalando.catwatch.backend;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zalando.catwatch.backend.repo.ContributorRepositoryImpl;
import org.zalando.catwatch.backend.model.Contributor;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class sonnet35_run01_ContributorRepositoryImplTest {

    private ContributorRepositoryImpl repository;

    @Mock
    private EntityManager entityManager;

    @Before
    public void initTest() {
        MockitoAnnotations.initMocks(this);
        repository = new ContributorRepositoryImpl();
        // Use reflection to set the private field
        try {
            java.lang.reflect.Field field = ContributorRepositoryImpl.class.getDeclaredField("em");
            field.setAccessible(true);
            field.set(repository, entityManager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set private field", e);
        }
    }

    @Test
    public void testFindOrganizationId() {
        String organizationName = "testOrg";
        Query mockQuery = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.setMaxResults(anyInt())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(Arrays.asList(123L));

        Long result = repository.findOrganizationId(organizationName);
        assertEquals(Long.valueOf(123L), result);

        verify(entityManager).createQuery(anyString());
        verify(mockQuery).setParameter("organizationName", organizationName);
        verify(mockQuery).setMaxResults(1);
        verify(mockQuery).getResultList();
    }

    @Test
    public void testFindOrganizationIdNoResult() {
        String organizationName = "nonExistentOrg";
        Query mockQuery = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.setMaxResults(anyInt())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(new ArrayList<>());

        Long result = repository.findOrganizationId(organizationName);
        assertEquals(Long.valueOf(-1L), result);
    }

    @Test(expected = NullPointerException.class)
    public void testFindOrganizationIdNullInput() {
        repository.findOrganizationId(null);
    }

    @Test
    public void testFindPreviousSnapShotDate() {
        Date snapshotDate = new Date();
        Query mockQuery = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.setMaxResults(anyInt())).thenReturn(mockQuery);
        Date expectedDate = new Date(snapshotDate.getTime() - 86400000); // One day before
        when(mockQuery.getResultList()).thenReturn(Arrays.asList(expectedDate));

        Date result = repository.findPreviousSnapShotDate(snapshotDate);
        assertEquals(expectedDate, result);

        verify(entityManager).createQuery(anyString());
        verify(mockQuery).setParameter("date", snapshotDate);
        verify(mockQuery).setMaxResults(1);
        verify(mockQuery).getResultList();
    }

    @Test
    public void testFindPreviousSnapShotDateNoResult() {
        Date snapshotDate = new Date();
        Query mockQuery = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.setMaxResults(anyInt())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(new ArrayList<>());

        Date result = repository.findPreviousSnapShotDate(snapshotDate);
        assertNull(result);
    }

    @Test
    public void testFindPreviousSnapShotDateNullInput() {
        Query mockQuery = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.setMaxResults(anyInt())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(new ArrayList<>());

        Date result = repository.findPreviousSnapShotDate(null);
        assertNull(result);
    }

    @Test
    @Ignore
    public void testFindAllTimeTopContributors() {
        Long organizationId = 1L;
        Date snapshotDate = new Date();
        String namePrefix = "test";
        Integer offset = 0;
        Integer limit = 10;

        CriteriaBuilder mockCb = mock(CriteriaBuilder.class);
        CriteriaQuery<Contributor> mockCq = mock(CriteriaQuery.class);
        Root<Contributor> mockRoot = mock(Root.class);
        Path<Object> mockPath = mock(Path.class);
        TypedQuery<Contributor> mockTypedQuery = mock(TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(mockCb);
        when(mockCb.createQuery(Contributor.class)).thenReturn(mockCq);
        when(mockCq.from(Contributor.class)).thenReturn(mockRoot);
        when(mockRoot.get(anyString())).thenReturn(mockPath);
        when(mockPath.get(anyString())).thenReturn(mockPath);
        when(entityManager.createQuery(mockCq)).thenReturn(mockTypedQuery);
        when(mockTypedQuery.setFirstResult(anyInt())).thenReturn(mockTypedQuery);
        when(mockTypedQuery.setMaxResults(anyInt())).thenReturn(mockTypedQuery);

        List<Contributor> expectedResult = new ArrayList<>();
        when(mockTypedQuery.getResultList()).thenReturn(expectedResult);

        List<Contributor> result = repository.findAllTimeTopContributors(organizationId, snapshotDate, namePrefix, offset, limit);

        assertEquals(expectedResult, result);
        verify(entityManager).getCriteriaBuilder();
        verify(mockCb).createQuery(Contributor.class);
        verify(mockCq).from(Contributor.class);
        verify(entityManager).createQuery(mockCq);
        verify(mockTypedQuery).setFirstResult(offset);
        verify(mockTypedQuery).setMaxResults(limit);
        verify(mockTypedQuery).getResultList();
    }

    @Test
    @Ignore
    public void testFindContributorsTimeSeries() {
        Long organizationId = 1L;
        Date startDate = new Date(System.currentTimeMillis() - 86400000);
        Date endDate = new Date();
        String namePrefix = "test";

        CriteriaBuilder mockCb = mock(CriteriaBuilder.class);
        CriteriaQuery<Contributor> mockCq = mock(CriteriaQuery.class);
        Root<Contributor> mockRoot = mock(Root.class);
        Path<Object> mockPath = mock(Path.class);
        TypedQuery<Contributor> mockTypedQuery = mock(TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(mockCb);
        when(mockCb.createQuery(Contributor.class)).thenReturn(mockCq);
        when(mockCq.from(Contributor.class)).thenReturn(mockRoot);
        when(mockRoot.get(anyString())).thenReturn(mockPath);
        when(mockPath.get(anyString())).thenReturn(mockPath);
        when(entityManager.createQuery(mockCq)).thenReturn(mockTypedQuery);

        List<Contributor> expectedResult = new ArrayList<>();
        when(mockTypedQuery.getResultList()).thenReturn(expectedResult);

        List<Contributor> result = repository.findContributorsTimeSeries(organizationId, startDate, endDate, namePrefix);

        assertEquals(expectedResult, result);
        verify(entityManager).getCriteriaBuilder();
        verify(mockCb).createQuery(Contributor.class);
        verify(mockCq).from(Contributor.class);
        verify(entityManager).createQuery(mockCq);
        verify(mockTypedQuery).getResultList();
    }
}
