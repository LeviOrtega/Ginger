package main;
/*
Main driving class
 */

import java.util.Random;

//TODO Import inputs, expected, weights, and biases from file, export each back to same file after learning. Testing will pull from file only
//TODO Differentiate runNetwork into two functions: learning and testing


public class NeuralNetwork {
    final static int batchSize = 5;
    final static int runNum = 10000;
    final static int inputLen = 3;
    final static int h1Len = 10;
    final static int h2Len = 10;
    final static int outputLen = 10;
    final static int randSeed = 1;
    static int runCount;                // how many times network has been run through
    private FeedForward feedForward;
    private BackPropagation backPropagation;
    private Node[][] inputs, hiddenLayer1, hiddenLayer2, outputs;
    private double[][][] weights1, weights2, weights3;
    private double[][] bias1, bias2, bias3;
    private String[] networkErrors;     // used for displaying the errors of each feed forward


    public NeuralNetwork(){
        initWeights();
        takeWeights(true); // pass in true if you want to give weights rand value, else read from file
        initBiases();
        takeBiases(true);
        initNodes();
        takeInputs();

        feedForward = new FeedForward();
        backPropagation = new BackPropagation();
        networkErrors = new String[outputLen];
    }

    public void networkLearn(boolean printResults, boolean debug){
        //feed forward
        batchFeedForward();
        if(debug) System.out.println("Before BackPropagation \n" + this.debugToString());

        //back propagate
        batchBackPropagation();
        if (debug) System.out.println("After BackPropagation \n" + this.debugToString() + "\n");
        if (printResults) System.out.println(this);
    }

    public void batchFeedForward() {
        for (int b = 0; b < batchSize; b++) {
            //runCount++;       // if you want to show how many ff total
            feedForward.setPrevActivationLayer(inputs[b]);
            hiddenLayer1[b] = feedForward.generateNextLayer(weights1[b], hiddenLayer1[b]);
            hiddenLayer2[b] = feedForward.generateNextLayer(weights2[b], hiddenLayer2[b]);
            outputs[b] = feedForward.generateNextLayer(weights3[b], outputs[b]);
        }
        runCount++;
    }

    public void batchBackPropagation() {
        for (int b = 0; b < batchSize; b++) {
            backPropagation.generateOutputError(outputs[b], takeExpected()[b]);
            networkErrors[b] = backPropagation.getTotalError();
            backPropagation.calcNewBiases(bias3[b]);
            weights3[b] = backPropagation.calcNewWeights(weights3[b], hiddenLayer2[b]);
            // does not use backPropBatchLoop because this loop is for generating the first output error
        }
        backPropagation.averageBatchWeights(weights3);
        backPropagation.averageBatchBiases(bias3, outputs);

        backPropBatchLoop(weights3, weights2, bias2, outputs, hiddenLayer2, hiddenLayer1);
        backPropagation.averageBatchWeights(weights2);
        backPropagation.averageBatchBiases(bias2, hiddenLayer2);

        backPropBatchLoop(weights2, weights1, bias1, hiddenLayer2, hiddenLayer1, inputs);
        backPropagation.averageBatchWeights(weights1);
        backPropagation.averageBatchBiases(bias1, hiddenLayer1);
    }

    private void backPropBatchLoop(double[][][] weights, double[][][] prevWeights, double[][] bias, Node[][] layer, Node[][] prevLayer, Node[][] prevPrevLayer) {
        for (int b = 0; b < batchSize; b++) {
            backPropagation.generateNextLayerError(prevLayer[b], layer[b], weights[b]);
            backPropagation.calcNewBiases(bias[b]);
            prevWeights[b] = backPropagation.calcNewWeights(prevWeights[b], prevPrevLayer[b]);
        }
    }


    // Initialize node layers
    public void initNodes(){
        inputs = new Node[batchSize][inputLen];
        hiddenLayer1 = new Node[batchSize][h1Len];
        hiddenLayer2 = new Node[batchSize][h2Len];
        outputs = new Node[batchSize][outputLen];
        for (int b = 0; b < batchSize; b++) {
            for (int i = 0; i < inputs[0].length; i++) {
                inputs[b][i] = new Node(0, 0, true);        // input layer has no bias
            }
            for (int i = 0; i < hiddenLayer1[0].length; i++) {
                hiddenLayer1[b][i] = new Node(0, bias1[b][i], false);   // init each node with biases
            }
            for (int i = 0; i < hiddenLayer2[0].length; i++) {
                hiddenLayer2[b][i] = new Node(0, bias2[b][i], false);
            }
            for (int i = 0; i < outputs[0].length; i++) {
                outputs[b][i] = new Node(0, bias3[b][i], false);
            }
        }
    }

