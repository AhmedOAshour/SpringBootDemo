package com.vodafone.SpringBootDemo.service;

import com.vodafone.SpringBootDemo.contoller.AuthorController;
import com.vodafone.SpringBootDemo.model.Author;
import com.vodafone.SpringBootDemo.model.Links;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class AuthorServiceImpl implements AuthorService {
    List<Author> authors = new ArrayList<>();
    Integer id = 0;

    public AuthorServiceImpl() {
        Author author = new Author();
        author.setId(1);
        author.setName("ahmed");
        authors.add(author);
    }

    @Override
    public Author getAuthorById(Integer id) {
        for (Author author : authors) {
            if (id.equals(author.getId())) {
                return author;
            }
        }
        return null;
    }

    @Override
    public List<Author> getAllAuthors(Integer page, Integer size) {
        return authors;
    }

    @Override
    public Author addAuthor(Author author) {
        authors.add(author);
        return authors.get(authors.size()-1);
    }

    @Override
    public Author updateAuthor(Integer id, Author author) {
        for (Author author1 :
                authors) {
            if (author1.getId() == author.getId()) {
                author.setId(id);
                authors.remove(author1);
                authors.add(author);
            }
        }
        return authors.get(authors.size()-1);
    }

    @Override
    public void deleteAuthor(Integer id) {
        for (Author author1 :
                authors) {
            if (author1.getId() == id) {
                authors.remove(author1);
            }
        }
    }

    @Override
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
