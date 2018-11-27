package data.managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bot.interfaces.DataCorrector;
import bot.interfaces.DataManager;
import bot.interfaces.DataReader;
import bot.interfaces.DataSearcher;
import bot.interfaces.DataWriter;
import exceptions.*;
import structures.InfoPair;

/*класс для хранения и редактирования относительно небольшой базы данных по 
типу ключ-значения в виде хэш-таблицы. Инкапсулирует всю работу с информацией.
делегирует работу с самими источниками данных (файлами, интернетом и т. п.)
другим кллассам*/ 
public class VirtualDataManager extends DataManager{

	//хэш-таблица данных
	private Map<String, List<String>> data;
	//используется для сохранения информации
	private DataWriter dataSaver;
	//используется лишь при создании класса для подгрузки информации. Вообще говоря может быть отличен от сохраняемого.
	private DataReader dataSource;

	public VirtualDataManager(DataReader source, DataWriter saver) throws IOException, UncorrectDataException
	{
		dataSaver = saver;
		dataSource = source;
		data = source.getAllData();
		updateData();
	}
	
	//создает виртуальный менеджер данных на основе одного файла
	public VirtualDataManager(String fileName) throws IOException, UncorrectDataException
	{
		dataSaver = new FileDataWriter(fileName);
		dataSource = new FileDataReader(fileName);
		data = dataSource.getAllData();
		updateData();
	}

	//получает Map всех данных
	public Map<String, List<String>> getAllData() {
		return data;
	}
	
	//writes to file data-map
	public void writeAllData(Map<String, List<String>> data) throws IOException {
		this.data = data;
		updateData();
	}

	//return the pair of key-value data
	public InfoPair getData(String key) throws UnfoundedDataException {
		List<String> value = data.get(key);
		if (value == null)
			throw new UnfoundedDataException("The data with key <" + key + "> wasn't found in the file.");
		
		return new InfoPair(key, value);
	}

	//очищает данные в файле и в Map'e
	public void clearData() throws IOException {
		data.clear();
		updateData();
	}

	//убирает все данные по заданному ключу
	public void removeData(String key) throws IOException {
		data.remove(key);
		updateData();
	}
	
	//метод, сохраняющий изменения в базе данных
	private void updateData() throws IOException
	{
		dataSaver.writeAllData(data);
	}

	
}
