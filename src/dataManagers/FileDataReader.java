package dataManagers;

import bot_interfaces.DataReader;
import exceptions.UncorrectDataException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//����� ��� ������ ���������� ���� ����-�������� �� �����
public class FileDataReader implements DataReader {

	//���� �� �������� ����� ������
	private File file;
	
	public FileDataReader (String fileName) throws IOException {
		file = new File(fileName);
		
		if (!file.exists())
			throw new IOException("There isn't file " + fileName + " for reading.");
	}
	
	//����� ������� ���� ���������� �� �����
	public Map<String, List<String>> getAllData() throws IOException, UncorrectDataException {
		
		Map<String, List<String>> keyMap = new HashMap<String, List<String>>();
		
		//*����� ������*
		//���������� ���������� �����, ����� �� �������� ������������ ����� �������� ����� �����
		//� ���������� exception, ����� �������� �����, ��� 2 � ����� ���� ����������� � ����� ������
		//��������� UncorrectDataException
		try(FileReader reader = new FileReader(file))
		{
			String key = "";
			//������ ���� �� ������ �� ����� �����
			while ((key = readWord(reader)) != null) //readWord ������ ����� ����� ������ <...>
			{
				if ((char)reader.read() != ':')//���� ����� ����� �� ����� ':', �� ������ ����������
					throw new UncorrectDataException("The file <" + file.getName() + "> hasn't the ':' after key.");
				String value = readWord(reader);
				
				//���� ����� ����� �� ���� �������� ������ ����������
				if (value == null)
					throw new UncorrectDataException("The file <" + file.getName() + "> hasn't value for key.");
				
				//��������� ����� ������� � ����������� ��� ������
				if (!keyMap.containsKey(key))
					keyMap.put(key, new ArrayList<String>());
				
				keyMap.get(key).add(value);
				
				//��������� ������� ������ � ����� ������
				if ((char)reader.read() != '\r')
					throw new UncorrectDataException("The file <" + file.getName() + "> hasn't \\r.");
			
				if ((char)reader.read() != '\n')
					throw new UncorrectDataException("The file <" + file.getName() + "> hasn't \\n.");
			}
			
			return keyMap;
		}
	}

	private static String readWord(FileReader reader) throws IOException, UncorrectDataException
	{
		int code;
		String word = "";
		
		if ((code = reader.read()) <= 0)
			return null;
		
		if ((char)code != '<')
			throw new UncorrectDataException("The word hasn't '<' at the start, but nas a '" + (char)code + "'.");
		
		
		while ((code = reader.read()) > 0 && (char)code != '>') {
			word += (char) code;
		}
		
		return word;
	}
}
