package com.vitorbionic.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Table(name = "article")
@Entity
public class Article implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 80)
    private String title;
    
    @Column(nullable = false)
    private String content;
    
    @Column(name = "publication_date", nullable = false)
    private Instant publicationDate;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "article_tag", joinColumns = {@JoinColumn (name = "id_article")},
            inverseJoinColumns = {@JoinColumn (name = "id_tag")})
    private List<Tag> tags;
    
    public Article() {}

    public Article(Long id, String title, String content, Instant publicationDate, List<Tag> tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.publicationDate = publicationDate;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Instant publicationDate) {
        this.publicationDate = publicationDate;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, id, publicationDate, tags, title);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Article other = (Article) obj;
        return Objects.equals(content, other.content) && Objects.equals(id, other.id)
                && Objects.equals(publicationDate, other.publicationDate) && Objects.equals(tags, other.tags)
                && Objects.equals(title, other.title);
    }

}
