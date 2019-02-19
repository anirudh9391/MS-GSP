The folder contains seven java files:

ReadFile.java - Used for reading inputs from data and parameter files.
             Path to root folder containing these files needs to be specified here.

fSequence.java - Associates every sequence with their respective counts

MScandidategen.java - Used for generating candidates of k>2

Level2CandidateGen.java - Used for generating candidates of length 2(k=2)

MSGSPMain.java - Contains the major portion of the algorithm, including writing the output to the file, calls different functions from dependant files

Seq.java - Maintains a sequence and implements a funcitonality of checking if given is a subset

ExtSequence.java - Provides functionality of getting first item, last item, checking for subsets in a supersequence. 

Copy the input data to "data.txt" file and the parameters to the "para.txt" file

To RUN the program:
Required: Java 8
Run MSGSPMain.java file in the above environment, providing appropriate inputs(data, parameters).
The result will be written to the "output.txt" file that gets generated in the root folder. A JDK is required for a java compiler to funciton. 