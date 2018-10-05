package bot_01;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bot_interfaces.Algorithm;
import bot_interfaces.UserControl;
import bot_interfaces.MessageController;
import console_bot.*;
import algs.*;

public class Main {

	public static void main(String[] args) {
		

		Scanner input = new Scanner(System.in);
		List<String> users = new ArrayList<String>();

		UserControl control = new CUserController();
		Algorithm alg = new HelloAlg();
		MessageController talker = new CTalker(alg);
		MessageController[] talkers = new MessageController[] {talker};
		
		
		while (true)
		{
			//проверка появления новых пользователей (циклическая проверка актульна для соцсетей)
			updateUsers(control, users);
			//анализирует все имеющиеся новые сообщения и отвечает
			talk(talkers);
		}
	}

	public static void updateUsers(UserControl control, List<String> users) {
		if (control.areNewUsers()) {
			String[] newUsers = control.getNewUsers();
			for (String user : newUsers)
				users.add(user);
		}
	}

	public static void talk(MessageController[] talkers) {
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
