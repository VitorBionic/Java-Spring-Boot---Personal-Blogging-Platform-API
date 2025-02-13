package com.vitorbionic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vitorbionic.domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

}
