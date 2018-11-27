package bot.interfaces;

import java.util.List;

import structures.UserInfo;

/*��������, ������, ����������� �� ���������� ����� �������������. �������� ������ �� 
 * ������ �������� ���������, � ����� ����� ��������� ����������� �����������*/

public interface UserControl {
	//����� ��������, ��������� �� ����� ������������
	public boolean areNewUsers();
	//����� ���������� ������ ����� �������������
	public List<UserInfo> getNewUsers();
}
