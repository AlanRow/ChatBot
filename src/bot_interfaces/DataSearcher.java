package bot_interfaces;

import java.util.List;
import structures.InfoPair;

//��������� DataReader
public interface DataSearcher extends DataReader {
	//������ ����-������ �� �����
	public InfoPair getData(String key);
}
