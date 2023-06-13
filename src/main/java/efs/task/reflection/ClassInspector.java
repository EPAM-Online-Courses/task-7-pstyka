package efs.task.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ClassInspector {

  /**
   * Metoda powinna wyszukać we wszystkich zadeklarowanych przez klasę polach te które oznaczone
   * są adnotacją podaną jako drugi parametr wywołania tej metody. Wynik powinien zawierać tylko
   * unikalne nazwy pól (bez powtórzeń).
   *
   * @param type       klasa (typ) poddawana analizie
   * @param annotation szukana adnotacja
   * @return lista zawierająca tylko unikalne nazwy pól oznaczonych adnotacją
   */
  public static Collection<String> getAnnotatedFields(final Class<?> type, final Class<? extends Annotation> annotation) {
    Set<String> set_annotated = new HashSet<>();
    Field[] fields = type.getDeclaredFields();

    for(Field f : fields) {
      if(f.isAnnotationPresent(annotation)) {
        set_annotated.add(f.getName());
      }
    }

    return set_annotated;
  }
    //TODO usuń zawartość tej metody i umieść tutaj swoje rozwiązanie


  /**
   * Metoda powinna wyszukać wszystkie zadeklarowane bezpośrednio w klasie metody oraz te
   * implementowane przez nią pochodzące z interfejsów, które implementuje. Wynik powinien zawierać
   * tylko unikalne nazwy metod (bez powtórzeń).
   *
   * @param type klasa (typ) poddawany analizie
   * @return lista zawierająca tylko unikalne nazwy metod zadeklarowanych przez klasę oraz te
   * implementowane
   */
  public static Collection<String> getAllDeclaredMethods(final Class<?> type) {
    Set<String> declared_methods = new HashSet<>();
    Method[] methods = type.getDeclaredMethods();

    for (Method m : methods) {
      declared_methods.add(m.getName());
    }

    Class<?>[] interfaces = type.getInterfaces();
    for (Class<?> inter_face : interfaces) {
      Method[] interfaceMethods = inter_face.getDeclaredMethods();

      for (Method inter_face_method : interfaceMethods) {
        declared_methods.add(inter_face_method.getName());
      }
    }

    return declared_methods;
  }

  /**
   * Metoda powinna odszukać konstruktor zadeklarowany w podanej klasie który przyjmuje wszystkie
   * podane parametry wejściowe. Należy tak przygotować implementację aby nawet w przypadku gdy
   * pasujący konstruktor jest prywatny udało się poprawnie utworzyć nową instancję obiektu
   * <p>
   * Przykładowe użycia:
   * <code>ClassInspector.createInstance(Villager.class)</code>
   * <code>ClassInspector.createInstance(Villager.class, "Nazwa", "Opis")</code>
   *
   * @param type klasa (typ) którego instancje ma zostać utworzona
   * @param args parametry które mają zostać przekazane do konstruktora
   * @return nowa instancja klasy podanej jako parametr zainicjalizowana podanymi parametrami
   * @throws Exception wyjątek spowodowany nie znalezieniem odpowiedniego konstruktora
   */
  public static <T> T createInstance(final Class<T> type, final Object... args) throws Exception {
    Constructor<T>[] constr = (Constructor<T>[]) type.getDeclaredConstructors();

    for (Constructor<T> constructor : constr) {
      if (constructor.getParameterCount() == args.length) {

        boolean instance = true;
        for (int i = 0; i < args.length; i++) {
          if (!constructor.getParameterTypes()[i].isInstance(args[i])) {
            instance = false;
            break;
          }
        }

        if (instance) {
          constructor.setAccessible(true);

          return constructor.newInstance(args);
        }
      }
    }

    throw new Exception("Can't find matching constructor!");
  }
}
