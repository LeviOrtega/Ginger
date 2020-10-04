package main;

/*
Facilitates basics of activation node 
*/
public class Node {

    private double activation;
    //private double bias;
    private double error;

    public Node(double activation){
        this.activation = activation;
    }

    public double getActivation() {
        return activation;
    }

    public void setActivation(double activation) {
        this.activation = activation;
    }

}
