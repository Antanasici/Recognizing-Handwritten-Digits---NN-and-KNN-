/**
 * Simple class that creates a result object based on the distance to a class
 */
package csd3939_coursework2;
/**
 * @author Antanas
 * @date 14th of February 2019
 */
public class KNN_Digit_Result {
        double distance; //Result distance
        String digitLabel; //Digit label
        
        public KNN_Digit_Result(double distance, String label){
            this.digitLabel = label;
            this.distance = distance;
        }
    }
