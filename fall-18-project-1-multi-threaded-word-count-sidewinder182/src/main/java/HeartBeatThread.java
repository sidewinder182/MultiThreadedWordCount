import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HeartBeatThread extends Thread {
	
	private AtomicInteger isAlive;
	private AtomicInteger isDone;
	private Socket socket;
	private ConcurrentHashMap<Integer, String> outputMap;
	private String filename;
	private int PID;
	
	public HeartBeatThread(Socket socket, AtomicInteger isAlive, AtomicInteger isDone, ConcurrentHashMap<Integer, String> outputMap, String filename, int PID) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.isAlive = isAlive;
		this.isDone = isDone;
		this.outputMap = outputMap;
		this.filename = filename;
		this.PID = PID;
	}

	
	
	public void run()
	{
		
		try 
		{
			PrintStream PS = new PrintStream(socket.getOutputStream());
		
			while(true)
			{
			
				
				PS.println("heartbeat message");
				InputStreamReader IR = new InputStreamReader(this.socket.getInputStream());
				BufferedReader BR = new BufferedReader(IR);				
				String heartBeat = BR.readLine();
				if(heartBeat != null)
				{	
					String arr[] = heartBeat.split(" ");
					if(arr[0].equals("heartbeat"))
					{
						System.out.println(heartBeat);
						TimeUnit.SECONDS.sleep(3);
						
						
					}
					if(arr[0].equals("status"))
					{
						
						System.out.println("map " + arr[1] + " done in heartbeat");
						outputMap.put(this.PID, arr[1]);
						isDone.set(1);
						
						break;
					}
				}
			}	
		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				isAlive.set(0);
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();;
				isAlive.set(0);
				
			}
		
		
	}
}
			
		
	
	

