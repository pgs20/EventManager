package dev.petrov.dto;

import java.time.LocalDateTime;

public record ErrorMessageResponse(
        String message,
        String detailedMessage,
        LocalDateTime dateTime
) {
    @Override
    public String toString() {
        return "ErrorMessageResponse{" +
                "message='" + message + '\'' +
                ", detailedMessage='" + detailedMessage + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
