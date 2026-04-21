package com.jhost.dev.controller.error;


import com.jhost.dev.bean.ErrorResponse;
import com.jhost.dev.exception.meta.UsernameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

public class ErrorController {

    private ResponseEntity<ErrorResponse> getResponse(RuntimeException ex, WebRequest request) {
        return getResponse(ex, request, true);
    }

    private ResponseEntity<ErrorResponse> getResponse(RuntimeException ex, WebRequest request, boolean includeClientInfo) {
        ErrorResponse response = new ErrorResponse(ex.getMessage(), request, includeClientInfo);

        return new ResponseEntity<>(response, HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        return getResponse(ex, request);
    }
}
