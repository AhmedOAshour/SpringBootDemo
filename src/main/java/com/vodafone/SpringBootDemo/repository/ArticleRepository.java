package com.vodafone.SpringBootDemo.repository;

import com.vodafone.SpringBootDemo.model.Article;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer>
{
    Page<Article> findByAuthor(String author, Pageable pageable);

    Optional<Article> findByName(String name);
}