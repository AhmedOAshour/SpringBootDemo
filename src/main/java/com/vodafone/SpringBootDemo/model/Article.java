package com.vodafone.SpringBootDemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="Article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String author;
    private int authorId;
    @JsonProperty("_links")
    @Transient
    private List<Links> links;

    @Override
    public String toString() {
        return "Article [id=" + id + ", name=" + name + ", author=" + author + ", authorId=" + authorId + ", links="
                + links + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Links> getLinks() {
        return links;
    }

    public void setLinks(List<Links> links) {
        this.links = links;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return authorId == article.authorId && Objects.equals(id, article.id) && Objects.equals(name, article.name) && Objects.equals(author, article.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, author, authorId);
    }
}
