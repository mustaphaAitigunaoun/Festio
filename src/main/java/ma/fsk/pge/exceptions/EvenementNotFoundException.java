package ma.fsk.pge.exceptions;

public class EvenementNotFoundException extends RuntimeException {

    // Constructor with only a message
    public EvenementNotFoundException(String message) {
        super(message);
    }

    // Constructor with a message and a cause
    public EvenementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}