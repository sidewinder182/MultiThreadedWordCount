import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MasterWorkerThread extends Thread{
	
	private Socket socket;
	private AtomicInteger isAlive = new AtomicInteger(1);
	private AtomicInteger isDone = new AtomicInteger(0);
	private int PID;
	private ConcurrentHashMap<String, Integer> fileMap = new ConcurrentHashMap<String, Integer>();
	private String filename;
	private int jobFlag;
	private ConcurrentHashMap<Integer, String> outputMap;
	private AtomicInteger statusFlag; //0->no change, 1->worker stopped, 2->mapping complete
	private AtomicInteger activeWorkers;
	
	public MasterWorkerThread(Socket socket, ConcurrentHashMap<String, Integer> fileMap, ConcurrentHashMap<Integer, String> outputMap, int jobFlag, AtomicInteger statusFlag, AtomicInteger activeWorkers)
	{
		this.socket = socket;
		this.fileMap = fileMap;
		this.jobFlag = jobFlag;
		this.outputMap = outputMap;
		this.statusFlag = statusFlag;
		this.activeWorkers = activeWorkers;
	}

	
	public void run()
	{
		
		try {
				
					
				InputStreamReader IR = new InputStreamReader(this.socket.getInputStream());
				BufferedReader BR = new BufferedReader(IR);
					
				String Message = BR.readLine();
				System.out.println(Message);
				PrintStream PS = new PrintStream(this.socket.getOutputStream());
				if(Message != null)
				{
					String arr[] = Message.split(" ");
				
					if(arr[0].equals("PID"))
					{							
						PID = Integer.parseInt(arr[1]);
						System.out.println("Connection made with PID = " + PID);
					}
				}
				if(jobFlag == 0)
				{
					for (String key : this.fileMap.keySet())
					{
						if(this.fileMap.get(key) == -1)
						{
							//System.out.println("map " + key);
							
							this.fileMap.computeIfPresent(key, (k,v) -> v.equals(-1) ? PID : v);
							if(this.fileMap.get(key) == PID)
							{
								this.filename = key;
								break;
							}
								
						}
					}
					if(this.filename != null)
					{
						System.out.println("map " + this.filename);
						PS.println("map " + this.filename);
					}
				}
				
				
				if(this.fileMap.containsValue(PID))
				{
					Message = BR.readLine();
					if(!Message.equals(null) && Message.equals("start") )
					{
						System.out.println("start");
						new HeartBeatThread(socket, isAlive, isDone, outputMap, filename, PID).start();
					}
				}
				
				else
				{
					System.out.println("Closing worker cause no file");
					PS.println("close");
				}
				
			} catch (IOException e) {
			// TODO Auto-generated catch block
				isAlive.set(0);
				e.printStackTrace();
			
		}
		while(statusFlag.get() != 2)
		{
			if(isAlive.get() == 0)
			{
				
				this.fileMap.put(this.filename, -1);
				this.statusFlag.set(1);
				this.activeWorkers.decrementAndGet();
				System.out.println("Worker PID: " + this.PID + " connection stopped");
				break;
			}
			
			if(isDone.get() == 1)
			{
//				System.out.println("In isDone = 1");
				this.fileMap.put(this.filename, 1);
//				System.out.println("Map for " + this.filename +" Done in masterworkerthread");
				if(fileMap.containsValue(-1))
				{
					System.out.println("adding file to worker with PID "+ PID);
					for (String key : this.fileMap.keySet())
					{
						if(this.fileMap.get(key) == -1)
						{
							//System.out.println("map " + key);
							
							this.fileMap.computeIfPresent(key, (k,v) -> v.equals(-1) ? this.PID : v);
							if(this.fileMap.get(key) == PID)
							{
								this.filename = key;
								isDone.set(0);
								break;
							}
								
						}
					}
					if(this.filename != null)
					{
						
						try {
								InputStreamReader IR = new InputStreamReader(this.socket.getInputStream());
								BufferedReader BR = new BufferedReader(IR);
								PrintStream PS = new PrintStream(this.socket.getOutputStream());
								isDone.set(0);
								PS.println("map " + this.filename);
								System.out.println("map " + this.filename);
								String Message = BR.readLine();
								if(Message.equals("start"))
								{
									new HeartBeatThread(socket, isAlive, isDone, outputMap, filename, PID).start();
								}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				else if(!outputMap.containsValue(""))
				{
					boolean allOnes = true;
					for (String key : this.fileMap.keySet())
					{
						if(fileMap.get(key) != 1)
						{
							allOnes = false;
							break;
						}
						 
					}
					if(allOnes == true)
					{
						System.out.println("all maps done");
						
						try {
							PrintStream PS = new PrintStream(this.socket.getOutputStream());
							PS.println("close");
							
							System.out.println("close");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						statusFlag.set(2);
						int s = outputMap.size();
					}
				}
				
				
				
				
			}
			
		}
		
		try {
			System.out.println("Closing socket");
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
