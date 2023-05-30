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
import tech.jazz.apicadastro.presentation.handler.exceptions.DuplicateCpfException;
import tech.jazz.apicadastro.presentation.handler.exceptions.InvalidCepException;
import tech.jazz.apicadastro.presentation.handler.exceptions.InvalidCepFormatException;

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
                e.getClass().getSimpleName(), "Ocorreu um erro de comunicação com o servidor viaCep", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ProblemDetail> handlerDateTimeParseException(DateTimeParseException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(HttpStatus.BAD_REQUEST,
                e.getClass().getSimpleName(), "Insira uma data no formato 'yyyy-MM-dd'", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(DuplicateCpfException.class)
    public ResponseEntity<ProblemDetail> handlerDuplicateCpfException(DuplicateCpfException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(HttpStatus.CONFLICT,
                e.getClass().getSimpleName(), "O CPF informado já consta na base de dados e não pode ser duplicado", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(InvalidCepException.class)
    public ResponseEntity<ProblemDetail> handlerInvalidCepException(InvalidCepException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(HttpStatus.BAD_REQUEST,
                e.getClass().getSimpleName(), "O cep informado não corresponde com um endereço existente", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(InvalidCepFormatException.class)
    public ResponseEntity<ProblemDetail> handlerInvalidCepFormatException(InvalidCepFormatException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(HttpStatus.BAD_REQUEST,
                e.getClass().getSimpleName(), "Informe o cep em um formato válido: 00000-000 ou 00000000", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }
}
