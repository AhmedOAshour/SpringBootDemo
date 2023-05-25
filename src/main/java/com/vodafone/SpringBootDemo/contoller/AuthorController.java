package com.vodafone.SpringBootDemo.contoller;

import com.vodafone.SpringBootDemo.model.Author;
import com.vodafone.SpringBootDemo.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
public class AuthorController {

    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Author>> getAuthors(@RequestParam(name = "page", required = false) Integer page,
                                                   @RequestParam(name = "size", required = false) Integer size){
        return ResponseEntity.ok(authorService.getAllAuthors(page, size));
    }

    @GetMapping(value = "/authors/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> getAuthorById(@PathVariable(name = "id") Integer id){
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PostMapping(value = "authors", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> addAuthor(@RequestBody Author author){
        return new ResponseEntity(authorService.addAuthor(author), HttpStatus.CREATED);
    }

    @PutMapping(value = "/authors/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> updateAuthor(@PathVariable(name = "id") Integer id, @RequestBody Author author) {
        return new ResponseEntity<Author>(authorService.updateAuthor(id, author), HttpStatus.OK);
    }

    @DeleteMapping(value = "/authors/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> deleteAuthor(@PathVariable(name = "id") Integer id) {
        authorService.deleteAuthor(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
