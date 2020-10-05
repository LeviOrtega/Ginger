package main;

public class BackPropagation {
    final static double learningRate = 2;
    double[] actualSigmoid,actualActivation,error;
    double[] prevActualSigmoid, prevError;
    double[][] weightChangeMatrix;

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

    public double[][] calcNewWeights(double[][] weights, Node[] prevLayer){
        generateWeightChange(prevLayer);

        for(int i = 0; i < weights.length; i++){
            for (int j = 0; j < weights[0].length; j++){
                weights[i][j] -= weightChangeMatrix[i][j];
            }
        }
        return weights;
    }

    // generates new matrix of weights
    public void generateWeightChange(Node[] prevLayer){
        generatePrevSigmoid(prevLayer);
        generateWeightChangeMatrix();

        // each index of error multiplied by each row of prev activations
        for (int i = 0; i < weightChangeMatrix.length; i++){
            for (int j = 0; j < weightChangeMatrix[0].length; j++){
                weightChangeMatrix[i][j] *= error[i] * learningRate;        // delta W(oi) = a(i) * error(o) * lr
            }
        }

        prevError = error;
        // after weights are calculated, update prevError to use next Backprop
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
    public void generatePrevSigmoid(Node[] node){
        prevActualSigmoid = new double[node.length];
        for (int i = 0; i < node.length; i++){
            prevActualSigmoid[i] = node[i].getSigmoidActivation();
            // for weight derivative, we need array of sigmoid calculated activations
        }
    }

    public void generateWeightChangeMatrix(){
        // activation matrix will be a output.len x input.len size matrix
        // change of weights is directly associated with sigmoid activation values of prev activation layer
        weightChangeMatrix = new double[actualSigmoid.length][prevActualSigmoid.length];
        for(int i = 0; i < weightChangeMatrix.length; i++){
            for (int j = 0; j < weightChangeMatrix[0].length; j++){
                weightChangeMatrix[i][j] = prevActualSigmoid[j];
                /*
                Matrix of
                |a1..an|
                |a1..an|
                |a1..an|
                ... for m number of output nodes and n number of input nodes
                 */
            }
        }
    }


    public double[][] transpose(double[][] array){

        return null;
    }

    public double sigmoidDerivative(double x){ // 1/(e^x * (1+e^-x)^2)
        return 1/(Math.pow(Math.E, x) * (Math.pow(1+Math.pow(Math.E, -x),2)));
    }

}
