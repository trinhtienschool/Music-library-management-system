package com.trintien2212.music_library_management_system.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseExceptionDTO> resourceNotFoundException(ResourceNotFoundException ex) {
        ResponseExceptionDTO resourceExceptionDTO = ResponseExceptionDTO.of(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resourceExceptionDTO);
    }

    @ExceptionHandler(ResourceExistedException.class)
    public ResponseEntity<ResponseExceptionDTO> resourceExisted(ResourceExistedException ex) {
        ResponseExceptionDTO resourceExceptionDTO = ResponseExceptionDTO.of(HttpStatus.CONFLICT.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(resourceExceptionDTO);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseExceptionDTO> badRequest(BadRequestException ex) {
        ResponseExceptionDTO resourceExceptionDTO = ResponseExceptionDTO.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourceExceptionDTO);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ResponseExceptionDTO> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String message = "Request method " + ex.getMethod() + " is not supported for this endpoint. Supported methods are " + Arrays.toString(ex.getSupportedMethods());
        ResponseExceptionDTO resourceExceptionDTO = ResponseExceptionDTO.of(HttpStatus.METHOD_NOT_ALLOWED.value(), message);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(resourceExceptionDTO);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ResponseExceptionDTO> handleMissingParams(MissingServletRequestParameterException ex) {
        String message = "The " + ex.getParameterName() + " parameter is missing";
        ResponseExceptionDTO resourceExceptionDTO = ResponseExceptionDTO.of(HttpStatus.METHOD_NOT_ALLOWED.value(), message);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(resourceExceptionDTO);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseExceptionDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        ResponseExceptionDTO resourceExceptionDTO = ResponseExceptionDTO.of(HttpStatus.BAD_REQUEST.value(), ex.getFieldError().getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourceExceptionDTO);
    }
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ResponseExceptionDTO> resourceNotFoundException(UnAuthorizedException ex) {
        ResponseExceptionDTO resourceExceptionDTO = ResponseExceptionDTO.of(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resourceExceptionDTO);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseExceptionDTO> httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ResponseExceptionDTO resourceExceptionDTO = ResponseExceptionDTO.of(HttpStatus.BAD_REQUEST.value(), "Request body not null");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourceExceptionDTO);
    }
}
