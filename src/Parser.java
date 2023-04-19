import java.util.*;
import java.util.function.Consumer;

import static java.lang.System.exit;

public class Parser {
    private static Integer number = 0;
    private static Integer lineNumber = 0;
    private LinkedList<String> commandInput = new LinkedList<>();
    private int totalRows, totalCols;
    private int inputRow, inputCol;
    private int outputRow, outputCol;
    private LinkedList<String> inputNumbers = new LinkedList<>();
    private LinkedList<String> initialInputNumbers = new LinkedList<>();
    private LinkedList<String> outputNumbers = new LinkedList<>();
    private static LinkedList<Silo> siloList = new LinkedList<>();
    private String inputDirection, outputDirection;

    private static LinkedList<String> input1 = new LinkedList<>();
    protected Map<Integer,List<Runnable>> siloRunnable = new TreeMap<>();

    private static Integer siloToGrab;
    private static LinkedList<Input> inputList = new LinkedList<>();
    private static LinkedList<Output> outputList = new LinkedList<>();


    Parser(LinkedList input, LinkedList<TransferRegion> transferRegions, Integer totalInputs){
        this.commandInput = input;

        //This is to grab the total rows and columns first
        String temp1 = commandInput.get(0);
        String[] temp2 = temp1.split(" ");
        totalRows = Integer.valueOf(temp2[0]);
        totalCols = Integer.valueOf(temp2[1]);
        commandInput.remove(0);

        int count = 0;
        int currentInputTotal = 0;
        boolean silofinished = false;
        boolean inputfinished = false;
        LinkedList<String> siloCode = new LinkedList<>();
        for (String s: commandInput) {
            if (!silofinished){
                if (s.equals("INPUT")){
                    System.out.println("detected input");
                    currentInputTotal++;
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
                    //siloRunnable.put(count,codeToRunnable(siloCode,count));
                    siloList.get(count).addRunnableList(codeToRunnable(siloCode,count));
                    count++;
                    siloCode.clear();
                }
            }
            else {
                if (s.equals("END") && !inputfinished){
                    inputList.add(new Input(inputNumbers));
                    inputNumbers.clear();
                    if (currentInputTotal == totalInputs){
                        inputfinished = true;
                    }
                }
                else if (s.matches("INPUT")){
                    currentInputTotal++;
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
                        outputList.add(new Output(outputRow,outputCol));
                    }
                }
            }
        }

