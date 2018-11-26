package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import dataManagers.FileDataWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FileDataWriterTests {

	@Test
	void constructTest() throws IOException {
		File file = new File("writingTest.txt");
		file.delete();
		
		FileDataWriter dataWriter = new FileDataWriter(file.getName());
		assertEquals(true, file.exists());
		
		file.delete();
	}
	
	@Test
	void writeAllDataTest() throws IOException {

		File file = new File("writingTest.txt");
		file.delete();
		FileDataWriter dataWriter = new FileDataWriter(file.getName());
		
		Map<String, List<String>> data = new HashMap<String, List<String>>();
		
		List<String> line1 = new ArrayList<String>();
		List<String> line2 = new ArrayList<String>();
		
		line1.add("value11");
		line1.add("value12");
		line2.add("value21");
		
		data.put("key1", line1);
		data.put("key2", line2);
		
		dataWriter.writeAllData(data);
		
		String fileText = "";
		int code;
		
		try (FileReader reader = new FileReader(file)){
			while ((code = reader.read()) > 0) {
				fileText += (char) code;
			}
		}
		
		assertEquals("$<key1>:<value11>:<value12>\r\n$<key2>:<value21>\r\n", fileText);
		
		file.delete();
	}

	@Test
	void clearTest() throws IOException {
		File file = new File("writingTest.txt");
		file.delete();
		FileDataWriter dataWriter = new FileDataWriter(file.getName());
		
		Map<String, List<String>> data = new HashMap<String, List<String>>();
		
		List<String> line1 = new ArrayList<String>();
		List<String> line2 = new ArrayList<String>();
		
		line1.add("value11");
		line1.add("value12");
		line2.add("value21");
		
		data.put("key1", line1);
		data.put("key2", line2);
		
		dataWriter.writeAllData(data);
		
		dataWriter.clearData();
		try (FileReader reader = new FileReader(file.getName()))
		{
			assertEquals(true, reader.read() < 0);
		}
		
		file.delete();
	}
}
