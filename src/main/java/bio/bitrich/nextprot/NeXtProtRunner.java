package bio.bitrich.nextprot;

import bio.bitrich.nextprot.model.Sequence;
import bio.bitrich.nextprot.service.SequenceQueryService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NeXtProtRunner {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("enter neXtProt id: ");
        String neXtProtId = bufferedReader.readLine()
                // clean-up potential surrounding chars
                .replace("\"", "")
                .replace("'", "")
                .replace("‘", "")
                .replace("’", "");
        Sequence sequence = SequenceQueryService.fetchFastaSequence(neXtProtId);
        System.out.println("output: " + sequence.getInformationOnMostFrequentAminoAcids());
    }
}
