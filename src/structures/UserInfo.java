package structures;

import org.telegram.telegrambots.api.objects.User;

//класс содержащий всю информацию о пользователе, будь то пользователь в Telegram или в ¬ 
public class UserInfo {
	private long id; //id - уневерсальный идентификатор пользовател€
	private String userName; //ник пользовател€
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return userName;
	}
	
	public UserInfo(long userId, String userName) {
		id = userId;
		this.userName = userName;
	}
	
	public UserInfo(User user) {
		id = user.getId();
		userName = (user.getUserName() == null) ? user.getFirstName() : "@" + user.getUserName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		
		if (obj instanceof UserInfo)
			return id == ((UserInfo)obj).getId();//сравниваем пользователей по id
		//примечание: хорошо бы сделать id уникальным и дл€ разных платформ
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int) id;
	}
}
