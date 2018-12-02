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
import algs.OwnerAlg;
import algs.UserAlg;
import bot.interfaces.DataManager;
import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
import exceptions.tryToRemoveOwnerException;

//класс встречи. инкапсулирует всю организацию и работу с базами данных, 
//позволяя работать с конкретной встречей.
public class Meeting {
	private int meetingId;//уникален. Не должно быть двух встреч с одинаковым id.
	private String password;//password for host-abilities, if this equals to null, then meeting will be public
	private OwnerAlg owner;//owner of meeting. Owner should be one.
	private DataManager userList;
	private Map<Long, MemberAlg> members;
	private Map<Long, HostAlg> hosts;
	private Map<Long, ListWriteAlg> subscribes;
	private String info;
	
	public Meeting(int id, DataManager users) throws IOException, UncorrectDataException, UnfoundedDataException {
		owner = null;
		meetingId = id;
		userList = users;
		password = null;
		subscribes = new HashMap<Long, ListWriteAlg>();
		info = null;
		
		members = new HashMap<Long, MemberAlg>();
		hosts = new HashMap<Long, HostAlg>();
		
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
						hosts.put(host.getUser().getId(), host);
						member = host;
						break;
					case "owner":
						if (owner != null)
							throw new UncorrectDataException("In the list meeting number<" + id + "> has more than one owbers!!!");
						
						OwnerAlg ownerAlg = new OwnerAlg(this, new UserInfo(Long.parseLong(pair.getKey()), info.get(0)), "");
						setOwner(ownerAlg.getUser());
						member = ownerAlg;
						break;
					default:
						throw new UncorrectDataException("The state of the user with name <" + info.get(0) + "> isn't a host or a member or an owner");
				}
				members.put(member.getUser().getId(), member);
			}
		}
	}
	
	public Meeting(int id, DataManager users, String setPassword) throws IOException, UncorrectDataException, UnfoundedDataException {
		this(id, users);
		password = setPassword;
	}
	
	public int getId() {
		return meetingId;
	}
	
	public List<UserInfo> getUsers() {
		List<UserInfo> users = new ArrayList<UserInfo>();
		
		for (MemberAlg member : members.values())
			users.add(member.getUser());
		
		return users;
	}
	
	public MemberAlg getMember(UserInfo user) {
		return members.get(user.getId());
	}
	
	//better to change this return type to List<Member>, but the Meeting cannot to work with namesakes
	public MemberAlg getMemberByName(String name) {
		if (name == null)
			return null;
		
		for (MemberAlg member : members.values()) {
			if (name.equals(member.getUser().getName()))
				return member;
		}
		
		return null;
	}
	
	public HostAlg getHost(UserInfo user) {
		return hosts.get(user.getId());
	}
	
	public HostAlg getHostByName(String name) {
		if (name == null)
			return null;
		
		for (HostAlg host : hosts.values()) {
			if (name.equals(host.getUser().getName()))
				return host;
		}
		
		return null;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void setInfo(String newInfo) {
		info = newInfo;
	}
	
	//REQUIRED CHANGING: CHECK THAT MEMBER-MEETING_ID CORRESPONDS TO THIS MEETING
	public /*<SuperMember extends MemberAlg>*/ void addMember(/*SuperMember*/MemberAlg newMember, ListWriteAlg memberAlg) throws IOException, UncorrectDataException, UnfoundedDataException {
		//adds user to file
		UserInfo user = newMember.getUser();
		List<String> info = new ArrayList<String>();
		info.add(user.getName());
		info.add(Integer.toString(meetingId));
		info.add(newMember.getMemberType());
		userList.writeData(Long.toString(user.getId()), info);
			
		members.put(newMember.getUser().getId(), newMember);
			
		if (memberAlg != null) {
			subscribe(memberAlg);
			memberAlg.switchMainAlg(newMember);
		}
		
		//supporting code, that helps to create owner without creating meeting function
		if (owner == null)
			setOwner(user);
	}
	
	public /*<SuperMember extends MemberAlg>*/ void addMember(/*SuperMember*/ MemberAlg newMember) throws IOException, UncorrectDataException, UnfoundedDataException {
		ListWriteAlg alg = subscribes.get(newMember.getUser().getId());
		addMember(newMember, alg);
	}
	
	public boolean isPublic() {
		return password == null;
	}
	
	public boolean checkPassword(String checking) {
		return isPublic() || (checking != null && checking.equals(password));
	}
	
	public void setPassword(String newPassword) {
		if ("none".equals(newPassword))
			password = null;
		else
			password = newPassword;
	}
	
	public void subscribe(ListWriteAlg listUser) {
		subscribes.put(listUser.getUser().getId(), listUser);
	}
	
	public void removeMember(UserInfo user, String goodbyeMessage) throws IOException, UncorrectDataException, UnfoundedDataException, tryToRemoveOwnerException {
		
		if (owner != null && owner.getUser().equals(user))
			throw new tryToRemoveOwnerException("");
		
		relegateHost(user, "");
		
		userList.removeData(Long.toString(user.getId()));
		
		ListWriteAlg alg = subscribes.get(user.getId());
		if (alg != null)
			alg.switchMainAlg(new UserAlg(alg, alg.getMeetingsMap(), user, goodbyeMessage));
		
		members.remove(user.getId());
		desubscribe(user);
	}
	
	public void appointAsHost(HostAlg newHost) throws IOException, UncorrectDataException, UnfoundedDataException {		
		addMember(newHost);
		hosts.put(newHost.getUser().getId(), newHost);
	}
	
	public void relegateHost(UserInfo host, String relegateMessage) throws IOException, UncorrectDataException, UnfoundedDataException {
		
		addMember(new MemberAlg(this, host, relegateMessage));
		hosts.remove(host.getId());
	}
	
	public OwnerAlg getOwner() {
		return owner;
	}
	
	public void setOwner(UserInfo newOwner) throws IOException, UncorrectDataException, UnfoundedDataException {
		if (owner != null)
			appointAsHost(new HostAlg(this, owner.getUser(), ""));//previous owner becaming simle host
		
		OwnerAlg ownerAlg = new OwnerAlg(this, newOwner, "");
		owner = ownerAlg;
		addMember(ownerAlg);
		appointAsHost(ownerAlg);
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
