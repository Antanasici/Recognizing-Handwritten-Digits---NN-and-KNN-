/**
 * Main implementation of the KNN algorithm, based results with K = 1, this class
 * holds the main method for KNN, reads in input and test files 
 * (builds the objects of the digits), decides the majority 
 * of neighbours.
 */
package csd3939_coursework2;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import static csd3939_coursework2.CSD3939_CourseWork2.K_VALUE;
import static csd3939_coursework2.CSD3939_CourseWork2.TEST_FILE_PATH;
import static csd3939_coursework2.CSD3939_CourseWork2.TRAINING_FILE_PATH;
import static csd3939_coursework2.CSD3939_CourseWork2.NERD_PRINTS;
/**
 * A machine learning system to categorise one of the UCI digit 
 * tasks at Middlesex University London, Computer Science faculty
 * @author Antanas Icikovic M00537517
 * @date 14th of February 2019
 */
public class KNN {
    
    /**
     * Main method for the KNN algorithm, reads in input file and calculates the 
     * euclidian distance between test and train points, calls the majority counter 
     * and prints out the results
     * @throws FileNotFoundException 
     */
    public void run() throws FileNotFoundException{
        List<KNN_Digit_Class> inputList = new ArrayList<KNN_Digit_Class>();
        List<KNN_Digit_Class> testList = readTestData();
        
        //Read the training file and build a list of object
        try{
        Scanner scanner = new Scanner(new File(TRAINING_FILE_PATH));
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            
            //Find the index of the last comma
            int lastCommaIndex = line.lastIndexOf(',');
            //Pull the label of the digit (last index)
            int label = Integer.parseInt(line.substring(lastCommaIndex+1, lastCommaIndex +2));
            //Pull  the rest of the line and split it by the comma
            String newLine = line.substring(0,lastCommaIndex);
            String[] splitLine = newLine.split(",");

            //Converts all the input strings into integers
            ArrayList<Integer> valueList = new ArrayList<Integer>();
            for(String i : splitLine){
                valueList.add(Integer.parseInt(i));
            }
            
            // Creates a digit object and saves it in the input list
            inputList.add(new KNN_Digit_Class(valueList, label));
        }
        scanner.close();
        }catch(FileNotFoundException ex){
            System.out.println("Error reading test file!");
        }
        //Build a result object with euclidean distance and save it in the output list
        int wellGuessed = 0; //Correct guess counter
        int totalInputs = inputList.size();
        for(KNN_Digit_Class testDigit : testList){
            List<KNN_Digit_Result> outputList = new ArrayList<KNN_Digit_Result>(); // Output list for one test iteration
            for(int value = 0; value < testDigit.digitGrayScaleValue.length; value++){ 
                for(KNN_Digit_Class digit : inputList){
                    double dist = 0.0;
                    //Get the distance of training and test digit
                    for(int j = 0; j < digit.digitGrayScaleValue.length; j++){
                        dist += Math.pow(digit.digitGrayScaleValue[j] - testDigit.digitGrayScaleValue[j], 2);
                    }
                    double distance = Math.sqrt(dist);
                    //Save it to single test digit output list
                    outputList.add(new KNN_Digit_Result(distance, digit.digitLabel));
                }
            }
            //Sort the outputList
            Collections.sort(outputList, new Utility.DistanceComparator());
            String[] allNeigbours = new String[K_VALUE];
            //Get digits of the k nearest neigbours from the distance outputlist into an array
            for(int x = 0; x < K_VALUE; x++){
                allNeigbours[x] = outputList.get(x).digitLabel;
            }
            String majClass = findMajorityCounter(allNeigbours);
            if(Integer.parseInt(testDigit.digitLabel) == Integer.parseInt(majClass)){
                if(NERD_PRINTS){
                    System.out.println("Original " + testDigit.digitLabel + " Guess: " + majClass  + " Good guess");
                }
                wellGuessed++;
            }else{
                if(NERD_PRINTS){
                    System.out.println("Original " + testDigit.digitLabel + " Guess: " + majClass  + " Bad guess");
                }
            }
        }
            //Returns final results (accuracy and etc)
           Utility.printFinalResults(wellGuessed, totalInputs);
    }
    
    /**
     * Gets the majority of occurrences in the output list, 
     * decides what is the biggest amount of neighbours closest to the tested digit
     * @param array array of neighbours
     * @return a string of a digit (guess)
     */
    private static String findMajorityCounter(String[] array)
	{
	//Add the String array to a HashSet to get unique String values
	Set<String> h = new HashSet<String>(Arrays.asList(array));
	//Convert the HashSet back to array
	String[] uniqueValues = h.toArray(new String[0]);
        
	//Counts for unique strings
	int[] counts = new int[uniqueValues.length];
	//Loop through unique strings and count how many times they appear in origianl array   
	for (int i = 0; i < uniqueValues.length; i++) {
            for (int j = 0; j < array.length; j++) {
		if(array[j].equals(uniqueValues[i])){
                    counts[i]++;
		}
            }        
	}

	int max = counts[0];
	for (int counter = 1; counter < counts.length; counter++) {
            if (counts[counter] > max) {
		max = counts[counter];
            }
	}
        if(NERD_PRINTS){
            System.out.println("Times in the output list: " +max);
        }

	/*How many times max appears
	 *we know that max will appear at least once in counts
	 *so the value of freq will be 1 at minimum after this loop*/
	int freq = 0;
	for (int counter = 0; counter < counts.length; counter++) {
            if (counts[counter] == max) {
		freq++;
            }
        }

	//Index of most freq value if we have only one mode
	int index = -1;
	if(freq==1){
            for (int counter = 0; counter < counts.length; counter++) {
		if (counts[counter] == max) {
                    index = counter;
                    break;
                }
            }
            return uniqueValues[index];
	}else{//Multiple modes
            int[] ix = new int[freq];//Array of indexis of modes
            int ixi = 0;
            for (int counter = 0; counter < counts.length; counter++) {
                if (counts[counter] == max) {
                    ix[ixi] = counter;//Save index of each max count value
                    ixi++; //Increase index of ix array
                }
            }

            for (int counter = 0; counter < ix.length; counter++)         
                System.out.println("class index: "+ix[counter]);       

            //Now choose one at random
            Random generator = new Random();        
            //Get random number 0 <= rIndex < size of ix
            int rIndex = generator.nextInt(ix.length);
            System.out.println("random index: "+rIndex);
            int nIndex = ix[rIndex];
            //Return unique value at that index 
            return uniqueValues[nIndex];
	}
    }
    
    /**
     * Reads in the test file 
     * @return returns list of test digit objects
     * @throws FileNotFoundException 
     */
    public List<KNN_Digit_Class> readTestData() throws FileNotFoundException{
        List<KNN_Digit_Class> returnList = new ArrayList<KNN_Digit_Class>(); //Return the test dataset
        try{
            Scanner scanner = new Scanner(new File(TEST_FILE_PATH));
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();

                //Find the index of the last comma
                int lastCommaIndex = line.lastIndexOf(',');
                //Pull the label of the digit (last index)
                int label = Integer.parseInt(line.substring(lastCommaIndex+1, lastCommaIndex +2));
                //Pull  the rest of the line and split it by the comma
                String newLine = line.substring(0,lastCommaIndex);
                String[] splitLine = newLine.split(",");

                //Converts all the input strings into integers
                ArrayList<Integer> valueList = new ArrayList<Integer>();
                for(String i : splitLine){
                    valueList.add(Integer.parseInt(i));
                }

                // Creates a digit object and saves it in the input list
                returnList.add(new KNN_Digit_Class(valueList, label));
            }
            scanner.close();
        }catch(FileNotFoundException ex){
            System.out.println("Error reading test file!");
        }
        return returnList;
    }
}
