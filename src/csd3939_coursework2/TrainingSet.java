/*
 * Class that holds sets for training and testing, includes methods for control of a set
 */
package csd3939_coursework2;

import java.util.ArrayList;

/**
 * @author Antanas
 * @date 2nd of March 2019
 */
public class TrainingSet {
    public final int INPUT_SIZE;
    public final int OUTPUT_SIZE;

    //double[][] <- index1: 0 = input, 1 = output || index2: index of element
    private ArrayList<double[][]> data = new ArrayList<>();

    public TrainingSet(int INPUT_SIZE, int OUTPUT_SIZE) {
        this.INPUT_SIZE = INPUT_SIZE;
        this.OUTPUT_SIZE = OUTPUT_SIZE;
    }

    /**
     * Adds arrays to the object
     * @param in grayscale input
     * @param expected  expected label
     */
    public void addData(double[] in, double[] expected) {
        if(in.length != INPUT_SIZE || expected.length != OUTPUT_SIZE) return;
        data.add(new double[][]{in, expected});
    }

    /**
     * Pulls a batch of specified size (used to improve accuracy 
     * with bigger sets while training on a batch)
     * @param size size of the batch
     * @return returns a set of specified batch
     */
    public TrainingSet extractBatch(int size) {
        if(size > 0 && size <= this.size()) {
            TrainingSet set = new TrainingSet(INPUT_SIZE, OUTPUT_SIZE);
            Integer[] ids = Utility.randomValues(0,this.size() - 1, size);
            for(Integer i:ids) {
                set.addData(this.getInput(i),this.getOutput(i));
            }
            return set;
        }else return this;
    }
    
    /*public String toString() {
        String s = "TrainSet ["+INPUT_SIZE+ " ; "+OUTPUT_SIZE+"]\n";
        int index = 0;
        for(double[][] r:data) {
            s += index +":   "+Arrays.toString(r[0]) +"  >-||-<  "+Arrays.toString(r[1]) +"\n";
            index++;
        }
        return s;
    }*/

    /**
     * Returns the size of the set
     * @return set size
     */
    public int size() {
        return data.size();
    }

    /**
     * Returns input of specified index
     * @param index index to return
     * @return returns the input
     */
    public double[] getInput(int index) {
        if(index >= 0 && index < size())
            return data.get(index)[0];
        else return null;
    }

    /**
     * Returns output of specified index
     * @param index index to return
     * @return returns the specified output
     */
    public double[] getOutput(int index) {
        if(index >= 0 && index < size())
            return data.get(index)[1];
        else return null;
    }

    /**
     * Returns the size of input array
     * @return size of input array
     */
    public int getINPUT_SIZE() {
        return INPUT_SIZE;
    }

    /**
     * Returns the size of output array
     * @return size of output array
     */
    public int getOUTPUT_SIZE() {
        return OUTPUT_SIZE;
    }
}
