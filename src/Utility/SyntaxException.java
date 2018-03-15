package Utility;

public class SyntaxException extends Exception {
    private int cause;
    public SyntaxException(int cause) {
        this.cause = cause;
    }

    public int getIntCause() {
        return cause;
    }
}
