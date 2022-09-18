package com.edu.ulab.app.entity;

import java.util.HashSet;
import java.util.Set;


public class UserEntity {
    private String fullName;
    private String title;
    private int age;
    Set<Long> bookIdSet = new HashSet<>();

    public UserEntity(String fullName, String title, int age) {
        this.fullName = fullName;
        this.title = title;
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public String getTitle() {
        return title;
    }

    public int getAge() {
        return age;
    }

    public void addBookId(Long bookId) {
        bookIdSet.add(bookId);
    }

    public Set<Long> getBookIdSet() {
        return bookIdSet;
    }

}
