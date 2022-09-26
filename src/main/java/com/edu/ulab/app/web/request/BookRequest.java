package com.edu.ulab.app.web.request;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class BookRequest {
    @NonNull
    private String title;
    @NonNull
    private String author;
    @NonNull
    private long pageCount;
}
