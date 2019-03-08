/*
 * Main class for neural netowrk implamentation, build the net, trains and etc.
 */
package csd3939_coursework2;
import static csd3939_coursework2.CSD3939_CourseWork2.LEARNING_RATE;
import static csd3939_coursework2.CSD3939_CourseWork2.BIAS_RANGE_SMALLEST;
import static csd3939_coursework2.CSD3939_CourseWork2.BIAS_RANGE_BIGGEST;
import static csd3939_coursework2.CSD3939_CourseWork2.WEIGHTS_RANGE_SMALLEST;
import static csd3939_coursework2.CSD3939_CourseWork2.WEIGHTS_RANGE_BIGGEST;
/**
 * @author Antanas
 * @date 28th of February 2019
 */
public class NetworkBase {

    /*Constats defining the neural network*/
    public final int[] NETWORK_LAYER_SIZE;
    public final int INPUT_LAYER_SIZE;
    public final int OUTPUT_LAYER_SIZE;
    public final int NETWORK_SIZE;
       
    // First dimension - layer index, 2nd dimension - neuron index
    private double[][] output;
    //1st - layer, 2nd - neuron, 3rd - previous layer neuron
    private double[][][] weights;
    //1st layer, 2nd - neuron
    private double[][]bias;
    
    private double[][] error_signal;
    private double[][] output_derivative;
    
    /**
     * Constructor method for the neural network
     * @param NETWORK_LAYER_SIZE the size of neural network
     */
    public NetworkBase(int[] NETWORK_LAYER_SIZE){
        this.NETWORK_LAYER_SIZE = NETWORK_LAYER_SIZE;
        this.INPUT_LAYER_SIZE = NETWORK_LAYER_SIZE[0];
        this.NETWORK_SIZE = NETWORK_LAYER_SIZE.length;
        this.OUTPUT_LAYER_SIZE = NETWORK_LAYER_SIZE[NETWORK_SIZE-1]; //Index of the first layer is 0
        //=output[layer][neuron]
        this.output = new double[NETWORK_SIZE][];
        //=weights[layer][neuron][prevNeuron]
        this.weights = new double[NETWORK_SIZE][][];
        //=bias[layer][neuron]
        this.bias = new double[NETWORK_SIZE][];
        this.error_signal = new double[NETWORK_SIZE][];
        this.output_derivative = new double[NETWORK_SIZE][];
        
        for(int index = 0; index < NETWORK_SIZE; index++){
            //Set the size of the layers
            this.output[index] = new double[NETWORK_LAYER_SIZE[index]];
            //Holds error values
            this.error_signal[index] = new double[NETWORK_LAYER_SIZE[index]];
            //Holds the derivative values for change sensitivity  
            this.output_derivative[index] = new double[NETWORK_LAYER_SIZE[index]];
            //Fills the bias with random 
            this.bias[index] = Utility.buildRandomArray(NETWORK_LAYER_SIZE[index], BIAS_RANGE_SMALLEST, BIAS_RANGE_BIGGEST);
            
            //Exclude the input layer[0] 
            if(index > 0){
                //Weights for a layer, also specifie the previous layer.
                weights[index] = Utility.buildRandomArray(NETWORK_LAYER_SIZE[index], NETWORK_LAYER_SIZE[index-1], WEIGHTS_RANGE_SMALLEST, WEIGHTS_RANGE_BIGGEST);
            }
        }
    }

