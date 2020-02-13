/*-----------------------------------------------------------------------------------------------------
:: ProcessInput.java
::
:: Contains functions used to process the mushroom dataset file into training and testing data files.
---------------------------------------------------------------------------------------------------------*/
package ChadCollinsAsmt3;
import java.io.*;

public class ProcessInput {
	
    private static int MAXLINES;	// Max lines in mushroom data file
    
    /*
     * name: processFile
     * 
     * purpose: Process the mushroom data file by converting the given characters 
     * 	to digits
     * 
     * input: mushroom input file
     * 
     * return: nothing
     */  
    public static void processFile(File inputFile) {
        try {
            MAXLINES = countTotalLines(inputFile);
        } catch(IOException e){
        	System.out.print("Error processing file. Exiting.");
            System.exit(1);
        }
        int trainingCount = 0;
        int testingCount = 0;
        FileReader readFile = null;
        FileWriter writeTrainingFile = null;
        FileWriter writeTestingFile = null;
        
        try {
            BufferedReader readBuffer = new BufferedReader(new FileReader(inputFile));
            writeTrainingFile = new FileWriter("training-data.txt");
            writeTestingFile = new FileWriter("testing-data.txt");
            
            String line;
            while((line = readBuffer.readLine()) != null) {

                if(findSplit(trainingCount, testingCount) == 0) {
                    writeToFile(writeTrainingFile, line);
                    trainingCount++;
                } else {
                    writeToFile(writeTestingFile, line);
                    testingCount++;
                }
            }
            readBuffer.close();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
        	closeStream(readFile);
        	closeStream(writeTestingFile);
        	closeStream(writeTrainingFile);
        }
    }
    
    /*
     * name: closeStream
     * 
     * purpose: Closes an open stream
     * 
     * input: any stream
     * 
     * return: nothing
     */  
    public static void closeStream(Closeable stream) {
        try {
            if(stream != null)
                stream.close();
        } catch(IOException e) {}
    }
    
    /*
     * name: countTotalLines
     * 
     * purpose: Counts the total number of lines in a file
     * 
     * input: any file
     * 
     * return: total number of lines in a file
     */  
    public static int countTotalLines(File inputFile) throws IOException {
        InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
        try {
            byte[] chr = new byte[1024];
            int count = 0;
            int readChar = 0;
            boolean empty = true;
            while ((readChar = inputStream.read(chr)) != -1) {
               empty = false;
               for (int i = 0; i < readChar; ++i) {
                   if (chr[i] == '\n')
                       count++;
               }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            inputStream.close();
        }
    }
    
    /*
     * name: findSplit
     * 
     * purpose: Split the mushroom data file into an 85/15 split for testing and training purposes
     * 
     * input:
     * 
     * return: 0 or 1 to indicate training or testing file to write to
     */  
    private static int findSplit(int trainingCounter, int testingCounter) {
        if ((double) trainingCounter/MAXLINES >= 0.85)
            return 0;
        else if ((double) testingCounter/MAXLINES >= 0.15)
            return 1;
        else {
            double val = Math.random();
            if (val < 0.85)
                return 0;
            else
                return 1;
        }
    }
    
    /*
     * name: writeToFile
     * 
     * purpose: Convert and write data from the mushroom data set to a new file.
     * 	Used to write to the testing and training data files
     * 
     * input: a file to write to, the line to write
     * 
     * return: nothing
     */  
    private static void writeToFile(FileWriter writeFile, String line) throws IOException {
        String delim = "[,]+";
        String[] tokens = line.split(delim);
        int i;
        for(i = 0; i < (tokens.length - 1); i++) {
        	if (i == 0) {
        		if (tokens[0].equals("p"))
    			writeFile.write("0,");
        		else if (tokens[0].equals("e"))
    			writeFile.write("1,");
        	} else {
    			writeFile.write(Math.abs(Character.getNumericValue(tokens[i].charAt(0))/50.) + ",");
    		}
        }
        writeFile.write(Math.abs(Character.getNumericValue(tokens[i].charAt(0))/50.) + "\n");
    }

}

