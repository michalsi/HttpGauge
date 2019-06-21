import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class CsvHandler {

    private FileWriter fileWriter;
    private PrintWriter printWriter;

    CsvHandler(String filePath) throws IOException {
        fileWriter = new FileWriter(filePath);
        printWriter = new PrintWriter(fileWriter);
        printWriter.printf("%s,%s,%s \n", "TIMESTAMP","RESPONSE_TIME", "STATUS_CODE");

    }

    void writeResult(ResultObject r) {
        printWriter.printf("%s,%s,%s", r.getTimestamp(), r.getResponseTime(), r.getStatusCode());
    }

    void close() throws IOException {
        fileWriter.close();
    }


}
