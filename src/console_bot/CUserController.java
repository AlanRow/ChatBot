package console_bot;

import java.util.Scanner;
import bot_interfaces.UserControl;

public class CUserController implements UserControl
{
	private boolean isUserAuth;
	private String userName;
	
	public CUserController()
	{
		isUserAuth = false;
		userName = "";
	}
	
	public boolean areNewUsers() 
	{
		if (!isUserAuth)
		{
			isUserAuth = true;
			return true;
		}
		
		return false;
	}

	public String[] getNewUsers() {
		
		Scanner input = new Scanner(System.in);

		System.out.println("Здравствуйте, представтьесь, пожалуйста.");
		return new String[] { input.nextLine()};
	}
	
}
