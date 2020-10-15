package main;
/*
Feed Forward algorithm
 */

public class FeedForward {

    private Node[] prevActivationLayer;

    public FeedForward() {}

    public void setPrevActivationLayer(Node[] inputs){
        this.prevActivationLayer = inputs;                          // start off with inputs of neural network being the first activation layer
    }

    public Node[] generateNextLayer(double[][] weights, Node[] layer){
        for (int i = 0; i < weights.length; i++){                  // weights row # is # of output, col is # of input
            double activation = 0;
            for (int j = 0; j < weights[0].length; j++){
                activation += prevActivationLayer[j].getSigmoidActivation() * weights[i][j];
            }
            activation += layer[i].getBias();
            layer[i].setActivation(activation);
            layer[i].setError(0);
        }
        prevActivationLayer = layer;                              // save this layer for next feed forward
        return layer;
    }

    public static double sigmoid(double activation){
        return (1/(1+Math.pow(Math.E, -activation)));
    }
}
