package com.vitorbionic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vitorbionic.domain.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

}
