package algs;

import java.io.IOException;
import java.util.List;

import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
import exceptions.tryToRemoveOwnerException;
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
		
		String[] commands = message.split(" ");
		if (commands.length == 0) {
			isReady = false;
			return;
		}
			
		switch (commands[0].toLowerCase()) {
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
					answer = "Now, you're not a host.";
				} catch (IOException | UncorrectDataException ex) {
					answer = "Sorry, the file-working failed...";
				} catch (UnfoundedDataException ex) {
					answer = "Sorry, I haven't found you in file...";
				}
				break;
				
			case "exclude":
				if (commands.length < 2 || commands[1] == null || commands[1] == "") {
					answer = "Please, type the name of user, who you want to exclude...";
					break;
				}
				
				String memberName = commands[1];
				for (int i = 2; i < commands.length; i++)
					memberName += " " + commands[i];
					
				MemberAlg removing = meeting.getMemberByName(memberName);
				
				if (removing == null) {
					answer = "Here isn't the person with name <" + memberName + ">.";
					break;
				}
					
				if (memberName.equals(user.getName())) {
					answer = "You try to shot in yourself leg. If you want to exit, then using <wont>";
					break;
				}
				
				if (removing instanceof HostAlg) {
					answer = "You try to exclude other host. You cannot do this";
					break;
				}
				
				try {
					meeting.removeMember(removing.getUser(), "Sorry, you've excluded from hosts of meeting.");
					answer = memberName + " has excluded.";
				} catch (IOException | UncorrectDataException | UnfoundedDataException e) {
					answer = "Sorry, the file-working failed...";
				} catch (tryToRemoveOwnerException ex) {
					answer = "You try to remove owner of meeting.";
				}
				break;
				
			case "help":
				answer = "Commands list:\n" +
						"help - show this help-list\n" +
						"info - show the information about meeting (name, time, place, etc.)\n" +
						"wont - struck off you from list\n" +
						"show - show the list of meeting members\n" +
						"dehost - if you want to refuse from hosting of meeting\n" +
						"exclude [username] - exclude some user from meeting\n";
				break;
			default:
				isReady = false;
				super.readMessage(message);	
		}
	}

}
