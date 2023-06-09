package com.vodafone.SpringBootDemo.contoller;

import com.vodafone.SpringBootDemo.model.Article;
import com.vodafone.SpringBootDemo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/v2")
public class ArticleControllerV2 {

    @Autowired
    private ArticleService articleService;

    @GetMapping(value = "/articles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Article>> getArticles(@RequestParam(name = "author", required = false) String author) {
        List<Article> articles = new ArrayList<>();
        if (author != null) {
//            articles = articleService.getArticlesByAuthorName(author, 0, 0);
        } else {
//            articles = articleService.getAllArticles(0,100);
        }
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping(value = "/articles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> getArticle(@PathVariable(name = "id") Integer id) {
        Article article = articleService.getArticleById(id);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @PostMapping(value = "/articles", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> addArticle(@RequestBody Article article) {
        article = articleService.addArticle(article);
        return new ResponseEntity<>(article, HttpStatus.CREATED);
    }
    @PutMapping(value = "/articles/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> updateArticle(@PathVariable(name = "id") Integer id,@RequestBody Article article) {
        article = articleService.updateArticle(id,article);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }
    @DeleteMapping(value = "/articles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> deleteArticle(@PathVariable(name = "id") Integer id) {
        articleService.deleteArticle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
