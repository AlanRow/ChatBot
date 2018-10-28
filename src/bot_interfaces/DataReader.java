package bot_interfaces;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import exceptions.UnCorrectDataException;

public interface DataReader {

	public Map<String, List<String>> getAllData() throws IOException, UnCorrectDataException;
}
