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
                    //siloRunnable.put(count,codeToRunnable(siloCode,count));
                    siloList.get(count).addRunnableList(codeToRunnable(siloCode,count));
//                    //Test
//                    List<Runnable> methods = new ArrayList<>();
//                    int finalCount = count;
//                    methods.add(() -> siloList.get(finalCount).parserTest());
//                    methods.add(() -> siloList.get(finalCount).parserTest2());
//                    System.out.println(methods);
//                    siloRunnable.put(count,new ArrayList<Runnable>(methods));
//                    //Test above
                    //methods.clear();
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
            System.out.println("Input being added to silo: " + inputCol + " input Direction: " + inputDirection);
        }
        else if (inputCol < 0 && inputRow >= 0){
            siloList.get((totalCols * (inputRow))).trLeft = transferRegions.get((totalCols * totalRows));
            inputDirection = "RIGHT";
            System.out.println("Input being added to silo: " + (totalCols * (inputRow)) + " input Direction: " + inputDirection);
        }
        else if (inputRow >= totalRows && inputCol < totalCols){
            siloList.get((totalCols * (inputRow - 1)) + inputCol).trDown = transferRegions.get((totalCols * totalRows));
            inputDirection = "UP";
            System.out.println("Input being added to silo: " + ((totalCols * (inputRow - 1)) + inputCol) + " input Direction: " + inputDirection);
        }
        else if (inputCol >= totalCols && inputRow < totalRows){
            siloList.get((inputRow * totalCols) + (inputCol - 1)).trRight = transferRegions.get((totalCols * totalRows));
            inputDirection = "LEFT";
            System.out.println("Input being added to silo: " + ((inputRow * totalCols) + (inputCol - 1)) + " input Direction: " + inputDirection);
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

    static void addonCount(Integer a){
        number++;
        lineNumber++;
    }

    LinkedList<String> sendInputList(){
        return initialInputNumbers;
    }

    LinkedList<String> sendOutputList(){
        return outputNumbers;
    }

}
