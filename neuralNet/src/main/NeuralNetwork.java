package main;
/*
Main driving class
 */

import java.util.Random;

public class NeuralNetwork {
    //final static int networkLen = 4;    // 4 layers
    final static int runNum = 500;
    final static int inputLen = 3;
    final static int h1Len = 4;
    final static int h2Len = 3;
    final static int outputLen = 2;
    private FeedForward feedForward;
    private BackPropagation backPropagation;
    private Node[] inputs, hiddenLayer1, hiddenLayer2, outputs;
    private double[][] weights1, weights2, weights3;

    public NeuralNetwork(){
        takeInputs();
        initWeights(true);
        feedForward = new FeedForward();
        backPropagation = new BackPropagation();
    }

    public void runNetwork(boolean printResults){
        //feed forward
        feedForward.setPrevActivationLayer(inputs);
        hiddenLayer1 = feedForward.generateNextLayer(weights1);
        hiddenLayer2 = feedForward.generateNextLayer(weights2);
        outputs = feedForward.generateNextLayer(weights3);

        if(printResults) System.out.println("Before BackPropagation \n" + this);

        //back propagate
        backPropagation.generateOutputError(outputs,getExpected());
        weights3 = backPropagation.calcNewWeights(weights3, hiddenLayer2);
        backPropagation.generateNextLayerError(hiddenLayer2, weights3);
        weights2 = backPropagation.calcNewWeights(weights2, hiddenLayer1);
        backPropagation.generateNextLayerError(hiddenLayer1, weights2);
        weights2 = backPropagation.calcNewWeights(weights1, inputs);
        if(printResults) System.out.println("After BackPropagation \n" + this + "\n" + "Network Error: " + backPropagation.getTotalError());
        backPropagation.resetTotalErrorString();
    }


    public void initWeights(boolean isNewNetwork){
        weights1 = new double[h1Len][inputLen];
        weights2 = new double[h2Len][h1Len];
        weights3 = new double[outputLen][h2Len];

        if(isNewNetwork) {
            weights1 = giveRandom(weights1);
            weights2 = giveRandom(weights2);
            weights3 = giveRandom(weights3);
        }

        // else... read from a file
    }

    public double[][] giveRandom(double[][] weights){       // initialize weights with random value if a new neural network
        Random rand = new Random(1);
        for (int i = 0; i < weights.length; i++){
            for (int j = 0; j < weights[0].length; j++){
                weights[i][j] = rand.nextDouble();
            }
        }
        return weights;
    }

    public void takeInputs(){
    // for now we will give custom inputs
     inputs = new Node[]{
             new Node(1, true),
             new Node(0.5, true),
             new Node(0, true)
     };
    }

    public double[] getExpected(){
        double[] expected = {1,0};
        return expected;
        // or get from file
    }



    public String toString(){

        String message = "Nodes: \n";
        message += getLayerStringValue(inputs, 1);
        message += getLayerStringValue(hiddenLayer1, 2);
        message += getLayerStringValue(hiddenLayer2, 3);
        message += getLayerStringValue(outputs, 4);
        message += "\nWeights: \n";
        message += getWeightStringValue(weights1, 1);
        message += getWeightStringValue(weights2, 2);
        message += getWeightStringValue(weights3, 3);
        return message;
    }

    public String getLayerStringValue(Node[] node, int num){
        String result = "Layer " + num + "\n";
        for (Node n: node){
            result += n + "\n";
        }
        return result;
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
        neuralNetwork.runNetwork(true);
        for (int i = 0; i < runNum; i++){
            neuralNetwork.runNetwork(false);
        }
        neuralNetwork.runNetwork(true);

    }



}
