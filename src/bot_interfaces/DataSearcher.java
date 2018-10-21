package bot_interfaces;

import java.util.List;

public interface DataSearcher extends DataReader {
	
	public List<String> getData(String key);
}
