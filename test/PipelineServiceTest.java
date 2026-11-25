import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class PipelineServiceTest {

    @Test
    void testPipelineNormalData() {
        AtomicInteger sum = new AtomicInteger(0);
        PipelineService.runPipeline(List.of(1, 2, 3, 4), n -> n % 2 == 0, n -> n * 10, sum::addAndGet);
        assertEquals(60, sum.get());
    }

    @Test
    void testPipelineEmptyList() {
        AtomicInteger sum = new AtomicInteger(0);
        PipelineService.runPipeline(List.of(), (Integer n) -> n > 0, n -> n * 2, sum::addAndGet);
        assertEquals(0, sum.get());
    }

    @Test
    void testPipelineNoMatches() {
        AtomicInteger sum = new AtomicInteger(0);
        PipelineService.runPipeline(List.of(1, 3, 5), n -> n % 2 == 0, n -> n * 10, sum::addAndGet);
        assertEquals(0, sum.get());
    }

    @Test
    void testMathCompositionOrder() {
        assertEquals(16, PipelineService.mathAndThen(5));
        assertEquals(13, PipelineService.mathCompose(5));
        assertNotEquals(PipelineService.mathAndThen(5), PipelineService.mathCompose(5));
    }

    @Test
    void testStringCompositionOrder() {
        assertEquals("ID_jav", PipelineService.stringAndThen("java"));
        assertEquals("ID_", PipelineService.stringCompose("java"));
    }

    @Test
    void testCustomFunctionalInterface() {
        DataProcessor<String, Integer> length = String::length;
        DataProcessor<Integer, Integer> doubleIt = n -> n * 2;
        assertEquals(8, length.andThen(doubleIt).process("Java"));
        assertEquals("Test", DataProcessor.identity().process("Test"));
    }
}
