package bio.bitrich.nextprot.service;

import bio.bitrich.nextprot.model.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.stream.Stream;

/**
 * A "service" to fetch sequences from the neXtProt API.
 */
public class SequenceQueryService {
    private static final Logger logger = LoggerFactory.getLogger(SequenceQueryService.class);
    private static final String BASE_URL = "https://api.nextprot.org/entry/%s/isoform.json";

    /**
     * Fetches the sequence of a protein from the neXtProt API.
     * @param neXtProtId the identifier to query
     * @return a {@link Sequence} object describing the response
     * @throws UncheckedIOException thrown when API cannot be reached or the format of the response is unexpected
     */
    public static Sequence fetchFastaSequence(String neXtProtId) {
        logger.debug("querying neXtProt API for sequence for protein {}",
                neXtProtId);
        // cut id into BASE_URL
        String query = String.format(BASE_URL, neXtProtId);
        try {
            URL url = new URL(query);

            // use try-with-resource to actually close InputStream when using BufferedReader#lines
            try (InputStream inputStream = url.openStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                 Stream<String> lines = bufferedReader.lines()) {
                String sequence = lines.map(String::trim)
                        // filter for sequence information in JSON response
                        .filter(line -> line.startsWith("\"sequence\""))
                        // extract actual information from line
                        .map(line -> line.split("\"")[3])
                        .findFirst()
                        .orElseThrow(() -> new IOException("neXtProt response did not contain sequence"));
                logger.debug("sequence for {}: {}",
                        neXtProtId,
                        sequence);

                return new Sequence(neXtProtId,
                        sequence);
            }
        } catch (IOException e) {
            // wrap potential exception into unchecked exception
            throw new UncheckedIOException("could not fetch " + neXtProtId + " from neXtProt API",
                    e);
        }
    }
}
