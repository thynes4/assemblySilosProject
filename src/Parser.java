import java.util.LinkedList;

import static java.lang.System.exit;

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
    private LinkedList<Silo> siloList = new LinkedList<>();
    private LinkedList<String> initialInputNumbers = new LinkedList<>();
    private String inputDirection, outputDirection;

    Parser(LinkedList input, LinkedList<TransferRegion> transferRegions){
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
                    System.out.println("detected input");
                    silofinished = true;
                }
                else if (!s.equals("END")){
                    siloCode.add(s);
                }
                else {
                    //This is some Test Code
                    System.out.println(count);
                    System.out.println(siloCode);
                    //Remove Above when finished

                    //first entry no Up/Left TR
                    if (count == 0){
                        siloList.add(new Silo(count,siloCode,transferRegions.get(count),null, null,
                                transferRegions.get(count + 1),transferRegions.get(count + totalCols)));
                    }
                    //Top Right no Up/Right TR
                    else if (count == (totalCols - 1)){
                        siloList.add(new Silo(count,siloCode,transferRegions.get(count),null, transferRegions.get(count-1),
                                null,transferRegions.get(count + totalCols)));
                    }
                    //Remaining Top Row no Up TR
                    else if (count < totalCols){
                        siloList.add(new Silo(count,siloCode,transferRegions.get(count),null, transferRegions.get(count-1),
                                transferRegions.get(count + 1),transferRegions.get(count + totalCols)));
                    }
                    //Bottom left no Down/Left TR
                    else if (count == ((totalRows - 1) * totalCols)) {
                        siloList.add(new Silo(count,siloCode,transferRegions.get(count),transferRegions.get(count - totalCols), null,
                                transferRegions.get(count + 1),null));
                    }
                    //Bottom Right no Down/Right TR
                    else if (count == ((totalRows * totalCols) - 1)){
                        siloList.add(new Silo(count,siloCode,transferRegions.get(count),transferRegions.get(count - totalCols),
                                transferRegions.get(count-1), null,null));
                    }
                    //Left column no Left TR
                    else if (count%totalCols == 0){
                        siloList.add(new Silo(count,siloCode,transferRegions.get(count),transferRegions.get(count - totalCols), null,
                                transferRegions.get(count + 1),transferRegions.get(count + totalCols)));
                    }
                    //Right Column no Right TR
                    else if (count%totalCols == (totalCols - 1)){
                        siloList.add(new Silo(count,siloCode,transferRegions.get(count),transferRegions.get(count - totalCols),
                                transferRegions.get(count - 1), null,transferRegions.get(count + totalCols)));
                    }
                    //Remaining Bottom Row no Down TR
                    else if (count > ((totalRows - 1) * totalCols)){
                        siloList.add(new Silo(count, siloCode,transferRegions.get(count),transferRegions.get(count - totalCols),
                                transferRegions.get(count - 1), transferRegions.get(count + 1),null));
                    }
                    //Remainder case where they get all 4 transfer Regions
                    else {
                        siloList.add(new Silo(count,siloCode,transferRegions.get(count),transferRegions.get(count - totalCols),
                                transferRegions.get(count - 1), transferRegions.get(count + 1),
                                transferRegions.get(count + totalCols)));
                    }
                    count++;
                    siloCode.clear();
                }
            }
            else {
                if (s.equals("END")){
                    inputfinished = true;
                }
                //This is grabbing all the input data to send in to the silos.
                else if (!inputfinished){
                    inputNumbers.add(s);
                }
                else {
                    //This is to grab the Output Row Number and Output Column Number
                    //From the input and ignore the word OUTPUT and END
                    if (!s.equals("OUTPUT") && !s.equals("END")){
                        String temp3 = s;
                        String[] temp4 = temp3.split(" ");
                        outputRow = Integer.valueOf(temp4[0]);
                        outputCol = Integer.valueOf(temp4[1]);
                    }
                }
            }
        }

        //This is to grab the Input Row Number and Column Number
        //from the input Numbers list
        String temp5 = inputNumbers.get(0);
        String[] temp6 = temp5.split(" ");
        inputRow = Integer.valueOf(temp6[0]);
        inputCol = Integer.valueOf(temp6[1]);
        inputNumbers.remove(0);

        System.out.println("Input Numbers: " + inputNumbers);
        initialInputNumbers.addAll(inputNumbers);
        System.out.println("Initial Input Numbers: " + initialInputNumbers);


        System.out.println("Input Row is: " + inputRow);
        System.out.println("Input Column is: " + inputCol);

        //To put in the Input Transfer Region
        if (inputRow < 0 && inputCol>= 0){
            siloList.get((inputCol)).trUp = transferRegions.get((totalCols * totalRows));
            inputDirection = "DOWN";
            System.out.println("CORRECT");
        }
        else if (inputCol < 0 && inputRow >= 0){
            siloList.get((totalCols * (inputRow))).trLeft = transferRegions.get((totalCols * totalRows));
            inputDirection = "RIGHT";
        }
        else if (inputRow >= totalRows && inputCol < totalCols){
            siloList.get((totalCols * (inputRow - 1)) + inputCol).trDown = transferRegions.get((totalCols * totalRows));
            inputDirection = "UP";
        }
        else if (inputCol >= totalCols && inputRow < totalRows){
            siloList.get((inputRow * totalCols) + (inputCol - 1)).trRight = transferRegions.get((totalCols * totalRows));
            inputDirection = "LEFT";
        }
        else {
            System.out.println("Bad Input Coordinates, Closing");
            exit(0);
        }

        System.out.println("Output Row is: " + outputRow);
        System.out.println("Output Column is: " + outputCol);
        //To put in the Output Transfer Region
        if (outputRow < 0 && outputCol >=0){
            siloList.get(outputCol).trUp = transferRegions.get((totalCols * totalRows) + 1);
            outputDirection = "DOWN";
        }
        else if (outputCol < 0 && outputRow >= 0){
            siloList.get((totalCols * (outputRow))).trLeft = transferRegions.get((totalCols * totalRows) + 1);
            outputDirection = "RIGHT";
        }
        else if (outputRow >= totalRows && outputCol < totalCols){
            siloList.get((totalCols * (outputRow - 1)) + outputCol).trDown = transferRegions.get((totalCols * totalRows) + 1);
            outputDirection = "UP";
        }
        else if (outputCol >= totalCols && outputRow < totalRows){
            siloList.get((outputRow * totalCols) + (outputCol - 1)).trRight = transferRegions.get((totalCols * totalRows) + 1);
            outputDirection = "LEFT";
        }
        else {
            System.out.println("Bad Output Coordinates, Closing");
            exit(0);
        }
    }

    String getOutputDirection(){
        return outputDirection;
    }

    String getInputDirection() {
        return inputDirection;
    }

    /**
     * To grab the silo List that was made when creating all silos
     * @return the Silo List holding all anonymous silos
     */
    LinkedList<Silo> sendSilos (){
        return siloList;
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

    LinkedList<String> sendInputList(){
        return initialInputNumbers;
    }

    LinkedList<String> sendOutputList(){
        return outputNumbers;
    }

}
