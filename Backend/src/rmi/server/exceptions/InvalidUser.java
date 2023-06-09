package rmi.server.exceptions;

public class InvalidUser extends Exception {
    private String message;

    public InvalidUser() {
        super();
        this.message = "Invalid User";
    }

    public InvalidUser(String exceptionMessage) {
        super();
        this.message = exceptionMessage;
    }

    public String getErrorMessage() {
        return this.message;
    }
}
