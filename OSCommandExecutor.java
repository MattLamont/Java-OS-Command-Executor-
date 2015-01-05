import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class OSCommandExecutor 
{
	
	//The os command provided through the constructor to be passed to the OS and run
	ArrayList<String> OS_command;
	
	//The string representing the current working directory of the process to be executed
	String workingDirectory;
	
	//The process builder that will be used to run the OS command
	ProcessBuilder processBuilder;
	
	//The output stream that is taken from the process created by processbuilder
	InputStream processOutputStream;
	
	//The input stream that is taken from the process created by processbuilder
	OutputStream processInputStream;
	
	//the process returned by processbuilder when it is started
	Process process;
	
	//This is an instance of the thread class I created to read in and log the process's
	//standard output stream.
	CommandOutputStreamReader processOutputThread;
	
	//This is the instance of the thread used to wait for the end of the command that was run.
	CommandExitDetectThread exitDetectThread;
	
	
	//A constructor that configures the process builder with the given command and the directory
	//to be executed in.
	public OSCommandExecutor( ArrayList<String> command_list )
	{
		if( command_list == null )
		{
			throw new NullPointerException( "Error: the provided list of commands is empty." );
		}
		
		this.OS_command = command_list;
		this.workingDirectory = null;
		
		this.processBuilder = new ProcessBuilder();
		this.processBuilder.command( OS_command );
		
	}
	
	
	public void executeCommand() throws IOException
	{
		
		try 
		{
			//I'm redirecting the process's error stream into the output stream for simplicity's
			//sake.
			this.processBuilder.redirectErrorStream( true );
			
			this.process = processBuilder.start();
			
			this.processOutputStream = process.getInputStream();
			this.processInputStream = process.getOutputStream();
			
			//setup thread to capture the output of the command
			processOutputThread = new CommandOutputStreamReader( processOutputStream );
			processOutputThread.start();
			
			//setup thread to wait for the end of the command
			this.exitDetectThread = new CommandExitDetectThread( processOutputThread , process );
			this.exitDetectThread.start();
		}
		
		catch (IOException e) 
		{
			System.out.println( "Error: IO problem when starting the OS command." );
			e.printStackTrace();
		} 
	}
	
	
	public String getCommandStandardOutput()
	{
		return processOutputThread.getProcessOutput();
	}
	
	//returns the exit code of the exit thread
	public int getReturnCode()
	{
		return exitDetectThread.getExitValue();
	}
	
	//returns a string with exit thread's id and name
	public String getProcessId()
	{
		String id = new String();
		id = "Thread: ";
		id += exitDetectThread.getName();
		id += "	ID#: ";
		id += exitDetectThread.getId();
		return id;
	}
}
