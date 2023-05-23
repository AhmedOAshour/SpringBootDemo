package com.vodafone.SpringBootDemo.service;

import com.vodafone.SpringBootDemo.model.Article;

import java.util.List;

public interface ArticleService {
    List<Article> getAllArticles(Integer page, Integer size);
    Article getArticleById(Integer id);
    List<Article> getArticlesByAuthorName(String authorName, Integer page, Integer size);
    Article addArticle(Article article);

    void deleteArticle(Integer id);

    Article updateArticle(Integer id, Article article);

    Article addLinks(Article article);
}
