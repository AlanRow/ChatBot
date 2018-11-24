package algs;

import bot_interfaces.Algorithm;
import structures.Meeting;
import structures.UserInfo;

public class MemberAlg implements Algorithm {
	
	protected UserInfo user;
	protected ListWriteAlg listAlg;
	protected Meeting meeting;
	protected String answer;
	protected boolean isReady;
	
	public MemberAlg(ListWriteAlg alg, Meeting meet, UserInfo user, String startMessage) {
		listAlg = alg;
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
				listAlg.switchMainAlg(new UserAlg(listAlg, listAlg.getMeetingsMap(), user, "Ok, I've struck off you."));
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
		return answer;
	}

	public Algorithm genererateSame(UserInfo another) {
		return new MemberAlg(listAlg, meeting, another, "");
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
