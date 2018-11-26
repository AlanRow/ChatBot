package tests;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.io.*;
import dataManagers.FileDataReader;
import exceptions.UncorrectDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class FileDataReaderTests {
	
	@Test
	void getAllDataTest() throws IOException, UncorrectDataException {
		
		File reading = new File("readTest.txt");
		try (FileWriter writer = new FileWriter(reading))
		{
			writer.write("$<key1>:<value11>:<value12>\r\n$<key2>:<value21>\r\n");
			writer.flush();
		}
		
		FileDataReader dataReader = new FileDataReader(reading.getName());
		Map<String, List<String>> data = dataReader.getAllData();
		
		assertEquals(true, data.containsKey("key1"));
		assertEquals(true, data.containsKey("key2"));
		
		assertEquals(2, data.get("key1").size());
		assertEquals(1, data.get("key2").size());
		
		assertEquals("value11", data.get("key1").get(0));
		assertEquals("value12", data.get("key1").get(1));
		assertEquals("value21", data.get("key2").get(0));
		
		reading.delete();
	}
	
	@Test
	void UncorrectDataTest() throws IOException {

		File error1 = new File("readTest1.txt");
		try (FileWriter writer = new FileWriter(error1))
		{
			writer.write("sdfgdsfgdsfgas");
			writer.flush();
		}
		
		File error2 = new File("readTest2.txt");
		try (FileWriter writer = new FileWriter(error2))
		{
			writer.write("<key1>:<value11>\r\n");
			writer.flush();
		}
		
		File error3 = new File("readTest3.txt");
		try (FileWriter writer = new FileWriter(error3))
		{
			writer.write("$<key1>:<value11>:<value12>$<key2>:<value21>\\r\\n");
			writer.flush();
		}
		
		try {
			new FileDataReader(error1.getName()).getAllData();
			assertEquals(true, false);
		}
		catch (UncorrectDataException ex) {
		}
		
		try {
			new FileDataReader(error2.getName()).getAllData();
			assertEquals(true, false);
		}
		catch (UncorrectDataException ex) {
		}
		
		try {
			new FileDataReader(error3.getName()).getAllData();
			assertEquals(true, false);
		}
		catch (UncorrectDataException ex) {
		}
		
		error1.delete();
		error2.delete();
		error3.delete();
	}

}
