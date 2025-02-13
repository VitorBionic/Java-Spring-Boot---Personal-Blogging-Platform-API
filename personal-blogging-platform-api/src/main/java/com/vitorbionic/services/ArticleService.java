package com.vitorbionic.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vitorbionic.domain.Article;
import com.vitorbionic.domain.Tag;
import com.vitorbionic.domain.dto.ArticleDTO;
import com.vitorbionic.domain.dto.TagDTO;
import com.vitorbionic.repositories.ArticleRepository;

@Service
public class ArticleService {
    
    @Autowired
    private ArticleRepository repository;
    
    private Logger logger = Logger.getLogger(ArticleService.class.getName());
    
    private static ArticleDTO convertToDto(Article article) {
        List<TagDTO> listTagDto = new ArrayList<>();
        
        for (Tag tag : article.getTags())
            listTagDto.add(new TagDTO(tag.getId(), tag.getDescription()));
        
        return new ArticleDTO(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getPublicationDate(),
                listTagDto);
    }
    
    private static List<ArticleDTO> convertToDtoList(List<Article> articles) {
        List<ArticleDTO> dtos = new ArrayList<>();
        for (Article article : articles)
            dtos.add(convertToDto(article));
        return dtos;
    }
    
    public List<ArticleDTO> findAll() {
        
        logger.info("Finding all people!");
        
        return convertToDtoList(repository.findAll());
    }
}
