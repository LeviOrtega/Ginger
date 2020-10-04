package main;

public class NeuralNetwork {
    final static int inputLen = 3;
    final static int h1Len = 4;
    final static int h2Len = 3;
    final static int outputLen = 2;
    private Node[] inputs;
    private Node[] hiddenLayer1;
    private Node[] hiddenLayer2;
    private Node[] outputs;
    private double[][] weights1, weights2, weigths3;

    public NeuralNetwork(){
        takeInputs();

    }

    public void initWeights(){


    }

    public void takeInputs(){
    // for now we will give costom inputs
     inputs = new Node[]{new Node(1, true), new Node(2, true), new Node(3, true)};
    }


    public String toString(){
        String message = "Input nodes: \n";
        for (Node n: inputs){
            message += n + "\n";
        }
        return message;
    }




    public static void main(String[] args){
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        //System.out.println(neuralNetwork);
    }



}
