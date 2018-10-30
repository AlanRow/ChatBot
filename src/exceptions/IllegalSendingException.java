package exceptions;

public class IllegalSendingException extends Exception {

	public IllegalSendingException() {
		super("You tried to send message to user, that don't talked to bot.");
	}
}
