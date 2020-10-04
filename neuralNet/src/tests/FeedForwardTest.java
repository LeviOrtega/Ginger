package tests;


import org.junit.jupiter.api.*;


import main.FeedForward;

import java.util.Random;

class FeedForwardTest {

    public double[] input;
    public double [][] weights;
    public FeedForward feedForward;

    @BeforeEach
    void setup(){
        double[][] weights = {{2, 3}, {4,5}, {1, 2}};
        double[] input = {1, 0.5};
        FeedForward ff = new FeedForward(input);
        this.weights = weights;
        this.input = input;
        this.feedForward = ff;
    }

    @Test
    void generateNextLayer() {
        double[] expected = {3.5, 6.5, 2};
        double[] output = feedForward.generateNextLayer(weights);
        Assertions.assertArrayEquals(expected, output);

    }




}