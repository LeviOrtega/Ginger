package tests;

import main.FileIO;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class FileIOTest {

    private FileIO fileIO;
    private double[][] biases;
    private double[][][] weights;
    private String[] biasesFiles = {"neuralNet/src/testFiles/bias1Test.csv", "neuralNet/src/testFiles/bias2Test.csv", "neuralNet/src/testFiles/bias3Test.csv"};
    private String[] weightsFiles = {"neuralNet/src/testFiles/weights1Test.csv","neuralNet/src/testFiles/weights2Test.csv","neuralNet/src/testFiles/weights3Test.csv"};

    @BeforeEach
    void setup(){
        fileIO = new FileIO();
        // 3 layers, 3 rows
        weights = new double[3][2][2];
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
        weights[0][0][0] = 1.0;
        weights[0][0][1] = 2.0;
        weights[0][1][0] = 3.0;
        weights[0][1][1] = 4.0;
        weights[1][0][0] = 5.0;
        weights[1][0][1] = 6.0;
        weights[1][1][0] = 7.0;
        weights[1][1][1] = 8.0;
        weights[2][0][0] = 9.0;
        weights[2][0][1] = 10.0;
        weights[2][1][0] = 11.0;
        weights[2][1][1] = 12.0;
    }

    @Test
    void writeWeights() {
        fileIO.writeWeights(weights[0], weightsFiles[0]);
        fileIO.writeWeights(weights[1], weightsFiles[1]);
        fileIO.writeWeights(weights[2], weightsFiles[2]);
    }

    @Test
    void readWeights() {
        double[][] results1 = fileIO.readWeights(weightsFiles[0]);
        double[][] results2 = fileIO.readWeights(weightsFiles[1]);
        double[][] results3 = fileIO.readWeights(weightsFiles[2]);
        assertArrayEquals(results1[0], weights[0][0]);
        assertArrayEquals(results1[1], weights[0][1]);
        assertArrayEquals(results2[0], weights[1][0]);
        assertArrayEquals(results2[1], weights[1][1]);
        assertArrayEquals(results3[0], weights[2][0]);
        assertArrayEquals(results3[1], weights[2][1]);

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