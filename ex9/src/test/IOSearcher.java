package test;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class IOSearcher implements FileSearcher {

    // Data Members
    private AtomicBoolean stopFlag = new AtomicBoolean(false);

    @Override
    public boolean search(String word, String... fileNames) {

        for (String fileName : fileNames) {
            if (stopFlag.get()) {
                return false;
            }
            if (searchInFile(fileName, word) == true) {
                return true;
            }
        }
        return false;
    }

    private boolean searchInFile(String fileName, String word){

        try {
            // Open the file using FileReader
            FileReader fileReader = new FileReader(fileName);
            // Wrap the FileReader in a BufferedReader for efficient reading
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Read each line from the file
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                List<String> wordList = Arrays.asList(line.split("\\s+"));
                if(wordList.contains(word) == true){
                    return true;
                }
            }

            // Close the BufferedReader
            bufferedReader.close();
            fileReader.close();

        } catch (IOException e) {
            // Handle any IO exceptions
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public void stop() {
        stopFlag.set(true);
    }
}
