package bot_interfaces;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import exceptions.UncorrectDataException;

//интерфейс, способный читать информацию из какого-то источника
public interface DataReader {

	public Map<String, List<String>> getAllData() throws IOException, UncorrectDataException;
}
