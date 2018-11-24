package bot_interfaces;

import java.util.List;
import structures.InfoPair;

//улучшение DataReader
public interface DataSearcher extends DataReader {
	//читать ключ-данные по ключу
	public InfoPair getData(String key);
}
