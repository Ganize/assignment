package com.example.bookstore.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "BOOK")
public class Book {
    @Id
    @Column(name = "ISBN")
    String isbn;
    @Column(name = "TITLE")
    String title;
    @Column(name = "YEAR")
    Integer year;
    @Column(name = "PRICE")
    Double price;
    @Column(name = "GENRE")
    String genre;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "BOOK_AUTHOR",
            joinColumns = { @JoinColumn(name = "BOOK_ISBN") },
            inverseJoinColumns = { @JoinColumn(name = "AUTHOR_ID") }
    )
    Set<Author> authors = new HashSet<>();

    public void updateNonNullFields(Book updatedBook) {
        this.title = updatedBook.title != null ? updatedBook.title : this.title;
        this.year = updatedBook.year != null ? updatedBook.year : this.year;
        this.price = updatedBook.price != null ? updatedBook.price : this.price;
        this.genre = updatedBook.genre != null ? updatedBook.genre : this.genre;
        this.authors = updatedBook.authors != null && !updatedBook.authors.isEmpty() ? updatedBook.authors : this.authors;
    }

}
