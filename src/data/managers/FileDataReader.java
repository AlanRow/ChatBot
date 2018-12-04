package data.managers;

import exceptions.UncorrectDataException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bot.interfaces.DataReader;

//класс для чтения информации типа ключ-значение из файла
public class FileDataReader implements DataReader {

	//файл из которого будем читать
	private File file;
	
	public FileDataReader (String fileName) throws IOException {
		file = new File(fileName);
		
		if (!file.exists())
			throw new IOException("There isn't file " + fileName + " for reading.");
	}
	
	//метод вычитки всей информации из файла
	@Override
	public Map<String, List<String>> getAllData() throws IOException, UncorrectDataException {
		
		Map<String, List<String>> keyMap = new HashMap<String, List<String>>();
		
		//*НУЖНЫ ПРАВКИ*
		//необходимо переписать метод, чтобы он считывал произвольное число значений после ключа
		//и выбрасывал exception, когда значений более, чем 2 и когда ключ встречается в файле дважды
		//используй UncorrectDataException
		try(FileReader reader = new FileReader(file))
		{
			//читает пока не дойдет до конца файла
			while (reader.read() > 0) //reads supporting symbol, which shows that there is next line
			{
				String key = readWord(reader);//reads the key
				List<String> value = readValues(reader);//reads the some number of values
				keyMap.put(key, value);//adds line to map
			}
			
			return keyMap;//returns map
		}
	}

	private List<String> readValues(FileReader reader) throws IOException, UncorrectDataException {
		int code;
		List<String> values = new ArrayList<String>();
		
		//reads until end of line
		while ((code = reader.read()) > 0 && ((char)code) != '\r') {
			
			//checks the existing of delimiter-character
			if ((char)code != ':')
				throw new UncorrectDataException("The data in <" + file.getName() + "> is uncorrect: unfound \':\' - delimiter.");
			
			//reads next value
			String word = readWord(reader);
			if (word == null)
				throw new UncorrectDataException("The data in <" + file.getName() + "> is uncorrect: there isn't \\r\\n in the end of file.");
			
			//adds value to list
			values.add(word);
		}
		
		//checks the correct final of the line
		if ((code = reader.read()) <= 0 || ((char)code) != '\n')
			throw new UncorrectDataException("The data in <" + file.getName() + "> is uncorrect: there isn't \\n in the end of line.");
		
		return values;
	}
	
	private String readWord(FileReader reader) throws IOException, UncorrectDataException
	{
		int code;
		String word = "";
		
		if ((code = reader.read()) <= 0)
			return null;
		
		if ((char)code != '<')
			throw new UncorrectDataException("The word in <" + file.getName() + "> hasn't '<' at the start, but nas a '" + (char)code + "'.");
		
		
		while ((code = reader.read()) > 0 && (char)code != '>') {
			word += (char) code;
		}
		
		return word;
	}
}
