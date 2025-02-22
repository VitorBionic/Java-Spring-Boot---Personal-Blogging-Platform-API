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
import org.springframework.web.bind.annotation.RestController;

import com.vitorbionic.domain.dto.TagDTO;
import com.vitorbionic.services.TagService;

@RestController
@RequestMapping(value = "/api/tag")
public class TagController {
    
    @Autowired
    private TagService service;
    
    @GetMapping
    public List<TagDTO> findAll() {
        return service.findAll();
    }
    
    @GetMapping(value = "/{id}")
    public TagDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }
    
    @PostMapping
    public TagDTO create(@RequestBody TagDTO article) {
        return service.create(article);
    }
    
    @PutMapping
    public TagDTO update(@RequestBody TagDTO article) {
        return service.update(article);
    }
    
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
