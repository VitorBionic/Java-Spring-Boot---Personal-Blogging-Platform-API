package com.vitorbionic.unittests.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.vitorbionic.domain.Article;
import com.vitorbionic.domain.Tag;
import com.vitorbionic.integrationtests.testcontainers.AbstractIntegrationTest;
import com.vitorbionic.repositories.ArticleRepository;
import com.vitorbionic.unittests.mocks.MockArticle;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticleRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ArticleRepository repository;
    
    private Article article1;
    
    private MockArticle articleInput = new MockArticle();
    
    @BeforeEach
    public void setup() {
        article1 = articleInput.mockEntity(1L);
    }
    
    @Test
    void testSave() {
        
        article1.setId(null);
        article1.getTags().get(0).setDescription(null);
        article1.getTags().get(0).setId(1L);
        
        Article savedArticle = repository.save(article1);
        
        assertNotNull(savedArticle);
        assertTrue(savedArticle.getId() > 0);
        
        assertEquals("Title Test 1", savedArticle.getTitle());
        assertEquals("Content Test 1", savedArticle.getContent());
        assertEquals("2025-01-08T10:14:45Z", savedArticle.getPublicationDate().toString());
        assertIterableEquals(List.of(new Tag(1L, null)), savedArticle.getTags());
    }
    
    @Test
    void testFindAll() {
        
        List<Article> articles = repository.findAll();
        
        assertNotNull(articles);
        assertEquals(4, articles.size());
        
        Article articleTwo = articles.get(1);
        
        assertEquals(2L, articleTwo.getId());
        assertEquals("MySQL Tips and Tricks", articleTwo.getTitle());
        assertEquals("Learn some cool MySQL optimizations and best practices.", articleTwo.getContent());
        assertEquals("2024-02-03T14:45:00Z", articleTwo.getPublicationDate().toString());
        assertIterableEquals(List.of(new Tag(2L, "MySQL")), articleTwo.getTags());
        
    }
    
    @Test
    void testFindById() {
        
        Article article = repository.findById(2L).get();
        
        assertNotNull(article);
        
        assertEquals("MySQL Tips and Tricks", article.getTitle());
        assertEquals("Learn some cool MySQL optimizations and best practices.", article.getContent());
        assertEquals("2024-02-03T14:45:00Z", article.getPublicationDate().toString());
        assertIterableEquals(List.of(new Tag(2L, "MySQL")), article.getTags());
        
    }
    
    @Test
    void testDelete() {
        
        article1.setId(null);
        article1.getTags().get(0).setDescription(null);
        article1.getTags().get(0).setId(1L);
        
        Article savedArticle1 = repository.save(article1);
        Long article1Id = savedArticle1.getId();
        
        repository.delete(savedArticle1);
        
        assertTrue(repository.findById(article1Id).isEmpty());
        
    }
    
    @Test
    void testFindArticlesWithFilter() {
        
        Instant instantFilter = Instant.parse("2024-02-05T00:00:00Z");
        
        List<Article> articles = repository.findArticlesWithFilter(instantFilter);
        
        assertNotNull(articles);
        assertEquals(2, articles.size());
        
        Article articleOne = articles.get(0);
        
        assertEquals(3L, articleOne.getId());
        assertEquals("The Power of Writing", articleOne.getTitle());
        assertEquals("Why writing consistently can improve your skills.", articleOne.getContent());
        assertEquals("2024-02-05T09:15:00Z", articleOne.getPublicationDate().toString());
        assertIterableEquals(List.of(new Tag(3L, "Writing")), articleOne.getTags());
        
    }
    
}
