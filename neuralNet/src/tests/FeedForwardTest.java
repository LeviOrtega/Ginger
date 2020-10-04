package tests;


import org.junit.jupiter.api.*;


import main.FeedForward;

import java.util.Random;

class FeedForwardTest {

    public double[] input;
    public FeedForward feedForward;

    @BeforeEach
    void setup(){
        double[] input = {1, 0.5};
        FeedForward ff = new FeedForward(input);
        this.input = input;
        this.feedForward = ff;
    }

    @Test
    void testGenerateNextLayer() {
        //test for a 3 layer neural network without sigmoid
        double[][] weights1 = {{2, 3}, {4,5}, {1, 2}};
        double[] expected1 = {3.5, 6.5, 2};
        double[] output1 = feedForward.generateNextLayer(weights1);
        Assertions.assertArrayEquals(expected1, output1);
        // test for second feed forward
        double[][] weights2 = {{1,2,3},{3,2,1}};
        double[] expected2 = {22.5, 25.5};
        double[] output2 = feedForward.generateNextLayer(weights2);
        Assertions.assertArrayEquals(expected2, output2);
        //final output
        double[][] weights3 = {{3,5}};
        double[] expected3 = {195.0};
        double[] output3 = feedForward.generateNextLayer(weights3);
        Assertions.assertArrayEquals(expected3, output3);


    }

    @Test
    void testSigmoid(){
        //test a couple numbers for sigmoid values
        Assertions.assertEquals(0.8807970779778823, feedForward.sigmoid(2));
        Assertions.assertEquals(0.11920292202211757, feedForward.sigmoid(-2));
        Assertions.assertEquals(0.5, feedForward.sigmoid(0));
    }




}