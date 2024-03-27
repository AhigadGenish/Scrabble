package test;


import java.util.HashMap;

/// CacheManager Class
/// Scrabble exercise 10
/// Student Name: Ahigad Genish
/// ID : 31628022
public class CacheManager {

    // Data Members
    private HashMap<String, Boolean> cache;
    private int maximumSize;
    private CacheReplacementPolicy cacheReplacementPolicy;

	// Constructor
    public CacheManager(int newCacheSize, CacheReplacementPolicy newCacheReplacementPolicy){
        this.cache = new HashMap<>();
        this.maximumSize = newCacheSize;
        this.cacheReplacementPolicy = newCacheReplacementPolicy;
    }

    // Methods

    // Query method, return if a given word exist in the cache
    public boolean query(String anyString){
        return this.cache.containsKey(anyString);
    }

    // Add method, add given word to the cache, handle the case of when exceed maximum cache size
    public void add(String newString){

        this.cacheReplacementPolicy.add(newString);
        this.cache.put(newString, true);

        // If exceed size, handle it.
        if(this.cache.size() > this.maximumSize) {
            String removeWord = this.cacheReplacementPolicy.remove();
            this.cache.remove(removeWord);
        }
    }
}
