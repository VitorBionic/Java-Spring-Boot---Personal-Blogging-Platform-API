package com.vitorbionic.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vitorbionic.domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    
    @Query("SELECT art FROM Article art WHERE art.publicationDate >= :publicationDate")
    List<Article> findArticlesWithFilter(@Param("publicationDate") Instant publicationDate);

}
