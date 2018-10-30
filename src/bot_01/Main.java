package bot_01;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import bot_interfaces.Algorithm;
import bot_interfaces.DataCorrector;
import bot_interfaces.DataReader;
import bot_interfaces.DataWriter;
import bot_interfaces.UserControl;
import bot_interfaces.MessageController;
import console_bot.*;
import dataManagers.FileDataReader;
import dataManagers.FileDataWriter;
import dataManagers.VirtualDataManager;
import exceptions.ManyTelBotsException;
import exceptions.UnCorrectDataException;
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

		/*MyAmazingBot myBot;
		try {
			myBot = MyAmazingBot.getBot();
		}
		catch (ManyTelBotsException ex) {
			return;
		}*/
		Scanner input = new Scanner(System.in);
		
		List<UserInfo> users = new ArrayList<UserInfo>();
		
		DataWriter fileWriter;
		DataReader fileReader;
		DataCorrector corrector;
		try {
				fileWriter = new FileDataWriter("list1.txt");
				fileReader = new FileDataReader("list1.txt");
				corrector = new VirtualDataManager(fileReader, fileWriter);
		}
		catch (IOException ex) {
			System.out.println("Programm can't work with file list1.txt.");
			return;
		}
		catch (UnCorrectDataException ex) {
			System.out.println("The data is broken");
			return;
		}
			

		//UserControl control = new CUserController();
		MyAmazingBot control;
		try {
			control = MyAmazingBot.getBot();
		} catch (ManyTelBotsException e) {
			System.out.println("So many bots!");
			return;
		}
		
		//updateUsers(control, users);
		List<MessageController> talkers = new ArrayList<MessageController>();
			

		//updateTelegramUsers(control, talkers, users, corrector);
		//for (UserInfo user : users)
		//{	
			//Algorithm alg = new ListWriteAlg(corrector, user);
			//talkers.add(new TelegramTalker(user, alg, control));
		//}

			
		while (true)
		{
			//проверка появления новых пользователей (циклическая проверка актульна для соцсетей)
			//updateUsers(control, users);
			updateTelegramUsers(control, talkers, users, corrector);
			
			//анализирует все имеющиеся новые сообщения и отвечает
			talk(talkers);
		}
	}

	//public static void updateUsers(UserControl control, List<UserInfo> users) {
		//}
	//}

	public static void updateTelegramUsers(MyAmazingBot bot, List<MessageController> talkers, List<UserInfo> users, DataCorrector corrector){
		if (bot.areNewUsers()) {
			List<UserInfo> newUsers = bot.getNewUsers();
			for (UserInfo user : newUsers) {
				users.add(user);
				Algorithm alg = new ListWriteAlg(corrector, user);
				talkers.add(new TelegramTalker(user, alg, bot));
			}
		}
	}
	
	public static void talk(List<MessageController> talkers) {
		//System.out.println("Разговор идет...");
		for (MessageController talker : talkers) {
			Algorithm alg = talker.getAlgorithm();
			List<String> messages = talker.getNewMessages();
			
			for (String mes : messages) {
				System.out.println("алгоритму склрмили: " + mes);
				alg.readMessage(mes);
				while (alg.isReadyToGenerate())
					talker.send(alg.generateMessage());
			}
		}
	}

}
