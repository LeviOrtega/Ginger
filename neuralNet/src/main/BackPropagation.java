package main;
/*
Learning algorithm for network
 */

//TODO Account inputs for batches
//TODO Create bias change matrix
//TODO Include bias changes for calcNewWeights

public class BackPropagation {
    final static double learningRate = 4;
    static String totalError;
    double[] actualSigmoid,actualActivation,error;
    double[] prevActualSigmoid, prevError;
    double[][] weightChangeMatrix;

    public BackPropagation(){}

    public void generateOutputError(Node[] outputs, double[] expected){
        generateActualValues(outputs);
        error = new double[outputs.length];
        totalError = "";
        double tError = 0;
        for (int i = 0; i < error.length; i++){
            tError += 0.5*(Math.pow((expected[i] - actualSigmoid[i]),2));                                   // Sum(1/2 * (y-a)^2)
            error[i] = (actualSigmoid[i] - expected[i]) * sigmoidDerivative(actualActivation[i]);           // (a-y)*Sig'(Z)
            outputs[i].setError(error[i]);
        }
        totalError += Double.toString(tError);
    }

    public void generateNextLayerError(Node[] layer, double[][] prevWeights){
        generateActualValues(layer);
        error = new double[layer.length];
        prevWeights = transpose(prevWeights);       // makes it easier for multiplying
        double[] prevErrorDotWeights = errorDotProduct(prevWeights);

        for (int i = 0; i < error.length; i++){
            error[i] = prevErrorDotWeights[i] * sigmoidDerivative(actualActivation[i]);                     // error(l-1)*W(l-1)T x Sig'(Z(l))
            layer[i].setError(error[i]);
        }

    }

    public double[] errorDotProduct(double[][] prevWeights){
        double[] result = new double[error.length];     // Hadamard dot product calls for vector of equal length
        double value = 0;
        for (int i = 0; i < prevWeights.length; i++){
            for (int j = 0; j < prevWeights[0].length; j++){
                value += prevWeights[i][j] * prevError[j];
            }
            result[i] = value;
        }

        return result;

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
        // after weights are calculated, update prevError to use next Backpropagation
    }


    public void generateActualValues(Node[] node){
        // we are going to need the actual values for Z function, and sigmoid for error
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

    public String getTotalError(){
        return totalError;
    }

    public double[][] transpose(double[][] array){
        double[][] result = new double[array[0].length][array.length];          // row = col, col = row
        for (int i = 0; i < array.length; i++){
            for (int j = 0; j < array[0].length; j++){
                result[j][i] = array[i][j];
            }
        }
        return result;
    }

    public double sigmoidDerivative(double x){ // 1/(e^x * (1+e^-x)^2)
        return 1/(Math.pow(Math.E, x) * (Math.pow(1+Math.pow(Math.E, -x),2)));
    }



}
