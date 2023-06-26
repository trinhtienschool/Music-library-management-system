package com.trintien2212.music_library_management_system.rest.exception;

public class ResourceExistedException extends RuntimeException{
    public ResourceExistedException(String message) {
        super(message);
    }
}
