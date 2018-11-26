package algs;

import java.io.IOException;

import bot_interfaces.Algorithm;
import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
import structures.Meeting;
import structures.UserInfo;

public class MemberAlg implements Algorithm {
	
	protected UserInfo user;
	protected Meeting meeting;
	protected String answer;
	protected boolean isReady;
	
	public MemberAlg(Meeting meet, UserInfo user, String startMessage) {
		this.user = user;
		meeting = meet;
		answer = startMessage;
		isReady = (!"".equals(answer));
	}
	
	public UserInfo getUser() {
		return user;
	}
	
	public void readMessage(String message) {
		isReady = true;
		
		switch (message.toLowerCase()) {
			case "will":
				answer = "You've just been a member.";
				break;
			case "wont":
			try {
				meeting.removeMember(user, "Ok, I've struck off you.");
			
			} catch (IOException | UncorrectDataException ex) {
				answer = "Sorry, the file-working failed...";
			} catch (UnfoundedDataException ex) {
				answer = "Sorry, I haven't found you in file...";
			}
				break;
			case "help":
				answer = "Commands list:\n" +
							"help - show this help-list\n" +
							"info - show the information about meeting (name, time, place, etc.)\n" +
							"wont - struck off you from list\n";
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

	public Algorithm genererateSame(UserInfo another) {
		return new MemberAlg(meeting, another, "");
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MemberAlg)
			return user.equals(((MemberAlg)obj).user) && meeting.equals(((MemberAlg)obj).meeting);
		return false;
	}
	
	@Override
	public int hashCode() {
		return ((int)user.getId()) ^ meeting.getId();
	}
}
