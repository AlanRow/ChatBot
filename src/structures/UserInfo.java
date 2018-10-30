package structures;

import org.telegram.telegrambots.api.objects.User;

public class UserInfo {
	private long id;
	private String name;
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public UserInfo(int userId, String userName) {
		id = userId;
		name = userName;
	}
	
	public UserInfo(User user) {
		id = user.getId();
		name = user.getUserName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		
		if (obj instanceof UserInfo)
			return id == ((UserInfo)obj).getId();
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int) id;
	}
}
