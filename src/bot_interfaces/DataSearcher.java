package bot_interfaces;

import java.util.List;

//��������� DataReader
public interface DataSearcher extends DataReader {
	//������ ������ �� �����
	public List<String> getData(String key);
}
