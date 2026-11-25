import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        // --- Пункт 2: Лямбда та посилання на методи ---
        DataProcessor<Integer, Integer> squareLambda = x -> x * x;           // Лямбда-вираз
        DataProcessor<String, Integer> parseStatic = Integer::parseInt;      // Статичне посилання
        String prefix = "Result: ";
        DataProcessor<String, String> concatInstance = prefix::concat;       // Екземплярне посилання

        // Використання default-методу (andThen) та static-методу (identity)
        DataProcessor<String, Integer> parseAndSquare = parseStatic.andThen(squareLambda);
        System.out.println(concatInstance.process(String.valueOf(parseAndSquare.process("5")))); // Result: 25
        System.out.println("Identity: " + DataProcessor.identity().process("Java"));

        // --- Пункт 3: Конвеєр (Predicate -> Function -> Consumer) ---
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        AtomicInteger sum = new AtomicInteger(0);
        PipelineService.runPipeline(
                numbers,
                n -> n % 2 == 0,      // Predicate: тільки парні
                n -> n * 10,          // Function: помножити на 10
                sum::addAndGet        // Consumer: агрегування (сума)
        );
        System.out.println("Сума після конвеєра: " + sum.get()); // (2+4+6)*10 = 120

        // --- Пункт 4: Композиція функцій ---
        System.out.println("Math andThen(5): " + PipelineService.mathAndThen(5));       // (5+3)*2 = 16
        System.out.println("Math compose(5): " + PipelineService.mathCompose(5));       // (5*2)+3 = 13
        System.out.println("String andThen('java'): " + PipelineService.stringAndThen("java")); // ID_jav
        System.out.println("String compose('java'): " + PipelineService.stringCompose("java")); // ID_

        // --- Пункт 6: Порівняння продуктивності (10 000 елементів) ---
        int[] data = IntStream.rangeClosed(1, 10_000).toArray();

        long startLoop = System.nanoTime();
        long sumLoop = 0;
        for (int n : data) {
            if (n % 2 == 0) sumLoop += (long) n * 2;
        }
        long timeLoop = System.nanoTime() - startLoop;

        long startStream = System.nanoTime();
        long sumStream = Arrays.stream(data)
                .filter(n -> n % 2 == 0)
                .mapToLong(n -> (long) n * 2)
                .sum();
        long timeStream = System.nanoTime() - startStream;

        System.out.printf("For-loop:   sum=%d, time=%.3f ms%n", sumLoop, timeLoop / 1_000_000.0);
        System.out.printf("Stream API: sum=%d, time=%.3f ms%n", sumStream, timeStream / 1_000_000.0);
    }
}
