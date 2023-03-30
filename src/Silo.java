import java.util.ArrayList;
import java.util.LinkedList;

public class Silo {
    private LinkedList<String> siloCode = new LinkedList<String>();
    private Integer accValue;
    private Integer bakValue;
    private Integer siloNumber;
    private Integer siloLineNumber;
    private Integer upValue, downValue, leftValue, rightValue;

    Silo(LinkedList data, Integer silo){
        this.siloCode = data;
        this.accValue = 0;
        this.bakValue = 0;
        this.siloNumber = silo;
        this.siloLineNumber = 0;
        this.upValue = null;
        this.downValue = null;
        this.leftValue = null;
        this.rightValue = null;
    }

    void runLine(Integer lineNumber){

    }

    Integer returnLineNumber(){
        return this.siloLineNumber;
    }

    String returnLineData(Integer lineNumber){
        return this.siloCode.get(lineNumber);
    }

    LinkedList returnAllData(){

        return null;
    }

    void label(String string){

    }

    void noop(){

    }

    void move(String source, String destination, Silo s){

    }

    Integer getPort(){

        return null;
    }

    void swap(){

    }

    void save(){

    }

    void add(Integer sourceValue, String source){

    }

    void sub(Integer sourceValue, String source){

    }

    void negate(){

    }

    void jump(String label){

    }

    void jez(String label){

    }

    void jnz(String label){

    }

    void jgz(String label){

    }

    void jlz(String label){

    }

    void jro(Integer sourceValue){

    }

}
