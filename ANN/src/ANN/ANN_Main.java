/*-----------------------------------------------------------------------------------------------------
:: Name: Chad Collins.
:: Class: CSCI 402.
:: Project: Assignment 3/Final; Artificial Neural Network with Back Propagation.
::
:: For this project, I used the information provided by the slides from the course to come up with and
:: solve whether mushrooms are poisonous of edible based on the UCI mushroom dataset. This is done by using
:: an artificial neural network with back propagation, using a sigmoid function.
---------------------------------------------------------------------------------------------------------*/

package ChadCollinsAsmt3;

import java.io.*;
import java.util.*;

public class ANN_Main {
	long startTime = System.currentTimeMillis();
	static int TRAININGMAXLINES = 0;
	static int TESTINGMAXLINES = 0;
	
	final ArrayList<Neuron> inputLayer = new ArrayList<Neuron>();
    final ArrayList<Neuron> hiddenLayer = new ArrayList<Neuron>();
    final ArrayList<Neuron> outputLayer = new ArrayList<Neuron>();
    final Neuron bias = new Neuron();
    final int[] layers;
	
    static double inputs[][];
    static double testInputs[][];
    static double trainingOutputs[][];
    static double resultOutputs[][];
    static double testingOutputs[][];
    static double output[];
    
    final double learningSpeed = 0.9;		// Learning rate.
    final double weightChange = 0.7;		// Rate of change, or momentum for the weights.
    static int inputNeurons = 22;
    static int hiddenNeurons = 4;
    static double minFailureRate = 0.001;	// Failure rate before the training data completes
    static int maxEpochs = 2000;			// Maximum epochs before the training data completes
    
    public static void main(String[] args) {
        // Process the data to numbers and then into test or training files.
        File inputFile = new File("agaricus-lepiota.data");
        /*
        if(0 < args.length) {
            inputFile = new File(args[0]);
        } else {
            System.err.println("Invalid argument count:" + args.length);
            System.exit(0);
        }*/
        System.out.println("Processing data...");
        ProcessInput.processFile(inputFile);
        ProcessOutput.createOutputFile();
        getValues();

        ANN_Main trainNetwork = new ANN_Main(inputNeurons, hiddenNeurons, 1, false);
        trainNetwork.processNetwork();
        trainNetwork.printValues("f");
        trainNetwork.runTest();
    }
    
	/*
	 * name: ANN_Main
	 * 
	 * purpose: Constructor. Calculates initial values of the input, hidden, and output nodes.
	 * 	Also calculates their initial weights
	 * 
	 * input: input, hidden, and output nodes
	 * 
	 * return: nothing
	 */  
    public ANN_Main(int input, int hidden, int output, boolean runTestData) {
        this.layers = new int[] { input, hidden, output };
        // Create layers
        for (int i = 0; i < layers.length; i++) {
        	// Input layer
            if (i == 0) {
                for (int j = 0; j < layers[i]; j++) {
                    Neuron neuron = new Neuron();
                    inputLayer.add(neuron);
                }
            } 
            // Hidden layer
            else if (i == 1) {
                for (int j = 0; j < layers[i]; j++) {
                    Neuron neuron = new Neuron();
                    neuron.initialConnection(inputLayer);
                    neuron.addBiasConnection(bias);
                    hiddenLayer.add(neuron);
                }
            }
            // Output layer
            else if (i == 2) {
                for (int j = 0; j < layers[i]; j++) {
                    Neuron neuron = new Neuron();
                    neuron.initialConnection(hiddenLayer);
                    neuron.addBiasConnection(bias);
                    outputLayer.add(neuron);
                }
            } else {
                System.out.println("Error initializing NeuralNetwork.");
            }
        }
    	ProcessOutput.processInitialValues(hidden, learningSpeed);
    	// Calculate random weights for the hidden and output layers
        Random rand = new Random();
	    for (Neuron neuron : hiddenLayer) {
	        ArrayList<NetworkConnection> connections = neuron.getAllConnections();
	        for (NetworkConnection conn : connections) {
	            double weight = (rand.nextDouble() * 2 - 1);
	            conn.setWeight(weight);
	            ProcessOutput.processWeights(neuron.id, conn.id, weight, "h", "i", hiddenNeurons, inputNeurons);
	        }
	    }
	    for (Neuron neuron : outputLayer) {
	        ArrayList<NetworkConnection> connections = neuron.getAllConnections();
	        for (NetworkConnection conn : connections) {
	            double weight = (rand.nextDouble() * 2 - 1);
	            conn.setWeight(weight);
	            ProcessOutput.processWeights(neuron.id, conn.id, weight, "o", "i", hiddenNeurons, inputNeurons);
	        }
	    }
        Neuron.idCounter = 0;
        NetworkConnection.idCounter = 0; 
        printValues("dummystring");  
    }
  
