import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

public class driver {
    public static void main(String[] args) {
        Parser parser = new Parser("/IMDB-Movie-Data.csv");
        //parser.printFile();
        Collection<Object[]> output = parser.transformDocument();
        try {
            Writer writer = new Writer("/TransformedMovies.csv", output);
            writer.write();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
