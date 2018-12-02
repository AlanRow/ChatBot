package algs;

import java.io.IOException;
import java.util.List;

import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
import structures.Meeting;
import structures.UserInfo;

public class OwnerAlg extends HostAlg {

	public OwnerAlg(Meeting meet, UserInfo user, String startMessage) {
		super(meet, user, "Hello, you are owner of this meeting.");
	}
	
	@Override
	public String getMemberType() {
		return "owner";
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
		
			case "sethost":
				if (commands.length < 2 || commands[1] == null || commands[1] == "") {
					answer = "Please, type the name of user, which you want to appoint as a host.";
					break;
				}
				
				String name = commands[1];
				for (int i = 2; i < commands.length; i++)
					name += " " + commands[i];
					
				MemberAlg willHost = meeting.getMemberByName(name);
					
				if (willHost == null) {
					answer = "Sorry, the member with name <" + name + "> has unfound in meeting-list.";
					break;
				}
					
				if (user.equals(willHost.getUser())) {
					answer = "You're the owner. It's cooler. than host.";
					break;
				}
				
				if (willHost instanceof HostAlg) {
					answer = "This member is host already.";
					break;
				}
						
				try {
					meeting.appointAsHost(new HostAlg(meeting, willHost.getUser(), "You've appointed as a host."));
					answer = "User <" + willHost.getUser().getName() + "> has appointed as a host.";
				} catch (IOException | UncorrectDataException | UnfoundedDataException ex) {
					answer = "Sorry, the file-working failed...";
				}
				break;
				
			case "dehost":
				if (commands.length < 2 || commands[1] == null || commands[1] == "") {
					answer = "Please, type the name of user, which you want to relegate from hosts.";
					break;
				}
				
				String hostName = commands[1];
				for (int i = 2; i < commands.length; i++)
					hostName += " " + commands[i];
					
				HostAlg wontHost = meeting.getHostByName(hostName);
				
				if (wontHost == null) {
					answer = "Sorry, the member with name <" + hostName + "> has unfound in hosts-list.";
					break;
				}
					
				if (user.equals(wontHost.getUser())) {
					answer = "You're the owner and you cannot to exclude yourself from hosts. If you want to delegate your authority, then try to type \"delegate [user_name]\".";
					break;
				}
				
				try {
					meeting.relegateHost(wontHost.getUser(), "Sorry, you've excluded from hosts of meeting.");
				} catch (IOException | UncorrectDataException | UnfoundedDataException e) {
					answer = "Sorry, the file-working failed...";
				}
				
				answer = "Ok, host has excluded";
				break;
				
			case "delegate":
				if (commands.length < 2 || commands[1] == null || commands[1] == "") {
					answer = "Please, type the name of user, which you want to relegate from hosts.";
					break;
				}
				
				String newOwnerName = commands[1];
				for (int i = 2; i < commands.length; i++)
					newOwnerName += " " + commands[i];
					
				MemberAlg newOwner = meeting.getMemberByName(newOwnerName);
				
				if (newOwner == null) {
					answer = "Sorry, the member with name <" + newOwnerName + "> has unfound in meeting-list.";
					break;
				}
					
				if (user.equals(newOwner.getUser())) {
					answer = "Winnie, it's your home!.";
					break;
				}
				
			try {
				meeting.setOwner(newOwner.getUser());
				answer = "Ok, the user <"+ newOwner.getUser().getName() + "> has became owner.";
			} catch (IOException | UncorrectDataException | UnfoundedDataException e) {
				answer = "Sorry, the file-working failed...";
			}
			
			break;
			
			case "setpswd":
				if (commands.length < 2 || commands[1] == null || commands[1] == "") {
					answer = "If you want to meke the meeting public then type <set none>.";
					break;
				}
				
				if (commands[1].toLowerCase().equals("none")) {
					meeting.setPassword(null);
					answer = "Ok, meeting is public.";
					break;
				}
					
				meeting.setPassword(commands[1]);
				answer = "Ok, password is saved.";
				break;
				
			case "setinfo":
				if (commands.length < 2 || commands[1] == null || commands[1] == "") {
					answer = "Type after <info> the description of meeting or <none> (if you want to clear the info)";
					break;
				}
				
				String newInfo = commands[1];

				for (int i = 2; i < commands.length; i++)
					newInfo += " " + commands[i];
				
				if (newInfo.toLowerCase().equals("none")) {
					meeting.setInfo(null);
					answer = "Description has cleared";
					break;
				}
					
				meeting.setInfo(newInfo);
				answer = "Description has saved";
				break;
				
			case "help":
				answer = "Commands list:\n" +
						"help - show this help-list\n" +
				  /*no*/"info - show the information about meeting (name, time, place, etc.)\n" +
						"show - show the list of meeting members\n" +
					/*no*/"exclude [username] - exclude some user from meeting\n"+
						"dehost [username] - if you want to refuse somebody from hosting meeting\n" +
					/*no*/"setpswd [password]- set the password of meeting. Type <set none> if you want to make the meeting public.\n" +
					/*no*/"setinfo [info] - set information about meeting (name, time, place, etc.)\n" +
						"sethost [username] - for adding some user to hosts of meeting\n"+
						"delegate [username] - delegate yor ownership to some member or host\n";
				break;
				
			default:
				isReady = false;
				super.readMessage(message);	
		}
	}
}
