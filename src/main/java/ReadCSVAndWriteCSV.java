import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ReadCSVAndWriteCSV {
    private static final String CSV_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/tweets.csv";
    private static final String CSV_RESULT_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/tweets_tag.csv";

    public static void main(String[] args) throws IOException {
        List<String[]> myEntries = readAllRecordsAtOnce();
        List<String[]> testedEntries = new ArrayList<>();
        for (String[] row : myEntries) {
            String[] withTestResult = addTagResult(row, getTag(row));
            testedEntries.add(withTestResult);
        }
        writeToFile(testedEntries);
    }

    private static void readRecordsOneByOne() throws IOException { // WE DONT NEED THIS FUNCTION
        try (
                Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            // Reading Records One by One in a String array
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                System.out.println("Tweet : " + nextRecord[0]);
                System.out.println("==========================");
            }
        }
    }

    private static List<String[]> readAllRecordsAtOnce() throws IOException {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            return csvReader.readAll();
        }
    }

    private static String[] addTagResult(String[] row, String result) {
        return (String.join("\t", row) + "\t" + result).split("\t");
    }

    private static String getTag(String[] row) {
        String tweet = row[0];
        List<String> words = Arrays.asList("HIV", "flu");// YOU CAN ADD YOUR WORDS HERE
        Boolean match = words.stream().anyMatch(tweet::contains);
        return match ? "WORRY" : "NOWORRY";
    }

    private static void writeToFile(List<String[]> myEntries) throws IOException {
        try (
                Writer writer = Files.newBufferedWriter(Paths.get(CSV_RESULT_FILE_PATH));
                CSVWriter csVWriter = new CSVWriter(writer,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END)
        ) {
            for (String[] row : myEntries) {
                csVWriter.writeNext(row);
            }
        }
    }
}
