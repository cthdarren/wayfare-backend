package com.wayfare.backend.request;

public record RegisterRequest(String username, String password, String verifyPassword, String email, String phoneNumber) {
}
