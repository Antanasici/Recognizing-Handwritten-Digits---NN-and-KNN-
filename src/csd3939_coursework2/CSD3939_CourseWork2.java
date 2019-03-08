/*
 * This is the main class that has all the options and settings, also runs other 
 * to solve the digit task
 */
package csd3939_coursework2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Antanas
 * @date 5th of March 2019
 */
public class CSD3939_CourseWork2 {
   
    //Default settings
    public static final String TRAINING_FILE_PATH = "C:\\\\Users\\\\Antanas\\\\Desktop\\\\cw2DataSet2.csv"; 
    public static final String TEST_FILE_PATH = "C:\\\\Users\\\\Antanas\\\\Desktop\\\\cw2DataSet1.csv"; 
    public final static boolean NERD_PRINTS = true; //If set to true will print the guess and original labels
    
    //K-nearest neigbours  settings
    public final static boolean USE_KNN_ALGORITHM = true; //If set to true, app will run KNN algorithm
    public final static int K_VALUE = 1;   //Value for the amount of the nambers to check for (Best results with K = 1)
    
    //Neural Network settings
    public final static boolean USE_NEURAL_NETWORK = false; //If set to true, app will run NEURAL NETWORK
    public static final double LEARNING_RATE = 0.1;
    public final static double BIAS_RANGE_SMALLEST = -0.5;
    public final static double BIAS_RANGE_BIGGEST = 0.7;
    public final static double WEIGHTS_RANGE_SMALLEST = -1;
    public final static double WEIGHTS_RANGE_BIGGEST = 1;
    final static int TRAINING_EPOCHS_VALUE = 500;
    final static int TRAINING_LOOPS_VALUE = 500;
    final static int TRAINING_BATCH_SIZE = 32;
    final static int FIRST_HIDDEN_LAYER_NODE_AMOUNT = 26;
    final static int SECOND_HIDDEN_LAYER_NODE_AMOUNT = 15;
    final static int INPUT_LAYER_NODE_AMOUNT = 64; //Because the image is 64pixels 8*8
    
    /**
     * Main method for the application
     * @param args 
     */
    public static void main(String[] args) {
        if(USE_KNN_ALGORITHM){
            System.out.println("Starting KNN algorithm...");
            KNN knn = new KNN();
            try{
                knn.run();
            }catch(FileNotFoundException ex){
                System.out.println("Error when reading KNN test or train file!");
            }
        }
        if(USE_NEURAL_NETWORK){
            System.out.println("Starting neural network...");
            //Build a network with 64 nodes in input layer, 26 in 1st hidden layer, 15 in 2nd hidden layer and 10 output
            NetworkBase network = new NetworkBase(new int[]{INPUT_LAYER_NODE_AMOUNT, FIRST_HIDDEN_LAYER_NODE_AMOUNT, SECOND_HIDDEN_LAYER_NODE_AMOUNT, 10});
            try{
                TrainingSet set = createTrainingSet();
                trainData(network, set , TRAINING_EPOCHS_VALUE, TRAINING_LOOPS_VALUE, TRAINING_BATCH_SIZE);
                TrainingSet testSet = createTestingSet();
                testTrainSet(network, testSet);
            }catch(FileNotFoundException ex){
                System.out.println("Error when reading NN test or train file!");
            }
        }
    }
    
    /**
     * This method tests the network, also keeps tracks of the the guessed results and provided
     * the final output.
     * @param net - NetworkBase object (NN implementation)
     * @param set - testing set object (Till uses TrainingSet class)
     */
    public static void testTrainSet(NetworkBase net, TrainingSet set){
        int correct = 0;
        for(int i = 0; i < set.size(); i++){
            double highest = Utility.returnIndexOfHighestValue(net.calculationFunction(set.getInput(i)));
            double actualHighest = Utility.returnIndexOfHighestValue(set.getOutput(i));
            if(NERD_PRINTS){
                System.out.println("Guess : " + highest + " Real thing : " + actualHighest);
            }
            if(highest == actualHighest){
                correct ++;
            }
        }
        Utility.printFinalResults(correct, set.size());
    }

    /**
     * Method that reads a training file, build a TraingSet object from the inputs
     * @return the newly created training set
     * @throws FileNotFoundException 
     */
    public static TrainingSet createTrainingSet() throws FileNotFoundException{
        TrainingSet set = new TrainingSet(INPUT_LAYER_NODE_AMOUNT, 10);
        Scanner scanner = new Scanner(new File(TRAINING_FILE_PATH));
        //Do while there is new line.
        while(scanner.hasNextLine()){
            //Read in and split the line
            String line = scanner.nextLine();
            int lastCommaIndex = line.lastIndexOf(',');
            int label = Integer.parseInt(line.substring(lastCommaIndex+1, lastCommaIndex +2));
            String newLine = line.substring(0,lastCommaIndex);
            String[] splitLine = newLine.split(",");
            
            //Build an array to add to a set
            double[] splitLineNumber = new double[splitLine.length];
            for(int i = 0; i<splitLine.length; i++){
                splitLineNumber[i] = Double.parseDouble(splitLine[i]);
            }
            double[] output = new double[10];
            output[label] = 1d;
            set.addData(splitLineNumber, output); 
        }
        scanner.close();
        return set;
    }
    /**
     * Method that builds a testing set and returns a TrainingSet object that
     * that is used for testing
     * @return TrainingSet object
     * @throws FileNotFoundException 
     */
    public static TrainingSet createTestingSet() throws FileNotFoundException{
        TrainingSet set = new TrainingSet(INPUT_LAYER_NODE_AMOUNT, 10);
        Scanner scanner = new Scanner(new File(TEST_FILE_PATH));
        //Do while there is new line.
        while(scanner.hasNextLine()){
            //Read in and split the line
            String line = scanner.nextLine();
            int lastCommaIndex = line.lastIndexOf(',');
            int label = Integer.parseInt(line.substring(lastCommaIndex+1, lastCommaIndex +2));
            String newLine = line.substring(0,lastCommaIndex);
            String[] splitLine = newLine.split(",");
            //Build an array to add to a set
            double[] splitLineNumber = new double[splitLine.length];
            for(int i = 0; i<splitLine.length; i++){
                splitLineNumber[i] = Double.parseDouble(splitLine[i]);
            }
            double[] output = new double[10];
            output[label] = 1d;
            set.addData(splitLineNumber, output); 
        }
        scanner.close();
        return set;
    }
    /**
     * This is a method for training the net for a specified amount of epochs.
     * @param net - NetworkBase object
     * @param set - The training set object
     * @param epochs - amount of epochs for the training
     * @param loops  - amount of loops for the training within one epoch
     * @param batch_size  - batch size, only use with bigger data sets ( should improve accuracy )
     */
    public static void trainData(NetworkBase net, TrainingSet set, int epochs, int loops, int batch_size){
        for(int epoch= 0; epoch < epochs; epoch++){
            net.train(set, loops, batch_size);
            System.out.println("Training neural network...");
            if(NERD_PRINTS){
                System.out.println("Epochs : " + epoch + "/" + epochs);
            }
        }
    }
}
