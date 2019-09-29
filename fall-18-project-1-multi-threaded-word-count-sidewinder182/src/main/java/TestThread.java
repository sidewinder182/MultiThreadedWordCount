import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestThread extends Thread{
	AtomicInteger i = new AtomicInteger();
	
	public TestThread(AtomicInteger i)
	{
		this.i= i;
	}
	
	public void run()
	{
		this.i.set(20);
		
		
	}
}
