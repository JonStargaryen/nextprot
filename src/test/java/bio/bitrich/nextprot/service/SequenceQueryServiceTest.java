package bio.bitrich.nextprot.service;

import bio.bitrich.nextprot.model.Sequence;
import org.junit.Assert;
import org.junit.Test;

import java.io.UncheckedIOException;
import java.util.stream.Stream;

public class SequenceQueryServiceTest {
    /**
     * Check for validity of method signature. This particular method should throw {@link UncheckedIOException} when ran
     * into trouble.
     */
    @Test(expected = UncheckedIOException.class)
    public void shouldFailForWRONG_ID() {
        String wrongId = "WRONG_ID";
        SequenceQueryService.fetchFastaSequence(wrongId);
    }

    /**
     * Check if the output for the given task is recreated.
     */
    @Test
    public void shouldFetchNX_P01308() {
        String insulinId = "NX_P01308";
        Sequence sequence = SequenceQueryService.fetchFastaSequence(insulinId);

        Assert.assertEquals("fetched sequence does not match expectation",
                "MALWMRLLPLLALLALWGPDPAAAFVNQHLCGSHLVEALYLVCGERGFFYTPKTRREAEDL" +
                        "QVGQVELGGGPGAGSLQPLALEGSLQKRGIVEQCCTSICSLYQLENYCN",
                sequence.getSequence());

        Assert.assertEquals("occurrence string does not match expected format or absolute frequencies",
                "L = 20, G = 12, A = 10",
                sequence.getInformationOnMostFrequentAminoAcids());
    }

    /**
     * Check if all referenced identifiers can be processed. {@link Sequence#getSequence()} cannot be null if the query
     * was processed successfully.
     */
    @Test
    public void shouldFetchSequenceForAllIds() {
        Stream.of("NX_P01308", "NX_Q96IX5", "NX_O95714")
                .map(SequenceQueryService::fetchFastaSequence)
                .map(Sequence::getSequence)
                .forEach(Assert::assertNotNull);
    }
}
