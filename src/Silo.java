import java.util.ArrayList;
import java.util.LinkedList;

public class Silo {
    private LinkedList<String> siloCode = new LinkedList<String>();

    Silo(LinkedList data){
        this.siloCode = data;
    }

    void runLine(Integer lineNumber, Silo s){

    }

    Integer returnLineNumber(Silo s){

        return -1;
    }

    String returnLineData(Integer lineNumber, Silo s){
        return s.siloCode.get(lineNumber);
    }

    LinkedList returnAllData(Silo s){

        return null;
    }

    void label(String string){

    }

    void noop(){

    }

    void move(String source, String destination){

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

    void jez(Silo s, String label){

    }

    void jnz(Silo s, String label){

    }

    void jgz(Silo s, String label){

    }

    void jlz(Silo s, String label){

    }

    void jro(Silo s, Integer sourceValue){

    }

}
