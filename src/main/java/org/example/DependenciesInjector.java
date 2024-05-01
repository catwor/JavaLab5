package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Класс для внедрения зависимостей в поля объектов, помеченные аннотацией @AutoInjectable.
 */
public class DependenciesInjector {
  static Properties properties = new Properties();

  /**
   * Статический блок инициализации, загружающий свойства из файла dependencies.properties.
   */
  static {
    try (InputStream inputStream = DependenciesInjector.class.getResourceAsStream("/dependencies.properties")) {
      properties.load(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Метод для внедрения зависимостей в поля объекта.
   *
   * @param instance объект, в который нужно внедрить зависимости
   * @param <T> тип объекта
   * @return тот же самый объект с внедренными зависимостями
   */
  public <T> T inject(T instance) {
    Field[] fields = instance.getClass().getDeclaredFields();

    for (Field field : fields) {
      if (field.isAnnotationPresent(AutoInjectable.class)) {
        String dependencyClassName = properties.getProperty(field.getType().getName());
        if (dependencyClassName != null) {
          try {
            Class<?> dependencyClass = Class.forName(dependencyClassName);
            Object dependencyInstance = dependencyClass.getDeclaredConstructor().newInstance();
            field.setAccessible(true);
            field.set(instance, dependencyInstance);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }
}