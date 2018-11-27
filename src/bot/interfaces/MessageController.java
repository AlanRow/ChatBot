package bot.interfaces;

import java.util.List;

import structures.UserInfo;

/*��������� ������, ����������� �� �������� ��������� ������������. ������������� ��� 
 ������ � ���������� ������ �������� ��������� (��, ��������, ������� � ��.). �� ���������� 
 ������� ��������� ���. ��� ������� ������������ ��������� controller*/

public interface MessageController {
	
	//���������� ������ message ������������
	public void send(String message);
	//���������, ���� �� ����� ��������� �� ������������
	public boolean areNewMessages();
	//���������� ������ ���� ����� ���������
	public List<String> getNewMessages();
	//���������� ��������, ����������� � ������� controller'�
	public Algorithm getAlgorithm();
	//���������� ������������, � ������� �������� Controller
	public UserInfo getUser();
	//��������� ����� ������ �� talker'� � same-����������, �� � ������ �������������.
	public MessageController genererateSame(UserInfo another);
}
