package algs;

import bot.interfaces.Algorithm;
import structures.UserInfo;

public class HelloAlg implements Algorithm
{
	private boolean isGreeted;
	private boolean readyToGreet;

	public HelloAlg()
	{
		isGreeted = false;
		readyToGreet = false;
	}
	
	public void readMessage(String message) 
	{
		if (message.equalsIgnoreCase("hello") || message.equalsIgnoreCase("hi"))
			readyToGreet = true;
	}

	public boolean isReadyToGenerate() {
			return readyToGreet;
	}

	public String generateMessage() {
		if (!readyToGreet)
			return "";
		
		readyToGreet = false;
		String answer = (isGreeted) ? "We have greeted, yet." : "Hello!";
		isGreeted = true;
		return answer;
	}

	@Override
	public Algorithm genererateSame(UserInfo another) {
		return new HelloAlg();
	}
}
