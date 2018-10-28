package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import algs.ListWriteAlg;
import dataManagers.FileDataReader;
import dataManagers.FileDataWriter;
import dataManagers.VirtualDataManager;
import exceptions.UnCorrectDataException;

class ListWriteAlgTests {

	@Test
	void willComeTest() throws IOException, UnCorrectDataException {
		ListWriteAlg alg = initializeLWA("lwaTest1.txt", "user", "");
		
		alg.readMessage("Я пРиДу");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("Хорошо, я вас записал.", alg.generateMessage());
	}
	
	void wontComeTest() throws IOException, UnCorrectDataException {
		ListWriteAlg alg = initializeLWA("lwaTest2.txt", "user", "<user><will>\r\n");
		
		alg.readMessage("я не ПРИДУ");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("Хорошо, я вас вычеркнул.", alg.generateMessage());
	}
	
	void tautologyTest() throws IOException, UnCorrectDataException {
		ListWriteAlg alg = initializeLWA("lwaTest2.txt", "user", "<user><will>\r\n");
		
		alg.readMessage("Я ПРИДУ");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("Да, я так и понял.", alg.generateMessage());
		
		alg.readMessage("я не приду");
		alg.generateMessage();
		alg.readMessage("Я не ПридУ");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("Да, я так и понял.", alg.generateMessage());
	}
	
	private ListWriteAlg initializeLWA(String fileName, String user, String data) throws IOException, UnCorrectDataException
	{
		try (FileWriter writer = new FileWriter(fileName))
		{
			writer.write(data);
			writer.flush();
		}
		
		VirtualDataManager manager = new VirtualDataManager(new FileDataReader(fileName), new FileDataWriter(fileName));
		
		return new ListWriteAlg(manager, user);
	}
}
