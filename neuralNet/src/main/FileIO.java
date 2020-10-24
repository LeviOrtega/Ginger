package main;
/*
Manages input and output of weights, biases, test data, and training data
 */


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileIO {

    private double[] doubleLabels;
    private double[][] doubleImages;
    private int[] labels;
    private List<int[][]> images;



    public FileIO(){}

    public double[][] readWeights(String fileName){
        double[][] weights;
        // I use arrayList here so that I dont have to check to see how long the weights row is
        ArrayList<String[]> lines = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(fileName);
            Scanner fileScanner = new Scanner(fileReader);
            while (fileScanner.hasNextLine()){
                String[] line = fileScanner.nextLine().split(",");
                lines.add(line);
            }
            weights = new double[lines.size()][lines.get(0).length];
            for (int i = 0; i < lines.size(); i++){
                for (int j = 0; j < lines.get(0).length; j++){
                    weights[i][j] = Double.parseDouble(lines.get(i)[j]);
                }
            }
            return weights;
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not read from file: " + fileName);
        }


        return null;
    }

    public void writeWeights(double[][] weights, String fileName){
        try {
            FileWriter fileWriter = new FileWriter(fileName, false);
            for (int i = 0; i < weights.length; i++) {
                for (int j = 0; j < weights[0].length; j++) {

                    fileWriter.write(weights[i][j] + ",");
                }
                fileWriter.append("\n");
            }
            fileWriter.flush();
        }
        catch (IOException e){
            System.out.println("Could not write to file: " + fileName);
        }
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
           System.out.println("Could read from file: " + fileName);
        }
        return null;
    }

    public void writeBiases(double[] biases, String fileName){
        try {
           FileWriter fileWriter = new FileWriter(fileName, false);
           for (int i = 0; i < biases.length; i++){
               fileWriter.write(biases[i] + ",");
           }
           fileWriter.write("\n");
           fileWriter.flush();
       }
       catch (IOException e){
           System.out.println("Could not write to file: " + fileName);
       }

    }


    public double[][] getDataBatch(int index, int batchSize){
        double[][] batchInputs = new double[batchSize][doubleImages[0].length];
        //System.out.println("Double" + Arrays.toString(doubleImages[index]));
        int t = 0;
        for (int i = index; i < index + batchSize ; i ++){
                batchInputs[t] = doubleImages[i];
            t++;
        }
        //System.out.println("Batch" + Arrays.toString(batchInputs[0]));
        return batchInputs;
    }

    public int[] getLabelsBatch(int index, int batchSize){
        int[] batchInputs = new int[batchSize];
        int t = 0;
        for (int i = index; i < index + batchSize; i++){
            batchInputs[t] = labels[i];
            t++;
        }
        return batchInputs;
    }



    public void getDataFromFiles(String labelFile, String imageFile){
        labels = MnistReader.getLabels(labelFile);
        images = MnistReader.getImages(imageFile);

        // want to convert

        doubleImages = new double[images.size()][images.get(0).length * images.get(0)[0].length];
        for (int i = 0; i < images.size(); i ++){
            // dii index is used for taking all rows and cols in images and squishing them to better fit input array
            int dii = 0;        // doubleImagesIndex
            for (int r = 0; r < images.get(0).length; r++){
                for (int c = 0; c < images.get(0)[0].length; c++){
                    doubleImages[i][dii] = images.get(i)[r][c];
                    dii++;
                }
            }
        }
    }

}
