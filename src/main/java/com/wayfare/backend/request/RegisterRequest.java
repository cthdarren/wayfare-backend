package com.wayfare.backend.request;

public record RegisterRequest(String username, String firstName, String lastName, String password, String verifyPassword, String email, String phoneNumber) {
}
