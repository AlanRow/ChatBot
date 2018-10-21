package bot_interfaces;

import java.io.IOException;

import exceptions.UnfoundedDataException;

public interface DataCorrector extends DataWriter
{
	public void removeData(String key);
	
	public void removeData(String key, String data) throws UnfoundedDataException;
}
