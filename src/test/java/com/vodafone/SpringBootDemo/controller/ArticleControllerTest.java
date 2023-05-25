package com.vodafone.SpringBootDemo.controller;

import com.vodafone.SpringBootDemo.contoller.ArticleController;
import com.vodafone.SpringBootDemo.errorhandlling.DuplicateEntryException;
import com.vodafone.SpringBootDemo.errorhandlling.NotFoundException;
import com.vodafone.SpringBootDemo.model.Article;
import com.vodafone.SpringBootDemo.service.ArticleService;
import com.vodafone.SpringBootDemo.util.ArticleFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
//@ActiveProfiles("test")
@SpringBootTest
public class ArticleControllerTest {
    private ArticleController articleController;

    @Mock
    private ArticleService articleService;

    @BeforeEach
    public void setUp(){
        articleController = new ArticleController(articleService);
    }

    @Test
    public void getArticlesTest_SendAuthorName_ReturnResponseEntityArticleList() {
        // Arrange
        String authorName = "ahmed";
        List<Article> articles = new ArrayList<>();
        articles.add(ArticleFactory.createArticle(1, "ahmed", "ahmed", 1));
        articles.add(ArticleFactory.createArticle(2, "mohamed", "ahmed", 1));
        articles.add(ArticleFactory.createArticle(3, "saad", "ahmed", 1));
        when(articleService.getArticlesByAuthorName(authorName, null, null)).thenReturn(articles);
        // Act
        ResponseEntity<List<Article>> result = articleController.getArticles(authorName, null, null);
        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(3, result.getBody().size());
        assertEquals(articles.get(0), result.getBody().get(0));
        assertEquals(articles.get(1), result.getBody().get(1));
        assertEquals(articles.get(2), result.getBody().get(2));
    }

    @Test
    public void getArticleTest_SendId_ReturnResponseEntityArticle() {
        // Arrange
        Article article = ArticleFactory.createArticle(1, "ahmed", "ahmed", 1);
        when(articleService.getArticleById(article.getId())).thenReturn(article);
        // Act
        ResponseEntity<Article> result = articleController.getArticle(article.getId());
        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(article, result.getBody());
    }

    @Test
    public void getArticleTest_SendInvalidId_ThrowNotFoundException() {
        // Arrange
        Integer id = 1;
        when(articleService.getArticleById(1)).thenThrow(NotFoundException.class);
        // Act
        // Assert
        assertThrows(NotFoundException.class, ()->{articleController.getArticle(id);});
        // TODO: check for response here or create test for exception handler?
        // assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void addArticleTest_SendNewArticle_ReturnResponseEntityArticle() {
        // Arrange
        Article article = ArticleFactory.createArticle("Ahmed", "Ahmed", 0);
        when(articleService.addArticle(article)).thenReturn(article);
        // Act
        ResponseEntity<Article> result = articleController.addArticle(article);
        article.setId(result.getBody().getId());
        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(article, result.getBody());
//         assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void addArticleTest_SendDuplicateArticle_ThrowDuplicateEntryException() {
        // Arrange
        Article article = ArticleFactory.createArticle("", "", 1);
        when(articleService.addArticle(article)).thenThrow(DuplicateEntryException.class);
        // Act
        // Assert
        assertThrows(DuplicateEntryException.class, ()->{articleController.addArticle(article);});
//         assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    public void updateArticleTest_SendValidId_ReturnResponseEntityArticle() {
        // Arrange
        Article article = ArticleFactory.createArticle("Ahmed", "Ahmed", 1);
        Integer id = 1;
        when(articleService.updateArticle(id, article)).thenReturn(
                ArticleFactory.createArticle(id, article.getName(), article.getAuthor(), article.getAuthorId()));
        // Act
        ResponseEntity<Article> result = articleController.updateArticle(id, article);
        article.setId(id);
        // Assert
         assertEquals(HttpStatus.OK, result.getStatusCode());
         assertEquals(article, result.getBody());
    }

    @Test
    public void updateArticleTest_SendInvalidId_ThrowNotFoundException() {
        // Arrange
        Article article = ArticleFactory.createArticle("","",1);
        Integer id = 1;
        when(articleService.updateArticle(id, article)).thenThrow(NotFoundException.class);
        // Act
        // Assert
        assertThrows(NotFoundException.class, ()->{articleController.updateArticle(id, article);});
//         assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    public void deleteArticleTest_SendValidId_ReturnResponseEntity() {
        // Arrange
        Integer id = 1;
        doNothing().when(articleService).deleteArticle(id);
        // Act
        ResponseEntity<Article> result = articleController.deleteArticle(id);
        // Assert
         assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
         assertNull(result.getBody());
    }

    @Test
    public void deleteArticleTest_SendInvalidId_ThrowNotFoundException() {
        // Arrange
        Integer id = 1;
        doThrow(NotFoundException.class).when(articleService).deleteArticle(id);
        // Act
        // Assert
        assertThrows(NotFoundException.class, ()->{articleController.deleteArticle(id);});
//         assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

}
