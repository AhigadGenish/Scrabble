package test;


import java.util.*;

/// LRU Class
/// Scrabble exercise 10
/// Student Name: Ahigad Genish
/// ID : 31628022
public class LRU implements CacheReplacementPolicy{

    // Data members
    private Queue<String> lruQueue;

    // Constructor
    public LRU(){
        this.lruQueue = new ArrayDeque<>();
    }

    // Methods

    // Add method
    @Override
    public void add(String word) {
        if(this.lruQueue.contains(word) == true)
            this.lruQueue.remove(word);
        this.lruQueue.add(word);
    }

    // Remove method
    @Override
    public String remove() {
        return this.lruQueue.remove();
    }
}
