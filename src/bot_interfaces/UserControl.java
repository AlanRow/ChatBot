package bot_interfaces;

/*��������, ������, ����������� �� ���������� ����� �������������. �������� ������ �� 
 * ������ �������� ���������, � ����� ����� ��������� ����������� �����������*/

public interface UserControl {
	//����� ��������, ��������� �� ����� ������������
	public boolean areNewUsers();
	//����� ���������� ������ ���� (��� ���������������) ����� �������������
	public String[] getNewUsers();
}
