package com.vodafone.SpringBootDemo.controller;

import com.vodafone.SpringBootDemo.contoller.AuthorController;
import com.vodafone.SpringBootDemo.errorhandlling.DuplicateEntryException;
import com.vodafone.SpringBootDemo.errorhandlling.NotFoundException;
import com.vodafone.SpringBootDemo.model.Author;
import com.vodafone.SpringBootDemo.service.AuthorService;
import com.vodafone.SpringBootDemo.util.AuthorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
//@ActiveProfiles("test")
@SpringBootTest
public class AuthorControllerTest {
    private AuthorController authorController;

    @Mock
    private AuthorService authorService;

    @BeforeEach
    public void setUp() {
        authorController = new AuthorController(authorService);
    }

    @Test
    public void getAuthorsTest_SendPageSize_ReturnResponseEntityAuthorList() {
        // Arrange
        Integer page = 0, size = 10;
        // Act
        when(authorService.getAllAuthors(page, size)).thenReturn(new ArrayList<>());
        ResponseEntity<List<Author>> result = authorController.getAuthors(page, size);
        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(0, result.getBody().size());
    }

    @Test
    public void getAuthorTest_SendId_ReturnResponseEntityAuthor() {
        // Arrange
        Integer id = 1;
        // Act
        when(authorService.getAuthorById(id)).thenReturn(AuthorFactory.createAuthor(id, "ahmed"));
        ResponseEntity<Author> result = authorController.getAuthorById(id);
        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(id, result.getBody().getId());
    }

    @Test
    public void getAuthorTest_SendInvalidId_ThrowNotFoundException() {
        // Arrange
        Integer id = 1;
        // Act
        when(authorService.getAuthorById(1)).thenThrow(NotFoundException.class);
        // Assert
        assertThrows(NotFoundException.class, () -> {
            authorController.getAuthorById(id);
        });
        // TODO: check for response here or create test for exception handler?
        // assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void addAuthorTest_SendNewAuthor_ReturnResponseEntityAuthor() {
        // Arrange
        Author author = new Author();
        // Act
        when(authorService.addAuthor(author)).thenReturn(new Author());
        ResponseEntity<Author> result = authorController.addAuthor(author);
        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
//         assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void addAuthorTest_SendDuplicateAuthor_ThrowDuplicateEntryException() {
        // Arrange
        Author author = new Author();
        // Act
        when(authorService.addAuthor(author)).thenThrow(DuplicateEntryException.class);
        // Assert
        assertThrows(DuplicateEntryException.class, () -> {
            authorController.addAuthor(author);
        });
//         assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    public void updateAuthorTest_SendValidId_ReturnResponseEntityAuthor() {
        // Arrange
        Author author = new Author();
        Integer id = 1;
        // Act
        when(authorService.updateAuthor(id, author)).thenReturn(new Author());
        ResponseEntity<Author> result = authorController.updateAuthor(id, author);
        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertInstanceOf(Author.class, result.getBody());
    }

    @Test
    public void updateAuthorTest_SendInvalidId_ThrowNotFoundException() {
        // Arrange
        Author author = new Author();
        Integer id = 1;
        // Act
        when(authorService.addAuthor(author)).thenThrow(NotFoundException.class);
        // Assert
        assertThrows(NotFoundException.class, () -> {
            authorController.addAuthor(author);
        });
//         assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    public void deleteAuthorTest_SendValidId_ReturnResponseEntity() {
        // Arrange
        Integer id = 1;
        // Act
        doNothing().when(authorService).deleteAuthor(id);
        ResponseEntity<Author> result = authorController.deleteAuthor(id);
        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    public void deleteAuthorTest_SendInvalidId_ThrowNotFoundException() {
        // Arrange
        Integer id = 1;
        // Act
        doThrow(NotFoundException.class).when(authorService).deleteAuthor(id);
        // Assert
        assertThrows(NotFoundException.class, () -> {
            authorController.deleteAuthor(id);
        });
//         assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }
}