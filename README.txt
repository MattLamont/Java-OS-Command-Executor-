Matt Lamont
830382541
Homework 4 and 5
Readme


Included Files
	-OSCommandExecutor.java
	-CommandOutputStreamReader.java
	-ProgramTest.java (contains main function)
	-CommandExitDetectThread.java
	-OSTaskCoordinator.java
	-commands.txt (contains the linux commands)
	-Sample_Output.txt (sample of the program output)


Program Design

	This test program is currently configured to execute Linux commands
	with BASH. The main program calls an instance of OSTaskCoordinator.
	This class takes in a supplied ".txt" file containing OS scripts
	that can be executed concurrently. The OSTaskCoordinator utilizes
	the OSCommandExecutor component to concurrently execute all the 
	scripts found in the file. 
	
	Currently, the linux scripts can be found within "commands.txt".
	Each script is begins with string "<COMMAND>" and ends with the
	string "<END>". There are 5 scripts that copy "american-english" 		dictionary into the Documents directory and extracts words matching a 		unique tag (I couldn't find "en-US.dic" on my system). These matching 		words are placed into separate files and also displayed to the user.
	Some sample output can be viewed in the file "Sample_Output.txt".
