package tests;


import main.Node;
import org.junit.jupiter.api.*;


import main.FeedForward;

import java.util.Random;

class FeedForwardTest {

    public FeedForward feedForward;

    @BeforeEach
    void setup(){
        // Inputy nodes
        Node node1 = new Node(1, true);
        Node node2 = new Node(1, true);
        Node node3 = new Node(0, true);
        Node[] inputs = {node1, node2, node3};
        FeedForward feedForward = new FeedForward(inputs);
        this.feedForward = feedForward;
    }

    @Test
    void testGenerateNextLayer() {
        //tests 2 layer neural network

        // hidden layer1 neurons
        double[][] weights1 = {{2,3,1},{1,2,3}};
        Node node1 = new Node(5, false);
        Node node2 = new Node(3, false);
        Node[] expected1 = {node1, node2};
        Node[] output1 = feedForward.generateNextLayer(weights1);
        for (int i = 0; i < expected1.length; i++){
            Assertions.assertEquals(true, expected1[i].equals(output1[i]));
        }

        // Output neuron
        // I use 2d arrays for weights and 1d array for expected neuron because of method params for functions
        double[][] weights2 = {{2,1}};
        Node outputNode = new Node(2.939188424973864, false);
        Node[] expected2 = {outputNode};
        Node[] output2 = feedForward.generateNextLayer(weights2);
        Assertions.assertEquals(true, expected2[0].equals(output2[0]));



    }

    @Test
    void testSigmoid(){
        //test a couple numbers for sigmoid values
        Assertions.assertEquals(0.8807970779778823, feedForward.sigmoid(2));
        Assertions.assertEquals(0.11920292202211757, feedForward.sigmoid(-2));
        Assertions.assertEquals(0.5, feedForward.sigmoid(0));
    }




}