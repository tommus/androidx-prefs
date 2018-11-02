package co.windly.androidxprefs.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Prefs {

  String value() default "";

  String fileName() default "";

  int fileMode() default -1;

  boolean disableNullable() default false;
}
