package test;


import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class LFU implements CacheReplacementPolicy{

    private class LFUElement {
        String key;
        int frequency;
        long insertionTime;

        public LFUElement(String key, int frequency, long insertionTime) {
            this.key = key;
            this.frequency = frequency;
            this.insertionTime = insertionTime;
        }
    }


    private final PriorityQueue<LFUElement> priorityLfu;
    private final HashMap<String, LFUElement> frequesntHashMap;

    public LFU() {
        this.priorityLfu = new PriorityQueue<>(Comparator
                .comparingInt((LFUElement o) -> o.frequency)
                .thenComparingLong(o -> o.insertionTime));
        this.frequesntHashMap = new HashMap<>();
    }


    @Override
    public void add(String word) {
        LFUElement element = this.frequesntHashMap.get(word);
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
