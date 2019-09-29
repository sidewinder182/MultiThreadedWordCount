import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class HeartBeatWorkerThread extends Thread{

	private Socket workerSocket;
	private AtomicInteger isMapDone;
	private int PID;
	private FileWriter fileWriter;
	private String targetFile;

	public HeartBeatWorkerThread(Socket workerSocket, AtomicInteger isMapDone, int PID, String targetFile) {
		// TODO Auto-generated constructor stub
		this.workerSocket = workerSocket;
		this.isMapDone = isMapDone;
		this.PID = PID;
//		this.fileWriter = fileWriter;
		this.targetFile = targetFile;
	}
	
	public void run()
	{
		try {
//			System.out.println(this.workerSocket.isClosed());
			InputStreamReader IR = new InputStreamReader(this.workerSocket.getInputStream());
			BufferedReader BR = new BufferedReader(IR);
			PrintStream PS = new PrintStream(this.workerSocket.getOutputStream());
			while(true)
			{
				
				String heartBeat = BR.readLine();
			 	String arr[] = heartBeat.split(" ");
			 	
		 		if(arr[0].equals("map"))
				{
					System.out.println("start");
					this.fileWriter = createFile(this.PID);
					new MapperThread(arr[1], this.PID, this.isMapDone, this.fileWriter).start();
					PS.println("start");
					
				}
		 		if(heartBeat.equals("heartbeat message") && isMapDone.get() == 0)
				{
					PS.println("heartbeat message from " + this.PID);
				}
				if(heartBeat.equals("heartbeat message") && isMapDone.get() == 1)
				{
					PS.println("status "+ this.targetFile);
					isMapDone.set(0);
				}
				if(heartBeat.equals("close"))
				{
					System.out.println("in close condition");
					isMapDone.set(2);
					break;
				}
			 	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public FileWriter createFile(int PID) throws IOException
	{
		String cwd = System.getProperty("user.dir");
		String workerDir = cwd+"\\map_output\\"+PID+".txt";
//		String workerDir = "C:\\GitHub\\fall-18-project-1-multi-threaded-word-count-sidewinder182\\map_output\\"+PID+".txt";
		File dir = new File(workerDir);
		dir.getParentFile().mkdirs();
//		if(dir.exists())
//		{
//			dir.delete();
//		}
		dir.createNewFile();
		this.targetFile = workerDir;
		FileWriter fw = new FileWriter(dir, true);
		return fw;
		
	}
	
}
