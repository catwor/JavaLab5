package org.example;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, используемая для автоматической инъекции зависимостей в поля класса.
 * Поля, помеченные этой аннотацией, будут внедряться с помощью DependencyInjector.
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface AutoInjectable {
}
