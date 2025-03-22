package com.vitorbionic.services;

import java.time.Instant;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vitorbionic.domain.Article;
import com.vitorbionic.domain.Tag;
import com.vitorbionic.domain.dto.ArticleDTO;
import com.vitorbionic.domain.dto.TagDTO;
import com.vitorbionic.exceptions.InvalidRequestException;
import com.vitorbionic.exceptions.RequiredObjectIsNullException;
import com.vitorbionic.exceptions.ResourceNotFoundException;
import com.vitorbionic.repositories.ArticleRepository;
import com.vitorbionic.repositories.TagRepository;

@Service
public class ArticleService {
    
    @Autowired
    private ArticleRepository articleRepository;
    
    @Autowired
    private TagRepository tagRepository;
    
    private Logger logger = Logger.getLogger(ArticleService.class.getName());
    
    public List<ArticleDTO> findAll() {
        
        logger.info("Finding all articles!");
        
        return articleRepository.findAll().stream()
                .map(article -> new ArticleDTO(
                        article.getId(),
                        article.getTitle(), 
                        article.getContent(),
                        article.getPublicationDate(),
                        article.getTags().stream().map(tag -> new TagDTO(
                                tag.getId(),
                                tag.getDescription())
                                ).collect(Collectors.toList()))
                        ).collect(Collectors.toList());
    }
    
    public List<ArticleDTO> findArticlesWithFilter(String instant, String tags) {
        
        logger.info("Finding articles by filtering!");
        
        instant = formatInstant(instant);
        
        List<ArticleDTO> dtoListSemiFiltered = articleRepository.findArticlesWithFilter(Instant.parse(instant)).stream()
                .map(article -> new ArticleDTO(
                        article.getId(),
                        article.getTitle(), 
                        article.getContent(),
                        article.getPublicationDate(),
                        article.getTags().stream().map(tag -> new TagDTO(
                                tag.getId(),
                                tag.getDescription())
                                ).collect(Collectors.toList()))
                        ).collect(Collectors.toList());
        
        String[] tagsArr = tags.split(";");
        
        Predicate<ArticleDTO> filter = (dto) -> {
            if (tagsArr.length == 1 && tagsArr[0].isEmpty())
                return true;
            for (String tag : tagsArr) {
                boolean flag = false;
                for (TagDTO tagDto : dto.tags()) {
                    if (tag.equals(tagDto.description())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag)
                    return false;
            }
            return true;
        };
        
        return dtoListSemiFiltered.stream().filter(filter).collect(Collectors.toList());
    }

    public ArticleDTO findById(Long id) {
        
        logger.info("Finding one article!");
        
        Article entity = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        
        return new ArticleDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getPublicationDate(),
                entity.getTags().stream().map(tag -> new TagDTO(tag.getId(), tag.getDescription())).collect(Collectors.toList())
                );
    }
    
    public ArticleDTO create(ArticleDTO dto) {
        
        if (dto == null)
            throw new RequiredObjectIsNullException();
        for (TagDTO tag : dto.tags()) {
            if (tag.id() == null)
                throw new InvalidRequestException("Cannot pass a null id Tag to be associated to a Article");
            tagRepository.findById(tag.id())
            .orElseThrow(() -> new InvalidRequestException("Tag with id " + tag.id() + " does not exist!"));
        }
        
        logger.info("Creating one article!");
        
        Article entity = new Article(
                null,
                dto.title(),
                dto.content(),
                dto.publicationDate(),
                dto.tags().stream().map(tagDTO -> new Tag(tagDTO.id(), null)).collect(Collectors.toList())
                );
        
        entity = articleRepository.save(entity);
        entity = articleRepository.findById(entity.getId()).get();
        
        return new ArticleDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getPublicationDate(),
                entity.getTags().stream().map(tag -> new TagDTO(tag.getId(), tag.getDescription())).collect(Collectors.toList())
                );
    }
    
    public ArticleDTO update(ArticleDTO dto) {
        
        if (dto == null)
            throw new RequiredObjectIsNullException();
        if (dto.id() == null)
            throw new InvalidRequestException("Cannot update a Article without its ID!");
        for (TagDTO tag : dto.tags()) {
            if (tag.id() == null)
                throw new InvalidRequestException("Cannot pass a null ID Tag to be associated to a Article");
            tagRepository.findById(tag.id())
            .orElseThrow(() -> new InvalidRequestException("Tag with ID " + tag.id() + " does not exist!"));
        }
        
        logger.info("Updating one article!");
        
        Article entity = articleRepository.findById(dto.id())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        
        entity.setTitle(dto.title());
        entity.setContent(dto.content());
        entity.setPublicationDate(dto.publicationDate());
        entity.setTags(dto.tags().stream().map(tagDTO -> new Tag(tagDTO.id(), null)).collect(Collectors.toList()));
        entity = articleRepository.save(entity);
        entity = articleRepository.findById(entity.getId()).get();
        
        return new ArticleDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getPublicationDate(),
                entity.getTags().stream().map(tag -> new TagDTO(tag.getId(), tag.getDescription())).collect(Collectors.toList())
                );
    }
    
    public void delete(Long id) {
        
         logger.info("Deleting one article!");
         
         Article entity = articleRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
         
         articleRepository.delete(entity);
    }
    
    private String formatInstant(String instant) {
        if (instant.matches("^\\d{4}-\\d{2}-\\d{2}$"))
            instant += "T00:00:00.00Z";
        else if (instant.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$"))
            instant += ".00Z";
        else
            throw new InvalidRequestException("Invalid date format: " + instant);
        return instant;
    }
}
