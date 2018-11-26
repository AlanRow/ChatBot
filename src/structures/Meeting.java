package structures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import algs.HostAlg;
import algs.ListWriteAlg;
import algs.MemberAlg;
import algs.UserAlg;
import bot_interfaces.DataManager;
import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;

//класс встречи. инкапсулирует всю организацию и работу с базами данных, 
//позволяя работать с конкретной встречей.
public class Meeting {
	private int meetingId;//уникален. Не должно быть двух встреч с одинаковым id.
	private DataManager userList;
	private List<MemberAlg> members;
	private List<HostAlg> hosts;
	private Map<Long, ListWriteAlg> subscribes; 
	
	public Meeting(int id, DataManager users) throws IOException, UncorrectDataException {
		meetingId = id;
		userList = users;
		subscribes = new HashMap<Long, ListWriteAlg>();
		
		members = new ArrayList<MemberAlg>();
		hosts = new ArrayList<HostAlg>();
		
		//adds all user who are in meetings file yet.
		for (Map.Entry<String, List<String>> pair : userList.getAllData().entrySet()) {
			List<String> info = pair.getValue();
			if (Integer.parseInt(info.get(1)) == meetingId) {
				MemberAlg member;
				switch (info.get(2)) {
					case "member":
						member = new MemberAlg(this, new UserInfo(Long.parseLong(pair.getKey()), info.get(0)), "");
						break;
					case "host":
						HostAlg host = new HostAlg(this, new UserInfo(Long.parseLong(pair.getKey()), info.get(0)), "");
						hosts.add(host);
						member = host;
						break;
					default:
						throw new UncorrectDataException("The state of the user with name <" + info.get(0) + "> isn't a host or a member");
				}
				members.add(member);
			}
		}
	}
	
	public int getId() {
		return meetingId;
	}
	
	public List<UserInfo> getUsers() {
		List<UserInfo> users = new ArrayList<UserInfo>();
		
		for (MemberAlg member : members)
			users.add(member.getUser());
		
		return users;
	}
	
	public MemberAlg getMember(UserInfo user) {
		for (MemberAlg member : members) {
			if (member.getUser().equals(user))
				return member;
		}
		
		return null;
	}
	
	//REQUIRED CHANGING: CHECK THAT MEMBER-MEETING_ID CORRESPONDS TO THIS MEETING
	public void addMember(MemberAlg newMember, ListWriteAlg memberAlg) throws IOException, UncorrectDataException {
		if (!members.contains(newMember)) {
			//adds user to file
			UserInfo user = newMember.getUser();
			List<String> info = new ArrayList<String>();
			info.add(user.getName());
			info.add(Integer.toString(meetingId));
			info.add("member");
			userList.writeData(Long.toString(user.getId()), info);
			members.add(newMember);
			
			subscribe(memberAlg);
			if (subscribes.get(newMember.getUser().getId()) == null)
				System.out.println("algs is null!!!");;
			subscribes.get(newMember.getUser().getId()).switchMainAlg(newMember);
		}
	}
	
	public void subscribe(ListWriteAlg listUser) {
		subscribes.put(listUser.getUser().getId(), listUser);
	}
	
	public void removeMember(UserInfo user, String goodbyeMessage) throws IOException, UncorrectDataException, UnfoundedDataException {
		
		relegateHost(user, "");
		
		userList.removeData(Long.toString(user.getId()));
		Iterator i = members.iterator();
		while (i.hasNext()) {
			if (((MemberAlg)i.next()).getUser().equals(user)) {
				i.remove();
				ListWriteAlg alg = subscribes.get(user.getId());
				if (alg != null)
					alg.switchMainAlg(new UserAlg(alg, alg.getMeetingsMap(), user, goodbyeMessage));
				break;
			}
		}
		
		desubscribe(user);
	}
	
	public void appointAsHost(HostAlg newHost) throws IOException, UncorrectDataException, UnfoundedDataException {

		userList.writeData(Long.toString(newHost.getUser().getId()), 3, "host");
		
		UserInfo user = newHost.getUser();
		Iterator i = members.iterator();
		while (i.hasNext()) {
			if (((MemberAlg)i.next()).getUser().equals(user)) {
				i.remove();
				break;
			}
		}
		
		members.add(newHost);
		if (!hosts.contains(newHost))
			hosts.add(newHost);
		ListWriteAlg alg = subscribes.get(user.getId());
		if (alg != null)
			alg.switchMainAlg(newHost);
	}
	
	public void relegateHost(UserInfo host, String relegateMessage) throws IOException, UncorrectDataException, UnfoundedDataException {
		
		userList.writeData(Long.toString(host.getId()), 3, "member");
		
		MemberAlg relegated = new MemberAlg(this, host, relegateMessage);
		Iterator i = members.iterator();
		while (i.hasNext()) {
			if (((MemberAlg)i.next()).getUser().equals(host)) {
				i.remove();
				break;
			}
		}
		members.add(relegated);
		
		Iterator j = hosts.iterator();
		while (j.hasNext()) {
			if (((HostAlg)j.next()).getUser().equals(host)) {
				j.remove();
				ListWriteAlg alg = subscribes.get(host.getId());
				if (alg != null)
					alg.switchMainAlg(relegated);
				break;
			}
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Meeting))
			return false;
		
		return meetingId == ((Meeting)obj).meetingId;
	}
	
	@Override
	public int hashCode() {
		return meetingId;
	}

	private void desubscribe(UserInfo user) {
		subscribes.remove(user.getId());
	}
}
