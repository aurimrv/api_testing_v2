
package org.zalando.catwatch.backend;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.List;
import static org.evomaster.client.java.controller.api.EMTestUtils.*;
import org.evomaster.client.java.controller.SutHandler;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.ValidatableResponse;
import static org.evomaster.client.java.sql.dsl.SqlDsl.sql;
import org.evomaster.client.java.controller.api.dto.database.operations.InsertionResultsDto;
import org.evomaster.client.java.controller.api.dto.database.operations.InsertionDto;
import static org.hamcrest.Matchers.*;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static org.evomaster.client.java.controller.contentMatchers.NumberMatcher.*;
import static org.evomaster.client.java.controller.contentMatchers.StringMatcher.*;
import static org.evomaster.client.java.controller.contentMatchers.SubStringMatcher.*;
import static org.evomaster.client.java.controller.expect.ExpectationHandler.expectationHandler;
import org.evomaster.client.java.controller.expect.ExpectationHandler;
import io.restassured.path.json.JsonPath;
import java.util.Arrays;

// Mock ProjectBuilder class
class ProjectBuilder {
    private Project project;

    public ProjectBuilder() {
        this.project = new Project();
    }

    public ProjectBuilder(ProjectRepository repository) {
        this.project = new Project();
    }

    public ProjectBuilder(ProjectRepository repository, java.util.Date snapshotDate, Long gitHubProjectId,
                          String name, String primaryLanguage, int contributorsCount, int forksCount,
                          int starsCount, int watchersCount, int openIssuesCount, int closedIssuesCount) {
        this.project = new Project();
        this.project.setName(name);
        this.project.setPrimaryLanguage(primaryLanguage);
        this.project.setGitHubProjectId(gitHubProjectId);
    }

    public ProjectBuilder days(int days) {
        this.project.setSnapshotDate(new java.util.Date());
        return this;
    }

    public ProjectBuilder externalContributorsCount(int count) {
        this.project.setContributorsCount(count);
        return this;
    }

    public ProjectBuilder description(String description) {
        this.project.setDescription(description);
        return this;
    }

    public ProjectBuilder lastPushed(String lastPushed) {
        this.project.setLastPushed(lastPushed);
        return this;
    }

    public ProjectBuilder languages(List<String> languages) {
        this.project.setLanguageList(languages);
        return this;
    }

    public Project getProject() {
        return this.project;
    }

    public Project create() {
        this.project.setUrl("http://example.com");
        return this.project;
    }

    public void save() {
        if (this.project == null) {
            throw new NullPointerException("Project is null");
        }
    }
}

// Mock Project class
class Project {
    private String name;
    private String primaryLanguage;
    private Long gitHubProjectId;
    private Integer contributorsCount;
    private String description;
    private String lastPushed;
    private List<String> languageList;
    private String url;
    private java.util.Date snapshotDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public Long getGitHubProjectId() {
        return gitHubProjectId;
    }

    public void setGitHubProjectId(Long gitHubProjectId) {
        this.gitHubProjectId = gitHubProjectId;
    }

    public Integer getContributorsCount() {
        return contributorsCount;
    }

    public void setContributorsCount(Integer contributorsCount) {
        this.contributorsCount = contributorsCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastPushed() {
        return lastPushed;
    }

    public void setLastPushed(String lastPushed) {
        this.lastPushed = lastPushed;
    }

    public List<String> getLanguageList() {
        return languageList;
    }

    public void setLanguageList(List<String> languageList) {
        this.languageList = languageList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public java.util.Date getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(java.util.Date snapshotDate) {
        this.snapshotDate = snapshotDate;
    }
}

// Mock ProjectRepository class
class ProjectRepository {
}
