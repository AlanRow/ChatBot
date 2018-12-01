package algs;

import java.io.IOException;
import java.util.List;

import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
import structures.Meeting;
import structures.UserInfo;

public class HostAlg extends MemberAlg {
	
	public HostAlg(/*ListWriteAlg alg,*/ Meeting meet, UserInfo user, String startMessage) {
		super(/*alg,*/ meet, user, startMessage);
	}
	
	@Override
	public String getMemberType() {
		return "host";
	}
	
	@Override
	public void readMessage(String message) {
		isReady = true;
		
		if ("".equals(message)) {
			isReady = false;
			return;
		}
		
		String[] commands = message.toLowerCase().split(" ");
		if (commands.length == 0) {
			isReady = false;
			return;
		}
			
		switch (commands[0]) {
			case "show":
				List<UserInfo> users = meeting.getUsers();
				answer = "";
				for(UserInfo user : users) {
					answer += user.getName() + "\n";
				}
				
				if ("".equals(answer))
					answer = "list is empty...";
				
				break;
			case "dehost":
				try {
					meeting.relegateHost(user, "Now, you're not a host.");
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
						"show - show the list of meeting members\n" +
						"dehost - if you want to refuse from hosting meeting\n";
				break;
			default:
				super.readMessage(message);	
		}
	}

}
