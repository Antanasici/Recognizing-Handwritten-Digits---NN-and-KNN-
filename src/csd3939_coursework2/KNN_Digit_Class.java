/**
 * This class represents an object of one digit
 */
package csd3939_coursework2;
import java.util.ArrayList;
/**
 * @author Antanas
 * @date 14th of February 2019
 */
public class KNN_Digit_Class {
        Integer[] digitGrayScaleValue; //Array of integers that define the grayscale of the image
        String digitLabel;          //The digit that is repfresented by the class
        
        //Contructor for the object
        public KNN_Digit_Class(ArrayList<Integer> input, int label){
            this.digitLabel = Integer.toString(label);
            this.digitGrayScaleValue = input.toArray(new Integer[input.size()]);
        }
    }
