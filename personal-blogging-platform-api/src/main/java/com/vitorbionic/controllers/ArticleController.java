package com.vitorbionic.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
    
    @GetMapping(value = "/{id}")
    public ArticleDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }
}
