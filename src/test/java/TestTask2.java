import org.junit.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import static org.junit.Assert.assertEquals;

public class TestTask2 {

    Path writeFilePath = Paths.get("task3\\task2.properties");

    @Test
    public void Test1(){
        Class<SomeClass>class1 = SomeClass.class;
        try {
           SomeClass testClass = Task2.loadFromProperties(class1,writeFilePath);
           assertEquals(testClass.getStringProperty(),"value1");
           assertEquals(testClass.getMyNumber(),10);

        } catch (IOException | ClassNotFoundException | NoSuchMethodException |
                InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void Test2(){
        Class<Test1> test1Class = Test1.class;
        try {
           Task2.loadFromProperties(test1Class,writeFilePath);
        } catch (IOException | ClassNotFoundException | InstantiationException |
                NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
class Test1{

    @SomeClass.Property(name = "error")
    public String stringProperty;

    @SomeClass.Property(name = "numberProperty",format = "3452")
    public int myNumber;

    @SomeClass.Property(name = "timeProperty", format = "dd.MM.yyyy HH:mm")
    public Instant timeProperty;
}