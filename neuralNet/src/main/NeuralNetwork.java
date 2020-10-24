package main;
/*
Main driving class
 */

import java.util.Arrays;
import java.util.Random;

//TODO Import inputs, expected, weights, and biases from file, export each back to same file after learning. Testing will pull from file only



public class NeuralNetwork {
    public final static boolean isNewNetwork = false;
    public final static int inputLen = 3;
    public final static int h1Len = 10;
    public final static int h2Len = 10;
    public final static int outputLen = 10;
    public final static int randSeed = 1;       // seed for random biases and weights at start of new network
    private static int runCount;                // how many times network has been fed forward
    //Much easier to use different files for when changing sizes of nodes in layers
    public final static String[] biasesFiles = {"neuralNet/src/data/bias1.csv", "neuralNet/src/data/bias2.csv", "neuralNet/src/data/bias3.csv"};
    public final static String[] weightsFiles = {"neuralNet/src/data/weights1.csv","neuralNet/src/data/weights2.csv","neuralNet/src/data/weights3.csv"};
    private static int batchSize;
    private static int runNum;                  // number of times the network will run
    private FeedForward feedForward;
    private BackPropagation backPropagation;
    private NetworkStatus networkStatus;
    private FileIO fileIO;
    private Node[][] inputs, hiddenLayer1, hiddenLayer2, outputs;
    private double[][][] weights1, weights2, weights3;
    private double[][] bias1, bias2, bias3, expectedNodeForm;
    private String[] networkErrors;             // used for displaying the errors of each feed forward



    public NeuralNetwork(){
        feedForward = new FeedForward();
        backPropagation = new BackPropagation();
        fileIO = new FileIO();
        networkErrors = new String[outputLen];
    }

    // this method is used to take values from either test, training, or hardcoded values if new network
    public void initializeValues(){
        initWeights();
        initBiases();
        initNodes();

        takeBiases(isNewNetwork);
        takeWeights(isNewNetwork); // pass in true if you want to give weights rand value, else read from file
        // networkStatus is set to END when the weights and or biases were not properly imported from a file
        if (networkStatus != NetworkStatus.END) {
            takeInputs();
            takeExpected();

            // note that input layer has no biases to be set
            setNodesBiases(bias1, hiddenLayer1);
            setNodesBiases(bias2, hiddenLayer2);
            setNodesBiases(bias3, outputs);
        }
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

    /*  Because everything is averaged in the batches from backpropagation,
        all weights and biases are the same after a full backpropagation.
        I simply use the first batch index for each "batch" array as once a networkRun is chosen, batch size is defaulted
        to be the size of 1. It will be considered a 2D array still, but with a row length of 1 as each time network is changed
        from learning to running, initializeValues() is called which initializes each 2D array again to the batch size chosen.
    */
    public void networkRun(boolean printResults){
        feedForward.setPrevActivationLayer(inputs[0]);
        hiddenLayer1[0] = feedForward.generateNextLayer(weights1[0], hiddenLayer1[0]);
        hiddenLayer2[0] = feedForward.generateNextLayer(weights2[0], hiddenLayer2[0]);
        outputs[0] = feedForward.generateNextLayer(weights3[0], outputs[0]);
        if (printResults) {
            int choice = 0;
            double maxChoiceValue = 0;          // find greatest choice that neural network made
                for (int i = 0; i < outputs[0].length; i++) {
                     if (maxChoiceValue < outputs[0][i].getSigmoidActivation()) {
                        choice = i;
                        maxChoiceValue = outputs[0][i].getSigmoidActivation();
                     }
                }
    System.out.println("Network chose: " + choice + ". Expected: " + Arrays.toString(expectedNodeForm[0]));
        }
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
            backPropagation.generateOutputError(outputs[b], expectedNodeForm[b]);
            networkErrors[b] = backPropagation.getTotalError();
            backPropagation.calcNewBiases(bias3[b]);
            backPropagation.calcNewWeights(weights3[b], hiddenLayer2[b]);
            // does not use backPropBatchLoop because this loop is for generating the first output error
        }
        backPropagation.averageBatchWeights(weights3);
        backPropagation.averageBatchBiases(bias3, outputs);
        setNodesBiases(bias3, outputs);

        backPropBatchLoop(weights3, weights2, bias2, outputs, hiddenLayer2, hiddenLayer1);
        backPropagation.averageBatchWeights(weights2);
        backPropagation.averageBatchBiases(bias2, hiddenLayer2);
        setNodesBiases(bias2, hiddenLayer2);

        backPropBatchLoop(weights2, weights1, bias1, hiddenLayer2, hiddenLayer1, inputs);
        backPropagation.averageBatchWeights(weights1);
        backPropagation.averageBatchBiases(bias1, hiddenLayer1);
        setNodesBiases(bias1, hiddenLayer1);
    }

    // refactored method, messy, but less loops in batchBackProp
    private void backPropBatchLoop(double[][][] weights, double[][][] prevWeights, double[][] bias, Node[][] layer, Node[][] prevLayer, Node[][] prevPrevLayer) {
        for (int b = 0; b < batchSize; b++) {
            backPropagation.generateNextLayerError(prevLayer[b], layer[b], weights[b]);
            backPropagation.calcNewBiases(bias[b]);
            backPropagation.calcNewWeights(prevWeights[b], prevPrevLayer[b]);
        }
    }


