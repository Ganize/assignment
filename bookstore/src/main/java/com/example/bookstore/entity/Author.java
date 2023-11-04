package com.example.bookstore.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "AUTHOR")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    Long id;
    @Column(name = "NAME")
    String name;
    @Column(name = "BIRTHDAY")
    String birthday;
}

