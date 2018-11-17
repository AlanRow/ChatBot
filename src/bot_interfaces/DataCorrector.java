package bot_interfaces;

import java.io.IOException;

import exceptions.UnfoundedDataException;

//улучшение интерфейса DataWriter
public interface DataCorrector extends DataWriter
{
	//удаление данных по ключу
	public void removeData(String key) throws IOException;
	
	//удаление данных по ключу и значению
	public void removeData(String key, String data) throws UnfoundedDataException, IOException;
}
