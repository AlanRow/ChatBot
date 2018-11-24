package algs;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import bot_interfaces.Algorithm;
import bot_interfaces.DataCorrector;
import bot_interfaces.DataManager;
import bot_interfaces.DataReader;
import bot_interfaces.DataSearcher;
import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
import structures.Meeting;
import structures.UserInfo;

//�������� ������ �� ������� �������
public class ListWriteAlg implements Algorithm {
	
	private Algorithm main;//������� ���������� �������� user ��� ��� ���������
	
	private Map<Integer, Meeting> meetingsMap;
	private UserInfo user;
	private boolean isReady;
	private String info;
	private boolean haveUnsolvedExceptions;
	private boolean stoppedWork;
	private String answer;
	String errorMessage;

	public ListWriteAlg(Map<Integer, Meeting> meetings, UserInfo user) {
		meetingsMap = meetings;
		
		for (Meeting meet : meetings.values()) {
			main = meet.getMember(user);
			if (main != null)
				break;
		}
		if (main == null)
			main = new UserAlg(this, meetings, user, 
					"�����������, ��� ��� ��� ������� ������ �������. ����� ������ ������ ������� \"\"");
		
		this.user = user;
		haveUnsolvedExceptions = false;
		stoppedWork = false;
		isReady = false;
		info = "";
		answer = "";
		errorMessage = "";
	}
	
	public ListWriteAlg(Map<Integer, Meeting> meetings, UserInfo user, String inform) {
		this(meetings, user);
		info = inform;
	}
	
	public Map<Integer, Meeting> getMeetingsMap(){
		return meetingsMap;
	}
	
	/*public void switchToUser(String startMessage) {
		main = new UserAlg(this, meetingsMap, startMessage);
	}*/
	
	public void switchMainAlg(Algorithm alg) {
		main = alg;
	}
	
	//��������� ���������� ���������
	public void readMessage(String message) {
		
		isReady = true;
		
		if (haveUnsolvedExceptions)
			return;
		
		//try {
			main.readMessage(message);//�������� �������� ������ ���������
			
			switch (message.toLowerCase()) {
				//������� ��������� � �������
				case "info":
					if (!info.equals(""))
						answer = info;
					else
						answer = "Sorry, there isn't information about this meeting.";
					break;
					//������� ���������� � ����
				case "/start":
					answer = "This bot makes list of your meeting. If you want to know more, please write \"help\"";
					break;
				//����� ������ ������
				case "help":
					answer = "Commands list:\n" +
								"help - show this help-list\n" +
								"info - show the information about meeting (name, time, place, etc.)\n";
						break;
			}
			
			//��������� �������
			/*switch (message.toLowerCase())
			{
				//������ ������������ � ������
				case "will":
					if (!willCome)
					{
						listWriter.writeData(Long.toString(user.getId()), user.getName());
						willCome = true;
						answer = "Ok, I've recorded you.";
					}
					else
						answer = "Yes, I've just understand.";
					break;
				//������������ �� ������
				case "wont":
					if (willCome)
					{
						willCome = false;
						listWriter.removeData(Long.toString(user.getId()));
						answer = "Ok, I've struck off you";
					}
					else
						answer = "You haven't recorded, yet.";
					break;
				//����� ������ �������
				case "show":
					answer = "";
				try {
					for (String extended : source.getAllData().keySet()) {
						answer += source.getData(extended).get(0) + "\n";
					}
				} catch (UncorrectDataException e) {
					errorMessage = "Sorry, data is broken.";
					haveUnsolvedExceptions = true;
				}
				if (answer.equals(""))
					answer = "The list is empty.";
					break;
				//����� ������ ������
				case "help":
					answer = "Commands list:\n" +
								"help - show this help-list\n" +
								"info - show the information about meeting (name, time, place, etc.)\n" +
								"will - record you to list\n" +
								"wont - struck off you from list\n" +
								"show - show the meeting-list\n";
					break;
				//������� ��������� � �������
				case "info":
					if (!info.equals(""))
						answer = info;
					else
						answer = "Sorry, there isn't information about this meeting.";
					break;
				//������� ���������� � ����
				case "/start":
					answer = "This bot makes list of your meeting. If you want to know more, please write \"help\"";
					break;
				default:
					isReady = false;
					break;
			}*/
		//}
		/*catch (IOException ex) {
			haveUnsolvedExceptions = true;
			errorMessage = "Sorry, file working is incorrect...";
		}*/
		
		
		
	}

	public boolean isReadyToGenerate() {
		return isReady || main.isReadyToGenerate();//���� �������� �� ��������� ��� ������� ����� ��������, �� ��������
	}

	public String generateMessage() {
		isReady = false;
		
		//���������� ��������� �� ��������� ���������
		//������� ��������� ��������� ��������� ������������
		if (main.isReadyToGenerate())
			return main.generateMessage();
		
		if (haveUnsolvedExceptions)
			return errorMessage;
		
		return answer;
	}

	public Algorithm genererateSame(UserInfo another) {
		return new ListWriteAlg(meetingsMap, another);
	}
	
	
}
