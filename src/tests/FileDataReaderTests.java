package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.io.*;
import dataManagers.FileDataReader;
import exceptions.UnCorrectDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class FileDataReaderTests {

	@Test
	void readWordTest() throws FileNotFoundException, IOException, UnCorrectDataException {
		try (FileWriter writer = new FileWriter("test2.txt"))
		{
			writer.write("<key1><value1><key2><value2>");
			writer.flush();
		}
		
		try (FileReader reader = new FileReader("test2.txt")){
			assertEquals("key1", FileDataReader.readWord(reader));
			assertEquals("value1", FileDataReader.readWord(reader));
			assertEquals("key2", FileDataReader.readWord(reader));
			assertEquals("value2", FileDataReader.readWord(reader));
		}
	}
	
	@Test
	void getAllDataTest() throws IOException, UnCorrectDataException {
		
		try (FileWriter writer = new FileWriter("test3.txt"))
		{
			writer.write("<key1>:<value1>\r\n<key2>:<value2>\r\n<key1>:<value3>\r\n");
			writer.flush();
		}
		
		FileDataReader dataReader = new FileDataReader("test3.txt");
		Map<String, List<String>> data = dataReader.getAllData();
		
		assertEquals("key1", data.keySet().toArray()[0]);
		assertEquals("key2", data.keySet().toArray()[1]);
		assertEquals("value1", data.get("key1").get(0));
		assertEquals("value2", data.get("key2").get(0));
		assertEquals("value3", data.get("key1").get(1));
	}

}
