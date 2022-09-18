package com.edu.ulab.app.entity;

public class BookEntity {
    private String title;
    private String author;
    private long pageCount;
    private Long userId;

    public BookEntity(String title, String author, long pageCount, Long userId) {
        this.title = title;
        this.author = author;
        this.pageCount = pageCount;
        this.userId = userId;
    }
}
