package structures;

import java.util.List;
//����� ���������� ���������� ���� ����-��������, ��������������� ����� ������ �����
public class InfoPair {
	public String key;
	public List<String> values;
	
	public InfoPair(String setKey, List<String> setValues) {
		key = setKey;
		values = setValues;
	}
}
