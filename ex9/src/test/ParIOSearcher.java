package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParIOSearcher implements FileSearcher {

    private ExecutorService executorService;
    public ParIOSearcher() {
        executorService = Executors.newCachedThreadPool();
    }
    @Override
    public boolean search(String word, String... fileNames) {

        AtomicBoolean found = new AtomicBoolean(false);
        // Create new Thread for any file search
        for (String fileName : fileNames) {
            executorService.submit(() -> {
                if (found.get() == false) {
                    IOSearcher ioSearcher = new IOSearcher();
                    boolean result = ioSearcher.search(word, fileName);
                    // If found set to found and stop;
                    if (result == true) {
                        found.set(true);
                        stop();
                    }
                }
            });
        }
        return found.get();
    }

    @Override
    public void stop() {
        executorService.shutdownNow();
    }

}
