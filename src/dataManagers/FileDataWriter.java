package dataManagers;

import bot_interfaces.DataWriter;
import java.io.*;
import java.util.List;
import java.util.Map;

public class FileDataWriter implements DataWriter {

	//файл для записи
	private File file;
	
	public FileDataWriter (String fileName) throws IOException {
		file = new File(fileName);
		
		if (!file.exists())
			file.createNewFile();
	}
	
	//writes all map to file in the next form: $<key>:<value1>:<value2>:...:<valueN>
	public void writeAllData(Map<String, List<String>> data) throws IOException {
		try (FileWriter writer = new FileWriter(file, false))
		{
			for (Map.Entry<String, List<String>> pair : data.entrySet()) {
				//'$' - the supporting character, which says that there is start of new line
				writer.append("$<" + pair.getKey() + ">");
				
				for (String val : pair.getValue())
					writer.append(":<" + val + ">");
				
				//end of line
				writer.append("\r\n");
			}
			writer.flush();
		}
	}

	public void clearData() throws IOException {
		try (FileWriter writer = new FileWriter(file))
		{
			writer.write("");
			writer.flush();
		}
	}


	
}
