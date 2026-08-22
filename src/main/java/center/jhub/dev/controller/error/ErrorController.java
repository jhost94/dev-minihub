package center.jhub.dev.controller.error;


import center.jhub.dev.bean.ErrorResponse;
import center.jhub.dev.exception.InvalidTemplateException;
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


    @ExceptionHandler(InvalidTemplateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTemplateException(InvalidTemplateException ex, WebRequest request) {
        return getResponse(ex, request);
    }
}
