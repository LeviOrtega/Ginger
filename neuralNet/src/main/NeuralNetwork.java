package main;


import java.util.Random;

public class NeuralNetwork {
    final static int inputLen = 3;
    final static int h1Len = 4;
    final static int h2Len = 3;
    final static int outputLen = 2;
    private Node[] inputs;
    private Node[] hiddenLayer1;
    private Node[] hiddenLayer2;
    private Node[] outputs;
    private double[][] weights1, weights2, weights3;

    public NeuralNetwork(){
        takeInputs();
        initWeights(true);
    }

    public void initWeights(boolean isNewNetwork){
        weights1 = new double[4][3];
        weights2 = new double[3][4];
        weights3 = new double[2][3];

        if(isNewNetwork) {
            weights1 = giveRandom(weights1);
            weights2 = giveRandom(weights2);
            weights3 = giveRandom(weights3);
        }

        // else... read from a file
    }

    public double[][] giveRandom(double[][] weights){       // initialize weights with random value if a new neural network
        Random rand = new Random();
        for (int i = 0; i < weights.length; i++){
            for (int j = 0; j < weights[0].length; j++){
                weights[i][j] = rand.nextDouble();
            }
        }
        return weights;
    }

    public void takeInputs(){
    // for now we will give custom inputs
     inputs = new Node[]{new Node(1, true), new Node(2, true), new Node(3, true)};
    }


    public String toString(){
        String message = "Input nodes: \n";
        for (Node n: inputs){
            message += n + "\n";
        }

        message += "Weights: \n";
        message += getWeightStringValue(weights1, 1);
        message += getWeightStringValue(weights2, 2);
        message += getWeightStringValue(weights3, 3);

        return message;
    }

    public String getWeightStringValue(double[][] weights, int num){
        String result = "Weight Matrix " + num + "\n";
        for (int i = 0; i < weights.length; i++){
            for (int j = 0; j < weights[0].length; j++){
                result += "["+ i + "]" + "["+ j + "] " + weights[i][j] + " ";
            }
            result += "\n";
        }
        return result;
    }


    public static void main(String[] args){
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        System.out.println(neuralNetwork);
    }



}
