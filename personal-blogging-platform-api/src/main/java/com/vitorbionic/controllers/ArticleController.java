package com.vitorbionic.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vitorbionic.domain.dto.ArticleDTO;
import com.vitorbionic.services.ArticleService;

@RestController
@RequestMapping("/api/article")
public class ArticleController {
    
    @Autowired
    private ArticleService service;
    
    @GetMapping
    public List<ArticleDTO> findAll() {
        return service.findAll();
    }
    
    @GetMapping("/findArticlesWithFilter")
    public List<ArticleDTO> findArticlesWithFilter(@RequestParam (value = "date", defaultValue = "1800-01-01T00:00:00.00Z") String instant, @RequestParam (value = "tagsSeparatedBySemicolons", defaultValue = "") String tags) {
        return service.findArticlesWithFilter(instant, tags);
    }
    
    @GetMapping(value = "/{id}")
    public ArticleDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }
    
    @PostMapping
    public ArticleDTO create(@RequestBody ArticleDTO article) {
        return service.create(article);
    }
    
    @PutMapping
    public ArticleDTO update(@RequestBody ArticleDTO article) {
        return service.update(article);
    }
    
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
