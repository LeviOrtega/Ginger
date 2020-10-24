package main;
/*
Manages input and output of weights, biases, test data, and training data
 */


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class FileIO {

    public static double[][] trainingData, testingData;
    public static String[] trainingLabels, testingLabels;
    private Scanner inputScanner;
    private FileReader fileReader;



    public FileIO(){
        inputScanner = new Scanner(System.in);
    }

    public double[][] readWeights(String fileName){

        return null;
    }

    public double[] readBiases(String fileName){
        double[] biases;
        String[] line;
        try {
            FileReader fileReader = new FileReader(fileName);
            Scanner fileScanner = new Scanner(fileReader);
            if (fileScanner.hasNextLine()) {
                line = fileScanner.nextLine().split(",");
                biases = new double[line.length];
                for (int i = 0; i < biases.length; i++){
                    biases[i] = Double.parseDouble(line[i]);
                }
                return biases;
            }
        }
        catch (FileNotFoundException e) {
           System.out.println("Could not find file: " + fileName);
        }
        return null;
    }

    public void writeWeights(double[][] weights, String fileName){

    }

    public void writeBiases(double[] biases, String fileName){
        try {
           FileWriter fileWriter = new FileWriter(fileName);
           for (int i = 0; i < biases.length; i++){
               fileWriter.append(biases[i] + ",");
           }
           fileWriter.append("\n");
           fileWriter.flush();
           fileWriter.close();
       }
       catch (IOException e){
           System.out.println("Cannot open file: " + fileName);
       }

    }

    public int getRunNumber(){

        while (true) {
            System.out.println("Enter how many times to run network: ");
            try {
                    int input = Integer.parseInt(inputScanner.next());
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
                int input = Integer.parseInt(inputScanner.next());
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
                int input = Integer.parseInt(inputScanner.next());
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
                String answer = inputScanner.next().trim().toLowerCase();
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
