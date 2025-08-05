package br.edu.calc.plus.exception;

/**
 * Exception thrown when attempting to create a user with a login that already exists.
 */
public class DuplicateLoginException extends RuntimeException {

    public DuplicateLoginException(String message) {
        super(message);
    }

    public DuplicateLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}