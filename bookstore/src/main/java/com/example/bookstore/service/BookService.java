package com.example.bookstore.service;

import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.bookstore.exception.BookNotFoundException;


import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookService {
    @Autowired
    BookRepository bookRepository;

    @Transactional
    public Book addBook(Book book) {
        try {
            return bookRepository.save(book);
        } catch (Exception e) {
            log.error("Error in adding book: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public Book updateBook(String isbn, Book updatedBook) {
        Optional<Book> existingBookOpt = bookRepository.findById(isbn);
        log.info("Existing book: {}", existingBookOpt);
        if (existingBookOpt.isPresent()) {
            Book existingBook = existingBookOpt.get();
            existingBook.updateNonNullFields(updatedBook);
            return bookRepository.save(existingBook);
        }
        log.warn("Book with ISBN {} not found", isbn);
        throw new BookNotFoundException("Book with ISBN " + isbn + " not found");
    }



    public List<Book> findBooksByTitle(String title) {
        List<Book> books = bookRepository.findByTitle(title);
        if (books.isEmpty()) {
            throw new BookNotFoundException("No books found with title: " + title);
        }
        return books;
    }

    public List<Book> findBooksByAuthor(String authorName) {
        List<Book> books = bookRepository.findByAuthorsName(authorName);
        if (books.isEmpty()) {
            throw new BookNotFoundException("No books found by author: " + authorName);
        }
        return books;
    }

    public void deleteBook(String isbn) {
        Optional<Book> existingBookOpt = bookRepository.findById(isbn);
        if (existingBookOpt.isPresent()) {
            bookRepository.deleteById(isbn);
        } else {
            throw new BookNotFoundException("Book with ISBN " + isbn + " not found");
        }
    }

}