    // Initialize node layers
    public void initNodes(){
        inputs = new Node[batchSize][inputLen];
        hiddenLayer1 = new Node[batchSize][h1Len];
        hiddenLayer2 = new Node[batchSize][h2Len];
        outputs = new Node[batchSize][outputLen];
        // All nodes will be defaulted to have a bias of 0, setNodesBiases handles this later
        for (int b = 0; b < batchSize; b++) {
            for (int i = 0; i < inputs[0].length; i++) {
                inputs[b][i] = new Node(0, 0, true);
            }
            for (int i = 0; i < hiddenLayer1[0].length; i++) {
                hiddenLayer1[b][i] = new Node(0, 0, false);
            }
            for (int i = 0; i < hiddenLayer2[0].length; i++) {
                hiddenLayer2[b][i] = new Node(0,0, false);
            }
            for (int i = 0; i < outputs[0].length; i++) {
                outputs[b][i] = new Node(0, 0, false);
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
            else {
                weights1[b] = fileIO.readWeights(weightsFiles[0]);
                weights2[b] = fileIO.readWeights(weightsFiles[1]);
                weights3[b] = fileIO.readWeights(weightsFiles[2]);

                if (weights1[b] == null || weights2[b] == null || weights2[b] == null){
                    networkStatus = NetworkStatus.END;
                }
            }
        }
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
            else {
                bias1[b] = fileIO.readBiases(biasesFiles[0]);
                bias2[b] = fileIO.readBiases(biasesFiles[1]);
                bias3[b] = fileIO.readBiases(biasesFiles[2]);

                if (bias1[b] == null || bias2[b] == null || bias2[b] == null){
                    networkStatus = NetworkStatus.END;
                }
            }
        }
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

    public void takeExpected(){
        this.expectedNodeForm = new double[][]{
                {1,0,0,0,0,0,0,0,0,0},
                {0,1,0,0,0,0,0,0,0,0},
                {0,0,1,0,0,0,0,0,0,0},
                {0,0,0,1,0,0,0,0,0,0},
                {0,0,0,0,1,0,0,0,0,0}
        };

        // or get from file
    }

    // set each node's biases. Important for feed forward, called when initializing network and backpropagation
    public void setNodesBiases(double[][] biases, Node[][] nodes){
        for (int b = 0; b < nodes.length; b++ ){
            for (int i = 0; i < nodes[0].length; i++){
                nodes[b][i].setBias(biases[b][i]);
            }
        }
    }
    // initialize weights with random value if a new neural network
    public double[][] giveRandomWeights(double[][] weights){
        Random rand = new Random(randSeed);
        for (int i = 0; i < weights.length; i++){
            for (int j = 0; j < weights[0].length; j++){
                weights[i][j] = rand.nextDouble();
            }
        }
        return weights;
    }
    // only used when the network is fresh and has no file to read from
    public double[] giveRandomBiases(double[] bias){
        Random rand = new Random(randSeed);
        for (int i = 0; i < bias.length; i++){
            bias[i] = rand.nextDouble();
        }
        return bias;
    }

    // handles user IO for how to run the network
    public void startNetwork(){
            boolean satisfiedWithChoices = false;
            while (satisfiedWithChoices == false) {
                this.networkStatus = fileIO.getNetworkInfo();
                if (networkStatus == NetworkStatus.LEARN) {
                    this.batchSize = fileIO.getBiasLength();
                } else if (networkStatus == NetworkStatus.RUN) {
                    this.batchSize = 1; // running the network does so one at a time, no need for batches
                }
                this.runNum = fileIO.getRunNumber();
                System.out.println(getChoices());
                satisfiedWithChoices = fileIO.isSatisfied();
            }
            initializeValues(); // initialize all values. Even when networks been running always read and write to a file
            if (networkStatus == NetworkStatus.LEARN) {
                learnLoop();
            }
            else if (networkStatus == NetworkStatus.RUN) {
                runLoop();
            }
            else if (networkStatus == NetworkStatus.END){
                System.out.println("Closing network, files not formatted correctly");
            }
    }

    public void runLoop(){
        for (int i = 0; i < runNum; i++){
            //networkRun(false);
        }
        networkRun(true);
    }

    public void learnLoop(){
        boolean printFinal = false;
        for (int i = 0; i < runNum; i++){
            if (i == runNum - 1) printFinal = true;
            networkLearn(printFinal, false);
        }

        // after running, write all learning to file
        fileIO.writeBiases(bias1[0], biasesFiles[0]);
        fileIO.writeBiases(bias2[0], biasesFiles[1]);
        fileIO.writeBiases(bias3[0], biasesFiles[2]);
        fileIO.writeWeights(weights1[0], weightsFiles[0]);
        fileIO.writeWeights(weights2[0], weightsFiles[1]);
        fileIO.writeWeights(weights3[0], weightsFiles[2]);

        //runLoop();

    }

    public String getChoices(){
        String result = "You chose: \n";
        result += "Network Status: " + networkStatus.getValue() + "\n";
        result += "Batch size: " + batchSize + "\n";
        result += "Number of runs: " + runNum + "\n";
        return result;
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
        neuralNetwork.startNetwork();
    }



}
