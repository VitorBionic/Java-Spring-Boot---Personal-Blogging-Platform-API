package com.vitorbionic.unittests.mocks;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.vitorbionic.domain.Article;
import com.vitorbionic.domain.Tag;
import com.vitorbionic.domain.dto.ArticleDTO;
import com.vitorbionic.domain.dto.TagDTO;

public class MockArticle {
    
    private MockTag input = new MockTag();
    
    public Article mockEntity() {
        return mockEntity(0L);
    }
    
    public ArticleDTO mockDTO() {
        return mockDTO(0L);
    }
    
    public List<Article> mockEntityList() {
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < 14; i++)
            articles.add(mockEntity((long)i));
        return articles;
    }
    
    public List<ArticleDTO> mockDTOList() {
        List<ArticleDTO> articles = new ArrayList<>();
        for (int i = 0; i < 14; i++)
            articles.add(mockDTO((long)i));
        return articles;
    }
    
    public Article mockEntity(Long id) {
        Article article = new Article();
        article.setId(id);
        article.setTitle("Title Test " + id);
        article.setContent("Content Test " + id);
        article.setPublicationDate(Instant.parse("2025-01-07T10:14:45.00Z").plus(Duration.ofDays(id)));
        
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < id; i++) {
            tags.add(input.mockEntity());
        }
        
        article.setTags(tags);
        
        return article;
    }
    
    public ArticleDTO mockDTO(Long id) {
        List<TagDTO> tags = new ArrayList<>();
        for (int i = 0; i < id; i++) {
            tags.add(input.mockDTO());
        }
        
        return new ArticleDTO(
                id,
                "Title Test " + id,
                "Content Test " + id,
                Instant.parse("2025-01-07T10:14:45.00Z").plus(Duration.ofDays(id)),
                tags
                );
    }
}
