import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
//	private AtomicInteger i = new AtomicInteger(10);
//	
//	public AtomicInteger get()
//	{
//		return this.i;
//	}
//	
//	public void set(int k)
//	{
//		this.i.set(k);
//	}
//	
//	public static void main(String[] args)
//	{
//		Test test = new Test();
//		
//		new TestThread(test.get()).start();
//		try {
//			TimeUnit.SECONDS.sleep(3);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(test.get().get());
//		
//	}
	
	public static void main(String[] args) throws IOException {
//		String cwd = System.getProperty("user.dir");
//		String workerDir = cwd + "\\map_output\\hello.txt";
//		File dir = new File(workerDir);
//		dir.getParentFile().mkdirs();
//		if(dir.exists())
//		{
//			boolean delete = dir.delete();
//			System.out.println(delete);
//		}
//		dir.createNewFile();
//		FileWriter fw = new FileWriter(dir, true);
//		BufferedWriter bw = new BufferedWriter(fw);
//	    PrintWriter out = new PrintWriter(bw);
//		out.write("hey buyog");
//		String newline = System.getProperty("line.separator");
//		out.write(newline);
//		out.write("hey buyogy swag");
//		out.write(newline);
//		out.close();
		String cwd = System.getProperty("user.dir");
		System.out.println(cwd);
		ClassLoader loader = Test.class.getClassLoader();
        System.out.println(loader.getResource("Test.class"));
		
		
		
		
	}
	
}

