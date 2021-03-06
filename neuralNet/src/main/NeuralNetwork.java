package main;
/*
Main driving class
 */

import java.util.Arrays;
import java.util.Random;


public class NeuralNetwork {
    private final static int inputLen = 784;
    private final static int hiddenLen = 15;
    private final static int outputLen = 10;
    private final static int randSeed = 1;       // seed for random biases and weights at start of new network
    private static int iterations;
    private static int batchSize;
    private static int runNum;                  // number of times the network will run
    private static boolean isNewNetwork;
    private FeedForward feedForward;
    private BackPropagation backPropagation;
    private NetworkStatus networkStatus;
    private UserIO userIO;
    private FileIO fileIO;
    private Node[][] inputs, hiddenLayer, outputs;
    private double[][][] weights1, weights2;
    private double[][] bias1, bias2, expectedNodeForm;
    private String[] networkErrors;             // used for displaying the errors of each feed forward
    //Much easier to use different files for when changing sizes of nodes in layers
    private final static String[] biasesFiles = {"neuralNet/src/data/bias1.csv", "neuralNet/src/data/bias2.csv"};
    private final static String[] weightsFiles = {"neuralNet/src/data/weights1.csv","neuralNet/src/data/weights2.csv"};
    private final static String trainFile = "neuralNet/src/data/train-images.idx3-ubyte";
    private final static String trainLabelFile = "neuralNet/src/data/train-labels.idx1-ubyte";
    private final static String testFile = "neuralNet/src/data/t10k-images.idx3-ubyte";
    private final static String testLabelFile = "neuralNet/src/data/t10k-labels.idx1-ubyte";



    public NeuralNetwork(){
        feedForward = new FeedForward();
        backPropagation = new BackPropagation();
        userIO = new UserIO();
        fileIO = new FileIO();
    }


    public static void main(String[] args){
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        neuralNetwork.startNetwork();
    }

    // handles user IO for how to run the network
    public void startNetwork(){
        boolean satisfiedWithChoices = false;
        while (!satisfiedWithChoices) {
            networkStatus = userIO.getNetworkInfo();
            if (networkStatus == NetworkStatus.END){
                closeNetwork();
            }
            else if (networkStatus == NetworkStatus.TRAIN) {
                batchSize = userIO.getBatchSize();
                backPropagation.setLearningRate(userIO.getLearningRate());
            }
            else if (networkStatus == NetworkStatus.TEST) {
                batchSize = 1; // running the network does so one at a time, no need for batches
            }
            runNum = userIO.getRunNumber();
            isNewNetwork = userIO.isNewNetwork();
            System.out.println(getChoices());
            if (networkStatus == NetworkStatus.TRAIN){ System.out.print("Learning rate: " + backPropagation.getLearningRate() + "\n"); }
            satisfiedWithChoices = userIO.isSatisfied();
        }
        initializeValues(); // initialize all values. Even when networks been running always read and write to a file
        if (networkStatus == NetworkStatus.TRAIN) {
            fileIO.getDataFromFiles(trainLabelFile, trainFile);
            trainLoop();
        }
        else if (networkStatus == NetworkStatus.TEST) {
            fileIO.getDataFromFiles(testLabelFile, testFile);
            testLoop();
        }
    }

    // this method is used to take values from either test, training, or hardcoded values if new network
    public void initializeValues(){
        networkErrors = new String[batchSize];
        iterations = 0;
        initWeights();
        initBiases();
        initNodes();

        takeBiases(isNewNetwork);
        takeWeights(isNewNetwork); // pass in true if you want to give weights rand value, else read from file
        // networkStatus is set to END when the weights and or biases were not properly imported from a file
        if (networkStatus != NetworkStatus.END) {
            // note that input layer has no biases to be set
            setNodesBiases(bias1, hiddenLayer);
            setNodesBiases(bias2, outputs);
        }
    }


    public void initWeights(){
        weights1 = new double[batchSize][hiddenLen][inputLen];
        weights2 = new double[batchSize][outputLen][hiddenLen];
    }

    public void initBiases(){
        bias1 = new double[batchSize][hiddenLen];
        bias2 = new double[batchSize][outputLen];
    }

