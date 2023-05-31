package tech.jazz.apicadastro.presentation.handler.exceptions;

public class InvalidCepException extends RuntimeException {
    public InvalidCepException(String message) {
        super(message);
    }
}
