package main;

public class BackPropagation {
    final static double learningRate = 2;
    double[] actualSigmoid,actualActivation,error;
    double[] prevActualActivation, prevError;

    public BackPropagation(){}

    public void generateOutputError(Node[] outputs, double[] expected){
        generateActualValues(outputs);
        error = new double[expected.length];
        for (int i = 0; i < expected.length; i++){
            error[i] = (actualSigmoid[i] - expected[i]) * sigmoidDerivative(actualActivation[i]);       // (a-y)*Sig'(Z)
        }
    }

    public void generateNextLayerError(){

    }

    // generates new matrix of weights
    public double[][] generateWeights(Node[] prevLayer, double[][] weights){
        double[][] deltaW = new double[weights.length][weights[0].length];      // create equal size matrix for delta weights




        prevError = error;
        // after weights are calculated, update prevError to use next Backprop
        return null;
    }


    public void generateActualValues(Node[] node){
        // we are going to need the actual values for Z funciton, and sigmoid for error
        actualActivation = new double[node.length];
        actualSigmoid = new double[node.length];
        for(int i = 0; i < node.length; i++){
            actualActivation[i] = node[i].getActivation();
            actualSigmoid[i] = node[i].getSigmoidActivation();
        }
    }
    public void generatePrevActivation(Node[] node){
        prevActualActivation = new double[node.length];
        for (int i = 0; i < node.length; i++){
            prevActualActivation[i] = node[i].getActivation();  // for weight deriv, we need array of activations
        }
    }

    public double sigmoidDerivative(double x){ // 1/(e^x * (1+e^-x)^2)
        return 1/(Math.pow(Math.E, x) * (Math.pow(1+Math.pow(Math.E, -x),2)));
    }

}
