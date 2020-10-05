package main;
/*
Feed Forward algorithm
 */
public class FeedForward {

    private Node[] prevActivationLayer;

    public FeedForward() {}

    public void setPrevActivationLayer(Node[] inputs){
        this.prevActivationLayer = inputs;              // start off with inputs of neural network being the first activation layer
    }

    public Node[] generateNextLayer(double[][] weights){
        Node[] result = new Node[weights.length];       // next activation layer is size of number of rows of weights array

        for (int i = 0; i < weights.length; i++){       // weights row # is # of output, col is # of input
            double activation = 0;
            for (int j = 0; j < weights[0].length; j++){
                activation += prevActivationLayer[j].getSigmoidActivation() * weights[i][j];
            }
            result[i] = new Node(activation, false);    // all new nodes will not be an input node so isInputNode is false
        }
        prevActivationLayer = result;                   // save this layer for next use
        return result;
    }

    public static double sigmoid(double activation){
        return (1/(1+Math.pow(Math.E, -activation)));
    }
}
