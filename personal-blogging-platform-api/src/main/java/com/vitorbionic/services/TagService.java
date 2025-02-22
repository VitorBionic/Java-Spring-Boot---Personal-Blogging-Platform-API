package com.vitorbionic.services;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vitorbionic.domain.Tag;
import com.vitorbionic.domain.dto.TagDTO;
import com.vitorbionic.exceptions.InvalidRequestException;
import com.vitorbionic.exceptions.RequiredObjectIsNullException;
import com.vitorbionic.exceptions.ResourceNotFoundException;
import com.vitorbionic.repositories.TagRepository;

@Service
public class TagService {
    
    @Autowired
    private TagRepository repository;
    
    private Logger logger = Logger.getLogger(TagService.class.getName());
    
    public List<TagDTO> findAll() {
        
        logger.info("Finding all tags!");
        
        return repository.findAll().stream().map(tag -> new TagDTO(tag.getId(), tag.getDescription()))
                .collect(Collectors.toList());
    }
    
    public TagDTO findById(Long id) {
        
        logger.info("Finding one Tag!");
        
        Tag entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        
        return new TagDTO(entity.getId(), entity.getDescription());
    }
    
    public TagDTO create(TagDTO dto) {
        
        if (dto == null)
            throw new RequiredObjectIsNullException();
        
        logger.info("Creating one Tag!");
        
        Tag entity = repository.save(new Tag(null, dto.description()));
        
        return new TagDTO(entity.getId(), entity.getDescription());
    }
    
    public TagDTO update(TagDTO dto) {
        
        if (dto == null)
            throw new RequiredObjectIsNullException();
        if (dto.id() == null)
            throw new InvalidRequestException("Cannot update a Tag without its ID!");
        
        logger.info("Updating one Tag!");
        
        Tag entity = repository.findById(dto.id())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        
        entity.setDescription(dto.description());
        entity = repository.save(entity);
        
        return new TagDTO(entity.getId(), entity.getDescription());
    }
    
    public void delete(Long id) {
        
        logger.info("Deleting one tag!");
        
        Tag entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        
        repository.delete(entity);
    }
}
