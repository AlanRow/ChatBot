package bot_01;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class MyAmazingBot extends TelegramLongPollingBot {
	@Override
	public void onUpdateReceived(Update update) {

	    // We check if the update has a message and the message has text
	    if (update.hasMessage() && update.getMessage().hasText()) {
	        // Set variables
	    	String name = update.getMessage().getCaption();
	    	long chat_id = update.getMessage().getChatId();
	        String message_text = update.getMessage().getText();
	        SendMessage message = new SendMessage()
	        		.setChatId(chat_id)
	                .setText("");
	        if (message_text.equals("������")) {
	        	 message = message
	        			.setChatId(chat_id)
	        			.setText("������ :�");
	        }
	        else {
	        	 message = message // Create a message object object
		                .setChatId(chat_id)
		                .setText(name);
	        }
	       
	        try {
	            execute(message); // Sending our message object to user
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
	    }
	}
    @Override
    public String getBotUsername() {
        // TODO
        return "SimSimi_hell_Bot";
    }

    @Override
    public String getBotToken() {
        // TODO
        return "652391891:AAEQ5IbXbfTbHXIgRCuty6C6xb7F-yszvmc";
    }
}