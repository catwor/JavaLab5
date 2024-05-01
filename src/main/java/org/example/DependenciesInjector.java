package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

public class DependenciesInjector {
  static Properties properties = new Properties();

  static {
    try (InputStream inputStream = DependenciesInjector.class.getResourceAsStream("/dependencies.properties")) {
      properties.load(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

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
