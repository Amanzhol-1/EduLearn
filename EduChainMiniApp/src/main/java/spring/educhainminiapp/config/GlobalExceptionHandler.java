package spring.educhainminiapp.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage();
        HttpStatus status;

        if ("Пользователь не авторизован".equals(message)) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (message.contains("не найден")) {
            status = HttpStatus.NOT_FOUND;
        } else if (message.contains("уже зарегистрированы") || message.contains("недостаточен")) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(message);
    }
}
