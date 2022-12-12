import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;


public class Task2 {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {

        Path writeFilePath = Paths.get("task3\\task2.properties");
        Class<SomeClass> mClassObject = SomeClass.class;

        try {
            SomeClass cls = loadFromProperties(mClassObject, writeFilePath);
        } catch (IOException | ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public static <T> T loadFromProperties(Class<T> cls, Path propertiesPath) throws IOException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        T c = cls.newInstance();
        try (FileReader reader = new FileReader(String.valueOf(propertiesPath))) {

            BufferedReader read = new BufferedReader(reader);
            ArrayList<String> get = new ArrayList<>();
            String line = read.readLine();
            while (line != null) {
                String[] field = line.split("=");
                get.add(field[0]);
                get.add(field[1]);
                line = read.readLine();
            }
            for (Field m : cls.getDeclaredFields()) {
                if (m.isAnnotationPresent(SomeClass.Property.class)) {
                    SomeClass.Property l = m.getAnnotation(SomeClass.Property.class);
                    for (int i = 0; i < get.size(); i++) {
                        if (l.name().equals(get.get(i))) {
                            if(m.getType() == int.class){
                                m.set(c, Integer.parseInt(get.get(i + 1)));
                                System.out.println(m.getName() + " set value " + get.get(i + 1));
                            }else if(m.getType() == Instant.class){
                                DateTimeFormatter df = new DateTimeFormatterBuilder()
                                        .appendPattern(l.format())
                                        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                                        .toFormatter()
                                        .withZone(ZoneId.of("Europe/Paris"));
                                LocalDate date = LocalDate.parse(get.get(i + 1),df);
                                Instant instant = date.atStartOfDay(ZoneId.of("Europe/Paris")).toInstant();
                                m.set(c, instant);
                                System.out.println(m.getName() + " set value " + get.get(i + 1));
                            }else {
                                m.set(c, get.get(i + 1));
                                System.out.println(m.getName() + " set value " + get.get(i + 1));
                            }
                        }
                    }

                }else{
                    for (int i = 0; i < get.size(); i++) {
                        if (m.getName().equals(get.get(i))) {
                            if(m.getType() == int.class){
                                m.set(c, Integer.parseInt(get.get(i + 1)));
                                System.out.println(m.getName() + " set value " + get.get(i + 1));
                            }else if(m.getType() == Instant.class){
                                DateTimeFormatter df = new DateTimeFormatterBuilder()
                                        .appendPattern("dd.MM.yyyy HH:mm")
                                        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                                        .toFormatter()
                                        .withZone(ZoneId.of("Europe/Paris"));
                                LocalDate date = LocalDate.parse(get.get(i + 1),df);
                                Instant instant = date.atStartOfDay(ZoneId.of("Europe/Paris")).toInstant();
                                m.set(c, instant);
                                System.out.println(m.getName() + " set value " + get.get(i + 1));
                            }else {
                                m.set(c, get.get(i + 1));
                                System.out.println(m.getName() + " set value " + get.get(i + 1));
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
}

