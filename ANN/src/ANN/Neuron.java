/*-----------------------------------------------------------------------------------------------------
:: Neuron.java
::
:: Class to keep track of individual neurons, including calculating their outputs.
---------------------------------------------------------------------------------------------------------*/
package ChadCollinsAsmt3;
import java.util.*;

public class Neuron {   
    
    NetworkConnection biasConnection;
    final double bias = -1;
    final public int id;
    static int idCounter = 0;
    double output;
     
    ArrayList<NetworkConnection> connections = new ArrayList<NetworkConnection>();
    HashMap<Integer,NetworkConnection> connectionNum = new HashMap<Integer, NetworkConnection>();
     
    public Neuron(){        
        id = idCounter;
        idCounter++;
    }
    
	/*
	 * name: calculateOutput
	 * 
	 * purpose: calculate the updated output weight using a sigmoid function
	 * 
	 * input:
	 * 
	 * return:
	 */ 
    public void calculateOutput(){
        double n = 0;
        for(NetworkConnection con : connections){
            Neuron prevNeuron = con.getPreviousNeuron();
            double weight = con.getWeight();
            double x = prevNeuron.getOutput();
             
            n = n + (weight*x);
        }
        n = n + (biasConnection.getWeight()*bias);
         
        output = findSigmoid(n);
    }
 
    double findSigmoid(double x) {
        return 1.0 / (1.0 + (Math.exp(-x)));
    }
    
	/*
	 * name: initialConnection
	 * 
	 * purpose: Setup the initial connections for each neuron
	 * 
	 * input: list of neurons
	 * 
	 * return:
	 */ 
    public void initialConnection(ArrayList<Neuron> neurons){
        for(Neuron n: neurons){
        	NetworkConnection con = new NetworkConnection(n, this);
            connections.add(con);
            connectionNum.put(n.id, con);
        }
    }
 
	/*
	 * name: addBiasConnection
	 * 
	 * purpose: add a connection bias for output function
	 * 
	 * input:
	 * 
	 * return:
	 */ 
    public void addBiasConnection(Neuron n){
    	NetworkConnection con = new NetworkConnection(n,this);
        biasConnection = con;
        connections.add(con);
    }
    
    public void setOutput(double out){
        output = out;
    }
    
    public ArrayList<NetworkConnection> getAllConnections(){
        return connections;
    }
    public double getBias() {
        return bias;
    }
    public double getOutput() {
        return output;
    }
    public NetworkConnection getConnection(int neuronIndex){
        return connectionNum.get(neuronIndex);
    }
}
