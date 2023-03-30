import java.util.LinkedList;

public interface ManagerInterface {

    void initialData(LinkedList data);

    void initializeSilos();

    LinkedList returnSiloData(Silo s);

    void start();

    void stop();

    void pause();

    void step();
}
