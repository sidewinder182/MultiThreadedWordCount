import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.net.*;


public class WordCount implements Master {
	private int workers;
	private String[] inputFiles;
	private ConcurrentHashMap<String, Integer> fileMap;
	private ConcurrentHashMap<Integer, String> outputMap;
	private AtomicInteger statusFlag = new AtomicInteger(0); //0->no change, 1->worker stopped, 2->mapping complete
	private Map<String, Integer> reducerMap = new TreeMap<String, Integer>();
	private String outputFile;
	private Collection<Process> activeProcesses = new LinkedList<Process>();
	PrintStream out;
	
	public AtomicInteger getStatusFlag() {
		return statusFlag;
	}
	public String[] getInputFiles() {
		return inputFiles;
	}
	public int getWorkers() {
		return workers;
	}


	public ConcurrentHashMap<String, Integer> getFileMap() {
		return fileMap;
	}
	
	public ConcurrentHashMap<Integer, String> getOutputMap() {
		return outputMap;
	}
	
	public WordCount(int workerNum, String[] filenames) throws IOException {
    	this.workers = workerNum;
    	this.inputFiles = filenames;
    	this.fileMap = new ConcurrentHashMap<String, Integer>(inputFiles.length);
		this.outputMap = new ConcurrentHashMap<Integer, String>(workers);
    	for(int i = 0; i<this.inputFiles.length; i++)
    	{
    		this.fileMap.put(this.inputFiles[i], -1);
    	}
    	this.statusFlag.set(0);
    	this.outputFile = "C:\\GitHub\\fall-18-project-1-multi-threaded-word-count-sidewinder182\\reduce_output\\WordCount"+new Date().getTime()+".txt";
    	
    }

    public void setOutputStream(PrintStream out) {
    	this.out = out;
    }

    
    
    public static void main(String[] args) throws Exception {
//    	String[] filenames = {"C:\\GitHub\\fall-18-project-1-multi-threaded-word-count-sidewinder182\\build\\resources\\test\\simple.txt"};
//    	String[] filenames = {};
    	String[] filenames= {"C:\\GitHub\\fall-18-project-1-multi-threaded-word-count-sidewinder182\\example-output\\war-and-peace.txt","C:\\GitHub\\fall-18-project-1-multi-threaded-word-count-sidewinder182\\example-output\\king-james-version-bible.txt","C:\\GitHub\\fall-18-project-1-multi-threaded-word-count-sidewinder182\\src\\test\\resources\\random.txt","C:\\GitHub\\fall-18-project-1-multi-threaded-word-count-sidewinder182\\src\\test\\resources\\random.txt"};
//    	
    	//"C:\\GitHub\\fall-18-project-1-multi-threaded-word-count-sidewinder182\\example-output\\war-and-peace.txt","C:\\GitHub\\fall-18-project-1-multi-threaded-word-count-sidewinder182\\example-output\\king-james-version-bible.txt","C:\\GitHub\\fall-18-project-1-multi-threaded-word-count-sidewinder182\\src\\test\\resources\\random.txt"
//    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	//C:\\GitHub\\fall-18-project-1-multi-threaded-word-count-sidewinder182\\src\\test\\resources\\random.txt
    	WordCount wordCount = new WordCount(2, filenames) ;
    	
//        wordCount.setOutputStream(out)
        wordCount.run();
//        Collection<Process> proc = wordCount.getActiveProcess();
//        if (!proc.isEmpty())
//        {
//        	Random rnd = new Random();
//        	int i = rnd.nextInt(proc.size());
//        	Process p = (Process)proc.toArray()[i];
//        	p.destroy();
//        }
        System.out.println("first run done");
        WordCount wordCount2 = new WordCount(4, filenames) ;
        wordCount2.run();
//    	System.out.println("reducing done");
    	

    }
    
    

    public void reduce() throws IOException {
		// TODO Auto-generated method stub
    	System.out.println("Reduce called");
//    	File dir = new File(this.outputFile);
//    	File dir = new File("C:\\GitHub\\fall-18-project-1-multi-threaded-word-count-sidewinder182\\reduce_output\\WordCount"+new Date().getTime()+".txt");
//		dir.getParentFile().mkdirs();
//		if(dir.exists())
//		{
//			dir.delete();
//		}
//		dir.createNewFile();    		
//		FileWriter fw = new FileWriter(dir, true);
//		BufferedWriter bw = new BufferedWriter(fw);
//	    PrintWriter fileOut = new PrintWriter(bw);
        for(String value : this.outputMap.values())
        {    		
        	BufferedReader br = new BufferedReader(new FileReader(value));
        	String line;
//			System.out.println(filename);
			while((line = br.readLine()) != null)
			{
				String[] words = line.split(" ");
				
				
				if (words[0]!=null)
				{
					if(reducerMap.containsKey(words[0]))
					{
						this.reducerMap.compute(words[0], (k,v)->v+1);
					}
					else
					{
						this.reducerMap.put(words[0], 1);
					}
					
				}
				
					        
			}
			
			br.close();	
        }
        List<Map.Entry<String, Integer> > list = 
	               new LinkedList<Map.Entry<String, Integer> >(reducerMap.entrySet()); 
		Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
         public int compare(Map.Entry<String, Integer> o1,  
                            Map.Entry<String, Integer> o2) 
         { 
             return (o2.getValue()).compareTo(o1.getValue()); 
         } 
     });
		HashMap<String, Integer> sortedWordCount = new LinkedHashMap<String, Integer>(); 
     for (Map.Entry<String, Integer> aa : list) { 
     	sortedWordCount.put(aa.getKey(), aa.getValue());
     }
     try {
		for(String key : sortedWordCount.keySet())
		 {
//		 	fileOut.println(sortedWordCount.get(key) + " : " + key);
		 	out.println(sortedWordCount.get(key) + " : " + key);
		 }
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
//		fileOut.close();
		//out.flush();
	}
    
    
	public void run()
    {   
		
		MasterSocketThread masterSocketThread = new MasterSocketThread(this);
		WorkerSocketThread workerSocketThread = new WorkerSocketThread(this);
		masterSocketThread.start();
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		workerSocketThread.start();
		if(this.inputFiles.length == 0)
    	{
//    		System.out.println("No input files");
//    		return;
			statusFlag.set(2);
    	
    	}
		while(true)
		{
			if(statusFlag.get() == 2)
			{
				break;
			}
		}
		try {
			
		
			workerSocketThread.join();
			masterSocketThread.join(); 
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if(inputFiles.length != 0)
			{
				reduce();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	} 
    	

 

    public Collection<Process> getActiveProcess() {
        return this.activeProcesses;
    }

    public void createWorker() throws IOException {
//    	String cwd = System.getProperty("user.dir");
//    	String workerDir = "C:\\GitHub\\fall-18-project-1-multi-threaded-word-count-sidewinder182\\src\\main\\java";
//    	ProcessBuilder workerProcessBuilderExec = new ProcessBuilder("java", "Worker");
//    	workerProcessBuilderExec.directory(new File(workerDir));
//    	Process workerProcessexec = workerProcessBuilderExec.start();
    	ClassLoader loader = Test.class.getClassLoader();
        System.out.println(loader.getResource("Worker.class"));
    	ProcessBuilder compileBuilder = new ProcessBuilder("java", (String)loader.getResource("Worker.class"));
    	Process compile = compileBuilder.start();
    	this.activeProcesses.add(compile);
    }
}

