package tech.jazz.apicadastro.presentation.handler.exceptions;

public class InvalidCepFormatException extends RuntimeException {
    public InvalidCepFormatException(String message) {
        super(message);
    }
}
