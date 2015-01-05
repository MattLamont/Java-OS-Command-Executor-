


public class CommandExitDetectThread extends Thread 
{
	private Process workerProcess;
	private CommandOutputStreamReader processThread;
	private int exitValue;
	
	//Constructor
	public CommandExitDetectThread( CommandOutputStreamReader thread , Process process )
	{
		workerProcess = process;
		processThread = thread;
		exitValue = -1;
	}
	
	//This thread's run method just waits for the other thread to finish and then returns control.
	public void run()
	{
		try 
		{
			exitValue = workerProcess.waitFor();
			processThread.interrupt();
			processThread.join();
		} 
		catch (InterruptedException e) 
		{
			System.out.println( "Error: Thread waiting for process to end was interupted." );
			e.printStackTrace();
		}
		
	}
	
	//This function provides the user with the process's exit value if needed.
	public int getExitValue()
	{
		return exitValue;
	}
}
