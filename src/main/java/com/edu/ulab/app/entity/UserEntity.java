package com.edu.ulab.app.entity;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
public class UserEntity {
    private final String fullName;
    private final String title;
    private final int age;
    private final Set<Long> bookIdSet = new HashSet<>();
}
