package bot.interfaces;

import java.util.List;

import structures.UserInfo;

/*интерфейс класса, отвечающего за передачу сообщений пользователю. »нкапсулирует всю 
 работу с конкретной средой передачи сообщений (¬ , телеграм, консоль и др.). Ќе генерирует 
 никаких сообщений сам. ƒл€ каждого пользовател€ отдельный controller*/

public interface MessageController {
	
	//отправл€ет строку message пользователю
	public void send(String message);
	//провер€ет, есть ли новые сообщени€ от пользовател€
	public boolean areNewMessages();
	//возвращает массив всех новых сообщений
	public List<String> getNewMessages();
	//возвращает алгоритм, прив€занный к данному controller'у
	public Algorithm getAlgorithm();
	//возвращает пользовател€, с которым работает Controller
	public UserInfo getUser();
	//порождает копию такого же talker'а с same-алгоритмом, но с другим пользователем.
	public MessageController genererateSame(UserInfo another);
}
