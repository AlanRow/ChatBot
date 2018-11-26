package algs;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import bot_interfaces.Algorithm;
import bot_interfaces.DataCorrector;
import bot_interfaces.DataManager;
import bot_interfaces.DataSearcher;
import exceptions.UncorrectDataException;
import structures.Meeting;
import structures.UserInfo;

//класс простого пользователя, которому ListWriteAlg может делегировать свою функциональность
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
		switch (message.toLowerCase()) {
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
		}
	}

	public boolean isReadyToGenerate() {
		return isReady;
	}

	public String generateMessage() {
		isReady = false;
		return answer;
	}

	//что с этим делать?
	public Algorithm genererateSame(UserInfo another) {
		return null;
	}
	
}
