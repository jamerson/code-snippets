
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConcurrentRunnerThread extends Thread {
	private static ConcurrentRunnerThread instance = null;
	private ConcurrentLinkedDeque<IRunnerUnit> queue;
	private AtomicBoolean stopExecution;
	
	private ConcurrentRunnerThread(){
		setName("ConcurrentRunnerThread");
		setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandlerCommon());
		queue = new ConcurrentLinkedDeque<IRunnerUnit>();
		stopExecution = new AtomicBoolean(false);
	}
	
	public static synchronized Runner getInstance(){
		if(instance == null){
			instance = new ConcurrentRunnerThread();
		}
		
		return instance;
	}
	
	public void add(RunnerActionInterface runnerAction){
		try{
			queue.add(runnerAction);
		}catch(NullPointerException e){
			//TODO: Handle this exception
		}
	}
	
	@Override
	public void run() {
		while(!stopExecution.get()){
			RunnerActionInterface runnerAction = queue.poll();
			
			if(runnerAction != null) {
				runnerAction.action();
			}
		}
		super.run();
	}
	
	public void stopExecution(){
		stopExecution.set(true);
	}
	 
}
