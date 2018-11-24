package structures;

import java.util.List;
//класс содержащий информацию типа ключ-значение, соответствующую одной строке файла
public class InfoPair {
	public String key;
	public List<String> values;
	
	public InfoPair(String setKey, List<String> setValues) {
		key = setKey;
		values = setValues;
	}
}