        //To put in the Input Transfer Region
        for (int i = 0; i < totalInputs; i++) {
            inputList.get(i).inputTransferRegion(transferRegions,siloList,totalCols,totalRows);
            outputList.get(i).outputTransferRegion(transferRegions,siloList,totalCols,totalRows);
        }
//        if (inputRow < 0 && inputCol>= 0){
//            siloList.get((inputCol)).trUp = transferRegions.get((totalCols * totalRows));
//            inputDirection = "DOWN";
//            System.out.println("Input being added to silo: " + inputCol + " input Direction: " + inputDirection);
//        }
//        else if (inputCol < 0 && inputRow >= 0){
//            siloList.get((totalCols * (inputRow))).trLeft = transferRegions.get((totalCols * totalRows));
//            inputDirection = "RIGHT";
//            System.out.println("Input being added to silo: " + (totalCols * (inputRow)) + " input Direction: " + inputDirection);
//        }
//        else if (inputRow >= totalRows && inputCol < totalCols){
//            siloList.get((totalCols * (inputRow - 1)) + inputCol).trDown = transferRegions.get((totalCols * totalRows));
//            inputDirection = "UP";
//            System.out.println("Input being added to silo: " + ((totalCols * (inputRow - 1)) + inputCol) + " input Direction: " + inputDirection);
//        }
//        else if (inputCol >= totalCols && inputRow < totalRows){
//            siloList.get((inputRow * totalCols) + (inputCol - 1)).trRight = transferRegions.get((totalCols * totalRows));
//            inputDirection = "LEFT";
//            System.out.println("Input being added to silo: " + ((inputRow * totalCols) + (inputCol - 1)) + " input Direction: " + inputDirection);
//        }
//        else {
//            System.out.println("Bad Input Coordinates, Closing");
//            exit(0);
//        }
        //To put in the Output Transfer Region
//        if (outputRow < 0 && outputCol >=0){
//            siloList.get(outputCol).trUp = transferRegions.get((totalCols * totalRows) + 1);
//            outputDirection = "DOWN";
//            siloToGrab = outputCol;
//            System.out.println("Output being added to silo: " + (outputCol) + " Output Direction: " + outputDirection);
//        }
//        else if (outputCol < 0 && outputRow >= 0){
//            siloList.get((totalCols * (outputRow))).trLeft = transferRegions.get((totalCols * totalRows) + 1);
//            outputDirection = "RIGHT";
//            siloToGrab = ((totalCols * (outputRow)));
//            System.out.println("Output being added to silo: " + (totalCols * (outputRow)) + " Output Direction: " + outputDirection);
//        }
//        else if (outputRow >= totalRows && outputCol < totalCols){
//            siloList.get((totalCols * (outputRow - 1)) + outputCol).trDown = transferRegions.get((totalCols * totalRows) + 1);
//            outputDirection = "UP";
//            siloToGrab = ((totalCols * (outputRow - 1)) + outputCol);
//            System.out.println("Output being added to silo: " + ((totalCols * (outputRow - 1)) + outputCol) + " Output Direction: " + outputDirection);
//        }
//        else if (outputCol >= totalCols && outputRow < totalRows){
//            siloList.get((outputRow * totalCols) + (outputCol - 1)).trRight = transferRegions.get((totalCols * totalRows) + 1);
//            outputDirection = "LEFT";
//            siloToGrab = ((outputRow * totalCols) + (outputCol - 1));
//            System.out.println("Output being added to silo: " + ((outputRow * totalCols) + (outputCol - 1)) + " Output Direction: " + outputDirection);
//        }
//        else {
//            System.out.println("Bad Output Coordinates, Closing");
//            exit(0);
//        }
    }

    List<Runnable> codeToRunnable(LinkedList<String> code, Integer siloNum){
        System.out.println("SiloCode sent to codeToRunnable" + code);
        List<Runnable> methods = new ArrayList<>();
        for (String s : code) {
            String temp1 = s;
            String[] temp2 = s.split(" ");
            switch (temp2[0]){
                case "NOOP" -> {
                    methods.add(() -> siloList.get(siloNum).noop());
                    System.out.println("NOOP Added");
                }
                case "MOVE" -> {
                    methods.add(() -> siloList.get(siloNum).move(temp2[1],temp2[2]));
                    System.out.println("Move Added");
                }
                case "SWAP" -> {
                    methods.add(() -> siloList.get(siloNum).swap());
                    System.out.println("SWAP Added");
                }
                case "SAVE" -> {
                    methods.add(() -> siloList.get(siloNum).save());
                    System.out.println("Save added");
                }
                case "ADD" -> {
                    methods.add(() -> siloList.get(siloNum).add(temp2[1]));
                    System.out.println("ADD added");
                }
                case "SUB" -> {
                    methods.add(() -> siloList.get(siloNum).sub(temp2[1]));
                    System.out.println("SUB Added");
                }
                case "NEGATE" -> {
                    methods.add(() -> siloList.get(siloNum).negate());
                    System.out.println("NEGATE Added");
                }
                case "JUMP" -> {
                    Integer temp3 = 0;

                    for (String g : code){
                        if (g.contains(":")){
                            if (g.contains(temp2[1])){
                                Integer finalTemp = temp3 + 1;
                                methods.add(() -> siloList.get(siloNum).changeSiloLineNumber(finalTemp));
                            }
                        }
                        temp3++;
                    }
                    System.out.println("Jump added");
                }
                case "JEZ" -> {
                    methods.add(() -> siloList.get(siloNum).jez(temp2[1]));
                    System.out.println("JEZ Added");
                }
                case "JNZ" -> {
                    methods.add(() -> siloList.get(siloNum).jnz(temp2[1]));
                    System.out.println("JNZ Added");
                }
                case "JGZ" -> {
                    methods.add(() -> siloList.get(siloNum).jgz(temp2[1]));
                    System.out.println("JGZ Added");
                }
                case "JLZ" -> {
                    methods.add(() -> siloList.get(siloNum).jlz(temp2[1]));
                    System.out.println("JLZ Added");
                }
                case "JRO" -> {
                    methods.add(() -> siloList.get(siloNum).jro(temp2[1]));
                    System.out.println("JRO Added");
                }
            }
        }
        return methods;
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
        if (inputNumbers.size() == 0){
            return null;
        }
        else {
            return inputNumbers.pop();
        }
    }


    /**
     * This is to get the output Transfer regions values to add to the Output List
     * @param transferRegions The output transfer region
     */
    void getOutput(LinkedList<TransferRegion> transferRegions){
        String temp = null;
        switch (outputDirection){
            case "UP" -> {
                if (!transferRegions.get(siloToGrab).down.matches(" ")){
                    temp = transferRegions.get(siloToGrab).down;
                    transferRegions.get(siloToGrab).down = " ";
                }
            }
            case "DOWN" -> {
                if (!transferRegions.get(siloToGrab).up.matches(" ")){
                    temp = transferRegions.get(siloToGrab).up;
                    transferRegions.get(siloToGrab).up = " ";
                }
            }
            case "LEFT" -> {
                if (!transferRegions.get(siloToGrab).right.matches(" ")){
                    temp = transferRegions.get(siloToGrab).right;
                    transferRegions.get(siloToGrab).right = " ";
                }
            }
            case "RIGHT" -> {
                if (!transferRegions.get(siloToGrab).left.matches(" ")){
                    temp = transferRegions.get(siloToGrab).left;
                    transferRegions.get(siloToGrab).left = " ";
                }
            }
        }

        System.out.println("Adding value: " + temp + " to output");
        if (temp != null){
            outputNumbers.add(temp);
        }
    }

    static void addonCount(Integer a){
        number++;
        lineNumber++;
    }

    LinkedList<Input> sendInputList(){
        return inputList;
    }

    LinkedList<String> sendOutputList(){
        return outputNumbers;
    }

}
