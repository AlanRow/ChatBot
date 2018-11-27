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
	
	//������� ����������� �������� ������ �� ������ ������ �����
	public VirtualDataManager(String fileName) throws IOException, UncorrectDataException
	{
		dataSaver = new FileDataWriter(fileName);
		dataSource = new FileDataReader(fileName);
		data = dataSource.getAllData();
		updateData();
	}

	//�������� Map ���� ������
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

	//������� ������ � ����� � � Map'e
	public void clearData() throws IOException {
		data.clear();
		updateData();
	}

	//������� ��� ������ �� ��������� �����
	public void removeData(String key) throws IOException {
		data.remove(key);
		updateData();
	}
	
	//�����, ����������� ��������� � ���� ������
	private void updateData() throws IOException
	{
		dataSaver.writeAllData(data);
	}

	
}
