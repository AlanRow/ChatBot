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
		
		String[] commands = message.toLowerCase().split(" ");
		if (commands.length == 0) {
			isReady = false;
			return;
		}
			
		switch (commands[0]) {
		
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
					meeting.appointAsHost(new HostAlg(meeting, user, "You've appointed as a host."));
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
			} catch (IOException | UncorrectDataException | UnfoundedDataException e) {
				answer = "Sorry, the file-working failed...";
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
