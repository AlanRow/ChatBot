package bot_interfaces;

/*��������� ������, ����������� �� �������� ��������� ������������. ������������� ��� 
 ������ � ���������� ������ �������� ��������� (��, ��������, ������� � ��.). �� ���������� 
 ������� ��������� ���. ��� ������� ������������ ��������� talker*/

public interface MessageController {
	
	//���������� ������ message ������������
	public void send(String message);
	//���������, ���� �� ����� ��������� �� ������������
	public boolean areNewMessages();
	//���������� ������ ���� ����� ���������
	public String[] getNewMessages();
	//���������� ��������, ����������� � ������� talker'�
	public Algorithm getAlgorithm();
}
