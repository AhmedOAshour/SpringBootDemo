package com.vodafone.SpringBootDemo.contoller;

import com.vodafone.SpringBootDemo.model.Article;
import com.vodafone.SpringBootDemo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
public class ArticlesController {

    @Autowired
    private ArticleService articleService;

    /*
     * get all articles using http get method with a optional query parameter
     * "author" for getting all articles with this author or all articles if 
     * "author" is not provided in query parameter
     */
    @GetMapping(value = "/articles", produces = {"application/json"})
    public ResponseEntity<List<Article>> getArticles(@RequestParam(name = "author", required = false) String author,
                                                     @RequestParam(name = "page", required = false) Integer page,
                                                     @RequestParam(name = "size", required = false) Integer size) {
        List<Article> articles;
        if (author != null) {
            articles = articleService.getArticlesByAuthorName(author, page, size);
        } else {
            articles = articleService.getAllArticles(page, size);
        }
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    /*
     * get single article using id with http get method
     */
    @GetMapping(value = "/articles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> getArticle(@PathVariable(name = "id") Integer id) {
        Article article = articleService.getArticleById(id);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    /*
     * create new article using http post method
     */
    @PostMapping(value = "/articles", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Article> addArticle(@RequestBody Article article) {
        article = articleService.addArticle(article);
        return new ResponseEntity<>(article, HttpStatus.CREATED);
    }

    /*
     * update article using http put method
     */
    @PutMapping(value = "/articles/{id}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Article> updateArticle(@PathVariable(name = "id") Integer id,@RequestBody Article article) {
        article = articleService.updateArticle(id,article);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    /*
     * delete article using http delete method
     */
    @DeleteMapping(value = "/articles/{id}", produces = {"application/json"})
    public ResponseEntity<Article> deleteArticle(@PathVariable(name = "id") Integer id) {
        articleService.deleteArticle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
