/*
 * Class that contains utility methods for the application
 */
package csd3939_coursework2;
import java.util.Comparator;
/**
 * @author Antanas
 * @date 28 th of February 2019
 */
public class Utility {
    
    /**
     * Method that creates an array of specified size and fills it with initial values
     * @param range size of an array
     * @param initialValue initial value of array values
     * @return returns a new array
     */
    /*public static double[] buildArray(int range, double initialValue){
        //If requested to generate an empty array return null
        if(range < 1){
            return null;
        }
        //Create an array of specified range
        double[] returnArray = new double[range];
        //Iterate through the array an fill it with initial values.
        for(int index = 0; index < range; index++){
            returnArray[index] = initialValue;
        }
        return returnArray;
    }*/
    
    /**
     * Method that creates an array of specified size and 
     *  fills it with values of specified bounds
     * @param range size of an array
     * @param smallest lower bound
     * @param biggest higher bound
     * @return  random array
     */
    public static double[] buildRandomArray(int range, double smallest, double biggest){
        //If requested to generate an empty array return null
        if(range < 1){
            return null;
        }
        //Create an array of specified range
        double[] returnArray = new double[range];
        //Iterate through the array an fill it with random value withing
        //the specified range
        for(int index = 0; index < range; index++){
            returnArray[index] = generateRandomValue(smallest, biggest);
        }
        return returnArray;
    }
    
    /**
     * Method that creates a 2 dimensional array of specified size and 
     *  fills it with values of specified bounds
     * @param rangeX 1st dimension size
     * @param rangeY 2nd dimension size
     * @param smallest lower bound
     * @param biggest higher bound
     * @return 2 dimension random array
     */
    public static double[][] buildRandomArray(int rangeX, int rangeY, double smallest, double biggest){
        //If requested to generate an empty array return null
        if(rangeX < 1 || rangeY < 1){
            return null;
        }
        //Create an array of specified range
        double[][] returnArray = new double[rangeX][rangeY];
        //Iterate through the array of specified size, and build a random array
        // of specified size
        for(int index = 0; index < rangeX; index++){
            returnArray[index] = buildRandomArray(rangeY,smallest, biggest);
        }
        return returnArray;
    }
    /**
     * Method that creates a random value of specified bounds
     * will be used when generating a random array
     * @param smallest lowest bound
     * @param biggest highest bound
     * @return  random number
     */
    public static double generateRandomValue(double smallest, double biggest){
        return Math.random()*(biggest - smallest) + smallest;
    }
    /**
     * Generates an array of integers of specified bounds and size, but also limits
     * the values to once in array
     * @param smallest lower bound
     * @param biggest higher bound
     * @param size size of random array 
     * @return array of random values
     */
    public static Integer[] randomValues(int smallest, int biggest, int size){
        smallest --;
        
        if(size > (biggest - smallest)){
            return null;
        }
        
        Integer[] values = new Integer[size];
        for(int index = 0; index < size; index++){
            int number = (int) (Math.random() * (biggest - smallest + 1) + smallest);
            while(containsValue(values, number)){
                number = (int)(Math.random() * (biggest - smallest + 1) + smallest);
            }
            values[index] = number;
        }
        return values;
    }
    
    /**
     * Checks if array contains a value comparable extension
     * @param <T> object
     * @param array array of comparison
     * @param value value to compare
     * @return result 
     */
   public static <T extends Comparable<T>> boolean containsValue(T[] array, T value){
       for(int index = 0; index < array.length; index++){
           if(array[index] != null){
               if(value.compareTo(array[index]) == 0 ){
                   //If contains return true
                   return true;
               }
           }
       }
       //If doesnt contain return false
       return false;
   }
   
   /**
    * Return the index of the highest value in the provided array
    * @param input array to get the highest value index
    * @return index of the highest value
    */
   public static int returnIndexOfHighestValue(double[] input){
       int returnIndex = 0;
       for(int iterationIndex = 1; iterationIndex < input.length; iterationIndex++){
           if(input[iterationIndex] > input[returnIndex]){
               returnIndex = iterationIndex;
           }
       }
       return returnIndex;
   }
   

    /**
     * Compare class, used to compare distances of the results
     */
    static class DistanceComparator implements Comparator<KNN_Digit_Result>{
        @Override
        public int compare(KNN_Digit_Result a, KNN_Digit_Result b){
            return a.distance < b.distance ? -1 : a.distance == b.distance ? 0 : 1;
        }
    }
    /**
     * Prints out the accuracy of an algorithm
     * @param goodResults total of correct guessed results
     * @param totalInputs total inputs number
     */
    public static void printFinalResults(int goodResults, int totalInputs){
        System.out.println(goodResults + "/" + totalInputs);
        System.out.println("Accuracy: " + (double)((double)goodResults * 100 / (double)totalInputs) + "% ");
    }
}
