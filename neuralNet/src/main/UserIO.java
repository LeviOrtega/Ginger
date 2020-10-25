package main;
/*
Manages input and from user
 */

import java.util.Scanner;

public class UserIO {

    private Scanner inputScanner;

    public UserIO(){
        inputScanner = new Scanner(System.in);
    }

    public int getRunNumber(){return getInt("Enter how many times to run network: "); }

    public int getBatchSize(){ return getInt("Enter batch size: "); }

    public boolean isNewNetwork(){ return getYesNo("Is this a new Neural Network?"); }

    public boolean isSatisfied(){ return getYesNo("Is this what you want?"); }

    public boolean doContinue(){ return getYesNo("Continue testing? "); }

    public double getLearningRate(){
        while (true) {
            System.out.println("Enter learning rate: ");
            try {
                double input = Double.parseDouble(inputScanner.next());
                if (input > 0) {
                    return input;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal value greater than zero.");
            }
        }
    }

    public int getInt(String s) {
        while (true) {
            System.out.println(s);
            try {
                int input = Integer.parseInt(inputScanner.next());
                if (input > 0) {
                    return input;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter an integer greater than zero.");
            }
        }
    }

    public NetworkStatus getNetworkInfo(){
        // return 1 for run, 0 for learn
        while (true) {
            System.out.println("Enter: Network Train (0) Network Test (1) Network Quit(2)");
            try {
                int input = Integer.parseInt(inputScanner.next());
                if (input == 0) {
                    return NetworkStatus.TRAIN;
                }
                else if (input == 1){
                    return NetworkStatus.TEST;
                }
                else if (input == 2){
                    return NetworkStatus.END;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a correct format");
            }
        }
    }
    public boolean getYesNo(String message) {
        while (true){
            System.out.println(message + " (Y/N)");
            try {
                String answer = inputScanner.next().trim().toLowerCase();
                if (answer.equals("y")){
                    return true;
                }
                else if (answer.equals("n")){
                    return false;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a correct format");
            }
        }
    }

}