    public void initWeights(){
        weights1 = new double[batchSize][h1Len][inputLen];
        weights2 = new double[batchSize][h2Len][h1Len];
        weights3 = new double[batchSize][outputLen][h2Len];
    }
    public void takeWeights(boolean isNewNetwork){
        for (int b = 0; b < batchSize; b++) {
            if (isNewNetwork) {
                weights1[b] = giveRandomWeights(weights1[b]);
                weights2[b] = giveRandomWeights(weights2[b]);
                weights3[b] = giveRandomWeights(weights3[b]);
            }
            // else weights[b] = fileIO.readWeights...or something
        }
        //TODO import weights from file, run network first to get file initialized
    }

    public void initBiases(){
        bias1 = new double[batchSize][h1Len];
        bias2 = new double[batchSize][h2Len];
        bias3 = new double[batchSize][outputLen];
    }

    public void takeBiases(boolean isNewNetwork){
        for (int b = 0; b < batchSize; b++) {
            if (isNewNetwork) {
                bias1[b] = giveRandomBiases(bias1[b]);
                bias2[b] = giveRandomBiases(bias2[b]);
                bias3[b] = giveRandomBiases(bias3[b]);
            }
        }
        //TODO import biases from file, run network first to get file initialized
    }

    public void takeInputs(){
        //TODO take inputs from file
        inputs[0][0].setActivation(0);
        inputs[0][1].setActivation(0);
        inputs[0][2].setActivation(0);
        inputs[1][0].setActivation(1);
        inputs[1][1].setActivation(0);
        inputs[1][2].setActivation(0);
        inputs[2][0].setActivation(1);
        inputs[2][1].setActivation(0.5);
        inputs[2][2].setActivation(0);
        inputs[3][0].setActivation(1);
        inputs[3][1].setActivation(1);
        inputs[3][2].setActivation(0);
        inputs[4][0].setActivation(1);
        inputs[4][1].setActivation(1);
        inputs[4][2].setActivation(0.5);

        // double[][] batchInputs = fileIO.getTrainingDataBatch;
        // for (...)
        // inputs[b][i] = new Node(batchInputs[b][i], true);
    }

    public double[][] takeExpected(){
        return new double[][]{
                {1,0,0,0,0,0,0,0,0,0},
                {0,1,0,0,0,0,0,0,0,0},
                {0,0,1,0,0,0,0,0,0,0},
                {0,0,0,1,0,0,0,0,0,0},
                {0,0,0,0,1,0,0,0,0,0}
        };

        // or get from file
    }

    public double[][] giveRandomWeights(double[][] weights){       // initialize weights with random value if a new neural network
        Random rand = new Random(randSeed);
        for (int i = 0; i < weights.length; i++){
            for (int j = 0; j < weights[0].length; j++){
                weights[i][j] = rand.nextDouble();
            }
        }
        return weights;
    }

    public double[] giveRandomBiases(double[] bias){
        Random rand = new Random(randSeed);
        for (int i = 0; i < bias.length; i++){
            bias[i] = rand.nextDouble();
        }
        return bias;
    }


    public String toString(){
        String message = "";
        for (int b = 0; b < batchSize; b++) {
            message += "\nBatch number: " + b + "\n";
            message += "Run number: " + runCount + "\n";
            message += getLayerStringValue(inputs[b], 1);
            message += getLayerStringValue(outputs[b], 4);
            message += networkErrors[b];
        }
        return message;
    }

    public String debugToString(){
        String message = "";
        for (int b = 0; b < batchSize; b++) {
            message += "\nBatch number: " + b + "\n";
            message += "Nodes: \n";
            message += getLayerStringValue(inputs[b], 1);
            message += getLayerStringValue(hiddenLayer1[b], 2);
            message += getLayerStringValue(hiddenLayer2[b], 3);
            message += getLayerStringValue(outputs[b], 4);
            message += "\nWeights: \n";
            message += getWeightStringValue(weights1[b], 1);
            message += getWeightStringValue(weights2[b], 2);
            message += getWeightStringValue(weights3[b], 3);
        }
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
        //neuralNetwork.networkLearn(true,false);
        for (int i = 0; i < runNum; i++){
            neuralNetwork.networkLearn(false,false);
        }
        neuralNetwork.networkLearn(true,false);

    }



}
