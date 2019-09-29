import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkerSocketThread extends Thread{
	
	private int workers;
	private WordCount wordCount;
	private AtomicInteger statusFlag;
	private ConcurrentHashMap fileMap;
	private ConcurrentHashMap outputMap;
	
	public WorkerSocketThread(WordCount wordCount) {
		// TODO Auto-generated constructor stub
		this.workers = wordCount.getWorkers();
		this.wordCount = wordCount;
		this.statusFlag = wordCount.getStatusFlag();
		this.fileMap = wordCount.getFileMap();
		this.outputMap = wordCount.getOutputMap();
		
	}
	
	public void run()
	{
		System.out.println("In the workersocket thread");
		try {
//				ProcessBuilder compileBuilder = new ProcessBuilder("java", "Worker.java");
//			ProcessBuilder compileBuilder = new ProcessBuilder("java", "-cp", "build/classes/main", "Worker");
//				String cwd = System.getProperty("user.dir");
//				String workerDir = "";
//				String workerDir = "C:\\GitHub\\fall-18-project-1-multi-threaded-word-count-sidewinder182\\src\\main\\java";
//				compileBuilder.directory(new File(workerDir));
//				Process compile = compileBuilder.start();
				for (int i = 0; i < this.workers; i++)
				{
					
					this.wordCount.createWorker();
		
				}
				
					
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(statusFlag.get() != 2)
		{
			if(statusFlag.get() == 1)
			{
				statusFlag.set(0);
				try {
					this.wordCount.createWorker();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("creating new worker");
				}
			}
		}
	System.out.println("closing worker socket thread");	
	}
}
