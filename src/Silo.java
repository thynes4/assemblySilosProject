/**
 * Sean Davies, Thomas Hynes, Christopher Jarek
 * Project 4 Assembly Silos
 * This is to build Assembly Silos that take in their own code. An input is
 * sent in and the silos process their code together 1 line at a time (until all silos are
 * done with that line) then continue on until an output is achieved.
 *
 * This file is the Silo Object Class
 */


import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.util.LinkedList;
import static java.lang.Math.floor;

public class Silo {
    private LinkedList<String> siloCode = new LinkedList<>();
    private Integer accValue;
    private Integer bakValue;
    private Integer siloNumber;
    private Integer siloLineNumber;
    protected TransferRegion tr, trUp, trLeft, trRight, trDown;

    private Label siloNumLabel = new Label();
    private Label accLabel = new Label();
    private Label bakLabel = new Label();
    private Label pointerLabel = new Label();
    private TextArea codeArea = new TextArea();


    /**
     * Initialize the Silo with data from the Manager. Then assign it all to specific silo
     * to be grabbed at future times.
     * //@param data This is the Linked List of data for this silo.
     * @param siloNum This is to tell the silo which number it is (May not be needed)
     */
    Silo(Integer siloNum, LinkedList<String> code, TransferRegion self, TransferRegion up,
         TransferRegion left, TransferRegion right, TransferRegion down){
        this.siloCode.addAll(code);
        this.accValue = 0;
        this.bakValue = 0;
        this.siloNumber = siloNum;
        this.siloLineNumber = 0;

        this.tr = self;
        this.trUp = up;
        this.trLeft = left;
        this.trRight = right;
        this.trDown = down;
    }

    //Testing Purposes
    Silo(Integer siloNum){
        this.siloCode = new LinkedList<String>();
        this.accValue = 0;
        this.bakValue = 0;
        this.siloNumber = siloNum;
        this.siloLineNumber = 0;
    }

    LinkedList<String> getCode() {
        return siloCode;
    }

    /**
     * Refreshes the fx visuals for the given silo
     */
    void refreshFX(){
        //Refreshes the data column
        this.siloNumLabel.setText("num = " + this.siloNumber);
        this.accLabel.setText("acc = " + Integer.toString(accValue));
        this.bakLabel.setText("bak = " + Integer.toString(bakValue));

        //Refreshes the codeArea, not necessary unless automatically pushing code into silos
        /*
        String txt = "";
        for(String line : siloCode){
            txt = txt + line + "\n";
        }
        codeArea.setText(txt);
        */

        //Sets the line pointer to the correct spot
        String ptrTxt = "";
        for(int i = 0; i < siloLineNumber -1; i++){
            ptrTxt = ptrTxt + "\n";
        }
        ptrTxt = ptrTxt + "➡";
        pointerLabel.setText(ptrTxt);
    }

