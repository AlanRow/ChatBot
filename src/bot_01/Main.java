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
import exceptions.UnCorrectDataException;
import exceptions.UnfoundedDataException;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
//import src.MyAmazingBot;
import algs.*;

public class Main {

	public static void main(String[] args) {
		
		MyAmazingBot myBot = new MyAmazingBot();
		

		 ApiContextInitializer.init();
	      TelegramBotsApi botsApi = new TelegramBotsApi();
	      try {
	          botsApi.registerBot(myBot);
	      } catch (TelegramApiException e) {
	          e.printStackTrace();
	      }
	          
		
		Scanner input = new Scanner(System.in);
		List<String> users = new ArrayList<String>();
		try {
			DataWriter fileWriter = new FileDataWriter("list1.txt");
			DataReader fileReader = new FileDataReader("list1.txt");
			DataCorrector corrector = new VirtualDataManager(fileReader, fileWriter);
			

			UserControl control = new CUserController();
			updateUsers(control, users);
			List<MessageController> talkers = new ArrayList<MessageController>();
			
			for (String user : users)
			{	
				Algorithm alg = new ListWriteAlg(corrector, user);
				talkers.add(new CTalker(alg));
			}

			
			while (true)
			{
				//проверка появления новых пользователей (циклическая проверка актульна для соцсетей)
				updateUsers(control, users);
				//анализирует все имеющиеся новые сообщения и отвечает
				talk(talkers);
			}
		}
		catch (IOException ex)
		{
			System.out.println("Programm can't work with file.");
			return;
		}
		catch (UnCorrectDataException ex)
		{
			System.out.println("The data is broken.");
			return;
		}
	}

	public static void updateUsers(UserControl control, List<String> users) {
		if (control.areNewUsers()) {
			String[] newUsers = control.getNewUsers();
			for (String user : newUsers)
				users.add(user);
		}
	}

	public static void talk(List<MessageController> talkers) {
		for (MessageController talker : talkers) {
			Algorithm alg = talker.getAlgorithm();
			String[] messages = talker.getNewMessages();

			for (String mes : messages) {
				alg.readMessage(mes);
				while (alg.isReadyToGenerate())
					talker.send(alg.generateMessage());
			}
		}
	}

}
