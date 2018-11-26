package structures;

import org.telegram.telegrambots.api.objects.User;

//����� ���������� ��� ���������� � ������������, ���� �� ������������ � Telegram ��� � ��
public class UserInfo {
	private long id; //id - ������������� ������������� ������������
	private String userName; //��� ������������
	
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
			return id == ((UserInfo)obj).getId();//���������� ������������� �� id
		//����������: ������ �� ������� id ���������� � ��� ������ ��������
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int) id;
	}
}