    // Initialize node layers
    public void initNodes(){
        inputs = new Node[batchSize][inputLen];
        hiddenLayer = new Node[batchSize][hiddenLen];
        outputs = new Node[batchSize][outputLen];
        // All nodes will be defaulted to have a bias of 0, setNodesBiases handles this later
        for (int b = 0; b < batchSize; b++) {
            for (int i = 0; i < inputs[0].length; i++) {
                inputs[b][i] = new Node(0, 0, true);
            }
            for (int i = 0; i < hiddenLayer[0].length; i++) {
                hiddenLayer[b][i] = new Node(0, 0, false);
            }
            for (int i = 0; i < outputs[0].length; i++) {
                outputs[b][i] = new Node(0, 0, false);
            }
        }
    }

    public void takeBiases(boolean isNewNetwork){
        for (int b = 0; b < batchSize; b++) {
            if (isNewNetwork) {
                bias1[b] = giveRandomBiases(bias1[b]);
                bias2[b] = giveRandomBiases(bias2[b]);
            }
            else {
                bias1[b] = fileIO.readBiases(biasesFiles[0]);
                bias2[b] = fileIO.readBiases(biasesFiles[1]);

                if (bias1[b] == null || bias2[b] == null){
                    networkStatus = NetworkStatus.END;
                }
            }
        }
    }

    public void takeWeights(boolean isNewNetwork){
        for (int b = 0; b < batchSize; b++) {
            if (isNewNetwork) {
                weights1[b] = giveRandomWeights(weights1[b]);
                weights2[b] = giveRandomWeights(weights2[b]);
            }
            else {
                weights1[b] = fileIO.readWeights(weightsFiles[0]);
                weights2[b] = fileIO.readWeights(weightsFiles[1]);

                if (weights1[b] == null || weights2[b] == null){
                    networkStatus = NetworkStatus.END;
                }
            }
        }
    }

    public void takeInputs(){
        double[][] batchInputs = fileIO.getDataBatch(iterations * batchSize, batchSize);
        //Random random = new Random(randSeed);
        for (int b = 0; b < batchSize; b++){
            for (int i = 0; i < inputs[0].length; i++){
                //random.nextDouble()
                inputs[b][i].setActivation(batchInputs[b][i]/255.0);
            }
        }
    }

    public void takeExpected(boolean printExpected){
        expectedNodeForm = new double[batchSize][outputLen];
        // print expected prints the picture associated with the expected
        int[] labels = fileIO.getLabelsBatch(iterations * batchSize, batchSize, printExpected);
        // converts the expected label (1,2,3..etc) to array where that index is set to 1 for expected output of network
        for (int b = 0; b < expectedNodeForm.length; b++){
            expectedNodeForm[b][labels[b]] = 1;
            //expectedNodeForm[b][b] = 1; // for hardcoding testing
        }
    }
    // initialize weights with random value if a new neural network
    public double[][] giveRandomWeights(double[][] weights){
        Random rand = new Random(randSeed);
        for (int i = 0; i < weights.length; i++){
            for (int j = 0; j < weights[0].length; j++){
                weights[i][j] = (rand.nextDouble()-0.5)* Math.sqrt(6/(inputLen + outputLen));
            }
        }
        return weights;
    }

    // only used when the network is new and has no file to read from
    public double[] giveRandomBiases(double[] bias){
        Random rand = new Random(randSeed);
        for (int i = 0; i < bias.length; i++){
            bias[i] = 0; //(rand.nextDouble()+0.5) * Math.sqrt(1/(inputLen + outputLen));
        }
        return bias;
    }

    // set each node's biases. Important for feed forward, called when initializing network and backpropagation
    public void setNodesBiases(double[][] biases, Node[][] nodes){
        for (int b = 0; b < nodes.length; b++ ){
            for (int i = 0; i < nodes[0].length; i++){
                nodes[b][i].setBias(biases[b][i]);
            }
        }
    }


    public void networkTrain(boolean printResults){
        takeInputs();
        takeExpected(false);
        //feed forward
        batchFeedForward();

        //back propagate
        batchBackPropagation();

        iterations++;   // each epoch will be of batchSize

        if (printResults && ((iterations) % (runNum / 10) == 0  || iterations == 1)) System.out.println("Run number: " + (iterations ) + "\n" + this);
    }

