package com.example.usercrud;

import java.io.Serializable;

/** Immutable representation of a user record. */
public record User(Long id, String name, String email, int age) implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "%d | %-20s | %-30s | %d".formatted(id, name, email, age);
    }
}
