package com.vodafone.SpringBootDemo.service;

import com.vodafone.SpringBootDemo.util.ArticleFactory;
import com.vodafone.SpringBootDemo.util.PageUtil;
import com.vodafone.SpringBootDemo.errorhandlling.DuplicateEntryException;
import com.vodafone.SpringBootDemo.errorhandlling.NotFoundException;
import com.vodafone.SpringBootDemo.model.Article;
import com.vodafone.SpringBootDemo.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
//@ActiveProfiles("test")
@SpringBootTest
public class ArticleServiceImplV2Test {

    /*
        Either InjectMocks on class (not interface)

        or BeforeEach/All setup
     */
    @InjectMocks
    private ArticleServiceImplV2 articleService;

    @Mock
    private ArticleRepository articleRepository;

//    @BeforeEach
//    public void setUp(){
//        articleService = new ArticleServiceImplV2(articleRepository);
//    }

    @Test
    public void getAllArticlesTest_SendValidPageSize_ReturnArticleList() {
        // Arrange
        Integer page = Integer.valueOf(0);
        Integer size = Integer.valueOf(10);
        when(articleRepository.findAll(PageRequest.of(page,size))).thenReturn(Page.empty());
        // Act
        List<Article> result = articleService.getAllArticles(page, size);
        // Assert
        // TODO: assert what?
        assertTrue(result.size() == 0);
    }

    @Test
    public void getAllArticlesTest_SendInvalidPageSize_ReturnArticleList() {
        // Arrange
        Integer page = Integer.valueOf(-5);
        Integer size = Integer.valueOf(0);

        // Act
        when(articleRepository.findAll(PageRequest.of(PageUtil.validPage(page), PageUtil.validSize(size))))
                .thenReturn(Page.empty());
        List<Article> result = articleService.getAllArticles(page, size);
        // Assert
        // TODO: assert what?
        assertTrue(result.size() == 0);
    }

    @Test
    public void getArticleByIdTest_SendValidId_ReturnArticle() {
        // Arrange
        Integer id = Integer.valueOf(1);
        // Act
        when(articleRepository.findById(id)).thenReturn(Optional.of(new Article()));
        Article result = articleService.getArticleById(id);
        // Assert
        // TODO: assert what?
        assertTrue(result != null);
    }

    @Test
    public void getArticleByIdTest_SendInValidId_ThrowNotFoundException() {
        // Arrange
        Integer id = Integer.valueOf(4);
        // Act
        when(articleRepository.findById(id)).thenReturn(Optional.empty());
        // Assert
        assertThrows(NotFoundException.class,()->{articleService.getArticleById(id);});
    }

    @Test
    public void getArticlesByAuthorTest_SendValidName_ReturnArticleList() {
        // Arrange
        String authorName = "Ahmed";
        Integer page = 0;
        Integer size = 10;
        // Act
        when(articleRepository.findByAuthor(authorName, PageRequest.of(page,size))).thenReturn(Page.empty());
        List<Article> result = articleService.getArticlesByAuthorName(authorName, page, size);
        // Assert
        // TODO: assert what?
        assertTrue(result.size() == 0);
    }

    @Test
    public void getArticlesByAuthorTest_SendInvalidName_ReturnEmptyList() {
        // Arrange
        String authorName = null;
        Integer page = Integer.valueOf(0);
        Integer size = Integer.valueOf(10);
        // Act
        when(articleRepository.findByAuthor(authorName, PageRequest.of(page, size))).thenReturn(Page.empty());
        List<Article> result = articleService.getArticlesByAuthorName(authorName, page, size);
        // Assert
        assertTrue(result.size() == 0);

    }

    @Test
    public void addArticleTest_SendNewArticle_ReturnArticle() {
        // Arrange
        Article article = ArticleFactory.createArticle("book", "ahmed", 1);
        when(articleRepository.save(article)).thenReturn(ArticleFactory.createArticle(1, "book", "ahmed", 1));
        // Act
        Article result = articleService.addArticle(article);
        article.setId(1);
        // Assert
        assertEquals(article, result);
    }

    @Test
    public void addArticleTest_SendDuplicateArticle_ReturnDuplicateEntryException() {
        // Arrange
        Article article = ArticleFactory.createArticle("book", "ahmed", 1);

        // Act
        when(articleRepository.findByName(article.getName())).thenReturn(Optional.of(article));
        // Assert
        assertThrows(DuplicateEntryException.class,()->{articleService.addArticle(article);});

    }

    @Test
    public void deleteArticleTest_SendValidId_Return() {
        // Arrange
        Integer id = Integer.valueOf(1);
        // Act
        when(articleRepository.findById(id)).thenReturn(Optional.of(new Article()));
        // Assert
        assertDoesNotThrow(()->{articleService.deleteArticle(id);});
    }

    @Test
    public void deleteArticleTest_SendInvalidId_ThrowNotFoundException() {
        // Arrange
        Integer id = 1;
        // Act
        when(articleRepository.findById(id)).thenReturn(Optional.empty());
        // Assert
        assertThrows(NotFoundException.class, ()->{articleService.deleteArticle(id);});
    }

    @Test
    public void updateArticleTest_SendValidId_Return() {
        // Arrange
        Integer id = Integer.valueOf(1);
        Article article = ArticleFactory.createArticle("Ahmed", "Ahmed", 1);
        // Act
        when(articleRepository.findById(id)).thenReturn(Optional.of(
                ArticleFactory.createArticle(id, "", "", 1)));
        when(articleRepository.save(article)).thenReturn(
                ArticleFactory.createArticle(id, article.getName(), article.getAuthor(), article.getAuthorId()));
        // Assert
        assertDoesNotThrow(()->{articleService.updateArticle(id, article);});
    }

    @Test
    public void updateArticleTest_SendInvalidId_ThrowNotFoundException() {
        // Arrange
        Integer id = 1;
        Article article = ArticleFactory.createArticle("book", "name", 1);
        // Act
        when(articleRepository.findById(id)).thenReturn(Optional.empty());
        // Assert
        assertThrows(NotFoundException.class, ()->{articleService.updateArticle(id, article);});
    }

}
