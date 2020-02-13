/*-----------------------------------------------------------------------------------------------------
:: ProcessOutput.java
::
:: Contains functions used for processing the output data to the results.txt file.
---------------------------------------------------------------------------------------------------------*/
package ChadCollinsAsmt3;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class ProcessOutput {
	static DecimalFormat df1 = new DecimalFormat(".#");
	static DecimalFormat df2 = new DecimalFormat("#.##");
	static DecimalFormat df3 = new DecimalFormat("##.##");
	
	/*
	 * name: processInitialValues
	 * 
	 * purpose: process the initial values for the training set
	 * 
	 * input:
	 * 
	 * return:
	 */  
	public static void processInitialValues(int hiddenNodes, 
			double learningSpeed) {
		
    	BufferedWriter writeBuffer = null;

        try {
            writeBuffer = new BufferedWriter(new FileWriter("results.txt", true));
            writeBuffer.write("Hidden Layers used: 1.");
            writeBuffer.write(" Neurons used: ");
	        writeBuffer.write(hiddenNodes + "\n");
	        
	        writeBuffer.write("Initial learning speed: ");
	        writeBuffer.write(df2.format(learningSpeed) + "\n");
	        
        } catch(IOException e){}
        finally {
        	closeStream(writeBuffer);
        }
	}
	
	/*
	 * name: processWeights
	 * 
	 * purpose: process the weights of each neuron and its connecting nodes
	 * 
	 * input: 
	 * 
	 * return:
	 */ 	
	static public void processWeights(int nid, int cid, double weight, String s1, String s2, int hiddenLayer, int nodes) {
    	BufferedWriter writeBuffer = null;

        try {
            writeBuffer = new BufferedWriter(new FileWriter("results.txt", true));
            if (s1.equals("h")) {
            	writeBuffer.write("Hidden ");
            	writeBuffer.write("neuron# ");
            	writeBuffer.write(((nid) % nodes)+ ", ");
            } else {
            	writeBuffer.write("Output ");
            	writeBuffer.write("neuron, ");
            }
            
            writeBuffer.write("node# ");
            writeBuffer.write((cid % (nodes+1))+ ". ");
            if (s2.equals("i"))
            	writeBuffer.write("Initial ");
            else if (s2.equals("f"))
            	writeBuffer.write("Final ");
            else
            	writeBuffer.write("Intermediate ");
            writeBuffer.write("weight: ");
            writeBuffer.write(df2.format(weight) + ".\n");
        } catch(IOException e){}
        finally {
        	closeStream(writeBuffer);
        }
	}
	
	static public void processEpoch(int id, double accuracy) {
    	BufferedWriter writeBuffer = null;

        try {
            writeBuffer = new BufferedWriter(new FileWriter("results.txt", true));
            writeBuffer.write("End of epoch ");
            writeBuffer.write(id + ". ");
            writeBuffer.write("Accuracy: ");
            writeBuffer.write(df3.format(accuracy*100) + "%.\n");
        } catch(IOException e){}
        finally {
        	closeStream(writeBuffer);
        }
	}
	
    /*
     * name: processResults
     * 
     * purpose: Prints the result of a successful training set to the result file
     * 
     * input: 
     * 
     * return: nothing
     */  
    public static void processResults(int id, double accuracy, double runtime) {
    	
    	BufferedWriter writeBuffer = null;
        try {
            writeBuffer = new BufferedWriter(new FileWriter("results.txt", true));
            writeBuffer.write("TOTAL EPOCHS: ");
            writeBuffer.write(id + ". ");
            writeBuffer.write("ACCURACY: ");
            writeBuffer.write(df3.format(accuracy*100) + "%.\n");
            writeBuffer.write("RUNTIME: ");
            writeBuffer.write(runtime + " ms.\n");
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
        	closeStream(writeBuffer);
        }
    }
  
    /*
     * name: processTestResults
     * 
     * purpose: Prints the accuracy result of a testing set to the results file
     * 
     * input: accuracy
     * 
     * return: nothing
     */  
    public static void processTestResults(double accuracy) {
    	
    	BufferedWriter writeBuffer = null;
        try {
            writeBuffer = new BufferedWriter(new FileWriter("results.txt", true));
            writeBuffer.write("ACCURACY FOR TESTING: ");
            writeBuffer.write(df3.format(accuracy*100) + "%.\n");
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
        	closeStream(writeBuffer);
        }
    }
    /*
     * name: createOutputFile
     * 
     * purpose: Creates a blank output file and closes it for later use
     * 
     * input: nothing
     * 
     * return: nothing
     */
    public static void createOutputFile() {
    	FileWriter resultsFile = null;
    	try {
    		resultsFile = new FileWriter("results.txt");
    	} catch (IOException e) {}
    	finally {
    		closeStream(resultsFile);
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

}
