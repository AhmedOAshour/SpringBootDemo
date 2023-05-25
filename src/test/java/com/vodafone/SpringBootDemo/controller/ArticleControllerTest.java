package com.vodafone.SpringBootDemo.controller;

import com.vodafone.SpringBootDemo.contoller.ArticleController;
import com.vodafone.SpringBootDemo.errorhandlling.DuplicateEntryException;
import com.vodafone.SpringBootDemo.errorhandlling.NotFoundException;
import com.vodafone.SpringBootDemo.model.Article;
import com.vodafone.SpringBootDemo.service.ArticleService;
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
//        Integer page = 0, size = 10;
        String authorName = "ahmed";
        // Act
        when(articleService.getArticlesByAuthorName(authorName, null, null)).thenReturn(new ArrayList<>());
        ResponseEntity<List<Article>> result = articleController.getArticles(authorName, null, null);
        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(0, result.getBody().size());
    }

    @Test
    public void getArticleTest_SendId_ReturnResponseEntityArticle() {
        // Arrange
        Integer id = 1;
        // Act
        when(articleService.getArticleById(id)).thenReturn(new Article(1, "",""));
        ResponseEntity<Article> result = articleController.getArticle(id);
        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(id, result.getBody().getId());
    }

    @Test
    public void getArticleTest_SendInvalidId_ThrowNotFoundException() {
        // Arrange
        Integer id = 1;
        // Act
        when(articleService.getArticleById(1)).thenThrow(NotFoundException.class);
        // Assert
        assertThrows(NotFoundException.class, ()->{articleController.getArticle(id);});
        // TODO: check for response here or create test for exception handler?
        // assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void addArticleTest_SendNewArticle_ReturnResponseEntityArticle() {
        // Arrange
        Article article = new Article();
        // Act
        when(articleService.addArticle(article)).thenReturn(new Article());
        ResponseEntity<Article> result = articleController.addArticle(article);
        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
//         assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void addArticleTest_SendDuplicateArticle_ThrowDuplicateEntryException() {
        // Arrange
        Article article = new Article();
        // Act
        when(articleService.addArticle(article)).thenThrow(DuplicateEntryException.class);
        // Assert
        assertThrows(DuplicateEntryException.class, ()->{articleController.addArticle(article);});
//         assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    public void updateArticleTest_SendValidId_ReturnResponseEntityArticle() {
        // Arrange
        Article article = new Article();
        Integer id = 1;
        // Act
        when(articleService.updateArticle(id, article)).thenReturn(new Article());
        ResponseEntity<Article> result = articleController.updateArticle(id, article);
        // Assert
         assertEquals(HttpStatus.OK, result.getStatusCode());
         assertInstanceOf(Article.class, result.getBody());
    }

    @Test
    public void updateArticleTest_SendInvalidId_ThrowNotFoundException() {
        // Arrange
        Article article = new Article();
        Integer id = 1;
        // Act
        when(articleService.addArticle(article)).thenThrow(NotFoundException.class);
        // Assert
        assertThrows(NotFoundException.class, ()->{articleController.addArticle(article);});
//         assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    public void deleteArticleTest_SendValidId_ReturnResponseEntity() {
        // Arrange
        Integer id = 1;
        // Act
        doNothing().when(articleService).deleteArticle(id);
        ResponseEntity<Article> result = articleController.deleteArticle(id);
        // Assert
         assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
         assertNull(result.getBody());
    }

    @Test
    public void deleteArticleTest_SendInvalidId_ThrowNotFoundException() {
        // Arrange
        Integer id = 1;
        // Act
        doThrow(NotFoundException.class).when(articleService).deleteArticle(id);
        // Assert
        assertThrows(NotFoundException.class, ()->{articleController.deleteArticle(id);});
//         assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

}
