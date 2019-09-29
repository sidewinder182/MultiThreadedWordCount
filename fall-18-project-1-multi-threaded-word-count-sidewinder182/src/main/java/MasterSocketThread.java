import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MasterSocketThread extends Thread{
	
	String[] inputFiles;
	private int intWorkers;
	private ConcurrentHashMap<String, Integer> fileMap;
	private ConcurrentHashMap<Integer, String> outputMap;
	private AtomicInteger statusFlag; //0->no change, 1->worker stopped, 2->mapping complete
	private int jobFlag = 0; //0 -> map, 1-> reduce
	private AtomicInteger activeWorkers = new AtomicInteger(0);
	
	
	public MasterSocketThread(WordCount wordCount)
	{
		this.inputFiles = wordCount.getInputFiles();
		this.intWorkers = wordCount.getWorkers();
		this.fileMap = wordCount.getFileMap();
		this.outputMap = wordCount.getOutputMap();
		this.statusFlag = wordCount.getStatusFlag();
    	
	}

	public void run()
	{
		try {
				int i = 0;
				ServerSocket master = new ServerSocket(5000);
				System.out.println("In the mastersocket thread");
				while(i < intWorkers)
				{
					Socket socket = master.accept();
					new MasterWorkerThread(socket, fileMap, outputMap, jobFlag, statusFlag, activeWorkers).start();
					i++;
				}
				
				this.activeWorkers.set(this.intWorkers);
				
				while(statusFlag.get() !=2)
				{	
					if(this.activeWorkers.get()<intWorkers)
					{
						Socket socket = master.accept();
						if(fileMap.containsValue(-1))
						{
							
							new MasterWorkerThread(socket, fileMap, outputMap, jobFlag = 0, statusFlag, activeWorkers).start();
							this.activeWorkers.incrementAndGet();
						}
						
					}
				}
				master.close();
				System.out.println("master socket closed");
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			
		}
	}
}
