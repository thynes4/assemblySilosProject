import java.util.ArrayList;
import java.util.LinkedList;

public class Silo {
    private LinkedList<String> siloCode = new LinkedList<String>();
    private Integer accValue;
    private Integer bakValue;
    private Integer siloNumber;
    private Integer siloLineNumber;
    private Integer upValue, downValue, leftValue, rightValue;

    /**
     * Initialize the Silo with data from the Manager. Then assign it all to specific silo
     * to be grabbed at future times.
     * @param data This is the Linked List of data for this silo.
     * @param siloNum This is to tell the silo which number it is (May not be needed)
     */
    Silo(LinkedList data, Integer siloNum){
        this.siloCode = data;
        this.accValue = 0;
        this.bakValue = 0;
        this.siloNumber = siloNum;
        this.siloLineNumber = 0;
        this.upValue = null;
        this.downValue = null;
        this.leftValue = null;
        this.rightValue = null;
    }

    /**
     * This will run the line of code for the silo.
     * @param lineNumber This is the current line number of the silo.
     */
    void runLine(Integer lineNumber){
        String temp = returnLineData(lineNumber);
        //Check if its a label
        if (temp.contains(":")){

        }
        else {
            //Split string then use a switch

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
        //Build all data into a Linked List to return
        return null;
    }

    /**
     * This is if the input is a Label and to check and advance the code line to the label
     * @param string This is the string contained within the ':'
     */
    void label(String string){

    }

    /**
     * This is an instruction that does nothing except advance the line count
     */
    void noop(){

    }

    /**
     * This is to move a value from the Source to the destination
     * @param source This is the source where the value is being grabbed or the initial value itself
     * @param destination This is where the value is being moved to
     * @param s This is the silo the value may be moved to.
     */
    void move(String source, String destination, Silo s){

    }

    /**
     * This is to switch the value of the ACC register with the value of the BAK register
     */
    void swap(){

    }

    /**
     * This is to write the value from the ACC register onto the BAK register
     */
    void save(){

    }

    /**
     * This is to add a value to the ACC register
     * @param source This is the value being added to ACC. If a literal value then cast to Integer.
     */
    void add(String source){

    }

    /**
     * This is to subtract a value from the ACC register.
     * @param source This is the value being subtracted to ACC. If a literal value then cast to Integer.
     */
    void sub(String source){

    }

    /**
     * The value of the register ACC is negated, zero remains zero
     */
    void negate(){

    }

    /**
     * This is to jump control of the program to the instruction following a given label
     * @param label The label being jumped to
     */
    void jump(String label){

    }

    /**
     * To jump control of the program to the instruction following the given label if
     * the value in the register ACC is equal to zero
     * @param label The label being designated
     */
    void jez(String label){

    }

    /**
     * To jump control of the program to the instruction following the given label if
     * the value in the register ACC is not equal to zero
     * @param label The label being designated
     */
    void jnz(String label){

    }


    /**
     * To jump control of the program to the instruction following the given label if
     * the value in the register ACC is greater than zero
     * @param label The label being designated
     */
    void jgz(String label){

    }


    /**
     * To jump control of the program to the instruction following the given label if
     * the value in the register ACC is less than zero
     * @param label The label being designated
     */
    void jlz(String label){

    }

    /**
     * This is to jump control of the program to the instruction specified by the offset which is the
     * value contained within source.
     * if the offset is greater than the size of the program then you should wrap around to the beginning
     * of the program until you get to an offset which is contained within the program. (Use Modulo)
     * @param source This is the value. If literal value then cast to Integer.
     */
    void jro(String source){

    }

}
