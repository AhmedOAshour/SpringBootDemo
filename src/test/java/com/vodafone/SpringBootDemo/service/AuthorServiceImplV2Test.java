package com.vodafone.SpringBootDemo.service;

import com.vodafone.SpringBootDemo.util.PageUtil;
import com.vodafone.SpringBootDemo.errorhandlling.DuplicateEntryException;
import com.vodafone.SpringBootDemo.errorhandlling.NotFoundException;
import com.vodafone.SpringBootDemo.model.Author;
import com.vodafone.SpringBootDemo.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
//@ActiveProfiles("test")
@SpringBootTest
public class AuthorServiceImplV2Test {
    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;

    @BeforeEach
    public void setUp(){
        authorService = new AuthorServiceImplV2(authorRepository);
    }

    @Test
    public void getAllAuthorsTest_SendValidPageSize_ReturnAuthorList() {
        // Arrange
        Integer page = Integer.valueOf(0);
        Integer size = Integer.valueOf(10);
        // Act
        when(authorRepository.findAll(PageRequest.of(page,size))).thenReturn(Page.empty());
        List<Author> result = authorService.getAllAuthors(page, size);
        // Assert
        // TODO: assert what?
        assertTrue(result.size() == 0);
    }

    @Test
    public void getAllAuthorsTest_SendInvalidPageSize_ReturnAuthorList() {
        // Arrange
        Integer page = Integer.valueOf(-5);
        Integer size = Integer.valueOf(0);

        // Act
        when(authorRepository.findAll(PageRequest.of(PageUtil.validPage(page), PageUtil.validSize(size))))
                .thenReturn(Page.empty());
        List<Author> result = authorService.getAllAuthors(page, size);
        // Assert
        // TODO: assert what?
        assertTrue(result.size() == 0);
    }

    @Test
    public void getAuthorByIdTest_SendValidId_ReturnAuthor() {
        // Arrange
        Integer id = Integer.valueOf(1);
        // Act
        when(authorRepository.findById(id)).thenReturn(Optional.of(new Author()));
        Author result = authorService.getAuthorById(id);
        // Assert
        // TODO: assert what?
        assertTrue(result != null);
    }

    @Test
    public void getAuthorByIdTest_SendInValidId_ThrowNotFoundException() {
        // Arrange
        Integer id = Integer.valueOf(4);
        // Act
        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        // Assert
        assertThrows(NotFoundException.class,()->{authorService.getAuthorById(id);});
    }

    @Test
    public void addAuthorTest_SendNewAuthor_ReturnAuthor() {
        // Arrange
        Author author = new Author();
        // Act
        when(authorRepository.save(author)).thenReturn(new Author());
        Author result = authorService.addAuthor(author);
        // Assert
        assertTrue(result != null);
    }

    @Test
    public void addAuthorTest_SendDuplicateAuthor_ReturnDuplicateEntryException() {
        // Arrange
        Author author = new Author();
        // Act
        when(authorRepository.findByName(author.getName())).thenReturn(Optional.of(author));
        // Assert
        assertThrows(DuplicateEntryException.class,()->{authorService.addAuthor(author);});

    }

    @Test
    public void deleteAuthorTest_SendValidId_Return() {
        // Arrange
        Integer id = Integer.valueOf(1);
        // Act
        when(authorRepository.findById(id)).thenReturn(Optional.of(new Author()));
        // Assert
        assertDoesNotThrow(()->{authorService.deleteAuthor(id);});
    }

    @Test
    public void deleteAuthorTest_SendInvalidId_ThrowNotFoundException() {
        // Arrange
        Integer id = 1;
        // Act
        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        // Assert
        assertThrows(NotFoundException.class, ()->{authorService.deleteAuthor(id);});
    }

    @Test
    public void updateAuthorTest_SendValidId_Return() {
        // Arrange
        Integer id = Integer.valueOf(1);
        Author author = new Author();
        // Act
        when(authorRepository.findById(id)).thenReturn(Optional.of(new Author()));
        when(authorRepository.save(author)).thenReturn(new Author());
        // Assert
        assertDoesNotThrow(()->{authorService.updateAuthor(id, author);});
    }

    @Test
    public void updateAuthorTest_SendInvalidId_ThrowNotFoundException() {
        // Arrange
        Integer id = 1;
        Author author = new Author();
        // Act
        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        // Assert
        assertThrows(NotFoundException.class, ()->{authorService.updateAuthor(id, author);});
    }

}
