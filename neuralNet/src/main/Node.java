package main;

/*
Facilitates basics of activation node 
*/
//TODO Include bias functions
public class Node{

    private double activation;              // keep this value around for backpropigation
    private double sigmoidActivation;
    public boolean isInputNode;
    private double bias;
    private double error;

    public Node(double activation, double bias, boolean isInputNode) {
        this.isInputNode = isInputNode;
        this.activation = activation;
        this.bias = bias;

        setSigmoidActivation(activation);
    }

    public double getSigmoidActivation() {
        return sigmoidActivation;
    }

    public void setSigmoidActivation(double activation) {
        if (isInputNode == false){
            this.sigmoidActivation = FeedForward.sigmoid(activation);       // if the node is input, we only want raw input data
        }
        else {
            this.sigmoidActivation = activation;
        }
    }

    public double getActivation() {
        return activation;
    }

    public void setActivation(double activation) {
        this.activation = activation;
        setSigmoidActivation(this.activation);  // keep sigmoid value up to date
    }

    public double getBias(){return this.bias;}

    public void setBias(double bias){this.bias = bias;}

    public void setError(double error){
    this.error = error;
    }

    public double getError(){
        return this.error;
    }

    @Override
    public String toString() {
        return "Node{" +
                "activation=" + activation +
                ", sigmoidActivation=" + sigmoidActivation +
                ", isInputNode=" + isInputNode +
                ", bias=" + bias +
                ", error=" + error +
                '}';
    }

    public boolean equals(Node node) {
       return this.activation == node.getActivation() && this.sigmoidActivation == node.getSigmoidActivation();
    }
}
