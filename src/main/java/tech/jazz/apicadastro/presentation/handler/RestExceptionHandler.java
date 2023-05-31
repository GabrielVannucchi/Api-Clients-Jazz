package tech.jazz.apicadastro.presentation.handler;

import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.time.format.DateTimeParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.jazz.apicadastro.presentation.handler.exceptions.ClientNotFoundException;
import tech.jazz.apicadastro.presentation.handler.exceptions.CpfOutOfFormatException;
import tech.jazz.apicadastro.presentation.handler.exceptions.DuplicateCpfException;
import tech.jazz.apicadastro.presentation.handler.exceptions.InvalidCepException;
import tech.jazz.apicadastro.presentation.handler.exceptions.InvalidCepFormatException;
import tech.jazz.apicadastro.presentation.handler.exceptions.UuidOutOfFormatException;

@RestControllerAdvice
public class RestExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    private ProblemDetail problemDetailBuilder(HttpStatus status, String title, String message, RuntimeException e) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/" + status.value()));
        problemDetail.setTitle(title);
        problemDetail.setDetail(message);
        logger.error(String.format("%s: %s",problemDetail.getTitle(),problemDetail.getDetail()),e);
        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handlerConstraintViolationException(ConstraintViolationException e) {
        final String detail;
        int start = e.getMessage().indexOf("propertyPath=") + "propertyPath=".length();
        if (start > 0) {
            int end = e.getMessage().indexOf(",", start);
            final String field = e.getMessage().substring(start, end);
            start = e.getMessage().indexOf("interpolatedMessage='") + "interpolatedMessage='".length();
            end = e.getMessage().indexOf("'", start);
            final String interpolatedMessage = e.getMessage().substring(start, end);
            detail = field + ": " + interpolatedMessage;
        } else {
            detail = e.getMessage();
        }
        final ProblemDetail problemDetail = problemDetailBuilder(HttpStatus.BAD_GATEWAY, e.getClass().getSimpleName(), detail, e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ProblemDetail> handlerFeignException(FeignException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(HttpStatus.BAD_REQUEST,
                e.getClass().getSimpleName(), "Connection error with viaCepApi", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ProblemDetail> handlerDateTimeParseException(DateTimeParseException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(HttpStatus.BAD_REQUEST,
                e.getClass().getSimpleName(), "Invalid DateTime format. 'yyyy-MM-dd'", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(DuplicateCpfException.class)
    public ResponseEntity<ProblemDetail> handlerDuplicateCpfException(DuplicateCpfException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(HttpStatus.CONFLICT,
                e.getClass().getSimpleName(), e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(InvalidCepException.class)
    public ResponseEntity<ProblemDetail> handlerInvalidCepException(InvalidCepException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(HttpStatus.BAD_REQUEST,
                e.getClass().getSimpleName(), e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(InvalidCepFormatException.class)
    public ResponseEntity<ProblemDetail> handlerInvalidCepFormatException(InvalidCepFormatException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(HttpStatus.BAD_REQUEST,
                e.getClass().getSimpleName(), e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(CpfOutOfFormatException.class)
    public ResponseEntity<ProblemDetail> handlerCpfOutOfFormatException(CpfOutOfFormatException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(HttpStatus.BAD_REQUEST,
                e.getClass().getSimpleName(), e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(UuidOutOfFormatException.class)
    public ResponseEntity<ProblemDetail> handlerUuidOutOfFormatException(UuidOutOfFormatException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(HttpStatus.BAD_REQUEST,
                e.getClass().getSimpleName(), e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlerClientNotFoundExceptionException(ClientNotFoundException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(HttpStatus.NOT_FOUND,
                e.getClass().getSimpleName(), e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }


}
