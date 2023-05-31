package tech.jazz.apicadastro.presentation.handler.exceptions;

public class DuplicateCpfException extends RuntimeException {
    public DuplicateCpfException(String message) {
        super(message);
    }
}
