package algs;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import bot.interfaces.Algorithm;
import bot.interfaces.DataCorrector;
import bot.interfaces.DataManager;
import bot.interfaces.DataSearcher;
import exceptions.UncorrectDataException;
import structures.Meeting;
import structures.UserInfo;

//����� �������� ������������, �������� ListWriteAlg ����� ������������ ���� ����������������
public class UserAlg implements Algorithm{

	private Map<Integer, Meeting> meetingsMap;
	private ListWriteAlg listAlg;
	private UserInfo user;
	private String answer;
	private boolean isReady;
	
	public UserAlg(ListWriteAlg alg, Map<Integer, Meeting> meetings, UserInfo user, String startMessage) {
		listAlg = alg;
		this.user = user;
		answer = startMessage;
		meetingsMap = meetings;
		isReady = (!"".equals(startMessage));
	}
	
	public void readMessage(String message) {

		isReady = true;
		
		if ("".equals(message)) {
			isReady = false;
			return;
		}
		
		switch (message.toLowerCase().split(" ")[0]) {
			case "will":
				Meeting meet = meetingsMap.get(1);
				MemberAlg asMember = new MemberAlg(meet, user, "Ok, I've recorded you.");
				try {
					meet.addMember(asMember, listAlg);
				} catch (IOException | UncorrectDataException e) {
					answer = "Sorry, the file-working failed.";
				}
				break;
			case "help":
				answer = "Commands list:\n" +
						"help - show this help-list\n" +
						"info - show the information about meeting (name, time, place, etc.)\n" +
						"will - record you to list\n";
				break;
			default:
				isReady = false;	
		}
	}

	public boolean isReadyToGenerate() {
		return isReady;
	}

	public String generateMessage() {
		isReady = false;
		return answer;
	}

	//��� � ���� ������?
	public Algorithm genererateSame(UserInfo another) {
		return null;
	}
	
}
