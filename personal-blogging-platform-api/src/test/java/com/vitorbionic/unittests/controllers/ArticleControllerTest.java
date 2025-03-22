package com.vitorbionic.unittests.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitorbionic.config.TestConfigs;
import com.vitorbionic.domain.dto.ArticleDTO;
import com.vitorbionic.domain.dto.TagDTO;
import com.vitorbionic.exceptions.ResourceNotFoundException;
import com.vitorbionic.services.ArticleService;
import com.vitorbionic.services.TagService;
import com.vitorbionic.unittests.mocks.MockArticle;

@WebMvcTest
public class ArticleControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;
    
    @MockitoBean
    private ArticleService articleService;
    
    @MockitoBean
    private TagService tagService;
    
    private ArticleDTO dto1; 
    
    private MockArticle input = new MockArticle();
    
    @BeforeEach
    public void setup() {
        dto1 = input.mockDTO(1L);
    }
    
    @Test
    void testCreate() throws JsonProcessingException, Exception {
        
        when(articleService.create(any(ArticleDTO.class)))
            .then(invocation -> invocation.getArgument(0));
        
        ResultActions response = mockMvc.perform(post("/api/article")
                .contentType("application/json")
                .content(mapper.writeValueAsString(dto1)));
        
        response.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.title", is("Title Test 1")))
            .andExpect(jsonPath("$.content", is("Content Test 1")))
            .andExpect(jsonPath("$.publicationDate", is("2025-01-08T10:14:45Z")))
            .andExpect(jsonPath("$.tags.size()", is(1)));
    }
    
    @Test
    void findAll() throws Exception {
        
        List<ArticleDTO> dtoList = new ArrayList<>();
        dtoList.add(dto1);
        dtoList.add(input.mockDTO(2L));
        
        when(articleService.findAll()).thenReturn(dtoList);
        
        ResultActions response = mockMvc.perform(get("/api/article"));
        
        response.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(dtoList.size())));
    }
    
    @Test
    void testFindById() throws JsonProcessingException, Exception {
        
        Long id = dto1.id();
        when(articleService.findById(id)).thenReturn(dto1);
        
        ResultActions response = mockMvc.perform(get("/api/article/{id}", id));
        
        response.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.title", is("Title Test 1")))
            .andExpect(jsonPath("$.content", is("Content Test 1")))
            .andExpect(jsonPath("$.publicationDate", is("2025-01-08T10:14:45Z")))
            .andExpect(jsonPath("$.tags.size()", is(1)))
            .andExpect(jsonPath("$.tags[0].id", is(0)))
            .andExpect(jsonPath("$.tags[0].description", is("Description 1")));
    }
    
    @Test
    void testFindByIdThrowingNotFoundException() throws Exception {
        
        Long id = dto1.id();
        when(articleService.findById(id)).thenThrow(ResourceNotFoundException.class);
        
        ResultActions response = mockMvc.perform(get("/api/article/{id}", id));
        
        response.andExpect(status().isNotFound())
            .andDo(print());  
    }
    
    @Test
    void testUpdate() throws JsonProcessingException, Exception {
        
        when(articleService.update(any(ArticleDTO.class)))
            .then(invocation -> invocation.getArgument(0));
        
        ArticleDTO updatedDTO = new ArticleDTO(1L, "New Title Test 1", "New Content Test 1", Instant.parse("2025-02-10T11:18:45Z"), dto1.tags());
        
        ResultActions response = mockMvc.perform(put("/api/article")
                .contentType("application/json")
                .content(mapper.writeValueAsString(updatedDTO)));
        
        response.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.title", is("New Title Test 1")))
            .andExpect(jsonPath("$.content", is("New Content Test 1")))
            .andExpect(jsonPath("$.publicationDate", is("2025-02-10T11:18:45Z")))
            .andExpect(jsonPath("$.tags.size()", is(1)))
            .andExpect(jsonPath("$.tags[0].id", is(0)))
            .andExpect(jsonPath("$.tags[0].description", is("Description 1")));
    }
    
    @Test
    void testUpdateThrowingNotFoundException() throws JsonProcessingException, Exception {
        
        when(articleService.update(any(ArticleDTO.class))).thenThrow(ResourceNotFoundException.class);
        
        ArticleDTO updatedDTO = new ArticleDTO(1L, "New Title Test 1", "New Content Test 1", Instant.parse("2025-02-10T11:18:45Z"), dto1.tags());
        
        ResultActions response = mockMvc.perform(put("/api/article")
                .contentType("application/json")
                .content(mapper.writeValueAsString(updatedDTO)));
        
        response.andExpect(status().isNotFound())
            .andDo(print());
    }
    
    @Test
    void testDelete() throws Exception {
        
        Long id = dto1.id();
        doNothing().when(articleService).delete(id);
        
        ResultActions response = mockMvc.perform(delete("/api/article/{id}", id));
        
        response.andExpect(status().isNoContent())
            .andDo(print());
    }
    
    @Test
    void testfindArticlesWithFilter() throws Exception {
        
        List<ArticleDTO> dtoList = new ArrayList<>();
        dtoList.add(dto1);
        dtoList.add(input.mockDTO(2L));
        dtoList.add(input.mockDTO(3L));
        
        List<ArticleDTO> filteredList = List.of(dtoList.get(2));
        when(articleService.findArticlesWithFilter("2025-01-08", "Description 6")).thenReturn(filteredList);
        
        ResultActions response = mockMvc.perform(get("/api/article/findArticlesWithFilter")
                .params(MultiValueMap.fromSingleValue(Map.of("date", "2025-01-08", "tagsSeparatedBySemicolons", "Description 6"))));
        
        response.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", is(filteredList.size())))
            .andExpect(jsonPath("$.[0].id", is(3)))
            .andExpect(jsonPath("$.[0].title", is("Title Test 3")))
            .andExpect(jsonPath("$.[0].content", is("Content Test 3")))
            .andExpect(jsonPath("$.[0].publicationDate", is("2025-01-10T10:14:45Z")))
            .andExpect(jsonPath("$.[0].tags.size()", is(3)))
            .andExpect(jsonPath("$.[0].tags[2].id", is(5)))
            .andExpect(jsonPath("$.[0].tags[2].description", is("Description 6")));;
    }
    
}
