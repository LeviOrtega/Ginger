package tests;


import main.Node;
import org.junit.jupiter.api.*;


import main.FeedForward;
class FeedForwardTest {

    public FeedForward feedForward;

    @BeforeEach
    void setup(){
        // Input nodes
        Node node1 = new Node(1, 0,true);
        Node node2 = new Node(1, 0, true);
        Node node3 = new Node(0, 0, true);
        Node[] inputs = {node1, node2, node3};
        FeedForward feedForward = new FeedForward();
        feedForward.setPrevActivationLayer(inputs);
        this.feedForward = feedForward;
    }

    @Test
    void testGenerateNextLayer() {
        //tests 2 layer neural network

        // hidden layer1 neurons
        double[][] weights1 = {{2,3,1},{1,2,3}};
        Node node1 = new Node(7,2,  false);
        Node node2 = new Node(6, 3, false);
        Node[] expected1 = {node1, node2};

        Node node3 = new Node(0,2,  false);
        Node node4 = new Node(0, 3, false);
        Node[] output1 = {node3, node4};
        feedForward.generateNextLayer(weights1, output1);
        for (int i = 0; i < expected1.length; i++){
            Assertions.assertEquals(expected1[i].getActivation(),output1[i].getActivation());
        }

        // Output neuron
        // I use 2d arrays for weights and 1d array for expected neuron because of method params for functions
        double[][] weights2 = {{2,1}};
        Node expectedNode = new Node(7.995705274454564, 5,false);
        Node[] expected2 = {expectedNode};

        Node outputNode = new Node(0, 5, false);
        Node[] output2 = {outputNode};
        feedForward.generateNextLayer(weights2, output2);
        Assertions.assertEquals(expected2[0].getActivation(),output2[0].getActivation());



    }

    @Test
    void testSigmoid(){
        //test a couple numbers for sigmoid values
        Assertions.assertEquals(0.8807970779778823, feedForward.sigmoid(2));
        Assertions.assertEquals(0.11920292202211757, feedForward.sigmoid(-2));
        Assertions.assertEquals(0.5, feedForward.sigmoid(0));
    }

}