	/*
	 * name: printValues
	 * 
	 * purpose: Print weights to the results file
	 * 
	 * input: String to determine initial, intermediate, or final values
	 * 
	 * return:
	 */  
    public void printValues(String s) {
	    for (Neuron neuron : hiddenLayer) {
	        ArrayList<NetworkConnection> connections = neuron.getAllConnections();
	        for (NetworkConnection conn : connections)
	            ProcessOutput.processWeights(neuron.id, conn.id, conn.getWeight(), "h", s, hiddenNeurons, inputNeurons);
	    }
	    for (Neuron neuron : outputLayer) {
	        ArrayList<NetworkConnection> connections = neuron.getAllConnections();
	        for (NetworkConnection conn : connections)
	            ProcessOutput.processWeights(neuron.id, conn.id, conn.getWeight(), "o", s, hiddenNeurons, inputNeurons);
	    }
    }
	
    /*
	 * name: processNetwork
	 * 
	 * purpose: Main function to run the ANN and BP. Breaks if either max epochs is hit or
	 * 	minimum failure rate is surpassed.
	 * 
	 * input:
	 * 
	 * return:
	 */  
    private void processNetwork() {
        int currEpoch;
        double accuracy = 0;
        double failureRate = 1;
        for (currEpoch = 0; currEpoch < maxEpochs && failureRate > minFailureRate; currEpoch++) {
            failureRate = 0;
            for (int p = 0; p < inputs.length; p++) {
                setInput(inputs[p]);
                activateLayers();
                output = getOutput();
                resultOutputs[p] = output;
                for (int j = 0; j < trainingOutputs[p].length; j++) {
                    double error = Math.pow(output[j] - trainingOutputs[p][j], 2);
                    failureRate += error;
                }
                backPropagate(trainingOutputs[p]);
            }
            failureRate = failureRate/TRAININGMAXLINES;
            accuracy = 1 - failureRate;
            ProcessOutput.processEpoch(currEpoch, accuracy);
            if ((currEpoch + 1) % 100 == 0)
            	printValues("dummystring");
        }
        if (currEpoch == maxEpochs) {
            System.out.println("Training data max epochs reached.");
        } else {
        	System.out.println("Training completed, min failure rate achieved.");
        }
    	long endTime   = System.currentTimeMillis();
    	long totalTime = endTime - startTime;
    	ProcessOutput.processResults(currEpoch, accuracy, totalTime);
    }
    
	/*
	 * name: runTest
	 * 
	 * purpose: Run the training data against the testing data.
	 * 	outputs the testing accuracy to the results file
	 * 
	 * input:
	 * 
	 * return:
	 */   
    private void runTest() {
        double accuracy = 0;
            for (int p = 0; p < testInputs.length; p++) {
                setInput(testInputs[p]);
                activateLayers();
                output = getOutput();
                resultOutputs[p] = output;
                for (int j = 0; j < testingOutputs[p].length; j++) {
                    double error = Math.pow(output[j] - testingOutputs[p][j], 2);
                    accuracy += error;
                }
            }
        accuracy = 1 - (accuracy/TESTINGMAXLINES);
        ProcessOutput.processTestResults(accuracy);
    } 
    
	/*
	 * name: activateLayers
	 * 
	 * purpose: activate hidden and output layers
	 * 
	 * input:
	 * 
	 * return:
	 */  
    public void activateLayers() {
        for (Neuron n : hiddenLayer)
            n.calculateOutput();
        for (Neuron n : outputLayer)
            n.calculateOutput();
    }
    
