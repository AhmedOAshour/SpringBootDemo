package com.vodafone.SpringBootDemo.service;

import com.vodafone.SpringBootDemo.PageUtil;
import com.vodafone.SpringBootDemo.contoller.ArticleController;
import com.vodafone.SpringBootDemo.contoller.AuthorController;
import com.vodafone.SpringBootDemo.errorhandlling.DuplicateEntryException;
import com.vodafone.SpringBootDemo.errorhandlling.NotFoundException;
import com.vodafone.SpringBootDemo.model.Article;
import com.vodafone.SpringBootDemo.model.Links;
import com.vodafone.SpringBootDemo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
@Primary
public class ArticleServiceImplV2 implements ArticleService{
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImplV2(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> getAllArticles(Integer page, Integer size) {
        page = PageUtil.validPage(page);
        size = PageUtil.validSize(size);
        Pageable pageable = PageRequest.of(page, size);
        List<Article> articles = articleRepository.findAll(pageable).getContent();
        for (Article article :
                articles) {
            addLinks(article);
        }
        return articles;
    }

    @Override
    public Article getArticleById(Integer id) {
        Article article = articleRepository.findById(id).map(this::addLinks).orElse(null);
        if (article==null)
            throw new NotFoundException("Article with that id doesn't exist");
        return article;
    }

    @Override
    public List<Article> getArticlesByAuthorName(String authorName, Integer page, Integer size) {
        page = PageUtil.validPage(page);
        size = PageUtil.validSize(size);
        Pageable pageable = PageRequest.of(page,size);
        Page<Article> articles = articleRepository.findByAuthor(authorName, pageable);
        for (Article article :
                articles) {
            addLinks(article);
        }
        return articles.getContent();
    }

    @Override
    public Article addArticle(Article article) {
        if (articleRepository.findByName(article.getName()).isPresent()) {
            throw new DuplicateEntryException("Article with that name already exists.");
        }
        articleRepository.save(article);
        return addLinks(articleRepository.save(article));
    }

    @Override
    public void deleteArticle(Integer id) {
        if (articleRepository.findById(id).isEmpty()) {
            System.out.println("Article not found");
            throw new NotFoundException("Article not found.");
        }
        articleRepository.deleteById(id);
    }

    @Override
    public Article updateArticle(Integer id, Article article) {
        if (articleRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Article not found.");
        }
        article.setId(id);
        return addLinks(articleRepository.save(article));
    }

    @Override
    public Article addLinks(Article article){
        List<Links> links = new ArrayList<>();
        Links self = new Links();

        Link selfLink = linkTo(methodOn(ArticleController.class)
                .getArticle(article.getId())).withRel("self");

        self.setRel("self");
        self.setHref(selfLink.getHref());

        Links authorLink = new Links();
        Link authLink = linkTo(methodOn(AuthorController.class)
                .getAuthorById(article.getAuthorId())).withRel("author");
        authorLink.setRel("author");
        authorLink.setHref(authLink.getHref());

        links.add(self);
        links.add(authorLink);
        article.setLinks(links);
        return article;
    }
}
