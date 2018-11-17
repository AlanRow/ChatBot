package dataManagers;

import bot_interfaces.DataCorrector;
import bot_interfaces.DataManager;
import bot_interfaces.DataReader;
import bot_interfaces.DataSearcher;
import bot_interfaces.DataWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import exceptions.*;

/*����� ��� �������� � �������������� ������������ ��������� ���� ������ �� 
���� ����-�������� � ���� ���-�������. ������������� ��� ������ � �����������.
���������� ������ � ������ ����������� ������ (�������, ���������� � �. �.)
������ ��������*/ 
public class VirtualDataManager extends DataManager{

	//���-������� ������
	private Map<String, List<String>> data;
	//������������ ��� ���������� ����������
	private DataWriter dataSaver;
	//������������ ���� ��� �������� ������ ��� ��������� ����������. ������ ������ ����� ���� ������� �� ������������.
	private DataReader dataSource;

	public VirtualDataManager(DataReader source, DataWriter saver) throws IOException, UncorrectDataException
	{
		dataSaver = saver;
		dataSource = source;
		data = source.getAllData();
		updateData();
	}
	
	public VirtualDataManager(String fileName) throws IOException, UncorrectDataException
	{
		dataSaver = new FileDataWriter(fileName);
		dataSource = new FileDataReader(fileName);
		data = dataSource.getAllData();
		updateData();
	}
	
	public List<String> getData(String key) {
		return data.get(key);
	}

	public Map<String, List<String>> getAllData() {
		return data;
	}

	public void writeData(String key, String newData) throws IOException {
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

	public void clearData() throws IOException {
		data.clear();
		updateData();
	}

	public void removeData(String key) throws IOException {
		data.remove(key);
		updateData();
	}

	public void removeData(String key, String removingData) throws UnfoundedDataException, IOException {
		if (!data.containsKey(key))
			throw new UnfoundedDataException("The key " + key + " has not found in data.");
		data.get(key).remove(removingData);
		
		updateData();
	}
	
	//�����, ����������� ��������� � ���� ������
	private void updateData() throws IOException
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
