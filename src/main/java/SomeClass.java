import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.Instant;

public class SomeClass<T> {
    @Property(name = "stringProperty")
    public String stringProperty;

    @Property(name = "numberProperty")
    public int myNumber;

    @Property(name = "timeProperty", format = "dd.MM.yyyy tt:mm")
    public Instant timeProperty;

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Property {
        String name() default "";

        String format() default "";
    }

    public String getStringProperty() {
        return stringProperty;
    }

    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }

    public int getMyNumber() {
        return myNumber;
    }

    public void setMyNumber(int myNumber) {
        this.myNumber = myNumber;
    }

    public Instant getTimeProperty() {
        return timeProperty;
    }

    public void setTimeProperty(Instant timeProperty) {
        this.timeProperty = timeProperty;
    }
}
