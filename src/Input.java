import java.util.LinkedList;

public class Input {

    private Integer row, column;
    private LinkedList<String> initialInputs = new LinkedList();
    private LinkedList<String> currentInputs = new LinkedList<>();

    Input(LinkedList inputs, Integer row, Integer column){
        this.row = row;
        this.column = column;
        this.initialInputs.addAll(inputs);

    }
}
