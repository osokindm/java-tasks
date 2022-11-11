package ru.mail.polis.homework.io.objects;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.assertEquals;

public class SerializerTest {

    private static final int NUMBER_OF_OBJECTS = 500000;
    private static final Path PATH = Path.of("src", "test", "resources", "objects");
    private final static String fileName = "src/test/resources/objects/serializeTest.bin";
    private static final Serializer serializer = new Serializer();


    @Before
    public void setUp() throws IOException {
        Files.createDirectories(PATH);
        Files.createFile(Path.of(fileName));
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.forceDelete(PATH.toFile());
    }

    @Test
    public void defaultSerialize() throws IOException {
        List<Animal> animals = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_OBJECTS; i++) {
            animals.add(generateAnimal());
        }

        long startOut = System.currentTimeMillis();
        serializer.defaultSerialize(animals, fileName);
        long finishOut = System.currentTimeMillis();

        long startIn = System.currentTimeMillis();
        var animalsAfter = serializer.defaultDeserialize(fileName);
        long finishIn = System.currentTimeMillis();

        assertEquals(animals, animalsAfter);
        System.out.println("Default: \n\tSerialization time: \t" + (finishOut - startOut) + " ms\n" +
                "\tDeserialization time: \t" + (finishIn - startIn) + " ms\n" +
                "\tFile size: \t" + Files.size(Path.of(fileName)) + " b\n");
    }

    @Test
    public void serializeWithMethods() throws IOException {
        List<AnimalWithMethods> animals = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_OBJECTS; i++) {
            animals.add(generateAnimalWithMethods());
        }

        long startOut = System.currentTimeMillis();
        serializer.serializeWithMethods(animals, fileName);
        long finishOut = System.currentTimeMillis();

        long startIn = System.currentTimeMillis();
        var animalsAfter = serializer.deserializeWithMethods(fileName);
        long finishIn = System.currentTimeMillis();

        assertEquals(animals, animalsAfter);
        System.out.println("With methods: \n\tSerialization time: \t" + (finishOut - startOut) + " ms\n" +
                "\tDeserialization time: \t" + (finishIn - startIn) + " ms\n" +
                "\tFile size: \t" + Files.size(Path.of(fileName)) + " b\n");
    }

    @Test
    public void serializeWithExternalizable() throws IOException {
        List<AnimalExternalizable> animals = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_OBJECTS; i++) {
            animals.add(generateAnimalExternalizable());
        }

        long startOut = System.currentTimeMillis();
        serializer.serializeWithExternalizable(animals, fileName);
        long finishOut = System.currentTimeMillis();

        long startIn = System.currentTimeMillis();
        var animalsAfter = serializer.deserializeWithExternalizable(fileName);
        long finishIn = System.currentTimeMillis();

        assertEquals(animals, animalsAfter);
        System.out.println("With externalizable: \n\tSerialization time: \t" + (finishOut - startOut) + " ms\n" +
                "\tDeserialization time: \t" + (finishIn - startIn) + " ms\n" +
                "\tFile size: \t" + Files.size(Path.of(fileName)) + " b\n");
    }

    @Test
    public void customSerialize() throws IOException {
        List<Animal> animals = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_OBJECTS; i++) {
            animals.add(generateAnimal());
        }

        long startOut = System.currentTimeMillis();
        serializer.customSerialize(animals, fileName);
        long finishOut = System.currentTimeMillis();

        long startIn = System.currentTimeMillis();
        var animalsAfter = serializer.customDeserialize(fileName);
        long finishIn = System.currentTimeMillis();

        assertEquals(animals, animalsAfter);
        System.out.println("Custom: \n\tSerialization time: \t" + (finishOut - startOut) + " ms\n" +
                "\tDeserialization time: \t" + (finishIn - startIn) + " ms\n" +
                "\tFile size: \t" + Files.size(Path.of(fileName)) + " b\n");
    }

    private Animal generateAnimal() {
        return new Animal(
                generateString(),
                AnimalType.values()[rnd.nextInt(AnimalType.values().length)],
                rnd.nextInt(100),
                rnd.nextDouble() * rnd.nextInt(100),
                new Meal(
                        generateString(),
                        LocalDateTime.now(),
                        rnd.nextInt(10) + 1
                )
        );
    }

    private AnimalWithMethods generateAnimalWithMethods() {
        return new AnimalWithMethods(
                generateString(),
                AnimalType.values()[rnd.nextInt(AnimalType.values().length)],
                rnd.nextInt(100),
                rnd.nextDouble() * rnd.nextInt(100),
                new MealWithMethods(
                        generateString(),
                        LocalDateTime.now(),
                        rnd.nextInt(10) + 1
                )
        );
    }

    private AnimalExternalizable generateAnimalExternalizable() {
        return new AnimalExternalizable(
                generateString(),
                AnimalType.values()[rnd.nextInt(AnimalType.values().length)],
                rnd.nextInt(100),
                rnd.nextDouble() * rnd.nextInt(100),
                new MealExternalizable(
                        generateString(),
                        LocalDateTime.now(),
                        rnd.nextInt(10) + 1
                )
        );
    }

    private static final Random rnd = new Random();

    private static String generateString() {
        char[] chars = new char[rnd.nextInt(18) + 2];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (rnd.nextInt('z' - '0') + '0');
        }
        return new String(chars);
    }
}
