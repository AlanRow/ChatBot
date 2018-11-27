package bot.interfaces;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;

//абстрактный класс, который объединяет Corrector и Searcher
public abstract class DataManager implements DataCorrector, DataSearcher {
	
	//required optimization in the VrtualDM class (by using internal map)
	public void writeData(String key, List<String> value) throws IOException, UncorrectDataException {
		Map<String, List<String>> data = getAllData();
		data.put(key, value);
		
		writeAllData(data);
	}

	//required optimization in the VrtualDM class (by using internal map)
	public void writeData(String key, int valueNumber, String value) throws IOException, UncorrectDataException, UnfoundedDataException {
		Map<String, List<String>> data = getAllData();
		List<String> vals = data.get(key);
		
		if (vals == null)
			throw new UnfoundedDataException("The data with key <" + key + "> wasn't found in the file.");
		
		if (vals.size() < valueNumber)
			throw new UnfoundedDataException("The data with key <" + key + "> haven't " + valueNumber + "'th value.");
		
		vals.set(valueNumber - 1, value);
		
		writeAllData(data);
	}
}
