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
    final static int batchSize = 5;
    final static int runNum = 1000;
    final static int inputLen = 3;
    final static int h1Len = 4;
    final static int h2Len = 4;
    final static int outputLen = 10;
    static int runCount;            // how many times network has been back propagated
    private FeedForward feedForward;
    private BackPropagation backPropagation;
    private Node[][] inputs, hiddenLayer1, hiddenLayer2, outputs;
    private double[][][] weights1, weights2, weights3;
    private String[] networkErrors;
    //private double[][] weightBatchAverage;

    public NeuralNetwork(){
        initWeights(true);
        initNodes();
        takeInputs();
        feedForward = new FeedForward();
        backPropagation = new BackPropagation();
        networkErrors = new String[outputLen];
    }

    public void networkLearn(boolean printResults, boolean debug){
        //feed forward
        for (int b = 0; b < batchSize; b++) {
            this.runCount++;
            feedForward.setPrevActivationLayer(inputs[b]);
            hiddenLayer1[b] = feedForward.generateNextLayer(weights1[b], hiddenLayer1[b]);
            hiddenLayer2[b] = feedForward.generateNextLayer(weights2[b], hiddenLayer2[b]);
            outputs[b] = feedForward.generateNextLayer(weights3[b], outputs[b]);
        }

        if(debug) System.out.println("Before BackPropagation \n" + this.debugToString());

        //back propagate
        batchBackPropagation();
        if (debug) System.out.println("After BackPropagation \n" + this.debugToString() + "\n");
        if (printResults) System.out.println(this);
    }

    public void batchBackPropagation(){
        double[][] weight3Average = new double[weights3.length][weights3[0].length];
        for (int b = 0; b < batchSize; b++) {
            backPropagation.generateOutputError(outputs[b], takeExpected()[b]);
            networkErrors[b] = backPropagation.getTotalError();
            weights3[b] = backPropagation.calcNewWeights(weights3[b], hiddenLayer2[b]);
            backPropagation.generateNextLayerError(hiddenLayer2[b], weights3[b]);
            weights2[b] = backPropagation.calcNewWeights(weights2[b], hiddenLayer1[b]);
            backPropagation.generateNextLayerError(hiddenLayer1[b], weights2[b]);
            weights1[b] = backPropagation.calcNewWeights(weights1[b], inputs[b]);

        }

          /*
            backPropagation.generateNextLayerError(hiddenLayer2[b], weights3[b]);
            weights2[b] = backPropagation.calcNewWeights(weights2[b], hiddenLayer1[b]);
            backPropagation.generateNextLayerError(hiddenLayer1[b], weights2[b]);
            weights1[b] = backPropagation.calcNewWeights(weights1[b], inputs[b]);
            backPropagation.resetTotalErrorString();
         */
    }

    // Initialize node layers
    public void initNodes(){
        // double[][] batchInputs = fileIO.getTrainingDataBatch;
        inputs = new Node[batchSize][inputLen];
        hiddenLayer1 = new Node[batchSize][h1Len];
        hiddenLayer2 = new Node[batchSize][h2Len];
        outputs = new Node[batchSize][outputLen];
        for (int b = 0; b < batchSize; b++) {
            for (int i = 0; i < inputs[0].length; i++) {
                inputs[b][i] = new Node(0, true);  // = new Node(batchInputs[b][i], true);
            }
            for (int i = 0; i < hiddenLayer1[0].length; i++) {
                hiddenLayer1[b][i] = new Node(0, false);
            }
            for (int i = 0; i < hiddenLayer2[0].length; i++) {
                hiddenLayer2[b][i] = new Node(0, false);
            }
            for (int i = 0; i < outputs[0].length; i++) {
                outputs[b][i] = new Node(0, false);
            }
        }
    }

    public void initWeights(boolean isNewNetwork){
        weights1 = new double[batchSize][h1Len][inputLen];
        weights2 = new double[batchSize][h2Len][h1Len];
        weights3 = new double[batchSize][outputLen][h2Len];

        for (int b = 0; b < batchSize; b++) {
            if (isNewNetwork) {
                weights1[b] = giveRandom(weights1[b]);
                weights2[b] = giveRandom(weights2[b]);
                weights3[b] = giveRandom(weights3[b]);
            }
            // else weights[b] = fileIO.readWeights...or something
        }
        //TODO import weights from file, run network first to get file initialized
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
        neuralNetwork.networkLearn(true,false);
        for (int i = 0; i < runNum; i++){
            neuralNetwork.networkLearn(false,false);
        }
        neuralNetwork.networkLearn(true,false);

    }



}
