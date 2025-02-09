package com.reliaquest.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmployeeServerResponse<T>(T data, Status status, String error) {

    public static <T> EmployeeServerResponse<T> handled() {
        return new EmployeeServerResponse<>(null, Status.HANDLED, null);
    }

    public static <T> EmployeeServerResponse<T> handledWith(T data) {
        return new EmployeeServerResponse<>(data, Status.HANDLED, null);
    }

    public static <T> EmployeeServerResponse<T> error(String error) {
        return new EmployeeServerResponse<>(null, Status.ERROR, error);
    }

    public enum Status {
        HANDLED("Successfully processed request."),
        ERROR("Failed to process request.");

        @JsonValue
        @Getter
        private final String value;

        Status(String value) {
            this.value = value;
        }
    }
}
