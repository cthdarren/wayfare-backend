package com.wayfare.backend.request;

import java.util.List;

public record EditProfileRequest(String pictureUrl, String aboutMe, List<String> languagesSpoken) {
}
