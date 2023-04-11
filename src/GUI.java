import java.util.LinkedList;
import java.util.Scanner;

/**
 * Sean Davies, Thomas Hynes, Christopher Jarek
 * Project 4 Assembly Silos
 * This is to build Assembly Silos that take in their own code. An input is
 * sent in and the silos process their code together 1 line at a time (until all silos are
 * done with that line) then continue on until an output is achieved.
 *
 * This file is the GUI file which will display and allow the User to run the program.
 */

public class GUI {
    static LinkedList<TransferRegion> transferRegions = new LinkedList<>();

    public static void main(String[] args) {
        LinkedList<String> commandInput = new LinkedList<>();
        boolean inputFinished = false;

        Scanner sc = new Scanner(System.in);
        String temp1 = sc.nextLine();
        commandInput.add(temp1);
        String[] temp2 = temp1.split(" ");
        int var1 = Integer.valueOf(temp2[0]);
        int var2 = Integer.valueOf(temp2[1]);
        int totalEnds = (var1 * var2) + 2;
        int count = 0;
        while (!inputFinished){
            temp1 = sc.nextLine();
            if (temp1.equals("END")){
                count++;
            }
            commandInput.add(temp1);
            //By checking the Initial row and column information can see how many
            //ENDs should be written to prevent further input.
            if (count == totalEnds){
                inputFinished = true;
            }
        }

        //Creating all the transfer regions. It is row X Column + 2.
        //The + 2 is the input and output.
        for (int i = 0; i < totalEnds; i++){
            transferRegions.add(new TransferRegion());
        }

        //Creating the parser will create all silos too.
        Parser parser = new Parser(commandInput, transferRegions);
    }
}
