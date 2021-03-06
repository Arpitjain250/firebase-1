package com.icu.database;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table MESSAGE.
 */
public class Message {

    private Long id;
    private String message;
    private Double latitude;
    private Double longitude;
    private String userId;

    public Message() {
    }

    public Message(Long id) {
        this.id = id;
    }

    public Message(Long id, String message, Double latitude, Double longitude, String userId) {
        this.id = id;
        this.message = message;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
