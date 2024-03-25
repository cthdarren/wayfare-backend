package com.wayfare.backend.request;

public record EditAccountDetailsRequest(String username, String firstName, String lastName, String phoneNumber) {
}
