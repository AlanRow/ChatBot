package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import dataManagers.FileDataWriter;
import java.io.*;
import java.util.Arrays;

class FileDataWriterTests {

	@Test
	void constructTest() throws IOException {
		File file = new File("file1.txt");
		file.delete();
		
		FileDataWriter dataWriter = new FileDataWriter("file1.txt");
		assertEquals(true, file.exists());
	}
	
	@Test
	void writeTest() throws IOException {

		File file = new File("file1.txt");
		file.delete();
		FileDataWriter dataWriter = new FileDataWriter("file1.txt");
		
		dataWriter.writeData("key1", "value1");
		dataWriter.writeData("key2", "value2");
		
		
	}

	@Test
	void clearTest() throws IOException {
		File file = new File("file1.txt");
		file.delete();
		FileDataWriter dataWriter = new FileDataWriter("file1.txt");
		dataWriter.writeData("key1", "value1");
		dataWriter.writeData("key2", "value2");
		
		dataWriter.clearData();
		try (FileReader reader = new FileReader("file1.txt"))
		{
			assertEquals(true, reader.read() < 0);
		}
	}
}
