package com.edu.ulab.app.web.request;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class UserRequest {
    @NonNull
    private String fullName;
    @NonNull
    private String title;
    @NonNull
    private int age;
}
