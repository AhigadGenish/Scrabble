package test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.*;

/// ParIOSearcher Class
/// Scrabble exercise 10
/// Student Name: Ahigad Genish
/// ID : 31628022
public class ParIOSearcher implements FileSearcher {

    // Data Members
    private ExecutorService executorService;
    private CompletionService<Boolean> completionService;
    private volatile boolean stopSearch;
    private List<IOSearcher> ioSearchers;

    // Constructor
    public ParIOSearcher() {
        this.executorService = Executors.newCachedThreadPool();
        this.completionService = new ExecutorCompletionService<>(executorService);
        this.ioSearchers = new ArrayList<>();
    }

    // Methods
    // Search method return true if a word exist in the given file paths
    @Override
    public boolean search(String word, String... fileNames) {
        // Create a CompletableFuture to represent the asynchronous result
        CompletableFuture<Boolean> futureResult = new CompletableFuture<>();

        new Thread(() -> {

            // Create list of tasks
            List<Callable<Boolean>> tasks = createTasks(word, fileNames);
            // Submit tasks into the completion service
            for (Callable<Boolean> task : tasks) {
                completionService.submit(task);
            }

            int numTasks = tasks.size();
            int completedTasks = 0;

            // While there are still tasks to get their answers
            while (stopSearch == false && completedTasks < numTasks) {
                try {
                    // Get a completion task
                    Future<Boolean> result = completionService.take();
                    if (result != null && result.isDone() == true) { // Check this task done
                        completedTasks += 1; // Increment completion tasks
                        if (result.get() == true) { // Found
                            this.stop();
                            futureResult.complete(true);
                            return;
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            futureResult.complete(false);
        }).start();

        try {
            return futureResult.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    // Stop method
    @Override
    public void stop() {
        // Set the flag to stop the search
        this.stopSearch = true;
        // Shut down all services
        this.executorService.shutdownNow();
        // Stop each IOSearcher instance
        for (IOSearcher ioSearcher : ioSearchers) {
            ioSearcher.stop();
        }
    }

    // Finalize method
    @SuppressWarnings("deprecation")
    @Override
    protected void finalize() throws Throwable {
        try {
            // Perform cleanup operations here
            this.executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Ensure super.finalize() is called
            super.finalize();
        }
    }

    // Create list of tasks method
    private List<Callable<Boolean>> createTasks(String word, String... fileNames) {
        // Return list of tasks that in given word create a callable which return true if searcher find word in a file
        return Arrays.asList(fileNames).stream()
                .map(fileName -> (Callable<Boolean>) () -> {
                    IOSearcher ioSearcher = new IOSearcher();
                    ioSearchers.add(ioSearcher); // Add to the list
                    return ioSearcher.search(word, fileName);
                })
                .collect(Collectors.toList());
    }

}
