/*-----------------------------------------------------------------------------------------------------
:: NetworkConnection.java
:: 
:: Used for keeping track of each connection node of a Neuron, its weights, ID, and previous neuron.
---------------------------------------------------------------------------------------------------------*/
package ChadCollinsAsmt3;

public class NetworkConnection {
    final Neuron prevNeuron;
    final Neuron currNeuron;
    final public int id;
    static int idCounter = 0;
    double weight = 0;
    double prevDiff = 0;
    double weightDiff = 0;
    
	/*
	 * name: NetworkConnection
	 * 
	 * purpose: Constructor. Tracks the previous and current nodes and attaches an id
	 * 
	 * input: previous neuron, current neuron
	 * 
	 * return:
	 */  
    public NetworkConnection(Neuron prev, Neuron curr) {
        prevNeuron = prev;
        currNeuron = curr;
        id = idCounter;
        idCounter++;
    }
 
    public void setWeight(double w) {
        weight = w;
    }
    public void setWeightDiff(double w) {
        prevDiff= weightDiff;
        weightDiff = w;
    }
 
    public double getWeight() {
        return weight;
    }
    public double getPrevWeightDiff() {
        return prevDiff;
    }
    public Neuron getPreviousNeuron() {
        return prevNeuron;
    }
}
