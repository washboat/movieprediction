import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.File;
import java.io.InputStreamReader;
import java.util.*;

public class Parser {
    File file;
    String fileName;
    InputStreamReader reader;
    List<String[]> document;
    Collection<Object[]>  output;

    HashMap<String, Integer> genraMap;
    List<String> genreList;
    int genreCount;

    HashMap<String, Integer> actorMap;
    List<String> actorList;
    int actorCount;

    HashMap<String, Integer> directorMap;
    List<String> directorList;
    int directorCount;

    int maxActorPerRow;
    int maxGenrePerRow;
    int maxDirectorPerRow;


    public Parser(String fileName){
        this.fileName = fileName;
        reader = openFile(this.fileName);
        walkThroughFile();
        createMappings();
    }
    private void walkThroughFile(){
        if (reader == null)
            throw new IllegalStateException("No file has been opened!");
        CsvParserSettings settings = new CsvParserSettings();
        //settings.getFormat().setDelimiter("\n");
        settings.getFormat().setLineSeparator("\n");
        CsvParser parser = new CsvParser(settings);
        document = parser.parseAll(reader);
        System.out.println(document.get(0));
        document.remove(0);
    }
    public void printFile(){
        for (String[] strings : document) {
            for (String s : strings) {
                System.out.print( "[ "+s+" ]");
            }
            System.out.println("\n");
        }
        System.out.println(directorMap);
    }
    public Collection<Object[]> transformDocument(){
        Object[] transformedRow;
        output = new ArrayList<Object[]>();
        List<Integer> mappedGenres;
        List<Integer> mappedActors;
        for (String[] row : document) {
            String[] genres = row[2].split(",");
            String[] actors = row[5].split(",");
            mappedGenres = new ArrayList<Integer>(Arrays.asList(mapGenres(genres, genraMap)));
            mappedActors = new ArrayList<Integer>(Arrays.asList(mapActors(actors, actorMap)));
            transformedRow = new Object[row.length + maxGenrePerRow + maxActorPerRow];
            transformedRow[0] = row[0];
            transformedRow[1] = mappedGenres.get(0);
            transformedRow[2] = mappedGenres.get(1);
            transformedRow[3] = mappedGenres.get(2);
            transformedRow[4] = directorMap.get(row[4]);
            transformedRow[5] = mappedActors.get(0);
            transformedRow[6] = mappedActors.get(1);
            transformedRow[7] = mappedActors.get(2);
            transformedRow[8] = mappedActors.get(3);
            transformedRow[9] = row[6];
            transformedRow[10] = mapRuntime(row[7]);//runtime
            transformedRow[11] = row[8];
            transformedRow[12] = row[9];
            assert mapRevenue(row[10]) != null;
            transformedRow[13] = (mapRevenue(row[10]) == null) ? 999 : mapRevenue(row[10]);
            //transformedRow[13] = (row[10] == null) ? "9999" : row[10];
            transformedRow[14] = (row[11] == null) ? "9999" : row[11];
            output.add(transformedRow);
        }
        return output;
    }
    private String mapRuntime(String runtime){
        String result = null;
        int runtimeNum = Integer.parseInt(runtime);
        if (runtimeNum < 80)
            result = "1";
        else if (runtimeNum >= 80 & runtimeNum < 100)
            result = "2";
        else if (runtimeNum >= 100 & runtimeNum < 120)
            result = "3";
        else if (runtimeNum >= 120 & runtimeNum < 140)
            result = "4";
        else if (runtimeNum >= 140 & runtimeNum < 160)
            result = "5";
        else if (runtimeNum >= 160 & runtimeNum < 180)
            result = "6";
        else if (runtimeNum >= 180 & runtimeNum < 200)
            result = "7";
        return result;
    }
    private String mapRevenue(String revenue){
        String result = null;
        try {
            Double revenueNum = Double.parseDouble(revenue);

        if(revenueNum < 50.0)
            result = "1";
        else if (revenueNum >= 50.0 & revenueNum < 100.0)
            result = "2";
        else if (revenueNum >= 100.0 & revenueNum < 150.0)
            result = "3";
        else if (revenueNum >= 150.0 & revenueNum < 200.0)
            result = "4";
        else if (revenueNum >= 200.0 & revenueNum < 250.0)
            result = "5";
        else if (revenueNum >= 250.0 & revenueNum < 300.0)
            result = "6";
        else if (revenueNum >= 300.0)
            result = "7";
        else
            return null;
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
        return result;
    }
    private Integer[] mapGenres(String[] toMap, HashMap<String, Integer> map ){
        Integer[] result = new Integer[3];
        return performMap(toMap, map, result);
    }
    private Integer[] mapActors(String[] toMap, HashMap<String, Integer> map ){
        Integer[] result = new Integer[4];
        return performMap(toMap, map, result);
    }
    private Integer[] performMap(String[] toMap, HashMap<String, Integer> map, Integer[] result){
        Arrays.fill(result, 9999);
        for(int i = 0; i < toMap.length; i++){
            result[i] = map.get(toMap[i]);
        }
        return result;
    }

    private void createMappings(){
        genraMap = new HashMap<String, Integer>();
        genreList = new ArrayList<String>();
        genreCount = 0;
        maxGenrePerRow = 0;

        actorMap = new HashMap<String, Integer>();
        actorList = new ArrayList<String>();
        actorCount = 0;
        maxActorPerRow = 0;

        directorMap = new HashMap<String, Integer>();
        directorList = new ArrayList<String>();
        directorCount = 0;
        maxDirectorPerRow = 0;

        String[] genres;
        String[] actors;
        String[] directors;
        for (String[] row : document) {
            genres = row[2].split(",");
            actors = row[5].split(",");
            directors = row[4].split(",");
            genreCount = buildMap(genres, genreList, genraMap, genreCount);
            actorCount = buildMap(actors, actorList, actorMap, actorCount);
            directorCount = buildMap(directors, directorList, directorMap, directorCount);
            if (genres.length > maxGenrePerRow)
                maxGenrePerRow = genres.length;
            if (actors.length > maxActorPerRow)
                maxActorPerRow = actors.length;
            if (directors.length > maxDirectorPerRow)
                maxDirectorPerRow = directors.length;
        }
        System.out.print(maxActorPerRow);

    }
    private int buildMap(String[] builtArray, List<String> emptyList, HashMap<String, Integer> map, int counter){
        for (String str : builtArray) {
            if (!emptyList.contains(str)){
                map.put(str, counter);
                emptyList.add(str);
                counter++;
            }
        }
    return counter;
    }
    private InputStreamReader openFile(String fileName){
        return new InputStreamReader(this.getClass().getResourceAsStream(fileName));
    }
}
