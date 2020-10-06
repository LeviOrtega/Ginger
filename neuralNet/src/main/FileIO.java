package main;
/*
Manages input and output of weights, biases, test data, and training data
 */


public class FileIO {

    public static double[][] trainingData, testingData;
    public static String[] trainingLabels, testingLabels;



    public FileIO(){

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
