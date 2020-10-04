/*
Feed Forward algorithm
 */
public class FeedForward {

    private double[] prevActivationLayer;
    private double[][] weights;

    public FeedForward(double[] inputs) {
        this.prevActivationLayer = inputs;  // start off with inputs of neural network being the first activation layer

    }

    public double[] generateNextLayer(double[] prevActivationLayer, double[][] weights){
        double[] result = new double[weights[0].length]; // next activation layer is size of number of rows of weights array



        return null;
    }
}
