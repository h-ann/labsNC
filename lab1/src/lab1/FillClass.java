package lab1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Annotation for filler classes
 * @author Anna Hulita
 * @version 1.0
 */
@Target(value= ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

public @interface FillClass {
}
