package exceptions;

public class IllegalReadingException extends Exception {

	public IllegalReadingException() {
		super("You tried to read message from user, that don't talked to bot.");
	}
}