package structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import algs.HostAlg;
import algs.MemberAlg;
import algs.UserAlg;
import bot_interfaces.DataManager;

//класс встречи. инкапсулирует всю организацию и работу с базами данных, 
//позволяя работать с конкретной встречей.
public class Meeting {
	private int meetingId;//уникален. Не должно быть двух встреч с одинаковым id.
	private DataManager userList;
	private List<MemberAlg> members;
	private List<HostAlg> hosts;
	
	public Meeting(int id, DataManager users) {
		meetingId = id;
		userList = users;
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
	
	public void addMember(MemberAlg newMember) {
		if (!members.contains(newMember))
			members.add(newMember);
	}
	
	public void removeMember(UserInfo user) {
		
		relegateHost(user);
		
		Iterator i = members.iterator();
		while (i.hasNext()) {
			if (((MemberAlg)i.next()).getUser().equals(user)) {
				i.remove();
				break;
			}
		}
	}
	
	public void addHost(HostAlg newHost) {
		addMember(newHost);
		
		if (!hosts.contains(newHost))
			hosts.add(newHost);
	}
	
	public void relegateHost(UserInfo host) {
		Iterator i = hosts.iterator();
		while (i.hasNext()) {
			if (((HostAlg)i.next()).getUser().equals(host)) {
				i.remove();
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

}
