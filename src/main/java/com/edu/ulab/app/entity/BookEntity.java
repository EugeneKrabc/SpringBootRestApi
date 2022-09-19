package com.edu.ulab.app.entity;

import lombok.Data;


@Data
public class BookEntity {
    private final String title;
    private final String author;
    private final long pageCount;
    private final Long userId;
}
