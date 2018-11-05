package tests;

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
	void getAllDataTest() throws IOException, UnCorrectDataException {
		
		File reading = new File("readTest.txt");
		try (FileWriter writer = new FileWriter(reading))
		{
			writer.write("<key1>:<value1>\r\n<key2>:<value2>\r\n<key1>:<value3>\r\n");
			writer.flush();
		}
		
		FileDataReader dataReader = new FileDataReader(reading.getName());
		Map<String, List<String>> data = dataReader.getAllData();
		
		assertEquals("key1", data.keySet().toArray()[0]);
		assertEquals("key2", data.keySet().toArray()[1]);
		assertEquals("value1", data.get("key1").get(0));
		assertEquals("value2", data.get("key2").get(0));
		assertEquals("value3", data.get("key1").get(1));
		
		reading.delete();
	}

}
