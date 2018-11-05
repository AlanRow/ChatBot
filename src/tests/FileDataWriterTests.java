package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import dataManagers.FileDataWriter;
import java.io.*;
import java.util.Arrays;

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
	void writeTest() throws IOException {

		File file = new File("writingTest.txt");
		file.delete();
		FileDataWriter dataWriter = new FileDataWriter(file.getName());
		
		dataWriter.writeData("key1", "value1");
		
		String fileText = "";
		int code;
		
		try (FileReader reader = new FileReader(file)){
			while ((code = reader.read()) > 0) {
				fileText += (char) code;
			}
		}
		
		assertEquals("<key1>:<value1>\r\n", fileText);
		fileText = "";
		
		dataWriter.writeData("key2", "value2");
		
		try (FileReader reader = new FileReader(file)){
			while ((code = reader.read()) > 0) {
				fileText += (char) code;
			}
		}
		
		assertEquals("<key1>:<value1>\r\n<key2>:<value2>\r\n", fileText);
		
		file.delete();
	}

	@Test
	void clearTest() throws IOException {
		File file = new File("writingTest.txt");
		file.delete();
		FileDataWriter dataWriter = new FileDataWriter(file.getName());
		dataWriter.writeData("key1", "value1");
		dataWriter.writeData("key2", "value2");
		
		dataWriter.clearData();
		try (FileReader reader = new FileReader(file.getName()))
		{
			assertEquals(true, reader.read() < 0);
		}
		
		file.delete();
	}
}
