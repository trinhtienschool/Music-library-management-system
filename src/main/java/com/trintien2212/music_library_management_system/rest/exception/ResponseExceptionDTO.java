package com.trintien2212.music_library_management_system.rest.exception;

import java.time.LocalDateTime;

public class ResponseExceptionDTO {
    private int status;
    private String message;
    private LocalDateTime time;

    public ResponseExceptionDTO(int status, String message) {
        this.status = status;
        this.message = message;
        this.time = LocalDateTime.now();
    }

    public ResponseExceptionDTO() {
    }
    public static ResponseExceptionDTO of(int status, String message){
        return new ResponseExceptionDTO(status, message);
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
