package bio.bitrich.nextprot.model;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

/**
 * Represents a sequence with an identifier and actual sequence data.
 */
public class Sequence {
    private final String id;
    private final String sequence;

    public Sequence(String id, String sequence) {
        this.id = id;
        this.sequence = sequence;
    }

    public String getId() {
        return id;
    }

    public String getSequence() {
        return sequence;
    }

    /**
     * Transform sequence to an occurrence map of the individual amino acids.
     * @return a Map with amino acid one-letter-codes as key and their absolute occurrence as value
     */
    private Map<Character, Long> getOccurenceMap() {
        // create map of the absolute frequency of amino acids in this sequence
        return sequence.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * Report the 3 most frequent amino acids in the desired format.
     * @return a String of the 3 most frequent amino acids and their occurrence
     */
    public String getInformationOnMostFrequentAminoAcids() {
        return getOccurenceMap().entrySet()
                .stream()
                // sort map by absolute frequency in descending order
                // perks of the Java compiler right here ;)
                .sorted(Comparator.comparingLong((ToLongFunction<Map.Entry<Character, Long>>) Map.Entry::getValue).reversed())
                // output should encompass the 3 most frequent amino acids
                .limit(3)
                .map(entry -> entry.getKey() + " = " + entry.getValue())
                .collect(Collectors.joining(", "));
    }
}
