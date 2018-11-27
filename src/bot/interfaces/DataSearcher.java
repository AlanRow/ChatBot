package bot.interfaces;

import java.util.List;

import exceptions.UnfoundedDataException;
import structures.InfoPair;

//��������� DataReader
public interface DataSearcher extends DataReader {
	//������ ����-������ �� �����
	public InfoPair getData(String key) throws UnfoundedDataException;
}
