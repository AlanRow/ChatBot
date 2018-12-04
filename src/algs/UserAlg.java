package algs;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import bot.interfaces.Algorithm;
import bot.interfaces.DataCorrector;
import bot.interfaces.DataManager;
import bot.interfaces.DataSearcher;
import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
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
	
	public UserInfo getUser() {
		return user;
	}

	@Override
	public void readMessage(String message) {

		isReady = true;
		
		if ("".equals(message)) {
			isReady = false;
			return;
		}
		
		String[] commands = message.split(" ");
		if (commands.length == 0) {
			isReady = false;
			return;
		}
			
		switch (commands[0].toLowerCase()) {
			
			case "will":
				Meeting meet = meetingsMap.get(1);
				
				if (meet.isPublic()) {
					MemberAlg asMember = new MemberAlg(meet, user, "Ok, I've recorded you.");
					try {
						meet.addMember(asMember, listAlg);
						answer = "Ok, I've recorded you.";
					} catch (IOException | UncorrectDataException | UnfoundedDataException e) {
						answer = "Sorry, the file-working failed.";
					}
				}
				else {
					if (commands.length < 2 || commands[1] == null || commands[1] == "") {
						answer = "This meet isn't public. Please, type the password after <will>.";
						break;
					}
					
					if (meet.checkPassword(commands[1])) {
						MemberAlg asMember = new MemberAlg(meet, user, "Ok, I've recorded you.");
						try {
							meet.addMember(asMember, listAlg);
							answer = "Ok, I've recorded you.";
						} catch (IOException | UncorrectDataException | UnfoundedDataException e) {
							answer = "Sorry, the file-working failed.";
						}
					}
					else
						answer = "Sorry, the password is incorrect.";
				}
				break;
				
			case "help":
				answer = "Commands list:\n" +
						"help - show this help-list\n" +
						"will - join to public meeting\n" +
						"will [password] - join to not-public meeting\n";
				break;
			
			default:
				isReady = false;	
		}
	}

	@Override
	public boolean isReadyToGenerate() {
		return isReady;
	}

	@Override
	public String generateMessage() {
		isReady = false;
		return answer;
	}

	//что с этим делать?
	@Override
	public Algorithm genererateSame(UserInfo another) {
		return null;
	}
	
}
