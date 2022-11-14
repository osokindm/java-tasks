package ru.mail.polis.homework.reflection;


import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Необходимо реализовать метод reflectiveToString, который для произвольного объекта
 * возвращает его строковое описание в формате:
 * <p>
 * {field_1: value_1, field_2: value_2, ..., field_n: value_n}
 * <p>
 * где field_i - имя поля
 * value_i - его строковое представление (String.valueOf),
 * за исключением массивов, для которых value формируется как:
 * [element_1, element_2, ..., element_m]
 * где element_i - строковое представление элемента (String.valueOf)
 * элементы должны идти в том же порядке, что и в массиве.
 * <p>
 * Все null'ы следует представлять строкой "null".
 * <p>
 * Порядок полей
 * Сначала следует перечислить в алфавитном порядке поля, объявленные непосредственно в классе объекта,
 * потом в алфавитном порядке поля объявленные в родительском классе и так далее по иерархии наследования.
 * Примеры можно посмотреть в тестах.
 * <p>
 * Какие поля выводить
 * Необходимо включать только нестатические поля. Также нужно пропускать поля, помеченные аннотацией @SkipField
 * <p>
 * Упрощения
 * Чтобы не усложнять задание, предполагаем, что нет циклических ссылок, inner классов, и transient полей
 * <p>
 * Реализация
 * В пакете ru.mail.polis.homework.reflection можно редактировать только этот файл
 * или добавлять новые (не рекомендуется, т.к. решение вполне умещается тут в несколько методов).
 * Редактировать остальные файлы нельзя.
 * <p>
 * Баллы
 * В задании 3 уровня сложности, для каждого свой набор тестов:
 * Easy - простой класс, нет наследования, массивов, статических полей, аннотации SkipField (4 балла)
 * Easy + Medium - нет наследования, массивов, но есть статические поля и поля с аннотацией SkipField (6 баллов)
 * Easy + Medium + Hard - нужно реализовать все требования задания (10 баллов)
 * <p>
 * Итого, по заданию можно набрать 10 баллов
 * Баллы могут снижаться за неэффективный или неаккуратный код
 */
public class ReflectionToStringHelper {

    public static String reflectiveToString(Object object) {
        if (object == null) {
            return "null";
        }

        Class easyClass = object.getClass();
        Field[] fields = easyClass.getDeclaredFields();
        if (fields.length == 0) {
            return "{}";
        }

        Arrays.sort(fields, Comparator.comparing(Field::getName));
        StringBuilder result = new StringBuilder("{");

        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) || field.isAnnotationPresent(SkipField.class)) {
                continue;
            }
            field.setAccessible(true);
            result.append(field.getName());
            result.append(": ");
            try {
                if (field.get(object) != null && field.getType().isArray()) {
//                    if (Array.getLength(field.get(object)) > 0) {
//                        Object[] copy = Arrays.copyOf((Object[]) field.getType().cast(field.get(object)), Array.getLength(field.get(object)));
//                        System.out.println("        TIST " + Arrays.toString(copy));
//
//                        result.append(Arrays.toString(copy));
//                    }
//                    //result.append(Array.get(field.get(object), ));
                    int length = Array.getLength(field.get(object));
                    result.append("[");
                    for (int i = 0; i < length; i++) {
                        result.append(", ").append(Array.get(field.get(object), i));
                    }
                    result.delete(result.length() - 2, result.length()).append("]");
                } else {
                    result.append(field.get(object)).append(", ");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (easyClass.getSuperclass() != Object.class) {
            result.append(reflectiveToString(easyClass.getSuperclass()));
        }

        result.delete(result.length() - 2, result.length()).append("}");
        return result.toString();
    }

    public static void main(String[] args) {
        int[] array = {6, 7645, 63, 25, 6};
        System.out.println(Arrays.toString(array));


    }
}
