package ru.otus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateClientResponse {

    @JsonProperty
    private final Status status;

    public CreateClientResponse(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        OK, FAIL
    }
}
