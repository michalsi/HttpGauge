import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class CsvHandler {

    private FileWriter fileWriter;
    private PrintWriter printWriter;
    private String filePath;

    CsvHandler(String filePath) {
        this.filePath = filePath;

        try {
            fileWriter = new FileWriter(filePath);
            printWriter = new PrintWriter(fileWriter);
            printWriter.printf("%s,%s,%s \n", "TIMESTAMP","RESPONSE_TIME", "STATUS_CODE");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void writeResult(ResultObject r) {
        printWriter.printf("%s,%s,%s\n", r.getTimestamp(), r.getResponseTime(), r.getStatusCode());
    }

    void close() {
        try {
            fileWriter.close();
            System.out.println("Results file saved in:" + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
