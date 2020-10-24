package tests;

import main.FileIO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileIOTest {

    private FileIO fileIO;
    private double[][] biases;
    private String[] biasesFiles = {"neuralNet/src/data/bias1.csv", "neuralNet/src/data/bias2.csv", "neuralNet/src/data/bias3.csv"};
    private String[] weightsFiles = {"neuralNet/src/data/weights1.csv","neuralNet/src/data/weights2.csv","neuralNet/src/data/weights2.csv"};

    @BeforeEach
    void setup(){
        fileIO = new FileIO();
        biases = new double[3][3];
        biases[0][0] = 1.0;
        biases[0][1] = 2.0;
        biases[0][2] = 3.0;
        biases[1][0] = 4.0;
        biases[1][1] = 5.0;
        biases[1][2] = 6.0;
        biases[2][0] = 7.0;
        biases[2][1] = 8.0;
        biases[2][2] = 9.0;

    }

    @Test
    void readWeights() {

    }

    @Test
    void writeWeights() {
    }

    @Test
    void writeBiases() {
        fileIO.writeBiases(biases[0], biasesFiles[0]);
        fileIO.writeBiases(biases[1], biasesFiles[1]);
        fileIO.writeBiases(biases[2], biasesFiles[2]);

    }

    @Test
    void readBiases() {
        double[] results1 = fileIO.readBiases(biasesFiles[0]);
        double[] results2 = fileIO.readBiases(biasesFiles[1]);
        double[] results3 = fileIO.readBiases(biasesFiles[2]);
        assertArrayEquals(results1, biases[0]);
        assertArrayEquals(results2, biases[1]);
        assertArrayEquals(results3, biases[2]);
    }
}