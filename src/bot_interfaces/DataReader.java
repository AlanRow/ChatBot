package bot_interfaces;

public interface DataReader 
{
	public boolean areNewData();
	
	public String[] getNewData();
	
	public String[] getAllData();
}
