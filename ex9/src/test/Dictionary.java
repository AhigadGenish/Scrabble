package test;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dictionary {

    private CacheManager existsWordsCacheManager;
    private CacheManager noneExistsWordsCacheManager;
    private BloomFilter bloomFilter;
    private ParIOSearcher proxyIOSearcher;
    private final int LRUCacheSize = 400;
    private final int LFUCacheSize = 100;
    private final int bloomFilterSize = 256;
    private String[] fileNames;
    public Dictionary(String... fileNames) {
        this.existsWordsCacheManager = new CacheManager(LRUCacheSize, new LRU());
        this.noneExistsWordsCacheManager = new CacheManager(LFUCacheSize, new LFU());
        this.bloomFilter = new BloomFilter(bloomFilterSize, "MD5","SHA1");
        this.proxyIOSearcher = new ParIOSearcher();
        this.insertFileWordsIntoBloomFilter(fileNames);
        this.fileNames = fileNames;

    }

    private void insertFileWordsIntoBloomFilter(String... fileNames) {

        for(String fileName : fileNames){
            List<String> allWordsInFile = getWordsInFile(fileName);
            this.insertWordsIntoBloomfilter(allWordsInFile);
        }

    }

    private void insertWordsIntoBloomfilter(List<String> allWordsInFile) {

        for(String word : allWordsInFile)
            this.bloomFilter.add(word);
    }

    public List<String> getWordsInFile(String fileName){
        List<String> wordList = new ArrayList<>();
        try {
            // Open the file using FileReader
            FileReader fileReader = new FileReader(fileName);
            // Wrap the FileReader in a BufferedReader for efficient reading
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Read each line from the file
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                List<String> currentLineWords = Arrays.asList(line.split("\\s+"));
                wordList.addAll(currentLineWords);
            }

            // Close the BufferedReader
            bufferedReader.close();
            fileReader.close();

        } catch (IOException e) {
            // Handle any IO exceptions
            e.printStackTrace();
        }
        return wordList;
    }
    public boolean query(String word) {
        if(this.existsWordsCacheManager.query(word) == true)
            return true;
        if(this.noneExistsWordsCacheManager.query(word) == true)
            return false;
        if(this.bloomFilter.contains(word) == true){
            this.existsWordsCacheManager.add(word);
            return true;
        }
        else{
            this.noneExistsWordsCacheManager.add(word);
            return false;
        }
    }

    public boolean challenge(String word){


        boolean answer =  this.proxyIOSearcher.search(word, this.fileNames);
        if(answer == false)
            this.noneExistsWordsCacheManager.add(word);
        else
            this.existsWordsCacheManager.add(word);
        return answer;
    }
    
    public void close(){
        this.proxyIOSearcher.stop();
    }
}
