package main;

/*
Facilitates basics of activation node 
*/
public class Node{

    private double activation;              // keep this value around for backpropigation
    private double sigmoidActivation;
    public boolean isInputNode;
    //private double bias;
    private double error;

    public Node(double activation, boolean isInputNode) {
        this.isInputNode = isInputNode;
        this.activation = activation;

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

    public void setError(double error){
    this.error = error;
    }

    @Override
    public String toString() {
        return "Node{" +
                "activation = " + activation +
                ", sigmoidActivation = " + sigmoidActivation +
                ", isInputNode = " + isInputNode +
                ", error = " + error +
                '}';
    }


    public boolean equals(Node node) {
       return this.activation == node.getActivation() && this.sigmoidActivation == node.getSigmoidActivation();
    }
}
