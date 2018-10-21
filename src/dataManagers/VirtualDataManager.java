package dataManagers;

import bot_interfaces.DataCorrector;
import bot_interfaces.DataSearcher;
import bot_interfaces.DataWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import exceptions.*;

/*класс для хранения и редактирования относительно небольшой базы данных по 
типу ключ-значения в виде хэш-таблицы. Инкапсулирует всю работу с информацией.
делегирует работу с самими источниками данных (файлами, интернетом и т. п.)
другим кллассам*/ 
public class VirtualDataManager implements DataCorrector, DataSearcher{

	//хэш-таблица данных
	private Map<String, List<String>> data;
	//используется для сохранения информации
	private DataWriter dataSaver;
	//используется лишь при создании класса для подгрузки информации. Вообще говоря может быть отличен от сохраняемого.
	private DataSearcher dataSource;

	public VirtualDataManager(DataSearcher source, DataWriter saver)
	{
		dataSaver = saver;
		dataSource = source;
		data = source.getAllData();
		
		updateData();
	}
	
	public List<String> getData(String key) {
		return data.get(key);
	}

	public Map<String, List<String>> getAllData() {
		return data;
	}

	public void writeData(String key, String newData) {
		if (data.containsKey(key))
			data.get(key).add(newData);
		else
		{
			List<String> newDataList = new ArrayList<String>();
			newDataList.add(newData);
			data.put(key, newDataList);
		}
		
		updateData();
	}

	public void clearData() {
		data.clear();
		updateData();
	}

	public void removeData(String key) {
		data.remove(key);
		updateData();
	}

	public void removeData(String key, String removingData) throws UnfoundedDataException {
		if (!data.containsKey(key))
			throw new UnfoundedDataException("The key " + key + " has not found in data.");
		data.get(key).remove(removingData);
		
		updateData();
	}
	
	//метод, сохраняющий изменения в базе данных
	private void updateData()
	{
		dataSaver.clearData();
		
		for (String key : data.keySet())
		{
			for (String value : data.get(key))
			{
				dataSaver.writeData(key, value);
			}
		}
	}
	
}
