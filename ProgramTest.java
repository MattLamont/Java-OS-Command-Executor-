


public class ProgramTest 
{
	//The instance of the OS task coordinator used in this test
	public static OSTaskCoordinator runner;

	public static void main(String[] args) 
	{
		System.out.println( "STARTING TEST\n\n" );
		
		try 
		{
			//initialize the os task coordinator
			runner = new OSTaskCoordinator( "commands.txt" );
			
			//Run the commands given to the task coordinator. While this runner executes
			//each of the given scripts concurrently, it will block the main thread
			//until all scripts have finished.
			runner.runCommands();
		} 
		catch (Exception e1) 
		{
			System.out.println( "Error: Commands not successfully run from main." );
			e1.printStackTrace();
		}
		
		try 
		{
			//print out the results of the OS operations to th user
			System.out.println( "	WORDS MATCHING TAG: 'phant'\n");
			System.out.println( runner.getScriptOutput( 0 ) + "\n\n" );
			
			System.out.println( "	WORDS MATCHING TAG: 'zun'\n");
			System.out.println( runner.getScriptOutput( 1 ) + "\n\n" );
			
			System.out.println( "	WORDS MATCHING TAG: 'veri'\n");
			System.out.println( runner.getScriptOutput( 2 ) + "\n\n" );
			
			System.out.println( "	WORDS MATCHING TAG: 'func'\n");
			System.out.println( runner.getScriptOutput( 3 ) + "\n\n" );
			
			System.out.println( "	WORDS MATCHING TAG: 'wolv'\n");
			System.out.println( runner.getScriptOutput( 4 ) + "\n\n" );
		}
		catch( NullPointerException e )
		{
			System.out.println( e.getMessage() );
		}
		
		System.out.println( "\n\n\nENDING TEST" );
		
		
	}
}


