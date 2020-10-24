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


    public int getRunNumber(){

        while (true) {
            System.out.println("Enter how many times to run network: ");
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

    public int getBiasLength(){

        while (true) {
            System.out.println("Enter batch size: ");
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
            System.out.println("Enter: Network Train (0) or Network Test (1)");
            try {
                int input = Integer.parseInt(inputScanner.next());
                if (input == 0) {
                    return NetworkStatus.TRAIN;
                }
                else if (input == 1){
                    return NetworkStatus.TEST;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a correct format");
            }
        }
    }

    public boolean isSatisfied(){
        while (true){
            System.out.println("Is this what you want? (Y/N)");
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
