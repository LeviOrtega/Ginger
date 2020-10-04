package main;

/*
Feed Forward algorithm
 */
public class FeedForward {

    private double[] prevActivationLayer;

    public FeedForward(double[] inputs) {
        this.prevActivationLayer = inputs;  // start off with inputs of neural network being the first activation layer

    }

    public double[] generateNextLayer(double[][] weights){
        double[] result = new double[weights.length]; // next activation layer is size of number of rows of weights array

        for (int i = 0; i < weights.length; i++){       // weights row # is # of output, col is # of input
            double activation = 0;
            for (int j = 0; j < weights[0].length; j++){
                activation += prevActivationLayer[j] * weights[i][j];
            }
            result[i] = activation;
        }
        prevActivationLayer = result;       // save this layer for next use
        return result;
    }
}
