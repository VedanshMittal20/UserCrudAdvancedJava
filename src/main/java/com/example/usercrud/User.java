package com.example.usercrud;

/** Immutable representation of a user record. */
public record User(Long id, String name, String email, int age) {
    @Override
    public String toString() {
        return "%d | %-20s | %-30s | %d".formatted(id, name, email, age);
    }
}
