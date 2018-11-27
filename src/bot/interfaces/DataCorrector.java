package bot.interfaces;

import java.io.IOException;

import exceptions.UnfoundedDataException;

//��������� ���������� DataWriter
public interface DataCorrector extends DataWriter
{
	//�������� ������ �� �����
	public void removeData(String key) throws IOException;
	
	//�������� ������ �� ����� � ������ ��������: 1 - id, 2 - username ��� ���, 3 - ����� ������� (0 ��� 1)
	//4 - ������ (user, member, host)
	//public void removeData(String key, int dataNumber) throws UnfoundedDataException, IOException;
}
