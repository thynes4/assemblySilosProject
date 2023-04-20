import java.util.*;

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
    private static int count = 0;
    private static int currentInputTotal = 0;
    private Integer totalInputs = 0;
    private LinkedList<TransferRegion> transferRegions = new LinkedList<>();
    private boolean silofinished = false;
    private boolean inputfinished = false;


    /**
     * This is the Parser Class
     * @param input This is the command line input from the User in List form
     * @param transferRegions This is the list of all transfer regions
     * @param totalInputs This is the total number of input regions
     */
    Parser(LinkedList input, LinkedList<TransferRegion> transferRegions, Integer totalInputs){
        this.commandInput = input;
        this.transferRegions = transferRegions;
        this.totalInputs = totalInputs;

        //This is to grab the total rows and columns first
        String temp1 = commandInput.get(0);
        String[] temp2 = temp1.split(" ");
        totalRows = Integer.valueOf(temp2[0]);
        totalCols = Integer.valueOf(temp2[1]);
        commandInput.remove(0);

        this.parserCommandText();
        this.finalParse();

    }

    /**
     * This parses the list and create the silos
     */
    void finalParse(){
        LinkedList<String> siloCode = new LinkedList<>();
        for (String s: commandInput) {
            if (!silofinished){
                if (s.equals("INPUT")){
                    currentInputTotal++;
                    silofinished = true;
                }
                else if (!s.equals("END")){
                    siloCode.add(s);
                }
                else {
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
            outputList.get(i).outputTransferRegion(transferRegions,siloList,totalCols,totalRows, totalInputs, i);
        }
    }

    /**
     * This will parse the command text entered and check for any errors initially.
     * @return Syntax Error Count
     */
    Integer parserCommandText(){
        int syntaxErrorCount = 0;
        int currentLine = 0;
        LinkedList<String> tempList = new LinkedList<>();
        tempList.addAll(commandInput);
        //Checked for Syntax Errors
        for (String s : commandInput){
            String temp5 = s;
            String[] temp6 = temp5.split(" ");
            switch (temp6[0]){
                case "NOOP" -> {
                    if (temp6.length > 1){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                }
                case "MOVE" -> {
                    if (temp6.length < 3){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                    if (!isInteger(temp6[1])){
                        for (int i = 1; i < 3; i++){
                            switch (temp6[i]){
                                case "UP", "NIL", "BAK", "ACC", "RIGHT", "LEFT", "DOWN" -> {}
                                default -> {
                                    tempList.remove(currentLine);
                                    tempList.add(currentLine,("ERROR" + s));
                                    syntaxErrorCount++;
                                }
                            }
                        }
                    }
                }
                case "SWAP" -> {
                    if (temp6.length > 1){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                }
                case "SAVE" -> {
                    if (temp6.length > 1){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }

                }
                case "ADD" -> {
                    if (temp6.length > 2){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                    //Add code to check if real number
                    if (!isInteger(temp6[1])) {
                        switch (temp6[1]) {
                            case "UP", "NIL", "BAK", "ACC", "RIGHT", "LEFT", "DOWN" -> {
                            }
                            default -> {
                                tempList.remove(currentLine);
                                tempList.add(currentLine,("ERROR" + s));
                                syntaxErrorCount++;
                            }
                        }
                    }
                }
                case "SUB" -> {
                    if (temp6.length > 2){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                    //Checks to see if it is an integer
                    if (!isInteger(temp6[1])) {
                        switch (temp6[1]) {
                            case "UP", "NIL", "BAK", "ACC", "RIGHT", "LEFT", "DOWN" -> {
                            }
                            default -> {
                                tempList.remove(currentLine);
                                tempList.add(currentLine,("ERROR" + s));
                                syntaxErrorCount++;
                            }
                        }
                    }
                }
                case "NEGATE" -> {
                    if (temp6.length > 1){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                }
                case "JUMP" -> {
                    if (temp6.length > 2){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                    if (!commandInput.contains(":" + temp6[1] + ":")){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                }
                case "JEZ" -> {
                    if (temp6.length > 2){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                    if (!commandInput.contains(":" + temp6[1] + ":")){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                }
                case "JNZ" -> {
                    if (temp6.length > 2){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                    if (!commandInput.contains(":" + temp6[1] + ":")){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                }
                case "JGZ" -> {
                    if (temp6.length > 2){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                    if (!commandInput.contains(":" + temp6[1] + ":")){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                }
                case "JLZ" -> {
                    if (temp6.length > 2){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                    if (!commandInput.contains(":" + temp6[1] + ":")){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                }
                case "JRO" -> {
                    if (temp6.length > 2){
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                    if (isInteger(temp6[1])) {
                        switch (temp6[1]) {
                            case "UP", "NIL", "BAK", "ACC", "RIGHT", "LEFT", "DOWN" -> {
                            }
                            default -> {
                                tempList.remove(currentLine);
                                tempList.add(currentLine,("ERROR" + s));
                                syntaxErrorCount++;
                            }
                        }
                    }
                }
                case "END" -> {
                    if (temp6.length > 1){
                        System.out.println("Syntax Error with END on Line: " + currentLine);
                        System.out.println("More statements than allowed. Syntax: END");
                        syntaxErrorCount++;
                    }
                }
                case "INPUT" -> {
                    if (temp6.length > 1){
                        System.out.println("Syntax Error with INPUT on Line: " + currentLine);
                        System.out.println("More statements than allowed. Syntax: INPUT");
                        syntaxErrorCount++;
                    }
                }
                case "OUTPUT" -> {
                    if (temp6.length > 1){
                        System.out.println("Syntax Error with OUTPUT on Line: " + currentLine);
                        System.out.println("More statements than allowed. Syntax: OUTPUT");
                        syntaxErrorCount++;
                    }
                }
                default -> {
                    if (isInteger(temp6[0])) {
                        if (temp6.length == 1){

                        }
                        else if (temp6.length != 2) {
                            System.out.println("Syntax Error with on Line: " + currentLine);
                            System.out.println("More statements than allowed. Syntax: [ROW] [COL]");
                            syntaxErrorCount++;
                        }
                        else {
                            if (!isInteger(temp6[1])){
                                System.out.println("Syntax Error with on Line: " + currentLine);
                                System.out.println("More statements than allowed. Syntax: [ROW] [COL]");
                                System.out.println("ROW and COL should be real numbers");
                                syntaxErrorCount++;
                            }
                        }
                    }
                    else {
                        tempList.remove(currentLine);
                        tempList.add(currentLine,("ERROR" + s));
                        syntaxErrorCount++;
                    }
                }
            }
            currentLine++;
        }
        commandInput.clear();
        commandInput.addAll(tempList);
        return syntaxErrorCount;
    }

    /**
     * This is just to check the updated silo code in the Silo TextFields
     * @param siloList This is the list of silos
     * @return The amount of errors. 0 If none
     */
    Integer parseSiloErrors(LinkedList<Silo> siloList){
        int syntaxErrorCount = 0;
        int currentLine = 0;
        for (Silo silo : siloList){
            LinkedList<String> tempList = new LinkedList<>();
            tempList.addAll(silo.siloCode);
            for (String s : silo.siloCode){
                String temp5 = s;
                String[] temp6 = temp5.split(" ");
                switch (temp6[0]){
                    case "NOOP" -> {
                        if (temp6.length > 1){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                        }
                    }
                    case "MOVE" -> {
                        if (temp6.length < 3){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                        }
                        if (!isInteger(temp6[1])){
                            for (int i = 1; i < 3; i++){
                                switch (temp6[i]){
                                    case "UP", "NIL", "BAK", "ACC", "RIGHT", "LEFT", "DOWN" -> {}
                                    default -> {
                                        tempList.remove(currentLine);
                                        tempList.add(currentLine,("ERROR" + s));
                                        syntaxErrorCount++;
                                    }
                                }
                            }
                        }
                    }
                    case "SWAP" -> {
                        if (temp6.length > 1){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }
                    }
                    case "SAVE" -> {
                        if (temp6.length > 1){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }

                    }
                    case "ADD" -> {
                        if (temp6.length > 2){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }
                        //Add code to check if real number
                        if (!isInteger(temp6[1])) {
                            switch (temp6[1]) {
                                case "UP", "NIL", "BAK", "ACC", "RIGHT", "LEFT", "DOWN" -> {
                                }
                                default -> {
                                    tempList.remove(currentLine);
                                    tempList.add(currentLine,("ERROR" + s));
                                    syntaxErrorCount++;
                                }
                            }
                        }
                    }
                    case "SUB" -> {
                        if (temp6.length > 2){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }
                        //Checks to see if it is an integer
                        if (!isInteger(temp6[1])) {
                            switch (temp6[1]) {
                                case "UP", "NIL", "BAK", "ACC", "RIGHT", "LEFT", "DOWN" -> {
                                }
                                default -> {
                                    tempList.remove(currentLine);
                                    tempList.add(currentLine,("ERROR" + s));
                                    syntaxErrorCount++;
                                }
                            }
                        }
                    }
                    case "NEGATE" -> {
                        if (temp6.length > 1){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }
                    }
                    case "JUMP" -> {
                        if (temp6.length > 2){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }
                        if (!commandInput.contains(":" + temp6[1] + ":")){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }
                    }
                    case "JEZ" -> {
                        if (temp6.length > 2){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }
                        if (!commandInput.contains(":" + temp6[1] + ":")){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }
                    }
                    case "JNZ" -> {
                        if (temp6.length > 2){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }
                        if (!commandInput.contains(":" + temp6[1] + ":")){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }
                    }
                    case "JGZ" -> {
                        if (temp6.length > 2){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }
                        if (!commandInput.contains(":" + temp6[1] + ":")){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }
                    }
                    case "JLZ" -> {
                        if (temp6.length > 2){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }
                        if (!commandInput.contains(":" + temp6[1] + ":")){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }
                    }
                    case "JRO" -> {
                        if (temp6.length > 2){
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }
                        if (isInteger(temp6[1])) {
                            switch (temp6[1]) {
                                case "UP", "NIL", "BAK", "ACC", "RIGHT", "LEFT", "DOWN" -> {
                                }
                                default -> {
                                    tempList.remove(currentLine);
                                    tempList.add(currentLine,("ERROR" + s));
                                    syntaxErrorCount++;
                                }
                            }
                        }
                    }
                    case "END" -> {
                        if (temp6.length > 1){
                            System.out.println("Syntax Error with END on Line: " + currentLine);
                            System.out.println("More statements than allowed. Syntax: END");
                            syntaxErrorCount++;
                        }
                    }
                    case "INPUT" -> {
                        if (temp6.length > 1){
                            System.out.println("Syntax Error with INPUT on Line: " + currentLine);
                            System.out.println("More statements than allowed. Syntax: INPUT");
                            syntaxErrorCount++;
                        }
                    }
                    case "OUTPUT" -> {
                        if (temp6.length > 1){
                            System.out.println("Syntax Error with OUTPUT on Line: " + currentLine);
                            System.out.println("More statements than allowed. Syntax: OUTPUT");
                            syntaxErrorCount++;
                        }
                    }
                    default -> {
                        if (isInteger(temp6[0])) {
                            if (temp6.length == 1){

                            }
                            else if (temp6.length != 2) {
                                System.out.println("Syntax Error with on Line: " + currentLine);
                                System.out.println("More statements than allowed. Syntax: [ROW] [COL]");
                                syntaxErrorCount++;
                            }
                            else {
                                if (!isInteger(temp6[1])){
                                    System.out.println("Syntax Error with on Line: " + currentLine);
                                    System.out.println("More statements than allowed. Syntax: [ROW] [COL]");
                                    System.out.println("ROW and COL should be real numbers");
                                    syntaxErrorCount++;
                                }
                            }
                        }
                        else {
                            tempList.remove(currentLine);
                            tempList.add(currentLine,("ERROR" + s));
                            syntaxErrorCount++;
                        }
                    }
                }
                currentLine++;
            }
            silo.siloCode.clear();
            silo.siloCode.addAll(tempList);
            tempList.clear();
            currentLine = 0;
        }
        return syntaxErrorCount;
    }

    /**
     * This takes the Silo Code and changes it to a list of runnables
     * @param code This is the Silo Code
     * @param siloNum This is the Silo Number
     * @return The list of runnables made from the silo code
     */
    List<Runnable> codeToRunnable(LinkedList<String> code, Integer siloNum){
        List<Runnable> methods = new ArrayList<>();
        for (String s : code) {
            String temp1 = s;
            String[] temp2 = s.split(" ");
            switch (temp2[0]){
                case "NOOP" -> {
                    methods.add(() -> siloList.get(siloNum).noop());
                }
                case "MOVE" -> {
                    methods.add(() -> siloList.get(siloNum).move(temp2[1],temp2[2]));
                }
                case "SWAP" -> {
                    methods.add(() -> siloList.get(siloNum).swap());
                }
                case "SAVE" -> {
                    methods.add(() -> siloList.get(siloNum).save());
                }
                case "ADD" -> {
                    methods.add(() -> siloList.get(siloNum).add(temp2[1]));
                }
                case "SUB" -> {
                    methods.add(() -> siloList.get(siloNum).sub(temp2[1]));
                }
                case "NEGATE" -> {
                    methods.add(() -> siloList.get(siloNum).negate());
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
                }
                case "JEZ" -> {
                    methods.add(() -> siloList.get(siloNum).jez(temp2[1]));
                }
                case "JNZ" -> {
                    methods.add(() -> siloList.get(siloNum).jnz(temp2[1]));
                }
                case "JGZ" -> {
                    methods.add(() -> siloList.get(siloNum).jgz(temp2[1]));
                }
                case "JLZ" -> {
                    methods.add(() -> siloList.get(siloNum).jlz(temp2[1]));
                }
                case "JRO" -> {
                    methods.add(() -> siloList.get(siloNum).jro(temp2[1]));
                }
            }
        }
        return methods;
    }

    /**
     * This is to get the output arrow direction
     * @return the output arrow direction in string form
     */
    String getOutputDirection(){
        return outputDirection;
    }

    /**
     * This is to get the input arrow direction
     * @return The input arrow direction in string form
     */
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

    static void addonCount(Integer a){
        number++;
        lineNumber++;
    }

    /**
     * This is to send the list of input values
     * @return The input values
     */
    LinkedList<Input> sendInputList(){
        return inputList;
    }

    /**
     * This is to send the list of output values
     * @return The output values
     */
    LinkedList<Output> sendOutputList(){
        return outputList;
    }

    /**
     * To check if the string is a Integer
     * @param string The string being checked
     * @return True if Integer, False if not
     */
    Boolean isInteger(String string){
        if (string == null){
            return false;
        }
        if(string.length() == 0){
            return false;
        }
        int i = 0;
        if (string.charAt(0) == '-'){
            if (string.length() == 1){
                return false;
            }
            //Increment if first is a '-'
            i = 1;
        }
        for (; i < string.length(); i++){
            char c = string.charAt(i);
            if (c < '0' || c > '9'){
                return false;
            }
        }
        return true;
    }

}
