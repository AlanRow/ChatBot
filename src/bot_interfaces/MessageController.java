package bot_interfaces;

/*интерфейс класса, отвечающего за передачу сообщений пользователю. »нкапсулирует всю 
 работу с конкретной средой передачи сообщений (¬ , телеграм, консоль и др.). Ќе генерирует 
 никаких сообщений сам. ƒл€ каждого пользовател€ отдельный talker*/

public interface MessageController {
	
	//отправл€ет строку message пользователю
	public void send(String message);
	//провер€ет, есть ли новые сообщени€ от пользовател€
	public boolean areNewMessages();
	//возвращает массив всех новых сообщений
	public String[] getNewMessages();
	//возвращает алгоритм, прив€занный к данному talker'у
	public Algorithm getAlgorithm();
}
