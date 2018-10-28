package algs;

import java.io.IOException;
import java.util.List;

import bot_interfaces.Algorithm;
import bot_interfaces.DataCorrector;
import exceptions.UnfoundedDataException;

public class ListWriteAlg implements Algorithm {
	private DataCorrector listWriter;
	private String user;
	private boolean willCome;
	private boolean isReady;
	private boolean isTautology;
	private boolean haveUnsolvedExceptions;
	private String answer;
	
	private List userList;

	public ListWriteAlg(DataCorrector usersDataWriter, String user)
	{
		listWriter = usersDataWriter;
		this.user = user;
	}
	
	public void readMessage(String message) {
		if (haveUnsolvedExceptions)
			return;
		
		isReady = true;
		try {
			switch (message.toLowerCase())
			{
				case "� �����":
					if (!willCome)
					{
						listWriter.writeData(user, "will");
						willCome = true;
						answer = "������, � ��� �������.";
					}
					else
						answer = "��, � ��� � �����.";
					break;
				case "� �� �����":
					if (willCome)
					{
						willCome = false;
						try {
							listWriter.removeData(user, "will");
							answer = "������, � ��� ���������.";
						}
						catch (UnfoundedDataException ex) {
							answer = "�������, ��� �� ���� � ������...";
						}
					}
					else
						answer = "��, � ��� � �����.";
					break;
				default:
					isReady = false;
					break;
			}
		} 
		catch (IOException ex) {
			haveUnsolvedExceptions = true;
			answer = "�������, �������� ������ � ������ � ������.";
		}
		
	}

	public boolean isReadyToGenerate() {
		return isReady;
	}

	public String generateMessage() {
		isReady = false;
		return answer;
	}
	
	
}
