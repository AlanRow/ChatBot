package bot_01;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import structures.Meeting;
import structures.UserInfo;
import telegram_bot.MyAmazingBot;
import telegram_bot.TelegramTalker;
//import src.MyAmazingBot;
import algs.*;

public class Main {

	public static void main(String[] args) {
		
		//�������� ����������� ����� �������������
		UserControl bot;
		try {
			bot = MyAmazingBot.getBot();
		} catch (ManyTelBotsException e) {
			System.out.println("So many bots!");
			return;
		}
		
		//��������� ������
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
		
		//meetings map creating
		Map<Integer, Meeting> meetings = new HashMap<Integer, Meeting>();
		Meeting meet = null;
		try {
				meet = new Meeting(1, dataManager);
		} catch (UncorrectDataException ex) {
			System.out.println("Something wrong with data: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("Something wrong with file-working: " + ex.getMessage());
		}
		meetings.put(1, meet);
		
		//�������� ������� ���������
		Algorithm mainAlg = new ListWriteAlg(meetings, new UserInfo(0, "user"));
		//�������� ������� ��������������� ����������� ���������
		MessageController mainTalker = new TelegramTalker(new UserInfo(0, "user"), mainAlg, (MyAmazingBot) bot);
		//������ ���� ������������, ��������� ��� ������� ������������
		List<MessageController> talkers = new ArrayList<MessageController>();
		
		//���������� ������������� � ������ ��� ������ ����������� ������������� � �������
		updateSameUsers(bot, talkers, mainTalker);
			
		while (true)
		{
			//�������� ��������� ����� ������������� (����������� �������� �������� ��� ��������)
			updateSameUsers(bot, talkers, mainTalker);
			
			//����������� ��� ��������� ����� ��������� � ��������
			talk(talkers);
			
			try {
				Thread.sleep(50);//�������� �������, ����� �� ������� ���������
			} 
			catch (InterruptedException e) {
			}
		}
	}

	//������� ����� �������� ��� ������������ �������������
	public static List<MessageController> generateTalkers(List<UserInfo> users, MessageController original) {
		List<MessageController> talkers = new ArrayList<MessageController>();
		
		for (UserInfo user : users) {
			talkers.add(original.genererateSame(user));
		}
		
		return talkers;
	}
	
	//��������� ����� �������� ���������� original, �� � ������ �������������
	public static void updateSameUsers(UserControl bot, List<MessageController> talkers, MessageController original){
		if (bot.areNewUsers()) {
			talkers.addAll(generateTalkers(bot.getNewUsers(), original));
		}
	}
	
	//��������� ����� ��������� � ����� ��������
	public static void talk(List<MessageController> talkers) {
		for (MessageController talker : talkers) {
			//������� �������� ���������������� �������
			Algorithm alg = talker.getAlgorithm();
			
			
			List<String> messages = new ArrayList<String>();
			if (talker.areNewMessages()) {
				//���� ������ ����� ���������, �� ������� �� � ������
				messages = talker.getNewMessages();
			}
			
			//���������� ������ ��������� � ���� �������� ����� �������� �������� ��������� ����� �������
			for (String mes : messages) {
				alg.readMessage(mes);
				while (alg.isReadyToGenerate())
					talker.send(alg.generateMessage());
			}
		}
	}

}
