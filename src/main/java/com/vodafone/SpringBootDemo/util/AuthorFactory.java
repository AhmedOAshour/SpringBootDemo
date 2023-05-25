package com.vodafone.SpringBootDemo.util;

import com.vodafone.SpringBootDemo.model.Author;
import com.vodafone.SpringBootDemo.model.Author;

public class AuthorFactory {
    public static Author createAuthor(Integer id, String name) {
        Author author = new Author();
        author.setId(id);
        author.setName(name);
        return author;
    }

    public static Author createAuthor(String name) {
        Author author = new Author();
        author.setName(name);
        return author;
    }
}
