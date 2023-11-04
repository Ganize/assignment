package com.example.bookstore.service;

import com.example.bookstore.entity.Author;
import com.example.bookstore.repository.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Author addAuthor(Author author) {
        return authorRepository.save(author);
    }
}
