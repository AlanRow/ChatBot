package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import dataManagers.FileDataReader;
import dataManagers.FileDataWriter;
import dataManagers.VirtualDataManager;
import exceptions.UnCorrectDataException;
import exceptions.UnfoundedDataException;

class VirtualDataManagerTests {

	@Test
	void getAllDataTest() throws IOException, UnCorrectDataException {
		File file = new File("vdmTest.txt");
		VirtualDataManager manager = initializeDM(file, "<key1>:<value1>\r\n<key2>:<value2>\r\n<key1>:<value3>\r\n");
		
		Map<String, List<String>> data = manager.getAllData();
		
		assertEquals(data.keySet().toArray()[0], "key1");
		assertEquals(data.keySet().toArray()[1], "key2");
		assertEquals(data.get("key1").get(0), "value1");
		assertEquals(data.get("key2").get(0), "value2");
		assertEquals(data.get("key1").get(1), "value3");
		
		file.delete();
	}
	
	@Test
	void getDataTest() throws IOException, UnCorrectDataException {
		File file = new File("vdmTest.txt");
		VirtualDataManager manager = initializeDM(file, "<key1>:<value1>\r\n<key2>:<value2>\r\n<key1>:<value3>\r\n");
		
		assertEquals(manager.getData("key1").get(0), "value1");
		assertEquals(manager.getData("key2").get(0), "value2");
		assertEquals(manager.getData("key1").get(1), "value3");
		file.delete();
	}
	

	@Test
	void clearDataTest() throws IOException, UnCorrectDataException {
		File file = new File("vdmTest.txt");
		VirtualDataManager manager = initializeDM(file, "<key1>:<value1>\r\n<key2>:<value2>\r\n<key1>:<value3>\r\n");
		
		manager.clearData();
		try (FileReader reader = new FileReader(file))
		{
			assertEquals(true, reader.read() < 0);
		}
		
		file.delete();
	}
	
	@Test
	void writeDataTest() throws IOException, UnCorrectDataException {
		File file = new File("vdmTest.txt");
		VirtualDataManager manager = initializeDM(file, "");
		
		manager.writeData("someKey","someValue");
		try (FileReader reader = new FileReader(file))
		{
			assertEquals("<someKey>:<someValue>\r\n", readFile(reader));
		}
		
		file.delete();
	}
	
	@Test
	void removeDataTest() throws IOException, UnCorrectDataException, UnfoundedDataException {
		File file = new File("vdmTest.txt");
		VirtualDataManager manager = initializeDM(file, "<key1>:<value1>\r\n<key2>:<value2>\r\n<key1>:<value3>\r\n<key2>:<value4>\r\n");
		
		manager.removeData("key1");
		try (FileReader reader = new FileReader(file))
		{
			assertEquals("<key2>:<value2>\r\n<key2>:<value4>\r\n", readFile(reader));
		}
		
		manager.removeData("key2", "value2");
		try (FileReader reader = new FileReader(file))
		{
			assertEquals("<key2>:<value4>\r\n", readFile(reader));
		}
		
		file.delete();
	}
	
	private static String readFile(FileReader reader) throws IOException
	{
		String fileText = "";
		int code;
		
		while ((code = reader.read()) >= 0)
		{
			fileText += (char) code;
		}
		
		return fileText;
	}
	
	private VirtualDataManager initializeDM(File file, String data) throws IOException, UnCorrectDataException
	{
		try (FileWriter writer = new FileWriter(file))
		{
			writer.write(data);
			writer.flush();
		}
		
		return new VirtualDataManager(new FileDataReader(file.getName()), new FileDataWriter(file.getName()));
	}
}
