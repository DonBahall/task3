import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Task2 {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {

        Path writeFilePath = Paths.get("C:\\Users\\Дима\\Downloads\\task3\\task2.properties");
        Class<SomeClass> mClassObject = SomeClass.class;
        try {
            loadFromProperties(mClassObject, writeFilePath);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static <T> T loadFromProperties(Class<T> cls, Path propertiesPath) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<String> names = new ArrayList<>();
        Object obj = Class.forName("SomeClass").newInstance();
         T c = cls.newInstance();

        List<Field> fields = Arrays.stream(SomeClass.class.getDeclaredFields())
                .toList();
        fields.forEach(f -> names.add(f.getName()));
        String file = Files.readString(propertiesPath);

        try (FileReader reader = new FileReader(String.valueOf(propertiesPath))) {
            BufferedReader read = new BufferedReader(reader);
            String line = read.readLine();
            while (line != null) {
                String[]field = line.split("=");
                for (Field m : cls.getDeclaredFields() ) {
                    if(m.isAnnotationPresent(SomeClass.Property.class)){
                        SomeClass.Property l = m.getAnnotation(SomeClass.Property.class);
                        if(l.name().equals(field[0])){
                            m.set(c,field[1]);
                            System.out.println("====");
                          //  Method  method
                           //         = cls.getMethod("set" + m.getName().substring(0,1).toUpperCase(Locale.ROOT)
                          //          + m.getName().substring(1),String.class);
                          // method.invoke(obj, Instant.class);
                        }
                    }else if(names.contains(field[0])){

                    }
                }
                line = read.readLine();
            }
        } catch ( IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) cls;
    }
}

