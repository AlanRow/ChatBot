package algs;

import java.util.List;

import structures.Meeting;
import structures.UserInfo;

public class HostAlg extends MemberAlg {
	
	public HostAlg(ListWriteAlg alg, Meeting meet, UserInfo user, String startMessage) {
		super(alg, meet, user, startMessage);
	}
	
	@Override
	public void readMessage(String message) {
		isReady = true;
		switch (message.toLowerCase()) {
			case "show":
				List<UserInfo> users = meeting.getUsers();
				for(UserInfo user : users) {
					answer += user.getName() + "\n";
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

}
