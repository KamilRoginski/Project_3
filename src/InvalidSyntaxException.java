// Name: Kamil Roginski
// Project: CMSC 315 Project 3
// Date: 22 APR 2025
// Description: Checked exception for malformed tree input.

public class InvalidSyntaxException extends Exception {
    public InvalidSyntaxException(String message) {
        super(message);
    }
}
