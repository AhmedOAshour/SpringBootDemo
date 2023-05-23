package com.vodafone.SpringBootDemo.service;

import com.vodafone.SpringBootDemo.model.Author;

import java.util.List;

public interface AuthorService {
    Author getAuthorById(Integer id);

    List<Author> getAllAuthors();

    Author addAuthor(Author author);

    Author updateAuthor(Integer id, Author author);

    void deleteAuthor(Integer id);

    Author addLinks(Author author);
}
