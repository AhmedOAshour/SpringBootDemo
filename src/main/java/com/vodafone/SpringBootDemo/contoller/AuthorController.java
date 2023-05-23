package com.vodafone.SpringBootDemo.contoller;

import com.vodafone.SpringBootDemo.model.Author;
import com.vodafone.SpringBootDemo.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping(value = "/authors", produces = {"application/json"})
    public ResponseEntity<List<Author>> getAuthors(){
        return ResponseEntity.ok(authorService.getAllAuthors(0, 0));
    }

    @GetMapping(value = "/authors/{id}", produces = {"application/json"})
    public ResponseEntity<Author> getAuthorById(@PathVariable(name = "id") Integer id){
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PostMapping(value = "authors", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Author> addAuthor(@RequestBody Author author){
        return ResponseEntity.ok(authorService.addAuthor(author));
    }

    @PutMapping(value = "/authors/{id}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Author> updateAuthor(@PathVariable(name = "id") Integer id, @RequestBody Author author) {
        return new ResponseEntity<Author>(authorService.updateAuthor(id, author), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/authors/{id}", produces = {"application/json"})
    public ResponseEntity<Author> deleteAuthor(@PathVariable(name = "id") Integer id) {
        authorService.deleteAuthor(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
