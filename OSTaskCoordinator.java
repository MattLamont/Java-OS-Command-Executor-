import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;


public class OSTaskCoordinator
{
	//String that holds the name of the file that contains the OS Commands
	private String commandFilename;
	
	//regex string for checking file extension
	private final String FILE_TYPE_REGEX = "([^\\s]+(\\.(?i)(txt))$)";
	
	//regex string that is used to search file for commands
	private final String COMMAND_STRING = "^<COMMAND>$";
	
	//regex string that is used to search for end of command
	private final String COMMAND_END_STRING = "^<END>$";
	
	//a list of strings to hold the os scripts
	private List< ArrayList<String> > scriptList;
	
	//an arraylist to hold the command executor components
	private List<OSCommandExecutor> executorList;
	
	
	//A constructor that takes the filename that contains the commands
	public OSTaskCoordinator( final String filename ) throws Exception
	{
		commandFilename = filename;
		scriptList = new ArrayList< ArrayList<String>>();
		executorList = new ArrayList<OSCommandExecutor>();
		getFileCommands();
	}
	
	public void runCommands()
	{
		//Start all the scripts in the script list in their own command executor
		for( int i = 0 ; i < scriptList.size() ; i++ )
		{
			try 
			{
				OSCommandExecutor current = new OSCommandExecutor( scriptList.get(i));
				current.executeCommand();
				executorList.add( current );
			} 
			catch (IOException e) 
			{
				System.out.println( "Error: Problem running command." );
				e.printStackTrace();
			}
		}
		
		//Spin in this loop until all commands have been executed
		Boolean finished = false;
		while( finished == false )
		{
			finished = true;
			
			for( int i = 0 ; i < executorList.size() ; i++ )
			{
				OSCommandExecutor current = executorList.get(i);
				
				if( current.getReturnCode() == -1 )
				{
					finished = false;
				}
			}
		}
		
		//Print the return codes of the threads to the user
		for( int i = 0 ; i < executorList.size() ; i++ )
		{
			OSCommandExecutor current = executorList.get(i);
			System.out.print( "> " );
			System.out.print( current.getProcessId() );
			System.out.print( " has returned with exit value: " );
			System.out.print( current.getReturnCode() + "\n" );	
		}
		
		System.out.println( "\n> All threads finished\n\n" );
	}
	
	private void getFileCommands() throws Exception
	{
		//make regex pattern to check if the filename had .txt extension
		Pattern pattern = Pattern.compile( FILE_TYPE_REGEX );
		java.util.regex.Matcher matcher = pattern.matcher( commandFilename );
		
		//if not text file then throw exception
		if( matcher.matches() == false )
		{
			System.out.println( "Error: " + commandFilename + " is not a .txt file.\n" );
			throw new Exception( "Error: " + commandFilename + " is not a .txt file.\n" );
		}
		
		//Initializing variables for reading in the file
		Scanner fileScanner = new Scanner( new File( commandFilename ));
		String lineBuffer = new String("");
		ArrayList<String> script = new ArrayList<String>();

		
		while( fileScanner.hasNextLine() )
		{
			lineBuffer = fileScanner.nextLine();
			
			//marks the beginning of a script of commands
			if( checkCommandBegin( lineBuffer ) )
			{
				script = new ArrayList<String>();
				continue;
			}
	
			//marks the end of a script of commands
			else if( checkCommandEnd( lineBuffer ))
			{
				scriptList.add( script );
				continue;
			}
			
			//If the line is empty, then skip to next line
			else if( lineBuffer == "" )
			{
				continue;
			}
			
			//add the command to the current script
			else
			{
				script.add( lineBuffer );
			}
		}
		
		fileScanner.close();
		
	}
	
	private Boolean checkCommandBegin( String line )
	{
		//check for the <COMMAND> string. returns true is present.
		Pattern pattern = Pattern.compile(COMMAND_STRING);
		java.util.regex.Matcher matcher = pattern.matcher( line );
		if( matcher.matches() )
		{
			return true;
		}
		
		else
		{
			return false;
		} 
	}
	
	private Boolean checkCommandEnd( String line )
	{
		//check for <END> string. returns true is present
		Pattern pattern = Pattern.compile(COMMAND_END_STRING);
		java.util.regex.Matcher matcher = pattern.matcher( line );
		if( matcher.matches() )
		{
			return true;
		}
		
		else
		{
			return false;
		} 
	}
	
	//returns the output of the given script
	public String getScriptOutput( int scriptNumber )
	{
		//Check if invalid script number
		if( scriptNumber < 0 || scriptNumber > executorList.size() - 1 )
		{
			throw( new NullPointerException( "Error: There is not script number: "+ scriptNumber));
		}
		
		//returns the output of the given script
		OSCommandExecutor current = executorList.get(scriptNumber);
		return current.getCommandStandardOutput();
	}
}
