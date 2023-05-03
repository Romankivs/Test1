import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class WordDistanceAnalyzerTest {
    static WordDistanceAnalyzer analyzer;

    @BeforeAll
    static void setUp() {
        System.out.println("Starting testing...");

        analyzer = new WordDistanceAnalyzer();
    }

    @Test
    void emptyInput() {
        assertThrows(IllegalArgumentException.class, () -> { analyzer.getPairsWithMaxDistance(""); });
    }

    @Test
    void singleWord() {
        var emptyArrayOfWordPairs = new ArrayList<Pair<String, String>>();
        assertEquals(analyzer.getPairsWithMaxDistance("Word"), emptyArrayOfWordPairs);
    }

    @Test
    void caseSensitive() {
        var array = new ArrayList(Arrays.asList(new Pair("WORD", "words")));
        assertEquals(analyzer.getPairsWithMaxDistance("WORD words WORDS"), array);
    }

    @Test
    void notNull() {
        assertNotNull(analyzer.getPairsWithMaxDistance("a b"));
    }

    @Test
    void simpleWithoutOrder() {
        assertThat(analyzer.getPairsWithMaxDistance("a b c"), containsInAnyOrder(
                new Pair("a", "b"),
                new Pair("a", "c"),
                new Pair("b", "c")
        ));
    }

    @Test
    void simpleWithOrder() {
        assertThat(analyzer.getPairsWithMaxDistance("a b c"), contains(
                new Pair("a", "b"),
                new Pair("a", "c"),
                new Pair("b", "c")
        ));
    }

    @Test
    void delimiters() {
        assertThat(analyzer.getPairsWithMaxDistance("a$b(c^d+e"), hasSize(10));
    }

    @Test
    void simpleCorrectSize() {
        assertThat(analyzer.getPairsWithMaxDistance("a b c"), hasSize(3));
    }

    static Stream<Arguments> parametersProvider() {
        return Stream.of(
            Arguments.arguments("a aa aaa", new ArrayList(Arrays.asList(
                    new Pair("a", "aaa")))),
            Arguments.arguments("a bb abb", new ArrayList(Arrays.asList(
                    new Pair("a", "bb"),
                    new Pair("a", "abb"),
                    new Pair("bb", "abb")))),
            Arguments.arguments("aaa aaa bbb bbb bbb ccc ccc", new ArrayList(Arrays.asList(
                    new Pair("aaa", "bbb"),
                    new Pair("aaa", "ccc"),
                    new Pair("bbb", "ccc")))),
            Arguments.arguments("aa ba ab cc", new ArrayList(Arrays.asList(
                    new Pair("aa", "cc"),
                    new Pair("ba", "ab"),
                    new Pair("ba", "cc"),
                    new Pair("ab", "cc"))))
        );
    }

    @ParameterizedTest
    @MethodSource("parametersProvider")
    void parameterizedTest(String input, ArrayList<Pair<String, String>> expectedRes) {
        assertEquals(analyzer.getPairsWithMaxDistance(input), expectedRes);
    }
}