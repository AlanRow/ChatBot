package bot_interfaces;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//���������, ��������� ������ ������ ����-����
public interface DataWriter {
	//������ ������ � ���� ����-��������
	public void writeAllData(Map<String, List<String>> data) throws IOException;
	//�������� ������
	public void clearData() throws IOException;
}
