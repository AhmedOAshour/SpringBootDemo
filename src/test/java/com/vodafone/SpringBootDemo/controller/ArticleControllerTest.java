package com.vodafone.SpringBootDemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
//@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ArticleControllerTest {

    @MockBean
    private ArticleService articleService;

    @Autowired
    private MockMvc mockMvc;

    private String asJsonString(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    @Test
    public void getArticlesTest_SendPageSize_ReturnArticleListOk() throws Exception {
        // Arrange
        Integer page = 0;
        Integer size = 1;
        List<Article> articles = new ArrayList<>();
        articles.add(ArticleFactory.createArticle(1, "ahmed", "ahmed", 1));
        articles.add(ArticleFactory.createArticle(2, "mohamed", "ahmed", 1));
        articles.add(ArticleFactory.createArticle(3, "saad", "ahmed", 1));
        when(articleService.getAllArticles( page, size)).thenReturn(articles);
        // Act
        // Assert

        mockMvc.perform(get(String.format("/v1/articles?page=%d&size=%d", page, size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    public void getArticlesTest_SendAuthorName_ReturnArticleListOk() throws Exception {
        // Arrange
        String authorName = "ahmed";
        List<Article> articles = new ArrayList<>();
        articles.add(ArticleFactory.createArticle(1, "ahmed", "ahmed", 1));
        articles.add(ArticleFactory.createArticle(2, "mohamed", "ahmed", 1));
        articles.add(ArticleFactory.createArticle(3, "saad", "ahmed", 1));
        when(articleService.getArticlesByAuthorName("ahmed", null, null)).thenReturn(articles);
        // Act
        // Assert

        mockMvc.perform(get("/v1/articles?author=" + authorName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    public void getArticleTest_SendId_ReturnArticleOk() throws Exception {
        // Arrange
        Integer id = 1;
        Article article = ArticleFactory.createArticle(id, "ahmed", "ahmed", 1);
        when(articleService.getArticleById(article.getId())).thenReturn(article);
        // Act
        // Assert
        mockMvc.perform(get("/v1/articles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    public void getArticleTest_SendInvalidId_ThrowNotFoundException() throws Exception {
        // Arrange
        Integer id = 1;
        when(articleService.getArticleById(1)).thenThrow(NotFoundException.class);
        // Act
        // Assert
        mockMvc.perform(get("/v1/articles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("Not Found"));
    }

    @Test
    public void addArticleTest_SendNewArticle_ReturnResponseEntityArticle() throws Exception {
        // Arrange
        Article article = ArticleFactory.createArticle("Ahmed", "Ahmed", 0);
        when(articleService.addArticle(article)).thenReturn(article);
        // Act
        // Assert
        mockMvc.perform(post("/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(article)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(article.getName()));
    }

    @Test
    public void addArticleTest_SendDuplicateArticle_ThrowDuplicateEntryException() throws Exception {
        // Arrange
        Article article = ArticleFactory.createArticle("", "", 1);
        when(articleService.addArticle(article)).thenThrow(DuplicateEntryException.class);
        // Act
        // Assert
        mockMvc.perform(post("/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(article)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("Conflict"));
    }

    @Test
    public void updateArticleTest_SendValidId_ReturnResponseEntityArticle() throws Exception {
        // Arrange
        Article article = ArticleFactory.createArticle("Ahmed", "Ahmed", 1);
        Integer id = 1;
        when(articleService.updateArticle(id, article)).thenReturn(
                ArticleFactory.createArticle(id, article.getName(), article.getAuthor(), article.getAuthorId()));
        // Act
        // Assert
        mockMvc.perform(put("/v1/articles/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(article)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(article.getName()));
    }

    @Test
    public void updateArticleTest_SendInvalidId_ThrowNotFoundException() throws Exception {
        // Arrange
        Article article = ArticleFactory.createArticle("","",1);
        Integer id = 1;
        when(articleService.updateArticle(id, article)).thenThrow(NotFoundException.class);
        // Act
        // Assert
        mockMvc.perform(put("/v1/articles/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(article)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("Not Found"));
    }

    @Test
    public void deleteArticleTest_SendValidId_ReturnResponseEntity() throws Exception {
        // Arrange
        Integer id = 1;
        doNothing().when(articleService).deleteArticle(id);
        // Act
        // Assert
        mockMvc.perform(delete("/v1/articles/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteArticleTest_SendInvalidId_ThrowNotFoundException() throws Exception {
        // Arrange
        Integer id = 1;
        doThrow(NotFoundException.class).when(articleService).deleteArticle(id);
        // Act
        // Assert
        mockMvc.perform(delete("/v1/articles/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("Not Found"));
    }

}