   /**
    * Feedforward function
    * @param input
    * @return 
    */
    public double[] calculationFunction(double[] input){
        //If this is true, too much or not enough data, and it is impossible
        //to perform a calculation, return null
        if(input.length != this.INPUT_LAYER_SIZE){
            return null;
        }
        // index 0 is not really a layer, it sorta a buffer to store the data
        this.output[0] = input;
        // Iteration through the rest of the layers 
        for(int layer = 1; layer < NETWORK_SIZE; layer ++){
            //Iterate trough every neuron in specified layer
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZE[layer]; neuron++){
                //Sum 
                
                double sum = 0;
                for(int previousNeuron = 0; previousNeuron < NETWORK_LAYER_SIZE[layer-1];
                        previousNeuron++){
                    //Sum is increast by the output of previous layer and
                    //multiplied by the weights of a previous neuron
                    sum += output[layer-1][previousNeuron] * 
                            weights[layer][neuron][previousNeuron];
                }
                //Increase the sum with the bias of a particular neuron
                sum += bias[layer][neuron];
                
                //Get the sigmoid of the sum
                output[layer][neuron] = sigmoidFunction(sum);
                output_derivative[layer][neuron] = output[layer][neuron] * (1 - output[layer][neuron]);
                
            }
        }
        // Last layer return
        return output[NETWORK_SIZE-1];
    }
   
    /**
     * Training for the specified batch 
     * @param set the set used for training
     * @param loops amount of loops to process
     * @param batch_size  batch size used only in bigger sets ( should improve accuracy )
     */
    public void train(TrainingSet set, int loops, int batch_size){
        if(set.INPUT_SIZE != INPUT_LAYER_SIZE || set.OUTPUT_SIZE != OUTPUT_LAYER_SIZE){
            return;
        }
        for(int index = 0; index < loops; index++){
            TrainingSet batch = set.extractBatch(batch_size);
            for(int b = 0; b < batch_size; b++){
                this.training(batch.getInput(b), batch.getOutput(b), LEARNING_RATE);
            }
        }
    }
    
    /**
     * Function for average squared difference between the estimated values and what is estimated
     * @param input array of guesses
     * @param target array of targets
     * @return 
     */
    public double MeanSquaredErrorFunction(double[] input, double[] target){
        if(input.length != INPUT_LAYER_SIZE || target.length != OUTPUT_LAYER_SIZE){
            return 0;
        }
        calculationFunction(input);
        double error = 0;
        for(int index = 0; index < target.length; index++){
            error += (target[index] - output[NETWORK_SIZE-1][index]) * (target[index] - output[NETWORK_SIZE-1][index]);
        }
        return error / (2D * target.length);
    }
    
    /**
     * Average squared difference between the estimated values and what is estimated for the set
     * @param set training set
     * @return squared error of a set
     */
    public double MeanSquaredErrorFunction(TrainingSet set){
        double error = 0;
        for(int index = 0; index < set.size(); index++){
            error += MeanSquaredErrorFunction(set.getInput(index), set.getOutput(index));
        }
        return error / set.size();
    }
    
    /**
     * Training function as a main
     * @param input input array
     * @param target target array 
     * @param learningRate  learning rate 
     */
    public void training(double[] input, double[] target, double learningRate){
        if(input.length != INPUT_LAYER_SIZE || target.length != OUTPUT_LAYER_SIZE){
            return;
        }
        calculationFunction(input);
        backpropError(target);
        updateWeights(learningRate);
    }
    /**
     * calculate a gradient that is needed in the calculation of the weights to be used in the network
     * @param target  target output
     */
    public void backpropError(double[] target){
        for(int neuron = 0; neuron < NETWORK_LAYER_SIZE[NETWORK_SIZE-1]; neuron++){
            error_signal[NETWORK_SIZE-1][neuron] = (output[NETWORK_SIZE-1][neuron] - target[neuron]) 
                    * output_derivative[NETWORK_SIZE-1][neuron];
        }
        for(int layer = NETWORK_SIZE-2; layer > 0; layer--){
            for(int neuron = 0;  neuron < NETWORK_LAYER_SIZE[layer]; neuron++){
                double sum = 0;
                for(int nextNeuron = 0; nextNeuron < NETWORK_LAYER_SIZE[layer+1]; nextNeuron++){
                    sum += weights[layer+1][nextNeuron][neuron] * error_signal[layer+1][nextNeuron];
                }
                this.error_signal[layer][neuron] = sum * output_derivative[layer][neuron];
            }
        }
    }
    /**
     * Function for gradient descent learning rule for updating the weights
     * @param learningRate learning rate
     */
    public void updateWeights(double learningRate){
        for(int layer = 1; layer < NETWORK_SIZE; layer++){
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZE[layer]; neuron++){
                
                double delta = - learningRate *  error_signal[layer][neuron];
                bias[layer][neuron] += delta;
                
                for(int previousNeuron = 0; previousNeuron < NETWORK_LAYER_SIZE[layer-1]; previousNeuron++){
                    //weights[layer][neuron][previousNeuron]
                    weights[layer][neuron][previousNeuron] += delta * output[layer-1][previousNeuron];
                }
            }
        }
    }
    /**
     * Simple sigmoid function
     * @param inputValue
     * @return returns the output of sigmoid
     */
    private double sigmoidFunction(double inputValue){
        //Standart sigmoid formula calculation as a double value
        return 1D /(1 + Math.exp(-inputValue));
    }
}
