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
		
		alg.readMessage("� �����");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("������, � ��� �������.", alg.generateMessage());
	}
	
	void wontComeTest() throws IOException, UnCorrectDataException {
		ListWriteAlg alg = initializeLWA("lwaTest2.txt", "user", "<user><will>\r\n");
		
		alg.readMessage("� �� �����");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("������, � ��� ���������.", alg.generateMessage());
	}
	
	void tautologyTest() throws IOException, UnCorrectDataException {
		ListWriteAlg alg = initializeLWA("lwaTest2.txt", "user", "<user><will>\r\n");
		
		alg.readMessage("� �����");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("��, � ��� � �����.", alg.generateMessage());
		
		alg.readMessage("� �� �����");
		alg.generateMessage();
		alg.readMessage("� �� �����");
		assertEquals(true, alg.isReadyToGenerate());
		assertEquals("��, � ��� � �����.", alg.generateMessage());
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
