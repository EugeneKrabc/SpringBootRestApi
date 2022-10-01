package com.edu.ulab.app.web.request;

import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.List;

@Data
public class UserBookRequest {
    private UserRequest userRequest;
    private List<BookRequest> bookRequests;
}