    /*  Because everything is averaged in the batches from backprop,
        all weights and biases are the same after a full backprop.
        I simply use the first batch index for each "batch" array as once a networkRun is chosen, batch size is defaulted
        to be the size of 1. It will be considered a 2D array still, but with a row length of 1 as each time network is changed
        from learning to running, initializeValues() is called which initializes each 2D array again to the batch size chosen.
    */
    public void networkTest(boolean printResults){
        takeInputs();
        takeExpected(true);
        feedForward.setPrevActivationLayer(inputs[0]);
        hiddenLayer[0] = feedForward.generateNextLayer(weights1[0], hiddenLayer[0]);
        outputs[0] = feedForward.generateNextLayer(weights2[0], outputs[0]);
        if (printResults) {
            int choice = 0;
            double maxChoiceValue = 0;          // find greatest choice that neural network made
                for (int i = 0; i < outputs[0].length; i++) {
                     if (maxChoiceValue < outputs[0][i].getSigmoidActivation()) {
                        choice = i;
                        maxChoiceValue = outputs[0][i].getSigmoidActivation();
                     }
                }

            iterations++;   // each epoch will be of batchSize
            System.out.println("Network chose: " + choice);
        }
    }
    public void batchFeedForward() {
        for (int b = 0; b < batchSize; b++) {
            feedForward.setPrevActivationLayer(inputs[b]);
            hiddenLayer[b] = feedForward.generateNextLayer(weights1[b], hiddenLayer[b]);
            outputs[b] = feedForward.generateNextLayer(weights2[b], outputs[b]);
        }
    }

    public void batchBackPropagation() {
        for (int b = 0; b < batchSize; b++) {
            backPropagation.generateOutputError(outputs[b], expectedNodeForm[b]);
            networkErrors[b] = backPropagation.getTotalError();
            backPropagation.calcNewBiases(bias2[b]);
            backPropagation.calcNewWeights(weights2[b], hiddenLayer[b]);
            // does not use backPropBatchLoop because this loop is for generating the first output error
        }
        backPropagation.averageBatchWeights(weights2);
        backPropagation.averageBatchBiases(bias2, outputs);
        setNodesBiases(bias2, outputs);

        backPropBatchLoop(weights2, weights1, bias1, outputs, hiddenLayer, inputs);
        backPropagation.averageBatchWeights(weights1);
        backPropagation.averageBatchBiases(bias1, hiddenLayer);
        setNodesBiases(bias1, hiddenLayer);
    }

    // refactored method, messy, but less loops in batchBackProp
    private void backPropBatchLoop(double[][][] weights, double[][][] prevWeights, double[][] bias, Node[][] layer, Node[][] prevLayer, Node[][] prevPrevLayer) {
        for (int b = 0; b < batchSize; b++) {
            backPropagation.generateNextLayerError(prevLayer[b], layer[b], weights[b]);
            backPropagation.calcNewBiases(bias[b]);
            backPropagation.calcNewWeights(prevWeights[b], prevPrevLayer[b]);
        }
    }

    public void testLoop(){
        boolean cont;
        for (int i = 0; i < runNum; i++){
            networkTest(true);
            cont = userIO.doContinue();
            if (!cont){
                startNetwork();
            }
        }

        //start network again
        startNetwork();
    }

    public void trainLoop(){
        for (int i = 0; i < runNum; i++){
            networkTrain(true);
        }
        // after running, write all learning to file
        fileIO.writeBiases(bias1[0], biasesFiles[0]);
        fileIO.writeBiases(bias2[0], biasesFiles[1]);
        fileIO.writeWeights(weights1[0], weightsFiles[0]);
        fileIO.writeWeights(weights2[0], weightsFiles[1]);

        // start network again
        startNetwork();
    }

    public String getChoices(){
        String result = "You chose: \n"
        +"Network Status: " + networkStatus.getValue() + "\n"
        + "Batch size: " + batchSize + "\n"
        + "Number of runs: " + runNum + "\n"
        + "New network: " + isNewNetwork;
        return result;
    }

    public String toString(){

        double avg = 0;
        for (int b = 0; b < batchSize; b++) {
           avg += Double.valueOf(networkErrors[b]);
        }
        avg /= batchSize;
        return "Network error: " + avg;
    }

    public String debugToString(){
        String message = "";
        for (int b = 0; b < batchSize; b++) {
            message += "\nBatch number: " + b + "\n"
            +   "Nodes: \n"
            +   getLayerStringValue(inputs[b], 1)
            +   getLayerStringValue(hiddenLayer[b], 2)
            +   getLayerStringValue(outputs[b], 3)
            +   "\nWeights: \n"
            +   getWeightStringValue(weights1[b], 1)
            +   getWeightStringValue(weights2[b], 2);
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

    public void closeNetwork(){
        System.out.println("Quitting..");
        System.exit(0);
    }

}
