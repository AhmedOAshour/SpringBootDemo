package com.vodafone.SpringBootDemo.util;

import com.vodafone.SpringBootDemo.model.Article;

public class ArticleFactory {
    public static Article createArticle(String name, String author, Integer articleId) {
        Article article = new Article();
        article.setName(name);
        article.setAuthor(author);
        article.setAuthorId(articleId);
        return article;
    }
    public static Article createArticle(Integer id, String name, String author, Integer articleId) {
        Article article = new Article();
        article.setId(id);
        article.setName(name);
        article.setAuthor(author);
        article.setAuthorId(articleId);
        return article;
    }
}
