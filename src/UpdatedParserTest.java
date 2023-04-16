import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UpdatedParserTest {
    private static Integer number = 0;
    private static Integer lineNumber = 0;

    public static void main(String[] args) {
        List<Runnable> methods = new ArrayList<>();
        System.out.println("Number is: " + number + " Line Number is: " + lineNumber);
        methods.add(() -> addonCount(1));
        methods.get(0).run();
        System.out.println("Number is: " + number + " Line Number is: " + lineNumber);

        while (lineNumber < 10){
            methods.get(0).run();
            System.out.println("Number is: " + number + " Line Number is: " + lineNumber);
        }
    }

    static void addonCount(Integer a){
        number++;
        lineNumber++;
    }

}
