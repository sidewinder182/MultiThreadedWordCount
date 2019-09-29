import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Worker {

	private int PID;
	private AtomicInteger isMapDone = new AtomicInteger(0); //0->busy, 1->idle after map, 2->all mapping done 
	private String targetFile;
	
	public static void main(String[] args) throws Exception
	{
		System.out.println("In the worker main");
		Worker worker = new Worker();
    	worker.run();
	}
	
	
	public void run() throws UnknownHostException, IOException 
	{
		// TODO Auto-generated method stub
		 System.out.println("In the worker run");
		 Socket workerSocket = new Socket("localhost", 5000);
		 //172.30.184.119
		 PrintStream PS = new PrintStream(workerSocket.getOutputStream());
		 this.PID = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
		 PS.println("PID " + this.PID);
//		 FileWriter fileWriter = createFile(PID);
//		 System.out.println(this.targetFile);
		 new HeartBeatWorkerThread(workerSocket, isMapDone, this.PID, this.targetFile).start();
			
		 
		 while(true)
		 {
			 
			 
			 if(isMapDone.get() == 2)
			 {
				 System.out.println("all mapping done");
//				 fileWriter.close();
				 break;
			 }
			 
		 }
		 workerSocket.close();
	}
		
//	public FileWriter createFile(int PID) throws IOException
//	{
//		String cwd = System.getProperty("user.dir");
//		String workerDir = "C:\\GitHub\\fall-18-project-1-multi-threaded-word-count-sidewinder182\\map_output\\"+PID+".txt";
//		File dir = new File(workerDir);
//		dir.getParentFile().mkdirs();
////		if(dir.exists())
////		{
////			dir.delete();
////		}
//		dir.createNewFile();
//		this.targetFile = workerDir;
//		FileWriter fw = new FileWriter(dir, true);
//		return fw;
//		
//	}
	
		 
}
