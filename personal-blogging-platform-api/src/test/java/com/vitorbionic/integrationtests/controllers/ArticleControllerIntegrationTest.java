package com.vitorbionic.integrationtests.controllers;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vitorbionic.config.TestConfigs;
import com.vitorbionic.domain.dto.ArticleDTO;
import com.vitorbionic.domain.dto.TagDTO;
import com.vitorbionic.integrationtests.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ArticleControllerIntegrationTest extends AbstractIntegrationTest {

    private static RequestSpecification spec;
    private static ObjectMapper mapper;
    private static ArticleDTO articleDTO;
    
    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.registerModule(new JavaTimeModule());
        
        spec = new RequestSpecBuilder()
                .setBasePath("/api/article")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
        
        articleDTO = new ArticleDTO(
                    null,
                    "Mastering Focus",
                    "Techniques to stay focused in a distracted world.",
                    Instant.parse("2025-02-16T14:00:00Z"),
                    List.of(new TagDTO(4L, "Productivity"), new TagDTO(5L, "Motivation"))
                );
    }
    
    @Order(1)
    @Test
    public void testCreate() throws JsonMappingException, JsonProcessingException {
        
        String content = given().spec(spec)
                .contentType("application/json")
                .body(articleDTO)
            .when()
                .post()
            .then()
                .statusCode(200)
                    .extract()
                        .body()
                            .asString();
        
        ArticleDTO createdArticle = mapper.readValue(content, ArticleDTO.class);
        
        articleDTO = createdArticle;
        
        assertNotNull(createdArticle);
        assertNotNull(createdArticle.id());
        assertNotNull(createdArticle.title());
        assertNotNull(createdArticle.content());
        assertNotNull(createdArticle.publicationDate());
        assertNotNull(createdArticle.tags());
        
        assertTrue(createdArticle.id() > 0);
        assertEquals(createdArticle.title(), "Mastering Focus");
        assertEquals(createdArticle.content(), "Techniques to stay focused in a distracted world.");
        assertEquals(createdArticle.publicationDate().toString(), "2025-02-16T14:00:00Z");
        assertEquals(createdArticle.tags().size(), 2);
    }
    
    @Order(2)
    @Test
    public void testUpdate() throws JsonMappingException, JsonProcessingException {
        
        ArticleDTO updateInput = new ArticleDTO(
                    articleDTO.id(),
                    "Mastering Focus to Perfection",
                    "Techniques to stay focused in a widely distracted world.",
                    articleDTO.publicationDate(),
                    articleDTO.tags()
                );
        
        String content = given().spec(spec)
                .contentType("application/json")
                .body(updateInput)
            .when()
                .put()
            .then()
                .statusCode(200)
                    .extract()
                        .body()
                            .asString();
        
        ArticleDTO updatedArticle = mapper.readValue(content, ArticleDTO.class);
        
        articleDTO = updatedArticle;
        
        assertNotNull(updatedArticle);
        assertNotNull(updatedArticle.id());
        assertNotNull(updatedArticle.title());
        assertNotNull(updatedArticle.content());
        assertNotNull(updatedArticle.publicationDate());
        assertNotNull(updatedArticle.tags());
        
        assertTrue(updatedArticle.id() > 0);
        assertEquals(updatedArticle.title(), "Mastering Focus to Perfection");
        assertEquals(updatedArticle.content(), "Techniques to stay focused in a widely distracted world.");
        assertEquals(updatedArticle.publicationDate().toString(), "2025-02-16T14:00:00Z");
        assertEquals(updatedArticle.tags().size(), 2);
    }
    
    @Order(3)
    @Test
    public void testFindById() throws JsonMappingException, JsonProcessingException {
        
        String content = given().spec(spec)
                .pathParam("id", articleDTO.id())
            .when()
                .get("{id}")
            .then()
                .statusCode(200)
                    .extract()
                        .body()
                            .asString();
        
        ArticleDTO foundArticle = mapper.readValue(content, ArticleDTO.class);
        
        articleDTO = foundArticle;
        
        assertNotNull(foundArticle);
        assertNotNull(foundArticle.id());
        assertNotNull(foundArticle.title());
        assertNotNull(foundArticle.content());
        assertNotNull(foundArticle.publicationDate());
        assertNotNull(foundArticle.tags());
        
        assertTrue(foundArticle.id() > 0);
        assertEquals(foundArticle.title(), "Mastering Focus to Perfection");
        assertEquals(foundArticle.content(), "Techniques to stay focused in a widely distracted world.");
        assertEquals(foundArticle.publicationDate().toString(), "2025-02-16T14:00:00Z");
        assertEquals(foundArticle.tags().size(), 2);
    }
    
    @Order(4)
    @Test
    public void testFindAll() throws JsonMappingException, JsonProcessingException {
        
        String content = given().spec(spec)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                                .asString();
        
        ArticleDTO[] articlesArray = mapper.readValue(content, ArticleDTO[].class);
        List<ArticleDTO> foundArticles = Arrays.asList(articlesArray);
        
        assertNotNull(foundArticles);
        assertTrue(foundArticles.size() == 5);
        
        ArticleDTO foundArticleOne = foundArticles.get(0);
        
        assertNotNull(foundArticleOne);
        assertNotNull(foundArticleOne.id());
        assertNotNull(foundArticleOne.title());
        assertNotNull(foundArticleOne.content());
        assertNotNull(foundArticleOne.publicationDate());
        assertNotNull(foundArticleOne.tags());
        
        assertTrue(foundArticleOne.id() > 0);
        assertEquals(foundArticleOne.title(), "Introduction to Blogging");
        assertEquals(foundArticleOne.content(), "This is the first article about how to start a blog.");
        assertEquals(foundArticleOne.publicationDate().toString(), "2024-02-01T10:30:00Z");
        assertEquals(foundArticleOne.tags().size(), 1);
        
        ArticleDTO foundArticleThree = foundArticles.get(2);
        
        assertNotNull(foundArticleThree);
        assertNotNull(foundArticleThree.id());
        assertNotNull(foundArticleThree.title());
        assertNotNull(foundArticleThree.content());
        assertNotNull(foundArticleThree.publicationDate());
        assertNotNull(foundArticleThree.tags());
        
        assertTrue(foundArticleThree.id() > 0);
        assertEquals(foundArticleThree.title(), "The Power of Writing");
        assertEquals(foundArticleThree.content(), "Why writing consistently can improve your skills.");
        assertEquals(foundArticleThree.publicationDate().toString(), "2024-02-05T09:15:00Z");
        assertEquals(foundArticleThree.tags().size(), 1);
        
        ArticleDTO foundArticleFive = foundArticles.get(4);
        
        assertNotNull(foundArticleFive);
        assertNotNull(foundArticleFive.id());
        assertNotNull(foundArticleFive.title());
        assertNotNull(foundArticleFive.content());
        assertNotNull(foundArticleFive.publicationDate());
        assertNotNull(foundArticleFive.tags());
        
        assertTrue(foundArticleFive.id() > 0);
        assertEquals(foundArticleFive.title(), "Mastering Focus to Perfection");
        assertEquals(foundArticleFive.content(), "Techniques to stay focused in a widely distracted world.");
        assertEquals(foundArticleFive.publicationDate().toString(), "2025-02-16T14:00:00Z");
        assertEquals(foundArticleFive.tags().size(), 2);
    }
    
    @Order(5)
    @Test
    public void testFindArticlesWithFilter() throws JsonMappingException, JsonProcessingException {
        
        String content = given().spec(spec)
                    .params(Map.of("date", "2024-02-05", "tagsSeparatedBySemicolons", "Productivity;Motivation"))
                .when()
                    .get("findArticlesWithFilter")
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                                .asString();
        
        ArticleDTO[] articlesArray = mapper.readValue(content, ArticleDTO[].class);
        List<ArticleDTO> filterdArticles = Arrays.asList(articlesArray);
        
        assertNotNull(filterdArticles);
        assertTrue(filterdArticles.size() == 2);
        
        ArticleDTO foundArticleOne = filterdArticles.get(0);
        
        assertNotNull(foundArticleOne);
        assertNotNull(foundArticleOne.id());
        assertNotNull(foundArticleOne.title());
        assertNotNull(foundArticleOne.content());
        assertNotNull(foundArticleOne.publicationDate());
        assertNotNull(foundArticleOne.tags());
        
        assertTrue(foundArticleOne.id() > 0);
        assertEquals(foundArticleOne.title(), "How to Stay Motivated");
        assertEquals(foundArticleOne.content(), "Tips on staying productive and focused.");
        assertEquals(foundArticleOne.publicationDate().toString(), "2024-02-07T18:00:00Z");
        assertEquals(foundArticleOne.tags().size(), 2);
        
        ArticleDTO foundArticleTwo = filterdArticles.get(1);
        
        assertNotNull(foundArticleTwo);
        assertNotNull(foundArticleTwo.id());
        assertNotNull(foundArticleTwo.title());
        assertNotNull(foundArticleTwo.content());
        assertNotNull(foundArticleTwo.publicationDate());
        assertNotNull(foundArticleTwo.tags());
        
        assertTrue(foundArticleTwo.id() > 0);
        assertEquals(foundArticleTwo.title(), "Mastering Focus to Perfection");
        assertEquals(foundArticleTwo.content(), "Techniques to stay focused in a widely distracted world.");
        assertEquals(foundArticleTwo.publicationDate().toString(), "2025-02-16T14:00:00Z");
        assertEquals(foundArticleTwo.tags().size(), 2);
    }
    
    @Order(6)
    @Test
    public void testDelete() throws JsonMappingException, JsonProcessingException {
        
        given().spec(spec)
                .pathParam("id", articleDTO.id())
            .when()
                .delete("{id}")
            .then()
                .statusCode(204);
        
    }
    
    
}
