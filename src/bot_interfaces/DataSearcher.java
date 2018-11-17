package bot_interfaces;

import java.util.List;

//улучшение DataReader
public interface DataSearcher extends DataReader {
	//читать данные по ключу
	public List<String> getData(String key);
}
