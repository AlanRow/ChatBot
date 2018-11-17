package bot_01;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import bot_interfaces.Algorithm;
import bot_interfaces.DataCorrector;
import bot_interfaces.DataManager;
import bot_interfaces.DataReader;
import bot_interfaces.DataWriter;
import bot_interfaces.UserControl;
import bot_interfaces.MessageController;
import console_bot.*;
import dataManagers.FileDataReader;
import dataManagers.FileDataWriter;
import dataManagers.VirtualDataManager;
import exceptions.ManyTelBotsException;
import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
import structures.UserInfo;
import telegram_bot.MyAmazingBot;
import telegram_bot.TelegramTalker;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
//import src.MyAmazingBot;
import algs.*;

public class Main {

	public static void main(String[] args) {
		
		//создание контроллера новых пользователей
		UserControl bot;
		try {
			bot = MyAmazingBot.getBot();
		} catch (ManyTelBotsException e) {
			System.out.println("So many bots!");
			return;
		}
		
		//подгрузка данных
		DataManager dataManager;
		try {
			dataManager = new VirtualDataManager("list1.txt");
		} catch (IOException e) {
			System.out.println("File working error with");
			return;
		} catch (UncorrectDataException e) {
			System.out.println("Data is uncorrect: " + e.getMessage());
			return;
		}
		
		//создание шаблона алгоритма
		Algorithm mainAlg = new ListWriteAlg(dataManager, dataManager, new UserInfo(0, "user"));
		//создание шаблона индивидуального контроллера сообщений
		MessageController mainTalker = new TelegramTalker(new UserInfo(0, "user"), mainAlg, (MyAmazingBot) bot);
		//список всех контроллеров, отдельный для каждого пользователя
		List<MessageController> talkers = new ArrayList<MessageController>();
		
		//обновление пользователей в списке при помощи контроллера пользователей и шаблона
		updateSameUsers(bot, talkers, mainTalker);
			
		while (true)
		{
			//проверка появления новых пользователей (циклическая проверка актульна для соцсетей)
			updateSameUsers(bot, talkers, mainTalker);
			
			//анализирует все имеющиеся новые сообщения и отвечает
			talk(talkers);
			
			try {
				Thread.sleep(50);//подождем немного, чтобы не грузить процессор
			} 
			catch (InterruptedException e) {
			}
		}
	}

	//создает новых толкеров для добавившихся пользователей
	public static List<MessageController> generateTalkers(List<UserInfo> users, MessageController original) {
		List<MessageController> talkers = new ArrayList<MessageController>();
		
		for (UserInfo user : users) {
			talkers.add(original.genererateSame(user));
		}
		
		return talkers;
	}
	
	//добавляет новых толкеров идентичных original, но с другим пользователем
	public static void updateSameUsers(UserControl bot, List<MessageController> talkers, MessageController original){
		if (bot.areNewUsers()) {
			talkers.addAll(generateTalkers(bot.getNewUsers(), original));
		}
	}
	
	//обработка новых сообщений и ответ толкеров
	public static void talk(List<MessageController> talkers) {
		for (MessageController talker : talkers) {
			//выносим алгоритм соответствующего толкера
			Algorithm alg = talker.getAlgorithm();
			
			
			List<String> messages = new ArrayList<String>();
			if (talker.areNewMessages()) {
				//если пришли новые сообщения, то добавим их в массив
				messages = talker.getNewMessages();
			}
			
			//обработаем каждое сообщение и если алгоритм готов ответить отправим сообщение через толкера
			for (String mes : messages) {
				alg.readMessage(mes);
				while (alg.isReadyToGenerate())
					talker.send(alg.generateMessage());
			}
		}
	}

}
