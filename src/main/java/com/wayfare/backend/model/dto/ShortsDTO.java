package com.wayfare.backend.model.dto;

import com.wayfare.backend.model.ValidateClass;

import java.util.Date;

public class ShortsDTO extends ValidateClass {
    private final String description;
    private final String shortsUrl;
    private final Date datePosted;
    private final String thumbnailUrl;

    public ShortsDTO(String description, String shortsUrl, Date datePosted, String thumbnailUrl) {
        this.description = description;
        this.shortsUrl = shortsUrl;
        this.datePosted = datePosted;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getShortsUrl() {
        return shortsUrl;
    }
    public Date getDatePosted() {
        return datePosted;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public void validate() {
        if (getShortsUrl() == null | getDatePosted() == null | getThumbnailUrl() == null){
            addErrors("Missing JSON Fields");
        }
        getErrors().remove(null);
    }
}
