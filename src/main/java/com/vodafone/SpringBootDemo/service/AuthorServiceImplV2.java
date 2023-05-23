package com.vodafone.SpringBootDemo.service;

import com.vodafone.SpringBootDemo.contoller.AuthorController;
import com.vodafone.SpringBootDemo.model.Author;
import com.vodafone.SpringBootDemo.model.Links;
import com.vodafone.SpringBootDemo.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
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
public class AuthorServiceImplV2 implements AuthorService{
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImplV2(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author getAuthorById(Integer id) {
        return authorRepository.findById(id).map(this::addLinks).orElse(null);
    }

    @Override
    public List<Author> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        for (Author author :
                authors) {
            addLinks(author);
        }
        return authors;
    }

    @Override
    public Author addAuthor(Author author) {
        return addLinks(authorRepository.save(author));
    }

    @Override
    public Author updateAuthor(Integer id, Author author) {
        author.setId(id);
        return addLinks(authorRepository.save(author));
    }

    @Override
    public void deleteAuthor(Integer id) {
        authorRepository.deleteById(id);
    }

    public Author addLinks(Author author){
        List<Links> links = new ArrayList<>();
        Links self = new Links();

        Link selfLink = linkTo(methodOn(AuthorController.class)
                .getAuthorById(author.getId())).withRel("self");

        self.setRel("self");
        self.setHref(selfLink.getHref());

        links.add(self);
        author.setLinks(links);
        return author;
    }
}
