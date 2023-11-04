package com.example.bookstore.controller;

import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book savedBook = bookService.addBook(book);
        return ResponseEntity.ok(savedBook);
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<Book> updateBook(@PathVariable String isbn, @RequestBody Book updatedBook) {
        Book updated = bookService.updateBook(isbn, updatedBook);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<Book>> findBooksByTitle(@PathVariable String title) {
        List<Book> books = bookService.findBooksByTitle(title);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/author/{authorName}")
    public ResponseEntity<List<Book>> findBooksByAuthor(@PathVariable String authorName) {
        List<Book> books = bookService.findBooksByAuthor(authorName);
        return ResponseEntity.ok(books);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{isbn}")
    public ResponseEntity<String> deleteBook(@PathVariable String isbn) {
        try {
            bookService.deleteBook(isbn);
            return ResponseEntity.ok("Book deleted successfully.");
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {  // This catch block may not be necessary as Spring Security should handle access denial
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. You do not have permission to delete books.");
        } catch (Exception e) {  // Catch any other exceptions that might occur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request.");
        }
    }

    @GetMapping("/whoami")
    public ResponseEntity<?> whoAmI() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(authentication.getAuthorities());
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("/");// replace 'route-path' with your specific path

    }
}
