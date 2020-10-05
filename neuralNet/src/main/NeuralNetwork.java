package main;
/*
Main driving class
 */

import java.util.Random;

//TODO Import inputs, expected, weights, and biases from file, export each back to same file after learning. Testing will pull from file only
//TODO Differentiate runNetwork into two functions: learning and testing
//TODO Divide inputs and their associative expected into batches. Instead of runs, train network on iterations and epochs. Node[] will now be a Node[][]


public class NeuralNetwork {
    //final static int networkLen = 4;    // 4 layers
    final static int runNum = 1000;
    final static int inputLen = 3;
    final static int h1Len = 4;
    final static int h2Len = 4;
    final static int outputLen = 10;
    static int runCount;            // how many times network has been back propagated
    private FeedForward feedForward;
    private BackPropagation backPropagation;
    private Node[] inputs, hiddenLayer1, hiddenLayer2, outputs;
    private double[][] weights1, weights2, weights3;

    public NeuralNetwork(){
        initWeights(true);
        initNodes();
        takeInputs();
        feedForward = new FeedForward();
        backPropagation = new BackPropagation();
    }

    public void runNetwork(boolean printResults, boolean debug){
        //feed forward
        this.runCount++;
        feedForward.setPrevActivationLayer(inputs);
        hiddenLayer1 = feedForward.generateNextLayer(weights1, hiddenLayer1);
        hiddenLayer2 = feedForward.generateNextLayer(weights2,hiddenLayer2);
        outputs = feedForward.generateNextLayer(weights3, outputs);

        if(debug) System.out.println("Before BackPropagation \n" + this.debugToString());

        //back propagate
        backPropagation.generateOutputError(outputs,takeExpected());
        weights3 = backPropagation.calcNewWeights(weights3, hiddenLayer2);
        backPropagation.generateNextLayerError(hiddenLayer2, weights3);
        weights2 = backPropagation.calcNewWeights(weights2, hiddenLayer1);
        backPropagation.generateNextLayerError(hiddenLayer1, weights2);
        weights1 = backPropagation.calcNewWeights(weights1, inputs);
        if(debug) System.out.println("After BackPropagation \n" + this.debugToString() + "\n");
        if (printResults) System.out.println(this);
        backPropagation.resetTotalErrorString();
    }
    // Initialize node layers
    public void initNodes(){
        inputs = new Node[inputLen];
        hiddenLayer1 = new Node[h1Len];
        hiddenLayer2 = new Node[h2Len];
        outputs = new Node[outputLen];
        for (int i = 0; i < inputs.length; i++){
            inputs[i] = new Node(0, true);
        }
        for (int i = 0; i < hiddenLayer1.length; i++){
            hiddenLayer1[i] = new Node(0, false);
        }
        for (int i = 0; i < hiddenLayer2.length; i++){
            hiddenLayer2[i] = new Node(0, false);
        }
        for (int i = 0; i < outputs.length; i++){
            outputs[i] = new Node(0, false);
        }
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
     inputs[0].setActivation(1);
     inputs[1].setActivation(0);
     inputs[2].setActivation(0.5);
    }

    public double[] takeExpected(){
        double[] expected = {0,0,0,0,0,1,0,0,0,0};
        return expected;
        // or get from file
    }



    public String toString(){
        String message = "Run number: " + runCount + "\n";
        message += "Input nodes: \n";
        message += getLayerStringValue(inputs, 1);
        message += "Output Nodes: \n";
        message += getLayerStringValue(outputs, 4);
        message += "Network Error: " + backPropagation.getTotalError() + "\n";
        return message;
    }

    public String debugToString(){
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
        // booleans in runNetwork are for printing results and printing debugging strings respectivly
        neuralNetwork.runNetwork(true,false);
        for (int i = 0; i < runNum; i++){
            neuralNetwork.runNetwork(false,false);
        }
        neuralNetwork.runNetwork(true,false);

    }



}
