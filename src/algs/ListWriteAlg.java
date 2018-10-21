package algs;

import java.io.IOException;
import java.util.List;

import bot_interfaces.Algorithm;
import bot_interfaces.DataCorrector;

public class ListWriteAlg implements Algorithm {
	private DataCorrector listWriter;
	private String user;
	private boolean willCome;
	private boolean isReady;
	private boolean isTautology;
	
	private List userList;

	public ListWriteAlg(DataCorrector usersDataWriter, String user)
	{
		listWriter = usersDataWriter;
		this.user = user;
	}
	
	public void readMessage(String message) {
		isReady = true;
		switch (message.toLowerCase())
		{
			case "я приду":
				if (!willCome)
				{
					listWriter.writeData(user, "will");
					willCome = true;
				}
				else
					isTautology = true;
				break;
			case "я не приду":
				if (willCome)
				{
					listWriter.removeData(user, "will");
					willCome = false;
				}
				else
					isTautology = true;
				break;
			default:
				isReady = false;
				break;
		}
	}

	public boolean isReadyToGenerate() {
		return isReady;
	}

	public String generateMessage() {
		return (isTautology) ? "Да, я так и понял." : (willCome) ? "Хорошо, я вас записал." : "Хорошо, я вас вычеркнул.";
	}
	
	
}
