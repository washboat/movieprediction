import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Writer {
    FileWriter fileWriter;
    Collection<Object[]> output;
    public Writer(String locationToWrite, Collection<Object[]> output) throws IOException {
        FileSystemView view = FileSystemView.getFileSystemView();
        String fullPath = view.getHomeDirectory() + locationToWrite;
        fileWriter = new FileWriter(fullPath);
        this.output = output;
    }
    public void write(){
        CsvWriter csvWriter = new CsvWriter(fileWriter, new CsvWriterSettings());
        List<String> headers = new ArrayList<String>();
        headers.add("Movie_ID");
        headers.add("Genre_1");
        headers.add("Genre_2");
        headers.add("Genre_3");
        headers.add("Director");
        headers.add("Actor_1");
        headers.add("Actor_2");
        headers.add("Actor_3");
        headers.add("Actor_4");
        headers.add("Year");
        headers.add("Runtime");
        headers.add("Rating");
        headers.add("Votes");
        headers.add("Revenue");
        headers.add("Metascore");
        csvWriter.writeHeaders(headers);
        csvWriter.writeRowsAndClose(output);
    }
}
