package main;
/*
Manages input and output of weights, biases, test data, and training data
 */


import java.util.InputMismatchException;
import java.util.Scanner;

public class FileIO {

    public static double[][] trainingData, testingData;
    public static String[] trainingLabels, testingLabels;
    private Scanner sc;


    public FileIO(){
        sc = new Scanner(System.in);
    }

    public double[][] readWeights(){

        return null;
    }

    public double[] readBiases(){
        return null;
    }

    public void writeWeights(double[][] weights){

    }

    public void writeBiases(double[] biases){

    }

    public int getRunNumber(){

        while (true) {
            System.out.println("Enter how many times to run network: ");
            try {
                    int input = Integer.parseInt(sc.next());
                    if (input > 0) {
                        return input;
                    }

            } catch (NumberFormatException e) {
                System.out.println("Please enter an integer greater than zero.");
            }
        }
    }

    public int getBiasLength(){

        while (true) {
            System.out.println("Enter batch size: ");
            try {
                int input = Integer.parseInt(sc.next());
                if (input > 0) {
                    return input;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter an integer greater than zero.");
            }
        }
    }

    public NetworkStatus getNetworkInfo(){
        // return 1 for run, 0 for learn
        while (true) {
            System.out.println("Enter: Network learn (0) or Network Run (1)");
            try {
                int input = Integer.parseInt(sc.next());
                if (input == 0) {
                    return NetworkStatus.LEARN;
                }
                else if (input == 1){
                    return NetworkStatus.RUN;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a correct format");
            }
        }
    }

    public boolean isSatisfied(){
        while (true){
            System.out.println("Is this what you want? (Y/N)");
            try {
                String answer = sc.next().trim().toLowerCase();
                if (answer.equals("y")){
                    return true;
                }
                else if (answer.equals("n")){
                    return false;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a correct format");
            }
        }
    }

    public void parseTraining(){
        //TODO parse pictures into double arrays of pixel values and a string array of labels
    }

    public void parseTesting(){
        //TODO parse pictures into double arrays of pixel values and a string array of labels
    }

    public double[][] getTrainingDataBatch(int index, int batchSize){
         return null;
    }

    public String[] getTrainingLabelsBatch(int index, int batchSize){
        return null;
    }

    public double[][] getTestingDataBatch(int index, int batchSize){
        return null;
    }

    public String[] getTestingLabelsBatch(int index, int batchSize){
        return null;
    }

}
