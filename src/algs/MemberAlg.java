package algs;

import java.io.IOException;

import bot.interfaces.Algorithm;
import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
import exceptions.tryToRemoveOwnerException;
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
	
	public String getMemberType() {
		return "member";
	}
	
	public Meeting getMeeting() {
		return meeting;
	}
	
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
				answer = "You've just been a member.";
				break;
				
			case "info":
				String info =  meeting.getInfo();
				answer = (info != null && !info.equals("")) ? info : "Here isn't any information about this meeting....";
				break;
				
			case "wont":
			try {
				meeting.removeMember(user, "Ok, I've struck off you.");
				answer = "Ok, I've struck off you.";
			} catch (IOException | UncorrectDataException ex) {
				answer = "Sorry, the file-working failed...";
			} catch (UnfoundedDataException ex) {
				answer = "Sorry, I haven't found you in file...";
			} catch (tryToRemoveOwnerException ex) {
				answer = "You try to remove owner of meeting.";
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
