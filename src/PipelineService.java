import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class PipelineService {

    public static <T, R> void runPipeline(List<T> data,
                                          Predicate<T> filter,
                                          Function<T, R> mapper,
                                          Consumer<R> aggregator) {
        data.stream()
                .filter(filter)
                .map(mapper)
                .forEach(aggregator);
    }

    public static int mathAndThen(int x) {
        Function<Integer, Integer> addThree = n -> n + 3;
        Function<Integer, Integer> multiplyByTwo = n -> n * 2;
        return addThree.andThen(multiplyByTwo).apply(x);
    }

    public static int mathCompose(int x) {
        Function<Integer, Integer> addThree = n -> n + 3;
        Function<Integer, Integer> multiplyByTwo = n -> n * 2;
        return addThree.compose(multiplyByTwo).apply(x);
    }

    public static String stringAndThen(String s) {
        Function<String, String> takeFirst3 = str -> str.substring(0, Math.min(3, str.length()));
        Function<String, String> addPrefix = str -> "ID_" + str;
        return takeFirst3.andThen(addPrefix).apply(s);
    }

    public static String stringCompose(String s) {
        Function<String, String> takeFirst3 = str -> str.substring(0, Math.min(3, str.length()));
        Function<String, String> addPrefix = str -> "ID_" + str;
        return takeFirst3.compose(addPrefix).apply(s);
    }
}


