package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import data.managers.FileDataReader;
import data.managers.FileDataWriter;
import data.managers.VirtualDataManager;
import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;

class VirtualDataManagerTests {

	/*@Test
	void getAllDataTest() throws IOException, UncorrectDataException {
		File file = new File("vdmTest.txt");
		VirtualDataManager manager = initializeDM(file, "<key1>:<value1>\r\n<key2>:<value2>\r\n<key1>:<value3>\r\n");
		
		Map<String, List<String>> data = manager.getAllData();
		
		assertEquals(data.keySet().toArray()[0], "key1");
		assertEquals(data.keySet().toArray()[1], "key2");
		assertEquals(data.get("key1").get(0), "value1");
		assertEquals(data.get("key2").get(0), "value2");
		assertEquals(data.get("key1").get(1), "value3");
		
		file.delete();
	}*/
	
	@Test
	void getDataTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("vdmTest.txt");
		file.delete();
		VirtualDataManager manager = initializeDM(file, "$<key1>:<value11>:<value12>\r\n$<key2>:<value21>\r\n");
		
		if (manager.getData("key1") == null)
			System.out.println("key is null!!!");;
		
		assertEquals("key1", manager.getData("key1").key);
		assertEquals("key2", manager.getData("key2").key);
		
		assertEquals(2, manager.getData("key1").values.size());
		assertEquals(1, manager.getData("key2").values.size());
		
		assertEquals("value11", manager.getData("key1").values.get(0));
		assertEquals("value12", manager.getData("key1").values.get(1));
		assertEquals("value21", manager.getData("key2").values.get(0));
		
		try {
			manager.getData("key3");
			assertEquals(true, false);
		}
		catch (UnfoundedDataException ex) {}
		
		file.delete();
	}

	@Test
	void writeDataTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("vdmTest.txt");
		VirtualDataManager manager = initializeDM(file, "$<key1>:<value11>:<value12>\r\n$<key2>:<value21>\r\n");
		
		manager.writeData("key2", 1, "changeValue21");
		manager.writeData("key1", 2, "changeValue12");
		
		assertEquals("changeValue21", manager.getData("key2").values.get(0));
		assertEquals("changeValue12", manager.getData("key1").values.get(1));
		
		try {
			manager.writeData("key3", 1, "someVal");
			assertEquals(true, false);
		}
		catch (UnfoundedDataException ex) {}
		
		try {
			manager.writeData("key2", 2, "someVal");
			assertEquals(true, false);
		}
		catch (UnfoundedDataException ex) {}
		
		file.delete();
	}
	
	@Test
	void removeDataTest() throws IOException, UncorrectDataException, UnfoundedDataException {
		File file = new File("vdmTest.txt");
		VirtualDataManager manager = initializeDM(file, "$<key1>:<value11>:<value12>\r\n$<key2>:<value21>\r\n");
		
		manager.removeData("key1");
		
		try {
			manager.getData("key1");
			assertEquals(true, false);
		}
		catch (UnfoundedDataException ex) {}
		
		file.delete();
	}
	
	@Test
	void clearDataTest() throws IOException, UncorrectDataException {
		File file = new File("vdmTest.txt");
		VirtualDataManager manager = initializeDM(file, "$<key1>:<value11>:<value12>\r\n$<key2>:<value21>\r\n");
		
		manager.clearData();
		try (FileReader reader = new FileReader(file))
		{
			assertEquals(true, reader.read() < 0);
		}
		
		file.delete();
	}
	
	private VirtualDataManager initializeDM(File file, String data) throws IOException, UncorrectDataException
	{
		try (FileWriter writer = new FileWriter(file))
		{
			writer.write(data);
			writer.flush();
		}
		
		return new VirtualDataManager(file.getName());
	}
}