	/*
	 * name: backPropagate
	 * 
	 * purpose: Applies the back propagation function.
	 * 
	 * input: expected outputs
	 * 
	 * return: nothing
	 */  
    public void backPropagate(double expectedOutput[]) {
        int count = 0;
        for (Neuron n : outputLayer) {
            ArrayList<NetworkConnection> connections = n.getAllConnections();
            for (NetworkConnection c : connections) {
                double out = n.getOutput();
                double p = c.prevNeuron.getOutput();
                double desiredOutput = expectedOutput[count];
                double partialDerivative = - out * (1 - out) * p * (desiredOutput - out);
                double weightDiff = -learningSpeed * partialDerivative;
                double newWeight = c.getWeight() + weightDiff;
                c.setWeightDiff(weightDiff);
                c.setWeight(newWeight + weightChange * c.getPrevWeightDiff());
            }
            count++;
        }
 
        for (Neuron n : hiddenLayer) {
            ArrayList<NetworkConnection> connections = n.getAllConnections();
            for (NetworkConnection c : connections) {
                double out = n.getOutput();
                double p = c.prevNeuron.getOutput();
                double sumOut = 0;
                int i = 0;
                for (Neuron outn : outputLayer) {
                    double cw = outn.getConnection(n.id).getWeight();
                    double desiredOutput = (double) expectedOutput[i];
                    double d = outn.getOutput();
                    i++;
                    sumOut = sumOut + (-(desiredOutput - d) * d * (1 - d) * cw);
                }
 
                double partialDerivative = out * (1 - out) * p * sumOut;
                double weightDiff = -learningSpeed * partialDerivative;
                double newWeight = c.getWeight() + weightDiff;
                c.setWeightDiff(weightDiff);
                c.setWeight(newWeight + weightChange * c.getPrevWeightDiff());
            }
        }
    }
    
    public void setInput(double inputs[]) {
        for (int i = 0; i < inputLayer.size(); i++)
            inputLayer.get(i).setOutput(inputs[i]);
    }
  
    public double[] getOutput() {
        double[] outputs = new double[outputLayer.size()];
        for (int i = 0; i < outputLayer.size(); i++)
            outputs[i] = outputLayer.get(i).getOutput();
        return outputs;
    }
    
	/*
	 * name: getValues
	 * 
	 * purpose: Update initial values of inputs, expected outputs, and fill result outputs with nothing
	 * 
	 * input: nothing
	 * 
	 * return: nothing
	 */  
    private static void getValues() {
    	File trainingFile = new File("training-data.txt");
    	File testingFile = new File("testing-data.txt");
        try {	
        	TRAININGMAXLINES = ProcessInput.countTotalLines(trainingFile);
	    } catch(IOException e){
	    	System.out.print("Error processing file. Exiting.");
	        System.exit(1);
	    }
        try {	
        	TESTINGMAXLINES = ProcessInput.countTotalLines(testingFile);
	    } catch(IOException e){
	    	System.out.print("Error processing file. Exiting.");
	        System.exit(1);
	    }
        inputs = new double[TRAININGMAXLINES][22];
        testInputs = new double[TESTINGMAXLINES][22];
        trainingOutputs = new double[TRAININGMAXLINES][1];
        resultOutputs = new double[TRAININGMAXLINES][1];
        testingOutputs = new double[TESTINGMAXLINES][1];
        BufferedReader readBuffer = null;
        BufferedReader readBuffer2 = null;
        try {
            readBuffer = new BufferedReader(new FileReader(trainingFile));
            readBuffer2 = new BufferedReader(new FileReader(testingFile));
            String delim = "[,]+";
            String line;
            for(int i = 0; i < TRAININGMAXLINES; i++) {
            	line = readBuffer.readLine();
            	String[] tokens = line.split(delim);
            	trainingOutputs[i][0] = Double.parseDouble(tokens[0]);
            	for(int j = 1; j < tokens.length; j++)
            		inputs[i][j-1] = Double.parseDouble(tokens[j]);
            }
            for(int i = 0; i < TESTINGMAXLINES; i++) {
            	line = readBuffer2.readLine();
            	String[] tokens = line.split(delim);
            	testingOutputs[i][0] = Double.parseDouble(tokens[0]);
            	for(int j = 1; j < tokens.length; j++)
            		testInputs[i][j-1] = Double.parseDouble(tokens[j]);
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
        	try {
        		readBuffer.close();
        		readBuffer2.close();
        	} catch (IOException e) {}
        }
    }
}
