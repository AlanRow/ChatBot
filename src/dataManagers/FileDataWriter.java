package dataManagers;

import bot_interfaces.DataWriter;
import java.io.*;

public class FileDataWriter implements DataWriter {

	//���� ��� ������
	private File file;
	
	public FileDataWriter (String fileName) throws IOException {
		file = new File(fileName);
		
		if (!file.exists())
			file.createNewFile();
	}
	
	//*����� ������*
	//����� �������� ��������� DataWriter, ������� ���� ����� �� writeAllData(Map <String, List<String>>)
	//������� ����������� ���� ����, �. �. 
	public void writeData(String key, String data) throws IOException {
		try (FileWriter writer = new FileWriter(file, true))
		{
			writer.write("<" + key + ">:<" + data + ">");
			writer.append("\r\n");
			writer.flush();
		}
	}

	public void clearData() throws IOException {
		try (FileWriter writer = new FileWriter(file))
		{
			writer.write("");
			writer.flush();
		}
	}
	
}
