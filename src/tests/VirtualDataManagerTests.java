package Tests;

import static org.junit.jupiter.api.Assertions.*;

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
		VirtualDataManager manager = initializeDM("vdmTest2.txt", "<key1>:<value1>\r\n<key2>:<value2>\r\n<key1>:<value3>\r\n");
		
		Map<String, List<String>> data = manager.getAllData();
		
		assertEquals(data.keySet().toArray()[0], "key1");
		assertEquals(data.keySet().toArray()[1], "key2");
		assertEquals(data.get("key1").get(0), "value1");
		assertEquals(data.get("key2").get(0), "value2");
		assertEquals(data.get("key1").get(1), "value3");
	}
	
	@Test
	void getDataTest() throws IOException, UnCorrectDataException {
		VirtualDataManager manager = initializeDM("vdmTest2.txt", "<key1>:<value1>\r\n<key2>:<value2>\r\n<key1>:<value3>\r\n");
		
		assertEquals(manager.getData("key1").get(0), "value1");
		assertEquals(manager.getData("key2").get(0), "value2");
		assertEquals(manager.getData("key1").get(1), "value3");
	}
	

	@Test
	void clearDataTest() throws IOException, UnCorrectDataException {
		VirtualDataManager manager = initializeDM("vdmTest3.txt", "<key1>:<value1>\r\n<key2>:<value2>\r\n<key1>:<value3>\r\n");
		
		manager.clearData();
		try (FileReader reader = new FileReader("vdmTest3.txt"))
		{
			assertEquals(true, reader.read() < 0);
		}
	}
	
	@Test
	void writeDataTest() throws IOException, UnCorrectDataException {
		VirtualDataManager manager = initializeDM("vdmTest4.txt", "");
		
		manager.writeData("someKey","someValue");
		try (FileReader reader = new FileReader("vdmTest4.txt"))
		{
			assertEquals("<someKey>:<someValue>\r\n", readFile(reader));
		}
	}
	
	@Test
	void removeDataTest() throws IOException, UnCorrectDataException, UnfoundedDataException {
		VirtualDataManager manager = initializeDM("vdmTest5.txt", "<key1>:<value1>\r\n<key2>:<value2>\r\n<key1>:<value3>\r\n<key2>:<value4>\r\n");
		
		manager.removeData("key1");
		try (FileReader reader = new FileReader("vdmTest5.txt"))
		{
			assertEquals("<key2>:<value2>\r\n<key2>:<value4>\r\n", readFile(reader));
		}
		
		manager.removeData("key2", "value2");
		try (FileReader reader = new FileReader("vdmTest5.txt"))
		{
			assertEquals("<key2>:<value4>\r\n", readFile(reader));
		}
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
	
	private VirtualDataManager initializeDM(String fileName, String data) throws IOException, UnCorrectDataException
	{
		try (FileWriter writer = new FileWriter(fileName))
		{
			writer.write(data);
			writer.flush();
		}
		
		return new VirtualDataManager(new FileDataReader(fileName), new FileDataWriter(fileName));
	}
}
