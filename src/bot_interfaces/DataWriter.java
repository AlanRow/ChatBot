package bot_interfaces;

import java.io.IOException;

public interface DataWriter {
	
	public void writeData(String key, String data) throws IOException;

	public void clearData() throws IOException;
}
