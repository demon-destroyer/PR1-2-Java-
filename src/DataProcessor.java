@FunctionalInterface
public interface DataProcessor<T, R> {
    R process(T input);

    // Метод за замовчуванням: послідовне об'єднання процесорів
    default <V> DataProcessor<T, V> andThen(DataProcessor<? super R, ? extends V> after) {
        return input -> after.process(this.process(input));
    }

    // Статичний метод: повертає процесор, що не змінює дані
    static <T> DataProcessor<T, T> identity() {
        return input -> input;
    }
}
