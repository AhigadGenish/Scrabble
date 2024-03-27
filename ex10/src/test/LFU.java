package test;


import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/// LFU Class
/// Scrabble exercise 10
/// Student Name: Ahigad Genish
/// ID : 31628022
public class LFU implements CacheReplacementPolicy{

    // Helper class
    private class LFUElement {

        // Data Members
        public String key;
        public int frequency;
        public long insertionTime;

        // Constructor
        public LFUElement(String key, int frequency, long insertionTime) {
            this.key = key;
            this.frequency = frequency;
            this.insertionTime = insertionTime;
        }
    }

    // Data Members
    private final PriorityQueue<LFUElement> priorityLfu;
    private final HashMap<String, LFUElement> frequesntHashMap;

    // Constructor
    public LFU() {
        this.priorityLfu = new PriorityQueue<>(Comparator
                .comparingInt((LFUElement o) -> o.frequency)
                .thenComparingLong(o -> o.insertionTime));
        this.frequesntHashMap = new HashMap<>();
    }

    // Methods
    @Override
    public void add(String word) {

        LFUElement element = this.frequesntHashMap.get(word);
        // If not exists
        if(element == null){
            element = new LFUElement(word, 1,  System.nanoTime());
            this.frequesntHashMap.put(word, element);
        }
        else{
            priorityLfu.remove(element);
            element.frequency += 1;

        }
        this.priorityLfu.offer(element);
    }

    @Override
    public String remove() {

        if (priorityLfu.isEmpty() == false) {

            return priorityLfu.poll().key;
        }
        return "";
    }
}
