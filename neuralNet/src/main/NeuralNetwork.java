package main;


import java.util.Random;

public class NeuralNetwork {
    final static int networkLen = 4;    // 4 layers
    final static int inputLen = 3;
    final static int h1Len = 4;
    final static int h2Len = 3;
    final static int outputLen = 2;
    private FeedForward feedForward;
    private Node[] inputs, hiddenLayer1, hiddenLayer2, outputs;
    private double[][] weights1, weights2, weights3;

    public NeuralNetwork(){
        takeInputs();
        initWeights(true);
    }

    public void runNetwork(){
        feedForward = new FeedForward(inputs);
        hiddenLayer1 = feedForward.generateNextLayer(weights1);
        hiddenLayer2 = feedForward.generateNextLayer(weights2);
        outputs = feedForward.generateNextLayer(weights3);
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
     inputs = new Node[]{
             new Node(1, true),
             new Node(0.5, true),
             new Node(0, true)
     };
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
        neuralNetwork.runNetwork();
        System.out.println(neuralNetwork);
    }



}
