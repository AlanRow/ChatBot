package bot.interfaces;

import java.util.List;

import exceptions.UnfoundedDataException;
import structures.InfoPair;

//улучшение DataReader
public interface DataSearcher extends DataReader {
	//читать ключ-данные по ключу
	public InfoPair getData(String key) throws UnfoundedDataException;
}
