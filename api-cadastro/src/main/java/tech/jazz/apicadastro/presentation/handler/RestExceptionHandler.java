package tech.jazz.apicadastro.presentation.handler;

import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.jazz.apicadastro.presentation.handler.exceptions.DuplicateCpfException;
import tech.jazz.apicadastro.presentation.handler.exceptions.InvalidCepException;
import tech.jazz.apicadastro.presentation.handler.exceptions.InvalidCepFormatException;

import java.net.URI;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class RestExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    private ProblemDetail problemDetailBuilder(int status, String title, String message, RuntimeException e){
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setType(URI.create(""));
        problemDetail.setTitle(title);
        problemDetail.setDetail(message);
        logger.error(String.format("%s: %s",problemDetail.getTitle(),problemDetail.getDetail()),e);
        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handlerConstraintViolationException(ConstraintViolationException e){
        String detail;
        int start = e.getMessage().indexOf("propertyPath=") + "propertyPath=".length();
        if (start > 0){
            int end = e.getMessage().indexOf(",", start);
            String field = e.getMessage().substring(start, end);
            start = e.getMessage().indexOf("interpolatedMessage='") + "interpolatedMessage='".length();
            end = e.getMessage().indexOf("'", start);
            String interpolatedMessage = e.getMessage().substring(start, end);
            detail = field + ": " + interpolatedMessage;
        }else{
            detail = e.getMessage();
        }
        ProblemDetail problemDetail = problemDetailBuilder(406, e.getClass().getSimpleName(), detail, e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ProblemDetail> handlerFeignException(FeignException e){
        ProblemDetail problemDetail = problemDetailBuilder(406, e.getClass().getSimpleName(), "Insira um cep no formato '00000000' ou '00000-000'", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ProblemDetail> handlerDateTimeParseException(DateTimeParseException e){
        ProblemDetail problemDetail = problemDetailBuilder(406, e.getClass().getSimpleName(), "Insira uma data no formato 'yyyy-MM-dd'", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(DuplicateCpfException.class)
    public ResponseEntity<ProblemDetail> handlerDuplicateCpfException(DuplicateCpfException e){
        ProblemDetail problemDetail = problemDetailBuilder(409, e.getClass().getSimpleName(), "O CPF informado já consta na base de dados e não pode ser duplicado", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(InvalidCepException.class)
    public ResponseEntity<ProblemDetail> handlerInvalidCepException(InvalidCepException e){
        ProblemDetail problemDetail = problemDetailBuilder(404, e.getClass().getSimpleName(), "O cep informado não corresponde com um endereço existente", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(InvalidCepFormatException.class)
    public ResponseEntity<ProblemDetail> handlerInvalidCepFormatException(InvalidCepFormatException e){
        ProblemDetail problemDetail = problemDetailBuilder(404, e.getClass().getSimpleName(), "Informe o cep em um formato válido: 00000-000 ou 00000000", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }
}
