package com.vitorbionic.domain.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public record ArticleDTO(Long id, String title, String content, Instant publicationDate, List<TagDTO> tags) implements Serializable {

}