    /**
     * Creates and returns the Node data used to represent a Silo in the UI
     * @return the Node used to represent a silo
     */
    Node getNode(){
        BorderPane innerPane = new BorderPane();

        // Creating the DataColumn within innerPane
        // siloNumLabel, accLabel, bakLabel
        VBox dataCol = new VBox();
        innerPane.setRight(dataCol);
        dataCol.getChildren().addAll(siloNumLabel, accLabel, bakLabel);

        // Creating the PointerColumn within innerPane
        // pointerLabel
        innerPane.setLeft(pointerLabel);

        // Creating the TextField within innerPane
        // codeArea
        innerPane.setCenter(codeArea);
        codeArea.setPrefSize(250, 250);
        String temp = "";
        System.out.println(this.siloCode);
        for (String s : this.siloCode){
            temp = temp + s + "\n";
        }
        codeArea.setText(temp);

        codeArea.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ENTER){
                siloLineNumber = 0;

                String[] codeArr = codeArea.getText().split("\n");
                siloCode.clear();
                for(String line : codeArr) {
                    siloCode.add(line);
                }

                System.out.println("Silo code was updated to: " + siloCode);

                this.refreshFX();
                //System.out.println(siloCode);
            }
        });

        refreshFX();
        return innerPane;
    }

    /**
     * Note: this is only useful for a 2x2 group of silos currently.
     * @return the x position of a given silo in a 2x2 group
     */
    int getPosX(int totalColumns){
        return 1+ (int) floor(siloNumber/totalColumns);
    }

    /**
     * Note: this is only useful for a 2x2 group of silos currently.
     * @return the y position of a given silo in a 2x2 group
     **/
    int getPosY(int totalColumns){
        return (siloNumber%totalColumns);
    }

    /**
     * Returns the assigned silo number
     * @return siloNumber
     */
    int getSiloNum(){return this.siloNumber;}

    void setSiloCode(String input){
        String[] temp = input.split("\n");

        for(String line : temp){
            siloCode.add(line);
        }
        refreshFX();
    }

    /**
     * This will run the line of code for the silo.
     * @param lineNumber This is the current line number of the silo.
     */
    void runLine(Integer lineNumber){
        String temp1 = returnLineData(lineNumber);
        //Check if it is a label
        if (temp1.contains(":")){
            runLine(lineNumber + 1);
        }
        else {
            String[] temp2 = temp1.split(" ");
            switch (temp2[0]) {
                case "NOOP" -> {
                    this.incrementLineNumber();
                }
                case "MOVE" -> {
                    //Needs work as transfer region is no longer part of Silo
                    this.move(temp2[1],temp2[2]);
                }
                case "SWAP" -> {
                    this.swap();
                }
                case "SAVE" -> {
                    this.save();
                }
                case "ADD" -> {
                    this.add(temp2[1]);
                }
                case "SUB" -> {
                    this.sub(temp2[1]);
                }
                case "NEGATE" -> {
                    this.negate();
                }
                case "JUMP" -> {
                    this.jump(temp2[1]);
                }
                case "JEZ" -> {
                    this.jez(temp2[1]);
                }
                case "JNZ" -> {
                    this.jnz(temp2[1]);
                }
                case "JGZ" -> {
                    this.jgz(temp2[1]);
                }
                case "JLZ" -> {
                    this.jlz(temp2[1]);
                }
                case "JRO" -> {
                    this.jro(temp2[1]);
                }
            }
        }
    }

    /**
     * This is to return the current line number of the silo.
     * @return The current line number of the silo code
     */
    Integer returnLineNumber(){
        return this.siloLineNumber;
    }

    /**
     * This is to increment the line number by 1.
     */
    void incrementLineNumber(){
        this.siloLineNumber++;
    }

    /**
     * This is to return the data at that specific line number in the code.
     * @param lineNumber The line number of code being requested
     * @return The string data from the line
     */
    String returnLineData(Integer lineNumber){
        return this.siloCode.get(lineNumber);
    }

    /**
     * This is to return all data of the silo in a Linked List. Main objective is to make a linked list
     * that the GUI can receive through the manager to display the silo properly with all data.
     * Line 1: ACC value
     * Line 2: BAK value
     * Line 3: Current Line Number
     * Line 4: Up port value
     * Line 5: Down port value
     * Line 6: Left port value
     * Line 7: Right port value
     * Line 8+: Code of Silo
     * @return This is a linked list described above
     */
    LinkedList returnAllData(){
        //I've filled this out temporarily for testing - Chris
        //Build all data into a Linked List to return
        LinkedList<Integer> data = new LinkedList<>();
        data.add(siloNumber);
        //data.add(this.getPosX());
        //data.add(this.getPosY());

        return data;
    }

    /**
     * This is to move a value from the Source to the destination
     * @param source This is the source where the value is being grabbed or the initial value itself
     * @param destination This is where the value is being moved to
     */
    void move(String source, String destination){
        Integer temp = 0;
        String temp2 = " ";
        //Need to add a portion where if Source is another transfer region
        switch (source) {
            case "UP" -> temp2 = this.trUp.getDown();
            case "DOWN" -> temp2 = this.trDown.getUp();
            case "LEFT" -> temp2 = this.trLeft.getRight();
            case "RIGHT" -> temp2 = this.trRight.getLeft();
            case "ACC" -> temp = this.accValue;
            case "NIL" -> temp = 0;
            case "BAK" -> temp = this.bakValue;
            default -> temp = sourceToInteger(source);
        }

        switch (destination) {
            case "UP" -> this.tr.addUp(temp2);
            case "DOWN" -> this.tr.addDown(temp2);
            case "LEFT" -> this.tr.addLeft(temp2);
            case "RIGHT" -> this.tr.addRight(temp2);
            case "ACC" -> this.accValue = temp;
        }
    }

    /**
     * This is to switch the value of the ACC register with the value of the BAK register
     */
    void swap() {
        int temp = accValue;
        accValue = bakValue;
        bakValue = temp;
    }

    /**
     * This is to write the value from the ACC register onto the BAK register
     */
    void save(){
        bakValue = accValue;
    }

    /**
     * This is to add a value to the ACC register
     * @param source This is the value being added to ACC. If a literal value then cast to Integer.
     */
    void add(String source) {
        accValue += sourceToInteger(source);
    }

    /**
     * This is to subtract a value from the ACC register.
     * @param source This is the value being subtracted to ACC. If a literal value then cast to Integer.
     */
    void sub(String source){
        accValue -= sourceToInteger(source);
    }

    /**
     * The value of the register ACC is negated, zero remains zero
     */
    void negate(){
        accValue *= -1;
    }

    /**
     * This is to jump control of the program to the instruction following a given label
     * @param label The label being jumped to
     */
    void jump(String label) {
        for (String s : siloCode) {
            if (s.contains(":" + label + ":")) {
                siloLineNumber = siloCode.indexOf(s);
            }
        }
    }

    /**
     * To jump control of the program to the instruction following the given label if
     * the value in the register ACC is equal to zero
     * @param label The label being designated
     */
    void jez(String label){
        if (accValue == 0) {
            jump(label);
        }
    }

    /**
     * To jump control of the program to the instruction following the given label if
     * the value in the register ACC is not equal to zero
     * @param label The label being designated
     */
    void jnz(String label){
        if (accValue != 0) {
            jump(label);
        }
    }


    /**
     * To jump control of the program to the instruction following the given label if
     * the value in the register ACC is greater than zero
     * @param label The label being designated
     */
    void jgz(String label){
        if (accValue > 0) {
            jump(label);
        }
    }


    /**
     * To jump control of the program to the instruction following the given label if
     * the value in the register ACC is less than zero
     * @param label The label being designated
     */
    void jlz (String label) {
        if (accValue < 0) {
            jump(label);
        }
    }

    /**
     * This is to jump control of the program to the instruction specified by the offset which is the
     * value contained within source.
     * if the offset is greater than the size of the program then you should wrap around to the beginning
     * of the program until you get to an offset which is contained within the program. (Use Modulo)
     * @param source This is the value. If literal value then cast to Integer.
     */
    void jro (String source) {
        Integer temp = sourceToInteger(source);
        siloLineNumber += temp % siloCode.size();
    }

    /**
     * Given a source, either a register or a String of an int, will return the Integer value of the source or the
     * Integer value of the string.
     * @param source either a register or an Integer
     * @return Integer of value stored in register, if the string is a number will return the value of the number
     */
    private Integer sourceToInteger (String source) {
        return switch (source) {
            //Commenting out most currently as it is currently not needed due to Silo not containing
            //The transfer regions now
//            case "UP" -> upValue;
//            case "RIGHT" -> rightValue;
//            case "DOWN" -> downValue;
//            case "LEFT" -> leftValue;
//            case "NIL" -> 0;
            default -> Integer.valueOf(source);
        };
    }
}