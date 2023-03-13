package ua.clamor1s.emailsender.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorModel {

    private final HttpStatus httpStatus;

    private LocalDateTime timestamp = LocalDateTime.now();

    private final String message;

    private final String details;

}
