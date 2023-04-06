import java.util.LinkedList;

/**
 * Sean Davies, Thomas Hynes, Christopher Jarek
 * Project 4 Assembly Silos
 * This is to build Assembly Silos that take in their own code. An input is
 * sent in and the silos process their code together 1 line at a time (until all silos are
 * done with that line) then continue on until an output is achieved.
 *
 * This file is the Parser Class
 */

public class Parser {
    private LinkedList<String> commandInput = new LinkedList<>();
    private int totalRows, totalCols;
    private int inputRow, inputCol;
    private int outputRow, outputCol;
    private LinkedList<String> inputNumbers = new LinkedList<>();
    private LinkedList<String> outputNumbers = new LinkedList<>();

    Parser(LinkedList input){
        this.commandInput = input;

        //This is to grab the total rows and columns first
        String temp1 = commandInput.get(0);
        String[] temp2 = temp1.split(" ");
        totalRows = Integer.valueOf(temp2[0]);
        totalCols = Integer.valueOf(temp2[1]);
        commandInput.remove(0);

        int count = 0;
        boolean silofinished = false;
        boolean inputfinished = false;
        LinkedList<String> siloCode = new LinkedList<>();
        for (String s: commandInput) {
            if (!silofinished){
                if (s.equals("INPUT")){
                    silofinished = true;
                }
                if (!s.equals("END")){
                    siloCode.add(s);
                }
                else {
                    //Initialize the silo with siloCode Linked List and Count for silo Number
                    //manager.initializeSilos(siloCode, count)
                    count++;
                    siloCode.clear();
                }
            }
            else {
                if (s.equals("END")){
                    inputfinished = true;
                }
                //This is grabbing all the input data to send in to the silos.
                if (!inputfinished){
                    inputNumbers.add(s);
                }
                else {
                    //This is to grab the Output Row Number and Output Column Number
                    //From the input and ignore the word OUTPUT and END
                    if (!s.equals("OUTPUT") || !s.equals("END")){
                        String temp3 = commandInput.get(0);
                        String[] temp4 = temp1.split(" ");
                        outputRow = Integer.valueOf(temp4[0]);
                        outputCol = Integer.valueOf(temp4[1]);
                    }
                }
            }
        }

        //This is to grab the Input Row Number and Column Number
        //from the input Numbers list
        String temp5 = inputNumbers.get(0);
        String[] temp6 = temp1.split(" ");
        inputRow = Integer.valueOf(temp6[0]);
        inputCol = Integer.valueOf(temp6[1]);
        inputNumbers.remove(0);
    }

    /**
     * This will grab the first string in the Input numbers and return it
     * @return the first string in the input numbers.
     */
    String sendInput(){
        return inputNumbers.pop();
    }

    /**
     * This is to get the output Transfer regions values to add to the Output List
     * @param output The output transfer region
     */
    void getOutput(TransferRegion output){
        String tempUp = output.getUp();
        String tempLeft = output.getLeft();
        String tempRight = output.getRight();
        String tempDown = output.getDown();

        if (tempUp != null){
            outputNumbers.add(tempUp);
        }
        else if(tempLeft != null){
            outputNumbers.add(tempLeft);
        }
        else if(tempRight != null){
            outputNumbers.add(tempRight);
        }
        else if(tempDown != null){
            outputNumbers.add(tempDown);
        }
    }

}
