package exceptions;

public class ManyTelBotsException extends Exception {

	public ManyTelBotsException() {
		super("You tried to make more than one interface for Telegram-bot.");
	}
}
