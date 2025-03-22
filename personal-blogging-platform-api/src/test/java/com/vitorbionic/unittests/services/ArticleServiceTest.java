package com.vitorbionic.unittests.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vitorbionic.domain.Article;
import com.vitorbionic.domain.Tag;
import com.vitorbionic.domain.dto.ArticleDTO;
import com.vitorbionic.domain.dto.TagDTO;
import com.vitorbionic.repositories.ArticleRepository;
import com.vitorbionic.repositories.TagRepository;
import com.vitorbionic.services.ArticleService;
import com.vitorbionic.unittests.mocks.MockArticle;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    
    MockArticle input;
    
    @InjectMocks
    private ArticleService service;
    
    @Mock
    ArticleRepository articleRepository;
    
    @Mock
    TagRepository tagRepository;
    
    
    @BeforeEach
    void setUpMocks() {
        input = new MockArticle();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        
        when(articleRepository.findAll()).thenReturn(input.mockEntityList());
        
        List<ArticleDTO> articles = service.findAll();
        
        assertNotNull(articles, () -> "articles list returned should not be null");
        assertEquals(articles.size(), 14);
        
        ArticleDTO firstArticle = articles.get(0);
        assertEquals(firstArticle.id(), 0);
        assertEquals(firstArticle.title(), "Title Test 0");
        assertEquals(firstArticle.content(), "Content Test 0");
        assertEquals(firstArticle.publicationDate().toString(), "2025-01-07T10:14:45Z");
        assertIterableEquals(firstArticle.tags(), List.of());
        
        ArticleDTO fourthArticle = articles.get(5);
        assertEquals(fourthArticle.id(), 5);
        assertEquals(fourthArticle.title(), "Title Test 5");
        assertEquals(fourthArticle.content(), "Content Test 5");
        assertEquals(fourthArticle.publicationDate().toString(), "2025-01-12T10:14:45Z");
        assertEquals(fourthArticle.tags().size(), 5);
        
        ArticleDTO sixthArticle = articles.get(7);
        assertEquals(sixthArticle.id(), 7);
        assertEquals(sixthArticle.title(), "Title Test 7");
        assertEquals(sixthArticle.content(), "Content Test 7");
        assertEquals(sixthArticle.publicationDate().toString(), "2025-01-14T10:14:45Z");
        assertEquals(sixthArticle.tags().size(), 7);
    }
    
    @Test
    void testFindById() {
        Article entity = input.mockEntity(1L);
        
        when(articleRepository.findById(1L)).thenReturn(Optional.of(entity));
        
        ArticleDTO result = service.findById(1L);
        
        assertNotNull(result);
        assertNotNull(result.id());
        assertNotNull(result.tags());
        
        assertEquals(result.id(), 1);
        assertEquals(result.title(), "Title Test 1");
        assertEquals(result.content(), "Content Test 1");
        assertEquals(result.publicationDate().toString(), "2025-01-08T10:14:45Z");
        assertIterableEquals(result.tags(), List.of(new TagDTO(0L, "Description 1")));      
    }
    
    @Test
    void testCreate() {
        ArticleDTO dto = input.mockDTO(1L);
        Article entity = input.mockEntity(1L);
        entity.setId(null);
        
        List<Tag> tags = entity.getTags();
        
        Article persisted = input.mockEntity(1L);
        persisted.getTags().get(0).setDescription(null);
        
        when(tagRepository.findById(0L)).thenReturn(Optional.of(tags.get(0)));
        when(articleRepository.save(any(Article.class))).thenReturn(persisted);
        entity.setId(persisted.getId());
        when(articleRepository.findById(persisted.getId())).thenReturn(Optional.of(entity));
        
        ArticleDTO persistedDTO = service.create(dto);
        
        assertNotNull(persistedDTO);
        assertNotNull(persistedDTO.id());
        assertNotNull(persistedDTO.tags());
        
        assertEquals(persistedDTO.id(), 1);
        assertEquals(persistedDTO.title(), "Title Test 1");
        assertEquals(persistedDTO.content(), "Content Test 1");
        assertEquals(persistedDTO.publicationDate().toString(), "2025-01-08T10:14:45Z");
        assertIterableEquals(persistedDTO.tags(), List.of(new TagDTO(0L, "Description 1")));     
    }
    
    @Test
    void testUpdate() {
        ArticleDTO dto = input.mockDTO(1L);
        dto = new ArticleDTO(
                dto.id(),
                "Title Test 2",
                "Content Test 2",
                dto.publicationDate(),
                List.of(new TagDTO(1L, "Description 2"))
                );
        Article persisted = input.mockEntity(1L);
        
        when(tagRepository.findById(1L)).thenReturn(Optional.of(new Tag(1L, "Description 2")));
        when(articleRepository.findById(dto.id())).thenReturn(Optional.of(persisted));
        persisted.setTitle(dto.title());
        persisted.setContent(dto.content());
        persisted.setPublicationDate(dto.publicationDate());
        persisted.setTags(List.of(new Tag(1L, "Description 2")));
        Article updated = new Article(persisted.getId(), persisted.getTitle(), persisted.getContent(), persisted.getPublicationDate(), List.copyOf(persisted.getTags()));
        updated.getTags().get(0).setDescription(null);
        when(articleRepository.save(persisted)).thenReturn(updated);
        when(articleRepository.findById(updated.getId())).thenReturn(Optional.of(persisted));
        
        
        ArticleDTO updatedDTO = service.update(dto);
        
        assertNotNull(updatedDTO);
        assertNotNull(updatedDTO.id());
        assertNotNull(updatedDTO.tags());
        
        assertEquals(updatedDTO.id(), 1);
        assertEquals(updatedDTO.title(), "Title Test 2");
        assertEquals(updatedDTO.content(), "Content Test 2");
        assertEquals(updatedDTO.publicationDate().toString(), "2025-01-08T10:14:45Z");
        assertIterableEquals(updatedDTO.tags(), List.of(new TagDTO(1L, null)));     
    }
    
    @Test
    void testDelete() {
        Article entity = input.mockEntity(1L);
        
        when(articleRepository.findById(1L)).thenReturn(Optional.of(entity));
        
        service.delete(1L);
    }

}
