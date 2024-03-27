package test;


import java.io.*;

import java.util.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/// IOSearcher Class
/// Scrabble exercise 10
/// Student Name: Ahigad Genish
/// ID : 31628022
public class IOSearcher implements FileSearcher {

    // Data Members
    private AtomicBoolean stopFlag = new AtomicBoolean(false);

    // Methods
    @Override
    public boolean search(String word, String... fileNames) {

        for (String fileName : fileNames) {
            if (stopFlag.get() == true) {
                return false;
            }
            if (searchInFile(fileName, word) == true) {
                return true;
            }
        }
        return false;
    }

    // Search a word in given file
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
