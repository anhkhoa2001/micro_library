package org.micro.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "li_book")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "content")
    private String content;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "book_type_id", referencedColumnName = "type_id")
    private BookType bookType;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author author;
}
