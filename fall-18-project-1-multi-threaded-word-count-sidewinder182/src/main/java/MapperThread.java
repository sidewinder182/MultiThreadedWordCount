import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MapperThread extends Thread {
	private String filename;	
	private AtomicInteger isMapDone;
	private FileWriter fileWriter;
	
	public MapperThread(String filename, int PID, AtomicInteger isMapDone, FileWriter filewriter)
	{
		this.filename = filename;
		this.isMapDone = isMapDone;
		this.fileWriter = filewriter;
		
	} 
	
	public void run()
	{
		try 
		{	
			System.out.println("mapper started");
			BufferedWriter bw = new BufferedWriter(this.fileWriter);
		    PrintWriter out = new PrintWriter(bw);
		
			BufferedReader br = new BufferedReader(new FileReader(filename));
			System.out.println(filename);
			String line;
			while ((line = br.readLine()) != null) 
			{
				
				
				
				String[] words = line.split(" ");
				for (String word: words)
				{
					if (word.length()>0)
					{
    					out.println(word + " 1");
//    					out.println(System.lineSeparator());
					}
				}
			    	
			}
			out.close();
			bw.close();
			this.fileWriter.close();
			br.close();
			System.out.println("closed all in mapper thread");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		try {
//			TimeUnit.SECONDS.sleep(5);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		isMapDone.set(1);
	}
	
	
}
